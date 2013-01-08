package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Enumeration;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONObject;


import java.util.Hashtable;

public class ServiceMatrix
{  
    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private String initialisationSql="";


    private List<PerfSonarHost> listOfSourceHosts=new ArrayList<PerfSonarHost>();
    private List<PerfSonarHost> listOfDestinationHosts=new ArrayList<PerfSonarHost>();
    private int numberOfHostNames=0;

    private String cloudName=null;
    private String serviceGroup=null;
    private int matrixId=-2;

    // matrix can be either "throughput" or "latency" or "apd_throughput"
    private String matrixType="throughput";

    PrintWriter out=null;

    Hashtable tableRows=null;

    public ServiceMatrix(ParameterBag paramBag,   DbConnector inputDb,int matrixId)
	{
	    String failurePoint="a";
	    try{
		// constructor 1
		this.parameterBag=paramBag;
		failurePoint="b";
		this.db=inputDb;
		failurePoint="c";
		this.setMatrixId(matrixId);
		
		// this will fill serviceGroup and matrixType
		failurePoint="d";
		this.obtainServiceMatrixNameAndType();
		
		failurePoint="e";
		this.listOfSourceHosts=this.obtainListOfHostsInThisMatrix();
		failurePoint="f";
		Collections.sort(this.listOfSourceHosts);
		failurePoint="g";
		this.listOfDestinationHosts=this.listOfSourceHosts;
		failurePoint="h";

		this.createMatrix(); 
		failurePoint="i";
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to create service matrix");
		System.out.println(getClass().getName()+" failurePoint="+failurePoint);
		System.out.println(getClass().getName()+" matrixId="+matrixId);
		System.out.println(getClass().getName()+" "+e);
	    }
	}


    public ServiceMatrix(ParameterBag paramBag,   DbConnector inputDb,String matrixType, String cloudName, String serviceGroup)
	{
	    parameterBag=paramBag;
	    db=inputDb;
	    this.serviceGroup= serviceGroup;

	    //=========== get matrix nodes =======
	    String getSourceHosts="select DISTINCT HostName2 from Services where ServiceGroup=? order by HostName2";	    
	    String getDestinationHosts="select DISTINCT HostName3 from Services where ServiceGroup=? order by HostName3";

	    
	    PreparedStatement getSourceHostsQuery=null;
	    try{
		getSourceHostsQuery=db.getConn().prepareStatement(getSourceHosts);
		getSourceHostsQuery.setString(1,this.serviceGroup);
		ResultSet rs=getSourceHostsQuery.executeQuery();
		int count = 0;
		while (rs.next ()){
		    String src=rs.getString("HostName2");
		    PerfSonarHost host = new PerfSonarHost(parameterBag,db,src);
		    listOfSourceHosts.add(host);
		    count=count+1;
		}
		rs.close();
		numberOfHostNames=count;

		Collections.sort(listOfSourceHosts);

	    }catch (Exception e){
		System.out.println(getClass().getName()+" point 3");
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+getSourceHosts);
		System.out.println(getClass().getName()+" "+e);
	    }

