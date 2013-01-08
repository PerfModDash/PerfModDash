package gov.bnl.racf.exda;

import java.lang.Math;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;
import java.awt.BasicStroke;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONObject;

public class PerfSonarService
{  
    private ParameterBag parameterBag=null;
    private DbConnector db=null;

    private String matrixType=null;

    private PerfSonarHost sourceHost=null;
    private PerfSonarHost destinationHost=null;
    private PerfSonarHost monitorHost=null;

    // internal details
    public String detailPageNameLatency="Latency Node";
    public String detailPageNameThroughput="Throughput Node";
    public String detailPageName="";

    public Timestamp state_time = null;
    public ProbeStatus status=null;

    // type specific variables

    public double throughput_min=0.0;
    public double throughput_max=0.0;
    public double throughput_avg=0.0;
    
    public double latency_min=0.0;
    public double latency_max=0.0;
    public double latency_avg=0.0;

    public double sigma=0.0;

    public String metricName=null;
    public String metricType=null;
    public ProbeStatus metricStatus = null; 
    public Timestamp timestamp = null;
    public String serviceType = null;
    public String serviceUri=null;
    public String gatheredAt=null;
    public String summaryData=null;
    public String detailsData = null;
    public String performanceData=null;
    public String voName=null;
    public int    samUploadFlag=0;
    public String hostName=null;
    public String hostNameDescription=null;
    public String hostName2=null;
    public String hostName2Description=null;    
    public String hostName3=null;
    public String hostName3Description=null;
    public double performanceData1=0.;
    public double performanceData2=0.;
    public double performanceData3=0.;
    public double performanceData4=0.;
    public double performanceData5=0.;
    public String performanceDataName1="";
    public String performanceDataName2="";
    public String performanceDataName3="";
    public String performanceDataName4="";
    public String performanceDataName5="";
    public String performanceDataDescription1="";
    public String performanceDataDescription2="";
    public String performanceDataDescription3="";
    public String performanceDataDescription4="";
    public String performanceDataDescription5="";
    public String probeCommand="";
    public String probeId="";    
    public int dbid=-1;
    public int matrixId=-2;
    public String serviceDescription="";

    public String dashboardComment="";

    public int checkInterval=-1;

    public Timestamp lastCheckTime=null;
    public Timestamp nextCheckTime=null;

    public boolean active=true;
    public String serviceGroup="";
    public String siteNameOrdered="";
    public String siteName="";

    public String primitiveService="N";

    public String schedulerName="MAIN";

    public String probeRunning="N";

    public int primitiveServiceId=-1;

    private Scheduler scheduler=null;


    public PerfSonarService(ParameterBag paramBag,  DbConnector inputDb,int dbid){

	this.parameterBag=paramBag;
	this.db=inputDb;
	this.dbid=dbid;
    }
    public PerfSonarService(ParameterBag paramBag,  DbConnector inputDb){
	// create empty service element, to be filled later
	this.parameterBag=paramBag;
	this.db=inputDb;
    }
    // ===  end of constructors ===//
    public void setMetricName(String inputString){
	this.metricName=inputString;
    }
    public String getMetricName(){
	return this.metricName;
    }
    public void setMetricStatus(int inpitVar){
	this.metricStatus = new ProbeStatus(inpitVar);
    }
    public void setMetricStatus(ProbeStatus inpitVar){
	this.metricStatus = inpitVar;
    }
    public void setMetricStatus(String inpitVar){
	this.metricStatus = new ProbeStatus( inpitVar);
    }
    public ProbeStatus getMetricStatus(){
	return this.metricStatus ;
    }
    public void setPerformanceDataName1(String inputString){
	this.performanceDataName1=inputString;
    }
    public String getPerformanceDataName1(){
	return this.performanceDataName1;
    }
    public void setPerformanceDataName2(String inputString){
	this.performanceDataName2=inputString;
    }
    public String getPerformanceDataName2(){
	return this.performanceDataName2;
    }
    public void setPerformanceDataName3(String inputString){
	this.performanceDataName3=inputString;
    }
    public String getPerformanceDataName3(){
	return this.performanceDataName3;
    }
    public void setPerformanceDataName4(String inputString){
	this.performanceDataName4=inputString;
    }
    public String getPerformanceDataName4(){
	return this.performanceDataName4;
    }
    public void setPerformanceDataName5(String inputString){
	this.performanceDataName5=inputString;
    }
    public String getPerformanceDataName5(){
	return this.performanceDataName5;
    }

