package gov.bnl.racf.exda;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

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


public class PrimitiveServiceRecord
{  

    private int primitiveServiceId=-1;
    private String metricName="";
    private String serviceDescription="";
    private String probeCommandSample="";

    private ParameterBag parameterBag = null;
    private DbConnector db=null;


    // ===== Start of constructors ===== //

    public PrimitiveServiceRecord(ParameterBag paramBag,  DbConnector inputDb, String MetricName)
	{
	    // constructor 1
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.setMetricName(MetricName);
	    	   
	    //=========== get information =======

	    String initialisationSqlQuery="select  * from PrimitiveServices where MetricName=?";
	    PreparedStatement initialisationSql1=null;
	    try{
		initialisationSql1= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql1.setString(1,this.getMetricName());

		ResultSet rs=initialisationSql1.executeQuery();
		unpackResultSet(rs);
		rs.close();

	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database in constructor 1");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" Metricname="+this.getMetricName());
		System.out.println(getClass().getName()+" "+e) ;   
	    }
	    try{
		initialisationSql1.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e) ;   
	    }		
	}
    public PrimitiveServiceRecord(ParameterBag paramBag,  DbConnector inputDb, int primitiveServiceId)
	{

	    // constructor 1
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.setPrimitiveServiceId(primitiveServiceId);
	    	   
	    //=========== get information =======

	    String initialisationSqlQuery="select  * from PrimitiveServices where primitiveServiceId=?";
	    PreparedStatement initialisationSql1= null;
	    try{
		initialisationSql1= db.getConn().prepareStatement(initialisationSqlQuery);
		initialisationSql1.setInt(1,this.getPrimitiveServiceId());

		ResultSet rs=initialisationSql1.executeQuery();
		unpackResultSet(rs);
		rs.close();

	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when reading database in constructor 1");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" Metricname="+this.getMetricName());
		System.out.println(getClass().getName()+" "+e) ;   
	    }
	    try{
		initialisationSql1.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+initialisationSqlQuery);
		System.out.println(getClass().getName()+" "+e) ;   
	    }
	}
    public  void setPrimitiveServiceId(int inputVar){
	this.primitiveServiceId=inputVar;
    }
    public  int getPrimitiveServiceId(){
	return this.primitiveServiceId;
    }
    public void setMetricName(String inputVar){
	this.metricName=inputVar;
    }
    public String getMetricName(){
	return this.metricName;
    }
    public String getServiceName(){
	return this.getMetricName();
    }
    public void setServiceDescription(String inputVar){
	this.serviceDescription=inputVar;
    }
    public String getServiceDescription(){
	return this.serviceDescription;
    }
    public void setProbeCommandSample(String inputVar){
	this.probeCommandSample=inputVar;
    }
    public String getProbeCommandSample(){
	return this.probeCommandSample;
    }
    public void unpackResultSet(ResultSet rs){
	    try{
		while (rs.next ()){
		    this.setMetricName(rs.getString("MetricName"));
		    this.setServiceDescription(rs.getString("ServiceDescription"));
		    this.setProbeCommandSample(rs.getString("ProbeCommandSample"));
		    this.setPrimitiveServiceId(rs.getInt("primitiveServiceId"));
		}
	    }catch(Exception e){
		System.out.println(getClass().getName()+" error occured when unpacking result set");
		System.out.println(getClass().getName()+" metricname="+this.getMetricName()) ;  
		System.out.println(getClass().getName()+" serviceid="+this.getPrimitiveServiceId()) ;  
		System.out.println(getClass().getName()+" "+e) ;   
	    }
    }


}
