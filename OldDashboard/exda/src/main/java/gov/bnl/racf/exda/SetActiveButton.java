package gov.bnl.racf.exda;
import java.io.*;
import java.sql.*;
import java.lang.Class;
public class SetActiveButton
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;
    private boolean active=true;

    private String destinationPageName="Confirm Active";


    public SetActiveButton(ParameterBag inputParameterBag,DbConnector inputDb)
	{
	    parameterBag=(ParameterBag)inputParameterBag.clone();
	    db=inputDb;
	    //System.out.println(getClass().getName()+" probe Id in button="+parameterBag.probeId);
	}
    public String getDestinationPageName(){
	return this.destinationPageName;
    }
    public void setDestinationPageName(String inputVar){
	this.destinationPageName=inputVar;
    }
 
    public String toHtml(){
	checkActiveNow();

	String form="";

	form="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">";	
	if (ParameterBag.variableNotEmpty(parameterBag.page)){
	    form = form + "<input type=\"hidden\" name=\"page\" value=\"" +parameterBag.pageAddress(this.getDestinationPageName())+  "\">";
	}
	if (ParameterBag.variableNotEmpty(parameterBag.src)){
	    form = form + "<input type=\"hidden\" name=\"src\" value=\"" +parameterBag.src+  "\">";
	}
	if (ParameterBag.variableNotEmpty(parameterBag.dst)){
	    form = form + "<input type=\"hidden\" name=\"dst\" value=\"" +parameterBag.dst+  "\">";
	}
	if (ParameterBag.variableNotEmpty(parameterBag.mon)){
	    form = form + "<input type=\"hidden\" name=\"mon\" value=\"" +parameterBag.mon+  "\">";
	}
	if (ParameterBag.variableNotEmpty(parameterBag.host)){
	    form = form + "<input type=\"hidden\" name=\"host\" value=\"" +parameterBag.host+  "\">";
	}
	if (ParameterBag.variableNotEmpty(parameterBag.service)){
	    form = form + "<input type=\"hidden\" name=\"service\" value=\"" +parameterBag.service+  "\">";
	}
	//if (ParameterBag.variableNotEmpty(parameterBag.site)){
	//    form = form + "<input type=\"hidden\" name=\"site\" value=\"" +parameterBag.site+  "\">";
	//}
	if (ParameterBag.variableNotEmpty(parameterBag.serviceName)){
	    form = form + "<input type=\"hidden\" name=\"serviceName\" value=\"" +parameterBag.serviceName+  "\">";
	}
	if (ParameterBag.variableNotEmpty(parameterBag.hostName)){
	    form = form + "<input type=\"hidden\" name=\"hostName\" value=\"" +parameterBag.hostName+  "\">";
	}
 	//if (ParameterBag.variableNotEmpty(parameterBag.executeNow)){
	//    form = form + "<input type=\"hidden\" name=\"executeNow\" value=\"yes\">";
	//}
 	if (ParameterBag.variableNotEmpty(parameterBag.probeId)){
	    form = form + "<input type=\"hidden\" name=\"probeId\" value=\""+parameterBag.probeId+"\">";
	}
	if(active){
	    form = form + "<input type=\"hidden\" name=\"toggleActive\" value=\"Y\">";
	    form=form+"<input type=\"submit\" value=\"Mark service as inactive\" />";
	}else{
	    //form = form + "<input type=\"hidden\" name=\"Active\" value=\"N\">";
	    form = form + "<input type=\"hidden\" name=\"toggleActive\" value=\"Y\">";
	    form=form+"<input type=\"submit\" value=\"Mark service as active\" />";
	}
	form = form + "</form> ";
	
	return form;
    }
    public void checkActiveNow(){
	// check if service is currently active

	// first of all: is the service active?
	String getActiveQuery="SELECT Active FROM Services WHERE ProbeId=? ";	  
	PreparedStatement getActiveSql=null;
	try{
	    getActiveSql=db.getConn().prepareStatement(getActiveQuery);
	    getActiveSql.setString(1,parameterBag.probeId);
	    ResultSet rs=getActiveSql.executeQuery();
	    while(rs.next()){
		active=rs.getBoolean("Active");
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to get Active info about service "+parameterBag.probeId);
	    System.out.println(getClass().getName()+" "+getActiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getActiveSql.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+getActiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }
    public void toggleActiveNow(){
	checkActiveNow();

	boolean newIsactive=!active;
	String updateIsactiveQuery="UPDATE Services  SET Active=? WHERE ProbeId=? ";	
	PreparedStatement updateIsactiveSql=null;
	try{
	    updateIsactiveSql=db.getConn().prepareStatement(updateIsactiveQuery);
	    updateIsactiveSql.setBoolean(1,newIsactive);
	    updateIsactiveSql.setString(2,parameterBag.probeId);
	    updateIsactiveSql.executeUpdate();
	    System.out.println(getClass().getName()+" Swap Active ");
	    System.out.println(getClass().getName()+" probeId= "+parameterBag.probeId);
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating active");
	    System.out.println(getClass().getName()+" "+updateIsactiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    updateIsactiveSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+updateIsactiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }    

    public String toString(){
	String result=toHtml();
	return result;
    }
}