    public int getDbid(){
	return this.dbid;
    }
    public void setDbid(int inputVar){
	this.dbid=inputVar;
    }

    public PerfSonarHost getSourceHost(){
	return this.sourceHost;
    }
    public void setSourceHost(PerfSonarHost inputVar){
	this.sourceHost=inputVar;
    }

    public PerfSonarHost getDestinationHost(){
	return this.destinationHost;
    }
    public void setDestinationHost(PerfSonarHost inputVar){
	this.destinationHost=inputVar;
    }

    public PerfSonarHost getMonitorHost(){
	return this.monitorHost;
    }
    public void setMonitorHost(PerfSonarHost inputVar){
	this.monitorHost=inputVar;
    }

    public String getSourceHostName(){
	return this.getSourceHost().getHostName();
    }
    public String getDestinationHostName(){
	return this.getDestinationHost().getHostName();
    }
    public String getMonitorHostName(){
	return this.getMonitorHost().getHostName();
    }


    public void setHostName(String hostName){
	this.hostName=hostName;
    }
    public String getHostName(){
	return this.hostName;
    }
    public void setHostName2(String hostName2){
	this.hostName2=hostName2;
    }
    public String getHostName2(){
	return this.hostName2;
    }
    public void setHostName3(String hostName3){
	this.hostName3=hostName3;
    }
    public String getHostName3(){
	return this.hostName3;
    }

    public String getSource(){
	return this.sourceHost.getHostName();
    }
    public String getDestination(){
	return this.destinationHost.getHostName();
    }
    public String getMonitor(){
	return this.monitorHost.getHostName();
    }

    public String getSchedulerName(){
	return this.schedulerName;
    }
    public void setSchedulerName(String inputVar){
	this.schedulerName=inputVar;
    }
    
    public String getPrimitiveService(){
	return this.primitiveService;
    }
    public void setPrimitiveService(String inputVar){
	this.primitiveService=inputVar;
    }


    public void setHostNameDescription(String inputString){
	this.hostNameDescription=inputString;
    }
    public String getHostNameDescription(){
	return this.hostNameDescription;
    }
    public void setHostName2Description(String inputString){
	this.hostName2Description=inputString;
    }
    public String getHostName2Description(){
	return this.hostName2Description;
    }
    public void setHostName3Description(String inputString){
	this.hostName3Description=inputString;
    }
    public String getHostName3Description(){
	return this.hostName3Description;
    }

    public void setProbeCommand(String probeCommand){
	this.probeCommand=probeCommand;
    }
    public String getProbeCommand(){
	return this.probeCommand;
    }
    public void setProbeId(String probeId){
	this.probeId=probeId;
    }
    public String getProbeId(){
	return this.probeId;
    }

    public void setMatrixId(int inputInt){
	this.matrixId=inputInt;
    }
    public int getMatrixId(){
	return this.matrixId;
    }

    public void setServiceDescription(String inputString){
	this.serviceDescription=inputString;
    }
    public String getServiceDescription(){
	return this.serviceDescription;
    }

    public void setPerformanceDataDescription1(String inputString){
	this.performanceDataDescription1=inputString;
    }
    public String getPerformanceDataDescription1(){
	return this.performanceDataDescription1;
    }
    public void setPerformanceDataDescription2(String inputString){
	this.performanceDataDescription2=inputString;
    }
    public String getPerformanceDataDescription2(){
	return this.performanceDataDescription2;
    }
    public void setPerformanceDataDescription3(String inputString){
	this.performanceDataDescription3=inputString;
    }
    public String getPerformanceDataDescription3(){
	return this.performanceDataDescription3;
    }
    public void setPerformanceDataDescription4(String inputString){
	this.performanceDataDescription4=inputString;
    }
    public String getPerformanceDataDescription4(){
	return this.performanceDataDescription4;
    }
    public void setPerformanceDataDescription5(String inputString){
	this.performanceDataDescription5=inputString;
    }
    public String getPerformanceDataDescription5(){
	return this.performanceDataDescription5;
    }

