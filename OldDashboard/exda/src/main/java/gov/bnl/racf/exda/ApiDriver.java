package gov.bnl.racf.exda;

import java.lang.Class;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import java.net.InetAddress;
import java.net.*;
import java.sql.*;

import javax.security.cert.X509Certificate;
import java.security.cert.*;


import org.json.simple.JSONObject;
  
//import java.io.File;

public class ApiDriver extends HttpServlet {

    private Properties dbParameters = null;
    private DbConnector dB=null;
    
    private String dbConfigFileName="WEB-INF/classes/db.properties"; 

    private String clientHostName=null;

    PrintWriter out=null;
    OutputStream outImage=null;

    public void init(ServletConfig config) throws ServletException {
	// Store the ServletConfig object and log the initialization
	java.util.Date now = new java.util.Date();
	System.out.println(now+" "+getClass().getName()+" ****************************************");
	System.out.println(now+" "+getClass().getName()+" *** New connection starts            ***");
	System.out.println(now+" "+getClass().getName()+" ****************************************");
	super.init(config);
	this.dbConfigFileName=getServletContext().getRealPath("/")+this.dbConfigFileName;
	this.openConnection();
    }

    private void openConnection(){
	// create database connector
	try{
	    dbParameters=new Properties();
	    dbParameters.load(new FileInputStream( this.dbConfigFileName ));
	}catch (IOException ex) {
	    System.out.println(getClass().getName()+" Error: unable to open configuration file "+this.dbConfigFileName);
	    System.out.println(getClass().getName()+" "+ex);
	    ex.printStackTrace();
	}
	String hostname="";
	try {
	    InetAddress addr = InetAddress.getLocalHost();
	    
	    // Get IP Address
	    byte[] ipAddr = addr.getAddress();
	    
	    // Get hostname
	    hostname = addr.getHostName();
	    this.clientHostName=hostname;
	    
	} catch (UnknownHostException e) {
	    java.util.Date now = new java.util.Date();
	    System.out.println(now+" "+getClass().getName()+" failed to get hostname: hostname="+hostname);
	    System.out.println(now+" "+getClass().getName()+" "+e);
	}
	
	if (hostname.indexOf("talos") > -1){  
	    java.util.Date now = new java.util.Date();
	    System.out.println(now+" "+getClass().getName()+" connect to griddev03");
	    dB=new DbConnector(6, dbConfigFileName );
	}else{
	    if (hostname.indexOf("grid09") > -1){  	    
		java.util.Date now = new java.util.Date();
		System.out.println(now+" "+getClass().getName()+" connect to grid09");
		dB=new DbConnector(4, dbConfigFileName );	    
	    }else{
		
		java.util.Date now = new java.util.Date();
		System.out.println(now+" "+getClass().getName()+" connect to grid21");
		dB=new DbConnector(5, dbConfigFileName );
	    }
	}
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

	if(this.dB.getConnection()==null){
	    openConnection();
	}else{
	    if(this.dB.isClosed()){
		this.dB.close();
		this.openConnection();
	    }
	}



	HttpSession session = request.getSession();

	String remoteAddr = request.getRemoteAddr(); 

	// Get client's (remote) hostname
	String remoteHost = request.getRemoteHost(); 


	ParameterBag paramBag=new ParameterBag();

	String driverAction=request.getParameter("driverAction");
	String id=request.getParameter("id");

	if(driverAction!=null){
	    if(driverAction.equals("getServices")){
		if(id==null){
		    ListOfServices listOfServices = new ListOfServices(paramBag, this.dB);
		    //System.out.println(getClass().getName()+" clientHostAddress="+remoteAddr);
		    //System.out.println(getClass().getName()+" clientHostName="+remoteHost);
		    String listOfServicesDue=listOfServices.getProbesForCollector(remoteHost);
		    //String listOfServicesDue=listOfServices.getProbesForCollector("rnagios01.usatlas.bnl.gov");
		    out.println(listOfServicesDue);
		    
		}else{
		    int dbid=Integer.parseInt(id);
		    PerfSonarService perfSonarService=new PerfSonarService(paramBag,this.dB,dbid);
		    perfSonarService.loadDataFromServicesTable();
		    String serviceJson = perfSonarService.executionDirectivesJson().toString();
		    out.println(serviceJson+"<BR>");
		}
	    }
	    if(driverAction.equals("lockService")){
		if(id!=null){
		    int dbid=Integer.parseInt(id);
		    PerfSonarService perfSonarService=new PerfSonarService(paramBag,this.dB,dbid);
		    boolean lockResult=perfSonarService.lockService();
		    if(lockResult){
			out.println("OK");
		    }else{
			out.println("ERROR, failed to lock service");
		    }
		}else{
		    out.println("ERROR, no service id");
		}
	    }
	    if(driverAction.equals("updateService")){
		if(id!=null){
		    int dbid=-1;
		    try{
			dbid=Integer.parseInt(id);
			String output=request.getParameter("output");
			String statusString=request.getParameter("status");
			int status=-1;
			try{
			    status=Integer.parseInt(statusString);
			}catch(Exception e){}

			if(output==null||status==-1){
			    System.out.println(getClass().getName()+" bad parameters");
			    System.out.println(getClass().getName()+" status="+statusString);
			    System.out.println(getClass().getName()+" output="+output);
			    out.println("ERROR");
			}else{
			    PerfSonarService perfSonarService=new PerfSonarService(paramBag,this.dB,dbid);
			    perfSonarService.load();
			    boolean updateresult = perfSonarService.update(status,output);
			    if( updateresult ){
				out.println("OK");
			    }else{
				out.println("ERROR");
			    }
			}
		    }catch(Exception e){
			System.out.println(getClass().getName()+" id is incomprehensible "+dbid);
			out.println("ERROR");
		    }
		}else{
		    out.println("ERROR");
		}		
		//out.println("driverAction="+driverAction+"<br>");
		//out.println("id="+id+"<br>");
	    }	    
	    if(driverAction.equals("test")){
		out.println("driverAction="+driverAction+"<br>");
		out.println("id="+id+"<br>");
	    }
	}else{
	    out.println("OK");
	}

    }
}



