package gov.bnl.racf.exda;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.jfree.*;
import org.jfree.chart.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;


public class PrimitiveService
{  


    private int dbid=-1;

    private Timestamp state_time = null;
    public ProbeStatus status=null;

    private String metricName=null;
    private String metricType=null;
    public ProbeStatus metricStatus = null; 
    private Timestamp timestamp = null;
    private String serviceType = null;
    private String serviceUri=null;
    private String gatheredAt=null;
    private String summaryData=null;
    private String detailsData = null;
    private String performanceData=null;
    private String voName=null;
    private int    samUploadFlag=0;
    public  String hostName=null;

    public String hostIp=null;

    private String hostNameDescription=null;
    private String hostName2=null;
    private String hostName2Description=null;    
    private String hostName3=null;
    private String hostName3Description=null;
    private double performanceData1=0.;
    private double performanceData2=0.;
    private double performanceData3=0.;
    private double performanceData4=0.;
    private double performanceData5=0.;
    private String performanceDataName1="";
    private String performanceDataName2="";
    private String performanceDataName3="";
    private String performanceDataName4="";
    private String performanceDataName5="";
    private String performanceDataDescription1="";
    private String performanceDataDescription2="";
    private String performanceDataDescription3="";
    private String performanceDataDescription4="";
    private String performanceDataDescription5="";
    private String probeCommand="";
    private String probeId="";
    private String serviceDescription="";

    private String probeRunning="";
    private String schedulerName="MAIN";
    
    private int matrixId=-1;

    private String dashboardComment="";

    private int checkInterval=-1;

    private Timestamp lastCheckTime=null;
    private Timestamp nextCheckTime=null;

    private boolean active=true;

    private String linkToNodeDetailPage=null;

    private ParameterBag parameterBag = null;

    private DbConnector db=null;

    private String initialisationSqlQuery="";
    // private PreparedStatement initialisationSql1=null;
    // private PreparedStatement initialisationSql2=null;
    // private PreparedStatement getHistoryPreparedSql=null;

    private ForceUpdateButton fb = null;
    private SetActiveButton setActiveButton = null;

    //private PerfSonarHost perfSonarHost=null;
    int perfSonarHostId=-1;

    private ActivityLogger activityLogger=null;


    List<PrimitiveServiceHistoryRecord> history=new ArrayList<PrimitiveServiceHistoryRecord>();
    // ===== End of internal variables ====//

    // ===== Start of constructors ===== //



    public PrimitiveService(ParameterBag paramBag,  DbConnector inputDb, String probeIdInput)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;

	    this.activityLogger=new ActivityLogger(this.parameterBag,this.db);
	    	   
	    //=========== get information =======