    public void setServiceGroup(String inputString){
	this.serviceGroup=inputString;
    }
    public String getServiceGroup(){
	return this.serviceGroup;
    }

    public void setServiceMatrixName(String inputString){
	this.setServiceGroup(inputString);
    }
    public String getServiceMatrixName(){
	return this.getServiceGroup();
    }

    public void setSiteNameOrdered(String inputVar){
	this.siteNameOrdered=inputVar;
    }
    public String getSiteNameOrdered(){
	return this.siteNameOrdered;
    }

    public void setActive(boolean inputBoolean){
	this.active=inputBoolean;
    }
    public boolean getActive(){
	return this.active;
    }

    public void setProbeRunning(String inputVar){
	this.probeRunning=inputVar;
    }
    public String getProbeRunning(){
	return this.probeRunning;
    }

    public void setCheckInterval(int inputVar){
	this.checkInterval=inputVar;
    }
    public int getCheckInterval(){
	return this.checkInterval;
    }

    public String getDisplayInterval(){
	return parameterBag.interval;
    }

    public void setServiceMatrixType(String serviceMarixType){
	this.matrixType=serviceMarixType;
    }
    public String getServiceMatrixType(){
	return this.matrixType;
    }

    public void setSiteName(String inputString){
	this.siteName=inputString;
    }
    public String getSiteName(){
	return this.siteName;
    }

