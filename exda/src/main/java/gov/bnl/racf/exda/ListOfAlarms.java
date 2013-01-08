package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfAlarms
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private ResultSet rs=null;

    private List<Alarm>listOfAlarms=new ArrayList<Alarm>();


    public ListOfAlarms(ParameterBag paramBag,   DbConnector inputDb)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.loadListOfAlarms();
	}
    private void loadListOfAlarms(){
	String getQuery="select alarmId from Alarms";
	PreparedStatement getQueryPS= null;
	try{
	    getQueryPS= db.getConn().prepareStatement(getQuery);

	    rs=getQueryPS.executeQuery();
	    int count = 0;
	    while (rs.next ())
		{
		    int alarmId=rs.getInt("alarmId");
		    this.listOfAlarms.add(new Alarm(this.parameterBag,this.db,alarmId));
		    count=count+1;
		}
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading alarms from database");
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

    public  List<Alarm> list(){
	return this.listOfAlarms;
    }
    public int size(){
	return this.list().size();
    }

    public HtmlLink  getLinkToCreateAlarmPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Alarm")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }


    public HtmlTable manipulationHtmlTable(){
	HtmlTable ht=new HtmlTable(3);
	ht.setBorder(1);
	ht.setPadding(0);
	
	Iterator itr = this.listOfAlarms.iterator(); 
	while(itr.hasNext()) {
	    Alarm alarm=(Alarm)itr.next();
	    ht.addRow(alarm.infoTableRow());
	} 
	return ht;
    }

    public String manipulationHtmlPage(){
	String result="<H1>Registered Alarms</H1>";
	result=result+"<br>";
	result=result+this.getLinkToCreateAlarmPage("<strong>Click here to create new alarm</strong>");
	result=result+"<br>";
	//result=result+" <strong>or</strong> ";

	result=result+this.manipulationHtmlTable().toHtml();
	return result;
    }

    public HtmlTable alarmsOverviewTable(){
	HtmlTable tb=new HtmlTable(6);
	tb.setBorder(1);
	tb.setPadding(0);
	
	Iterator itr = this.listOfAlarms.iterator(); 
	while(itr.hasNext()) {
	    Alarm alarm=(Alarm)itr.next();
	    tb.addCell(alarm.shortStatusCell());
	} 
	return tb;
    }

    public String alarmsOverviewPage(){
	String result="";
	result=result+"<H2>List of defined alarms</h2>";
	result=result+"<br>";
	HtmlTable tb = this.alarmsOverviewTable();
	result=result+tb.toHtml();
	return result;
    }


}
