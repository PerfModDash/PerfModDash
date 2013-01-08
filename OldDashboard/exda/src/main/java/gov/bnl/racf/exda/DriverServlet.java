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

//import java.io.File;

public class DriverServlet extends HttpServlet {

    private Properties dbParameters = null;
    private DbConnector dB=null;
    
    private String pictureDirectory=null; 
    private String dbConfigFileName="WEB-INF/classes/db.properties"; 

    private ListOfKnownUsers listOfKnownUsers=null;

    PrintWriter out=null;
    OutputStream outImage=null;


    private void openConnection(){
	// create database connector
	    // try{
	    // 	dbParameters=new Properties();
	    // 	dbParameters.load(new FileInputStream( dbConfigFileName ));
	    // }catch (IOException ex) {
	    // 	System.out.println(getClass().getName()+" Error: unable to open configuration file "+dbConfigFileName);
	    // 	System.out.println(getClass().getName()+" "+ex);
	    // 	ex.printStackTrace();
	    // }

	ParameterStore parameterStore = ParameterStore.getParameterStore();

	String hostname="";
	try {
	    InetAddress addr = InetAddress.getLocalHost();
	    
	    // Get IP Address
	    byte[] ipAddr = addr.getAddress();
	    
	    // Get hostname
	    hostname = addr.getHostName();
	    parameterStore.setHostname(hostname);

	    String hostAlias="talos";
	    if("grid21.racf.bnl.gov".equals(hostname)){
		hostAlias="perfsonar.usatlas.bnl.gov";
	    }
	    if("grid09.racf.bnl.gov".equals(hostname)){
		hostAlias="perfsonar.racf.bnl.gov";
	    }
	    parameterStore.setHostAlias(hostAlias);
	    

	} catch (UnknownHostException e) {
	    System.out.println(getClass().getName()+" failed to get hostname: hostname="+hostname);
	    System.out.println(getClass().getName()+" "+e);
	}
	
	System.out.println(getClass().getName()+" XXXXXXXXXXXXXXXX db config file="+dbConfigFileName);
	System.out.println(getClass().getName()+" hostname="+hostname);
	
	dB=new  DbConnector(hostname,dbConfigFileName);
	parameterStore.setDb(dB);
	
	// if (hostname.indexOf("talos") > -1){    
	// 	System.out.println(getClass().getName()+" connect to griddev03");
	// 	dB=new DbConnector(6, dbConfigFileName );
	// }else{
	// 	System.out.println(getClass().getName()+" connect to grid21");
	// 	dB=new DbConnector(5, dbConfigFileName );
	// }
    }

    public void init(ServletConfig config) throws ServletException {
            // Store the ServletConfig object and log the initialization
            super.init(config);

	    ParameterStore parameterStore = ParameterStore.getParameterStore();


	    pictureDirectory = getServletContext().getRealPath("/");
	    parameterStore.setBaseDirectory(pictureDirectory);
	    
	    dbConfigFileName=getServletContext().getRealPath("/")+dbConfigFileName;
	    
	    openConnection();

	    // get list of known users
	    getKnownUsers();

	    // set headless mode
	    System.setProperty("java.awt.headless", "true"); 
	    System.out.println( getClass().getName()+" headles mode set");

    }

