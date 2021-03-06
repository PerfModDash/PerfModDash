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


public class LatencyNode extends MatrixNode
{  

    public LatencyNode(ParameterBag paramBag,  DbConnector inputDb,String probeIdInput ){
	super(paramBag,inputDb,probeIdInput);
	
    }
    public LatencyNode(ParameterBag paramBag,  DbConnector inputDb,String inputSource, String inputDestination, String inputMonitor){
	super(paramBag,inputDb,inputSource,inputDestination,inputMonitor);
    }
    public void defineLocalVariables(){
	this.metricNameSelect="net.perfsonar.service.ma.latency";
	this.detailPageName="Latency Node";
    }

    public HtmlTableCell numericStatusCell(){
	// return very short status table containing only status word and color
	// to be used in throughput matrix	

	HtmlTableCell cell=null;
	if (emptyNode){
	    cell=new HtmlTableCell("---",metricStatus.color());
	}else{
	    String cellTitle="Initiator: "+monitor+", From: "+source+", To: "+destination;
	    String cellText=String.format("%4.1f",latency_max);
	    cellText=HtmlStringUtils.addFontTags(cellText,fontSize);
	    HtmlLink link=new HtmlLink(linkToNodeDetailPage,cellText,cellTitle);
	    cell=new HtmlTableCell(link,metricStatus.color());
	}
	return cell;
	
    }


    public HtmlTable fullHtmlTable(){
	String result="";
	result="<strong>ProbeId: </strong>"+probeId +"<br>";

	if(active){
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
	result=result+"<strong>Next check time: </strong>"+nextCheckTime;
	if(parameterBag.validUser=="yes"){
	    result=result+fb.toHtml();
	}
	result=result+"<br>";

	UserInfo currentUser=parameterBag.getUser();
	//result=result+currentUser.getDN()+"<br>";
	if (currentUser.isApproved()){
	    if(currentUser.hasRole("admin")){
		String editPage="Edit Latency Node";
		EditServiceButton eB=new EditServiceButton(parameterBag,db,editPage);
		result=result+eB.toHtml()+"<br>";
	    }
	}
	    

	
	// build the address to plot page
	
	ParameterBag temporaryParameterBag = (ParameterBag)parameterBag.clone();
	temporaryParameterBag.page=ParameterBag.pageAddress("Latency Node History Plot");
	String urlOfPlotPage=temporaryParameterBag.makeLink();
	HtmlLink linkToPlotPage=new HtmlLink(urlOfPlotPage,"Link to history plot");
	result=result+linkToPlotPage.toHtml()+"<br>";
	

	
	// build the address to history table page
	ParameterBag temporaryParameterBag2 = (ParameterBag)parameterBag.clone();
	temporaryParameterBag2.page=ParameterBag.pageAddress("Latency Node History Table");
	String urlOfHistoryTablePage=temporaryParameterBag2.makeLink();
	HtmlLink linkToHistoryTablePage=new HtmlLink(urlOfHistoryTablePage,"Link to history table");
	result=result+linkToHistoryTablePage.toHtml()+"<br>";
	
	HtmlTableCell hc =new HtmlTableCell(result,metricStatus.color());
	hc.alignLeft();
	HtmlTable ht=new HtmlTable(1);
	ht.addCell(hc);

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
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+saveServiceCommand);
		System.out.println(getClass().getName()+" "+e);	    
	    }		
	    result=result+"<br>";
	    result=result+"Service data saved:<br>";
	    result=result+"probeId="+parameterBag.probeId+"<br>";	
	    result=result+"checkInterval="+parameterBag.checkInterval+"<br>";
	    HtmlLink link=new HtmlLink(getLinkToNodeDetailsPage(),"Go to service details page");
	    result=result+link.toHtml()+"<br>";
	    
	}else{
	    result=result+"You are not authorized to perform this operation<BR>";
	}
	// write the result in log file
	String logUpdateStatement="INSERT INTO ActionHistory (MetricName,HostName,dn,message,action,TimeStamp) VALUES(?,?,?,?,?,NOW())"; 
	PreparedStatement insertLogStatement=null;
	try{
	    insertLogStatement=db.getConn().prepareStatement(logUpdateStatement);
	    insertLogStatement.setString(1,metricName);
	    insertLogStatement.setString(2,source+":"+destination+":"+monitor);
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

    public String editServicepage(){
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
	    //result=result+"<input type=\"text\" name=\"probeCommand\" value=\""+probeCommand+"\" /><br>";
	    result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Latency Node")+"\" />";
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
   

    public String makeHistoryImage(String plotDirectory){
	String plotFileName="temp_picture.jpg";

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
    
    public String makeHistoryPlotPage(String plotDirectory){
	String result="";
	result=result+"<h3>Source="+source+", destination="+destination+", monitor="+monitor+"</h3>";
	getHistory();

	String plotFileName=makeHistoryImage(plotDirectory);

	IntervalSelector iS=new IntervalSelector(parameterBag);

	result=result+iS.toHtml()+"<br>";

	//result=result+"<img style=\"width: XSIZEpx; height: YSIZEpx;\" alt=\"alternate text\" src=\""+ plotFileName +"\">";
	result=result+"<img style=\"width: XSIZEpx; height: YSIZEpx;\" alt=\"alternate text\" src=\""+ "ImageMaker" +"\">";
	result=result.replace("XSIZE",Integer.toString(imageXsize));
	result=result.replace("YSIZE",Integer.toString(imageYsize));
	result=result+"<br>";
	return result;	
    }
    

    public String getHistoryTablePage(){
	String result="";
	result=result+"<h3>Source="+source+", destination="+destination+", monitor="+monitor+"</h3>";
	IntervalSelector iS=new IntervalSelector(parameterBag);

	result=result+iS.toHtml()+"<br>";

	HtmlTable historyTable=getHistoryTable();
	result=result+historyTable.toHtml();
	return result;
    }
    



}
