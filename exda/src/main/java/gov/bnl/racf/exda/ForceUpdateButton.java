package gov.bnl.racf.exda;
import java.io.*;
import java.sql.*;
import java.lang.Class;
public class ForceUpdateButton
{  
    private String moduleName="ForceUpdateButton";
    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private String intervalVariable="state_time";
    private int timeZoneShift=0;

    public ForceUpdateButton(ParameterBag inputParameterBag,DbConnector inputDb)
	{
	    parameterBag=(ParameterBag)inputParameterBag.clone();
	    db=inputDb;
	}
 
    public String toHtml(){
	String form="";

	form="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">";	
	//if (parameterBag.variableNotEmpty(parameterBag.page)){
	form = form + "<input type=\"hidden\" name=\"page\" value=\"" +parameterBag.pageAddress("Force Test Matrix Element")+  "\">";
	    //}
	if (parameterBag.variableNotEmpty(parameterBag.src)){
	    form = form + "<input type=\"hidden\" name=\"src\" value=\"" +parameterBag.src+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.dst)){
	    form = form + "<input type=\"hidden\" name=\"dst\" value=\"" +parameterBag.dst+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.mon)){
	    form = form + "<input type=\"hidden\" name=\"mon\" value=\"" +parameterBag.mon+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.host)){
	    form = form + "<input type=\"hidden\" name=\"host\" value=\"" +parameterBag.host+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.service)){
	    form = form + "<input type=\"hidden\" name=\"service\" value=\"" +parameterBag.service+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.site)){
	    form = form + "<input type=\"hidden\" name=\"site\" value=\"" +parameterBag.site+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.serviceName)){
	    form = form + "<input type=\"hidden\" name=\"serviceName\" value=\"" +parameterBag.serviceName+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.hostName)){
	    form = form + "<input type=\"hidden\" name=\"hostName\" value=\"" +parameterBag.hostName+  "\">";
	}
 	if (parameterBag.variableNotEmpty(parameterBag.executeNow)){
	    form = form + "<input type=\"hidden\" name=\"executeNow\" value=\"yes\">";
	}
 	if (parameterBag.variableNotEmpty(parameterBag.probeId)){
	    form = form + "<input type=\"hidden\" name=\"probeId\" value=\""+parameterBag.probeId+"\">";
	}
 	if (parameterBag.variableNotEmpty(parameterBag.id)){
	    form = form + "<input type=\"hidden\" name=\"id\" value=\""+parameterBag.id+"\">";
	}
	form=form+"<input type=\"submit\" value=\"Test NOW()\" />";
	form = form + "</form> ";
	
	return form;
    }
    public void forceTestNow(){
	//String updateTimeSqlQuery="UPDATE Services  SET NextCheckTime=NOW() WHERE HostName=? and HostName2=? and HostName3=? ";	   
	String updateTimeSqlQuery="UPDATE Services  SET NextCheckTime=NOW() WHERE ProbeId=? ";	
	PreparedStatement updateTimeSql=null;
	try{
	    updateTimeSql=db.getConn().prepareStatement(updateTimeSqlQuery);
	    updateTimeSql.setString(1,parameterBag.probeId);
	    updateTimeSql.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating database");
	    System.out.println(updateTimeSqlQuery);
	    System.out.println(e);
	}
	try{
	    updateTimeSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(updateTimeSqlQuery);
	    System.out.println(e);
	}	    
    }    

    public String toString(){
	String result=toHtml();
	return result;
    }
}

