package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.Collections;


public class PerfSonarHost implements Comparable
{  

    private String hostName=null;
    private String ip=null;
    private String externalUrl=null;
    private String siteName="";    
    //private String cloudName=null;
    private int id=0;

    private ProbeStatus hostStatus = null;
    private boolean active=true;
    private boolean readOnly=false;

    private ProbeStatus hostPrimitivesStatus=null;

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private HostActiveButton button=null;

    private  List<PrimitiveServiceRecord>primitivesRecordsOnThisHost   =new ArrayList<PrimitiveServiceRecord>();
    private  List<PrimitiveServiceRecord>primitivesRecordsNotOnThisHost=new ArrayList<PrimitiveServiceRecord>();

    private  List<PrimitiveService>primitiveServicesOnThisHost=new ArrayList<PrimitiveService>();

    private ActivityLogger activityLogger=null;

    private String sortingCriteria="siteName";

    private boolean detailsLoaded=false;

    public PerfSonarHost(ParameterBag paramBag,  DbConnector inputDb)
    {
	// constructor 1, empty host
	this.parameterBag=paramBag;
	this.db=inputDb;
	this.button=new HostActiveButton(this.parameterBag,this.db,hostName);

	this.activityLogger=new ActivityLogger(this.parameterBag,this.db);

	this.setHostPrimitivesStatus(new ProbeStatus("UNDEFINED"));

    }
    public PerfSonarHost(ParameterBag paramBag,  DbConnector inputDb,String hostName )
	{
	    // constructor 2
	    this.parameterBag=paramBag;
	    this.db=inputDb;

	    this.hostName=hostName;

	    this.setHostPrimitivesStatus(new ProbeStatus("UNDEFINED"));


	    // == execute update status, if needed ===
	    //this.button=new HostActiveButton(this.parameterBag,this.db,hostName);

	    this.activityLogger=new ActivityLogger(this.parameterBag,this.db);

	    //=========== get information =======

	    String initialisationSqlQuery="select  * from Hosts where HostName=?";
	    PreparedStatement initialisationSql=null;
	    try{
		initialisationSql= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql.setString(1,hostName);

		ResultSet rs=initialisationSql.executeQuery();
		this.unpackResultSet(rs);
		
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database in constructor 2");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e);
	    }	 
	    try{
		initialisationSql.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e);
	    }

   
	    this.readSiteInformation();
	}
    public PerfSonarHost(ParameterBag paramBag,  DbConnector inputDb,int hostId )
	{
	    // constructor 3
	    parameterBag=paramBag;
	    db=inputDb;

	    this.id=hostId;

	    this.setHostPrimitivesStatus(new ProbeStatus("UNDEFINED"));

	    // == execute update status, if needed ===
	    //button=new HostActiveButton(parameterBag,db,hostName);

	    try{
		this.activityLogger=new ActivityLogger(this.parameterBag,this.db);
	    }catch(Exception e){
		System.out.println(getClass().getName()+" ERROR: failed to create Activity Logger, this may cause problems later");
	    }

	    //=========== get information =======

	    String initialisationSqlQuery="select  * from Hosts where dbid=?";
	    PreparedStatement initialisationSql=null;
	    try{
		initialisationSql= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql.setInt(1,this.id);

		ResultSet rs=initialisationSql.executeQuery();
		this.unpackResultSet(rs);
		
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database in constructor 3");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		initialisationSql.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e);
	    }
	  
	    this.readSiteInformation();
	}

    private void unpackResultSet(ResultSet rs){
	    try{	
		while (rs.next ()){
		    this.hostName=rs.getString("HostName").trim();
		    this.ip=rs.getString("ip").trim();
		    this.externalUrl =rs.getString("ExternalUrl");
		    this.setActive(rs.getBoolean("Active"));
		    this.setReadOnly(rs.getBoolean("ReadOnly"));
		    this.hostStatus=new ProbeStatus(rs.getString("HostStatus"),this.active);

		    this.id=rs.getInt("dbid");
		}
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when unpacking result set for host");
		System.out.println(getClass().getName()+" "+e);
	    }	       
    }

    public void unpackParameterBag(){
	// get private fields from Parameter bag
	if(parameterBag.variableNotEmpty("hostName")){
	    this.setHostName(parameterBag.hostName);
	}
	if(parameterBag.variableNotEmpty("ip")){
	    this.setIp(parameterBag.ip);
	}
	if(parameterBag.variableNotEmpty("externalUrl")){
	    this.setExternalUrl(parameterBag.externalUrl);
	}
	if(parameterBag.variableNotEmpty("Active")){
	    this.setActive(parameterBag.active);
	}
	//if(parameterBag.variableNotEmpty("ReadOnly")){
	//    this.setReadOnly(parameterBag.readOnly);
	//}
    }

    public void readSiteInformation(){
	
	String siteNameForThisHost="";

	String query="SELECT * from SitesHosts where hostId=?";
	String query2="SELECT * FROM Sites where SiteId=?";
	PreparedStatement queryPS=null;
	PreparedStatement query2PS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    query2PS= db.getConn().prepareStatement(query2);

	    queryPS.setInt(1,this.getId());
	    
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		    int siteId=rs.getInt("siteId");
		    query2PS.setInt(1,siteId);
		    ResultSet rs2 = query2PS.executeQuery();
		    while(rs2.next()){
			siteNameForThisHost=siteNameForThisHost+rs2.getString("SiteName");
		    }
	    }
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when obtaining site names for host");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    query2PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" "+e);
	}
	siteNameForThisHost=siteNameForThisHost.trim();
	this.setSiteName(siteNameForThisHost);

    }

    public String editHostLine(){
	String result="";
	result="<form>";

	return result;
    }

    public String swapActiveForm(){
	String message="";
	String result="<br><br><strong>";
	result=result+"You are about to change the active staus of host "+hostName+" to "+this.swapActive()+"<br>";
	result=result+"Your DN is "+parameterBag.user.getDN()+"<br>";
	result=result+"This operation will be logged<br>";
	result=result+"Please fill explanation for the log:<br>";
	result=result+"<form><input type=\"text\" name=\"message\" value=\""+message+"\" /><br>";
	result=result+"<input type=\"hidden\" name=\"hostName\" value=\""+hostName+"\" /><br>";

	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("PerfSonar Host Active Update")+"\" />";
	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form></strong>";
	return result;
    }
    public String editHostForm(){
	String result="<form>";
	// result=result+"<input type=\"text\" name=\"hostName\" value=\""+hostName+"\" /><br>";
	// result=result+"<input type=\"text\" name=\"ip\" value=\""+ip+"\" /><br>";
	// result=result+"<input type=\"text\" name=\"ExternalUrl\" value=\""+externalUrl+"\" /><br>";
	// result=result+"<input type=\"text\" name=\"CloudName\" value=\""+cloudName+"\" /><br>";
	// result=result+"<input type=\"text\" name=\"Active\" value=\""+boolean2string(active)+"\" /><br>";
		
	// result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Host")+"\" />";
	// result=result+"<input type=\"hidden\" name=\"dbid\" value=\""+id+"\" />";

	// result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	// result=result+"</form>";

	HtmlTable formTable=new HtmlTable(2);

	formTable.addCell("HostName");
	formTable.addCell("<input type=\"text\" name=\"hostName\" value=\""+this.hostName+"\" /><br>");

	//formTable.addCell("SiteName");
	//formTable.addCell("<input type=\"text\" name=\"siteName\" value=\""+this.siteName+"\" /><br>");

	//formTable.addCell("CloudName");
	//formTable.addCell("<input type=\"text\" name=\"cloudName\" value=\""+this.cloudName+"\" /><br>");

	formTable.addCell("External Url");
	formTable.addCell("<input type=\"text\" name=\"externalUrl\" value=\""+this.externalUrl+"\" /><br>");

	formTable.addCell("ip");
	formTable.addCell("<input type=\"text\" name=\"ip\" value=\""+ip+"\" /><br>");

	formTable.addCell("Active");

	String activeSelect="<select name=\"Active\">";
	activeSelect=activeSelect+"<option value=\"Y\">Y</option>";
	activeSelect=activeSelect+"<option value=\"N\">N</option>";
	activeSelect=activeSelect+"</select>";
	formTable.addCell(activeSelect);
	//	formTable.addCell("<input type=\"text\" name=\"Active\" value=\""+this.boolean2string(this.active)+"\" /><br>");
	    
	// result=result+"<input type=\"text\" name=\"hostName\" value=\""+hostName+"\" /><br>";
	// result=result+"<input type=\"text\" name=\"ip\" value=\""+ip+"\" /><br>";
	// result=result+"<input type=\"text\" name=\"ExternalUrl\" value=\""+externalUrl+"\" /><br>";
	// result=result+"<input type=\"text\" name=\"CloudName\" value=\""+cloudName+"\" /><br>";
	// result=result+"<input type=\"text\" name=\"Active\" value=\""+boolean2string(active)+"\" /><br>";

	if(this.id>0){
	    result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Host")+"\" />";
	    result=result+"<input type=\"hidden\" name=\"id\" value=\""+Integer.toString(id)+"\" />";
	}else{
	    result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Insert Host")+"\" />";
	}

     

	result=result+"<br>"+formTable.toHtml()+"<br>";

	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	
	return result;

    }
    public boolean insertHost(){
	boolean result=true;
	String insertHostQuery="INSERT INTO Hosts (HostName,ExternalUrl,ip,Active,ReadOnly,HostStatus) VALUES (?,?,?,?,?,'OK')";
	PreparedStatement insertHostQueryPS=null;
	try{
	    insertHostQueryPS = db.getConn().prepareStatement(insertHostQuery);
	    insertHostQueryPS.setString(1,this.getHostName());
	    insertHostQueryPS.setString(2,this.getExternalUrl());
	    insertHostQueryPS.setString(3,this.getIp());
	    insertHostQueryPS.setBoolean(4,this.getActive());
	    insertHostQueryPS.setBoolean(5,false);
	    insertHostQueryPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when inserting host into database");
	    System.out.println(getClass().getName()+" "+insertHostQuery);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}	    
	// now get index of the inserted host
	try{
	    ResultSet rs=insertHostQueryPS.getGeneratedKeys();
	    while (rs.next ()){
		this.setId(rs.getInt(1));
	    }
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not obtain key of newly inserted host");
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    insertHostQueryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+insertHostQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }
    public PerfSonarHost cloneHost(){
	PerfSonarHost newHost=new PerfSonarHost(this.parameterBag,this.db);

	newHost.setHostName(this.getHostName()+"-clone");

	newHost.setExternalUrl(this.externalUrl);

	newHost.setIp(this.getIp());
	newHost.setActive(this.getActive());
	newHost.setStatus(this.getHostStatus());
	newHost.insertHost();
	return newHost;
    }
    public void deleteHost(){
	// delete the current host from database
	String deleteCommand="DELETE FROM Hosts WHERE dbid=?";
	PreparedStatement deleteCommandPS=null;
	try{
	    deleteCommandPS=db.getConn().prepareStatement(deleteCommand);
	    deleteCommandPS.setInt(1,this.getId());
	    deleteCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to delete host");
	    System.out.println(getClass().getName()+" "+deleteCommand);
	    System.out.println(getClass().getName()+" host id="+this.id);
	    System.out.println(getClass().getName()+" "+e);
	}	
	try{
	    deleteCommandPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to delete prepared statement");
	    System.out.println(getClass().getName()+" "+deleteCommand);
	    System.out.println(getClass().getName()+" "+e);
	}	
    }
    public void saveHost(){
	// save the current host information. host must be already present in database
	String saveHostCommand="UPDATE Hosts SET HostName=?,ExternalUrl=?,ip=?,Active=?,HostStatus=? where dbid=?";
	PreparedStatement saveHostPS=null;
	try{
	    saveHostPS= db.getConn().prepareStatement(saveHostCommand);
	    saveHostPS.setString(1,this.hostName);
	    saveHostPS.setString(2,this.getExternalUrl());
	    saveHostPS.setString(3,this.getIp());
	    saveHostPS.setBoolean(4,this.getActive());
	    saveHostPS.setString(5,this.hostStatus.toString());	    
	    saveHostPS.setInt(6,this.id);
	    saveHostPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to save host");
	    System.out.println(getClass().getName()+" "+saveHostCommand);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    saveHostPS.close();
	 }catch (Exception e){
	    System.out.println(getClass().getName()+" failed to delete prepared statement");
	    System.out.println(getClass().getName()+" "+saveHostCommand);
	    System.out.println(getClass().getName()+" "+e);
	}   
    }

    public String editHostPage(){
	String result="<br><strong>We are on edit host page</strong><br>";
	result=result+this.editHostForm();
	result=result+"<br>";
	result=result+"<strong>"+this.linkToHostDetails("Go back to the host "+this.hostName+" page")+"</strong>";
	return result;
    }
    public String saveHostPage(){
	String result="<br><strong>We are on save host page</strong><br>";
	this.saveHost();
	result=result+"<strong>The host information has been saved</strong><br>";
	result=result+"<strong>"+this.linkToHostDetails("Go to the host "+this.hostName+" page")+"</strong> or ";
	result=result+"<strong>"+this.linkToListOfHosts("Go to the list of hosts")+"</strong>";
	return result;
    }
    public String cloneHostPage(){
	String result="<br><strong>We are on clone host page</strong><br>";
	PerfSonarHost clonedHost=this.cloneHost();
	result=result+"<strong>This is the newly cloned host:</strong><br>";
	result=result+clonedHost.fullHtmlTable().toHtml();
	result=result+"<br>";
	result=result+"<strong>"+this.linkToHostDetails("Go to the host "+this.hostName+" page")+" or </strong>";
	result=result+"<strong>"+this.linkToListOfHosts("Go to the list of hosts")+"</strong>";
	return result;
    }
    public String deleteHostPage(){
	this.deleteHost();
	String result="<br><strong>The host "+this.getHostName()+" has been deleted!</strong><br>";
	result=result+"<strong>"+this.linkToListOfHosts("Go back to the list of hosts")+"</strong>";
	return result;
    }
    public String confirmDeleteHostPage(){
	String result="<br><strong>The following host is about to be deleted:</strong><br>";	
	result=result+this.fullHtmlTable().toHtml()+"<BR>";
	result=result+"<br><strong>Do you really want to delete it?</strong><br>";
	result=result+"<strong>"+this.linkToHostDeletePage("Yes, delete it.").toHtml()+" ---  "+this.linkToListOfHosts("No, take me back to the list of hosts")+"</strong><br>";

	return result;
    }


    public String updateActivePage(){
	String result="<strong>";
	if(updateActive()){
	    result=result+"Host "+hostName+" has been set ";
	    if(active){
		result=result+"Active";
	    }else{
		result=result+"InActive";
	    }
	}else{
	   result=result+"Update of host "+hostName+" failed<br>";
	}
	result=result+"<br>";
	result=result+"Details for host "+linkToHostDetails(hostName).toHtml()+"<br>";
	result=result+"</strong>";
	return result;
    }
    public boolean updateActive(){
	boolean result=true;
	String updateHostStatement="UPDATE Hosts SET Active=? WHERE dbid=?"; 
	PreparedStatement updateHostPreparedStatement=null;
	try{
	    updateHostPreparedStatement=db.getConn().prepareStatement(updateHostStatement);
	    updateHostPreparedStatement.setBoolean(1,this.swapActive());
	    updateHostPreparedStatement.setInt(2,id);
	    updateHostPreparedStatement.executeUpdate();
	    result=true;
	    active=swapActive();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating active status of host "+hostName);
	    System.out.println(getClass().getName()+" "+updateHostStatement);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    updateHostPreparedStatement.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+updateHostStatement);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}


	// log the action
	String logUpdateHostStatement="INSERT INTO ActionHistory (HostName,dn,message,action,TimeStamp) VALUES(?,?,?,?,NOW())"; 
	PreparedStatement insertLogStatement=null;
	try{
	    insertLogStatement=db.getConn().prepareStatement(logUpdateHostStatement);
	    insertLogStatement.setString(1,hostName);
	    insertLogStatement.setString(2,parameterBag.user.getDN());
	    insertLogStatement.setString(3,parameterBag.message);
	    insertLogStatement.setString(4,"Set Active to "+active);
	    insertLogStatement.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating activity log of host "+hostName);
	    System.out.println(getClass().getName()+" "+logUpdateHostStatement);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    insertLogStatement.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+logUpdateHostStatement);
	    System.out.println(getClass().getName()+" "+e);
	}    

	// Ok. Now we can update all primitive services on this host

	
	// now we have a list of services on hostName
	String updateService="UPDATE Services SET Active=? where PrimitiveService='Y' and HostName=?";
	PreparedStatement updateServiceStatement=null;
	try{
	    updateServiceStatement=db.getConn().prepareStatement(updateService);
	    updateServiceStatement.setBoolean(1,active);
	    updateServiceStatement.setString(2,hostName);
	    updateServiceStatement.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating activity log for primitive services on host "+hostName);
	    System.out.println(getClass().getName()+" "+updateService);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    updateServiceStatement.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+updateService);
	    System.out.println(getClass().getName()+" "+e);
	}
	



	// mark this activity in log
	PreparedStatement logUpdateHostPreparedStatement=null;
	try{
	    logUpdateHostPreparedStatement=db.getConn().prepareStatement(logUpdateHostStatement);
	    logUpdateHostPreparedStatement.setString(1,hostName);
	    logUpdateHostPreparedStatement.setString(2,parameterBag.user.getDN());
	    logUpdateHostPreparedStatement.setString(3,parameterBag.message);
	    logUpdateHostPreparedStatement.setString(4,"primitive services on host "+hostName+" set to Active="+active);
	    logUpdateHostPreparedStatement.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating activity log for services on host "+hostName);
	    System.out.println(getClass().getName()+" "+logUpdateHostStatement);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    logUpdateHostPreparedStatement.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to clos prepared statement");
	    System.out.println(getClass().getName()+" "+logUpdateHostStatement);
	    System.out.println(getClass().getName()+" "+e);
	}	    

	return result;
    }

    public String updateHostPage(){
	String result="";
	if(updateHost()){
	    result=result+"Host "+hostName+" has been updated<br>";
	}else{
	    result=result+"Update of host "+hostName+" failed<br>";
	}
	result=result+linkToHostDetails(hostName).toHtml();
	return result;
    }
	    

    public boolean updateHost(){
	boolean result=true;
	String updateHostStatement="UPDATE Hosts SET HostName=?, ExternalUrl=?,ip=?,Active=?, ReadOnly=? HostStatus=? WHERE dbid=?"; 
	PreparedStatement updateHostPreparedStatement= null;
	try{
	    updateHostPreparedStatement= db.getConn().prepareStatement(updateHostStatement);
	    updateHostPreparedStatement.setString(1,this.getHostName());
	    updateHostPreparedStatement.setString(3,this.getExternalUrl());
	    updateHostPreparedStatement.setBoolean(4,this.getActive());
	    updateHostPreparedStatement.setBoolean(5,this.getReadOnly());

	    updateHostPreparedStatement.setString(6,hostStatus.toString());
	    updateHostPreparedStatement.setInt(7,id);
	    updateHostPreparedStatement.executeUpdate();
	    result=true;
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating host in database");
	    System.out.println(getClass().getName()+" "+updateHostStatement);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    updateHostPreparedStatement.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+updateHostStatement);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	return result;
    }

    public ProbeStatus getHostPrimitivesStatus(){
	return this.hostPrimitivesStatus;
    }
    public void setHostPrimitivesStatus(ProbeStatus inputVar){
	this.hostPrimitivesStatus=inputVar;
    }
    public void updateHostPrimitivesStatus(ProbeStatus inputVar){
	if(inputVar.statusLevel()>this.getHostPrimitivesStatus().statusLevel()){
	    this.setHostPrimitivesStatus(inputVar);
	}
    }

    public ProbeStatus getHostStatus(){
	return this.hostStatus;
    }
    public void setHostStatus(ProbeStatus inputVar){
	this.hostStatus=inputVar;
    }
    public String getExternalUrl(){
	return this.externalUrl;
    }
    public void setExternalUrl(String externalUrl){
	this.externalUrl=externalUrl;
    }
    public String getIp(){
	return ip;
    }
    public void setIp(String ip){
	this.ip=ip;
    }
    public String getHostName(){
	return hostName;
    }
    public void setHostName(String hostName){
	this.hostName=hostName;
    }
    public void setSiteName(String siteName){
    	this.siteName=siteName;
     }
    public String getSiteName(){
     	return siteName;
    }
    // public void setCloudName(String cloudName){
    // 	this.cloudName=cloudName;
    // }
    // public String getCloudName(){
    // 	return cloudName;
    // }
    public void setActive(String activeString){
	if("Y".equals(activeString)||"y".equals(activeString)||"Yes".equals(activeString)||"YES".equals(activeString)||"yes".equals(activeString)){
	    this.active=true;
	}else{
	    this.active=false;
	}
    }
    public void setActive(boolean active){
	this.active=active;
    }
    public boolean getActive(){
	return active;
    }
    public void setReadOnly(boolean inputVar){
	this.readOnly=inputVar;
    }
    public boolean getReadOnly(){
	return this.readOnly;
    }
    public void setId(int id){
	this.id=id;
    }
    public int getId(){
	return id;
    }
    public ProbeStatus getStatus(){
	return this.hostStatus;
    }
    public void setStatus(ProbeStatus status){
	this.hostStatus=status;
    }
    public boolean swapActive(){
	if(active){
	    return false;
	}else{
	    return true;
	}
    }

    public List<PrimitiveService> getListOfPrimitiveServices(){
	List<PrimitiveService> list=new  ArrayList<PrimitiveService>();

	String query="SELECT * FROM PrimitiveServicesHosts WHERE hostId=?";
	PreparedStatement queryPs= null;
	int failurePoint=0;
	int primitiveServiceDbid=-1;
	try{
	    failurePoint=1;
	    queryPs= db.getConn().prepareStatement(query);
	    failurePoint=2;
	    queryPs.setInt(1,this.getId());
	    failurePoint=3; 
	    ResultSet rs=queryPs.executeQuery();
	    failurePoint=4;
	    while (rs.next ()){
		failurePoint=5;
		primitiveServiceDbid=rs.getInt("dbid");
		failurePoint=6;
		PrimitiveService primitiveService=new PrimitiveService(this.parameterBag,this.db,primitiveServiceDbid);
		failurePoint=7;
		list.add(primitiveService);
		failurePoint=8;
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error when obtaining list of primitive services on a host");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" hostId="+this.getId());
	    System.out.println(getClass().getName()+" failurePoint="+failurePoint);
	    System.out.println(getClass().getName()+"  primitiveServiceDbid="+primitiveServiceDbid);
	    System.out.println(getClass().getName()+" "+e);	    
	}
	try{
	    queryPs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	    
	}	    
	return list;
    }
	

    public boolean hostBelongsToMatrix(int matrixId){
	// check if hosts belongs to matrix
	String query="SELECT hostId,matrixId FROM ServiceMatricesHosts WHERE hostId=? and matrixId=?";
	int numberOfRecordsFound=0;
	PreparedStatement queryPs= null;
	try{
	    queryPs= db.getConn().prepareStatement(query);
	    queryPs.setInt(1,this.getId());
	    queryPs.setInt(2,matrixId);	    
	    ResultSet rs=queryPs.executeQuery();
	    while (rs.next ()){
		numberOfRecordsFound=numberOfRecordsFound+1;
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error occured when obtaining matrix information for host ");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" hostId="+this.getId());
	    System.out.println(getClass().getName()+" matrixId="+matrixId);
	    System.out.println(getClass().getName()+" "+e);	    
	}
	try{
	    queryPs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	    
	}

	if(numberOfRecordsFound==0){
	    return false;
	}else{
	    return true;
	}
    }
    public boolean hostRunsPrimitiveService(PrimitiveServiceRecord  primitiveServiceRecord){
	return this.hostRunsPrimitiveService(primitiveServiceRecord.getPrimitiveServiceId());
    }
    public boolean hostRunsPrimitiveService(int primitiveServiceId){
	boolean result=false;
	String query="SELECT * FROM PrimitiveServicesHosts WHERE hostId=? AND primitiveServiceId=?";
	PreparedStatement queryPs= null;
	int numberOfRecordsFound=0;
	try{
	    queryPs= db.getConn().prepareStatement(query);
	    queryPs.setInt(1,this.getId());
	    queryPs.setInt(2,primitiveServiceId);	    
	    ResultSet rs=queryPs.executeQuery();
	    while (rs.next ()){
		numberOfRecordsFound=numberOfRecordsFound+1;
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error occured when obtaining primitive service information for host ");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" hostId="+this.getId());
	    System.out.println(getClass().getName()+" primitiveServiceId="+primitiveServiceId);
	    System.out.println(getClass().getName()+" "+e);	    
	}
	try{
	    queryPs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	    
	}
	
	if(numberOfRecordsFound>0){
	    result=true;
	}else{
	    result=false;
	}
	return result;
    }
    public void fillListsOfPrimitiveServicesRecords(){
	// fill lists 

	String query="SELECT * FROM PrimitiveServices";
	PreparedStatement queryPs=null;
	try{
	    queryPs= db.getConn().prepareStatement(query);
	    ResultSet rs=queryPs.executeQuery();
	    while (rs.next ()){
		int id=rs.getInt("primitiveServiceId");
		PrimitiveServiceRecord primitiveServiceRecord=new PrimitiveServiceRecord(this.parameterBag,this.db,id);
		if(this.hostRunsPrimitiveService(primitiveServiceRecord)){
		    this.primitivesRecordsOnThisHost.add(primitiveServiceRecord);
		}else{
		    this.primitivesRecordsNotOnThisHost.add(primitiveServiceRecord);
		}
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error occured when obtaining list of primitive service records ");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	    
	}
	try{
	    queryPs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	    
	}
    }
    public void fillPrimitiveStatus(){
	Iterator itr=this.getListOfPrimitiveServices().iterator();
	while(itr.hasNext()){
	    PrimitiveService primitiveService=(PrimitiveService)itr.next();
	    this.updateHostPrimitivesStatus(primitiveService.getServiceStatus());
	}
    }

    public boolean hostBelongsToSite(PerfSonarSite site){
	return this.hostBelongsToSite(site.getSiteId());
    }
    public boolean hostBelongsToSite(int siteId){
	// check if hosts belongs to site
	String query="SELECT hostId,siteId FROM SitesHosts WHERE hostId=? and siteId=?";
	int numberOfRecordsFound=0;
	PreparedStatement queryPs= null;
	try{
	    queryPs= db.getConn().prepareStatement(query);
	    queryPs.setInt(1,this.getId());
	    queryPs.setInt(2,siteId);	    
	    ResultSet rs=queryPs.executeQuery();
	    while (rs.next ()){
		numberOfRecordsFound=numberOfRecordsFound+1;
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error occured when obtaining site information for host ");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" hostId="+this.getId());
	    System.out.println(getClass().getName()+" siteId="+siteId);
	    System.out.println(getClass().getName()+" "+e);	    
	}
	try{
	    queryPs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	    
	}


	if(numberOfRecordsFound==0){
	    return false;
	}else{
	    return true;
	}
    }

    public HtmlLink getLinkHostToExternalUrl(){
	String linktext=this.hostName;
	String linkUrl=this.externalUrl;
	HtmlLink link=new HtmlLink(externalUrl,linktext);
	return link;
    }
    public HtmlLink getLinkSiteToExternalUrl(){
	String linktext="";
	String sitesOfThisHost=this.getSiteName();
	if (sitesOfThisHost.equals("")){
	    linktext=this.getHostName(); //siteName;
	}else{
	    linktext=sitesOfThisHost;
	}
	String linkUrl=this.getExternalUrl();
	HtmlLink link=new HtmlLink(externalUrl,linktext);
	return link;
    }
    public HtmlLink linkToListOfHosts(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("List of Hosts")  );
	paramBagLocal.addParam("id",Integer.toString(this.id));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink linkToHostSavePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Save Host")  );
	paramBagLocal.addParam("id",Integer.toString(this.id));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink linkToHostEditPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Edit Host")  );
	paramBagLocal.addParam("id",Integer.toString(this.id));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink linkToHostClonePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Clone Host")  );
	paramBagLocal.addParam("id",Integer.toString(this.id));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink linkToHostDeletePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Delete Host")  );
	paramBagLocal.addParam("id",Integer.toString(this.id));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink linkToConfirmHostDeletePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Confirm Delete Host")  );
	paramBagLocal.addParam("id",Integer.toString(this.id));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    public HtmlLink linkToAddRemovePrimitiveServicesPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Add Remove Primitive Services to Host")  );
	paramBagLocal.addParam("id",Integer.toString(this.id));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    public HtmlLink linkToHostDetails(){
	return this.linkToHostDetails(this.hostName);
    }
    public HtmlLink linkToHostDetails(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("PerfSonar Host")  );
	paramBagLocal.addParam("hostName",hostName);
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public String oneLineInfo(){
	String result="Host: "+linkToHostDetails().toHtml();
	return result;
    }
    public HtmlTable fullHtmlTable(){
	HtmlTableCell hc=fullStatusCell();
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(hc);
	return ht;
    }
    public HtmlTableCell fullStatusCell(){
	String result="";
	result="<strong>Host Name: </strong>"+hostName +"<br>";
	if(active){
	    result=result+"<strong>The host is active</strong>";
	}else{
	    result=result+"<strong>The host is not active, tests have been disabled</strong>";
	}

	// if(this.parameterBag.userIsValid()){
	//     result=result+this.button.toHtml();
	// }

	result=result+"<strong>IP: </strong>"+ip +"<br>";
	HtmlLink linkToExtrenalInfo=new HtmlLink(externalUrl,"Link to external information page");

	result=result+"<strong>"+linkToExtrenalInfo.toHtml()+"</strong><br>";

	HtmlTableCell hc =new HtmlTableCell(result,hostStatus.color());
	hc.alignLeft();

	return hc;
    }
    public HtmlTableCell shortStatusCell(){
	HtmlLink link=linkToHostDetails();
	HtmlTableCell cell=new HtmlTableCell(link,hostStatus.color() );
	return cell;

    }
    public HtmlTable shortStatusTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(shortStatusCell());
	return ht;
    }
    public HtmlTableCell shortRowCell(int rowNumber){
	String cellText= Integer.toString(rowNumber);
	cellText=cellText+this.getLinkSiteToExternalUrl().toHtml()+"<br>("+ HtmlStringUtils.addFontTags(this.getHostName(),1)+")";
	HtmlTableCell cell =  new HtmlTableCell(cellText);
	return cell;
    }
    public HtmlTableCell shortColumnCell(int columnNumber){
	HtmlTableCell cell = new HtmlTableCell(Integer.toString(columnNumber));
	return cell;
    }

    // row for info table
    public  List<HtmlTableCell> infoTableRow(){
	List<HtmlTableCell> result=new ArrayList<HtmlTableCell>();

	HtmlLink link=this.linkToHostDetails();
	HtmlTableCell cell1=new  HtmlTableCell(link);
	cell1.alignLeft();
	result.add(cell1);

	HtmlTableCell emptyCell=new  HtmlTableCell("  ");	
	result.add(emptyCell);

	//String site=this.getSiteName();
	//HtmlTableCell siteCell=new  HtmlTableCell(site);
	//siteCell.alignLeft();
	//result.add(siteCell);

	result.add(emptyCell);


	HtmlLink link2edit=this.linkToHostEditPage("edit");
	HtmlTableCell cell2=new  HtmlTableCell(link2edit);
	result.add(cell2);

	result.add(emptyCell);

	//HtmlLink link2clone=this.linkToHostClonePage("clone");
	//HtmlTableCell cell3=new  HtmlTableCell(link2clone);
	//result.add(cell3);

	//result.add(emptyCell);

	String delete="delete";
	HtmlLink link2delete=this.linkToConfirmHostDeletePage("delete");
	HtmlTableCell cell4=new  HtmlTableCell(link2delete);
	result.add(cell4);

	result.add(emptyCell);

	HtmlLink link2addRemovePrimitives=this.linkToAddRemovePrimitiveServicesPage("add/remove primitive services");
	HtmlTableCell cell5=new  HtmlTableCell(link2addRemovePrimitives);
	result.add(cell5);

	return result;
    }
    public boolean removePrimitives(List<PrimitiveServiceRecord>listOfPrimitives){
	boolean result=true;
	activityLogger.log("remove primitive services from host "+this.getHostName());

	String query = "SELECT * FROM PrimitiveServicesHosts  WHERE hostId=? AND primitiveServiceId=? ";
	String query2 = "DELETE FROM PrimitiveServicesHosts  WHERE hostId=? AND primitiveServiceId=? ";
	String query3 = "DELETE from Services where dbid=?";
	String query4 = "DELETE from MetricRecordSummary where dbid=?";
	String query5 = "DELETE from MetricRecord where dbid=?";

	PreparedStatement queryPS=null;
	PreparedStatement query2PS=null;
	PreparedStatement query3PS=null;
	PreparedStatement query4PS=null;
	PreparedStatement query5PS=null;

	PrimitiveServiceRecord primitiveServiceRecord=null;
	int dbid=-1;
	try{
	    queryPS  =db.getConn().prepareStatement(query);
	    query2PS =db.getConn().prepareStatement(query2);
	    query3PS =db.getConn().prepareStatement(query3);
	    query4PS =db.getConn().prepareStatement(query4);
	    query5PS =db.getConn().prepareStatement(query5);

	    Iterator itr=listOfPrimitives.iterator();
	    while(itr.hasNext()){
		primitiveServiceRecord=(PrimitiveServiceRecord)itr.next();
		queryPS.setInt(1,this.getId());
		queryPS.setInt(2,primitiveServiceRecord.getPrimitiveServiceId());

		ResultSet rs=queryPS.executeQuery();

		while (rs.next ()){
		    dbid = rs.getInt("dbid");

		    query3PS= db.getConn().prepareStatement(query3);
		    query3PS.setInt(1,dbid);
		    query3PS.executeUpdate();

		    query4PS= db.getConn().prepareStatement(query4);
		    query4PS.setInt(1,dbid);
		    query4PS.executeUpdate();

		    query5PS= db.getConn().prepareStatement(query5);
		    query5PS.setInt(1,dbid);
		    query5PS.executeUpdate();
		}

		query2PS.setInt(1,this.getId());
		query2PS.setInt(2,primitiveServiceRecord.getPrimitiveServiceId());
		query2PS.executeUpdate();
	    }

	}catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting primitive service");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+query2);
		System.out.println(getClass().getName()+" "+query3);
		System.out.println(getClass().getName()+" "+query4);
		System.out.println(getClass().getName()+" "+query5);
		System.out.println(getClass().getName()+" host id="+this.getId());
		System.out.println(getClass().getName()+" site id="+primitiveServiceRecord.getPrimitiveServiceId());
		System.out.println(getClass().getName()+" dbid="+dbid);
		System.out.println(getClass().getName()+" "+e);
		result=false;
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    query2PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    query3PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query3);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    query4PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query4);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    query5PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query5);
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }

    public boolean addPrimitives(List<PrimitiveServiceRecord>listOfPrimitives){
	boolean result=true;
	activityLogger.log("add primitive services to host id="+this.getId());

	String query="INSERT INTO PrimitiveServicesHosts (hostId,primitiveServiceId,dbid) VALUES(?,?,?)";
	String query1="INSERT INTO Services (MetricName,HostName,ProbeCommand,ProbeId,ServiceDescription,CheckInterval,ProbeRunning,SchedulerName,matrixId,Active,LastCheckTime,NextCheckTime,PrimitiveService) VALUES(?,?,?,?,?,?,?,?,?,?,NOW(),NOW(),'Y')";
	

	Iterator itr=listOfPrimitives.iterator();
	while(itr.hasNext()){
	    PrimitiveServiceRecord primitiveServiceRecord=(PrimitiveServiceRecord)itr.next();

	    PreparedStatement query1PS=null;

	    String probeCommand="";
	    String probeId="";
	    int idOfInsertedService=-1;

	    try{
		query1PS= db.getConn().prepareStatement(query1);
		query1PS.setString(  1,primitiveServiceRecord.getMetricName());
		query1PS.setString(  2,this.getHostName());
		
		probeCommand=primitiveServiceRecord.getProbeCommandSample();
		probeCommand=probeCommand.replace("{HOST_NAME}",this.getHostName());
		probeCommand=probeCommand.replace("{HOST_IP}",this.getIp());
		query1PS.setString(  3,probeCommand);
		
		probeId=primitiveServiceRecord.getServiceDescription()+"_"+this.getHostName();
		query1PS.setString(  4,probeId);
		
		query1PS.setString(  5,primitiveServiceRecord.getServiceDescription());
		query1PS.setInt(     6,10);
		query1PS.setString(  7,"N");	    
		query1PS.setString(  8,"MAIN");
		query1PS.setInt(     9,-1);
		query1PS.setBoolean(10,this.active);
		query1PS.executeUpdate();

		activityLogger.log("service "+primitiveServiceRecord.getServiceName()+" added to host "+this.getHostName());
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when inserting primitive service id into Services");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" service="+primitiveServiceRecord.getMetricName());
		System.out.println(getClass().getName()+" host="+this.getHostName() );
		System.out.println(getClass().getName()+" command="+primitiveServiceRecord.getMetricName());
		System.out.println(getClass().getName()+" interval=10");
		System.out.println(getClass().getName()+" probeRunning=N");
		System.out.println(getClass().getName()+" schedulerName=MAIN");
		System.out.println(getClass().getName()+" matrixId=-1");
		System.out.println(getClass().getName()+" active="+this.active);
		System.out.println(getClass().getName()+" "+e);
		result=false;
	    }

	    try{
		ResultSet rs=query1PS.getGeneratedKeys();
		while (rs.next ()){
		idOfInsertedService= rs.getInt(1);
		}
	    }catch (Exception e){
		System.out.println(getClass().getName()+" could not obtain key of newly inserted primitive service");
		System.out.println(getClass().getName()+" "+e);
		result=false;
	    }
	    
	    try{
		query1PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query1);
		System.out.println(getClass().getName()+" "+e);
	    }
	    


	    if(idOfInsertedService<1){
		System.out.println(getClass().getName()+" failed to add service "+primitiveServiceRecord.getServiceName()+" to host "+this.getHostName());
	    }

	    PreparedStatement queryPS= null;
	    try{
		queryPS= db.getConn().prepareStatement(query);
		queryPS.setInt(1,this.getId());
		queryPS.setInt(2,primitiveServiceRecord.getPrimitiveServiceId());
		queryPS.setInt(3,idOfInsertedService);
		queryPS.executeUpdate();
		activityLogger.log("service "+primitiveServiceRecord.getServiceName()+" added to host "+this.getHostName());
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when inserting host and service id into PrimitiveServicesHosts");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" dbid="+idOfInsertedService);
		System.out.println(getClass().getName()+" host id="+this.getId());
		System.out.println(getClass().getName()+" site id="+primitiveServiceRecord.getPrimitiveServiceId());
		System.out.println(getClass().getName()+" "+e);
		result=false;
	    }
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }

	}
	return result;
    }
    public String removePrimitivesPage(){
	String result="<H1>Remove Primitive Services from Host "+this.getHostName()+"</H1>";
	result=result+"<br>";
	result=result+"<br>";
	if(!this.getReadOnly()){
	    result=result+"<strong>The following services will be removed from host:</strong><br>";
	    List<PrimitiveServiceRecord>listOfPrimitives=new ArrayList<PrimitiveServiceRecord>();
	    Iterator itr = this.parameterBag.listOfIds.iterator();
	    while(itr.hasNext()){
		Integer integerPrimitiveId=(Integer)itr.next();
		int primitiveId=integerPrimitiveId.intValue();
		PrimitiveServiceRecord primitiveServiceRecord=new PrimitiveServiceRecord(this.parameterBag,this.db,primitiveId);
		listOfPrimitives.add(primitiveServiceRecord);
		result=result+primitiveServiceRecord.getServiceName()+"<br>";
	    }

	    boolean deleteResultOk=this.removePrimitives(listOfPrimitives);

	    if(deleteResultOk){
		result=result+"<strong>Services succesfully removed from host</strong><br>";
	    }else{
		result=result+"<strong>Failed to remove services from host, check the logs!</strong><br>";
	    }

	    result=result+"<br>";
	    result=result+"<br>";
	    result=result+this.linkToAddRemovePrimitiveServicesPage("<strong>Go back to add/remove services form</strong>")+"<br>";
		
	}else{
	    result=result+"<strong>Host is read only, you cannot remove services from it</strong><br>";
	}
	
	result=result+"<br>";
	result=result+this.linkToListOfHosts("<strong>Click here to return to the list of hosts</strong>");	
	return result;
    }
    public String addPrimitivesPage(){
	String result="<H1>Add primitive services to host "+this.getHostName()+"</H1>";
	result=result+"<br>";
	result=result+"<br>";
	if(!this.getReadOnly()){
	    result=result+"<strong>The following services will be added to host:</strong><br>";
	    List<PrimitiveServiceRecord>listOfPrimitives=new ArrayList<PrimitiveServiceRecord>();
	    Iterator itr = this.parameterBag.listOfIds.iterator();
	    while(itr.hasNext()){
		Integer integerPrimitiveId=(Integer)itr.next();
		int primitiveId=integerPrimitiveId.intValue();
		PrimitiveServiceRecord primitiveServiceRecord=new PrimitiveServiceRecord(this.parameterBag,this.db,primitiveId);
		listOfPrimitives.add(primitiveServiceRecord);
		result=result+primitiveServiceRecord.getServiceName()+"<br>";
	    }
	    this.addPrimitives(listOfPrimitives);
	    result=result+"<br>";
	    result=result+"<br>";
	    result=result+this.linkToAddRemovePrimitiveServicesPage("<strong>Go back to add/remove services form</strong>")+"<br>";
	}else{
	    result=result+"<strong>Host is read only, you cannot add services to it</strong><br>";
	}
	
	result=result+"<br>";
	result=result+this.linkToListOfHosts("<strong>Click here to return to the list of hosts</strong>");	
	return result;
    }

    public String removePrimitivesForm(){
	String form="<form>";

	Iterator itr=this.primitivesRecordsOnThisHost.iterator();
	while (itr.hasNext()){
	    PrimitiveServiceRecord primitiveServiceRecord=(PrimitiveServiceRecord)itr.next();
	    String serviceDescription=primitiveServiceRecord.getServiceDescription();
	    int primitiveServiceId=primitiveServiceRecord.getPrimitiveServiceId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+primitiveServiceId+"\">"+serviceDescription+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Remove Primitives from Host")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Remove Selected Services\" /><br>";
	form=form+"</form>";
	return form;
    }
    public String addPrimitivesForm(){
	String form="<form>";

	Iterator itr=this.primitivesRecordsNotOnThisHost.iterator();
	while (itr.hasNext()){
	    PrimitiveServiceRecord primitiveServiceRecord=(PrimitiveServiceRecord)itr.next();
	    String serviceDescription=primitiveServiceRecord.getServiceDescription();
	    int primitiveServiceId=primitiveServiceRecord.getPrimitiveServiceId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+primitiveServiceId+"\">"+serviceDescription+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Primitives to Host")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Add Selected Services\" /><br>";
	form=form+"</form>";
	return form;
    }

    public HtmlTable addRemovePrimitivesTable(){
	this.fillListsOfPrimitiveServicesRecords();

	HtmlTable ht=new HtmlTable(2);
	ht.addCell("Primitive Services running on this host");
	ht.addCell("Primitive Services not running on this host");

	String form1=this.removePrimitivesForm();
	HtmlTableCell formCell1=new HtmlTableCell(form1);
	formCell1.alignLeft();
	ht.addCell(formCell1);

	String form=this.addPrimitivesForm();
	HtmlTableCell formCell=new HtmlTableCell(form);
	formCell.alignLeft();
	ht.addCell(formCell);
	return ht;
    }

    public String addRemovePrimitiveServicesToHostPage(){
	String result="<H1>Add or Remove Primitive Services to Host "+this.getHostName()+"</h1>";
	result=result+"<br>";
	result=result+this.addRemovePrimitivesTable().toHtml();
	result=result+"<br>";
	result=result+"<br>";
	result=result+this.linkToListOfHosts("<strong>Go back to the list of hosts</strong>")+"<br>";
	return result;
    }

    public String insertNewHostPage(){
	String result="<H1>Insert new PerfSonar Host</h1>";
	result=result+"<br>";
	this.setHostName(this.parameterBag.hostName);
	this.setIp(this.parameterBag.ip);
	this.setExternalUrl(this.parameterBag.externalUrl);
	this.setActive(true);
	this.setReadOnly(false);
	boolean insertResult=this.insertHost();
	if(insertResult){
	    result=result+"<string>New host "+this.getHostName()+" has been created</strong><br>";
	}else{
	    result=result+"<string>Error occured while creating new host "+this.getHostName()+" , check the logs</strong><br>";
	}
	return result;
    }
    public String createNewHostPage(){
	String result="<H1>Create new PerfSonar Host</h1>";
	result=result+"<br>";
	result=result+this.editHostForm();
	return result;
    }


    public HtmlTable tableOfPrimitiveServices(){
	HtmlTable htmlTable=new HtmlTable(5);
	
	Iterator iter=this.getListOfPrimitiveServices().iterator();
	while(iter.hasNext()){
	    PrimitiveService primitiveService=(PrimitiveService)iter.next();
	    HtmlTableCell cell=primitiveService.shortStatusCell();
	    htmlTable.addCell(cell);
	}
	return htmlTable;
    }

    public void setSortingCriteria(String criteria){
	// normally : hostName, siteName
	this.sortingCriteria=criteria;
    }
    public String getSortingCriteria(){
	return this.sortingCriteria;
    }

    private String invertHostname(String inputHostName){
	String outputHostName="";

	String[] words = inputHostName.split ("\\.");

	for (int i=0; i < words.length; i++){
	    outputHostName="."+words[i]+outputHostName;
	}
	outputHostName="@#$"+outputHostName;
	outputHostName=outputHostName.replace("@#$.","");
	return outputHostName;
    }

    public int compareTo(Object obj) {
	PerfSonarHost otherHost=(PerfSonarHost)obj;

	if(this.sortingCriteria.equals("siteName")){

	    String thisSiteName=this.getSiteName().replace("BNL","AAAAABNL");

	    String otherSiteName=otherHost.getSiteName().replace("BNL","AAAABNL");
	    return thisSiteName.compareTo(otherSiteName);
	}else{
	    String thisHostname=this.getHostName();
	    String otherHostName=otherHost.getHostName();

	    String invertedThisHostName=this.invertHostname(thisHostname);
	    String invertedOtherHostName=this.invertHostname(otherHostName);
	    

	    return invertedThisHostName.compareTo(invertedOtherHostName);
	}
    }
    

}