	    try{
		getSourceHostsQuery.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+getSourceHosts);
		System.out.println(getClass().getName()+" "+e);
	    }

	    PreparedStatement getDestinationHostsQuery=null;
	    try{
		getDestinationHostsQuery=db.getConn().prepareStatement(getDestinationHosts);
		getDestinationHostsQuery.setString(1,this.serviceGroup);
		ResultSet rs=getDestinationHostsQuery.executeQuery();
		int count = 0;
		while (rs.next ()){
		    String dst=rs.getString("HostName3");
		    PerfSonarHost host = new PerfSonarHost(parameterBag,db,dst);
		    listOfDestinationHosts.add(host);
		    count=count+1;
		}
		rs.close();
		numberOfHostNames=count;

		Collections.sort(listOfDestinationHosts);

	    }catch (Exception e){
		System.out.println(getClass().getName()+" point 4");
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+getSourceHosts);
		System.out.println(getClass().getName()+" "+e);
	    }

	    try{
		getSourceHostsQuery.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+getDestinationHosts);
		System.out.println(getClass().getName()+" "+e);
	    }

	    // when we create matrix via service group, the input MatrixType is void, since the ServiceGroup already 
	    // contains Matrix Type information. We need to decode matrix type from MetricName now
	    this.matrixType=this.decodeMatrixType();

	    this.createMatrix(); 

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
		    //System.out.println(getClass().getName()+" before host");
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
	if(this.getServiceMatrixType().indexOf("apd")>-1){
	    return true;
	}else{
	    return false;
	}
    }
    private boolean isTraceroute(){
	if(this.getServiceMatrixType().indexOf("traceroute")>-1){
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
		    if(this.getServiceMatrixName().indexOf("traceroute")>-1){
			return "traceroute";
		    }else{
			return "unknown";
		    }
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
	if(matrixType.equals("throughput")){
	    result="net.perfsonar.service.ma.throughput";
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


    public ArrayList<JSONObject> toListOfJsonObjects(){
	ArrayList<JSONObject> listOfJsonObjects = new ArrayList<JSONObject>();

	for (int iSrc=0;iSrc<listOfSourceHosts.size();iSrc=iSrc+1){

	    PerfSonarHost sourceHost=(PerfSonarHost)listOfSourceHosts.get(iSrc);
	    String src=sourceHost.getHostName();

	    Hashtable row=(Hashtable)tableRows.get(src);

	    for (int iDst=0;iDst<listOfDestinationHosts.size();iDst=iDst+1){
		PerfSonarHost destinationHost=(PerfSonarHost)listOfDestinationHosts.get(iDst);
		String dst=destinationHost.getHostName();
		
		ServiceMatrixElementPair element=(ServiceMatrixElementPair)row.get(destinationHost.getHostName());
		ArrayList<JSONObject> componentServices = element.toJson();
		listOfJsonObjects.addAll(componentServices);
	    }
	}
	return listOfJsonObjects;
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
	if(this.isAPD()||this.isTraceroute()){
	    return this.htmlTableAPD();
	}else{
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
	    result=this.getExplanationThroughput();
	}
	if(this.matrixType.equals("latency")){
	    result=this.getExplanationLatency();
	}
	if(this.isTraceroute()){
	    result=this.getExplanationTraceroute();
	}
	return result;
    }
    public String getExplanationTraceroute(){
	String result="<br>The rows of this table represent SOURCE nodes for a traceroute test while the columns represent DESTINATION nodes.<br>";
	result=result+"OK=green, WARNING=yellow, CRITICIAL=red, UNKNOWN=brown<br>";
	return result;
    }
    public String getExplanationThroughput(){
	String result="<br>The rows of this table represent SOURCE nodes for a throughput test while the columns represent DESTINATION nodes.<br>";
	result=result+"Each cell in the table contains the result of two versions of a BWCTL throughput test for the specified source and destination.<br>";
	result=result+"Tests are configured to run by BOTH the source and destination once every 4 hour period.<br>";
	result=result+"The upper link in each cell represents the results of the throughput test (Gb/s) initiated from the SOURCE end.<br>";
	result=result+"The lower link in each cell represents the results of the throughput test (Gb/s) initiated from the DESTINATION end.<br>";
	result=result+"A cell is OK (green) if the measured bandwidth (averaged over all measurements in the last 24 hours) is >= 100 Mbits/sec.<br>";
	result=result+"A cell is WARNING (yellow) if the measured bandwidth (averaged over all measurements in the last 24 hours)  is >=10 Mbits/sec and <100 Mbits/sec.<br>";
	
	result=result+"A cell is CRITICAL (red) if the measured bandwidth is not available (no test defined?) or is  <10 Mbits/sec (averaged over all tests in the last 24 hours)";
	result=result+"<br>A 'TIMEOUT' (grey) result represents measurements which return 'Error contacting MA' message<br>An 'UNKNOWN' (brown) result may indicate any other test outcome, including but not limited to: uncomprehensible test output, no response, test timed out etc.<br>";
	result=HtmlStringUtils.addFontTags(result,1);
	return result;
    }
    public String getExplanationLatency(){
	String result="<br>The rows of this table represent SOURCE nodes for a test while the columns represent DESTINATION nodes.<br>Each cell in the table represents a source-destination LATENCY test via OWAMP (600 UDP packets/test) tests, 1/minute.<br>The number shown in the table is the maximum packet loss between source and destination in the past 20 minutes.<br>The metric we are using for alarm purposes and plotting is the packet loss between the source and destination averaged over the last 30 minutes. <br>Each cell contains the result of two tests:<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   The upper result is the loss measured in the test initiated from the source end.<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   The lower result is the loss measured in the test initiated from the destination end.<br>An 'OK' (green) result is when the average packet loss is less than 2 out of 600 packets.<br>A 'WARNING' (orange) result is when the average packet loss >=2 but < 10 out of 600 packets.<br>A 'CRITICAL' (red) result is when EITHER the test is not defined or the packet loss >= 10 out of 600 packets.<br>'TIMEOUT' (grey) result represents measurements which return 'Error contacting MA' message<br>An 'UNKNOWN' (brown) result may indicate any other test outcome, including but not limited to: uncomprehensible test output, no response, test timed out etc.<br>";
	result=HtmlStringUtils.addFontTags(result,1);
	return result;
    }

}
