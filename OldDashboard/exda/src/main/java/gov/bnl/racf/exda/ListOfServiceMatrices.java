package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfServiceMatrices
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private ResultSet rs=null;

    private String cloudName=null;

    private List<ServiceMatrixRecord>listOfServiceMatrixRecords=new ArrayList<ServiceMatrixRecord>();

    public ListOfServiceMatrices(ParameterBag paramBag,   DbConnector inputDb)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.loadListOfServiceMatrixRecords();
	}
    private void loadListOfServiceMatrixRecords(){

	String getQuery="select id from ServiceMatrices";
	PreparedStatement getQueryPS= null;
	try{
	    getQueryPS= db.getConn().prepareStatement(getQuery);

	    rs=getQueryPS.executeQuery();
	    int count = 0;
	    while (rs.next ()){
		int id=rs.getInt("id");
		listOfServiceMatrixRecords.add(new ServiceMatrixRecord( this.parameterBag,this.db,id));
		count=count+1;
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+getQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getQueryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+getQuery);
	    System.out.println(getClass().getName()+" "+e);
	}

    }
    public List<ServiceMatrixRecord> getList(){
	return this.listOfServiceMatrixRecords;
    }
    private HtmlLink getLinkToCreateServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Service Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToCreateThroughputServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Throughput Service Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToCreateTracerouteServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Traceroute Service Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToCreateAPDThroughputServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create APD Throughput Service Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToCreateAPDLatencyServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create APD Latency Service Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToChooseNameForThroughputServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Choose Name for Throughput Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToChooseNameForTracerouteServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Choose Name for Traceroute Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToChooseNameForAPDThroughputServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Choose Name for APD Throughput Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToChooseNameForAPDLatencyServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Choose Name for APD Latency Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToCreateLatencyServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Latency Service Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToChooseNameForLatencyServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Choose Name for Latency Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }

    public String toString(){
	String result="";
	Iterator iter=this.listOfServiceMatrixRecords.iterator();
	while (iter.hasNext()){
	    String nextCloudName=(String)iter.next();
	    result=result+nextCloudName+" ";
	}
	return result;
    }

    public HtmlTable htmlTable(){
	HtmlTable ht=new HtmlTable(6);
	ht.setBorder(1);
	ht.setPadding(0);
	
	Iterator itr = this.listOfServiceMatrixRecords.iterator(); 
	while(itr.hasNext()) {
	    ServiceMatrixRecord smr=(ServiceMatrixRecord)itr.next();
	    ht.addRow(smr.infoTableRow());
	} 
	return ht;
    }
    public String htmlPage(){
	String result="<H1>List of Service Matrices</H1>";
	result=result+"<br>";
	//result=result+this.getLinkToCreateServiceMatrixPage("<strong>Create new Service Matrix</strong>")+"<br>";
	result=result+this.getLinkToChooseNameForThroughputServiceMatrixPage("<strong>Create new Throughput Service Matrix</strong>");
	result=result+" or ";
	result=result+this.getLinkToChooseNameForLatencyServiceMatrixPage("<strong>Create new Latency Service Matrix</strong>");
	result=result+" or ";
	result=result+this.getLinkToChooseNameForTracerouteServiceMatrixPage("<strong>Create new Traceroute Service Matrix</strong>");
	result=result+" or ";
	result=result+this.getLinkToChooseNameForAPDThroughputServiceMatrixPage("<strong>Create new APD Throughput Service Matrix</strong>");
	result=result+"<br>";
	result=result+this.getLinkToChooseNameForAPDLatencyServiceMatrixPage("<strong>Create new APD Latency Service Matrix</strong>");
	result=result+"<br>";
	result=result+this.htmlTable().toHtml();
	return result;
    }
}
