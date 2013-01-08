package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfClouds
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private ResultSet rs=null;

    private String cloudName=null;

    private List<PerfSonarCloud>listOfClouds=new ArrayList<PerfSonarCloud>();

    public ListOfClouds(ParameterBag paramBag,   DbConnector inputDb)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.loadListOfClouds();
	}
    private void loadListOfClouds(){

	String getQuery="select cloudId from Clouds";
	PreparedStatement getQueryPS= null;
	try{
	    getQueryPS= db.getConn().prepareStatement(getQuery);

	    rs=getQueryPS.executeQuery();
	    int count = 0;
	    while (rs.next ())
		{
		    int cloudId=rs.getInt("cloudId");
		    this.listOfClouds.add(new PerfSonarCloud( this.parameterBag,this.db,cloudId));
		    count=count+1;
		}
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading clouds from database");
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

    public HtmlLink  getLinkToCreateCloudPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Cloud")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToManipulateCloudsPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Manipulate Clouds")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    public String toString(){
	String result="";
	Iterator iter=this.listOfClouds.iterator();
	while (iter.hasNext()){
	    PerfSonarCloud  nextCloud=(PerfSonarCloud)iter.next();
	    String nextCloudName=nextCloud.getCloudName();
	    result=result+nextCloudName+" ";
	}
	return result;
    }

    public HtmlTable htmlTable(){
	HtmlTable ht=new HtmlTable(5);
	//ht.setBorder(2);
	//ht.setPadding(0);
	
	Iterator itr = this.listOfClouds.iterator(); 
	while(itr.hasNext()) {
	    PerfSonarCloud cloud=(PerfSonarCloud)itr.next();
	    ht.addCell(cloud.shortStatusCell());
	} 
	return ht;
    }
    public HtmlTable manipulationHtmlTable(){
	HtmlTable ht=new HtmlTable(4);
	ht.setBorder(1);
	ht.setPadding(0);
	
	Iterator itr = this.listOfClouds.iterator(); 
	while(itr.hasNext()) {
	    PerfSonarCloud cloud=(PerfSonarCloud)itr.next();
	    ht.addRow(cloud.infoTableRow());
	} 
	return ht;
    }
    public String htmlPage(){
	String result="<H1>Known PerfSonar Clouds</H1>";
	result=result+"<br>";
	result=result+this.htmlTable().toHtml();
	return result;
    }
    public String manipulationHtmlPage(){
	String result="<H1>PerfSonar Clouds</H1>";
	result=result+"<br>";
	result=result+this.getLinkToCreateCloudPage("<strong>Click here to create new cloud</strong>")+"<br>";
	result=result+this.manipulationHtmlTable().toHtml();
	return result;
    }
    public String createNewCloudForm(){
	String result="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">" ;
	HtmlTable formTable=new HtmlTable(2);
	formTable.addCell("Cloud Name");
	formTable.addCell("<input type=\"text\" name=\"cloudName\" value=\"Fill new cloud name\" /><br>");

	result=result+formTable.toHtml();

	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Insert New Cloud")+"\" />";
	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	return result;
    }
    public String createNewCloudPage(){
	String result="<H1>Create New Cloud</H1>";
	result=result+"<br>";
	result=result+this.createNewCloudForm();
	return result;
    }
    public String insertNewCloudPage(String newCloudname){
	String result="<H1>New Cloud "+newCloudname+"</H1>";
	result=result+"<br>";
	PerfSonarCloud newCloud=new PerfSonarCloud(this.parameterBag,this.db);
	newCloud.setCloudName(newCloudname);
	newCloud.setReadOnly(false);
	boolean insertResult=newCloud.insert();
	if(insertResult==true){
	    result=result+"Cloud has been succesfully created<br>";
	}else{
	    result=result+"Cloud creation failed<br>";
	}
	result=result+this.getLinkToManipulateCloudsPage("<strong>Click here to go to list of clouds</strong>");
	return result;
    }

}
