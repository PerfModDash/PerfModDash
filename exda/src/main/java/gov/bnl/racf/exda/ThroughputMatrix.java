package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;



public class ThroughputMatrix
{  
    private ParameterBag parameterBag = null;

    private DbConnector db=null;

    private ResultSet rs=null;
    private String initialisationSql="";


    private List<PerfSonarHost> listOfSourceHosts=new ArrayList<PerfSonarHost>();
    private List<PerfSonarHost> listOfDestinationHosts=new ArrayList<PerfSonarHost>();
    private int numberOfHostNames=0;

    private String cloudName=null;
    private String serviceGroup=null;

    PrintWriter out=null;



    // public ThroughputMatrix(ParameterBag paramBag,   DbConnector inputDb,String cloudName)
    // 	{
    // 	    parameterBag=paramBag;
    // 	    db=inputDb;
    // 	    this.cloudName=cloudName;

    // 	    //=========== get matrix nodes =======
    // 	    String getSourceHosts="select DISTINCT HostName2 from MetricRecordSummary where MetricName='net.perfsonar.service.ma.throughput' order by HostName2";	    
    // 	    String getDestinationHosts="select DISTINCT HostName3 from MetricRecordSummary where MetricName='net.perfsonar.service.ma.throughput' order by HostName3";

    // 	    try{
    // 		rs=db.query(getSourceHosts);
    // 		int count = 0;
    // 		while (rs.next ())
    // 		    {
    // 			String src=rs.getString("HostName2");
    // 			PerfSonarHost host = new PerfSonarHost(parameterBag,db,src);
    // 			if(host.getCloudName().equals(cloudName)){
    // 			    listOfSourceHosts.add(host);
    // 			    count=count+1;
    // 			}
    // 		    }
    // 		numberOfHostNames=count;

    // 		Collections.sort(listOfSourceHosts);

    // 	    }catch (Exception e){
    // 		System.out.println(getClass().getName()+" error occured when reading database");
    // 		System.out.println(getClass().getName()+" "+e);
    // 	    }
    // 	    try{
    // 		rs=db.query(getDestinationHosts);
    // 		int count = 0;
    // 		while (rs.next ())
    // 		    {
    // 			String dst=rs.getString("HostName3");
    // 			PerfSonarHost host = new PerfSonarHost(parameterBag,db,dst);
    // 			if(host.getCloudName().equals(cloudName)){
    // 			    listOfDestinationHosts.add(host);
    // 			    count=count+1;
    // 			}
    // 		    }
    // 		numberOfHostNames=count;

    // 		Collections.sort(listOfDestinationHosts);

    // 	    }catch (Exception e){
    // 		System.out.println(getClass().getName()+" error occured when reading database");
    // 		System.out.println(getClass().getName()+" "+e);
    // 	    }

    // 	}

    public ThroughputMatrix(ParameterBag paramBag,   DbConnector inputDb)
	{

	    parameterBag=paramBag;
	    db=inputDb;

	    //=========== get matrix nodes =======
	    String getSourceHosts="select DISTINCT HostName2 from MetricRecordSummary where MetricName='net.perfsonar.service.ma.throughput' order by HostName2";	    

	    try{
		rs=db.query(getSourceHosts);
		int count = 0;
		while (rs.next ())
		    {
			String src=rs.getString("HostName2");
			listOfSourceHosts.add(new PerfSonarHost(parameterBag,db,src));
			listOfDestinationHosts.add(new PerfSonarHost(parameterBag,db,src));
			count=count+1;
		    }
		numberOfHostNames=count;

		Collections.sort(listOfSourceHosts);
		Collections.sort(listOfDestinationHosts);

	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database");
		System.out.println(getClass().getName()+" "+e);
	    }



	}

    public String toString(){
	String result="Hello world from throughput matrix";
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

	    //PerfSonarHost sourceHost=new PerfSonarHost(parameterBag,db,src);
	    PerfSonarHost sourceHost=(PerfSonarHost)listOfSourceHosts.get(iSrc);
	    String src=sourceHost.getHostName();

	    String fistCellText=Integer.toString(iSrc)+":"+sourceHost.getLinkSiteToExternalUrl().toHtml()+"<br>("+ HtmlStringUtils.addFontTags(sourceHost.getHostName(),1)+")";

	    HtmlTableCell firstColumnCell = new HtmlTableCell(fistCellText);
	    firstColumnCell.alignLeft();
	    ht.addCell(firstColumnCell);
	    
	    for (int iDst=0;iDst<listOfDestinationHosts.size();iDst=iDst+1){
		PerfSonarHost destinationHost=(PerfSonarHost)listOfDestinationHosts.get(iDst);
		String dst=destinationHost.getHostName();
		if (src.equals(dst)){
		    ht.addCell(new HtmlTableCell("---"));
		}else{
		    ThroughputNode upperNode=new ThroughputNode(parameterBag,db,src,dst,src);
		    ThroughputNode lowerNode=new ThroughputNode(parameterBag,db,src,dst,dst);
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
	}


	return ht;
    }

    public HtmlTable shortHtmlTable(){

	HtmlTable ht=new HtmlTable(1);

	return ht;	    	    		
    }

    public String getExplanation(){
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


}
