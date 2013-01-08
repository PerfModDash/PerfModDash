package gov.bnl.racf.exda;

import java.sql.*;

public class MatrixNodeHistoryRecord
{  

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

    private StringBuffer detailsStringBuffer=null;


    public MatrixNodeHistoryRecord()
    {
	    
    }
    public MatrixNodeHistoryRecord(ResultSet rs){
	try{
	    this.metricStatus = new ProbeStatus(rs.getString("MetricStatus"));
	    this.timestamp    = rs.getTimestamp("Timestamp");
	    this.serviceType  = rs.getString("ServiceType");
	    this.serviceUri   = rs.getString("ServiceUri");
	    this.gatheredAt   = rs.getString("GatheredAt");
	    this.summaryData  = rs.getString("SummaryData");
	    this.detailsData  = rs.getString("DetailsData");
	
	    this.detailsStringBuffer=new StringBuffer(this.detailsData);

	    this.performanceData4 = (double)rs.getDouble("PerformanceData4");
	    this.performanceData3 = (double)rs.getDouble("PerformanceData3");
	    this.performanceData1 = (double)rs.getDouble("PerformanceData1");
	    this.performanceData2 = (double)rs.getDouble("PerformanceData2");

	    this.throughput_min = this.performanceData3;
	    this.throughput_max = this.performanceData1;
	    this.throughput_avg = this.performanceData2;

	    this.sigma          = this.performanceData4;

	    this.latency_min=this.performanceData3;
	    this.latency_max=this.performanceData1;
	    this.latency_avg=this.performanceData2;
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when unpacking history record");
		System.out.println(getClass().getName()+" "+e);	    
	}
    }
}
