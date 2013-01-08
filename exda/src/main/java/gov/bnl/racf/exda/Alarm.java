package gov.bnl.racf.exda;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.io.*;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletContext;


import javax.security.cert.X509Certificate;
import java.security.cert.*;

import java.net.InetAddress;
import java.sql.*;

import java.util.Collections;
import org.json.simple.JSONObject;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Alarm implements Comparable
{  

    HttpServletRequest request=null;

    private DbConnector db=null;
    private ParameterBag parameterBag=null;

    private int alarmId=-1;
    private String alarmName="";
    private String alarmDescription="";
    private int checkInterval=10;
    private int notificationInterval=1440;
    private Timestamp lastFailureTime= new Timestamp (0);
    private Timestamp lastNotificationTime=new Timestamp(0);
    private Timestamp lastCheckTime=null;
    private Timestamp sendAlertAfter = new Timestamp(0);

    private int consecutiveOK = 0;
    private int consecutiveNotOK = 0;
    private int consecutiveOKCut = 3;
    private int consecutiveNotOKCut = 3;    


    private ProbeStatus status = null;

    private int numberOfChannelsForWarning=0;
    private int numberOfChannelsForCritical=0;
    private int numberOfChannelsForUnknown=0;  

    private String alarmText="";
    private String alarmUrl="";

    private String sendResult = "";

    private ListOfUsers listOfUsers=null;
    private List<Integer>listOfMatrixIds=new ArrayList<Integer>();
    private List<Integer>listOfSiteIds=new ArrayList<Integer>();

    private List<Integer>listOfUserIds=new ArrayList<Integer>(); // users in alarm
    private List<Integer>listOfUserIdsNotInAlarm=new ArrayList<Integer>();
    private List<Integer>listOfAllUserIds=new ArrayList<Integer>();

    private List<Integer>listOfServiceIds=new ArrayList<Integer>();

    private List<Integer>listOfSiteIdsNotInAlarm=new ArrayList<Integer>();
    private List<PerfSonarSite>listOfSitesInAlarm=new ArrayList<PerfSonarSite>();
    private List<PerfSonarSite>listOfSitesNotInAlarm=new ArrayList<PerfSonarSite>();

    List<AlarmHistoryRecord> history=new ArrayList<AlarmHistoryRecord>();

    // for debugging
    PrintWriter out=null;

    public Alarm(ParameterBag parameterBag,DbConnector inputDb){
	this.db=inputDb;
	this.parameterBag=parameterBag;
    }

    public Alarm(ParameterBag parameterBag,DbConnector inputDb,int alarmId){
	this.db=inputDb;
	this.parameterBag=parameterBag;
	this.setAlarmId(alarmId);

	this.load();
    }
    // ==== end of constructors === //

    // === load, save, insert, delete methods ===//
    public boolean load(){
	// load Alarm, assumes that alarmId has been set
	boolean result=true;
	String query="SELECT * FROM Alarms where alarmId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getAlarmId());
	    ResultSet rs=queryPS.executeQuery();
	    result=this.unpackResultSet(rs);
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load alarm by query");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}	
	// === get list of matrices which belong to this Alarm
	String query2="SELECT * FROM ServiceMatricesAlarms where alarmId=?";
	PreparedStatement queryPS2=null;
	this.listOfMatrixIds.clear();
	try{
	    queryPS2= db.getConn().prepareStatement(query2);
	    queryPS2.setInt(1,this.getAlarmId());	
	    ResultSet rs=queryPS2.executeQuery();
	    while(rs.next()){
		int matrixId=rs.getInt("matrixId");
		this.listOfMatrixIds.add(new Integer(matrixId));
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load matrices for alarm");
	    System.out.println(getClass().getName()+" query2="+query2);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS2.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" query2="+query2);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}
	// === get list of sites which belong to this Alarm
	String query3="SELECT * FROM SitesAlarms where alarmId=?";
	PreparedStatement queryPS3=null;
	try{
	    queryPS3= db.getConn().prepareStatement(query3);
	    queryPS3.setInt(1,this.getAlarmId());	
	    ResultSet rs=queryPS3.executeQuery();
	    while(rs.next()){
		int siteId=rs.getInt("siteId");
		this.listOfSiteIds.add(new Integer(siteId));
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load sites for alarm");
	    System.out.println(getClass().getName()+" query3="+query3);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS3.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" query3="+query3);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}

	// === get list of users assigned to this Alarm
	result=result & this.loadUsersFromDatabase();
	// === get list of channels assigned to this Alarm
	result=result & this.loadPrimitiveServicesFromDatabase();
	return result;	
    }
    public boolean loadUsersFromDatabase(){
	boolean result=true;
	String query4="SELECT * FROM UsersAlarms where alarmId=?";
	PreparedStatement queryPS4=null;
	this.listOfUserIds.clear();
	try{
	    queryPS4= db.getConn().prepareStatement(query4);
	    queryPS4.setInt(1,this.getAlarmId());	
	    ResultSet rs=queryPS4.executeQuery();
	    while(rs.next()){
		int userId=rs.getInt("UserId");
		this.listOfUserIds.add(new Integer(userId));
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load users for alarm");
	    System.out.println(getClass().getName()+" query4="+query4);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS4.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" query4="+query4);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}	
	return result;
    }
    public boolean loadAllUserIds(){
	boolean result=true;
	String query4="SELECT userId FROM Users";
	PreparedStatement queryPS4=null;
	this.listOfAllUserIds.clear();
	try{
	    queryPS4= db.getConn().prepareStatement(query4);
	    ResultSet rs=queryPS4.executeQuery();
	    while(rs.next()){
		int userId=rs.getInt("UserId");
		this.listOfAllUserIds.add(new Integer(userId));
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load users for alarm");
	    System.out.println(getClass().getName()+" query4="+query4);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS4.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" query4="+query4);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}	
	return result;
    }
    public boolean loadUserIdsNotInAlarm(){
	boolean result=true;
	Iterator iter = this.listOfAllUserIds.iterator();
	this.listOfUserIdsNotInAlarm.clear();
	while(iter.hasNext()){
	    Integer userIdInteger = (Integer)iter.next();
	    
	    if(this.listOfUserIds.contains(userIdInteger)){
		//do nothing
	    }else{
		this.listOfUserIdsNotInAlarm.add(userIdInteger);
	    }
	}
	return result;
    }
    public List<User> getListOfUsersNotInThisAlarm(){
	List<User> resultList=new ArrayList<User>();
	resultList.clear();
	Iterator iter = this.listOfUserIdsNotInAlarm.iterator();
	while(iter.hasNext()){
	    User user = new User(this.parameterBag,this.db,((Integer)iter.next()).intValue());
	    resultList.add(user);
	}
	return resultList;
    }
    public boolean loadPrimitiveServicesFromDatabase(){
	boolean result=true;
	String query5="SELECT * FROM ServicesAlarms where alarmId=?";
	PreparedStatement queryPS5=null;
	this.listOfServiceIds.clear();
	try{
	    queryPS5= db.getConn().prepareStatement(query5);
	    queryPS5.setInt(1,this.getAlarmId());	
	    ResultSet rs=queryPS5.executeQuery();
	    while(rs.next()){
		int dbid=rs.getInt("dbid");
		this.listOfServiceIds.add(new Integer(dbid));
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load services for alarm");
	    System.out.println(getClass().getName()+" query5="+query5);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS5.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" query5="+query5);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}
	return result;
    }
    // === end of load method === //
    
    // === delete method and its dependencies === //
    private boolean delete(){
	boolean result = true;
	boolean deleteUsersResult = this.deleteUsers();
	if(!deleteUsersResult){
	    System.out.println((new Date())+" "+getClass().getName()+" failed to delete users");
	}
	boolean deleteSitesResult = this.deleteSites();
	if(!deleteSitesResult){
	    System.out.println((new Date())+" "+getClass().getName()+" failed to delete sites");
	}
	boolean deleteMatricesResult =  this.deleteMatrices();
	if(!deleteMatricesResult){
	    System.out.println((new Date())+" "+getClass().getName()+" failed to delete matrices");
	}
	boolean deleteServicesResult = this.deleteServices();
	if(!deleteServicesResult){
	    System.out.println((new Date())+" "+getClass().getName()+" failed to delete Services");
	}
	boolean deleteAlarmsResult =  this.deleteAlarms();
	if(!deleteAlarmsResult){
	    System.out.println((new Date())+" "+getClass().getName()+" failed to delete Alarms");
	}
	result=result & deleteUsersResult;
	result=result & deleteSitesResult;
	result=result & deleteMatricesResult;
	result=result & deleteServicesResult;
	result=result & deleteAlarmsResult;
	return result;
    }
    private boolean executeDelete(String query){
	boolean result=true;
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getAlarmId());	
	    queryPS.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to execute delete command for an alarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement in executeDelete");
		System.out.println(getClass().getName()+" query="+query);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}
	return result;
    }
    private boolean deleteUsers(){
	boolean result=true;
	String query="DELETE FROM UsersAlarms WHERE alarmId=?";
	result=this.executeDelete(query);
	return result;
    }
    private boolean deleteSites(){
	boolean result=true;
	String query="DELETE FROM SitesAlarms WHERE alarmId=?";
	result=this.executeDelete(query);
	return result;
    }
    private boolean deleteMatrices(){
	boolean result=true;
	String query="DELETE FROM ServiceMatricesAlarms WHERE alarmId=?";
	result=this.executeDelete(query);
	return result;
    }
    private boolean deleteServices(){
	boolean result=true;
	String query="DELETE FROM ServicesAlarms WHERE alarmId=?";
	result=this.executeDelete(query);
	return result;
    }
    private boolean deleteAlarms(){
	boolean result=true;
	String query="DELETE FROM Alarms WHERE alarmId=?";
	result=this.executeDelete(query);
	return result;
    }
    // === insert method and its dependencies === //
    private boolean insert(){
	boolean result=true;
	boolean iUsers=this.insertUsers();
	System.out.println(getClass().getName()+" iUsers="+iUsers);
    	result=result & iUsers;

	boolean iSites = this.insertSites();
	System.out.println(getClass().getName()+" iSites="+iSites);
    	result=result & iSites;

	boolean iMatrices = this.insertMatrices();
	System.out.println(getClass().getName()+" iMatrices="+iMatrices);
	result=result & iMatrices;

	boolean iServices = this.insertServices();
	System.out.println(getClass().getName()+" iServices="+iServices);
	result=result & iServices;

	boolean iAlarm = this.insertAlarm();
	System.out.println(getClass().getName()+" iAlarm="+iAlarm);
	result=result & iAlarm;	

	System.out.println(getClass().getName()+" insert result="+result);
	return result;
    }
    private boolean insertUsers(){
	boolean result=true;
	String query="INSERT INTO UsersAlarms (alarmId,UserId) VALUES(?,?)";
	result=this.insertIds(query,this.listOfUserIds);
	return result;
    }
    private boolean insertSites(){
	boolean result=true;
	String query="INSERT INTO SitesAlarms (alarmId,siteId) VALUES(?,?)";
	result=this.insertIds(query,this.listOfSiteIds);
	return result;
    }

    private boolean insertMatrices(){
	boolean result=true;
	String query="INSERT INTO ServiceMatricesAlarms (alarmId,matrixId) VALUES(?,?)";
	result=this.insertIds(query,this.listOfMatrixIds);
	return result;
    }
    private boolean insertServices(){
	boolean result=true;
	String query="INSERT INTO ServiceMatricesAlarms (alarmId,matrixId) VALUES(?,?)";
	result=this.insertIds(query,this.listOfServiceIds);
	return result;
    }
    private boolean insertAlarm(){
	boolean result=true;
	String query="INSERT INTO Alarms (name,description,CheckInterval,NotificationInterval,LastFailureTime,LastNotificationTime,status,NumberOfChannelsForWarning,NumberOfChannelsForCritical,NumberOfChannelsForUnknown,statusLevel,LastCheckTime,AlarmUrl,AlarmText,sendAlertAfter) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    System.out.println(getClass().getName()+" this.getAlarmName()="+this.getAlarmName());
	    queryPS.setString(  1,this.getAlarmName());

	    System.out.println(getClass().getName()+" this.getAlarmDescription()="+this.getAlarmDescription());
	    queryPS.setString(  2,this.getAlarmDescription());

	    System.out.println(getClass().getName()+" this.getCheckInterval()="+this.getCheckInterval());
	    queryPS.setInt(     3,this.getCheckInterval());

	    System.out.println(getClass().getName()+" this.getNotificationInterval()="+this.getNotificationInterval());
	    queryPS.setInt(     4,this.getNotificationInterval());

	    System.out.println(getClass().getName()+" this.getLastFailureTime()="+this.getLastFailureTime());
	    queryPS.setTimestamp(5,this.getLastFailureTime());

	    System.out.println(getClass().getName()+" this.getLastNotificationTime()="+this.getLastNotificationTime());
	    queryPS.setTimestamp(6,this.getLastNotificationTime());

	    System.out.println(getClass().getName()+" this.getStatus().getStatusInt()="+this.getStatus().getStatusInt());
	    queryPS.setInt(     7,this.getStatus().getStatusInt());

	    System.out.println(getClass().getName()+" this.getNumberOfChannelsForWarning()="+this.getNumberOfChannelsForWarning());
	    queryPS.setInt(     8,this.getNumberOfChannelsForWarning());

	    System.out.println(getClass().getName()+" this.getNumberOfChannelsForCritical()="+this.getNumberOfChannelsForCritical());
	    queryPS.setInt(     9,this.getNumberOfChannelsForCritical());

	    System.out.println(getClass().getName()+" this.getNumberOfChannelsForUnknown()="+this.getNumberOfChannelsForUnknown());
	    queryPS.setInt(    10,this.getNumberOfChannelsForUnknown());

	    System.out.println(getClass().getName()+" this.getStatusLevel()="+this.getStatusLevel());
	    queryPS.setInt(    11,this.getStatusLevel());


	    System.out.println(getClass().getName()+" this.getLastCheckTime()="+this.getLastCheckTime());
	    queryPS.setTimestamp(12,this.getLastCheckTime());

	    System.out.println(getClass().getName()+" this.getAlarmUrl()="+this.getAlarmUrl());
	    queryPS.setString(13,this.getAlarmUrl());

	    System.out.println(getClass().getName()+" this.getAlarmText()="+this.getAlarmText());
	    queryPS.setString(14,this.getAlarmText());

	    queryPS.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to execute  insert for an alarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	// get the Id of the inserted alarm
	try{
	    ResultSet rs=queryPS.getGeneratedKeys();
	    while (rs.next ()){
		this.setAlarmId(rs.getInt(1));
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not obtain key of newly inserted alarm");
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	// since we have the key, we can create AlarmUrl
	

	// finally close the query
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement in insertAlarm");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	
	}
	

	this.setAlarmUrl(this.makeDefaultAlarmUrl());

	String query2="UPDATE Alarms SET AlarmUrl=? WHERE alarmId=?";
	PreparedStatement queryPS2=null;
	try{
	    queryPS2= db.getConn().prepareStatement(query2);	
	    queryPS2.setString(  1,this.getAlarmUrl());
	    queryPS2.setInt(2,this.getAlarmId());
	    queryPS2.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to insertAlarmId()");
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" getAlarmId()="+this.getAlarmId());
	    System.out.println(getClass().getName()+" "+e);	
	}
	// finally close the query
	try{
	    queryPS2.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement in insertAlarm");
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" "+e);	
	}	

	return result;	
    }

    private boolean insertIds(String query,  List<Integer>listOfIds){
	boolean result=true;
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getAlarmId());
	    Iterator iter = listOfIds.iterator();
	    while(iter.hasNext()){
		Integer idInteger= (Integer)iter.next();
		int id=idInteger.intValue();
		queryPS.setInt(2,id);
		queryPS.executeUpdate();
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to execute insert command for an alarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement in insertIds");
		System.out.println(getClass().getName()+" query="+query);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}	
	return result;
    }

    // === end of insert methods === //


    // === save method and its dependencies ===//
    public boolean save(){
	boolean result=true;
	result=result & this.deleteUsers();
	result=result & this.insertUsers();

	result=result & this.deleteSites();
	result=result & this.insertSites();

	result=result & this.deleteMatrices();
	result=result & this.insertMatrices();

	result=result & this.deleteServices();
	result=result & this.insertServices();

	result=result & this.saveAlarm();
	
	return result;
    }
    public boolean saveAlarm(){
	boolean result=true;
	String query="UPDATE Alarms SET name=?,description=?,CheckInterval=?,NotificationInterval=?,NumberOfChannelsForWarning=?,NumberOfChannelsForCritical=?,NumberOfChannelsForUnknown=?,AlarmUrl=?,AlarmText=?,consecutiveOKCut=?,consecutiveNotOKCut=?,consecutiveOK=?,consecutiveNotOK=? WHERE alarmId=?";

 
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setString(  1,this.getAlarmName());
	    queryPS.setString(  2,this.getAlarmDescription());
	    queryPS.setInt(     3,this.getCheckInterval());
	    queryPS.setInt(     4,this.getNotificationInterval());
	    queryPS.setInt(     5,this.getNumberOfChannelsForWarning());
	    queryPS.setInt(     6,this.getNumberOfChannelsForCritical());
	    queryPS.setInt(     7,this.getNumberOfChannelsForUnknown());
	    queryPS.setString(  8,this.getAlarmUrl());
	    queryPS.setString(  9,this.getAlarmText());

	    queryPS.setInt(     10,this.getConsecutiveOKCut());
	    queryPS.setInt(     11,this.getConsecutiveNotOKCut());

	    queryPS.setInt(     12,this.getConsecutiveOK());
	    queryPS.setInt(     13,this.getConsecutiveNotOK());

	    
	    queryPS.setInt(     14,this.getAlarmId());
	    queryPS.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to save an alarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());		    
	    System.out.println(getClass().getName()+" this.getAlarmName()="+this.getAlarmName());

	    System.out.println(getClass().getName()+" this.getAlarmDescription()="+ this.getAlarmDescription());
	    System.out.println(getClass().getName()+" this.getCheckInterval()="+ this.getCheckInterval());
	    System.out.println(getClass().getName()+" this.getNotificationInterval()="+ this.getNotificationInterval());
	    System.out.println(getClass().getName()+" this.getNumberOfChannelsForWarning()="+ this.getNumberOfChannelsForWarning());
	    System.out.println(getClass().getName()+" this.getNumberOfChannelsForCritical()="+ this.getNumberOfChannelsForCritical());
	    System.out.println(getClass().getName()+" this.getNumberOfChannelsForUnknown()="+ this.getNumberOfChannelsForUnknown());
	    System.out.println(getClass().getName()+" this.getStatusLevel()="+ this.getStatusLevel());
	    System.out.println(getClass().getName()+" this.getAlarmId()="+ this.getAlarmId());
	    System.out.println(getClass().getName()+" this.getAlarmUrl()="+ this.getAlarmUrl());
	    System.out.println(getClass().getName()+" this.getAlarmText()="+ this.getAlarmText());
	    System.out.println(getClass().getName()+" this.getConsecutiveOKCut()="+this.getConsecutiveOKCut());
	    System.out.println(getClass().getName()+" this.getConsecutiveNotOKCut()="+this.getConsecutiveNotOKCut());

	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}	    
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement in saveAlarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);	
	}

	return result;
    }



    // === unpack result set method ===//
    private boolean unpackResultSet(ResultSet rs){
	boolean result=true;
	try{
	     while(rs.next()){
		 this.setAlarmId(rs.getInt("alarmId"));
		 this.setAlarmName(rs.getString("name"));
		 this.setAlarmDescription(rs.getString("description"));
		 this.setCheckInterval(rs.getInt("CheckInterval"));
		 this.setNotificationInterval(rs.getInt("NotificationInterval"));
		 this.setLastFailureTime(rs.getTimestamp("LastFailureTime"));
		 this.setLastNotificationTime(rs.getTimestamp("LastNotificationTime"));
		 this.setLastCheckTime(rs.getTimestamp("LastCheckTime"));

		 int statusLevel=rs.getInt("statusLevel");

		 ProbeStatus status = new ProbeStatus( ProbeStatus.statusLevel2statusCode(statusLevel));

		 this.setStatus(status);

		 this.setNumberOfChannelsForWarning(rs.getInt("NumberOfChannelsForWarning"));
		 this.setNumberOfChannelsForCritical(rs.getInt("NumberOfChannelsForCritical"));
		 this.setNumberOfChannelsForUnknown(rs.getInt("NumberOfChannelsForUnknown"));

		 this.setAlarmUrl(rs.getString("AlarmUrl"));
		 this.setAlarmText(rs.getString("AlarmText"));

		 this.setConsecutiveOK(rs.getInt("consecutiveOK"));
		 this.setConsecutiveNotOK(rs.getInt("consecutiveNotOK"));
		 this.setConsecutiveOKCut(rs.getInt("consecutiveOKCut"));
		 this.setConsecutiveNotOKCut(rs.getInt("consecutiveNotOKCut"));
		 Timestamp ts=rs.getTimestamp("sendAlertAfter");

		 this.setSendAlertAfter(ts);


	     }
	}catch(Exception e){
	    result=false;
	    System.out.println(getClass().getName()+" error while unpacking result set");
	    System.out.println(getClass().getName()+" "+e);
	}	     
	return result;	
    }
    
    // === set and get methods === //
    public void setAlarmId(int inputVar){
	this.alarmId=inputVar;
    }
    public int getAlarmId(){
	return this.alarmId;
    }
    public String getAlarmName(){
	return this.alarmName;
    }
    public void setAlarmName(String inputVar){
	this.alarmName=inputVar;
    }
    public String getAlarmDescription(){
	return this.alarmDescription;
    }
    public void setAlarmDescription(String inputVar){
	this.alarmDescription=inputVar;
    }
    public void setAlarmUrl(String inputVar){
	this.alarmUrl=inputVar;
    }
    public String getAlarmUrl(){
	return this.alarmUrl;
    }
    public void setAlarmText(String inputVar){
	this.alarmText=inputVar;
    }
    public String getAlarmText(){
	return this.alarmText;
    }

    public int getCheckInterval(){
	return this.checkInterval;
    }
    public void setCheckInterval(int inputVar){
	this.checkInterval=inputVar;
    }
    public int getNotificationInterval(){
	return this.notificationInterval;
    }
    public void setNotificationInterval(int inputVar){
	this.notificationInterval=inputVar;
    }
    public Timestamp getLastFailureTime(){
	return this.lastFailureTime;
    }
    public void setLastFailureTime(Timestamp inputVar){
	this.lastFailureTime=inputVar;
    }
    public Timestamp getLastNotificationTime(){
	return this.lastNotificationTime;
    }
    public void setLastNotificationTime(Timestamp inputVar){
	this.lastNotificationTime=inputVar;
    }
    public Timestamp getLastCheckTime(){
	return this.lastCheckTime;
    }
    public void setLastCheckTime(Timestamp inputVar){
	this.lastCheckTime=inputVar;
    }

    public void setSendAlertAfter(Timestamp inputVar){
	this.sendAlertAfter=inputVar;
    }
    public Timestamp getSendAlertAfter(){
	return this.sendAlertAfter;
    }


    public void setStatus(ProbeStatus status){
	this.status=status;
    }
    public ProbeStatus getStatus(){
	return this.status;
    }
    
    public int getStatusLevel(){
	return this.status.statusLevel();
    }
    public int getNumberOfChannelsForWarning(){
	return this.numberOfChannelsForWarning;
    }
    public void setNumberOfChannelsForWarning(int inputVar){
	this.numberOfChannelsForWarning=inputVar;
    }
    public int getNumberOfChannelsForCritical(){
	return this.numberOfChannelsForCritical;
    }
    public void setNumberOfChannelsForCritical(int inputVar){
	this.numberOfChannelsForCritical=inputVar;
    }
    public int getNumberOfChannelsForUnknown(){
	return this.numberOfChannelsForUnknown;
    }
    public void setNumberOfChannelsForUnknown(int inputVar){
	this.numberOfChannelsForUnknown=inputVar;
    }

    public void setConsecutiveOK(int inputVar){
	this.consecutiveOK=inputVar;
    }
    public int getConsecutiveOK(){
	return this.consecutiveOK;
    }
    public void setConsecutiveNotOK(int inputVar){
	this.consecutiveNotOK=inputVar;
    }
    public int getConsecutiveNotOK(){
	return this.consecutiveNotOK;
    }
    public void setConsecutiveOKCut(int inputVar){
	this.consecutiveOKCut=inputVar;
    }
    public int getConsecutiveOKCut(){
	return this.consecutiveOKCut;
    }
    public void setConsecutiveNotOKCut(int inputVar){
	this.consecutiveNotOKCut=inputVar;
    }
    public int getConsecutiveNotOKCut(){
	return this.consecutiveNotOKCut;
    }

    public boolean notificationIsDue(){
	System.out.println(getClass().getName()+" we are in notificationIsDue");
	boolean result=false;
	Date now = new Date();

	Date nextNotificationTime = new Date(this.getSendAlertAfter().getTime());

	if(now.after(nextNotificationTime)){
	    result=true;
	}else{
	    // do nothing
	}
	if(result){
	    //System.out.println(getClass().getName()+" notificaion should be sent");
	}else{
	    //System.out.println(getClass().getName()+" no need to send notification");
	}
	return result;
    }
    public String getAlarmMessageText(){
	String message=this.getAlarmText()+"\n\n"+this.getAlarmDetails()+"\n\n"+"Link to alarm status page\n\n"+this.getAlarmUrl();
	return message;
    }
    public String getAlarmMessageTextForLogging(){
	HtmlLink linkToAlarmPage = new HtmlLink(this.getAlarmUrl(), "Link to alarm status page");
	String message=this.getAlarmText()+"\n\n"+this.getAlarmDetailsForLogging()+"\n\n"+linkToAlarmPage.toHtml();
	return message;
    }
    public String sendAlertToUser(User user){
	String result="";
	Date now = new Date();

	ParameterStore parameterStore = ParameterStore.getParameterStore();
	String hostname = parameterStore.getHostname();
	String hostAlias = parameterStore.getHostAlias();


	String to=user.getUserEmail();
	//String from = "perfsonar@perfsonar.usatlas.bnl.gov";
	String from = "perfsonar@"+hostAlias;
	String message="";

        String host = "rcf.rhic.bnl.gov";

	Properties props = new Properties();
	props.put("mail.smtp.host", host);
	props.put("mail.debug", "true");
        Session session = Session.getInstance(props);
	 try {
	     // Instantiatee a message
	     Message msg = new MimeMessage(session);
	     
	     //Set message attributes
	     msg.setFrom(new InternetAddress(from));
	     InternetAddress[] address = {new InternetAddress(to)};
	     msg.setRecipients(Message.RecipientType.TO, address);
	     msg.setSubject(this.alarmStatus().toString()+":"+this.getAlarmName());
	     msg.setSentDate(now);
	     
	     message=this.getAlarmMessageText()+"\n\n"+now;

	     msg.setText(message);
	     Transport.send(msg);
	     result=result+"mail sent to "+to+"\n";
	 }catch(MessagingException mex) {
	     mex.printStackTrace();
	 } 
	 // log message
	 String query="INSERT INTO AlarmsLog (alarmId,Message,Email,TimeSent) VALUES (?,?,?,NOW())";
	 PreparedStatement queryPS=null;
	 try{
	     queryPS= db.getConn().prepareStatement(query);
	     queryPS.setInt(  1,this.getAlarmId());
	     queryPS.setString(  2, message);
	     queryPS.setString(  3, to);
	     queryPS.executeUpdate();
	 }catch(Exception e){
	     System.out.println(getClass().getName()+" failed to log outgoing e-mail");
	     System.out.println(getClass().getName()+" query="+query);
	     System.out.println(getClass().getName()+" "+e);	
	 }
	 
	 
	 try{
	     queryPS.close();
	 }catch(Exception e){
	     System.out.println(getClass().getName()+" failed to close prepared statement in sendAlertToUser");
	     System.out.println(getClass().getName()+" query="+query);
	     System.out.println(getClass().getName()+" "+e);	
	 }
	 return result;
    }

    public void updateLastNotificationTime(){
	 String query="UPDATE Alarms SET lastNotificationTime=NOW() WHERE alarmId=?";
	 PreparedStatement queryPS=null;
	 try{
	     queryPS= db.getConn().prepareStatement(query);
	     queryPS.setInt(  1,this.getAlarmId());
	     queryPS.executeUpdate();
	 }catch(Exception e){
	     System.out.println(getClass().getName()+" failed to update last notification time");
	     System.out.println(getClass().getName()+" query="+query);
	     System.out.println(getClass().getName()+" "+e);	
	 }
	 
	 
	 try{
	     queryPS.close();
	 }catch(Exception e){
	     System.out.println(getClass().getName()+" failed to close prepared statement in updateLastNotificationTime");
	     System.out.println(getClass().getName()+" query="+query);
	     System.out.println(getClass().getName()+" "+e);	
	 }	
    }
    public String sendAlerts(){
	String result="";

	Iterator iter = this.getListOfUsersInThisAlarm().iterator();
	while(iter.hasNext()){
	    User user=(User)iter.next();
	    result=result+this.sendAlertToUser(user)+"\n";
	}
	return result;
    }

    public String updateConsecutiveCounts(){
	String result="";
	if(this.alarmStatus().isOK()){
	    int nConsecutive=this.getConsecutiveOK()+1;
	    this.setConsecutiveOK(nConsecutive);
	    this.setConsecutiveNotOK(0);
	}else{
	    int nConsecutive=this.getConsecutiveNotOK()+1;
	    this.setConsecutiveOK(0);
	    this.setConsecutiveNotOK(nConsecutive);	    
	}
	// write the numbers to database, simply save the alarm
	this.saveAlarm();
	//result=saveResult+"\n";
	return result;
    }


    public void updateSendNotificationTimeInDatabase(){
	 String query="UPDATE Alarms SET sendAlertAfter=? WHERE alarmId=?";
	 PreparedStatement queryPS=null;
	 try{
	     queryPS= db.getConn().prepareStatement(query);
	     queryPS.setTimestamp(  1,this.getSendAlertAfter());
	     queryPS.setInt(  2,this.getAlarmId());
	     queryPS.executeUpdate();
	 }catch(Exception e){
	     System.out.println(getClass().getName()+" failed to update sendAlertAfter time");
	     System.out.println(getClass().getName()+" query="+query);
	     System.out.println(getClass().getName()+" "+e);	
	 }
	 
	 
	 try{
	     queryPS.close();
	 }catch(Exception e){
	     System.out.println(getClass().getName()+" failed to close prepared statement in updateSendNotificationTimeInDatabase");
	     System.out.println(getClass().getName()+" query="+query);
	     System.out.println(getClass().getName()+" "+e);	
	 }	
    }

    public void setSendNotificationTimeToNow(){
	long timeNow = Calendar.getInstance().getTimeInMillis();
	java.sql.Timestamp ts = new java.sql.Timestamp(timeNow);
	this.setSendAlertAfter(ts);
	
	// now write it to database
	this.updateSendNotificationTimeInDatabase();	
    }

    public String setSendAlertsToNowPlusInterval(){
	String result="";
	long timeNow = Calendar.getInstance().getTimeInMillis();

	long notificationIntervalLong = (long)(this.getNotificationInterval()*1);
	timeNow = timeNow + 60*1000*notificationIntervalLong;

	java.sql.Timestamp ts = new java.sql.Timestamp(timeNow);
	this.setSendAlertAfter(ts);
	// now write it to database
	this.updateSendNotificationTimeInDatabase();		
	return result;
    }

    public String updateSendNotificationTime(){
	String result="";
	if(this.getConsecutiveOK()==this.getConsecutiveOKCut()){
	    // set sendAlertAfter to NOW()
	    this.setSendNotificationTimeToNow();
	}
	result=result+" Next notification time set to "+this.getSendAlertAfter()+"\n";
	return result;
    }

    public String doNotificationCheck(){
	String result="";
	// first order of business is to update consecutive counts
	result=result+this.updateConsecutiveCounts();

	// now update the notification time
	result=result+this.updateSendNotificationTime();

	// now we decide whether to send an alarm
	if(!this.alarmStatus().isOK()){
	    result=result+"Alarm is not OK\n";
	    if(this.getConsecutiveNotOK()>this.getConsecutiveNotOKCut()-1){
		if(this.notificationIsDue()){
		    result=result+"notification is due\n";

		    this.sendResult = this.sendAlerts();
		    result=result+sendResult;
		    result=result+this.setSendAlertsToNowPlusInterval();

		    this.logAlarm(true,"Notification sent");

		}else{
		    this.logAlarm(false, "Next notification will be sent after "+this.getSendAlertAfter());
		}
	    }else{
		// do nothing
		this.logAlarm(false,"Number of failures is below cut");
	    }
	}else{
	    result=result+"Alarm is OK";
	    this.logAlarm(false," ");
	}
	return result;
    }

    public void logAlarm(boolean messageSent,String comment){
	String query="INSERT INTO AlarmHistory (alarmId,Timestamp,Message,messageSent,status,consecutiveOK,consecutiveNotOK,Comment,sendResult) VALUES(?,NOW(),?,?,?,?,?,?,?)";
	PreparedStatement queryPS=null;
	
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(     1,this.getAlarmId()    );
	    //if(this.alarmStatus().isOK()){
	    //	queryPS.setString(2,"");
	    //}else{
	    queryPS.setString(2,this.getAlarmMessageTextForLogging());
		//}
	    queryPS.setBoolean(3,messageSent);
	    queryPS.setString(4,this.alarmStatus().toString());
	    queryPS.setInt(5,this.getConsecutiveOK());
	    queryPS.setInt(6,this.getConsecutiveNotOK());
	    queryPS.setString(7, comment);
	    queryPS.setString(8, this.sendResult);
	    queryPS.executeUpdate();
	}catch(Exception e){
	    Date now = new Date();
	    System.out.println(now+" "+getClass().getName()+" failed to insert alarm history record");
	    System.out.println(now+" "+getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    Date now = new Date();
	    System.out.println(now+" "+getClass().getName()+" failed to close prepared statement");
	    System.out.println(now+" "+getClass().getName()+" query="+query);
	    System.out.println(now+" "+getClass().getName()+" "+e);
	}
    }
    


    public void unpackParameterBag(){
	if(this.parameterBag.variableNotEmpty("notificationInterval")){
	    if(!this.parameterBag.notificationInterval.equals("")){
		this.setNotificationInterval(Integer.parseInt(this.parameterBag.notificationInterval));
	    }
	}
	//	if(this.parameterBag.variableNotEmpty("checkInterval")){
	//    if(!this.parameterBag.checkInterval.equals("")){
	//	this.setCheckInterval(Integer.parseInt(this.parameterBag.checkInterval));
	//    }
	//}

	if(this.parameterBag.variableNotEmpty("consecutiveOKCut")){
	    this.setConsecutiveOKCut(Integer.parseInt(this.parameterBag.consecutiveOKCut));
	}
	if(this.parameterBag.variableNotEmpty("consecutiveNotOKCut")){
	    this.setConsecutiveNotOKCut(Integer.parseInt(this.parameterBag.consecutiveNotOKCut));
	}

	if(this.parameterBag.variableNotEmpty("alarmDescription")){
	    this.setAlarmDescription(this.parameterBag.alarmDescription);
	}
	if(this.parameterBag.variableNotEmpty("alarmName")){
	    this.setAlarmName(this.parameterBag.alarmName);
	}
	if(this.parameterBag.variableNotEmpty("numberOfChannelsForWarning")){
	    this.setNumberOfChannelsForWarning(Integer.parseInt(this.parameterBag.numberOfChannelsForWarning));
	}
	if(this.parameterBag.variableNotEmpty("numberOfChannelsForCritical")){
	    this.setNumberOfChannelsForCritical(Integer.parseInt(this.parameterBag.numberOfChannelsForCritical));
	}
	//if(this.parameterBag.variableNotEmpty("numberOfChannelsForUnknown")){
	//    this.setNumberOfChannelsForUnknown(Integer.parseInt(this.parameterBag.numberOfChannelsForUnknown));
	//}
	if(this.parameterBag.variableNotEmpty("alarmUrl")){
	    this.setAlarmUrl(this.parameterBag.alarmUrl);
	}
	if(this.parameterBag.variableNotEmpty("alarmText")){
	    this.setAlarmText(this.parameterBag.alarmText);
	}
	if(this.parameterBag.variableNotEmpty("id")){
	    this.setAlarmId(Integer.parseInt(this.parameterBag.id));
	}
	if(this.parameterBag.listOfUserIds.isEmpty()==false){
	    Iterator iter=this.parameterBag.listOfUserIds.iterator();
	    while(iter.hasNext()){
		Integer idInteger=(Integer)iter.next();
		int id=idInteger.intValue();
		this.listOfUserIds.add(id);
	    }
	}
	if(this.parameterBag.listOfServiceIds.isEmpty()==false){
	    Iterator iter=this.parameterBag.listOfServiceIds.iterator();
	    while(iter.hasNext()){
		Integer idInteger=(Integer)iter.next();
		int id=idInteger.intValue();
		this.listOfServiceIds.add(id);
	    }
	}
	if(this.parameterBag.listOfMatrixIds.isEmpty()==false){
	    Iterator iter=this.parameterBag.listOfServiceIds.iterator();
	    while(iter.hasNext()){
		Integer idInteger=(Integer)iter.next();
		int id=idInteger.intValue();
		this.listOfMatrixIds.add(id);
	    }
	}
	if(this.parameterBag.listOfSiteIds.isEmpty()==false){
	    Iterator iter=this.parameterBag.listOfSiteIds.iterator();
	    while(iter.hasNext()){
		Integer idInteger=(Integer)iter.next();
		int id=idInteger.intValue();
		this.listOfSiteIds.add(id);
	    }
	}
    }

    // === packing methods ===//
   private JSONObject toJson(){
	JSONObject json = new JSONObject();

	json.put("alarmName", this.getAlarmName());
	json.put("alarmId",new Integer(this.getAlarmId()));
	
	return json;
    }

    // === links to pages ===//
    private String makeDefaultAlarmUrl(){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Display Alarm Overview")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	return linkToDetailPage;
    }

    private HtmlLink getLinkToAlarmHistoryDetailsPage(String linkText,int historyRecordId){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Alarm History Details Page")  );
	paramBagLocal.addParam("id",Integer.toString(historyRecordId ));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }  
    private HtmlLink getLinkToAlarmHistoryMessageDetailsPage(String linkText,int historyRecordId){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Alarm History Message Details Page")  );
	paramBagLocal.addParam("id",Integer.toString(historyRecordId ));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }   

    private HtmlLink getLinkToDisplayAlarmOverviewPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Display Alarm Overview")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToEditAlarmPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Edit Alarm")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToAlarmHistoryPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Alarm History Page")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToConfirmDeleteAlarmPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Confirm Delete Alarm")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToDeleteAlarmPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Delete Alarm")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToSaveAlarmPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Save Alarm")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToInsertAlarmPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Insert Alarm")  );
	//paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToListOfAlarmsPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("List of Alarms")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToAddRemoveSitesToAlarmPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Add Remove Site to Alarm")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToSelectSiteToAddAlarmsPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Select Site to Add Alarms")  );
	paramBagLocal.addParam("id",Integer.toString(this.getAlarmId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    // === html pages === //
    public String editAlarmForm(String insertOrSave){
	String result="";
	result=result+"<form>";
	HtmlTable formTable=new HtmlTable(2);
	formTable.setBorder(1);
	formTable.setPadding(0);

	formTable.addCell("Alarm Name");
	formTable.addCell("<input type=\"text\"  size=\"80\" name=\"alarmName\" value=\""+this.getAlarmName()+"\" /><br>");

	formTable.addCell("Alarm Description");
	formTable.addCell("<input type=\"text\"  size=\"80\" name=\"alarmDescription\" value=\""+this.getAlarmDescription()+"\" /><br>");

	formTable.addCell("Check Interval");
	formTable.addCell("<input type=\"text\" name=\"checkInterval\" value=\""+this.getCheckInterval()+"\" /><br>");

	formTable.addCell("Notification Interval");
	formTable.addCell("<input type=\"text\" name=\"notificationInterval\" value=\""+this.getNotificationInterval()+"\" /><br>");

	formTable.addCell("Number of consecutive failures");
	formTable.addCell("<input type=\"text\" name=\"consecutiveNotOKCut\" value=\""+this.getConsecutiveNotOKCut()+"\" /><br>");

	formTable.addCell("Number of consecutive succeses");
	formTable.addCell("<input type=\"text\" name=\"consecutiveOKCut\" value=\""+this.getConsecutiveOKCut()+"\" /><br>");



	formTable.addCell("Number of Channels for Warning");
	formTable.addCell("<input type=\"text\" name=\"numberOfChannelsForWarning\" value=\""+this.getNumberOfChannelsForWarning()+"\" /><br>");

	formTable.addCell("Number of Channels for Critical");
	formTable.addCell("<input type=\"text\" name=\"numberOfChannelsForCritical\" value=\""+this.getNumberOfChannelsForCritical()+"\" /><br>");




	formTable.addCell("Alarm Url");
	formTable.addCell("<input type=\"text\" size=\"80\" name=\"alarmUrl\" value=\""+this.getAlarmUrl()+"\" /><br>");
	//formTable.addCell("<input type=\"text\" cols=\"80\" name=\"alarmUrl\" value=\""+this.getAlarmUrl()+"\" /><br>");

	formTable.addCell("Alarm Text");
	formTable.addCell("<TEXTAREA name=\"alarmText\" rows=\"20\" cols=\"80\">"+this.getAlarmText()+" </TEXTAREA><br>");
   // First line of initial text.
   // Second line of initial text.
   // </TEXTAREA>

			  //formTable.addCell("<input type=\"textarea\" name=\"alarmText\" value=\""+this.getAlarmText()+"\" /><br>");

	result=result+"<br>"+formTable.toHtml()+"<br>";

	if(insertOrSave.equals("save")){
	    result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Alarm")+"\" />";
	}
	if(insertOrSave.equals("insert")){
	    result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Insert Alarm")+"\" />";
	}	
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";

	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	return result;
    }
    public HtmlTable alarmSitesTable(){
	HtmlTable ht=new HtmlTable(4);
	ht.setBorder(1);
	ht.setPadding(0);
	Iterator iter = this.listOfSiteIds.iterator();
	while(iter.hasNext()){
	    Integer siteIntegerId=(Integer)iter.next();
	    int siteId=siteIntegerId.intValue();
	    PerfSonarSite perfSonarSite = new PerfSonarSite(this.parameterBag,this.db,siteId);
	    ht.addCell(perfSonarSite.shortAlarmStatusCell());
	}
	return ht;
    }
    private void fillSiteIdsNotInAlarm(){
	this.listOfSiteIdsNotInAlarm.clear();
	String query="SELECT siteId from Sites";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		int siteId=rs.getInt("siteId");
		Integer siteIdInteger = new Integer(siteId);
		if(this.listOfSiteIds.contains(siteIdInteger)){
		    // pass
		}else{
		    this.listOfSiteIdsNotInAlarm.add(siteIdInteger);
		}
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to get list of sites not linked to alarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);	
	}	

    }
    public HtmlTable removeSitesTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.setBorder(0);
	ht.setPadding(0);
	Iterator iter = this.listOfSiteIds.iterator();
	while(iter.hasNext()){
	    Integer siteIdInteger = (Integer)iter.next();
	    int siteId=siteIdInteger.intValue();
	    PerfSonarSite perfSonarSite = new PerfSonarSite(this.parameterBag,this.db,siteId);
	    String siteName = perfSonarSite.getSiteName();
	    String cellText="<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+siteId+"\">"+siteName;
	    HtmlTableCell cell = new HtmlTableCell(cellText);
	    cell.alignLeft();
	    ht.addCell( cell );
	}
	return ht;
    }
    public HtmlTable addSitesTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.setBorder(0);
	ht.setPadding(0);
	Iterator iter = this.listOfSiteIdsNotInAlarm.iterator();
	while(iter.hasNext()){
	    Integer siteIdInteger = (Integer)iter.next();
	    int siteId=siteIdInteger.intValue();
	    PerfSonarSite perfSonarSite = new PerfSonarSite(this.parameterBag,this.db,siteId);
	    String siteName = perfSonarSite.getSiteName();
	    String cellText="<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+siteId+"\">"+siteName;
	    HtmlTableCell cell = new HtmlTableCell(cellText);
	    cell.alignLeft();
	    ht.addCell(cell);
	}
	return ht;
    }
    public String selectSitesToAddToAlarmForm(){
	String result="";
	result=result+"<form>";
	result=result+this.addSitesTable().toHtml();
	result=result+"<BR>";
	result=result+"<input type=\"submit\" value=\"Add Selected Sites\" /><br>";
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Sites to Alarm")+"\" />";
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	result=result+"</form>";
	return result;
    }
    public String selectSitesToRemoveFromAlarmForm(){
	String result="";
	result=result+"<form>";
	result=result+this.removeSitesTable().toHtml();
	result=result+"<BR>";
	result=result+"<input type=\"submit\" value=\"Remove Selected Sites\" /><br>";
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Remove Sites from Alarm")+"\" />";
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	result=result+"</form>";
	return result;
    }
    public HtmlTable addRemoveSitesTable(){
	// firs of all get sites information
	this.fillSiteIdsNotInAlarm();
	
	// now build a table
	HtmlTable ht=new HtmlTable(2);
	ht.setBorder(1);
	ht.setPadding(0);
	ht.addCell("<strong>Sites linked to this alarm</strong>");
	ht.addCell("<strong>Other sites</strong>");

	ht.addCell(this.selectSitesToRemoveFromAlarmForm());
	ht.addCell(this.selectSitesToAddToAlarmForm());

	return ht;
    }
    private String addSitesToAlarm(){
	String result="";
	result="<br><strong>The following sites are being added to the alarm:</strong><BR>";
	String query="INSERT INTO SitesAlarms (alarmId,siteId) VALUES (?,?)";
	PreparedStatement queryPS=null;
	int siteId=-1;
	
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(     1,this.getAlarmId()    );
	    Iterator iter = this.listOfSiteIds.iterator();
	    while(iter.hasNext()){
		Integer siteIdInteger = (Integer)iter.next();
		siteId = siteIdInteger.intValue();
		PerfSonarSite site = new PerfSonarSite(this.parameterBag,this.db,siteId);
		result=result+"add site "+site.getSiteName()+" ... ";
		queryPS.setInt(     2,siteId);
		queryPS.executeUpdate();
		result=result+" added!<BR>";
	    }
	}catch(Exception e){
	    result=result+" failed! PLease check the logs.<BR>";
	    System.out.println(getClass().getName()+" failed to add site to alarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());
	    System.out.println(getClass().getName()+" siteId="+siteId);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);	
	}	
	return result;
    }
    public void unpackListOfIds(){
	this.listOfSiteIds.clear();
	Iterator iter = this.parameterBag.listOfIds.iterator();
	while(iter.hasNext()){
	    this.listOfSiteIds.add((Integer)iter.next());
	}
    }
    public void getHistory(){

	String getHistorySql="select * from AlarmHistory where alarmId=? ";

	IntervalSelector iS = new IntervalSelector(parameterBag);
	iS.setTimeVariable("Timestamp");
	iS.setTimeZoneShift(0);
	getHistorySql=getHistorySql+" "+iS.buildQuery(parameterBag.interval);

	this.history.clear();
	PreparedStatement getHistoryPreparedSql=null;
	try{
	    
	    getHistoryPreparedSql= db.getConn().prepareStatement(getHistorySql);
	    getHistoryPreparedSql.setInt(1,this.getAlarmId());

	    ResultSet rs=getHistoryPreparedSql.executeQuery(); 

	    int count = 0;
	    while (rs.next ()){
		AlarmHistoryRecord historyRecord = new AlarmHistoryRecord();
		historyRecord.setId(rs.getInt("id"));
		historyRecord.setAlarmId(rs.getInt("alarmId"));
		historyRecord.setTimestamp(rs.getTimestamp("Timestamp"));
		historyRecord.setMessage(rs.getString("Message"));
		historyRecord.setMessageSent(rs.getBoolean("messageSent"));
		historyRecord.setStatus(new ProbeStatus(rs.getString("status")));
		historyRecord.setConsecutiveOK(rs.getInt("consecutiveOK"));
		historyRecord.setConsecutiveNotOK(rs.getInt("consecutiveNotOK"));
		historyRecord.setComment(rs.getString("comment"));

		history.add(historyRecord);

		count=count+1;
	    }
	    rs.close();

	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+getHistorySql);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getHistoryPreparedSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+getHistorySql);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	
    }
    
    public HtmlTable getHistoryTable(){
	
	this.getHistory();
	
	HtmlTable ht=new HtmlTable(5);
	ht.setBorder(0);
	ht.setPadding(0);

	HtmlTableCell cell = null;

	cell = new HtmlTableCell("<strong>Time</strong>");
	cell.alignLeft();
	ht.addCell(cell);

	
	cell = new HtmlTableCell("<strong>Status</strong>");
	cell.alignLeft();
	ht.addCell(cell);

	cell = new HtmlTableCell("<strong>No. OK</strong>");
	cell.alignLeft();
	ht.addCell(cell);
	
	cell = new HtmlTableCell("<strong>No. not OK</strong>");
	cell.alignLeft();
	ht.addCell(cell);

	cell = new HtmlTableCell("<strong>Comment</strong>");
	cell.alignLeft();
	ht.addCell(cell);

	Iterator iter = history.iterator();
	while (iter.hasNext()){
	    AlarmHistoryRecord currentRecord = (AlarmHistoryRecord)iter.next();
	    cell=new HtmlTableCell(currentRecord.getTimestamp().toString());
	    ht.addCell(cell);

	    HtmlLink link = this.getLinkToAlarmHistoryDetailsPage(currentRecord.getStatus().statusWord(),currentRecord.getId());
	    cell=new HtmlTableCell(link,currentRecord.getStatus().color());
	    ht.addCell(cell);

	    ht.addCell(new HtmlTableCell(Integer.toString(currentRecord.getConsecutiveOK())));
	    ht.addCell(new HtmlTableCell(Integer.toString(currentRecord.getConsecutiveNotOK())));
	    if(!currentRecord.getMessageSent()){
		cell = new HtmlTableCell(currentRecord.getComment());
		cell.alignLeft();
		ht.addCell(cell);
	    }else{
		HtmlLink link2=this.getLinkToAlarmHistoryMessageDetailsPage("<strong>"+currentRecord.getComment()+"</strong>",currentRecord.getId());
		cell = new HtmlTableCell(link2,new HtmlColor("lightblue"));
		cell.alignLeft();
		ht.addCell(cell);
	    }

	}

	return ht;
    }
    public String getHistoryTablePage(){
	String result="";

	result=result+"<H1>History of Alarm "+this.getAlarmName()+"</H1><BR>";

	IntervalSelector iS=new IntervalSelector(parameterBag);

	result=result+iS.toHtml()+"<br>";

	HtmlTable historyTable=this.getHistoryTable();
	result=result+historyTable.toHtml();
	return result;
    }

    public String addSitesToAlarmPage(){
	String result="";
	result=result+"<H2>Add sites to the alarm</h2>";
	result=result+"<br>";
	this.unpackListOfIds();
	result=result+this.addSitesToAlarm();
	result=result+"<br>";
	result=result+this.getLinkToEditAlarmPage("<strong>Click here to edit this alarm</strong>");
	result=result+"<strong> or </strong>";
	result=result+this.getLinkToListOfAlarmsPage("<strong>click here to go to list of alarms</strong>");
	return result;
    }
    private String removeSitesFromAlarm(){
	String result="";
	result="<br><strong>The following sites are being remove from the alarm:</strong><BR>";
	String query="DELETE FROM SitesAlarms WHERE alarmId=? and siteId=?";
	PreparedStatement queryPS=null;
	int siteId=-1;
	
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(     1,this.getAlarmId()    );
	    Iterator iter = this.listOfSiteIds.iterator();
	    while(iter.hasNext()){
		Integer siteIdInteger = (Integer)iter.next();
		siteId = siteIdInteger.intValue();
		PerfSonarSite site = new PerfSonarSite(this.parameterBag,this.db,siteId);
		result=result+"remove site "+site.getSiteName()+" ... ";
		queryPS.setInt(     2,siteId);
		queryPS.executeUpdate();
		result=result+" removed!<BR>";
	    }
	}catch(Exception e){
	    result=result+" failed! Please check the logs.<BR>";
	    System.out.println(getClass().getName()+" failed to remove site from alarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());
	    System.out.println(getClass().getName()+" siteId="+siteId);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);	
	}	
	return result;
    }
    public String removeSitesFromAlarmPage(){
	String result="";
	result=result+"<H2>Remove sites from alarm</h2>";
	result=result+"<br>";
	this.unpackListOfIds();
	result=result+this.removeSitesFromAlarm();
	result=result+"<br>";
	result=result+this.getLinkToEditAlarmPage("<strong>Click here to edit this alarm</strong>");
	result=result+"<strong> or </strong>";
	result=result+this.getLinkToListOfAlarmsPage("<strong>click here to go to list of alarms</strong>");
	return result;
    }
    public String addOrRemoveSitesToAlarmPage(){
	String result="<H1>Add/Remove Sites to Alarm</H1><br>";
	result=result+this.addRemoveSitesTable().toHtml();
	return result;
    }
    public String createAlarmPage(){
	String result="<H1>Create New Alarm</H1><br>";
	this.setAlarmName("New Alarm");
	
	this.setAlarmDescription("Fill description of this alarm here....");

	this.setAlarmId(-1);
	result=result+this.editAlarmForm("insert");
	return result;
    }
    private String removePrimitiveServicesFromAlarmForm(){
	String form="<form>";
	HtmlTable ht=new HtmlTable(4);
	Iterator iter = this.listOfServiceIds.iterator();
	while(iter.hasNext()){
	    Integer dbidInteger=(Integer)iter.next();
	    int dbid=dbidInteger.intValue();
	    PrimitiveService service = new PrimitiveService(this.parameterBag,this.db,dbid);
	    String serviceText=service.getHostName()+"/"+service.getServiceName();
	    String cellText="<input type=\"CHECKBOX\" name=\"listOfIds\" value=\"" + dbid + "\" />"+serviceText+ "<br />";
	    HtmlTableCell cell = new HtmlTableCell(cellText);
	    cell.alignLeft();
	    ht.addCell(cell);
	}
	form=form+ht.toHtml();
	form=form+"<input type=\"submit\" value=\"Remove Selected Primitive Services From This Alarm\" /><br>";
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Remove Primitive Services From Alarm")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	form=form+"</form>";
	return form;
    }
    public String addPrimitiveServicesToAlarmButton(){
	String form="<form>";
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Select Site to Add Alarms")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Add Primitive Services To This Alarm\" /><br>";	
	form=form+"</form>";
	return form;
    }
    public String addOrRemoveUsersToAlarmButton(){
	String form="<form>";
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Or Remove Users To Alarm")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Add Or Remove Users To This Alarm\" /><br>";	
	form=form+"</form>";
	return form;
    }
    public String addOrRemovePrimitiveServicesPageElement(){
	String result="";
	result=result+this.removePrimitiveServicesFromAlarmForm();
	result=result+"<br>";
	//result=result+this.getLinkToSelectSiteToAddAlarmsPage("<strong>Add primitive services to alarm</strong>");
	//result=result+"<br>";
	result=result+this.addPrimitiveServicesToAlarmButton();
	result=result+"<br>";
	return result;
    }
    public String editAlarmPage(){
	String result="<H1>Edit Alarm</H1><br>";	
	//result=result+this.editAlarmForm("save");
	result=result+"<br>";
	result=result+this.editAlarmElements();

	//result=result+this.addOrRemovePrimitiveServicesPageElement();
	//result=result+"<br>";
	result=result+this.getLinkToDisplayAlarmOverviewPage("<strong>Click here to get status of alarm components</strong>");
	return result;
    }

    public String saveAlarmPage(){
	String result="<H1>Save Alarm</H1><br>";
	boolean saveResult=false;
	if(this.getAlarmId()>0){
	    result=result+"<strong>I will save the alarm ...</strong><BR>";
	    saveResult=this.saveAlarm();
	}else{
	    result=result+"<strong>I will create the alarm ...</strong><BR>";
	    this.setStatus(new ProbeStatus("UNDEFINED"));
	    
	    saveResult=this.insert();
	}

	if(saveResult){
	    result=result+"<strong>Alarm has been saved</strong><br>";
	}else{
	    result=result+"<strong>Failed to save the alarm, please check the logs.</strong><br>";
	}

	result=result+this.editAlarmElements();

	// result=result+this.editAlarmForm("save");
	// result=result+"<br>";

	// this.loadPrimitiveServicesFromDatabase();
	// result=result+this.addOrRemovePrimitiveServicesPageElement();
	return result;
    }
    public String deleteAlarmPage(){
	String result="<H1>Delete Alarm</H1><br>";
	boolean deleteResult=this.delete();
	result=result+"<br>";
	if(deleteResult){
	    result=result+"<strong>Alarm has been deleted</strong><br>";
	}else{
	    result=result+"<strong>Failed to delete the alarm, please check the logs.</strong><br>";
	}
	result=result+"<br>";
	result=result+this.getLinkToListOfAlarmsPage("List of Alarms");
	result=result+"<br>";
	return result;
    }
    public String confirmDeleteAlarmPage(){
	String result="<H1>Confirm Delete Alarm</H1><br>";
	result=result+"<br>";
	result=result+"<strong>The alarm "+this.getAlarmName()+" will be deleted</strong>";
	result=result+"<br>";
	result=result+this.getLinkToDeleteAlarmPage("<strong>Click here to delete it</strong>");
	result=result+"<br>";
	result=result+"<strong>or</strong>";
	result=result+"<br>";
	result=result+this.getLinkToListOfAlarmsPage("<strong>click here to to back to the list of alarms</strong>");
	return result;
    }

    private HtmlTable tableWithAlarmUsers(){
	HtmlTable ht = new HtmlTable(3);
	Iterator iter = this.listOfUserIds.iterator();
	while(iter.hasNext()){
	    int userId = ((Integer)iter.next()).intValue();
	    User user = new User(this.parameterBag,this.db,userId);
	    String userName=user.getUserName();
	    HtmlTableCell cell = new HtmlTableCell(userName);
	    cell.alignLeft();
	    ht.addCell(cell);
	}
	return ht;
    }


    public String selectSiteToAddAlarmsForm(){
	String result="";
	result=result+"<form>";
	//cellText="<input type=\"radio\" name=\"listOfIds\" value=\"" + value + "\" />"+siteName+ "<br />";
	HtmlTable ht = new HtmlTable(5);

	ListOfPerfSonarSites listOfAllSites = new ListOfPerfSonarSites(this.parameterBag,this.db);
	Iterator iter = listOfAllSites.getList().iterator();
	while(iter.hasNext()){
	    PerfSonarSite site  = (PerfSonarSite)iter.next();
	    String siteName= site.getSiteName();
	    int siteId = site.getSiteId();
	    String cellText="<input type=\"radio\" name=\"listOfIds\" value=\"" + siteId + "\" />"+siteName+ "<br />";
	    HtmlTableCell cell = new HtmlTableCell(cellText);
	    cell.alignLeft();
	    ht.addCell(cell);
	}
	result=result+ht.toHtml();

	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Select Services From Site to Alarm")+"\" />";
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	result=result+"<input type=\"submit\" value=\"Display primitive services from selected site\" /><br>";
	result=result+"</form>";
	     
	return result;
    }
    public String selectSiteToAddAlarmsPage(){
	String result="<H1>Select Site To Add Services to the Alarm</H1><br>";
	result=result+this.selectSiteToAddAlarmsForm();
	return result;
    }
    public boolean deletePrimitiveServiceToThisAlarm(int dbid){
	boolean result = true;
	String query = "DELETE FROM ServicesAlarms where alarmId=? AND dbid=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getAlarmId());
	    queryPS.setInt(2,dbid);
	    queryPS.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to delete alarm-service associacion");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" dbid="+dbid);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" query="+query);
	}	
	return result;
    }
    public boolean insertPrimitiveServiceToThisAlarm(int dbid){
	boolean result = true;
	// set siteId to -1 for primitive services
	int siteId=1;
	String query2 = "INSERT INTO ServicesAlarms (alarmId,dbid) VALUES(?,?)";
	PreparedStatement queryPS2=null;
	try{
	    queryPS2= db.getConn().prepareStatement(query2);
	    queryPS2.setInt(1,this.getAlarmId());
	    queryPS2.setInt(2,dbid);
	    queryPS2.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to insert alarm-service associacion");
	    System.out.println(getClass().getName()+" query2="+query2);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" dbid="+dbid);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS2.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" query2="+query2);
	}
	return result;
    }
    public boolean addPrimitiveServiceToThisAlarm(int dbid){
	boolean result = true;
	boolean deleteResult = this.deletePrimitiveServiceToThisAlarm(dbid);
	boolean insertResult = this.insertPrimitiveServiceToThisAlarm(dbid);
	result=deleteResult&insertResult;
	return result;
    }
    public String addPrimitiveServicesFromSitePage(){
	String result="<H1>Add Primitive Services....</H1><br>";
	Iterator iter = this.parameterBag.listOfIds.iterator();
	while(iter.hasNext()){
	    Integer dbidInteger = (Integer)iter.next();
	    int dbid = dbidInteger.intValue();
	    result=result+" "+dbid;
	    PrimitiveService service = new PrimitiveService(this.parameterBag,this.db,dbid);
		
	    result=result+service.getServiceName()+" "+service.getHostName()+" ... ";
	    boolean addResult = this.addPrimitiveServiceToThisAlarm(dbid);
	    if(addResult){
		result=result+" added!<br>";
	    }else{
		result=result+" failed! Please check the logs!<br>";
	    }
		
	}

	result=result+this.getLinkToDisplayAlarmOverviewPage("<strong>Click here to display overview for this alarm</strong>");
	result=result+" <strong>or</strong> ";
	result=result+this. getLinkToEditAlarmPage("<strong>Click here to edit this alarm</strong>");
	result=result+"<br>";
	return result;
    }
    public String selectPrimitiveServicesFromSitePage(){
	String result="<H1>Add Primitive Services....</H1><br>";

	String form="<form>";
	HtmlTable ht = new HtmlTable(3);

	Iterator iter = this.parameterBag.listOfIds.iterator();
	while(iter.hasNext()){
	    Integer siteIdInteger = (Integer)iter.next();
	    int siteId = siteIdInteger.intValue();
	    PerfSonarSite site = new PerfSonarSite(this.parameterBag,this.db,siteId);
	    result=result+site.getSiteName()+"<br>";
	    result=result+"Primitive services:<br>";
	    Iterator iter2 = site.getListOfIdsOfPrimitiveServicesFromThisSite().iterator();
	    while(iter2.hasNext()){
		Integer dbidInteger = (Integer)iter2.next();
		int dbid = dbidInteger.intValue();
		System.out.println(getClass().getName()+" create primitive service dbid="+dbid);
		PrimitiveService service = new PrimitiveService(this.parameterBag,this.db,dbid);
		System.out.println(getClass().getName()+" primitive service created");

		String serviceText=service.getHostName()+"/"+service.getServiceName();
		String cellText="<input type=\"CHECKBOX\" name=\"listOfIds\" value=\"" + dbid + "\" />"+serviceText+ "<br />";
		HtmlTableCell cell = new HtmlTableCell(cellText);
		cell.alignLeft();
		ht.addCell(cell);
	    }
	    form=form+ht.toHtml();
	    form=form+"<input type=\"submit\" value=\"Add Selected Services\" /><br>";
	    form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Services From Site to Alarm")+"\" />";
	    form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	    form=form+"</form>";
	    result=result+form;
	    result=result+"<br>";
	}
	return result;
    }
    private List<PrimitiveService> getPrimitiveServicesInThisAlarm(){
	List<PrimitiveService> resultList=new ArrayList<PrimitiveService>();
	resultList.clear();
	String query="SELECT dbid FROM ServicesAlarms WHERE alarmId=?";
	PreparedStatement queryPS=null;
	try{

	    queryPS= db.getConn().prepareStatement(query);

	    queryPS.setInt(1,this.getAlarmId());
	    ResultSet rs = queryPS.executeQuery();

	    while(rs.next()){
		int dbid = rs.getInt("dbid");

		PrimitiveService primitiveService = new PrimitiveService(this.parameterBag, this.db,dbid);

		resultList.add(primitiveService);
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load list of primitive services for an alarm");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" query="+query);
	}	
	return resultList;
    }
    public HtmlTable displayPrimitiveServicesInAlarmTable(){
	HtmlTable ht =new HtmlTable(4);
	Iterator iter = this.getPrimitiveServicesInThisAlarm().iterator();
	while(iter.hasNext()){
	    PrimitiveService service = (PrimitiveService)iter.next();
	    HtmlTableCell cell = service.shortStatusCell();
	    ht.addCell(cell);
	}
	return ht;
    }
    public String displayAlarmOverviewPage(){
	String result="<H1>Status of Alarm "+this.getAlarmName()+"</H1>";
	result=result+this.displayPrimitiveServicesInAlarmTable().toHtml();
	result=result+"<BR>";
	result=result+"<strong>Last notification was sent on "+this.getLastNotificationTime()+"</strong>";
	result=result+"<BR>";
	result=result+this.getLinkToEditAlarmPage("<strong>Click here to edit this alarm</strong>");
	result=result+"<BR>";
	result=result+this.getLinkToAlarmHistoryPage("<strong>Click here to see alarm history</strong>");
	return result;
    }

    public boolean removePrimitiveServicesFromAlarm(List<Integer>listOfIds){
	boolean result=true;
	String query = "DELETE FROM ServicesAlarms WHERE alarmId=? AND dbid=?";
	Iterator iter=listOfIds.iterator();
	PreparedStatement queryPS=null;
	int dbid=-1;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getAlarmId());
	    while(iter.hasNext()){
		dbid=((Integer)iter.next()).intValue();
		queryPS.setInt(2,dbid);
		queryPS.executeUpdate();
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to delete alarm-service associacion");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());	    
	    System.out.println(getClass().getName()+" dbid="+dbid);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" query="+query);
	}	
	
	return result;
    }


    public String editAlarmElements(){
	String result="";
	result=result+this.editAlarmForm("save");
	result=result+"<br>";

	this.loadPrimitiveServicesFromDatabase();
	result=result+this.addOrRemovePrimitiveServicesPageElement();	

	result=result+"<br>";
	result=result+"<h4>Users assigned to watch this alarm:</h4>";
	result=result+this.tableWithAlarmUsers().toHtml();
	result=result+"<br>";
	result=result+this.addOrRemoveUsersToAlarmButton();
	return result;
    }

    public String removePrimitiveServicesFromSitePage(){
	String result="<H1>Remove Primitive Services....</H1><br>";
	boolean removeResult = this.removePrimitiveServicesFromAlarm(  this.parameterBag.listOfIds);
	if(removeResult){
	    result=result+"Services removed<br>";
	}else{
	    result=result+"Services not removed, please check the logs<br>";
	}
	result=result+this.editAlarmElements();
	return result;
    }
    
    private List<User> getListOfUsersInThisAlarm(){
	List<User> resultList = new ArrayList<User>();
	resultList.clear();
	Iterator iter = this.listOfUserIds.iterator();
	while(iter.hasNext()){
	    int userId=((Integer)iter.next()).intValue();
	    User user = new User(this.parameterBag,this.db,userId);
	    resultList.add(user);
	}
	return resultList;
    }
    public String removeUsersForm(){
	String result="";
	HtmlTable ht = new HtmlTable(1);
	ht.setBorder(0);

	result=result+"<form>";	

	Iterator iter = this.getListOfUsersInThisAlarm().iterator();
	while(iter.hasNext()){
	    User user = (User)iter.next();
	    String cellText="<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+user.getUserId()+"\">"+user.getUserName();
	    HtmlTableCell cell = new HtmlTableCell(cellText);
	    cell.alignLeft();
	    ht.addCell(cell);
	}
	result=result+"<br>";
	result=result+ht.toHtml();
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Remove Users From Alarm Page")+"\" />";
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	result=result+"<input type=\"submit\" value=\"Remove selected users from alarm\" /><br>";
	result=result+"</form>";

	return result;
    }
    public String addUsersForm(){
	String result="";
	HtmlTable ht = new HtmlTable(1);
	ht.setBorder(0);

	result=result+"<form>";

	Iterator iter = this.getListOfUsersNotInThisAlarm().iterator();
	while(iter.hasNext()){
	    User user = (User)iter.next();
	    String cellText="<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+user.getUserId()+"\">"+user.getUserName();

	    HtmlTableCell cell = new HtmlTableCell(cellText);
	    cell.alignLeft();
	    ht.addCell(cell);
	}
	result=result+"<br>";
	result=result+ht.toHtml();
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Users To Alarm Page")+"\" />";
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+this.getAlarmId()+"\" />";
	result=result+"<input type=\"submit\" value=\"Add selected users to alarm\" /><br>";
	result=result+"</form>";
	return result;
    }
    public HtmlTable addOrRemoveUsersTable(){
	HtmlTable ht = new HtmlTable(2);
	HtmlTableCell cell = new HtmlTableCell("Users assigned to this alarm");
	ht.addCell(cell);

	cell =  new HtmlTableCell("All other users");
	ht.addCell(cell);

	cell = new HtmlTableCell(this.removeUsersForm());
	ht.addCell(cell);

	cell = new HtmlTableCell(this.addUsersForm());
	ht.addCell(cell);
	return ht;
    }
    public String addOrRemoveUsersPage(){
	String result="<H1>Add or remove users to alarm "+this.getAlarmName()+"</H1><br>";
	this.loadAllUserIds();
	this.loadUserIdsNotInAlarm();

	result=result+this.addOrRemoveUsersTable().toHtml();
	return result;
    }
    public boolean addSelectedUsers(List<Integer> listOfIds){
	boolean result=true;
	String deleteQuery="DELETE FROM UsersAlarms where alarmId=? and userId=?";
	String insertQuery="INSERT INTO UsersAlarms (alarmId,userId) VALUES (?,?)";
	PreparedStatement deleteQueryPS=null;
	PreparedStatement insertQueryPS=null;
	int userId=-1;
	try{
	    deleteQueryPS= db.getConn().prepareStatement(deleteQuery);
	    insertQueryPS= db.getConn().prepareStatement(insertQuery);
	    deleteQueryPS.setInt(1,this.getAlarmId());
	    insertQueryPS.setInt(1,this.getAlarmId());

	    Iterator iter = listOfIds.iterator();
	    while(iter.hasNext()){
		userId = ((Integer)iter.next()).intValue();
		deleteQueryPS.setInt(2,userId);
		insertQueryPS.setInt(2,userId);
		deleteQueryPS.executeUpdate();
		insertQueryPS.executeUpdate();
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to add users to the alarm");
	    System.out.println(getClass().getName()+" deleteQuery="+deleteQuery);
	    System.out.println(getClass().getName()+" insertQuery="+insertQuery);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());
	    System.out.println(getClass().getName()+" userId="+userId);
	}
	try{
	    deleteQueryPS.close();
	    insertQueryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" deleteQuery="+deleteQuery);
	    System.out.println(getClass().getName()+" insertQuery="+insertQuery);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());
	    System.out.println(getClass().getName()+" userId="+userId);
	    System.out.println(getClass().getName()+" "+e);	
	}
	return result;
    }
    public boolean removeSelectedUsers(List<Integer> listOfIds){
	boolean result=true;
	String deleteQuery="DELETE FROM UsersAlarms where alarmId=? and userId=?";
	PreparedStatement deleteQueryPS=null;
	int userId=-1;
	try{
	    deleteQueryPS= db.getConn().prepareStatement(deleteQuery);
	    deleteQueryPS.setInt(1,this.getAlarmId());

	    Iterator iter = listOfIds.iterator();
	    while(iter.hasNext()){
		userId = ((Integer)iter.next()).intValue();
		deleteQueryPS.setInt(2,userId);
		deleteQueryPS.executeUpdate();
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to remove users from the alarm");
	    System.out.println(getClass().getName()+" deleteQuery="+deleteQuery);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());
	    System.out.println(getClass().getName()+" userId="+userId);
	}
	try{
	    deleteQueryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" deleteQuery="+deleteQuery);
	    System.out.println(getClass().getName()+" alarmId="+this.getAlarmId());
	    System.out.println(getClass().getName()+" userId="+userId);
	    System.out.println(getClass().getName()+" "+e);	
	}
	return result;
    }
    public String addUsersToAlarmPage(){
	String result="<H1>Add Users to alarm</H1>";
	result=result+"<br>";
	boolean addUsersResult=this.addSelectedUsers( this.parameterBag.listOfIds  );
	if(addUsersResult){
	    result=result+"users added!<br>";
	}else{
	    result=result+" error occured while adding users, please check the logs!<br>";
	}
	this.loadAllUserIds();
	this.loadUsersFromDatabase();
	this.loadUserIdsNotInAlarm();
	result=result+this.addOrRemoveUsersTable().toHtml();
	return result;
    }
    public String removeUsersFromAlarmPage(){
	String result="<H1>Remove Users From Alarm</H1>";
	result=result+"<br>";
	boolean removeUsersResult=this.removeSelectedUsers( this.parameterBag.listOfIds  );
	if(removeUsersResult){
	    result=result+"users removed!<br>";
	}else{
	    result=result+" error occured while removing users, please check the logs!<br>";
	}
	this.loadAllUserIds();
	this.loadUsersFromDatabase();
	this.loadUserIdsNotInAlarm();
	result=result+this.addOrRemoveUsersTable().toHtml();
	return result;
    }

    // === info methods === //
    public  List<HtmlTableCell> infoTableRow(){
	List<HtmlTableCell> result=new ArrayList<HtmlTableCell>();

	
	HtmlTableCell cell1=new  HtmlTableCell(this.getLinkToDisplayAlarmOverviewPage(this.getAlarmName()));
	cell1.alignLeft();
	result.add(cell1);

	//HtmlTableCell emptyCell=new  HtmlTableCell("  ");	
	//result.add(emptyCell);

	//result.add(emptyCell);


	HtmlLink link2edit=this.getLinkToEditAlarmPage("edit");
	HtmlTableCell cell2=new  HtmlTableCell(link2edit);
	result.add(cell2);

	//result.add(emptyCell);


	String delete="delete";
	HtmlLink link2delete=this.getLinkToConfirmDeleteAlarmPage("delete");
	HtmlTableCell cell4=new  HtmlTableCell(link2delete);
	result.add(cell4);


	return result;
    }

    public HtmlTableCell shortStatusCell(){
	String cellText=this.getAlarmName();
	
	HtmlLink link=this.getLinkToDisplayAlarmOverviewPage(cellText);
	HtmlTableCell cell=new HtmlTableCell(link,this.alarmStatus().color());
	return cell;
	
    }

    public String getAlarmDetails(){
	String result="";
	Iterator iter = this.getPrimitiveServicesInThisAlarm().iterator();
	while(iter.hasNext()){
	    PrimitiveService service = (PrimitiveService)iter.next();
	    ProbeStatus status = service.getServiceStatus();
	    result=result+service.getServiceName()+" at " +service.getHostName()+ " ... "+status.toString()+" "+service.makeLinkToDetailsPage(" ").getUrl()+"\n";
	}
	return result;
    }
    public String getAlarmDetailsForLogging(){
	String result="";
	Iterator iter = this.getPrimitiveServicesInThisAlarm().iterator();
	while(iter.hasNext()){
	    PrimitiveService service = (PrimitiveService)iter.next();
	    ProbeStatus status = service.getServiceStatus();
	    result=result+service.getServiceName()+" at " +service.getHostName()+ " ... "+service.makeLinkToDetailsPage(status.toString()).toHtml()+"\n";
	}
	return result;
    }
    


    public ProbeStatus alarmStatus(){
	int numberOfChannelsNotOk=0;
	Iterator iter = this.getPrimitiveServicesInThisAlarm().iterator();
	while(iter.hasNext()){
	    PrimitiveService service = (PrimitiveService)iter.next();
	    ProbeStatus status = service.getServiceStatus();
	    if(!status.isOK()){
		numberOfChannelsNotOk=numberOfChannelsNotOk+1;
	    }
	}
	ProbeStatus finalStatus=new ProbeStatus(ProbeStatus.OK);
	if(numberOfChannelsNotOk>=numberOfChannelsForCritical){
	    finalStatus=new ProbeStatus(ProbeStatus.CRITICAL);
	}else{
	    if(numberOfChannelsNotOk>=numberOfChannelsForWarning){
		finalStatus=new ProbeStatus(ProbeStatus.WARNING);
	    }
	}
	return finalStatus;
    }

    public int compareTo(Object obj) {
	Alarm other=(Alarm)obj;
	String otherName=other.getAlarmName();
	if (otherName==null){
	    if (this.getAlarmName()==null){
		return 0;
	    }else{
		return 1;
	    }
	}else{
	    if(this.getAlarmName()==null){
		return -1;
	    }else{
		return this.getAlarmName().compareTo(otherName);
	    }
	}
    }


    public boolean equals(Object obj) {
	Alarm otherAlarm=(Alarm)obj;
	if (otherAlarm==null){
	    if(this.getAlarmName()==null){
		return true;
	    }else{
		return false;
	    }
	}else{
	    String otherName=otherAlarm.getAlarmName();
	    if (otherName==null){
		if (this.getAlarmName()==null){
		    return true;
		}else{
		    return false;
		}
	    }else{
		if(this.getAlarmName()==null){
		    return false;
		}else{
		    return this.getAlarmName().equals(otherName);
		}
	    }	
	}
    }
}

