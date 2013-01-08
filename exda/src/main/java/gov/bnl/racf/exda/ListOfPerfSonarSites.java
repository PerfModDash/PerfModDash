package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfPerfSonarSites
{  

    private ParameterBag parameterBag = null;

    private DbConnector db=null;

    private ResultSet rs=null;

    private String cloudName=null;

    private List<PerfSonarSite> list=new ArrayList<PerfSonarSite>();
    public ListOfPerfSonarSites(ParameterBag paramBag,   DbConnector inputDb)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    String query="SELECT * from Sites";
	    PreparedStatement queryPS=null;

	    try{
		queryPS=db.getConn().prepareStatement(query);
		ResultSet rs=queryPS.executeQuery();
		while(rs.next()){
		    int siteId=rs.getInt("siteId");
		    PerfSonarSite perfSonarSite=new PerfSonarSite(this.parameterBag,this.db,siteId);
		    this.list.add(perfSonarSite);
		}
		rs.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when loading list of sites");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured closing prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }		
	}
    public ListOfPerfSonarSites(ParameterBag paramBag,   DbConnector inputDb,String cloudName)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.cloudName=cloudName;
	    this.addByCloudName();
	}
    private void addByCloudName(){

	String getSiteNames="select * from SitesClouds where CloudName=?";
	PreparedStatement initialisationSql=null;
	try{
	    initialisationSql= db.getConn().prepareStatement(getSiteNames);
	    initialisationSql.setString(1, this.cloudName  );
	    rs=initialisationSql.executeQuery();
	    int count = 0;
	    while (rs.next ()){
		String siteName=rs.getString("SiteName");
		PerfSonarSite ps = new PerfSonarSite(parameterBag,db,siteName);
		list.add( ps );
		count=count+1;
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when adding sites by cloudname");
	    System.out.println(getClass().getName()+" "+getSiteNames);
	    System.out.println(getClass().getName()+" "+this.cloudName);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    initialisationSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+getSiteNames);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }

    public boolean listContainsSite(PerfSonarSite site){
	boolean result=false;
	String siteName=site.getSiteName();
	Iterator itr= this.list.iterator();
	while(itr.hasNext()){
	    PerfSonarSite currentSite=(PerfSonarSite)itr.next();
	    if(currentSite.equals(siteName)){
		result=true;
		return result;
	    }
	}
	return result;
    }

    // links
    public HtmlLink  getLinkToCreateSitePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Site")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    public ProbeStatus listStatus(){
	ProbeStatus finalStatus=new ProbeStatus("OK");
	Iterator itr = list.iterator(); 
	while(itr.hasNext()) {
	    PerfSonarSite ps = (PerfSonarSite)itr.next(); 
	    ProbeStatus siteStatus=ps.getSiteStatus();
	    if (siteStatus.statusLevel()>finalStatus.statusLevel()){
		finalStatus=siteStatus;
	    }
	}
	return finalStatus;
    }

    public List<PerfSonarSite> getList(){
	return this.list;
    }
    public String toString(){
	String result="Hello world from list of sites";
	return result;
    }

    public HtmlTable htmlTable(){

	HtmlTable ht=new HtmlTable(10);
	
	Iterator itr = list.iterator(); 
	while(itr.hasNext()) {
	    PerfSonarSite ps = (PerfSonarSite)itr.next(); 
	    ht.addCell(ps.shortStatusCell());
	} 	

	return ht;
    }

    public String toHtml(){
	String result="<h3>List of perfSonar Sites</h3>";
	result=result+htmlTable().toHtml();
	return result;
    }

    public HtmlTableCell shortStatusCell(){

	/// create link to detail page
	//ParameterBag paramBagLocal=(ParameterBag)parameterBag.clone();
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("cloudName",cloudName);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("List of Sites"));

	//paramBagLocal.addParam("page",ParameterBag.pageAddress("Cloud Sites")  );
	//paramBagLocal.addParam("cloudName",cloudName);
	String linkToDetailPage=paramBagLocal.makeLink();

	HtmlLink link=new HtmlLink(linkToDetailPage,cloudName );
	HtmlTableCell cell=new HtmlTableCell(link,listStatus().color());
	return cell;
    }

    public HtmlTable shortHtmlTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(shortStatusCell());
	return ht;	    	    		
    }


    public HtmlTable manipulationHtmlTable(){
	HtmlTable ht=new HtmlTable(3);
	ht.setBorder(1);
	ht.setPadding(0);
	
	Iterator itr = this.list.iterator(); 
	while(itr.hasNext()) {
	    PerfSonarSite perfSonarSite=(PerfSonarSite)itr.next();
	    ht.addRow(perfSonarSite.infoTableRow());
	} 
	return ht;
    }
    public String manipulationHtmlPage(){
	String result="<H1>PerfSonar Sites</H1>";
	result=result+"<br>";
	result=result+this.getLinkToCreateSitePage("<strong>Click here to create a new site</strong>")+"<br>";
	result=result+this.manipulationHtmlTable().toHtml();
	return result;
    }

}