	    initialisationSqlQuery="select  * from MetricRecordSummary where ProbeId=? order by Timestamp DESC limit 1";
	    PreparedStatement initialisationSql1=null;
	    try{
		initialisationSql1= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql1.setString(1,probeIdInput);

		ResultSet rs=initialisationSql1.executeQuery();
		unpackResultSet(rs);
		rs.close();

	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e) ;   
	    }
	    try{
		initialisationSql1.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e) ;   
	    }
	
	    getExecutionControlData();

	    /// create link to node detail page
	    ParameterBag paramBagLocal=(ParameterBag)paramBag.clone();
	    paramBagLocal.addParam("page",ParameterBag.pageAddress("perfSonar Primitive")  );
	    paramBagLocal.addParam("hostName",hostName);
	    paramBagLocal.addParam("serviceName",metricName);
	    linkToNodeDetailPage=paramBagLocal.makeLink();

	}


    public PrimitiveService(ParameterBag paramBag,  DbConnector inputDb, int serviceId)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;

	    this.activityLogger=new ActivityLogger(this.parameterBag,this.db);

	    	   
	    //=========== get information =======

	    initialisationSqlQuery="select  * from MetricRecordSummary where dbid=? order by Timestamp DESC limit 1";
	    PreparedStatement initialisationSql1=null;
	    try{
		initialisationSql1= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql1.setInt(1,serviceId);

		ResultSet rs=initialisationSql1.executeQuery();
		unpackResultSet(rs);
		rs.close();

	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e) ;   
	    }
	    try{
		initialisationSql1.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e) ;   
	    }

	    this.getExecutionControlData();
	    
	    /// create link to node detail page
	    ParameterBag paramBagLocal=(ParameterBag)paramBag.clone();

	    paramBagLocal.addParam("page",ParameterBag.pageAddress("perfSonar Primitive")  );
	    paramBagLocal.addParam("hostName",hostName);
	    paramBagLocal.addParam("serviceName",metricName);
	    linkToNodeDetailPage=paramBagLocal.makeLink();

	}


    public PrimitiveService(ParameterBag paramBag,  DbConnector inputDb,String inputHostName, String serviceName)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;

	    this.activityLogger=new ActivityLogger(this.parameterBag,this.db);

	    this.hostName=inputHostName;
	    this.metricName=serviceName;

	    /// create link to node detail page
	    ParameterBag paramBagLocal=(ParameterBag)paramBag.clone();
	    paramBagLocal.addParam("page",ParameterBag.pageAddress("perfSonar Primitive")  );
	    paramBagLocal.addParam("hostName",hostName);
	    paramBagLocal.addParam("serviceName",metricName);
	    //paramBagLocal.addParam("metricName",metricName);
	    linkToNodeDetailPage=paramBagLocal.makeLink();
	    	   
	    //=========== get information =======

	    initialisationSqlQuery="select  * from MetricRecordSummary where HostName=? and MetricName=? order by Timestamp DESC limit 1";
	    PreparedStatement initialisationSql2=null;
	    try{
		initialisationSql2= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql2.setString(1,hostName);
		initialisationSql2.setString(2,serviceName);

		ResultSet rs=initialisationSql2.executeQuery();
	    	unpackResultSet(rs);   
		rs.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+metricName) ;   
		System.out.println(e);
	    }
	    try{
		initialisationSql2.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+metricName) ;   
		System.out.println(e);
	    }
	    this.getExecutionControlData();
	}

    public void setNextCheckTimeToNow(){
	// set next check time for this service to NOW()

	String dbCommand="UPDATE Services SET NextCheckTime=NOW() WHERE dbid=?";
	PreparedStatement dbCommandPS=null;
	try{
	    dbCommandPS=db.getConn().prepareStatement(dbCommand);
	    dbCommandPS.setInt(1,this.getId());
	    dbCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed set NextCheckTime to NOW()");
	    System.out.println(getClass().getName()+" "+dbCommand);
	    System.out.println(getClass().getName()+" probeId="+this.probeId);
	    System.out.println(getClass().getName()+" dbid="+this.getId());
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    dbCommandPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+dbCommand);
	    System.out.println(getClass().getName()+" "+e);
	}
    }


    public int insert(){
	// insert the service into Services Table, return id of inserted record
	boolean result=true;
	String query="INSERT INTO Services (MetricName,HostName,ProbeCommand,ProbeId,ServiceDescription,CheckInterval,ProbeRunning,SchedulerName,matrixId,Active,LastCheckTime,NextCheckTime,PrimitiveService) VALUES(?,?,?,?,?,?,?,?,?,?,NOW(),NOW(),'Y')";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setString(  1,this.metricName);
	    queryPS.setString(  2,this.hostName);
	    queryPS.setString(  3,this.probeCommand);
	    queryPS.setString(  4,this.probeId);
	    queryPS.setString(  5,this.serviceDescription);
	    queryPS.setInt(     6,this.checkInterval);
	    queryPS.setString(  7,this.probeRunning);	    
	    queryPS.setString(  8,this.schedulerName);
	    queryPS.setInt(     9,this.matrixId);
	    queryPS.setBoolean(10,this.active);
	    queryPS.executeUpdate();
	    activityLogger.log("service "+metricName+" added to host "+this.getHostName());
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when inserting primitive service id into Services");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" service="+this.metricName);
	    System.out.println(getClass().getName()+" host="+this.hostName);
	    System.out.println(getClass().getName()+" command="+this.probeCommand);
	    System.out.println(getClass().getName()+" interval="+this.checkInterval);
	    System.out.println(getClass().getName()+" probeRunning="+this.probeRunning);
	    System.out.println(getClass().getName()+" schedulerName="+this.schedulerName);
	    System.out.println(getClass().getName()+" matrixId="+this.matrixId);
	    System.out.println(getClass().getName()+" active="+this.active);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    ResultSet rs=queryPS.getGeneratedKeys();
	    while (rs.next ()){
		this.dbid = rs.getInt(1);
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not obtain key of newly inserted primitive service");
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	   } 
	return this.dbid;
    }
    public boolean save(){
	boolean result=true;
	String query="UPDATE Services SET MetricName=?,HostName=?,ProbeCommand=?,ProbeId=?,ServiceDescription=?,CheckInterval=?,ProbeRunning=?,SchedulerName=?,matrixId=?,Active=? WHERE dbid=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.metricName);
	    queryPS.setString(2,this.hostName);
	    queryPS.setString(3,this.probeCommand);
	    queryPS.setString(4,this.serviceDescription);
	    queryPS.setInt(5,this.checkInterval);
	    queryPS.setString(6,this.probeRunning);	    
	    queryPS.setString(7,this.schedulerName);
	    queryPS.setInt(8,this.matrixId);
	    queryPS.setBoolean(10,this.active);
	    queryPS.setInt(11,this.dbid);
		
	    queryPS.executeUpdate();
	    activityLogger.log("service "+this.metricName+" modified on host "+this.getHostName());
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when saving primitive service id into Services");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" service="+this.metricName);
	    System.out.println(getClass().getName()+" host="+this.hostName);
	    System.out.println(getClass().getName()+" command="+this.probeCommand);
	    System.out.println(getClass().getName()+" interval="+this.checkInterval);
	    System.out.println(getClass().getName()+" probeRunning="+this.probeRunning);
	    System.out.println(getClass().getName()+" schedulerName="+this.schedulerName);
	    System.out.println(getClass().getName()+" matrixId="+this.matrixId);
	    System.out.println(getClass().getName()+" active="+this.active);
	    System.out.println(getClass().getName()+" dbid="+this.dbid);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	   } 
	return result;
    }
    public boolean delete(){
	boolean result=true;
	String query="DELETE FROM Services WHERE dbid=?";
	PreparedStatement queryPS=null;	
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.dbid);
	    queryPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when deleting primitive service on Services");	    
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	   } 
	String query2="DELETE FROM MetricRecordSummary WHERE dbid=?";
	PreparedStatement query2PS=null;	
	try{
	    query2PS= db.getConn().prepareStatement(query);
	    query2PS.setInt(1,this.dbid);	    
	    query2PS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when deleting primitive service on MetricRecordSummary");	    
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    query2PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not close prepared statement");
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" "+e);
	   } 

	String query3="DELETE FROM MetricRecord WHERE dbid=?";
	PreparedStatement query3PS=null;	
	try{
	    query3PS= db.getConn().prepareStatement(query3);
	    query3PS.setInt(1,this.dbid);
	    query3PS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when deleting primitive service on MetricRecord");	    
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    query3PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not close prepared statement");
	    System.out.println(getClass().getName()+" "+query3);
	    System.out.println(getClass().getName()+" "+e);
	   } 

	// delete service from alerts
	String query4="DELETE FROM ServicesAlarms WHERE dbid=?";
	PreparedStatement query4PS=null;	
	try{
	    query4PS= db.getConn().prepareStatement(query4);
	    query4PS.setInt(1,this.dbid);
	    query4PS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when deleting primitive service on MetricRecord");	    
	    System.out.println(getClass().getName()+" "+query4);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    query4PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not close prepared statement");
	    System.out.println(getClass().getName()+" "+query4);
	    System.out.println(getClass().getName()+" "+e);
	   } 



	if(!result){
	    System.out.println(getClass().getName()+" service="+this.metricName);
	    System.out.println(getClass().getName()+" host="+this.hostName);
	    System.out.println(getClass().getName()+" command="+this.probeCommand);
	    System.out.println(getClass().getName()+" interval="+this.checkInterval);
	    System.out.println(getClass().getName()+" probeRunning="+this.probeRunning);
	    System.out.println(getClass().getName()+" schedulerName="+this.schedulerName);
	    System.out.println(getClass().getName()+" matrixId="+this.matrixId);
	    System.out.println(getClass().getName()+" active="+this.active);
	    System.out.println(getClass().getName()+" dbid="+this.dbid); 
	}	
	return result;
    }


    public void unpackResultSet(ResultSet rs){
	try{
	    while (rs.next ()){
		metricName=rs.getString("MetricName");
		metricType=rs.getString("MetricType");

		String temp=rs.getString("MetricStatus");

		metricStatus=new ProbeStatus(temp);
		
		timestamp=rs.getTimestamp("Timestamp");
		serviceType=rs.getString("ServiceType");
		serviceUri =rs.getString("ServiceUri");
		gatheredAt =rs.getString("GatheredAt");
		summaryData=rs.getString("SummaryData");
		detailsData=rs.getString("DetailsData");
		performanceData=rs.getString("PerformanceData");
		voName=rs.getString("VoName");
		samUploadFlag=rs.getInt("VoName");
		hostName=rs.getString("HostName");
		hostNameDescription=rs.getString("HostNameDescription");
		hostName2=rs.getString("HostName2");
		hostName2Description=rs.getString("HostName2Description");
		hostName3=rs.getString("HostName3");
		hostName3Description=rs.getString("HostName3Description");
		performanceData1=rs.getDouble("PerformanceData1");
		performanceData2=rs.getDouble("PerformanceData2");
		performanceData3=rs.getDouble("PerformanceData3");
		performanceData4=rs.getDouble("PerformanceData4");
		performanceData5=rs.getDouble("PerformanceData5");
		performanceDataName1=rs.getString("PerformanceDataName1");
		performanceDataName2=rs.getString("PerformanceDataName2");
		performanceDataName3=rs.getString("PerformanceDataName3");
		performanceDataName4=rs.getString("PerformanceDataName4");
		performanceDataName5=rs.getString("PerformanceDataName5");
		performanceDataDescription1=rs.getString("PerformanceDataDescription1");
		performanceDataDescription2=rs.getString("PerformanceDataDescription2");
		performanceDataDescription3=rs.getString("PerformanceDataDescription3");
		performanceDataDescription4=rs.getString("PerformanceDataDescription4");
		performanceDataDescription5=rs.getString("PerformanceDataDescription5");
		probeCommand=rs.getString("ProbeCommand");
		probeId=rs.getString("ProbeId");
		serviceDescription=rs.getString("ServiceDescription");

		this.setId(rs.getInt("dbid"));

	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error occured when unpacking result set");
	    System.out.println(getClass().getName()+" "+metricName) ;  
	    System.out.println(getClass().getName()+" "+e) ;   
	}
    }

    public ProbeStatus getServiceStatus(){
	return this.metricStatus;
    }
    public void setServiceStatus(ProbeStatus inputVar){
	this.metricStatus=inputVar;
    }


    public String getLinkToNodeDetailsPage(){
	String result="";
	String requestUri=parameterBag.requestUri;
	result=requestUri+"?";
	if (hostName!=null){
	    result=result+"hostName="+hostName+"&";
	}
	if (metricName!=null){
	    result=result+"serviceName="+metricName+"&";
	}	
	result=result+"page="+parameterBag.pageAddress("perfSonar Primitive")+"&";
	return result;
    }
    public HtmlLink makeLinkToDetailsPage(String linkText){

	ParameterBag  paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("perfSonar Primitive"));

	//paramBagLocal.addParam("hostName",hostName);
	//paramBagLocal.addParam("serviceName",metricName);
	paramBagLocal.addParam("id",Integer.toString(this.getId()));
	String linkToDetailPage=paramBagLocal.makeLink();

	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink makeLinkToHistoryTablePage(String linkText){

	ParameterBag  paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Primitive Node History Table"));

	paramBagLocal.addParam("id",Integer.toString(this.getId()));
	String linkToDetailPage=paramBagLocal.makeLink();

	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    public String getHostName(){
	return hostName;
    }
    public String getServiceName(){
	return metricName;
    }

    public int getId(){
	return this.dbid;
    }
    public void setId(int inputVar){
	this.dbid=inputVar;
    }

    public void getExecutionControlData(){
	// if force update was requested and user is valid, do it now.
	parameterBag.addParam("probeId",probeId);
	
	// decide if the probe is active or not
	setActiveButton = new SetActiveButton(parameterBag,db);
	setActiveButton.setDestinationPageName("Primitive Service Confirm Active Update");

	// decide whether to force update of service
	fb=new ForceUpdateButton(parameterBag,db);

	if (parameterBag.getUser().isApproved() && "yes".equals(parameterBag.executeNow)){
	    fb.forceTestNow();
	}

	// get check times information
	//=========== get information =======
	String checkTimeSqlQuery="select NextCheckTime,LastCheckTime,CheckInterval,Active from Services where ProbeId=? order by dbid limit 1";	    
	PreparedStatement checkTimeSql=null;
	try{
	    checkTimeSql=db.getConn().prepareStatement(checkTimeSqlQuery);
	    checkTimeSql.setString(1,probeId);
	    ResultSet rs=checkTimeSql.executeQuery();
	    while (rs.next ()){
		nextCheckTime=rs.getTimestamp("NextCheckTime");
		lastCheckTime=rs.getTimestamp("LastCheckTime");	
		checkInterval=rs.getInt("CheckInterval");
		// is the probe active?
		this.active=rs.getBoolean("Active");
		// add active information to probe status
		if(!this.active){
		    metricStatus.setInActive();
		}
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+checkTimeSqlQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    checkTimeSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when deleting prepared statement");
	    System.out.println(getClass().getName()+" "+checkTimeSqlQuery);
	    System.out.println(getClass().getName()+" "+e);
	}

	// protection against overdue services
	if (this.active){

	    java.util.Date currentTime = new java.util.Date();
	    Calendar currentCal = Calendar.getInstance();
	    currentCal.setTime(currentTime);



	    java.sql.Date  lastCheckDate      = new java.sql.Date(lastCheckTime.getTime());  
	    Calendar lastCheckCalendar = Calendar.getInstance();
	    lastCheckCalendar.setTime(lastCheckDate);

	    currentCal.add(Calendar.HOUR,-24);

	    if (lastCheckCalendar.before(currentCal)){
		dashboardComment="Service check is overdue";
		if (metricStatus.isOK()){
		    metricStatus.setWARNING(); 
		}
	    }
	}
    }


    public HtmlTableCell veryShortStatusCell(){
	//return very short service status cell containing only status word
	HtmlLink link=new HtmlLink(linkToNodeDetailPage,metricStatus.statusWordShort());
	HtmlTableCell cell=new HtmlTableCell(link,metricStatus.color());
	return cell;
	
    }
    public HtmlTable veryShortHtmlTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(veryShortStatusCell());
	return ht;
	
    }

    public HtmlTableCell shortStatusCell(){
	// return very short status table containing only service and host name word and color

	//HtmlLink link=new HtmlLink(linkToNodeDetailPage,metricName+"<br>"+hostName );
	HtmlLink link=makeLinkToDetailsPage(metricName+"<br>"+hostName );

	if(metricStatus==null){
	    System.out.println(getClass().getName()+" metricStatus=null");
	}

	HtmlTableCell cell=new HtmlTableCell(link,metricStatus.color());

	return cell;
	
    }
    public HtmlTable shortHtmlTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(shortStatusCell());
	return ht;
	
    }
    public String boolean2string(boolean inputBoolean){
	String result="";
	if (inputBoolean){
	    result="Y";
	}else{
	    result="N";
	}
	return result;
    }
    public boolean getActive(){
	return active;
    }
    public boolean swapActive(){
	if(active){
	    return false;
	}else{
	    return true;
	}
    }
    public boolean updateActive(){
	boolean result=true;
	String updateStatement="UPDATE Services SET Active=? WHERE ProbeId=?"; 
	PreparedStatement updatePreparedStatement=null;
	try{
	    updatePreparedStatement=db.getConn().prepareStatement(updateStatement);
	    updatePreparedStatement.setBoolean(1,this.swapActive());
	    updatePreparedStatement.setString(2,probeId);
	    updatePreparedStatement.executeUpdate();
	    active=this.swapActive();
	    result=true;
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating active status of service "+probeId);
	    System.out.println(getClass().getName()+" "+updateStatement);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    updatePreparedStatement.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+updateStatement);
	    System.out.println(getClass().getName()+" "+e);
	}

	// now mark the entry in log
	String logUpdateStatement="INSERT INTO ActionHistory (MetricName,HostName,dn,message,action,TimeStamp) VALUES(?,?,?,?,?,NOW())";
	PreparedStatement insertLogStatement=null;
	try{
	    insertLogStatement=db.getConn().prepareStatement(logUpdateStatement);
	    insertLogStatement.setString(1,metricName);
	    insertLogStatement.setString(2,hostName);
	    insertLogStatement.setString(3,parameterBag.user.getDN());
	    insertLogStatement.setString(4,parameterBag.message);
	    insertLogStatement.setString(5,"Set Active to "+boolean2string(active));
	    insertLogStatement.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating activity log of service "+probeId);
	    System.out.println(getClass().getName()+" "+logUpdateStatement);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    insertLogStatement.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+logUpdateStatement);
	    System.out.println(getClass().getName()+" "+e);
	}    

	return result;
    }

    public String updateActivePage(){
	String result="<strong><br><br>";
	if(updateActive()){
	    if(active){
		result=result+"Service has been updated to active";
	    }else{
		result=result+"Service has been updated to inactive";
	    }
	}else{
	    result=result+"Service update failed";
	}
	result=result+"<br>";
	result=result+this.linkToServiceDetails();
	result=result+"</strong>";
	return result;
    }

    public String confirmUpdateActivePage(){
	String result="";
	result=result+"<strong><br>You requested to change status of service "+metricName+" on host "+hostName+" to Active="+this.swapActive()+"<br>";
	result=result+"<br>This operation will be logged<br>";	
	result=result+"<br>Your DN is "+parameterBag.user.getDN()+"<br>";
	result=result+"<BR>Type your message for the log here:<br>";
	result=result+"<form>";
	result=result+"<input type=\"hidden\" name=\"page\" value=\"" +parameterBag.pageAddress("Update Active For Primitive Service")+  "\">";
	result=result+"<input type=\"hidden\" name=\"probeId\" value=\"" +parameterBag.probeId+  "\">";
	String message="";
	result=result+"<input type=\"text\" name=\"message\" value=\""+message+"\" /><br>";
	if (this.swapActive()){
	    result=result+"<input type=\"submit\" value=\"Mark service as active\" />";
	}else{
	    result=result+"<input type=\"submit\" value=\"Mark service as inactive\" />";
	}
	result=result+"</strong>";
	return result;
    }

    public String saveServicePage(){
	String result="<H1>Save service "+probeId+"</h1><br>";
	result=result+"<br>";

	if(parameterBag.validUser=="yes"){

	    String saveServiceCommand="UPDATE Services SET CheckInterval=?,ProbeCommand=? where dbid=?";
	    PreparedStatement saveServiceStatement=null;
	    try{
		saveServiceStatement=db.getConn().prepareStatement(saveServiceCommand);
		saveServiceStatement.setInt(1,Integer.parseInt(parameterBag.checkInterval));
		saveServiceStatement.setString(2,parameterBag.probeCommand);
		saveServiceStatement.setInt(3,this.getId());
		saveServiceStatement.executeUpdate();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+saveServiceCommand);
		System.out.println(getClass().getName()+" probeCommand="+parameterBag.probeCommand);
		System.out.println(getClass().getName()+" checkInterval="+parameterBag.checkInterval);
		System.out.println(getClass().getName()+" dbid="+this.getId());
		System.out.println(getClass().getName()+" "+e);	    
	    }
	    try{
		saveServiceStatement.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" error when closing prepared statement");
		System.out.println(getClass().getName()+" "+saveServiceCommand);
		System.out.println(getClass().getName()+" "+e);	    
	    }	
	    
	    // log the update
	    String logUpdateStatement="INSERT INTO ActionHistory (MetricName,HostName,dn,message,action,TimeStamp) VALUES(?,?,?,?,?,NOW())"; 
	    PreparedStatement insertLogStatement=null;
	    try{
		insertLogStatement=db.getConn().prepareStatement(logUpdateStatement);
		insertLogStatement.setString(1,metricName);
		insertLogStatement.setString(2,hostName);
		insertLogStatement.setString(3,parameterBag.user.getDN());
		insertLogStatement.setString(4," ");
		insertLogStatement.setString(5,"updated service "+metricName+" on host "+hostName);
		insertLogStatement.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when updating activity log of service "+probeId);
		System.out.println(getClass().getName()+" "+logUpdateStatement);
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		insertLogStatement.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+logUpdateStatement);
		System.out.println(getClass().getName()+" "+e);
	    }

	    
	    result=result+"<br>";
	    result=result+"Service data saved:<br>";
	    result=result+"probeId="+parameterBag.probeId+"<br>";	
	    //result=result+"saveServiceCommand="+saveServiceCommand+"<br>";
	    //result=result+"probeCommand="+parameterBag.probeCommand+"<br>";
	    result=result+"checkInterval="+parameterBag.checkInterval+"<br>";
	    result=result+"probeCommand="+parameterBag.probeCommand+"<br>";

	    HtmlLink link=new HtmlLink(getLinkToNodeDetailsPage(),"Go to service details page");
	    result=result+link.toHtml()+"<br>";
	    
	}else{
	    result=result+"You are not authorized to perform this operation<BR>";
	}

	return result;
    }

    public String editServicePage(){
	String result="";
	if(parameterBag.validUser=="yes"){
	    return serviceEditTable().toHtml();
	}else{
	    return "You are not authorized to perform this operation";
	}
    }

    public HtmlTable serviceEditTable(){
	String result="";
	result="<strong>ProbeId: </strong>"+probeId +"<br>";
	result=result+"<strong>HostName: </strong>"+hostName+"<br>";
	result=result+"<strong>Service: </strong>"+metricName+"<br>";
	result=result+"<strong>ServiceDescription: </strong>"+serviceDescription+"<br>";
	result=result+"<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">";	
	result=result+"<strong>Check Interval (min)</strong> <input type=\"text\" name=\"checkInterval\" value=\""+checkInterval+"\" /><br>";
	result=result+"<strong>Probe Command: </strong> <input type=\"text\" name=\"probeCommand\" value=\""+probeCommand+"\" /><br>";
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Primitive Service")+"\" />";
	//result=result+"<input type=\"hidden\" name=\"probeId\" value=\""+parameterBag.probeId+"\" />";
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+Integer.toString(this.getId())+"\" />";
	result=result+"<input type=\"hidden\" name=\"save\" value=\"yes\" /><br>";

	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";

	HtmlTableCell hc =new HtmlTableCell(result);
	hc.alignLeft();
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(hc);	
	return ht;	  
    }

    public HtmlTable fullHtmlTable(){
	String result="";
	result=result+"<strong>ProbeId: </strong>"+probeId +"<br>";
	result=result+"<strong>dbid   : </strong>"+dbid +"<br>";



	if(this.active){
	    result=result+"<strong>The service is active</strong><br>";
	}else{
	    result=result+"<strong>The service is not active, tests have been disabled</strong><br>";
	}
	if(parameterBag.validUser=="yes"){
	    result=result+setActiveButton.toHtml();
	}
	result=result+"<br>";
	
	if(!dashboardComment.equals("")){
	    result=result+"<strong>"+dashboardComment+"</strong><br>";
	}

	result=result+"<strong>HostName: </strong>"+hostName+"<br>";
	result=result+"<strong>Service: </strong>"+metricName+"<br>";
	result=result+"<strong>ServiceDescription: </strong>"+serviceDescription+"<br>";
	result=result+"<strong>Timestamp: </strong>"+timestamp+"<br>";

	result=result+"<strong>ServiceType: </strong>"+serviceType+"<br>";
	result=result+"<strong>ServiceUri: </strong>"+serviceUri+"<br>";

	result=result+"<strong>gatheredAt: </strong>"+gatheredAt+"<br>";
	result=result+"<strong>ProbeCommand: </strong>"+probeCommand+"<br>";
	result=result+"<strong>SummaryData: </strong>"+summaryData+"<br>";
	result=result+"<strong>DetailsData: </strong>"+detailsData+"<br>";

	result=result+"<strong>Check Interval: </strong>"+Integer.toString(checkInterval)+" (min)<br>";
	result=result+"<strong>Last check time: </strong>"+lastCheckTime+"<br>";
	result=result+"<strong>Next check time: </strong>"+nextCheckTime;
	// if(parameterBag.validUser=="yes"){
	//     result=result+fb.toHtml();
	// }
	result=result+"<br>";
	result=result+"<strong>"+this.getLinkToForceTestPrimitiveServicePage("Force test of this service NOW!").toHtml()+"</strong><br>";


	UserInfo currentUser=parameterBag.getUser();
	//result=result+currentUser.getDN()+"<br>";
	if (currentUser.isApproved()){
	    if(currentUser.hasRole("admin")){
		String editPage="Edit Primitive Service";
		EditServiceButton eB=new EditServiceButton(parameterBag,db,editPage);
		result=result+eB.toHtml()+"<br>";
	    }
	}
	    

	// build the address to history table page
	
	//ParameterBag temporaryParameterBag2 = (ParameterBag)parameterBag.clone();
	//temporaryParameterBag2.page=ParameterBag.pageAddress("Primitive Node History Table");
	//String urlOfHistoryTablePage=temporaryParameterBag2.makeLink();
	//HtmlLink linkToHistoryTablePage=new HtmlLink(urlOfHistoryTablePage,"Link to history table");

	HtmlLink linkToHistoryTablePage = this.makeLinkToHistoryTablePage("Link to history table");
	result=result+linkToHistoryTablePage.toHtml()+"<br>";

	//result=result+"query: "+initialisationSqlQuery;
	
	HtmlTableCell hc =new HtmlTableCell(result,metricStatus.color());
	hc.alignLeft();
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(hc);

	return ht;	    	    		
    }

    
   
    public void getHistory(){

	String getHistorySql="select * from MetricRecord where HostName=? and MetricName=? ";

	IntervalSelector iS = new IntervalSelector(parameterBag);
	iS.setTimeVariable("Timestamp");
	iS.setTimeZoneShift(0);
	getHistorySql=getHistorySql+" "+iS.buildQuery(parameterBag.interval);

	history.clear();
	PreparedStatement getHistoryPreparedSql=null;
	try{
	    
	    getHistoryPreparedSql= db.getConn().prepareStatement(getHistorySql);
	    getHistoryPreparedSql.setString(1,hostName);
	    getHistoryPreparedSql.setString(2,metricName);

	    ResultSet rs=getHistoryPreparedSql.executeQuery(); 

	    int count = 0;
	    while (rs.next ()){
		PrimitiveServiceHistoryRecord historyRecord = new PrimitiveServiceHistoryRecord();
		historyRecord.metricStatus = new ProbeStatus(rs.getString("MetricStatus"));
		historyRecord.timestamp    = rs.getTimestamp("Timestamp");
		historyRecord.serviceType  = rs.getString("ServiceType");
		historyRecord.serviceUri   = rs.getString("ServiceUri");
		historyRecord.gatheredAt   = rs.getString("GatheredAt");
		historyRecord.summaryData  = rs.getString("SummaryData");
		historyRecord.detailsData  = rs.getString("DetailsData");
		
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



    public String getHistoryTablePage(){
	String result="";

	IntervalSelector iS=new IntervalSelector(parameterBag);

	result=result+iS.toHtml()+"<br>";

	HtmlTable historyTable=getHistoryTable();
	result=result+historyTable.toHtml();
	return result;
    }
    

    public HtmlTable getHistoryTable(){
	
	getHistory();
	
	HtmlTable ht=new HtmlTable(3);
	ht.setBorder(0);
	ht.setPadding(0);

	Iterator iter = history.iterator();
	while (iter.hasNext()){
	    PrimitiveServiceHistoryRecord currentRecord = (PrimitiveServiceHistoryRecord)iter.next();
	    ht.addCell(new HtmlTableCell(currentRecord.timestamp.toString()));
	    ht.addCell(new HtmlTableCell(currentRecord.metricStatus.statusWord(),currentRecord.metricStatus.color()));
	    HtmlTableCell detailsCell=new HtmlTableCell(currentRecord.summaryData);
	    detailsCell.alignLeft();
	    ht.addCell(detailsCell);
	}

	return ht;
    }
    public HtmlLink getLinkToForceTestPrimitiveServicePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Force Test Primitive Service")  );
	paramBagLocal.addParam("id",Integer.toString(this.getId()));
	//paramBagLocal.addParam("hostName",hostName);
	//paramBagLocal.addParam("serviceName",metricName);
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToServiceDetailPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("perfSonar Primitive")  );
	paramBagLocal.addParam("hostName",hostName);
	paramBagLocal.addParam("serviceName",metricName);
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink linkToServiceDetails(){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("perfSonar Primitive")  );
	paramBagLocal.addParam("hostName",hostName);
	paramBagLocal.addParam("serviceName",metricName);
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,"Service "+metricName+" on  host "+hostName);
	return link;
    }
    
    public String forceTestNowPage(){
	String result="<H1>Force test of Primitive Service </h1><br>";
	this.setNextCheckTimeToNow();
	result=result+"<strong>The service "+this.getServiceName()+" has been signalled. It should perform status check in a few minutes</status><br>";
	result=result+"<br><br>";
	result=result+"<strong>"+this.getLinkToServiceDetailPage("Go back to service details").toHtml()+"</strong>";
	return result;
    }




}
