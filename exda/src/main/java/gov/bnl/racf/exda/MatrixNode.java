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


public class MatrixNode
{  

    public String metricNameSelect="";

    public String detailPageNameLatency="Latency Node";
    public String detailPageNameThroughput="Throughput Node";
    public String detailPageName="";

    public String source="";
    public String destination="";
    public String monitor="";
    
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
    public String serviceDescription="";

    public String dashboardComment="";

    public int checkInterval=-1;

    public Timestamp lastCheckTime=null;
    public Timestamp nextCheckTime=null;

    public boolean active=true;

    public String linkToNodeDetailPage=null;

    public ParameterBag parameterBag = null;

    public DbConnector db=null;

    public String initialisationSqlQuery="";
    // public PreparedStatement initialisationSql=null;
    // public PreparedStatement getHistoryPreparedSql=null;
    // public PreparedStatement checkTimeSql=null;

    public ForceUpdateButton fb = null;
    public SetActiveButton setActiveButton = null;

    public String imageFileName="temporary_file_delete_it.jpg";
    public int imageXsize=1000;
    public int imageYsize=600;

    public int fontSize=1;

    public File imageFile=null;

    public List<MatrixNodeHistoryRecord> history=new ArrayList<MatrixNodeHistoryRecord>();

    public boolean emptyNode=true;

    public PrintWriter out=null;

    public MatrixNode(){
	// create empty node
	this.emptyNode=true;
	metricStatus=new ProbeStatus("UNDEFINED");
    }