    public void setNextCheckTime(Timestamp inputVar){
	this.nextCheckTime=inputVar;
    }
    public Timestamp getNextCheckTime(){
	return this.nextCheckTime;
    }
    public void setLastCheckTime(Timestamp inputVar){
	this.lastCheckTime=inputVar;
    }
    public Timestamp getLastCheckTime(){
	return this.lastCheckTime;
    }
    public void setPrimitiveServiceId(int inputVar){
	this.primitiveServiceId=inputVar;
    }
    public int getPrimitiveServiceId(){
	return this.primitiveServiceId;
    }
    public void setDetailsData(String inputVar){
	this.detailsData=inputVar;
    }
    public String getDetailsData(){
	return this.detailsData;
    }
    public void setScheduler(String inputVar){
	this.scheduler=new Scheduler(this.parameterBag,this.db,inputVar);
    }
    public Scheduler getScheduler(){
	return this.scheduler;
    }
    public void setSummaryData(String inputVar){
	this.summaryData=inputVar;
    }
    public String getSummaryData(){
	return this.summaryData;
    }
    public void setPerformanceData1(double inputVar){
	this.performanceData1=inputVar;
    }
    public double getPerformanceData1(){
	return this.performanceData1;
    }
    public void setPerformanceData2(double inputVar){
	this.performanceData2=inputVar;
    }
    public double getPerformanceData2(){
	return this.performanceData2;
    }
    public void setPerformanceData3(double inputVar){
	this.performanceData3=inputVar;
    }
    public double getPerformanceData3(){
	return this.performanceData3;
    }
    public void setPerformanceData4(double inputVar){
	this.performanceData4=inputVar;
    }
    public double getPerformanceData4(){
	return this.performanceData4;
    }
    public void setPerformanceData5(double inputVar){
	this.performanceData5=inputVar;
    }
    public double getPerformanceData5(){
	return this.performanceData5;
    }
    //  ===  load methids === //
    public void load(){
	//	this.loadRecentStatus();
	this.loadDataFromServicesTable();
    }
    public boolean loadDataFromServicesTable(){
	boolean result=true;
	String query="select * from Services WHERE dbid=?";
	PreparedStatement queryPS= null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getDbid());
	    ResultSet rs=queryPS.executeQuery();
	    this.unpackServicesResultSet(rs);
	    
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading execution directoved for PerfSonarService");
	    System.out.println(getClass().getName()+" dbid="+this.getDbid());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}	
	return result;
    }
    private void unpackServicesResultSet(ResultSet rs){
	try{
	    while (rs.next ()){
		this.setProbeCommand(rs.getString("ProbeCommand"));
		this.setDbid(        rs.getInt("dbid"));
		this.setMetricName(  rs.getString("MetricName"));
		this.setHostName(    rs.getString("HostName"));
		this.setPerformanceDataName1(rs.getString("PerformanceDataName1"));
		this.setPerformanceDataName2(rs.getString("PerformanceDataName2"));
		this.setPerformanceDataName3(rs.getString("PerformanceDataName3"));
		this.setPerformanceDataName4(rs.getString("PerformanceDataName4"));
		this.setPerformanceDataName5(rs.getString("PerformanceDataName5"));
		this.setHostNameDescription( rs.getString("HostNameDescription"));	
		this.setHostName2(           rs.getString("HostName2"));
		this.setHostName2Description(rs.getString("HostName2Description"));
		this.setHostName3(           rs.getString("HostName3"));
		this.setHostName3Description(rs.getString("HostName3Description"));
		this.setProbeCommand(        rs.getString("ProbeCommand"));
		this.setProbeId(             rs.getString("ProbeId"));
		this.setServiceDescription(  rs.getString("ServiceDescription"));
		this.setPerformanceDataDescription1(rs.getString("PerformanceDataDescription1"));
		this.setPerformanceDataDescription2(rs.getString("PerformanceDataDescription2"));
		this.setPerformanceDataDescription3(rs.getString("PerformanceDataDescription3"));
		this.setPerformanceDataDescription4(rs.getString("PerformanceDataDescription4"));
		this.setPerformanceDataDescription5(rs.getString("PerformanceDataDescription5"));
		this.setServiceGroup(           rs.getString("ServiceGroup"));
		this.setSiteName(           rs.getString("SiteName"));
		this.setCheckInterval(           rs.getInt("CheckInterval"));
		this.setProbeRunning(           rs.getString("ProbeRunning"));
		this.setNextCheckTime(           rs.getTimestamp("NextCheckTime"));
		this.setLastCheckTime(           rs.getTimestamp("LastCheckTime"));
		this.setSiteNameOrdered(           rs.getString("SiteNameOrdered"));
		this.setPrimitiveService(           rs.getString("PrimitiveService"));
		this.setSchedulerName(           rs.getString("SchedulerName"));
		this.setScheduler(this.getSchedulerName());
		this.setMatrixId(           rs.getInt("matrixId"));
		this.setActive(           rs.getBoolean("Active"));
		this.setPrimitiveServiceId(           rs.getInt("primitiveServiceId"));
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" Error while unpacking result set");
	    System.out.println(getClass().getName()+" "+e);
	}
    }



    // === methods for making Json objects === //
    public JSONObject executionDirectivesJson(){
	JSONObject json = new JSONObject();
	json.put("dbid", this.getDbid());
	json.put("ProbeCommand",this.getProbeCommand());
	return json;
    }

    public boolean lockService(){
	boolean result=true;
	String query="UPDATE Services SET ProbeRunning='Y' WHERE dbid=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getDbid());
	    queryPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when locking service");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" id="+this.getDbid());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }

    private int sanitizeStatusCode(int status,String output){
	int OK=0;
	int WARNING=1;
	int CRITICAL=2;
	int UNKNOWN=3;
	int TIMEOUT = 4;

	String OK_STRING="OK";
	String WARNING_STRING="WARNING";
	String CRITICAL_STRING="CRITICAL";
	String UNKNOWN_STRING="UNKNOWN";
	String TIMEOUT_STRING="Error contacting MA";


	int result=OK;
	if(status==OK || status==WARNING || status==CRITICAL || status==UNKNOWN){
	    result=status;
	}else{
	    if(output.indexOf(OK_STRING)!=-1){
		result=OK;
	    }else{
		if(output.indexOf(WARNING_STRING)!=-1){
		    result=WARNING;
		}else{
		    if(output.indexOf(CRITICAL_STRING)!=-1){
			result=CRITICAL;
		    }else{
			
			result=UNKNOWN;
		    }
		}
	    }
	}
	if (output.indexOf(TIMEOUT_STRING)>-1){
	    result=TIMEOUT;
	}
	return result;
    }

    private float unpackPerformanceData(String performanceDataName){
	float result=(float)0.0;
	String detailsData=this.getDetailsData();

	String[] temp;
	String delimiter = ";;";
	temp = detailsData.split(delimiter);
	for(int i =0; i < temp.length ; i++){
	    if(temp[i].trim().indexOf(performanceDataName)>=0){
		String temp2[] = temp[i].split("=");
		String key=temp2[0].trim();
		String valString=temp2[1].trim().replace("Gbps","");
                valString=valString.replace("pps","");
		try{
		    result = Float.valueOf(valString).floatValue();
		}
		catch (Exception e){}
	    }
	}
 	return result;
    }

    public boolean insertCurrentStatus(){
	boolean result=true;
	String query1="DELETE FROM MetricRecordSummary where dbid=?";
	PreparedStatement query1PS= null;
	try{
	    query1PS= db.getConn().prepareStatement(query1);
	    query1PS.setInt(1,this.getDbid());
	    query1PS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when deleting record");
	    System.out.println(getClass().getName()+" "+query1);
	    System.out.println(getClass().getName()+" dbid="+this.getDbid());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    query1PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query1);
	    System.out.println(getClass().getName()+" "+e);
	}
                    
	String query2="INSERT INTO MetricRecordSummary (MetricName,MetricStatus,GatheredAt,SummaryData,DetailsData,HostName,PerformanceData1,PerformanceData2,PerformanceData3,PerformanceData4,PerformanceData5,PerformanceDataName1,PerformanceDataName2,PerformanceDataName3,PerformanceDataName4,PerformanceDataName5,HostNameDescription,HostName2,HostName2Description,HostName3,HostName3Description,ProbeCommand,ProbeId,ServiceDescription,PerformanceDataDescription1,PerformanceDataDescription2,PerformanceDataDescription3,PerformanceDataDescription4,PerformanceDataDescription5,dbid,matrixId,Active,primitiveServiceId,NextCheckTime,LastCheckTime,Timestamp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,DATE_ADD(NOW(), INTERVAL ?-1 MINUTE),NOW(),NOW())";

	//1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
        //          1                   2                   3
	PreparedStatement query2PS= null;
	try{
	    query2PS= db.getConn().prepareStatement(query2);

	    query2PS.setString(1,this.getMetricName());
	    query2PS.setString(2,this.getMetricStatus().toString());
	    query2PS.setString(3,this.getScheduler().getSchedulerHost());
	    query2PS.setString(4,this.getSummaryData().substring(0,Math.min(this.getSummaryData().length(),250)));
	    query2PS.setString(5,this.getDetailsData());

	    query2PS.setString(6,this.getHostName());

	    query2PS.setDouble(7,this.getPerformanceData1());
	    query2PS.setDouble(8,this.getPerformanceData2());
	    query2PS.setDouble(9,this.getPerformanceData3());
	    query2PS.setDouble(10,this.getPerformanceData4());
	    query2PS.setDouble(11,this.getPerformanceData5());

	    query2PS.setString(12,this.getPerformanceDataName1());
	    query2PS.setString(13,this.getPerformanceDataName2());
	    query2PS.setString(14,this.getPerformanceDataName3());
	    query2PS.setString(15,this.getPerformanceDataName4());
	    query2PS.setString(16,this.getPerformanceDataName5());

	    query2PS.setString(17,this.getHostNameDescription());
	    query2PS.setString(18,this.getHostName2());
	    query2PS.setString(19,this.getHostName2Description());
	    query2PS.setString(20,this.getHostName3());
	    query2PS.setString(21,this.getHostName3Description());

	    query2PS.setString(22,this.getProbeCommand());
	    query2PS.setString(23,this.getProbeId());
	    query2PS.setString(24,this.getServiceDescription());

	    query2PS.setString(25,this.getPerformanceDataDescription1());
	    query2PS.setString(26,this.getPerformanceDataDescription2());
	    query2PS.setString(27,this.getPerformanceDataDescription3());
	    query2PS.setString(28,this.getPerformanceDataDescription4());
	    query2PS.setString(29,this.getPerformanceDataDescription5());

	    query2PS.setInt(30,this.getDbid());
	    query2PS.setInt(31,this.getMatrixId());
	    query2PS.setBoolean(32,this.getActive());
	    query2PS.setInt(33,this.getPrimitiveServiceId());
	    query2PS.setInt(34,this.getCheckInterval());

	    query2PS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when inserting record");
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" dbid="+this.getDbid());

	    System.out.println(getClass().getName()+" getMetricName="+this.getMetricName());
	    System.out.println(getClass().getName()+" getMetricStatus()="+this.getMetricStatus().toString());
	    System.out.println(getClass().getName()+" getScheduler()="+this.getScheduler().getSchedulerHost());
	    System.out.println(getClass().getName()+" getSummaryData()="+this.getSummaryData());
	    System.out.println(getClass().getName()+" getDetailsData()="+this.getDetailsData());

	    System.out.println(getClass().getName()+" getHostName()="+this.getHostName());
	    System.out.println(getClass().getName()+" getPerformanceData1()="+this.getPerformanceData1());
	    System.out.println(getClass().getName()+" getPerformanceData2()="+this.getPerformanceData2());
	    System.out.println(getClass().getName()+" getPerformanceData3()="+this.getPerformanceData3());
	    System.out.println(getClass().getName()+" getPerformanceData4()="+this.getPerformanceData4());
	    System.out.println(getClass().getName()+" getPerformanceData5()="+this.getPerformanceData5());
	    System.out.println(getClass().getName()+" getPerformanceDataName1()="+this.getPerformanceDataName1());
	    System.out.println(getClass().getName()+" getPerformanceDataName2()="+this.getPerformanceDataName2());
	    System.out.println(getClass().getName()+" getPerformanceDataName3()="+this.getPerformanceDataName3());
	    System.out.println(getClass().getName()+" getPerformanceDataName4()="+this.getPerformanceDataName4());
	    System.out.println(getClass().getName()+" getPerformanceDataName5()="+this.getPerformanceDataName5());
	    System.out.println(getClass().getName()+" getHostNameDescription()="+this.getHostNameDescription());
	    System.out.println(getClass().getName()+" getHostName2()="+this.getHostName2());
	    System.out.println(getClass().getName()+" getHostName2Description()="+this.getHostName2Description());
	    System.out.println(getClass().getName()+" getHostName3()="+this.getHostName3());
	    System.out.println(getClass().getName()+" getHostName3Description()="+this.getHostName3Description());
            System.out.println(getClass().getName()+" getProbeCommand()="+this.getProbeCommand());
	    System.out.println(getClass().getName()+" getProbeId()="+this.getProbeId());
	    System.out.println(getClass().getName()+" getServiceDescription()="+this.getServiceDescription());
	    System.out.println(getClass().getName()+" getPerformanceDataDescription1()="+this.getPerformanceDataDescription1());
	    System.out.println(getClass().getName()+" getPerformanceDataDescription2()="+this.getPerformanceDataDescription2());
	    System.out.println(getClass().getName()+" getPerformanceDataDescription3()="+this.getPerformanceDataDescription3());
	    System.out.println(getClass().getName()+" getPerformanceDataDescription4()="+this.getPerformanceDataDescription4());
	    System.out.println(getClass().getName()+" getPerformanceDataDescription5()="+this.getPerformanceDataDescription5());
	    System.out.println(getClass().getName()+" getCheckInterval()="+this.getCheckInterval());
	    System.out.println(getClass().getName()+" getDbid()="+this.getDbid());
	    System.out.println(getClass().getName()+" getMatrixId()="+this.getMatrixId());
	    System.out.println(getClass().getName()+" getActive()="+this.getActive());
	    System.out.println(getClass().getName()+" getPrimitiveServiceId()="+this.getPrimitiveServiceId());


	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    query2PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }
    public boolean insertHistory(){

	boolean result=true;
	String query3="INSERT INTO MetricRecord (MetricName,MetricStatus,GatheredAt,SummaryData,DetailsData,HostName,PerformanceData1,PerformanceData2,PerformanceData3,PerformanceData4,PerformanceData5,HostName2,HostName3,ProbeId,dbid,matrixId,Active,Timestamp) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";

	PreparedStatement query3PS= null;
	try{
	    query3PS= db.getConn().prepareStatement(query3);
	    query3PS.setString(1,this.getMetricName());
	    query3PS.setString(2,this.getMetricStatus().toString());
	    query3PS.setString(3,this.getScheduler().getSchedulerHost());

	    String summary=this.getSummaryData();
	    int length=254;
	    if (summary != null && summary.length() > length){
		summary = summary.substring(0, length);
	    }
	    query3PS.setString(4,summary);
	    query3PS.setString(5,this.getDetailsData());

	    query3PS.setString(6,this.getHostName());

	    query3PS.setDouble(7,this.getPerformanceData1());
	    query3PS.setDouble(8,this.getPerformanceData2());
	    query3PS.setDouble(9,this.getPerformanceData3());
	    query3PS.setDouble(10,this.getPerformanceData4());
	    query3PS.setDouble(11,this.getPerformanceData5());


	    query3PS.setString(12,this.getHostName2());
	    query3PS.setString(13,this.getHostName3());

	    query3PS.setString(14,this.getProbeId());

	    query3PS.setInt(15,this.getDbid());
	    query3PS.setInt(16,this.getMatrixId());
	    query3PS.setBoolean(17,this.getActive());

	    query3PS.executeUpdate();

	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when inserting record");
	    System.out.println(getClass().getName()+" "+query3);
	    System.out.println(getClass().getName()+" dbid="+this.getDbid());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    query3PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query3);
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }
    public boolean updateServicesTable(){
	boolean result=true;
	// finally update Services table
	String query4="UPDATE Services SET NextCheckTime=DATE_ADD(NOW(), INTERVAL ?-1 MINUTE), LastCheckTime=NOW(), ProbeRunning='N' WHERE dbid=?";
	PreparedStatement query4PS= null;
	try{
	    query4PS= db.getConn().prepareStatement(query4);
	    query4PS.setInt(1,this.getCheckInterval());
	    query4PS.setInt(2,this.getDbid());
	    query4PS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when deleting record");
	    System.out.println(getClass().getName()+" "+query4);
	    System.out.println(getClass().getName()+" dbid="+this.getDbid());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    query4PS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query4);
	    System.out.println(getClass().getName()+" "+e);
	}	
	return result;
    }

    public boolean update(int status,String output){
	boolean result=true;
	output=output.replace("\'","");
	output=output.trim();
	status=this.sanitizeStatusCode(status,output);
	this.setMetricStatus(status);

        try{
	    int locationOfMarker=output.indexOf("|");
            this.setSummaryData(output.substring(0,locationOfMarker));
            this.setDetailsData(output.substring(locationOfMarker+1));
	}catch(Exception e){
	    this.setSummaryData(output);
	    this.setDetailsData("");
	}
	
	if(!this.getPrimitiveService().equals("Y")){
	    this.setPerformanceData1(this.unpackPerformanceData(this.getPerformanceDataName1()));
	    this.setPerformanceData2(this.unpackPerformanceData(this.getPerformanceDataName2()));
	    this.setPerformanceData3(this.unpackPerformanceData(this.getPerformanceDataName3()));
	    this.setPerformanceData4(this.unpackPerformanceData(this.getPerformanceDataName4()));
	    this.setPerformanceData5(this.unpackPerformanceData(this.getPerformanceDataName5()));
	}else{
	    this.setPerformanceData1(0.0);
	    this.setPerformanceData2(0.0);
	    this.setPerformanceData3(0.0);
	    this.setPerformanceData4(0.0);
	    this.setPerformanceData5(0.0);
	}
	boolean result1=this.insertCurrentStatus();
	boolean result2=this.updateServicesTable();
	boolean result3=this.insertHistory();

	result=result1&result2&result3;

	return result;
    }
}
