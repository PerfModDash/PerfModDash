package gov.bnl.racf.exda;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;
import java.awt.BasicStroke;

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


public class ServiceMatrixElementAPD
{  
    private boolean emptyElement=true; // no data for element in metric summary table
    private boolean definedElement=false; // element undefined in Services table

    private ParameterBag parameterBag=null;
    private DbConnector db=null;

    private String matrixType=null;

    private PerfSonarHost sourceHost=null;
    private PerfSonarHost destinationHost=null;

    // internal details
    public String detailPageNameLatency="APD Latency Node";
    public String detailPageNameThroughput="APD Throughput Node";
    public String detailPageName="";

    public String source="";
    public String destination="";
    public String monitor=" ";
    
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

    public String voName=" ";
    public int    samUploadFlag=0;    

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

    public int fontSize=1;
    

    // history related variables
    public List<MatrixNodeHistoryRecord> history=new ArrayList<MatrixNodeHistoryRecord>();


    // plot related variables
    //public String imageFileName="temporary_file_delete_it.jpg";
    public int imageXsize=1000;
    public int imageYsize=600;
    public File imageFile=null;


    private String latencySampleCommand="undefined";
    private String throughputSampleCommand="/opt/perfsonar_ps/nagios/bin/check_apd_throughput.pl -u http://psmsu02.aglt2.org:8085/perfSONAR_PS/services/pSB -r 1296000 -s {SOURCE} -d {DESTINATION} -W 12";

