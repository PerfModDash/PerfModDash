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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
  
//import java.io.File;

public class ReadDataDriver extends HttpServlet {

    private Properties dbParameters = null;
    private DbConnector dB=null;

    ParameterBag paramBag=new ParameterBag();
    
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


	//ParameterBag paramBag=new ParameterBag();

	String driverAction=request.getParameter("action");
	String matrixId=request.getParameter("matrixId");
	if(matrixId==null){
	    matrixId="";
	}

	
	String matrixName=request.getParameter("matrixName");
	String source=request.getParameter("source");
	String destination=request.getParameter("destination");

	boolean knownAction=false;
	if("matrixServices".equals(driverAction)){
	    knownAction=true;
	    if(matrixId!=""){
		ServiceMatrix sm=new ServiceMatrix(paramBag,dB,Integer.parseInt(matrixId));
		ArrayList<JSONObject> listOfJsonObjects = sm.toListOfJsonObjects();
		Iterator iter = listOfJsonObjects.iterator();
		while(iter.hasNext()){
		    JSONObject json = (JSONObject)iter.next();
		    out.println(json.toString()+"<BR>");
		}
	    }
	}
	if("getServices".equals(driverAction)){
	    knownAction=true;
	    JSONArray listOfServicesAsJson=new JSONArray();
	    ArrayList listOfServices=getListOfServices(source,destination);
	    Iterator iter = listOfServices.iterator();
	    while(iter.hasNext()){
		ServiceMatrixElement element = (ServiceMatrixElement)iter.next();
		JSONObject json = element.toJson();
		listOfServicesAsJson.add(json);
	    }
	    out.println(listOfServicesAsJson.toString());
	}
	if(!knownAction){
	    out.println("unknown action: "+driverAction);
	}

    }
    public ArrayList<ServiceMatrixElement> getListOfServices(String sourceHostName, String destinationHostName){
	ArrayList<Integer> listOfIds = getListOfServiceIds(sourceHostName, destinationHostName);
	ArrayList<ServiceMatrixElement> services = getListOfServices(listOfIds);
	return services;

    }
    public ArrayList<Integer> getListOfServiceIds(String sourceHostName, String destinationHostName){
	ArrayList<Integer> result=new ArrayList();
	// select id's of services
	String query="SELECT dbid FROM MetricRecordSummary WHERE HostName2=? AND HostName3=?";
	PreparedStatement  queryPS=null;
	try{
	    queryPS=dB.getConn().prepareStatement(query);
	    queryPS.setString(1,sourceHostName);
	    queryPS.setString(2,destinationHostName);
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		int dbid=rs.getInt("dbid");
		Integer dbidInteger = new Integer(dbid);
		result.add(dbidInteger);
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to execute query");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	
	return result;
    }
    public ArrayList<ServiceMatrixElement> getListOfServices(ArrayList<Integer> listOfIds){
	ArrayList<ServiceMatrixElement> result = new ArrayList<ServiceMatrixElement>();
	Iterator iter = listOfIds.iterator();
	while(iter.hasNext()){
	    Integer dbidInteger = (Integer)iter.next();
	    int dbid = dbidInteger.intValue();
	    ServiceMatrixElement element = new ServiceMatrixElement(paramBag, dB,dbid);
	    result.add(element);
	}
	return result;
    }

}



