package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfSchedulers
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private ResultSet rs=null;

    private String schedulerName=null;

    private List<Scheduler>listOfSchedulers=new ArrayList<Scheduler>();

    public ListOfSchedulers(ParameterBag paramBag,   DbConnector inputDb)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.loadListOfSchedulers();
	}
    private void loadListOfSchedulers(){

	String getQuery="select schedulerId from Schedulers";
	PreparedStatement getQueryPS= null;
	try{
	    getQueryPS= db.getConn().prepareStatement(getQuery);

	    rs=getQueryPS.executeQuery();
	    int count = 0;
	    while (rs.next ())
		{
		    int schedulerId=rs.getInt("schedulerId");
		    this.listOfSchedulers.add(new Scheduler( this.parameterBag,this.db,schedulerId));
		    count=count+1;
		}
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading schedulers from database");
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

    public HtmlLink  getLinkToCreateSchedulerPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Scheduler")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToManipulateSchedulersPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Manipulate Schedulers")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    public String toString(){
	String result="";
	Iterator iter=this.listOfSchedulers.iterator();
	while (iter.hasNext()){
	    Scheduler  nextScheduler=(Scheduler)iter.next();
	    String nextSchedulerName=nextScheduler.getSchedulerName();
	    result=result+nextSchedulerName+" ";
	}
	return result;
    }


    public HtmlTable manipulationHtmlTable(){
	HtmlTable ht=new HtmlTable(3);
	ht.setBorder(1);
	ht.setPadding(0);
	
	Iterator itr = this.listOfSchedulers.iterator(); 
	while(itr.hasNext()) {
	    Scheduler scheduler=(Scheduler)itr.next();
	    ht.addRow(scheduler.infoTableRow());
	} 
	return ht;
    }

    public String manipulationHtmlPage(){
	String result="<H1>PerfSonar Schedulers</H1>";
	result=result+"<br>";
	result=result+this.getLinkToCreateSchedulerPage("<strong>Click here to create new scheduler</strong>")+"<br>";
	result=result+this.manipulationHtmlTable().toHtml();
	return result;
    }
    public String createNewSchedulerForm(){
	String result="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">" ;
	HtmlTable formTable=new HtmlTable(2);
	formTable.addCell("Scheduler Name");
	formTable.addCell("<input type=\"text\" name=\"schedulerName\" value=\"Fill new scheduler name\" /><br>");
	formTable.addCell("Scheduler Host");
	formTable.addCell("<input type=\"text\" name=\"schedulerHost\" value=\"Fill new scheduler host\" /><br>");
	result=result+formTable.toHtml();

	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Insert New Scheduler")+"\" />";
	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	return result;
    }
    public String createNewSchedulerPage(){
	String result="<H1>Create New Scheduler</H1>";
	result=result+"<br>";
	result=result+this.createNewSchedulerForm();
	return result;
    }
    public String insertNewSchedulerPage(String newSchedulerName,String newSchedulerHost){
	String result="<H1>New Scheduler "+newSchedulerName+"</H1>";
	result=result+"<br>";
	Scheduler scheduler=new Scheduler(this.parameterBag,this.db);
	scheduler.setSchedulerName(newSchedulerName);
	scheduler.setSchedulerHost(newSchedulerHost);
	scheduler.setReadOnly(false);
	boolean insertResult=scheduler.insert();
	if(insertResult==true){
	    result=result+"Scheduler has been succesfully created<br>";
	}else{
	    result=result+"Scheduler creation failed<br>";
	}
	result=result+this.getLinkToManipulateSchedulersPage("<strong>Click here to go to list of schedulers</strong>");
	return result;
    }

    public String schedulerSelector(String currentScheduler){
	String result="<select name=\"schedulerName\">";
	Iterator iter=this.listOfSchedulers.iterator();
	while (iter.hasNext()){
	    Scheduler  nextScheduler=(Scheduler)iter.next();
	    String nextSchedulerName=nextScheduler.getSchedulerName();
	    if(nextSchedulerName.equals(currentScheduler)){
	    result=result+"<option value=\""+nextSchedulerName+"\" SELECTED>"+nextSchedulerName+"</option>";
	    }else{
		result=result+"<option value=\""+nextSchedulerName+"\">"+nextSchedulerName+"</option>";
	    }
	}
	result=result+"</select>";
	return result;
    }

}