    public ServiceMatrixElementAPD(ParameterBag paramBag,  DbConnector inputDb,int matrixId, PerfSonarHost sourceHost,PerfSonarHost destinationHost){
	this.parameterBag=paramBag;
	this.db=inputDb;

	this.matrixId=matrixId;
	this.sourceHost=sourceHost;
	this.destinationHost=destinationHost;


	this.setHostName2(this.sourceHost.getHostName());
	this.setHostName3(this.destinationHost.getHostName());

	ServiceMatrixRecord smr = new ServiceMatrixRecord(this.parameterBag,this.db,this.matrixId);
	this.matrixType=smr.getServiceMatrixType();

	String getSummaryStatus="SELECT * from MetricRecordSummary WHERE HostName2=? and HostName3=? and matrixId=? LIMIT 1";
	PreparedStatement getSummaryStatusQuery=null;
	try{
	    getSummaryStatusQuery=db.getConn().prepareStatement(getSummaryStatus);
	    getSummaryStatusQuery.setString(1,sourceHost.getHostName());
	    getSummaryStatusQuery.setString(2,destinationHost.getHostName());
	    getSummaryStatusQuery.setInt(3,this.matrixId);
	    ResultSet rs=getSummaryStatusQuery.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to unpack result set for matrix element");
	    System.out.println(getClass().getName()+" "+getSummaryStatus);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getSummaryStatusQuery.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+getSummaryStatus);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	
	if(this.emptyElement){
	    // check for this service in Services table
	    String checkServicesTable="SELECT probeId from Services WHERE HostName2=? and HostName3=? and MetricName=? LIMIT 1";
	    PreparedStatement checkServicesTablePS=null;
	    try{
		checkServicesTablePS=db.getConn().prepareStatement(checkServicesTable);

		checkServicesTablePS.setString(1,sourceHost.getHostName());
		checkServicesTablePS.setString(2,destinationHost.getHostName());
		checkServicesTablePS.setString(3,this.getMetricName());
		ResultSet rs = checkServicesTablePS.executeQuery();
		while(rs.next()){
		    this.probeId=rs.getString("ProbeId");
		    this.definedElement=true;
		}
		rs.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to unpack result set for matrix element");
		System.out.println(getClass().getName()+" "+checkServicesTable);
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		checkServicesTablePS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+checkServicesTable);
		System.out.println(getClass().getName()+" "+e);
	    }

	}
	this.getServiceData();
    }


    public ServiceMatrixElementAPD(ParameterBag paramBag,  DbConnector inputDb,String matrixType, PerfSonarHost sourceHost,PerfSonarHost destinationHost){
	this.parameterBag=paramBag;
	this.db=inputDb;
	this.matrixType=matrixType;
	this.sourceHost=sourceHost;
	this.destinationHost=destinationHost;

	String getSummaryStatus="SELECT * from MetricRecordSummary WHERE HostName2=? and HostName3=? and MetricName=? LIMIT 1";
	PreparedStatement getSummaryStatusQuery=null;
	try{
	    getSummaryStatusQuery=db.getConn().prepareStatement(getSummaryStatus);

	    getSummaryStatusQuery.setString(1,sourceHost.getHostName());
	    getSummaryStatusQuery.setString(2,destinationHost.getHostName());
	    getSummaryStatusQuery.setString(3,this.getMetricName());
	    ResultSet rs=getSummaryStatusQuery.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to unpack result set for matrix element");
	    System.out.println(getClass().getName()+" "+getSummaryStatus);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getSummaryStatusQuery.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+getSummaryStatus);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	
	if(this.emptyElement){
	    // check for this service in Services table
	    String checkServicesTable="SELECT probeId from Services WHERE HostName2=? and HostName3=? and MetricName=? LIMIT 1";
	    PreparedStatement checkServicesTablePS=null;
	    try{
		checkServicesTablePS=db.getConn().prepareStatement(checkServicesTable);

		checkServicesTablePS.setString(1,sourceHost.getHostName());
		checkServicesTablePS.setString(2,destinationHost.getHostName());
		checkServicesTablePS.setString(3,this.getMetricName());
		ResultSet rs = checkServicesTablePS.executeQuery();
		while(rs.next()){
		    this.probeId=rs.getString("ProbeId");
		    this.definedElement=true;
		}
		rs.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to unpack result set for matrix element");
		System.out.println(getClass().getName()+" "+checkServicesTable);
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		checkServicesTablePS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+checkServicesTable);
		System.out.println(getClass().getName()+" "+e);
	    }		

	}
	this.getServiceData();
    }

    public ServiceMatrixElementAPD(ParameterBag paramBag,  DbConnector inputDb,String probeId){
	this.parameterBag=paramBag;
	this.db=inputDb;

	this.probeId=probeId;

	this.loadServiceByProbeId();
    }

    public ServiceMatrixElementAPD(ParameterBag paramBag,  DbConnector inputDb,int dbid){

	this.parameterBag=paramBag;
	this.db=inputDb;
	this.dbid=dbid;

	this.loadServiceById();
    }
    public ServiceMatrixElementAPD(ParameterBag paramBag,  DbConnector inputDb){
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

    public void setStatus(ProbeStatus inputVar){
	this.status=inputVar;
	this.metricStatus=inputVar;
    }
    public ProbeStatus getStatus(){
	return status;
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
    public boolean hasBeenDefined(){
	if(this.definedElement){
	    return true;
	}else{
	    return false;
	}
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


    public String getSourceHostName(){
	return this.getSourceHost().getHostName();
    }
    public String getDestinationHostName(){
	return this.getDestinationHost().getHostName();
    }

    public void setHostName2(String hostName2){
	this.hostName2=hostName2;
	this.source=this.hostName2;
    }
    public String getHostName2(){
	return this.hostName2;
    }
    public void setHostName3(String hostName3){
	this.hostName3=hostName3;
	this.destination=this.hostName3;
    }
    public String getHostName3(){
	return this.hostName3;
    }

    public String getMonitor(){
	return " ";
    }

    public String getSource(){
	return this.sourceHost.getHostName();
    }
    public String getDestination(){
	return this.destinationHost.getHostName();
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

    public boolean isThroughput(){
	if(this.getServiceMatrixType().equals("throughput")){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isLatency(){
	if(this.getServiceMatrixType().equals("latency")){
	    return true;
	}else{
	    return false;
	}
    }

    public boolean isEmpty(){
	return this.emptyElement;
    }

    private String getPlotFileName(){
	String result="temp_plot_{INTERVAL}.jpg";
	//String result="temp_plot_{ID}_{INTERVAL}.jpg";
	result=result.replace("{INTERVAL}",this.getDisplayInterval()  );
	//result=result.replace("{ID}",Integer.toString(this.getDbid())  );
	return result;
    }

    public void fillRemainingParameters(){
	// to be used by empty service. Assumes that hostName,hostName2,hostName3, serviceMatrixType and matrixId have been filled
	ServiceMatrixRecord smr = new ServiceMatrixRecord(this.parameterBag,  this.db, this.matrixId);
	this.setServiceMatrixType(smr.getServiceMatrixType());

	if(this.isThroughput()){
	    this.setMetricName("net.perfsonar.service.ma.apd_throughput");
	}else{
	    this.setMetricName("net.perfsonar.service.ma.apd_latency");
	}
	this.setPerformanceDataName1("Max");
	this.setPerformanceDataName1("Average");
	this.setPerformanceDataName1("Min");
	this.setPerformanceDataName1("Standard_Deviation");	
	this.setPerformanceDataName1("Count");

	this.setHostNameDescription(" ");
	this.setHostName2Description("source host");
	this.setHostName3Description("destination host");
	


	String command="";
	if(this.isThroughput()){
	    command=this.throughputSampleCommand;
	}else{
	    command=this.latencySampleCommand;
	}
	command=command.replace("{SOURCE}",this.getSourceHost().getHostName());
	command=command.replace("{DESTINATION}",this.getDestinationHost().getHostName());
	this.setProbeCommand(command);
	
	if(this.isThroughput()){
	    this.setProbeId(Integer.toString(this.matrixId)+"_apd_throughput_"+this.getSource()+"_"+this.getDestination());
	}else{
	    this.setProbeId(Integer.toString(this.matrixId)+"_apd_latency_"+this.getSource()+"_"+this.getDestination());
	}
	if(this.isThroughput()){
	    this.setServiceDescription("APD Throughput Measurement");
	}else{
	    this.setServiceDescription("APD Latency Measurement");
	}

	this.setServiceGroup(smr.getServiceMatrixName());	
    }

    public boolean serviceExistsInDb(){
	// check if this service is defined in database, Services table
	// first, check according to dbid
	if(this.dbid==-1){
	    // dbid is not set, service does not exist in database
	    return false;
	}else{
	    // db id is set, but does it exist in database?
	    String query1="SELECT * FROM Services where dbid=?";
	    int numberOfElementsFound=0;
	    PreparedStatement query1PS=null;
	    try{
		query1PS=db.getConn().prepareStatement(query1);
		query1PS.setInt(1,this.dbid);
		ResultSet rs=query1PS.executeQuery();
		while(rs.next()){
		    numberOfElementsFound=numberOfElementsFound+1;
		}
		rs.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to execute query");
		System.out.println(getClass().getName()+" "+query1);
		System.out.println(getClass().getName()+" dbid="+this.dbid);
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		query1PS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query1);
		System.out.println(getClass().getName()+" "+e);
	    }
	    if(numberOfElementsFound>0){
		return true;
	    }else{
		// identifier does not reveal a service, let us check according to other firlds
		String query="SELECT * from Services where HostName2=? and HostName3=? and ServiceMatrixType=? and matrixId=?";
		PreparedStatement queryPS=null;
		numberOfElementsFound=0;
		try{
		    queryPS=db.getConn().prepareStatement(query);

		    queryPS.setString(1,this.getHostName2());
		    queryPS.setString(2,this.getHostName3());
		    queryPS.setString(3,this.getServiceMatrixType());
		    queryPS.setInt(4,this.getMatrixId());
		    ResultSet rs = queryPS.executeQuery();
		    while(rs.next()){
			this.dbid=rs.getInt("dbid");
			this.definedElement=true;
			numberOfElementsFound=numberOfElementsFound+1;
		    }
		    rs.close();
		}catch(Exception e){
		    System.out.println(getClass().getName()+" failed to execute query");
		    System.out.println(getClass().getName()+" "+query);

		    System.out.println(getClass().getName()+" HostName2="+this.getHostName2());
		    System.out.println(getClass().getName()+" HostName3="+this.getHostName3());
		    System.out.println(getClass().getName()+" ServiceMatrixType="+this.getServiceMatrixType());
		    System.out.println(getClass().getName()+" matrixId="+this.getMatrixId());
		    System.out.println(getClass().getName()+" "+e);
		}
		try{
		    queryPS.close();
		}catch(Exception e){
		    System.out.println(getClass().getName()+" failed to close prepared statement");
		    System.out.println(getClass().getName()+" "+query);
		    System.out.println(getClass().getName()+" "+e);
		}		    
		if(numberOfElementsFound>0){
		    return true;
		}else{
		    return false;
		}
	    }
	}
    }
    
    

    public void getParametersFromBag(){
	// get new values of parameters from bag
	this.setCheckInterval(Integer.parseInt(parameterBag.checkInterval));
	this.setProbeCommand(parameterBag.probeCommand);
	this.setSchedulerName(parameterBag.schedulerName);
    }

    public void insertNewElement(){
	this.fillFieldsForEmptyElement();
	this.insertElement();
    }

    public void fillFieldsForEmptyElement(){
	// fill fields for empty element.
	// the element must have defined source,destination and monitor hosts and matrixId

	String sourceHostName=this.getSourceHostName();
	String destinationHostName=this.getDestinationHostName();

	
	if(this.isThroughput()){
	    this.setMetricName("net.perfsonar.service.ma."+this.getServiceMatrixType());
	    this.setPerformanceDataName1("Max");
	    this.setPerformanceDataName2("Average");
	    this.setPerformanceDataName3("Min");
	    this.setPerformanceDataName4("Standard_Deviation");
	    this.setPerformanceDataName5("Count");

	    this.setHostName2Description("source host");
	    this.setHostName3Description("destination host");
	    String probeCommand=this.throughputSampleCommand;

	    probeCommand=probeCommand.replace("{SOURCE}",sourceHostName);
	    probeCommand=probeCommand.replace("{DESTINATION}",destinationHostName);
	    this.setProbeCommand(probeCommand);
	    this.setProbeId(this.getServiceMatrixType()+"_"+this.getSourceHostName()+"_"+this.getDestinationHostName()+"_"+this.matrixId);
	    this.setServiceDescription("Throughput Measurement");
	    this.setPerformanceDataDescription1("throughput max (Gb/s)");
	    this.setPerformanceDataDescription2("throughput avg (Gb/s)");
	    this.setPerformanceDataDescription3("throughput min (Gb/s)");
	    this.setPerformanceDataDescription4("sigma");
	    this.setPerformanceDataDescription5("count");
	    
	    this.setServiceGroup(this.getServiceMatrixName());
	    this.setCheckInterval(10);
	    this.setProbeRunning("N");
	    this.setSiteName(" ");
	    this.setSiteNameOrdered(" ");
	    this.setSchedulerName("MAIN");
	    this.setMatrixId(this.getMatrixId());
	    this.setActive(true);
	}
	if(this.isLatency()){
	    this.setMetricName("net.perfsonar.service.ma."+this.getServiceMatrixType());
	    this.setPerformanceDataName1("Max");
	    this.setPerformanceDataName2("Average");
	    this.setPerformanceDataName3("Min");
	    this.setPerformanceDataName4("Standard_Deviation");
	    this.setPerformanceDataName5("Count");
	    this.setHostNameDescription(" ");
	    this.setHostName2Description("source host");
	    this.setHostName3Description("destination host");

	    String probeCommand=latencySampleCommand;

	    probeCommand=probeCommand.replace("{SOURCE}",this.getSourceHostName());
	    probeCommand=probeCommand.replace("{DESTINATION}",this.getDestinationHostName());
	    this.setProbeCommand(probeCommand);
	    this.setProbeId(this.getServiceMatrixType()+"_"+this.getSourceHostName()+"_"+this.getDestinationHostName()+"_"+this.getMatrixId());
	    this.setServiceDescription("Latency Measurement");
	    this.setPerformanceDataDescription1("latency max");
	    this.setPerformanceDataDescription2("latency avg");
	    this.setPerformanceDataDescription3("latency min");
	    this.setPerformanceDataDescription4("sigma");
	    this.setPerformanceDataDescription5("count");
	    
	    this.setServiceGroup(this.getServiceMatrixName());
	    this.setCheckInterval(10);
	    this.setProbeRunning("N");
	    this.setSiteName(" ");
	    this.setSiteNameOrdered(" ");
	    this.setSchedulerName("MAIN");
	    this.setMatrixId(this.getMatrixId());
	    this.setActive(true);
	}
    }

    public void insertElement(){
	// insert current element into Services table. Assumes that element does not exist
	// element fields had to be defined by a series of set...() calls
	String insertCommand="INSERT INTO Services (MetricName,PerformanceDataName1,PerformanceDataName2,PerformanceDataName3,PerformanceDataName4,PerformanceDataName5,HostName,HostNameDescription,HostName2,HostName2Description,HostName3,HostName3Description,ProbeCommand,ProbeId,ServiceDescription,PerformanceDataDescription1,PerformanceDataDescription2,PerformanceDataDescription3,PerformanceDataDescription4,PerformanceDataDescription5,ServiceGroup,CheckInterval,ProbeRunning,SiteName,SiteNameOrdered,PrimitiveService,Active,matrixId,SchedulerName,NextCheckTime,LastCheckTime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW())";
	//             1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,
	PreparedStatement insertPS=null;
	try{
	    insertPS=db.getConn().prepareStatement(insertCommand);

	    insertPS.setString(1,this.getMetricName());
	    insertPS.setString(2,this.getPerformanceDataName1());
	    insertPS.setString(3,this.getPerformanceDataName2());
	    insertPS.setString(4,this.getPerformanceDataName3());
	    insertPS.setString(5,this.getPerformanceDataName4());
	    insertPS.setString(6,this.getPerformanceDataName5());
	    insertPS.setString(7, " ");
	    insertPS.setString(8,this.getHostNameDescription());
	    insertPS.setString(9,this.getHostName2());
	    insertPS.setString(10,this.getHostName2Description());	    
	    insertPS.setString(11,this.getHostName3());
	    insertPS.setString(12,this.getHostName3Description());	    
	    insertPS.setString(13,this.getProbeCommand());
	    insertPS.setString(14,this.getProbeId());
	    insertPS.setString(15,this.getServiceDescription());
	    insertPS.setString(16,this.getPerformanceDataDescription1());
	    insertPS.setString(17,this.getPerformanceDataDescription2());
	    insertPS.setString(18,this.getPerformanceDataDescription3());
	    insertPS.setString(19,this.getPerformanceDataDescription4());
	    insertPS.setString(20,this.getPerformanceDataDescription5());
	    insertPS.setString(21,this.getServiceGroup());
	    insertPS.setInt(   22,this.getCheckInterval());  
	    insertPS.setString(23,this.getProbeRunning());
	    insertPS.setString(24,this.getSiteName());
	    insertPS.setString(25,this.getSiteNameOrdered());
	    insertPS.setString(26,this.getPrimitiveService());
	    insertPS.setBoolean(27,this.getActive());
	    insertPS.setInt(28,this.getMatrixId());	    
	    insertPS.setString(29,this.getSchedulerName());
	    
	    insertPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to insert a new matrix element");
	    System.out.println(getClass().getName()+" "+insertCommand);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    ResultSet rs=insertPS.getGeneratedKeys();
	    while (rs.next ()){
		this.dbid = rs.getInt(1);
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" insertElement(): could not obtain key of newly inserted object");
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    insertPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+insertCommand);
	    System.out.println(getClass().getName()+" "+e);
	}  


    }

    public void saveService(){
	// save service. Assumes that probeId has been defined
	String updateCommand="UPDATE Services SET MetricName=?,HostName=?,PerformanceDataName1=?,PerformanceDataName2=?,PerformanceDataName3=?,PerformanceDataName4=?,PerformanceDataName5=?,HostNameDescription=?,HostName2=?,HostName2Description=?,HostName3=?,HostName3Description=?,ProbeCommand=?,ProbeId=?,ServiceDescription=?,PerformanceDataDescription1=?,PerformanceDataDescription2=?,PerformanceDataDescription3=?,PerformanceDataDescription4=?,PerformanceDataDescription5=?,Active=?,ServiceGroup=?,SiteName=?,CheckInterval=?,SiteNameOrdered=?,PrimitiveService=?,SchedulerName=?,matrixId=? where dbid=?";

	PreparedStatement updateCommandPS=null;
	try{
	    updateCommandPS=db.getConn().prepareStatement(updateCommand);
	    updateCommandPS.setString(1,this.getMetricName());
	    updateCommandPS.setString(2," ");
	    updateCommandPS.setString(3,this.getPerformanceDataName1());
	    updateCommandPS.setString(4,this.getPerformanceDataName2());
	    updateCommandPS.setString(5,this.getPerformanceDataName3());
	    updateCommandPS.setString(6,this.getPerformanceDataName4());
	    updateCommandPS.setString(7,this.getPerformanceDataName5());
	    updateCommandPS.setString(8, " ");
	    updateCommandPS.setString(9,this.getHostName2());
	    updateCommandPS.setString(10,this.getHostName2Description());
	    updateCommandPS.setString(11,this.getHostName3());
	    updateCommandPS.setString(12,this.getHostName3Description());
	    updateCommandPS.setString(13,this.getProbeCommand());
	    updateCommandPS.setString(14,this.getProbeId());
	    updateCommandPS.setString(15,this.getServiceDescription());
	    updateCommandPS.setString(16,this.getPerformanceDataDescription1());	    
	    updateCommandPS.setString(17,this.getPerformanceDataDescription2());	    
	    updateCommandPS.setString(18,this.getPerformanceDataDescription3());	    
	    updateCommandPS.setString(19,this.getPerformanceDataDescription4());	    
	    updateCommandPS.setString(20,this.getPerformanceDataDescription5());	    
	    updateCommandPS.setBoolean(21,this.getActive());
	    updateCommandPS.setString(22,this.getServiceGroup());
	    updateCommandPS.setString(23,this.getSiteName());
	    updateCommandPS.setInt(24,this.getCheckInterval());
	    updateCommandPS.setString(25,this.getSiteNameOrdered());
	    updateCommandPS.setString(26,this.getPrimitiveService());
	    updateCommandPS.setString(27,this.getSchedulerName());
	    updateCommandPS.setInt(   28,this.getMatrixId());
	    updateCommandPS.setInt(   29,this.getDbid());

	    System.out.println(getClass().getName()+" save service dbid="+this.getDbid());
	    System.out.println(getClass().getName()+" probeCommand="+this.getProbeCommand());	    
	    System.out.println(getClass().getName()+" probeId="+this.getProbeId());	    

	    updateCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to save service");
	    System.out.println(getClass().getName()+" probeId="+this.probeId);
	    System.out.println(getClass().getName()+" "+updateCommand);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    updateCommandPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+updateCommand);
	    System.out.println(getClass().getName()+" "+e);
	}

	
    }
    public void loadServiceById(){
	// load service from database. Assumes that this.dbid is already defined
	String getSummaryStatus="SELECT * from MetricRecordSummary WHERE dbid=? LIMIT 1";
	PreparedStatement getSummaryStatusQuery=null;
	try{
	    getSummaryStatusQuery=db.getConn().prepareStatement(getSummaryStatus);
	    getSummaryStatusQuery.setInt(1,this.dbid);
	    ResultSet rs=getSummaryStatusQuery.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	    this.matrixType=this.getMatrixType();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to unpack result set for matrix element");
	    System.out.println(getClass().getName()+" "+getSummaryStatus);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getSummaryStatusQuery.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+getSummaryStatus);
	    System.out.println(getClass().getName()+" "+e);
	}
	this.getServiceData();
    }
    public void loadServiceByProbeId(){
	// load service from database. Assumes that this.probeId is already defined
	String getSummaryStatus="SELECT * from MetricRecordSummary WHERE ProbeId=? LIMIT 1";
	PreparedStatement getSummaryStatusQuery=null;
	try{
	    getSummaryStatusQuery=db.getConn().prepareStatement(getSummaryStatus);
	    getSummaryStatusQuery.setString(1,this.probeId);
	    ResultSet rs=getSummaryStatusQuery.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	    this.matrixType=this.getMatrixType();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to unpack result set for matrix element");
	    System.out.println(getClass().getName()+" "+getSummaryStatus);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getSummaryStatusQuery.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+getSummaryStatus);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	this.getServiceData();

    }


    public String confirmUpdateActivePage(){
	String result="";
	result=result+"<strong><br>You requested to change status of service "+this.probeId+" on host to Active="+!this.active+"<br>";
	result=result+"<br>This operation will be logged<br>";	
	result=result+"<br>Your DN is "+parameterBag.user.getDN()+"<br>";
	result=result+"<BR>Type your message for the log here:<br>";
	result=result+"<form>";
	result=result+"<input type=\"hidden\" name=\"page\" value=\"" +parameterBag.pageAddress("Toggle Active ServiceMatrixElement")+  "\">";
	result=result+"<input type=\"hidden\" name=\"probeId\" value=\"" +parameterBag.probeId+  "\">";
	String message="";
	result=result+"<input type=\"text\" name=\"message\" value=\""+message+"\" /><br>";
	if (!this.active){
	    result=result+"<input type=\"submit\" value=\"Mark service as active\" />";
	}else{
	    result=result+"<input type=\"submit\" value=\"Mark service as inactive\" />";
	}
	result=result+"</strong>";
	return result;
    }
    public String activeUpdatedPage(){
	this.toggleActive();
	String result="";
	result=result+"<strong><br>The Active status of service "+this.probeId+" has been updated<br>";
	HtmlLink link=new HtmlLink(this.addressOfNodeDetailPage(),"Details page for service "+this.probeId,this.probeId);
	result=result+link.toHtml();
	result=result+"</strong>";
	return result;
    }

    private boolean getCurrentActive(){
	boolean result=false;
	String query="SELECT Active FROM Services where ProbeId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.probeId);
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		result=rs.getBoolean("Active");
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to read Active status for probeId="+this.probeId);
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	} 
	return result;
    }
    public void setCurrentActive(boolean inputActive){
	this.active=inputActive;
	String dbCommand="UPDATE Services SET Active=? WHERE ProbeId=?";
	PreparedStatement dbCommandPS=null;
	try{
	    dbCommandPS=db.getConn().prepareStatement(dbCommand);
	    dbCommandPS.setBoolean(1,this.active);
	    dbCommandPS.setString(2,this.probeId);
	    dbCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to update Active status for probeId="+this.probeId);
	    System.out.println(getClass().getName()+" "+dbCommand);
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
    public void toggleActive(){
	boolean currentActiveStatus=this.getCurrentActive();
	if (currentActiveStatus){
	    this.setCurrentActive(false);
	}else{
	    this.setCurrentActive(true);
	}
    }
    public String toggleActivePage(){
	String result="";
	result="<strong>";
	result=result+"ProbeId="+this.probeId+"<br>";
	this.toggleActive();
	result=result+"The Active flag has been set to "+this.boolean2string(this.active)+"<br>";

	String linkText="Go to details page for service "+this.probeId;
	String linkTitle="Service "+this.probeId;
	HtmlLink link=new HtmlLink(this.addressOfNodeDetailPage(),linkText,linkTitle);

	result=result+link.toString();
	result=result+"</strong>";
	return result;
    }
    public void setNextCheckTimeToNow(){
	// set next check time for this service to NOW()

	String dbCommand="UPDATE Services SET NextCheckTime=NOW() WHERE dbid=?";
	PreparedStatement dbCommandPS=null;
	try{
	    dbCommandPS=db.getConn().prepareStatement(dbCommand);
	    dbCommandPS.setInt(1,this.getDbid());
	    dbCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed set NextCheckTime to NOW()");
	    System.out.println(getClass().getName()+" "+dbCommand);
	    System.out.println(getClass().getName()+" probeId="+this.probeId);
	    System.out.println(getClass().getName()+" dbid="+this.getDbid());
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

    private String getMatrixType(){
	String result="";
	String query="select * from ServiceMatrices where id=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getMatrixId());
	    ResultSet rs=queryPS.executeQuery();
	    String serviceMatrixType="unknown";
	    while (rs.next ()){
		serviceMatrixType=rs.getString("ServiceMatrixType");
	    }
	    rs.close();
	    if(serviceMatrixType.equals("throughput")){
		result=serviceMatrixType;
	    }
	    if(serviceMatrixType.equals("latency")){
		result=serviceMatrixType;
	    }
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to obtain matrix type");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" id="+this.getMatrixId());
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);
	} 
	return result;
    }

    private void unpackResultSet(ResultSet rs){
	try{
	    while (rs.next ()){
		this.dbid=rs.getInt("dbid");
		this.matrixId=rs.getInt("matrixId");
		metricName=rs.getString("MetricName");
		metricType=rs.getString("MetricType");
		String statusOfTheProbe= rs.getString("MetricStatus");
		//metricStatus=new ProbeStatus(statusOfTheProbe);
		this.setStatus(new ProbeStatus(statusOfTheProbe));
		timestamp=rs.getTimestamp("Timestamp");
		serviceType=rs.getString("ServiceType");
		serviceUri =rs.getString("ServiceUri");
		gatheredAt =rs.getString("GatheredAt");
		summaryData=rs.getString("SummaryData");
		detailsData=rs.getString("DetailsData");
		performanceData=rs.getString("PerformanceData");
		voName=" ";
		samUploadFlag=0;
		
		hostName=" ";
		monitor=hostName;
		hostNameDescription=" ";
		
		hostName2=rs.getString("HostName2");
		source=hostName2;
		hostName2Description=rs.getString("HostName2Description");
		
		
		hostName3=rs.getString("HostName3");
		destination=hostName3;
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

		this.siteName=rs.getString("SiteName");
		this.siteNameOrdered=rs.getString("SiteNameOrdered");
		this.serviceGroup=rs.getString("ServiceGroup");
		
		// define class specific variables
		
		this.throughput_min = this.performanceData3;
		this.throughput_max = this.performanceData1;
		this.throughput_avg = this.performanceData2;
		
		this.latency_min=this.performanceData3;
		this.latency_max=this.performanceData1;
		this.latency_avg=this.performanceData2;
		
		this.emptyElement=false;
		this.definedElement=true;
 		
	    }
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when unpacking result set");
	    System.out.println(getClass().getName()+" "+metricName) ;   
	    System.out.println(getClass().getName()+" "+e);
	}
    }

    private void getServiceData(){
	String checkActiveStatus="SELECT NextCheckTime,LastCheckTime,CheckInterval,Active,ServiceGroup,ProbeRunning,PrimitiveService,SchedulerName FROM Services WHERE probeId=?";
	PreparedStatement checkActiveStatusQuery=null;
	try{
	    checkActiveStatusQuery=db.getConn().prepareStatement(checkActiveStatus);
	    checkActiveStatusQuery.setString(1,this.probeId);
	    ResultSet rs=checkActiveStatusQuery.executeQuery();
	    while(rs.next()){
		this.nextCheckTime=rs.getTimestamp("NextCheckTime");
		this.lastCheckTime=rs.getTimestamp("LastCheckTime");
		this.active=rs.getBoolean("Active");
		this.checkInterval=rs.getInt("CheckInterval");
		this.serviceGroup=rs.getString("ServiceGroup");
		this.probeRunning=rs.getString("ProbeRunning");
		this.primitiveService=rs.getString("PrimitiveService");
		this.setSchedulerName(rs.getString("SchedulerName"));
		
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+"  getServiceData(): error occured when unpacking result set");
	    System.out.println(getClass().getName()+" probeId="+this.probeId);
	    System.out.println(getClass().getName()+" "+checkActiveStatus) ;   
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    checkActiveStatusQuery.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+"  failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+checkActiveStatus) ;   
	    System.out.println(getClass().getName()+" "+e);
	}
    }



    private String boolean2string(boolean inputValue){
	if(inputValue){
	    return "Y";
	}else{
	    return "N";
	}
    }
    private boolean string2boolean(String inputString){
	if(inputString.equals("Y")||inputString.equals("y")||inputString.equals("Yes")||inputString.equals("yes")||inputString.equals("YES")){
	    return true;
	}else{
	    return false;
	}
    }
    private boolean isThroughputService(){
	if(this.matrixType.equals("throughput")){
	    return true;
	}else{
	    return false;
	}
    }
    private boolean isLatencyService(){
	if(matrixType.equals("latency")){
	    return true;
	}else{
	    return false;
	}
    }

    
    public String addressOfNodeDetailPage(){
	/// create link to node detail page
	//ParameterBag paramBagLocal=(ParameterBag)parameterBag.clone();
	ParameterBag paramBagLocal=new ParameterBag();
	String detailPageName="";
	if(this.isThroughputService()){
	    detailPageName="Throughput Node";
	}
	if(this.isLatencyService()){
	    detailPageName="Latency Node";
	}

	detailPageName="Matrix Element";

	paramBagLocal.addParam("page",ParameterBag.pageAddress(detailPageName)  );
	//paramBagLocal.addParam("probeId",this.probeId);
	paramBagLocal.addParam("id",Integer.toString(this.getDbid()));
	paramBagLocal.addParam("requestUri",parameterBag.requestUri);
	return paramBagLocal.makeLink();
    }

    public HtmlTableCell numericStatusCell(){
	// return very short status table containing only status word and color
	// to be used in  matrix	
	String linkTitle="Initiator: "+monitor+", From: "+source+", To: "+destination;
	String cellText="";
	String throughputFormat="%3.2f";
	String latencyFormat="%4.1f";
	if(!emptyElement){
	    if(this.isThroughputService()){
		cellText = HtmlStringUtils.addFontTags(String.format(throughputFormat,throughput_avg),this.fontSize);
	    }
	    if(this.isLatencyService()){
		cellText = HtmlStringUtils.addFontTags(String.format(latencyFormat,latency_max),this.fontSize);
	    }

	}else{
	    cellText=HtmlStringUtils.addFontTags("---",this.fontSize);
	}
	HtmlLink link=new HtmlLink(this.addressOfNodeDetailPage(),cellText,linkTitle);

	HtmlTableCell cell=null;
	if (definedElement){
	    //if(metricStatus==null){
	    //		System.out.println(getClass().getName()+" metric status is null");
	    //	System.out.println(getClass().getName()+" probeId="+this.probeId);
	    //}else{
	    //	System.out.println(getClass().getName()+" metric status="+metricStatus.toString());
	    //}
	    cell=new HtmlTableCell(link,metricStatus.color());
	}else{
	    cell=new HtmlTableCell(cellText);
	}
	return cell;
    }

    // === links ==== //
    public HtmlLink getLinkToMatrixElementDetailPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("APD Matrix Element")  );
	paramBagLocal.addParam("id",Integer.toString(this.getDbid()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToForceTestMatrixElementPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Force Test Matrix Element")  );
	paramBagLocal.addParam("id",Integer.toString(this.getDbid()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToEditMatrixElementPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Edit Matrix Element")  );
	paramBagLocal.addParam("id",Integer.toString(this.getDbid()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToMatrixElementHistoryTablePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Matrix Element History Table")  );
	paramBagLocal.addParam("id",Integer.toString(this.getDbid()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToMatrixElementHistoryPlotPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Matrix Element History Plot")  );
	paramBagLocal.addParam("id",Integer.toString(this.getDbid()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    // === end of links === //

    private String linksToTableAndPlot(){
	String result="";
	// build the address to plot page

 	HtmlLink link0=this.getLinkToEditMatrixElementPage("Edit this Service");
	result=result+link0.toHtml()+"<br>";

	// we do not make history plot for apd (for now)
	//HtmlLink link=this.getLinkToMatrixElementHistoryPlotPage("Link to history plot");
	//result=result+link.toHtml()+"<br>";

	// bild the address to history table page
	HtmlLink link2=this.getLinkToMatrixElementHistoryTablePage("Link to history table");
	result=result+link2.toHtml()+"<br>";
	return result;
    }
    public HtmlTable fullHtmlTableThroughput(){
	String result="";

	result="<strong>ProbeId: </strong>"+probeId +"<br>";
	if(active){
	    result=result+"<strong>The service is active</strong><br>";
	}else{
	    result=result+"<strong>The service is not active, tests have been disabled</strong><br>";
	}

	result=result+"<br>";
	
	if(!dashboardComment.equals("")){
	    result=result+"<strong>"+dashboardComment+"</strong><br>";
	}

	result=result+"<strong>Monitor: </strong>"+monitor+"<br>";
	result=result+"<strong>Source: </strong>"+source+"<br>";
	result=result+"<strong>Destination: </strong>"+destination+"<br>";
	result=result+"<strong>Timestamp: </strong>"+timestamp+"<br>";
	result=result+"<strong>Throughput Min=</strong>"+throughput_min+"<br>";
	result=result+"<strong>Throughput Max=</strong>"+throughput_max+"<br>";
	result=result+"<strong>Throughput Avg=</strong>"+throughput_avg+"<br>";
	result=result+"<strong>Sigma=</strong>"+sigma+"<br>";
	result=result+"<strong>ServiceType: </strong>"+serviceType+"<br>";
	result=result+"<strong>ServiceUri: </strong>"+serviceUri+"<br>";

	result=result+"<strong>gatheredAt: </strong>"+gatheredAt+"<br>";
	result=result+"<strong>ProbeCommand: </strong>"+probeCommand+"<br>";
	result=result+"<strong>SummaryData: </strong>"+summaryData+"<br>";
	result=result+"<strong>DetailsData: </strong>"+detailsData+"<br>";

	result=result+"<strong>Check Interval: </strong>"+Integer.toString(checkInterval)+" (min)<br>";

	result=result+"<strong>Last check time: </strong>"+lastCheckTime+"<br>";
	result=result+"<strong>Next check time: </strong>"+nextCheckTime+"<br>";
	result=result+"<strong>Scheduler/Collector: </strong>"+this.getSchedulerName()+"<br>";
	if(parameterBag.validUser=="yes"){
	    result=result+"<strong>"+this.getLinkToForceTestMatrixElementPage("Force test of this service NOW!").toHtml()+"</strong><br>";
	}
	result=result+"<br>";


	// add links to history table and plot
	result=result+this.linksToTableAndPlot();
	
	HtmlTableCell hc =new HtmlTableCell(result,metricStatus.color());
	hc.alignLeft();
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(hc);

	return ht;	    	    		
    }


    public HtmlTable fullHtmlTableLatency(){
	String result="";
	result="<strong>ProbeId: </strong>"+probeId +"<br>";

	if(active){
	    result=result+"<strong>The service is active</strong><br>";
	}else{
	    result=result+"<strong>The service is not active, tests have been disabled</strong><br>";
	}

	result=result+"<br>";
	
	if(!dashboardComment.equals("")){
	    result=result+"<strong>"+dashboardComment+"</strong><br>";
	}

	result=result+"<strong>Monitor: </strong>"+monitor+"<br>";
	result=result+"<strong>Source: </strong>"+source+"<br>";
	result=result+"<strong>Destination: </strong>"+destination+"<br>";
	result=result+"<strong>Timestamp: </strong>"+timestamp+"<br>";
	result=result+"<strong>Latency Min=</strong>"+latency_min+"<br>";
	result=result+"<strong>Latency Max=</strong>"+latency_max+"<br>";
	result=result+"<strong>Latency Avg=</strong>"+latency_avg+"<br>";
	result=result+"<strong>Sigma=</strong>"+sigma+"<br>";
	result=result+"<strong>ServiceType: </strong>"+serviceType+"<br>";
	result=result+"<strong>ServiceUri: </strong>"+serviceUri+"<br>";

	result=result+"<strong>gatheredAt: </strong>"+gatheredAt+"<br>";
	result=result+"<strong>ProbeCommand: </strong>"+probeCommand+"<br>";
	result=result+"<strong>SummaryData: </strong>"+summaryData+"<br>";
	result=result+"<strong>DetailsData: </strong>"+detailsData+"<br>";

	result=result+"<strong>Check Interval: </strong>"+Integer.toString(checkInterval)+" (min)<br>";

	result=result+"<strong>Last check time: </strong>"+lastCheckTime+"<br>";
	result=result+"<strong>Next check time: </strong>"+nextCheckTime+"<br>";
	result=result+"<strong>Scheduler/Collector: </strong>"+this.getSchedulerName()+"<br>";
	if(parameterBag.validUser=="yes"){
	    //result=result+forceUpdateButton.toHtml();
	    result=result+"<strong>"+this.getLinkToForceTestMatrixElementPage("Force test of this service NOW!").toHtml()+"</strong><br>";
	}

	result=result+"<br>";

	// UserInfo currentUser=parameterBag.getUser();
	// //result=result+currentUser.getDN()+"<br>";
	// if (currentUser.isApproved()){
	//     if(currentUser.hasRole("admin")){
	// 	String editPage="Edit Latency Node";
	// 	EditServiceButton eB=new EditServiceButton(parameterBag,db,editPage);
	// 	result=result+eB.toHtml()+"<br>";
	//     }
	// }
	    
	// add links to history table and plot
	result=result+this.linksToTableAndPlot();
	
	
	HtmlTableCell hc =new HtmlTableCell(result,metricStatus.color());
	hc.alignLeft();
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(hc);

	return ht;	    	    		
    }    

    public HtmlTable fullHtmlTable(){
	if(this.isThroughputService()){
	    return this.fullHtmlTableThroughput();
	}else{
	    return this.fullHtmlTableLatency();
	}
    }

    public void getHistory(){

	String getHistorySql="select * from MetricRecord where dbid=? ";

	IntervalSelector iS = new IntervalSelector(parameterBag);
	iS.setTimeVariable("Timestamp");
	iS.setTimeZoneShift(0);
	getHistorySql=getHistorySql+" "+iS.buildQuery(parameterBag.interval);

	history.clear();
	PreparedStatement getHistoryPreparedSql=null;
	try{
	    
	    getHistoryPreparedSql= db.getConn().prepareStatement(getHistorySql);
	    getHistoryPreparedSql.setInt(1,this.getDbid());
	
	    ResultSet rs=getHistoryPreparedSql.executeQuery(); 

	    int count = 0;
	    while (rs.next ()){
		MatrixNodeHistoryRecord historyRecord = new MatrixNodeHistoryRecord(rs);
		history.add(historyRecord);

		count=count+1;
	    }
	    rs.close();

	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to execute history query "+getHistorySql);
	    System.out.println(getClass().getName()+" "+e);
	    System.out.flush();
	}
	try{
	    getHistoryPreparedSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement "+getHistorySql);
	    System.out.println(getClass().getName()+" "+e);
	    System.out.flush();
	}	    
	
    }

    public HtmlTable getHistoryTable(){
	
	this.getHistory();
	
	HtmlTable ht=new HtmlTable(3);
	ht.setBorder(0);
	ht.setPadding(0);

	Iterator iter = history.iterator();
	while (iter.hasNext()){
	    MatrixNodeHistoryRecord currentRecord = (MatrixNodeHistoryRecord)iter.next();
	    ht.addCell(new HtmlTableCell(currentRecord.timestamp.toString()));
	    ht.addCell(new HtmlTableCell(currentRecord.metricStatus.statusWord(),currentRecord.metricStatus.color()));
	    HtmlTableCell detailsCell=new HtmlTableCell(currentRecord.summaryData);
	    detailsCell.alignLeft();
	    ht.addCell(detailsCell);
	}

	return ht;
    }

    public String getHistoryTablePage(){
	String result="";

	//result="<H2>Throughput Node History</H2><br>";
	result=result+"<h3>Source="+source+", destination="+destination+", monitor="+monitor+"</h3>";

	IntervalSelector iS=new IntervalSelector(parameterBag);

	result=result+iS.toHtml()+"<br>";

	HtmlTable historyTable=getHistoryTable();
	result=result+historyTable.toHtml();
	return result;
    }

    // make image
    public String makeLatencyHistoryImage(String plotDirectory){
	//String plotFileName="temp_picture.jpg";
	String plotFileName=this.getPlotFileName();

	// make history plot
	//DefaultTableXYDataset dataset = new DefaultTableXYDataset(); 
	XYSeriesCollection dataset = new XYSeriesCollection();

	ValueAxis timeAxis = new DateAxis("Date and Time"); 
	NumberAxis valueAxis = new NumberAxis("Latency"); 
	valueAxis.setAutoRangeIncludesZero(false); 
	StandardXYItemRenderer renderer = new StandardXYItemRenderer( StandardXYItemRenderer.LINES); 
	
	XYSeries latency_max_series=new XYSeries("latency max",false);
	XYSeries latency_avg_series=new XYSeries("latency avg",false);
	XYSeries latency_min_series=new XYSeries("latency min",false);

	Iterator iter = history.iterator();
	while (iter.hasNext()){
	    MatrixNodeHistoryRecord historyRecord = (MatrixNodeHistoryRecord)iter.next();
	    long xTime=historyRecord.timestamp.getTime();
	    double yAvg=historyRecord.latency_avg;
	    double yMax=historyRecord.latency_max;
	    double yMin=historyRecord.latency_min;
	    
	    latency_max_series.add(xTime,yMax);
	    latency_avg_series.add(xTime,yAvg);
	    latency_min_series.add(xTime,yMin);
	}
	dataset.addSeries(latency_max_series); 
	dataset.addSeries(latency_avg_series); 
	dataset.addSeries(latency_min_series); 
	
	//renderer.setSeriesPaint(0,new Color(255, 255, 180)); 
	//renderer.setSeriesPaint(1,new Color(206, 230, 255)); 
	//renderer.setSeriesPaint(2,new Color(255, 230, 230)); 

	renderer.setSeriesPaint(0,Color.BLUE); 
	renderer.setSeriesPaint(1,Color.RED); 
	renderer.setSeriesPaint(2,Color.BLACK); 

	float lineWidth=(float)4.0;
	renderer.setSeriesStroke(0,new BasicStroke(lineWidth));
	renderer.setSeriesStroke(1,new BasicStroke(lineWidth));
	renderer.setSeriesStroke(2,new BasicStroke(lineWidth));	
	
	XYPlot plot = new XYPlot(dataset, timeAxis, valueAxis, renderer); 

	JFreeChart chart = new JFreeChart("Latency: src="+source+"; dst="+destination+"; mon="+monitor,plot);

	try{
	    imageFile = new File (plotDirectory+plotFileName);
	    ChartUtilities.saveChartAsJPEG(imageFile,chart,imageXsize,imageYsize);
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to write output file to "+plotFileName);
	    System.out.println(getClass().getName()+" "+e);
	}
	return plotFileName;
    }
    public String makeThroughputHistoryImage(String plotDirectory){
	//String plotFileName="temp_picture.jpg";
	String plotFileName=this.getPlotFileName();

	// make history plot
	//DefaultTableXYDataset dataset = new DefaultTableXYDataset(); 
	XYSeriesCollection dataset = new XYSeriesCollection();

	ValueAxis timeAxis = new DateAxis("Date and Time"); 
	NumberAxis valueAxis = new NumberAxis("Throughput [Gb/s]"); 
	valueAxis.setAutoRangeIncludesZero(false); 
	StandardXYItemRenderer renderer = new StandardXYItemRenderer( StandardXYItemRenderer.LINES); 
	
	XYSeries throughput_max_series=new XYSeries("throughput max",false);
	XYSeries throughput_avg_series=new XYSeries("throughput avg",false);
	XYSeries throughput_min_series=new XYSeries("throughput min",false);

	Iterator iter = history.iterator();
	while (iter.hasNext()){
	    
	    MatrixNodeHistoryRecord historyRecord = (MatrixNodeHistoryRecord)iter.next();
	    long xTime=historyRecord.timestamp.getTime();
	    double yAvg=historyRecord.throughput_avg;
	    double yMax=historyRecord.throughput_max;
	    double yMin=historyRecord.throughput_min;
	    
	    throughput_max_series.add(xTime,yMax);
	    throughput_avg_series.add(xTime,yAvg);
	    throughput_min_series.add(xTime,yMin);
	}
	dataset.addSeries(throughput_max_series); 
	dataset.addSeries(throughput_avg_series); 
	dataset.addSeries(throughput_min_series); 
	
	//renderer.setSeriesPaint(0,new Color(255, 255, 180)); 
	//renderer.setSeriesPaint(1,new Color(206, 230, 255)); 
	//renderer.setSeriesPaint(2,new Color(255, 230, 230)); 

	renderer.setSeriesPaint(0,Color.BLUE); 
	renderer.setSeriesPaint(1,Color.RED); 
	renderer.setSeriesPaint(2,Color.BLACK); 

	float lineWidth=(float)4.0;
	renderer.setSeriesStroke(0,new BasicStroke(lineWidth));
	renderer.setSeriesStroke(1,new BasicStroke(lineWidth));
	renderer.setSeriesStroke(2,new BasicStroke(lineWidth));
	
	XYPlot plot = new XYPlot(dataset, timeAxis, valueAxis, renderer); 

	JFreeChart chart = new JFreeChart("Throughput: src="+source+"; dst="+destination+"; mon="+monitor,plot);

	try{
	    imageFile = new File (plotDirectory+plotFileName);
	    ChartUtilities.saveChartAsJPEG(imageFile,chart,imageXsize,imageYsize);
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to write output file to "+plotFileName);
	    System.out.println(getClass().getName()+" "+e);
	}
	return plotFileName;
    }
    public String makeHistoryImage(String plotDirectory){
	if(this.isThroughputService()){
	    return this.makeThroughputHistoryImage(plotDirectory);
	}else{
	   if(this.isLatencyService()){ 
	       return this.makeLatencyHistoryImage(plotDirectory);
	   }else{
	       System.out.println(getClass().getName()+" ProbeId="+this.probeId);
	       System.out.println(getClass().getName()+" Service is neither throughput nor latency!!!");
	       return "Unknown service type";
	   }
	}
    }
    public String getHistoryPlotPage(String plotDirectory){
	String result="";
	result=result+"<h3>Source="+source+", destination="+destination+", monitor="+monitor+"</h3>";
	this.getHistory();

	String plotFileName=this.makeHistoryImage(plotDirectory);

	IntervalSelector iS=new IntervalSelector(parameterBag);

	result=result+iS.toHtml()+"<br>";

	result=result+"<img style=\"width: XSIZEpx; height: YSIZEpx;\" alt=\"alternate text\" src=\""+ plotFileName +"\">";
	//result=result+"<img style=\"width: XSIZEpx; height: YSIZEpx;\" alt=\"alternate text\" src=\""+"ImageMaker" +"\">";
	result=result.replace("XSIZE",Integer.toString(imageXsize));
	result=result.replace("YSIZE",Integer.toString(imageYsize));
	result=result+"<br>";
	return result;	
    }
    //==================== edit service pages ========================//
    public String saveServicePage(){
	// save service
	this.getParametersFromBag();
	this.saveService();

	// print confirmation that service has been saved
	String result="";
	result=result+"<strong><br>The service "+this.probeId+" has been updated<br>";

	HtmlLink link=new HtmlLink(this.addressOfNodeDetailPage(),"Details page for service "+this.probeId,this.probeId);

	result=result+link.toHtml();
	result=result+"</strong>";

	return result;
    }	
    
    public String editServicePage(){
	String result="";
	return serviceEditTable().toHtml();
    }    
    public HtmlTable serviceEditTable(){
	String result="";
	if(parameterBag.validUser=="yes"){
	    result="<strong>ProbeId: </strong>"+probeId +"<br>";
	    result=result+"<strong>Monitor: </strong>"+monitor+"<br>";
	    result=result+"<strong>Source: </strong>"+source+"<br>";
	    result=result+"<strong>Destination: </strong>"+destination+"<br>";
	    
	    result=result+"<strong>Service: </strong>"+metricName+"<br>";
	    result=result+"<strong>ServiceDescription: </strong>"+serviceDescription+"<br>";
	    result=result+"<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">";	
	    result=result+"<strong>Check Interval (min):</strong><input type=\"text\" name=\"checkInterval\" value=\""+checkInterval+"\" /><br>";
	    result=result+"<strong>Probe Command:</strong><input type=\"text\" name=\"probeCommand\" value=\""+probeCommand+"\" /><br>";

	    ListOfSchedulers listOfSchedulers=new ListOfSchedulers(this.parameterBag,this.db);
	    String schedulerSelector = listOfSchedulers.schedulerSelector(this.getSchedulerName());
	    result=result+"<strong>Scheduler/Collector:</strong>"+schedulerSelector+"<br>";

	    result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Matrix Element")+"\" />";
	    //result=result+"<input type=\"hidden\" name=\"probeId\" value=\""+parameterBag.probeId+"\" />";
	    result=result+"<input type=\"hidden\" name=\"id\" value=\""+ Integer.toString(this.getDbid())   +"\" />";
	    
	    result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	    result=result+"</form>";
	}else{
	    result=result+"You are not authorized to perform this operation";
	}
	HtmlTableCell hc =new HtmlTableCell(result);
	hc.alignLeft();
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(hc);	
	return ht;	  
    }

    public String forceTestMatrixElementPage(){
	String result="<H1>Force test of Matrix Element</h1><br>";
	this.setNextCheckTimeToNow();
	result=result+"<strong>The service "+this.getServiceMatrixName()+" has been signalled. It should perform status in a few minutes</status><br>";
	result=result+"<br><br>";
	result=result+"<strong>"+this.getLinkToMatrixElementDetailPage("Go back to service details").toHtml()+"</strong>";
	return result;
    }

    public String toOneLineString(){
	String result="ServiceMatrixElement: src="+this.getSource()+" dst="+this.getDestination()+" mon="+this.getMonitor()+" matrixId="+this.getMatrixId();
	return result;
    }
    public HtmlTableCell htmlTableCell(){
	String linkTitle="From: "+this.getSource()+", To: "+this.getDestination();
	String cellText="";
	HtmlTableCell cell=null;
	if(this.isEmpty()){
	    cellText=HtmlStringUtils.addFontTags("---",this.fontSize);
	    cell=new HtmlTableCell(cellText);
	}else{
	    cellText = HtmlStringUtils.addFontTags(this.getStatus().statusWordShort(),this.fontSize);
	    HtmlLink link = this.getLinkToMatrixElementDetailPage(cellText);	    
	    cell=new HtmlTableCell(link,this.getStatus().color());
	}
	return cell;
    }
    public HtmlTable htmlTable(){
	HtmlTable ht = new HtmlTable(1);
	ht.setBorder(0);
	ht.setPadding(0);
	ht.addCell(this.htmlTableCell());
	return ht;
    }

}
