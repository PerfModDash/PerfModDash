package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfPrimitiveServices
{  
    private ParameterBag parameterBag = null;

    private DbConnector db=null;

    private String listName="";
    private String listNameShort="";

    private List<PrimitiveService> listOfServices=new ArrayList<PrimitiveService>();

    public ListOfPrimitiveServices(ParameterBag paramBag,   DbConnector inputDb)
	{
	    parameterBag=paramBag;
	    db=inputDb;
	}
    public void addByServiceDescription(String serviceDescription){

	listName="Services "+serviceDescription;
	listNameShort=serviceDescription;

	String getServices="select  HostName,MetricName from MetricRecordSummary where ServiceDescription=? ";	
	PreparedStatement initialisationSql=null;

	try{
	    initialisationSql= db.getConn().prepareStatement(getServices);
	    initialisationSql.setString(1, serviceDescription  );
	    ResultSet rs=initialisationSql.executeQuery();
	    int count = 0;
	    while (rs.next ())
		{
		    String hostName=rs.getString("HostName");
		    String serviceName=rs.getString("MetricName");
		    PrimitiveService ps = new PrimitiveService(parameterBag,db,hostName,serviceName);
		    listOfServices.add( ps );
		    count=count+1;
		}
	    rs.close();

	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    initialisationSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+getServices);
	    System.out.println(getClass().getName()+" "+e);
	}
    }
    public void addByServiceName(String serviceName){

	listName="Service "+serviceName;
	listNameShort=serviceName;

	String getServicesByName="select  HostName from MetricRecordSummary where MetricName=? ";
	PreparedStatement initialisationSql=null;    

	try{
	    initialisationSql= db.getConn().prepareStatement(getServicesByName);
	    initialisationSql.setString(1,  serviceName );
	    ResultSet rs=initialisationSql.executeQuery();
	    int count = 0;
	    while (rs.next ())
		{
		    String hostName=rs.getString("HostName");
		    PrimitiveService ps = new PrimitiveService(parameterBag,db,hostName,serviceName);
		    listOfServices.add( ps );
		    count=count+1;
		}
	    rs.close();

	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    initialisationSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error when closiong prepared statement");
	    System.out.println(getClass().getName()+" "+getServicesByName);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }
    public void addByHostName(String hostName){

	listName="Services on host "+hostName;
	listNameShort=hostName;

	String getServices="select * from MetricRecordSummary where HostName=? ";	    
	PreparedStatement initialisationSql= null;
	try{
	    initialisationSql= db.getConn().prepareStatement(getServices);
	    initialisationSql.setString(1,  hostName );
	    ResultSet rs=initialisationSql.executeQuery();
	    int count = 0;
	    while (rs.next ())
		{
		    String serviceName=rs.getString("MetricName");
		    PrimitiveService ps = new PrimitiveService(parameterBag,db,hostName,serviceName);
		    listOfServices.add( ps );
		    count=count+1;
		}
	    rs.close();

	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when making list of primitive services on "+hostName);
	    System.out.println(getClass().getName()+" "+getServices);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    initialisationSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+getServices);
	    System.out.println(getClass().getName()+" "+e);
	} 
    }

    public void addBySiteName(String siteName){

	listName="perfSONAR Services at "+siteName;
	listNameShort=siteName;

	String getServices="select  HostName,MetricName from MetricRecordSummary where SiteName=? ";	    
	PreparedStatement initialisationSql= null;
	try{
	    initialisationSql= db.getConn().prepareStatement(getServices);
	    initialisationSql.setString(1,  siteName);
	    ResultSet rs=initialisationSql.executeQuery();
	    int count = 0;
	    while (rs.next ())
		{
		    String hostName=rs.getString("HostName");
		    String serviceName=rs.getString("MetricName");
		    PrimitiveService ps = new PrimitiveService(parameterBag,db,hostName,serviceName);
		    listOfServices.add( ps );
		    count=count+1;
		}
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    initialisationSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+getServices);	    
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }

    public ProbeStatus listStatus(){
	ProbeStatus finalStatus=new ProbeStatus("OK");

	Iterator itr = listOfServices.iterator(); 
	while(itr.hasNext()) {
	    PrimitiveService primitiveService = (PrimitiveService)itr.next(); 
	    if (finalStatus.statusLevel()<primitiveService.metricStatus.statusLevel()){
		finalStatus=primitiveService.metricStatus;
	    }
	} 	
	return finalStatus;
    }

    public String toString(){
	String result="Hello world from list of primitive services";
	return result;
    }

    public HtmlTable htmlTable(){

	//HtmlTable ht=new HtmlTable(listOfServices.size());
	HtmlTable ht=new HtmlTable(3);
	
	Iterator itr = listOfServices.iterator(); 
	while(itr.hasNext()) {
	    PrimitiveService primitiveService = (PrimitiveService)itr.next(); 
	    ht.addCell(primitiveService.shortStatusCell());
	} 	

	return ht;
    }

    public String toHtml(){
	String result="<h3>"+listName+"</h3>";
	result=result+htmlTable().toHtml();
	return result;
    }

    public HtmlTableCell shortStatusCell(){

	/// create link to detail page
	ParameterBag paramBagLocal=(ParameterBag)parameterBag.clone();
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Site Services")  );
	paramBagLocal.addParam("siteName",listNameShort);
	//paramBagLocal.addParam("metricName",metricName);
	String linkToNodeDetailPage=paramBagLocal.makeLink();

	HtmlLink link=new HtmlLink(linkToNodeDetailPage,listNameShort );
	HtmlTableCell cell=new HtmlTableCell(link,listStatus().color());
	return cell;
    }

    public HtmlTable shortHtmlTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(shortStatusCell());
	return ht;	    	    		
    }

    public  HtmlTable getTableOfServicesOnHost(String hostName){
	HtmlTable ht=new HtmlTable(3);
	Iterator itr = 	listOfServices.iterator();
	while(itr.hasNext()) {	
	    PrimitiveService currentService=(PrimitiveService)itr.next();
	    if (currentService.hostName.equals(hostName)){
		ht.addCell(currentService.shortStatusCell());
	    }
	}
	return ht;
    }

    public String servicesGroupedByHost(){
	// display list of services grouped by host
	String result="<h2>"+listName+"</h2>";

	List<String> listOfHosts=new ArrayList<String>();
	Iterator itr = listOfServices.iterator(); 
	while(itr.hasNext()) {
	    PrimitiveService primitiveService = (PrimitiveService)itr.next(); 
	    String hostName=primitiveService.hostName;
	    if (!listOfHosts.contains(hostName)){
		listOfHosts.add(hostName);
	    }
	} 
	// we have list of hosts, now display table of services on this host
	result=result+"<br>";
	Iterator itr2 = 	listOfHosts.iterator();
	while(itr2.hasNext()) {
	    String hostName=(String)itr2.next();
	    //result=result+"<h3>Host:"+hostName+"</h3><br>";
	    PerfSonarHost ph=new PerfSonarHost(parameterBag,db,hostName);
	    result=result+"<h3>"+ph.oneLineInfo()+"</h3><br>";
	    //result=result+ph.shortStatusTable().toHtml()+"<br>";
	    HtmlTable ht=getTableOfServicesOnHost(hostName);
	    result=result+ht.toHtml()+"<br>";
	}
	return result;
    }

}