    public MatrixNode(ParameterBag paramBag,  DbConnector inputDb,String probeIdInput )
	{
	    parameterBag=paramBag;
	    db=inputDb;	    
	    probeId=probeIdInput;
	    defineLocalVariables();

	    /// create link to node detail page
	    ParameterBag paramBagLocal=(ParameterBag)paramBag.clone();
	    paramBagLocal.addParam("page",ParameterBag.pageAddress(detailPageName)  );

	    linkToNodeDetailPage=paramBagLocal.makeLink();

	    initialisationSqlQuery="select  * from MetricRecordSummary where ProbeId=? order by Timestamp DESC limit 1";
	    PreparedStatement initialisationSql=null;
	    try{
		initialisationSql= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql.setString(1,probeId);
		
		ResultSet rs=initialisationSql.executeQuery();
		unpackResultSet(rs);
		rs.close();

		monitor=hostName;
		source=hostName2;
		destination=hostName3;
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+metricName) ;   
		System.out.println(getClass().getName()+" "+e);
		emptyNode=true;
	    }
	    try{
		initialisationSql.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e);
		emptyNode=true;
	    }

	    getExecutionControlData();

	    paramBagLocal.addParam("src",source);
	    paramBagLocal.addParam("dst",destination);
	    paramBagLocal.addParam("mon",monitor);


	}
    public MatrixNode(ParameterBag paramBag,  DbConnector inputDb,PerfSonarHost sourceHost,PerfSonarHost destinationHost , PerfSonarHost monitorHost)
	{
	    parameterBag=paramBag;
	    db=inputDb;

	    source=sourceHost.getHostName();
	    destination=destinationHost.getHostName();
	    monitor=monitorHost.getHostName();



	    probeId=parameterBag.probeId;

	    defineLocalVariables();

	    fillFromHostNames();

	    getExecutionControlData();

	}
    public MatrixNode(ParameterBag paramBag,  DbConnector inputDb,String inputSource, String inputDestination, String inputMonitor)
	{
	    parameterBag=paramBag;
	    db=inputDb;

	    source=inputSource;
	    destination=inputDestination;
	    monitor=inputMonitor;

	    probeId=parameterBag.probeId;

	    defineLocalVariables();

	    fillFromHostNames();

	    getExecutionControlData();

	}

    public void fillFromHostNames(){
	    /// create link to node detail page

	    ParameterBag paramBagLocal=(ParameterBag)parameterBag.clone();	

	    paramBagLocal.addParam("page",ParameterBag.pageAddress(detailPageName)  );
	    paramBagLocal.addParam("src",source);
	    paramBagLocal.addParam("dst",destination);
	    paramBagLocal.addParam("mon",monitor);
	    linkToNodeDetailPage=paramBagLocal.makeLink();
	 
	    initialisationSqlQuery="select  * from MetricRecordSummary where HostName=? and HostName2=? and HostName3=? and MetricName=? order by Timestamp DESC limit 1";
	    
	    PreparedStatement initialisationSql=null;
	    try{
		initialisationSql= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql.setString(1,monitor);
		initialisationSql.setString(2,source);
		initialisationSql.setString(3,destination);
		initialisationSql.setString(4,metricNameSelect);
		System.out.println(getClass().getName()+"monitor="+monitor);
		System.out.println(getClass().getName()+"source="+source);
		System.out.println(getClass().getName()+"destination="+destination);
		System.out.println(getClass().getName()+"metricNameSelect="+metricNameSelect);

		
		ResultSet rs=initialisationSql.executeQuery();
		unpackResultSet(rs);
		rs.close();

	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+metricName) ;   
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		initialisationSql.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e);
	    }		
    }

    public void defineLocalVariables(){}



    public void getExecutionControlData(){

	// if force update was requested and user is valid, do it now.
	parameterBag.addParam("probeId",probeId);
	
	// decide if the probe is active or not
	setActiveButton = new SetActiveButton(parameterBag,db);
	if (parameterBag.getUser().isApproved()){
	    if( parameterBag.toggleActive.equals("Y")){
		setActiveButton.toggleActiveNow();
	    }
	}
	
	// decide whether to force update of service
	fb=new ForceUpdateButton(parameterBag,db);

	if (parameterBag.getUser().isApproved() && parameterBag.executeNow.equals("yes")){
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
		if (rs.getString("Active").equals("Y")){
		    active=true;
		}else{
		    active=false;
		}
		// add active information to probe status
		if(!active){
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
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+checkTimeSqlQuery);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	
	// protection against overdue services
	if (active){
	    java.util.Date currentTime = new java.util.Date();

	    Calendar currentCal = Calendar.getInstance();
	    currentCal.setTime(currentTime);

	    java.sql.Date  lastCheckDate   = new java.sql.Date(lastCheckTime.getTime());  
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

    public void unpackResultSet(ResultSet rs){
	try{
	    while (rs.next ()){
		metricName=rs.getString("MetricName");
		metricType=rs.getString("MetricType");
		String statusOfTheProbe= rs.getString("MetricStatus");
		System.out.println(getClass().getName()+"hello, point 2, status="+statusOfTheProbe);	
		metricStatus=new ProbeStatus(statusOfTheProbe);
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
		
		// define class specific variables
		
		this.throughput_min = this.performanceData3;
		this.throughput_max = this.performanceData1;
		this.throughput_avg = this.performanceData2;
		
		this.latency_min=this.performanceData3;
		this.latency_max=this.performanceData1;
		this.latency_avg=this.performanceData2;
		
		this.emptyNode=false;
 		
	    }
	    
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when unpacking result set");
	    System.out.println(getClass().getName()+" "+metricName) ;   
	    System.out.println(getClass().getName()+" "+e);
	}

    }


    public String getSource(){
	return source;
    }
    public String getDestination(){
	return destination;
    }
    public String getMonitor(){
	return monitor;
    }


    public String getLinkToNodeDetailsPage(){
	String result="";
	String requestUri=parameterBag.requestUri;
	result=requestUri+"?";
	if (source!=null){
	     result=result+"src="+source+"&";
	 }
	if (destination!=null){
	     result=result+"dst="+destination+"&";
	 }
	if (monitor!=null){
	     result=result+"mon="+monitor+"&";
	 }	
	result=result+"page="+parameterBag.pageAddress(detailPageName)+"&";
	return result;
    }


    public void setFontSize(int fontSize){
	this.fontSize=fontSize;
    }


    public HtmlTableCell numericStatusCell(){
	// return very short status table containing only status word and color
	// to be used in  matrix	
	String linkTitle="Initiator: "+monitor+", From: "+source+", To: "+destination;
	String cellText = HtmlStringUtils.addFontTags(String.format("%3.2f",throughput_avg),fontSize);
	HtmlLink link=new HtmlLink(linkToNodeDetailPage,cellText,linkTitle);

	HtmlTableCell cell=new HtmlTableCell(link,metricStatus.color());
	return cell;
	
    }
    public HtmlTableCell veryShortStatusCell(){
	// return very short status table containing only status word and color
	// to be used in  matrix	
	if(emptyNode){
	    HtmlTableCell cell=new HtmlTableCell("---");
	    return cell;
	}else{
	    String linkTitle="Initiator: "+monitor+", From: "+source+", To: "+destination;
	    HtmlLink link=new HtmlLink(linkToNodeDetailPage,metricStatus.statusWordShort(),linkTitle);
	    HtmlTableCell cell=new HtmlTableCell(link,metricStatus.color());
	    return cell;
	}
	
    }
    public HtmlTable veryShortHtmlTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(veryShortStatusCell());
	return ht;
	
    }

    public HtmlTableCell shortStatusCell(){
	// return very short status table containing only status word and color
	// to be used in throughput matrix
	if(emptyNode){
	    HtmlTableCell cell=new HtmlTableCell("---");
	    return cell;
	}else{
	    String linkTitle="Initiator: "+monitor+", From: "+source+", To: "+destination;
	    HtmlLink link=new HtmlLink(linkToNodeDetailPage,metricName, linkTitle);
	    HtmlTableCell cell=new HtmlTableCell(link,metricStatus.color());
	    return cell;
	}
    }
    public HtmlTable shortHtmlTable(){
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(shortStatusCell());
	return ht;
	
    }



    public String saveServicePage(){
	String result="Save service "+probeId;
	result=result+"<br>";

	if(parameterBag.validUser=="yes"){

	    String saveServiceCommand="UPDATE Services SET CheckInterval=? where ProbeId=?";
	    PreparedStatement saveServiceStatement=null;
	    try{
		saveServiceStatement=db.getConn().prepareStatement(saveServiceCommand);
		saveServiceStatement.setInt(1,Integer.parseInt(parameterBag.checkInterval));
		//saveServiceStatement.setString(2,parameterBag.probeCommand);
		saveServiceStatement.setString(2,parameterBag.probeId);
		saveServiceStatement.executeUpdate();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+saveServiceCommand);
		System.out.println(getClass().getName()+" "+e);	    
	    }
	    try{
		saveServiceStatement.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" error occured when closng prepared statement");
		System.out.println(getClass().getName()+" "+saveServiceCommand);
		System.out.println(getClass().getName()+" "+e);	    
	    }	
	    result=result+"<br>";
	    result=result+"Service data saved:<br>";
	    result=result+"probeId="+parameterBag.probeId+"<br>";	
	    //result=result+"saveServiceCommand="+saveServiceCommand+"<br>";
	    //result=result+"probeCommand="+parameterBag.probeCommand+"<br>";
	    result=result+"checkInterval="+parameterBag.checkInterval+"<br>";
	    HtmlLink link=new HtmlLink(getLinkToNodeDetailsPage(),"Go to service details page");
	    result=result+link.toHtml()+"<br>";
	}else{
	    result=result+"You are not authorized to perform this operation<BR>";
	}
	return result;
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
	    //result=result+"<input type=\"text\" name=\"probeCommand\" value=\""+probeCommand+"\" /><br>";
	    result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Throughput Node")+"\" />";
	    result=result+"<input type=\"hidden\" name=\"probeId\" value=\""+parameterBag.probeId+"\" />";
	    result=result+"<input type=\"hidden\" name=\"save\" value=\"yes\" /><br>";
	    
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


   
    public void getHistory(){

	String getHistorySql="select * from MetricRecord where HostName=? and HostName2=? and HostName3=?  and MetricName=? ";

	IntervalSelector iS = new IntervalSelector(parameterBag);
	iS.setTimeVariable("Timestamp");
	iS.setTimeZoneShift(0);
	getHistorySql=getHistorySql+" "+iS.buildQuery(parameterBag.interval);

	history.clear();

	PreparedStatement getHistoryPreparedSql=null;
	try{
	    getHistoryPreparedSql= db.getConn().prepareStatement(getHistorySql);
	    getHistoryPreparedSql.setString(1,hostName);
	    getHistoryPreparedSql.setString(2,hostName2);
	    getHistoryPreparedSql.setString(3,hostName3);
	    getHistoryPreparedSql.setString(4,metricNameSelect);
	
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

    public String makeHistoryImage(String plotDirectory){
	String plotFileName="temp_picture.jpg";

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
	    ThroughputNodeHistoryRecord historyRecord = (ThroughputNodeHistoryRecord)iter.next();
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
    
    public String makeHistoryPlotPage(String plotDirectory){
	String result="";
	result=result+"<h3>Source="+source+", destination="+destination+", monitor="+monitor+"</h3>";
	getHistory();

	String plotFileName=makeHistoryImage(plotDirectory);

	IntervalSelector iS=new IntervalSelector(parameterBag);

	result=result+iS.toHtml()+"<br>";

	//result=result+"<img style=\"width: XSIZEpx; height: YSIZEpx;\" alt=\"alternate text\" src=\""+ plotFileName +"\">";
	result=result+"<img style=\"width: XSIZEpx; height: YSIZEpx;\" alt=\"alternate text\" src=\""+"ImageMaker" +"\">";
	result=result.replace("XSIZE",Integer.toString(imageXsize));
	result=result.replace("YSIZE",Integer.toString(imageYsize));
	result=result+"<br>";
	return result;	
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
    

    public HtmlTable getHistoryTable(){
	
	getHistory();
	
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

    public String boolean2string(boolean inputBoolean){
	String result="";
	if (inputBoolean){
	    result="Y";
	}else{
	    result="N";
	}
	return result;
    }    

}
