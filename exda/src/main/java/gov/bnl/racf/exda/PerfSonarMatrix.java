package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Enumeration;
import java.util.Calendar;
import java.util.Date;

import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Hashtable;

public class PerfSonarMatrix
{  
    private ParameterBag parameterBag = null;
    private DbConnector db=null;


    private List<PerfSonarHost> listOfSourceHosts=new ArrayList<PerfSonarHost>();
    private List<PerfSonarHost> listOfDestinationHosts=new ArrayList<PerfSonarHost>();

    private String serviceGroup=null;
    private int matrixId=-2;

    // matrix can be either "throughput" or "latency" or "apd_throughput"
    private String matrixType="throughput";
    private String matrixName="matrix name";
    private String matrixDescription="";
    private String cloudName="";
    private String schedulerName="MAIN";

    private boolean readOnly=true;

    private ProbeStatus matrixStatus = new ProbeStatus(ProbeStatus.UNDEFINED);

    PrintWriter out=null;

    Hashtable tableRows=null;

    public PerfSonarMatrix(ParameterBag paramBag,   DbConnector inputDb ){
	// constructor 0
	    this.parameterBag=paramBag;
	    this.db=inputDb;	
    }


    public PerfSonarMatrix(ParameterBag paramBag,   DbConnector inputDb,int matrixId){
	// constructor 1
	this.parameterBag=paramBag;
	this.db=inputDb;
	this.setMatrixId(matrixId);

	this.loadMatrixHosts();
	this.loadMatrixElements();
	
    }


    // get ans set methods
    public String getMatrixName(){
	return this.matrixName;
    }
    public void setMatrixName(String inputVar){
	this.matrixName=inputVar;
    }
    public String getMatrixType(){
	return this.matrixType;
    }
    public void setMatrixType(String inputVar){
	this.matrixType=inputVar;
    }
    public boolean getReadOnly(){
	return this.readOnly;
    }
    public void setReadOnly(boolean inputVar){
	this.readOnly=inputVar;
    }
    public boolean isReadOnly(){
	return this.getReadOnly();
    }
    public String getCloudName(){
	return this.cloudName;
    }
    public void setCloudName(String inputVar){
	this.cloudName=inputVar;
    }
    public String getSchedulerName(){
	return this.schedulerName;
    }
    public void setSchedulerName(String inputVar){
	this.schedulerName=inputVar;
    }

    public String getMatrixDescription(){
	return this.matrixDescription;
    }
    public void setMatrixDescription(String inputVar){
	this.matrixDescription=inputVar;
    }
    public ProbeStatus getMatrixStatus(){
	return this.matrixStatus;
    }
    public void setMatrixStatus(ProbeStatus inputVar){
	this.matrixStatus=inputVar;
    }
    public  List<PerfSonarHost> getListOfSourceHosts(){
	return this.listOfSourceHosts;
    }
    public List<PerfSonarHost> getListOfDestinationHosts(){
	return this.listOfDestinationHosts;
    }
    public int getNumberOfSourceHosts(){
	return this.getListOfSourceHosts().size();
    }
    public int getNumberOfDestinationHosts(){
	return this.getListOfDestinationHosts().size();
    }    
    // util methods
    private String getLogSignature(){
	Date date = new Date();
	String signature=date+" "+getClass().getName()+" ";
	return signature;
    }

