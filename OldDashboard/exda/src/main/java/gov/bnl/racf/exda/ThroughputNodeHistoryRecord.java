package gov.bnl.racf.exda;

import java.sql.Timestamp;

public class ThroughputNodeHistoryRecord
{  

    public String moduleName="ThroughputNodeHistoryRecord";
    public double throughput_min=0.0;
    public double throughput_max=0.0;
    public double throughput_avg=0.0;
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


    public ThroughputNodeHistoryRecord()
    {
	    
    }
}