    public void logUser(String user,HttpServletRequest request,PrintWriter out){

	//String addr = req.getRemoteAddr(); 
	String client_host = request.getRemoteHost(); // hostname.com

	String insertSqlQuery="INSERT INTO AccessHistory (access_time,dn,hostname,client_hostname) VALUES(NOW(),?,?,?)";
	String ip="unknown";
	try{
	    InetAddress addr = InetAddress.getLocalHost();
	    ip=addr.toString();
	}catch (Exception e){
	    System.out.println( getClass().getName()+" error occured while obtaining host info");
	    System.out.println(e);
	}
	PreparedStatement insertSql= null;
	try{
	    insertSql= dB.getConn().prepareStatement(insertSqlQuery);
	    insertSql.setString(1,user);
	    insertSql.setString(2,ip);
	    insertSql.setString(3,client_host);
	    insertSql.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when writing to database");
	    System.out.println( getClass().getName()+" "+insertSqlQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    insertSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println( getClass().getName()+" "+insertSqlQuery);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }
    public void getKnownUsers(){
	listOfKnownUsers=new ListOfKnownUsers(dB);
    }

    // public void getKnownUsersX(){
    // 	String getUsersQuery="SELECT dn from Users";
    // 	try{
    // 	ResultSet rs = dB.getConn().prepareStatement(getUsersQuery).executeQuery();
    // 	int count = 0;
    // 	while (rs.next ()){
    // 	    String dn= rs.getString("dn");
    // 	    listOfKnownUsers.add(dn);
    // 	}
    // 	}catch (Exception e){
    // 	    System.out.println(moduleName+" failed to execute quer: "+getUsersQuery);
    // 	    System.out.println(moduleName+" "+e);
    // 	    System.out.flush();
    // 	}
    // }

    // public boolean knownUser(String user){
    // 	if (listOfKnownUsers.contains(user)){
    // 	    return true;
    // 	}else{
    // 	    return false;
    // 	}
    // }

    public String getUserInfo(HttpServletRequest request){
	String result="unknown";
	String cipherSuite = (String) request.getAttribute("javax.servlet.request.cipher_suite");  
	System.out.println(new java.util.Date()+" "+getClass().getName()+" we are in getUserInfo");

	java.security.cert.X509Certificate[] certs=(java.security.cert.X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
	if (certs != null) {
	    System.out.println(new java.util.Date()+" "+getClass().getName()+" certs are not null");
	    for (int i = 0; i < certs.length; i++) {
		String dn=certs[i].getSubjectDN().toString();
		System.out.println(new java.util.Date()+" "+getClass().getName()+"DriverServlet cert line="+i+" "+dn);
		if (dn.indexOf("People")!=-1){
		    result=dn;
		    System.out.println(new java.util.Date()+" "+getClass().getName()+"Dn contains People");
		}
	    }
	} else {
	    // do nothing
		System.out.println(new java.util.Date()+" "+getClass().getName()+" certs is null");    
	}	
	// end of certificate stuff
	return result;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
	
	ParameterStore.getParameterStore().setOut(out);

	if(dB.getConnection()==null){
	    openConnection();
	}else{
	    if(dB.isClosed()){
		dB.close();
		openConnection();
	    }
	}

	HttpSession session = request.getSession();

	ParameterBag pB=new ParameterBag();

	GridUser user= new GridUser(dB,request,listOfKnownUsers);

	UserInfo userInfo=user.getUserInfo();

	GridUser oldUser=(GridUser)session.getAttribute("user");

	//if(!user.equals(oldUser)){
	//    session.setAttribute("user",user);
	//user.log();
	//}

	if(session.isNew()){
	   session.setAttribute("user",user); 
	   user.log();
	}


	String validUser="no";
	if(user.isKnown()){
	    session.setAttribute("knownUser",true);
	    validUser="yes";
	}else{
	     session.setAttribute("knownUser",false);
	     validUser="no";
	}
	pB.addParam("validUser",validUser);
	pB.setUser(userInfo);

	// String userDn = (String)session.getAttribute("UserDN");
	// //out.println("First user Dn="+userDn);

	// String user=getUserInfo(request);

	// if (!user.equals(userDn)){
	//     //out.println("log user!!!!");
	//     session.setAttribute("UserDN", user);
	//     logUser(user,request,out);
	// }
	// String validUser="no";
	// if (knownUser(user)){
	//     session.setAttribute("knownUser",true);
	//     validUser="yes";
	// }else{
	//     session.setAttribute("knownUser",false);
	//     validUser="no";
	// }
	// pB.addParam("validUser",validUser);


	pB.setRequestUri(request.getRequestURI());
	pB.setWorkDirectory(request.getRealPath("/"));

	pB.addParam("serverName",request.getServerName());
	pB.addParam("serverPort",Integer.toString(request.getServerPort()));
	pB.addParam("servletPath",request.getServletPath());

	pB.addParam("id",request.getParameter("id"));

	pB.addParam("page",request.getParameter("page"));
	pB.addParam("src",request.getParameter("src"));
	pB.addParam("dst",request.getParameter("dst"));
	pB.addParam("mon",request.getParameter("mon"));

	pB.addParam("hostName",request.getParameter("hostName"));
	pB.addParam("ip",request.getParameter("ip"));
	pB.addParam("serviceName",request.getParameter("serviceName"));
	pB.addParam("siteName",request.getParameter("siteName"));
	pB.addParam("cloudName",request.getParameter("cloudName"));
	pB.addParam("schedulerName",request.getParameter("schedulerName"));
	pB.addParam("interval",request.getParameter("interval"));
	pB.addParam("executeNow",request.getParameter("executeNow"));
	pB.addParam("probeId",request.getParameter("probeId"));

	pB.addParam("externalUrl",request.getParameter("externalUrl"));
	pB.addParam("serviceMatrixName",request.getParameter("serviceMatrixName"));
	pB.addParam("serviceMatrixType",request.getParameter("serviceMatrixType"));

	pB.addParam("toggleActive",request.getParameter("toggleActive"));
	pB.addParam("active",request.getParameter("Active"));

	pB.addParam("checkInterval",request.getParameter("checkInterval"));
	pB.addParam("probeCommand",request.getParameter("probeCommand"));

	pB.addParam("userName",request.getParameter("userName"));
	pB.addParam("userEmail",request.getParameter("userEmail"));
	pB.addParam("userRoles",request.getParameter("userRoles"));
	pB.addParam("userDN",request.getParameter("userDN"));

	pB.addParam("notificationInterval",request.getParameter("notificationInterval"));
	pB.addParam("checkInterval",request.getParameter("checkInterval"));
	pB.addParam("alarmDescription",request.getParameter("alarmDescription"));
	pB.addParam("alarmName",request.getParameter("alarmName"));
	pB.addParam("alarmUrl",request.getParameter("alarmUrl"));
	pB.addParam("alarmText",request.getParameter("alarmText"));
	pB.addParam("numberOfChannelsForWarning",request.getParameter("numberOfChannelsForWarning"));
	pB.addParam("numberOfChannelsForCritical",request.getParameter("numberOfChannelsForCritical"));
	pB.addParam("numberOfChannelsForUnknown",request.getParameter("numberOfChannelsForUnknown"));



	pB.addParam("consecutiveOK", request.getParameter("consecutiveOK"));
	pB.addParam("consecutiveNotOK", request.getParameter("consecutiveNotOK"));
	pB.addParam("consecutiveOKCut", request.getParameter("consecutiveOKCut"));
	pB.addParam("consecutiveNotOKCut", request.getParameter("consecutiveNotOKCut"));

	//pB.addParam("",request.getParameter(""));

	pB.addParam("message",request.getParameter("message"));

	String[] list2=request.getParameterValues("listOfIds");
	List<Integer>listOfIds=new ArrayList<Integer>();
	if(list2!=null){
	    for (int i = 0; i < list2.length; i++){
		listOfIds.add(Integer.parseInt( list2[i]));
	    }
	    pB.addListOfIds(listOfIds);
	}


	//out.println("<br>point 1 page="+pB.page+"<br>");

	// --- man page displays USATLAS cloud //
	if (pB.page.equals("")||ParameterBag.pageAddress("Main").equals(pB.page)){
	    PerfSonarCloud cloud=null;
	    String cloudName="USATLAS";
	    if(!cloudName.equals("")){
		cloud=new PerfSonarCloud(pB,dB,cloudName);
	    }else{
		cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
	    }
	    out.println(cloud.cloudOverviewPage());
	}

	if (pB.page.equals("1000")){
	    out.println("<H1>TEST PAGE</H1>");
	}

	if ( ParameterBag.pageAddress("List of Matrices").equals(pB.page)){
	    ListOfServiceMatrices list=new ListOfServiceMatrices(pB,dB);
	    out.println(list.htmlPage());
	}

	if ( ParameterBag.pageAddress("Display Service Matrix").equals(pB.page)){
	    ServiceMatrix sm=new ServiceMatrix(pB,dB,Integer.parseInt( pB.id));
	    out.println("<H2>"+sm.getServiceMatrixName()+"</H2>");
	    out.println(sm.htmlTable().toHtml());
	}
	if ( ParameterBag.pageAddress("Create Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		out.println(sm.createServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Choose Name for Throughput Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		out.println(sm.chooseNameForThroughputServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Choose Name for Traceroute Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		out.println(sm.chooseNameForTracerouteServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Choose Name for APD Throughput Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		out.println(sm.chooseNameForAPDThroughputServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Choose Name for APD Latency Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		out.println(sm.chooseNameForAPDLatencyServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Choose Name for Latency Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		out.println(sm.chooseNameForLatencyServiceMatrixPage());
		out.println("Done!!");
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Create Throughput Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		sm.setServiceMatrixName(pB.serviceMatrixName);
		//sm.setServiceMatrixType("throughput");
		//sm.setSchedulerName(pB.schedulerName);
		out.println(sm.createThroughputServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Create Traceroute Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		sm.setServiceMatrixName(pB.serviceMatrixName);
		out.println(sm.createTracerouteServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Create APD Throughput Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		sm.setServiceMatrixName(pB.serviceMatrixName);
		out.println(sm.createAPDThroughputServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Create APD Latency Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		sm.setServiceMatrixName(pB.serviceMatrixName);
		out.println(sm.createAPDLatencyServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Create Latency Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB);
		sm.setServiceMatrixName(pB.serviceMatrixName);
		//sm.setSchedulerName(pB.schedulerName);
		out.println(sm.createLatencyServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Save Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB,Integer.parseInt( pB.id));
		sm.unpackParameterBag();
		out.println(sm.saveServiceMatrixPage());
	    }else{		
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Edit Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB,Integer.parseInt( pB.id));
		out.println("<H2>"+sm.getServiceMatrixName()+"</H2>");
		out.println(sm.editServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Confirm Delete Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		out.println("<H2>Confirm Delete service matrix "+pB.serviceMatrixName+"</h2><br>");
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB,Integer.parseInt( pB.id));
		out.println(sm.confirmDeleteServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Delete Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		out.println("<H2>Delete service matrix "+pB.serviceMatrixName+"</h2><br>");
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB,Integer.parseInt( pB.id));
		out.println(sm.deleteServiceMatrixPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Select Hosts to add to Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB,Integer.parseInt( pB.id));
		out.println(sm.selectHostsToAddPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Add Hosts to Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB,Integer.parseInt( pB.id));
		if(!sm.isReadOnly()){
		    out.println(sm.addHostsPage());
		}else{
		    out.println("Matrix is Read Only!");
		}
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Remove Hosts from Service Matrix").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixRecord sm=new ServiceMatrixRecord(pB,dB,Integer.parseInt( pB.id));
		if(!sm.isReadOnly()){
		    out.println(sm.removeHostsPage());
		}else{
		    out.println("Matrix is Read Only!");
		}
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");	
	    }
	}
	if ( ParameterBag.pageAddress("Manipulate Sites").equals(pB.page)){
	    ListOfPerfSonarSites listOfPerfSonarSites=new ListOfPerfSonarSites(pB,dB);
	    out.println(listOfPerfSonarSites.manipulationHtmlPage());
	}
	if ( ParameterBag.pageAddress("Create Site").equals(pB.page)){
	    PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB);
	    out.println(perfSonarSite.createNewSitePage());
	}
	if ( ParameterBag.pageAddress("Confirm Delete Site").equals(pB.page)){
	    if(user.isKnown()){	    		
		PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB,Integer.parseInt( pB.id));
		out.println(perfSonarSite.confirmDeleteSitePage());
	    }else{
		out.println("<strong><br>You are not authorized to delete sites!</strong><br>");			
	    }
	}
	if ( ParameterBag.pageAddress("Delete Site").equals(pB.page)){
	    if(user.isKnown()){	    		
		PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB,Integer.parseInt( pB.id));
		out.println(perfSonarSite.deleteSitePage());
	    }else{
		out.println("<strong><br>You are not authorized to delete sites!</strong><br>");			
	    }
	}
	if ( ParameterBag.pageAddress("Insert New Site").equals(pB.page)){
	    PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB);
	    out.println(perfSonarSite.insertNewSitePage(pB.siteName));
	}
	if ( ParameterBag.pageAddress("Add Remove Hosts to Site").equals(pB.page)){
	    if(user.isKnown()){	    		
		PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB,Integer.parseInt( pB.id));
		out.println(perfSonarSite.addRemoveHostsPage());
	    }else{
		out.println("<strong><br>You are not authorized to delete sites!</strong><br>");			
	    }
	}
	if ( ParameterBag.pageAddress("Add Hosts to Site").equals(pB.page)){
	    if(user.isKnown()){	    		
		PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB,Integer.parseInt( pB.id));
		out.println(perfSonarSite.addHostsPage());
	    }else{
		out.println("<strong><br>You are not authorized to add hosts to site sites!</strong><br>");			
	    }
	}
	if ( ParameterBag.pageAddress("Remove Hosts from Site").equals(pB.page)){
	    if(user.isKnown()){	    		
		PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB,Integer.parseInt( pB.id));
		out.println(perfSonarSite.removeHostsPage());
	    }else{
		out.println("<strong><br>You are not authorized to remove hosts from site!</strong><br>");			
	    }
	}
	if ( ParameterBag.pageAddress("List of Hosts").equals(pB.page)){
	    ListOfPerfSonarHosts list=new ListOfPerfSonarHosts(pB,dB);
	    out.println(list.htmlPage());
	}
	if ( ParameterBag.pageAddress("Edit Host").equals(pB.page)){
	     if(user.isKnown()){
		 PerfSonarHost host=new PerfSonarHost(pB,dB,Integer.parseInt(pB.id));
		 out.println(host.editHostPage());
	     }else{
		 out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	     }
	}
	if ( ParameterBag.pageAddress("Save Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB,Integer.parseInt(pB.id));
		host.unpackParameterBag();
		out.println("unpacked param bag");
		out.println(host.saveHostPage());
		out.println(host.fullHtmlTable().toHtml());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Clone Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB,Integer.parseInt(pB.id));
		out.println(host.cloneHostPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Create Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB);
		out.println(host.createNewHostPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Insert Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB);
		out.println(host.insertNewHostPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}

	if ( ParameterBag.pageAddress("Confirm Delete Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB,Integer.parseInt(pB.id));
		out.println(host.confirmDeleteHostPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Delete Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB,Integer.parseInt(pB.id));
		out.println(host.deleteHostPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Add Remove Primitive Services to Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB,Integer.parseInt(pB.id));
		out.println(host.getHostName()+"<br>");
		out.println(host.addRemovePrimitiveServicesToHostPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Add Primitives to Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB,Integer.parseInt(pB.id));
		out.println(host.getHostName()+"<br>");
		out.println(host.addPrimitivesPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Remove Primitives from Host").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost host=new PerfSonarHost(pB,dB,Integer.parseInt(pB.id));
		out.println(host.getHostName()+"<br>");
		out.println(host.removePrimitivesPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }
	}


	//========================= Matrix stuff ====================================
	if ( ParameterBag.pageAddress("Matrix Element").equals(pB.page)){
	    ServiceMatrixElement tn=new ServiceMatrixElement(pB,dB,Integer.parseInt(pB.id));
	    out.println("<H2>Matrix Element Status Detail</H2>");
	    out.println(tn.fullHtmlTable().toHtml());
	}
	if ( ParameterBag.pageAddress("APD Matrix Element").equals(pB.page)){
	    ServiceMatrixElementAPD tn=new ServiceMatrixElementAPD(pB,dB,Integer.parseInt(pB.id));
	    out.println("<H2>APD Matrix Element Status Detail</H2>");
	    out.println(tn.fullHtmlTable().toHtml());
	}
	if ( ParameterBag.pageAddress("Force Test Matrix Element").equals(pB.page)){
	    if(user.isKnown()){
		ServiceMatrixElement tn=new ServiceMatrixElement(pB,dB,Integer.parseInt(pB.id));
		out.println(tn.forceTestMatrixElementPage());
	    }else{
		out.println("<strong><br>You are not authorized to perform this operation</strong><br>");
	    }	
	}
	if ( ParameterBag.pageAddress("Matrix Element History Table").equals(pB.page)){
	     ServiceMatrixElement sme=new ServiceMatrixElement(pB,dB,Integer.parseInt(pB.id));
	     out.println(sme.getHistoryTablePage());
	}
	if ( ParameterBag.pageAddress("Matrix Element History Plot").equals(pB.page)){
	     ServiceMatrixElement sme=new ServiceMatrixElement(pB,dB,Integer.parseInt(pB.id));
	     out.println(sme.getHistoryPlotPage(pictureDirectory));
	}

	if ( ParameterBag.pageAddress("Confirm Active Update").equals(pB.page)){
	     if(user.isKnown()){
		 ServiceMatrixElement sme=new ServiceMatrixElement(pB,dB,pB.probeId);
		 out.println(sme.confirmUpdateActivePage());
	     }
	}

	if ( ParameterBag.pageAddress("Toggle Active").equals(pB.page)){
	     if(user.isKnown()){
		 PrimitiveService sme=new PrimitiveService(pB,dB,pB.probeId);
		 out.println(sme.updateActivePage());
	     }else{
		 out.println("You are not authorized to perform this operation");
	     }
	}

	if ( ParameterBag.pageAddress("Toggle Active ServiceMatrixElement").equals(pB.page)){
	     if(user.isKnown()){
		 ServiceMatrixElement sme=new ServiceMatrixElement(pB,dB,pB.probeId);
		 out.println(sme.activeUpdatedPage());
	     }else{
		 out.println("You are not authorized to perform this operation");
	     }
	}

	if ( ParameterBag.pageAddress("Edit Matrix Element").equals(pB.page)){
	     if(user.isKnown()){
		 ServiceMatrixElement sme=new ServiceMatrixElement(pB,dB,Integer.parseInt(pB.id));
		 out.println(sme.editServicePage());
	     }else{
		 out.println("You are not authorized to perform this operation");
	     }
	}

	if ( ParameterBag.pageAddress("Save Matrix Element").equals(pB.page)){
	     if(user.isKnown()){
		 ServiceMatrixElement sme=new ServiceMatrixElement(pB,dB,Integer.parseInt(pB.id));
		 out.println(sme.saveServicePage());
	     }else{
		 out.println("You are not authorized to perform this operation");
	     }
	}
	if ( ParameterBag.pageAddress("Select Scheduler for Service Matrix").equals(pB.page)){
	     if(user.isKnown()){
		 ServiceMatrixRecord sme=new ServiceMatrixRecord(pB,dB,Integer.parseInt(pB.id));
		 out.println(sme.selectSchedulerPage());
	     }else{
		 out.println("You are not authorized to perform this operation");
	     }
	}
	if ( ParameterBag.pageAddress("Update Scheduler for Service Matrix").equals(pB.page)){
	     if(user.isKnown()){
		 ServiceMatrixRecord sme=new ServiceMatrixRecord(pB,dB,Integer.parseInt(pB.id));
		 out.println(sme.updateSchedulerPage());
	     }else{
		 out.println("You are not authorized to perform this operation");
	     }
	}
	//======================================================================//


	// if (ParameterBag.pageAddress("Main").equals(pB.page)){
	//     String cloudName="USATLAS";
	//     PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,cloudName);
	//     out.println(cloud.cloudOverviewPage());
	// }
	if (ParameterBag.pageAddress("All Clouds").equals(pB.page)){
	    out.println("<H2>Status of All PerfSonar Clouds</h2>");

	    ListOfClouds listOfClouds=new ListOfClouds(pB,dB);
	    out.println(listOfClouds.htmlPage());
		    
	}

	// === Cloud stuff ====
	if (ParameterBag.pageAddress("Manipulate Clouds").equals(pB.page)){
	    out.println("<H2>List of All PerfSonar Clouds</h2>");

	    ListOfClouds listOfClouds=new ListOfClouds(pB,dB);
	    out.println(listOfClouds.manipulationHtmlPage());

	}
	if (ParameterBag.pageAddress("Create Cloud").equals(pB.page)){
	    out.println("<H2>Create New PerfSonar Cloud</h2>");
	    if(user.isKnown()){
		ListOfClouds listOfClouds=new ListOfClouds(pB,dB);
		out.println(listOfClouds.createNewCloudPage());
	    }else{
		 out.println("You are not authorized to perform this operation");
	     }
	}
	if (ParameterBag.pageAddress("Insert New Cloud").equals(pB.page)){
	    out.println("<H2>Save New PerfSonar Cloud</h2>");
	    if(user.isKnown()){
		ListOfClouds listOfClouds=new ListOfClouds(pB,dB);
		out.println(listOfClouds.insertNewCloudPage(pB.cloudName));
	    }else{
		 out.println("You are not authorized to perform this operation");
	     }
	}
	if (ParameterBag.pageAddress("Confirm Delete Cloud").equals(pB.page)){
	    PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
	    out.println(cloud.confirmCloudDeletePage());
	}
	if (ParameterBag.pageAddress("Delete Cloud").equals(pB.page)){
	    if(user.isKnown()){
		out.println("<br>");
		PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
		if(!cloud.getReadOnly()){
		    out.println(cloud.deleteCloudPage());
		}else{
		    out.println("<strong>Cloud "+cloud.getCloudName()+"is read only, You are not authorized to perform this operation</strong><br>");
		}
	    }else{
		 out.println("<strong>You are not authorized to perform this operation</strong>");
	     }
	}
	if (ParameterBag.pageAddress("Add remove Sites to Cloud Page").equals(pB.page)){
	    PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
	    out.println(cloud.addRemoveSitesToCloudPage());
	}

	if (ParameterBag.pageAddress("Add Sites To Cloud Page").equals(pB.page)){
	    if(user.isKnown()){
		out.println("<br>");
		PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
		if(!cloud.getReadOnly()){
		    out.println(cloud.addSitesToCloudPage());
		}else{
		    out.println("<strong>Cloud "+cloud.getCloudName()+"is read only, You are not authorized to perform this operation</strong><br>");
		}
	    }else{
		 out.println("<strong>You are not authorized to perform this operation</strong>");
	     }	    
	}
	if (ParameterBag.pageAddress("Remove Sites From Cloud Page").equals(pB.page)){
	    if(user.isKnown()){
		out.println("<br>");
		PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
		if(!cloud.getReadOnly()){
		    out.println(cloud.removeSitesFromCloudPage());
		}else{
		    out.println("<strong>Cloud "+cloud.getCloudName()+"is read only, You are not authorized to perform this operation</strong><br>");
		}
	    }else{
		 out.println("<strong>You are not authorized to perform this operation</strong>");
	     }	    
	}
	if (ParameterBag.pageAddress("Add Remove Matrices to Cloud Page").equals(pB.page)){
	    PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
	    out.println(cloud.addRemoveMatricesToCloudPage());
	}
	if (ParameterBag.pageAddress("Add Matrices To Cloud Page").equals(pB.page)){
	    if(user.isKnown()){
		out.println("<br>");
		PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
		if(!cloud.getReadOnly()){
		    out.println(cloud.addMatricesToCloudPage());
		}else{
		    out.println("<strong>Cloud "+cloud.getCloudName()+"is read only.</strong><br>");
		    out.println("<strong>You are not authorized to perform this operation.</strong><br>");
		}
	    }else{
		 out.println("<br><strong>You are not authorized to perform this operation</strong>");
	     }		    
	}
	if (ParameterBag.pageAddress("Remove Matrices From Cloud Page").equals(pB.page)){
	    if(user.isKnown()){
		out.println("<br>");
		PerfSonarCloud cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
		if(!cloud.getReadOnly()){
		    out.println(cloud.removeMatricesFromCloudPage());
		}else{
		    out.println("<strong>Cloud "+cloud.getCloudName()+"is read only.</strong><br>");
		    out.println("<strong>You are not authorized to perform this operation.</strong><br>");
		}
	    }else{
		 out.println("<br><strong>You are not authorized to perform this operation</strong>");
	     }		    
	}


	if (ParameterBag.pageAddress("Cloud Overview").equals(pB.page)){
	    PerfSonarCloud cloud=null;
	    String cloudName=pB.cloudName;
	    if(!cloudName.equals("")){
		cloud=new PerfSonarCloud(pB,dB,cloudName);
	    }else{
		cloud=new PerfSonarCloud(pB,dB,Integer.parseInt(pB.id));
	    }
	    out.println(cloud.cloudOverviewPage());
	}

	// === end of cloud stuff === //
	if ( ParameterBag.pageAddress("Throughput Node History Plot").equals(pB.page)){
	    ThroughputNode tn=new ThroughputNode(pB,dB,pB.src,pB.dst,pB.mon);
	    out.println("<H2>Throughtput History Plot</H2>");
	    out.println(tn.makeHistoryPlotPage(pictureDirectory));
	}
	if ( ParameterBag.pageAddress("Latency Node History Plot").equals(pB.page)){
	    LatencyNode tn=new LatencyNode(pB,dB,pB.src,pB.dst,pB.mon);
	    out.println("<H2>Latency History Plot</H2>");
	    out.println(tn.makeHistoryPlotPage(pictureDirectory));
	}
	if ( ParameterBag.pageAddress("Throughput Node History Table").equals(pB.page)){
	    ThroughputNode tn=new ThroughputNode(pB,dB,pB.src,pB.dst,pB.mon);
	    out.println("<H2>Throughtput History</H2>");
	    out.println(tn.getHistoryTablePage());
	}
	if ( ParameterBag.pageAddress("Latency Node History Table").equals(pB.page)){
	    LatencyNode tn=new LatencyNode(pB,dB,pB.src,pB.dst,pB.mon);
	    out.println("<H2>Latency History</H2>");
	    out.println(tn.getHistoryTablePage());
	}
	if ( ParameterBag.pageAddress("Throughput Node").equals(pB.page)){
	    ThroughputNode tn=new ThroughputNode(pB,dB,pB.src,pB.dst,pB.mon);
	    out.println("<H2>Throughtput Node Status Detail</H2>");
	    out.println(tn.fullHtmlTable().toHtml());
	}
	if ( ParameterBag.pageAddress("Latency Node").equals(pB.page)){
	    LatencyNode tn=new LatencyNode(pB,dB,pB.src,pB.dst,pB.mon);
	    out.println("<H2>Latency Node Status Detail</H2>");
	    out.println(tn.fullHtmlTable().toHtml());
	}
	if ( ParameterBag.pageAddress("Throughput Matrix").equals(pB.page)){
	    ThroughputMatrix tm= new ThroughputMatrix(pB,dB);
	    out.println("<H2>Throughtput Matrix</H2>");
	    out.println(tm.htmlTable().toHtml());
	    out.println("<br>");
	    out.println(tm.getExplanation());
	}
	if ( ParameterBag.pageAddress("Latency Matrix").equals(pB.page)){
	    LatencyMatrix tm= new LatencyMatrix(pB,dB);
	    out.println("<H2>Latency Matrix</H2>");
	    out.println(tm.htmlTable().toHtml());
	    out.println("<br>");
	    out.println(tm.getExplanation());
	}

	if ( ParameterBag.pageAddress("perfSonar Primitive").equals(pB.page)){
	    //PrimitiveService tm= new PrimitiveService(pB,dB,pB.hostName,pB.serviceName);
	    //out.println("<H2>Primitive Service: "+pB.hostName+"/"+pB.serviceName+"</H2>");

	    PrimitiveService tm= new PrimitiveService(pB,dB, Integer.parseInt(pB.id));
	    out.println("<H2>Primitive Service: "+tm.getHostName()+"/"+tm.getServiceName()+"</H2>");

	    out.println(tm.fullHtmlTable().toHtml());
	}

	if ( ParameterBag.pageAddress("Primitive Node History Table").equals(pB.page)){
	    //PrimitiveService tn=new PrimitiveService(pB,dB,pB.hostName,pB.serviceName);
	    //out.println("<H2>Primitive Service: "+pB.hostName+"/"+pB.serviceName+"</H2>");

	    PrimitiveService primitiveService= new PrimitiveService(pB,dB, Integer.parseInt(pB.id));
	    out.println("<H2>Primitive Service: "+primitiveService.getHostName()+"/"+primitiveService.getServiceName()+"</H2>");
	    out.println(primitiveService.getHistoryTablePage());
	}
	if ( ParameterBag.pageAddress("List of Sites").equals(pB.page)){

	    out.println("<H2>perfSonar Sites</H2>");

	    ListOfPerfSonarSites listOfPerfSonarSites = new ListOfPerfSonarSites(pB,dB);
	    out.println(listOfPerfSonarSites.toHtml());
	    out.println("<br>");
	}
	if ( ParameterBag.pageAddress("Site Services").equals(pB.page)){
	    PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB, Integer.parseInt(pB.id)  );
	    out.println(perfSonarSite.servicesGroupedByHost());
	}
	if ( ParameterBag.pageAddress("Display Site").equals(pB.page)){
	    PerfSonarSite perfSonarSite=new PerfSonarSite(pB,dB, Integer.parseInt(pB.id)  );
	    out.println(perfSonarSite.servicesGroupedByHost());
	}
	if ( ParameterBag.pageAddress("List of perfSonar Primitive Services").equals(pB.page)){

	    out.println("<H2>perfSONAR Primitive Services</H2>");

	    List<String> listOfServices=new ArrayList<String>();
	    listOfServices.add("perfSONAR_pSB");       
	    listOfServices.add("CheckLookupService");     
	    listOfServices.add("NDT_port_7123");           
	    listOfServices.add("bwctl_port_4823");         
	    listOfServices.add("owamp_port_861");          
	    listOfServices.add("bwctl_port_8570");         
	    listOfServices.add("NDT_port_3001");           
	    listOfServices.add("NPAD_port_8001");          
	    listOfServices.add("owamp_port_8569");         
	    listOfServices.add("NPAD_port_8000");        

	    Iterator it = listOfServices.iterator();
	    while(it.hasNext()){
		String serviceDescription=(String)it.next();
		ListOfPrimitiveServices listOfPrimitiveServices= new ListOfPrimitiveServices(pB,dB);
		listOfPrimitiveServices.addByServiceDescription(serviceDescription);
		out.println(listOfPrimitiveServices.toHtml());
	    }
	}
	if ( ParameterBag.pageAddress("Force Test Primitive Service").equals(pB.page)){
	    if(user.isKnown()){
		PrimitiveService tm= new PrimitiveService(pB,dB,Integer.parseInt(pB.id));
		out.println("<H2>Primitive Service: "+tm.getHostName()+"/"+tm.getServiceName()+"</H2>");
		out.println(tm.forceTestNowPage());
	    }else{
		out.println("<br><strong>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Edit Primitive Service").equals(pB.page)){
	    if(user.isKnown()){
		PrimitiveService tm= new PrimitiveService(pB,dB,pB.probeId);
		out.println("<H2>Primitive Service: "+tm.getHostName()+"/"+tm.getServiceName()+"</H2>");
		out.println(tm.editServicePage());
	    }else{
		out.println("<br><strong>You are not authorized to perform this operation</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Edit Latency Node").equals(pB.page)){
	    if(user.isKnown()){
		LatencyNode tm= new LatencyNode(pB,dB,pB.probeId);
		out.println("<H2>Latency Node: source="+tm.getSource()+", destination"+tm.getDestination()+", monitor="+tm.getMonitor()+"</H2>");
		out.println(tm.serviceEditTable().toHtml());
	    }
	}
	if ( ParameterBag.pageAddress("Edit Throughput Node").equals(pB.page)){
	    if(user.isKnown()){
		ThroughputNode tm= new ThroughputNode(pB,dB,pB.probeId);
		out.println("<H2>Throughput Node: source="+tm.getSource()+", destination"+tm.getDestination()+", monitor="+tm.getMonitor()+"</H2>");
		out.println(tm.serviceEditTable().toHtml());
	    }
	}
	if ( ParameterBag.pageAddress("Save Primitive Service").equals(pB.page)){
	    if(user.isKnown()){
		PrimitiveService tm= new PrimitiveService(pB,dB,Integer.parseInt(pB.id));
		out.println("<H2>Primitive Service: "+tm.getHostName()+"/"+tm.getServiceName()+"</H2>");
		out.println(tm.saveServicePage());
	    }
	}
	if ( ParameterBag.pageAddress("Save Latency Node").equals(pB.page)){
	    if(user.isKnown()){
		LatencyNode tm= new LatencyNode(pB,dB,pB.probeId);
		out.println("<H2>Latency Node: source="+tm.getSource()+", destination"+tm.getDestination()+", monitor="+tm.getMonitor()+"</H2>");
		out.println(tm.saveServicePage());
	    }
	}
	if ( ParameterBag.pageAddress("Save Throughput Node").equals(pB.page)){
	    if(user.isKnown()){
		ThroughputNode tm= new ThroughputNode(pB,dB,pB.probeId);
		out.println("<H2>Throughput Node: source="+tm.getSource()+", destination"+tm.getDestination()+", monitor="+tm.getMonitor()+"</H2>");
		out.println(tm.saveServicePage());
	    }
	}
	if ( ParameterBag.pageAddress("PerfSonar Host").equals(pB.page)){
	    PerfSonarHost pf=new PerfSonarHost(pB,dB,pB.hostName);
	    out.println(pf.fullHtmlTable().toHtml());
	}
	if ( ParameterBag.pageAddress("PerfSonar Host Active Form").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost ph=new PerfSonarHost(pB,dB,pB.hostName);
		out.println(ph.swapActiveForm());
	    }
	}
	if ( ParameterBag.pageAddress("PerfSonar Host Active Update").equals(pB.page)){
	    if(user.isKnown()){
		PerfSonarHost ph=new PerfSonarHost(pB,dB,pB.hostName);
		out.println(ph.updateActivePage());
	    }
	}
	if ( ParameterBag.pageAddress("Primitive Service Confirm Active Update").equals(pB.page)){
	    if(user.isKnown()){
		PrimitiveService ps= new PrimitiveService(pB,dB,pB.probeId);
		out.println(ps.confirmUpdateActivePage());
	    }
	}
	if ( ParameterBag.pageAddress("Update Active For Primitive Service").equals(pB.page)){
	    if(user.isKnown()){
		PrimitiveService ps= new PrimitiveService(pB,dB,pB.probeId);
		out.println(ps.updateActivePage());
	    }
	}
	//=== Schedulers =====//
	if ( ParameterBag.pageAddress("Manipulate Schedulers").equals(pB.page)){
	    if(user.isKnown()){
		ListOfSchedulers listOfSchedulers= new ListOfSchedulers(pB,dB);
		out.println(listOfSchedulers.manipulationHtmlPage());
	    }
	}
	if ( ParameterBag.pageAddress("Create Scheduler").equals(pB.page)){
	    if(user.isKnown()){
		Scheduler scheduler= new Scheduler(pB,dB);
		out.println(scheduler.createSchedulerPage());
	    }
	}	
	if ( ParameterBag.pageAddress("Insert New Scheduler").equals(pB.page)){
	    if(user.isKnown()){
		Scheduler scheduler= new Scheduler(pB,dB);
		scheduler.unpackParameterBag();
		out.println(scheduler.insertNewSchedulerPage());
	    }
	}
	if ( ParameterBag.pageAddress("Edit Scheduler").equals(pB.page)){
	    if(user.isKnown()){
		Scheduler scheduler= new Scheduler(pB,dB, Integer.parseInt(pB.id) );
		out.println(scheduler.editSchedulerPage());
	    }
	}
	if ( ParameterBag.pageAddress("Save Scheduler").equals(pB.page)){
	    if(user.isKnown()){
		Scheduler scheduler= new Scheduler(pB,dB, Integer.parseInt(pB.id) );
		scheduler.unpackParameterBag();
		out.println(scheduler.saveSchedulerPage());
	    }
	}
	if ( ParameterBag.pageAddress("Confirm Delete Scheduler").equals(pB.page)){
	    if(user.isKnown()){
		Scheduler scheduler= new Scheduler(pB,dB, Integer.parseInt(pB.id) );
		out.println(scheduler.confirmDeleteSchedulerPage());
	    }
	}
	if ( ParameterBag.pageAddress("Delete Scheduler").equals(pB.page)){
	    if(user.isKnown()){
		Scheduler scheduler= new Scheduler(pB,dB, Integer.parseInt(pB.id) );
		out.println(scheduler.deleteSchedulerPage());
	    }
	}
	if ( ParameterBag.pageAddress("Admin Page").equals(pB.page)){
	    //if(user.isAdmin()){
	    if(user.isKnown()){
		int id=-1;
		if(pB.id==null ){
		    id=0;
		}else{
		    if (pB.id.equals("")){
			id=0;
		    }else{
			id=Integer.parseInt(pB.id);
		    }
		}
		AdminPage adminPage= new AdminPage(pB,dB, id );
		out.println(adminPage.toHtml());
	    }else{
		out.println("<br><br><strong>You are not authorized to access this page</strong><br><br>");
	    }
	}	
	if ( ParameterBag.pageAddress("List Registered Users").equals(pB.page)){
	    if(user.isKnown()){
		ListOfUsers listOfUsers= new ListOfUsers(pB,dB );
		out.println(listOfUsers.manipulationHtmlPage());
	    }
	}
	if ( ParameterBag.pageAddress("Create User").equals(pB.page)){
	    if(user.isAdmin()){
		User newUser=new User(pB,dB);
		out.println(newUser.createUserPage());
	    }else{
		out.println("<BR><strong>You are not authorized to create new users</strong><br>");
	    }
	    
	}
	if ( ParameterBag.pageAddress("Edit User").equals(pB.page)){
	    if(user.isAdmin()){
		User userToBeEdited=new User(pB,dB,Integer.parseInt(pB.id));
		out.println(userToBeEdited.editUserPage());
	    }else{
		out.println("<BR><strong>You are not authorized to edit users</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Save User").equals(pB.page)){
	    if(user.isAdmin()){
		User userToBeSaved=new User(pB,dB);
		userToBeSaved.unpackParameterBag();
		out.println(userToBeSaved.saveUserPage());
	    }else{
		out.println("<BR><strong>You are not authorized to perform this action</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Delete User").equals(pB.page)){
	    if(user.isAdmin()){
		User userToBeDeleted=new User(pB,dB,Integer.parseInt(pB.id));
		out.println(userToBeDeleted.deleteUserPage());
	    }else{
		out.println("<BR><strong>You are not authorized to delete users</strong><br>");
	    }
	}
	if ( ParameterBag.pageAddress("Select DN").equals(pB.page)){
	    if(user.isKnown()){
		ListOfUsers listOfUsers= new ListOfUsers(pB,dB );
		out.println(listOfUsers.displayKnownDNPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Add DN").equals(pB.page)){
	    if(user.isKnown()){
		User newUser=new User(pB,dB);
		newUser.setUserDn(pB.userDN);
		out.println(newUser.createUserPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("List of Alarms").equals(pB.page)){
	    ListOfAlarms listOfAlarms=new ListOfAlarms(pB,dB);
	    out.println(listOfAlarms.manipulationHtmlPage());
	}
	if ( ParameterBag.pageAddress("Overview of Alarms").equals(pB.page)){
	    ListOfAlarms listOfAlarms=new ListOfAlarms(pB,dB);
	    out.println(listOfAlarms.alarmsOverviewPage());
	}
	if ( ParameterBag.pageAddress("Create Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB);
		out.println(alarm.createAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Insert Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB);
		alarm.unpackParameterBag();
		out.println(alarm.saveAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Save Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB);
		alarm.unpackParameterBag();
		out.println(alarm.saveAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Alarm History Page").equals(pB.page)){
	    Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
	    out.println(alarm.getHistoryTablePage());
	}
	if ( ParameterBag.pageAddress("Alarm History Details Page").equals(pB.page)){
	    AlarmHistoryRecord alarmHistoryRecord=new AlarmHistoryRecord(pB,dB,Integer.parseInt(pB.id));
	    out.println(alarmHistoryRecord.recordDetails());
	}
	if ( ParameterBag.pageAddress("Alarm History Message Details Page").equals(pB.page)){
	    if(user.isKnown()){
		AlarmHistoryRecord alarmHistoryRecord=new AlarmHistoryRecord(pB,dB,Integer.parseInt(pB.id));
		out.println(alarmHistoryRecord.messageDetails());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}	
	if ( ParameterBag.pageAddress("Edit Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.editAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Confirm Delete Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.confirmDeleteAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}	
	if ( ParameterBag.pageAddress("Delete Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.deleteAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Add Remove Site to Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.addOrRemoveSitesToAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Add Sites to Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.addSitesToAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Remove Sites from Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.removeSitesFromAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Site Alarms").equals(pB.page)){
	    PerfSonarSite site=new PerfSonarSite(pB,dB,Integer.parseInt(pB.id));
	    out.println(site.alarmsForSitePage());
	}
	if ( ParameterBag.pageAddress("Select Site to Add Alarms").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.selectSiteToAddAlarmsPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}

	if ( ParameterBag.pageAddress("Select Services From Site to Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.selectPrimitiveServicesFromSitePage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Add Services From Site to Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.addPrimitiveServicesFromSitePage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Display Alarm Overview").equals(pB.page)){
	    Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
	    out.println(alarm.displayAlarmOverviewPage());
	}
	if ( ParameterBag.pageAddress("Remove Primitive Services From Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.removePrimitiveServicesFromSitePage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Add Or Remove Users To Alarm").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.addOrRemoveUsersPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Add Users To Alarm Page").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.addUsersToAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
	if ( ParameterBag.pageAddress("Remove Users From Alarm Page").equals(pB.page)){
	    if(user.isKnown()){
		Alarm alarm=new Alarm(pB,dB,Integer.parseInt(pB.id));
		out.println(alarm.removeUsersFromAlarmPage());
	    }else{
		out.println("<BR><STRONG>You are not authorized to access this page</STRONG><BR>");
	    }
	}
    }
}



