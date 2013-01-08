package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.util.Collections;


import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfPerfSonarHosts
{  

    private ParameterBag parameterBag = null;

    private DbConnector db=null;

    private String cloudName=null;

    private List<PerfSonarHost> list=new ArrayList<PerfSonarHost>();

    public ListOfPerfSonarHosts(ParameterBag paramBag,   DbConnector inputDb)
	{
	    parameterBag=paramBag;
	    db=inputDb;
	    this.loadPerfSonarHosts();


	}
    private void loadPerfSonarHosts(){

	String getQuery="select distinct HostName from Hosts";
	PreparedStatement getQueryPS= null;
	try{
	    getQueryPS= db.getConn().prepareStatement(getQuery);

	    ResultSet rs=getQueryPS.executeQuery();
	    int count = 0;
	    while (rs.next ()){
		String hostName=rs.getString("HostName");
		PerfSonarHost host = new PerfSonarHost(parameterBag,db,hostName);
		list.add( host );
		count=count+1;
	    }
	    rs.close();

	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+getQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getQueryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+getQuery);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }
    public HtmlLink linkToHostCreatePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Host")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public  List<PerfSonarHost> getList(){
	return this.list;
    }

    public String toString(){
	String result="Hello world from list of hosts";
	return result;
    }

    public HtmlTable htmlTable(){
	HtmlTable ht=new HtmlTable(8);
	ht.setBorder(0);
	ht.setPadding(0);

	List<PerfSonarHost> temporaryList=new ArrayList<PerfSonarHost>();

	Iterator itr1 = this.list.iterator(); 
	while(itr1.hasNext()) {
	    PerfSonarHost host = (PerfSonarHost)itr1.next(); 
	    host.setSortingCriteria("hostName");
	    temporaryList.add(host);
	} 

	Collections.sort(temporaryList);

	Iterator itr = temporaryList.iterator(); 
	while(itr.hasNext()) {
	    PerfSonarHost host = (PerfSonarHost)itr.next(); 
	    ht.addRow(host.infoTableRow());
	} 
	return ht;
    }
    public String htmlPage(){
	String result="<H1>List of Hosts</H1>";
	result=result+"<br>";
	result=result+this.linkToHostCreatePage("<strong>Click here to create a new host</string>");
	result=result+"<br>";
	result=result+this.htmlTable().toHtml();
	return result;
    }

}
