package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfServices
{  

    private DbConnector db=null;
    private ParameterBag parameterBag = null;

 
    public ListOfServices(DbConnector inputDb){
	this.db=inputDb;
    }
    public ListOfServices(ParameterBag parambag, DbConnector inputDb){
	this.db=inputDb;
	this.parameterBag=parambag;
    }
    //  ========================================================  //
    public String getListOfServicesDue(){
	String result="";
	
	String getQuery="select dbid,ProbeId,NextCheckTime from Services WHERE NextCheckTime<NOW()";
	PreparedStatement getQueryPS= null;
	try{
	    getQueryPS= db.getConn().prepareStatement(getQuery);
	    ResultSet rs=getQueryPS.executeQuery();
	    while (rs.next ())
		{
		    int dbid=rs.getInt("dbid");
		    Timestamp nextCheckTime=rs.getTimestamp("NextCheckTime");
		    String probeId=rs.getString("ProbeId");
		    result=result+Integer.toString(dbid)+" "+nextCheckTime.toString()+" "+probeId+"<br>";
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
	return result;
    }


    public String getProbesForCollector(String schedulerIp){
	String result="";
	//result=result+"Probes scheduled for collector "+schedulerHost+"<br>";
	//result=result+"numberOfProbesFound={NUMBER_OF_PROBES}<BR>";

	Scheduler scheduler=new Scheduler(this.parameterBag,this.db,"","",schedulerIp);
	String schedulerName=scheduler.getSchedulerName();

	String getQuery="select dbid,probeId,nextCheckTime from Services WHERE NextCheckTime<NOW() and Active=TRUE and SchedulerName=? and not ProbeRunning='Y'";
	PreparedStatement getQueryPS= null;
	int numberOfProbes=0;
	try{
	    getQueryPS= db.getConn().prepareStatement(getQuery);
	    getQueryPS.setString(1,schedulerName);
	    ResultSet rs=getQueryPS.executeQuery();
	    while (rs.next ())
		{
		    int dbid=rs.getInt("dbid");
		    PerfSonarService perfSonarService=new PerfSonarService(this.parameterBag,this.db,dbid);
		    perfSonarService.loadDataFromServicesTable();
		    // Timestamp nextCheckTime=rs.getTimestamp("NextCheckTime");
		    // String probeId=rs.getString("probeId");
		    // result=result+Integer.toString(dbid)+" "+nextCheckTime.toString()+" "+probeId+"<br>";
		    result=result+perfSonarService.executionDirectivesJson().toString()+"<BR>";
		    numberOfProbes=numberOfProbes+1;
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
	result=result.replace("{NUMBER_OF_PROBES}",Integer.toString(numberOfProbes));
	return result;
    }

}