    // load methods
    public void loadMatrixStatus(){
	// set status to UNDEFINED
	this.setMatrixStatus(new ProbeStatus(ProbeStatus.UNDEFINED));
	String query="SELECT MetricStatus from MetricRecordSummary WHERE matrixId=?";
	PreparedStatement queryPS=null;
	String executionState="";
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getMatrixId());
	    executionState="we will execute query";
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		String metricStatus=rs.getString("MetricStatus");
		ProbeStatus currentStatus=new ProbeStatus(metricStatus);
		if(currentStatus.compareTo(this.getMatrixStatus())>0){
		    this.setMatrixStatus(currentStatus);
		}
	    } 
	    executionState="we will close rs";
	    rs.close();
	}catch(Exception e){
	    System.out.println(this.getLogSignature()+"failed to read database");
	    System.out.println(this.getLogSignature()+"e="+e);
	    System.out.println(this.getLogSignature()+"query="+query);
	    System.out.println(this.getLogSignature()+"matrixid="+this.getMatrixId());
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(this.getLogSignature()+" failed to close prepared statement");
	    System.out.println(this.getLogSignature()+" query="+query);
	}
    }
    public void loadBasicMatrixInfo(){
	String query="select * from ServiceMatrices WHERE id=?";
	PreparedStatement queryPS=null;
	String executionState="";
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getMatrixId());
	    executionState="we will execute query";
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		this.setMatrixName(rs.getString("ServiceMatrixName"));
		this.setMatrixType(rs.getString("ServiceMatrixType"));
		this.setReadOnly(rs.getBoolean("ReadOnly"));
		this.setCloudName(rs.getString("CloudName"));
		this.setCloudName(rs.getString("CloudName"));
		this.setSchedulerName(rs.getString("SchedulerName"));
	    }
	    executionState="we will close result set";
	    rs.close();
	}catch(Exception e){
	    System.out.println(this.getLogSignature()+"failed to load basic matrix info");
	    System.out.println(this.getLogSignature()+" execution state="+executionState);
	    System.out.println(this.getLogSignature()+"e="+e);
	    System.out.println(this.getLogSignature()+"query="+query);
	    System.out.println(this.getLogSignature()+"matrixid="+this.getMatrixId());
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(this.getLogSignature()+" failed to close prepared statement");
	    System.out.println(this.getLogSignature()+" query="+query);
	}	    

	
    }
    public void loadMatrixHosts(){
	this.listOfSourceHosts.clear();
	this.listOfDestinationHosts.clear();
	String query="SELECT hostId FROM ServiceMatricesHosts WHERE matrixId=?";
	PreparedStatement queryPS=null;
	String executionState="";
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getMatrixId());
	    executionState="we will execute query";
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		int hostId=rs.getInt("hostId");
		PerfSonarHost perfSonarHost = new PerfSonarHost(this.parameterBag,this.db,hostId);
		this.listOfSourceHosts.add(perfSonarHost);
		this.listOfDestinationHosts.add(perfSonarHost);
	    } 
	    executionState="we will close rs";
	    rs.close();
	}catch(Exception e){
	    System.out.println(this.getLogSignature()+"failed to read database");
	    System.out.println(this.getLogSignature()+"e="+e);
	    System.out.println(this.getLogSignature()+"query="+query);
	    System.out.println(this.getLogSignature()+"matrixid="+this.getMatrixId());
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(this.getLogSignature()+"failed close prepared statement");
	    System.out.println(this.getLogSignature()+"e="+e);
	    System.out.println(this.getLogSignature()+"query="+query);
	}	
    }
    public void loadMatrixElements(){
	// fill table with elements

	this.tableRows=new Hashtable();
	// loop over source hosts
	Iterator iterSource = this.getListOfSourceHosts().iterator();
	while(iterSource.hasNext()){
	    PerfSonarHost sourceHost=(PerfSonarHost)iterSource.next();	   

	    Hashtable row = new Hashtable();
	    // now loop over destination hosts	    
	    Iterator iterDest=this.getListOfDestinationHosts().iterator();
	    while(iterDest.hasNext()){
		PerfSonarHost destinationHost=(PerfSonarHost)iterDest.next();

		PerfSonarMatrixElement element = new PerfSonarMatrixElement(parameterBag,db,this.getMatrixType(),sourceHost,destinationHost);
		row.put(destinationHost.getHostName(),element);

		// update matrix status
		ProbeStatus elementStatus = element.getStatus();
		if(elementStatus.compareTo(this.getMatrixStatus())>0){
		    this.setMatrixStatus(elementStatus);
		}
	    }
	    tableRows.put(sourceHost.getHostName(),row);
	}
    }
    // link methods
    public HtmlLink linkToMatrixDisplayPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Display PerfSonarMatrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    // === display methods ====
    public HtmlTableCell basicHtmlTableCell(){
	HtmlTableCell cell = new HtmlTableCell(this.linkToMatrixDisplayPage(this.getMatrixName()),this.getMatrixStatus().color());
	return cell;
    }
    public HtmlTable basicHtmlTable(){
	HtmlTable ht=new HtmlTable(1);
	HtmlTableCell cell = this.basicHtmlTableCell();
	ht.addCell(cell);
	return ht;
    }
    public HtmlTable fullHtmlTable(){
	HtmlTable ht=new HtmlTable(this.getNumberOfDestinationHosts()+1);
	//first fill empty element
	HtmlTableCell firstCell = new HtmlTableCell(" ");
	ht.addCell(firstCell);

	// fill rest of first row
	Iterator destinationIter=this.getListOfDestinationHosts().iterator();
	int columnNumber=0;
	while(destinationIter.hasNext()){
	    PerfSonarHost destinationHost = (PerfSonarHost)destinationIter.next();
	    HtmlTableCell destinationShortCell = destinationHost.shortColumnCell(columnNumber);
	    ht.addCell(destinationShortCell);
	    columnNumber=columnNumber+1;
	}

	// fill rest of table
	Iterator sourceIter = this.getListOfSourceHosts().iterator();
	int rowNumber=0;
	while(sourceIter.hasNext()){
	    PerfSonarHost sourceHost = (PerfSonarHost)sourceIter.next();
	    HtmlTableCell sourceCell = sourceHost.shortRowCell(rowNumber);
	    ht.addCell(sourceCell);
	    rowNumber=rowNumber+1;

	    String sourceHostName = sourceHost.getHostName();
	    
	    Hashtable row=(Hashtable)tableRows.get(sourceHostName);
	    destinationIter = this.getListOfDestinationHosts().iterator();
	    while(destinationIter.hasNext()){
		PerfSonarHost destinationHost = (PerfSonarHost)destinationIter.next();
		String destinationHostName = destinationHost.getHostName();
		PerfSonarMatrixElement element = (PerfSonarMatrixElement)row.get(destinationHostName);
		HtmlTableCell matrixCell = element.htmlTableCell();
		ht.addCell(matrixCell);
	    }
	    
	}
	return ht;
    }



    private void obtainServiceMatrixNameAndType(){
	String query="SELECT * FROM ServiceMatrices where id=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.matrixId);
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		this.serviceGroup=rs.getString("ServiceMatrixName");
		this.matrixType=rs.getString("ServiceMatrixType");
	    }
	    rs.close();
	}catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading matrix name");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" matrixId="+this.matrixId);
		System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
    }

    private List<PerfSonarHost> obtainListOfHostsInThisMatrix(){
	// read list of hosts in this matrix from the database
	List<PerfSonarHost> list=new ArrayList<PerfSonarHost>();
	
	String query="SELECT * FROM ServiceMatricesHosts WHERE matrixId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.matrixId);
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		int hostId=rs.getInt("hostId");
		if(hostId>0){
		    PerfSonarHost perfSonarHost=new PerfSonarHost(this.parameterBag,this.db,hostId);
		    list.add(perfSonarHost);
		}
	    }
	    rs.close();
	}catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading list of hosts on a matrix");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" matrixId="+this.matrixId);
		System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	return list;
    }

    private boolean isAPD(){
	System.out.println(getClass().getName()+" we are in isAPD");
	System.out.println(getClass().getName()+" this.getServiceMatrixName()="+this.getServiceMatrixName());
	if(this.getServiceMatrixType().indexOf("apd")==0){
	    return true;
	}else{
	    return false;
	}
    }

    private String decodeMatrixType(){
	if(this.getServiceMatrixName().indexOf("apd_throughput")!=-1){
	    return "apd_throughput";
	}else{
	    if(this.getServiceMatrixName().indexOf("throughput")!=-1){
		return "throughput";
	    }else{
		if(this.getServiceMatrixName().indexOf("latency")!=-1){
		    return "latency";
		}else{
		    return "unknown";
		}
	    }
	}
    }
    private String getMetricName(){
	String result="";
	if(matrixType.equals("throughput")){
	    result="net.perfsonar.service.ma.throughput";
	}
	if(matrixType.equals("latency")){
	    result="net.perfsonar.service.ma.latency";
	}
	return result;
    }

    public void setMatrixId(int inputVar){
	this.matrixId=inputVar;
    }
    public int getMatrixId(){
	return this.matrixId;
    }

    public String getServiceMatrixName(){
	return this.serviceGroup;
    }
    public String getServiceMatrixType(){
	return this.matrixType;
    }

    private void createMatrix(){
	// fill table with elements

	tableRows=new Hashtable();
	// loop over source hosts
	Iterator iterSource = listOfSourceHosts.iterator();
	while(iterSource.hasNext()){
	    PerfSonarHost sourceHost=(PerfSonarHost)iterSource.next();	   

	    Hashtable row = new Hashtable();
	    // now loop over destination hosts	    
	    Iterator iterDest=listOfDestinationHosts.iterator();
	    while(iterDest.hasNext()){
		PerfSonarHost destinationHost=(PerfSonarHost)iterDest.next();
		ServiceMatrixElementPair element=new ServiceMatrixElementPair(parameterBag,db,this.matrixId,sourceHost,destinationHost);
		row.put(destinationHost.getHostName(),element);
	    }
	    tableRows.put(sourceHost.getHostName(),row);
	}
    }

    public HtmlTable htmlTableNonAPD(){
	HtmlTable ht=new HtmlTable(listOfDestinationHosts.size()+1);
	ht.setPadding(0);
	
	HtmlTableCell firstCell=new HtmlTableCell("---");
	ht.addCell(firstCell);


	for (int i=0;i<listOfDestinationHosts.size();i=i+1){
	    ht.addCell(new HtmlTableCell(Integer.toString(i)));
	}

	for (int iSrc=0;iSrc<listOfSourceHosts.size();iSrc=iSrc+1){

	    PerfSonarHost sourceHost=(PerfSonarHost)listOfSourceHosts.get(iSrc);
	    String src=sourceHost.getHostName();

	    String fistCellText=Integer.toString(iSrc)+":"+sourceHost.getLinkSiteToExternalUrl().toHtml()+"<br>("+ HtmlStringUtils.addFontTags(src,1)+")";

	    HtmlTableCell firstColumnCell = new HtmlTableCell(fistCellText);
	    firstColumnCell.alignLeft();
	    ht.addCell(firstColumnCell);

	    Hashtable row=(Hashtable)tableRows.get(src);

	    for (int iDst=0;iDst<listOfDestinationHosts.size();iDst=iDst+1){
		PerfSonarHost destinationHost=(PerfSonarHost)listOfDestinationHosts.get(iDst);
		String dst=destinationHost.getHostName();
		
		ServiceMatrixElementPair element=(ServiceMatrixElementPair)row.get(destinationHost.getHostName());
		HtmlTable elementTable=element.htmlTable();
		ht.addCell(new HtmlTableCell(elementTable.toHtml()));
		
	    }
	}
	return ht;
    }
    public HtmlTable htmlTableAPD(){
	HtmlTable ht=new HtmlTable(listOfDestinationHosts.size()+1);
	ht.setPadding(0);
	
	HtmlTableCell firstCell=new HtmlTableCell("---");
	ht.addCell(firstCell);


	for (int i=0;i<listOfDestinationHosts.size();i=i+1){
	    ht.addCell(new HtmlTableCell(Integer.toString(i)));
	}

	for (int iSrc=0;iSrc<listOfSourceHosts.size();iSrc=iSrc+1){

	    PerfSonarHost sourceHost=(PerfSonarHost)listOfSourceHosts.get(iSrc);
	    String src=sourceHost.getHostName();

	    String fistCellText=Integer.toString(iSrc)+":"+sourceHost.getLinkSiteToExternalUrl().toHtml()+"<br>("+ HtmlStringUtils.addFontTags(src,1)+")";

	    HtmlTableCell firstColumnCell = new HtmlTableCell(fistCellText);
	    firstColumnCell.alignLeft();
	    ht.addCell(firstColumnCell);

	    Hashtable row=(Hashtable)tableRows.get(src);

	    for (int iDst=0;iDst<listOfDestinationHosts.size();iDst=iDst+1){
		PerfSonarHost destinationHost=(PerfSonarHost)listOfDestinationHosts.get(iDst);
		String dst=destinationHost.getHostName();
		
		ServiceMatrixElementAPD element=new ServiceMatrixElementAPD(this.parameterBag,this.db,this.getMatrixId(),sourceHost,destinationHost);
		
		HtmlTable elementTable=element.htmlTable();
		ht.addCell(new HtmlTableCell(elementTable.toHtml()));
		
	    }
	}
	return ht;
    }

    public HtmlTable htmlTable(){
	if(this.isAPD()){
	    System.out.println(getClass().getName()+" this is APD table");	    
	    return this.htmlTableAPD();
	}else{
	    System.out.println(getClass().getName()+" this is non APD table");	 
	    return this.htmlTableNonAPD();
	}
    }

    public HtmlTable shortHtmlTable(){

	HtmlTable ht=new HtmlTable(1);

	return ht;	    	    		
    }

    public String getExplanation(){
	String result="Unknown matrix type!";
	if(this.matrixType.equals("throughput")){
	    result=getExplanationThroughput();
	}
	if(this.matrixType.equals("latency")){
	    result=getExplanationLatency();
	}
	return result;
    }

    public String getExplanationThroughput(){
	String result="<br>The rows of this table represent SOURCE nodes for a throughput test while the columns represent DESTINATION nodes.<br>";
	result=result+"Each cell in the table contains the result of two versions of a BWCTL throughput test for the specified source and destination.<br>";
	result=result+"Tests are configured to run by BOTH the source and destination once every 4 hour period.<br>";
	result=result+"The upper link in each cell represents the results of the throughput test initiated from the SOURCE end.<br>";
	result=result+"The lower link in each cell represents the results of the throughput test initiated from the DESTINATION end.<br>";
	result=result+"A cell is OK (green) if the measured bandwidth (averaged over all measurements in the last 24 hours) is >= 100 Mbits/sec.<br>";
	result=result+"A cell is WARNING (yellow) if the measured bandwidth (averaged over all measurements in the last 24 hours)  is >=10 Mbits/sec and <100 Mbits/sec.<br>";
	
	result=result+"A cell is CRITICAL (red) if the measured bandwidth is not available (no test defined?) or is  <10 Mbits/sec (averaged over all tests in the last 24 hours)";
	result=HtmlStringUtils.addFontTags(result,1);
	return result;
    }
    public String getExplanationLatency(){
	String result="<br>The rows of this table represent SOURCE nodes for a test while the columns represent DESTINATION nodes.<br>Each cell in the table represents a source-destination LATENCY test via OWAMP (600 UDP packets/test) tests, 1/minute.<br>The metric we are plotting is the packet loss between the source and destination averaged over the last 30 minutes. <br>Each cell contains the result of two tests:<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   The upper result is the loss measured in the test initiated from the source end.<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   The lower result is the loss measured in the test initiated from the destination end.<br>An 'OK' (green) result is when the average packet loss is less than 2 out of 600 packets.<br>A 'WARNING' (orange) result is when the average packet loss >=2 but < 10 out of 600 packets.<br>A 'CRITICAL' (red) result is when EITHER the test is not defined or the packet loss >= 10 out of 600 packets.<br>An 'UNKNOWN' (brown) result may indicate any other test outcome, including but not limited to: uncomprehensible test output, no response, test timed out etc.<br>";
	result=HtmlStringUtils.addFontTags(result,1);
	return result;
    }

}
