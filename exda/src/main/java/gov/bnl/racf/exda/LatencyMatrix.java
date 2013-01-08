package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class LatencyMatrix
{  

    private String latencyMetricName="net.perfsonar.service.ma.latency";

    private ParameterBag parameterBag = null;

    private DbConnector db=null;

    private ResultSet rs=null;

    // private List<String> listOfSourceHosts=new ArrayList<String>();
    //private List<String> listOfDestinationHosts=new ArrayList<String>();

    private List<PerfSonarHost> listOfSourceHosts=new ArrayList<PerfSonarHost>();
    private List<PerfSonarHost> listOfDestinationHosts=new ArrayList<PerfSonarHost>();

    private int numberOfHostNames=0;
    private String cloudName=null;


    public LatencyMatrix(ParameterBag paramBag,   DbConnector inputDb)
	{
	    parameterBag=paramBag;
	    db=inputDb;

	    //=========== get matrix nodes =======
	    String getSourceHosts="select DISTINCT HostName2 from MetricRecordSummary where MetricName=? order by HostName2";	    
	    PreparedStatement initialisationSql=null;
	    try{
		initialisationSql= db.getConn().prepareStatement(getSourceHosts);
		initialisationSql.setString(1,  latencyMetricName  );
		rs=initialisationSql.executeQuery();
		int count = 0;
		while (rs.next ())
		    {
			String src=rs.getString("HostName2");
			listOfSourceHosts.add(new PerfSonarHost(parameterBag,db,src));
			listOfDestinationHosts.add(new PerfSonarHost(parameterBag,db,src));
			count=count+1;
		    }
		rs.close();
		numberOfHostNames=count;
		Collections.sort(listOfSourceHosts);
		Collections.sort(listOfDestinationHosts);
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		initialisationSql.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+getSourceHosts);
		System.out.println(getClass().getName()+" "+e);
	    }		



	}

    public String toString(){
	String result="Hello world from latency matrix";
	return result;
    }

    public HtmlTable htmlTable(){

	HtmlTable ht=new HtmlTable(numberOfHostNames+1);
	ht.setPadding(1);
	
	HtmlTableCell firstCell=new HtmlTableCell("---");
	ht.addCell(firstCell);

	for (int i=0;i<listOfDestinationHosts.size();i=i+1){
	    ht.addCell(new HtmlTableCell(Integer.toString(i)));
	}

	for (int iSrc=0;iSrc<listOfSourceHosts.size();iSrc=iSrc+1){

	    PerfSonarHost sourceHost=(PerfSonarHost)listOfSourceHosts.get(iSrc);
	    String src=sourceHost.getHostName();

	    String firstColumnText=Integer.toString(iSrc)+":"+sourceHost.getLinkSiteToExternalUrl().toHtml()+"<br>("+ HtmlStringUtils.addFontTags(sourceHost.getHostName(),1)+")";

	    HtmlTableCell firstColumnCell = new HtmlTableCell(firstColumnText);
	    firstColumnCell.alignLeft();
	    ht.addCell(firstColumnCell);
	    
	    for (int iDst=0;iDst<listOfDestinationHosts.size();iDst=iDst+1){
		PerfSonarHost destinationHost=(PerfSonarHost)listOfDestinationHosts.get(iDst);
		String dst=destinationHost.getHostName();		

		LatencyNode upperNode=new LatencyNode(parameterBag,db,src,dst,src);
		LatencyNode lowerNode=new LatencyNode(parameterBag,db,src,dst,dst);
		    HtmlTable nodeTable=new HtmlTable(1);
		    //nodeTable.addCell(upperNode.veryShortStatusCell());
		    //nodeTable.addCell(lowerNode.veryShortStatusCell());
		    nodeTable.addCell(upperNode.numericStatusCell());
		    nodeTable.addCell(lowerNode.numericStatusCell());
		    nodeTable.setBorder(0);
		    nodeTable.setPadding(0);
		    ht.addCell(new HtmlTableCell(nodeTable.toHtml()));
		    
	    }
	}


	return ht;
    }

    public String getExplanation(){
	String result="<br>The rows of this table represent SOURCE nodes for a test while the columns represent DESTINATION nodes.<br>Each cell in the table represents a source-destination LATENCY test via OWAMP (600 UDP packets/test) tests, 1/minute.<br>The metric we are plotting is the packet loss between the source and destination averaged over the last 30 minutes. <br>Each cell contains the result of two tests:<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   The upper result is the loss measured in the test initiated from the source end.<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   The lower result is the loss measured in the test initiated from the destination end.<br>An 'OK' (green) result is when the average packet loss is less than 2 out of 600 packets.<br>A 'WARNING' (orange) result is when the average packet loss >=2 but < 10 out of 600 packets.<br>A 'CRITICAL' (red) result is when EITHER the test is not defined or the packet loss >= 10 out of 600 packets.<br>An 'UNKNOWN' (brown) result may indicate any other test outcome, including but not limited to: uncomprehensible test output, no response, test timed out etc.<br>";
	result=HtmlStringUtils.addFontTags(result,1);
	return result;
    }





    public HtmlTable shortHtmlTable(){

	HtmlTable ht=new HtmlTable(1);

	return ht;	    	    		
    }
}
