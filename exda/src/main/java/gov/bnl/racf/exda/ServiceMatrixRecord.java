package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ServiceMatrixRecord
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private String serviceMatrixName=null;
    private String serviceMatrixType=null;
    private String schedulerName="";
    private int id=-1;
    private boolean readOnly=true;

    private List<PerfSonarHost>hostsInThisMatrix=new ArrayList<PerfSonarHost>();
    private List<PerfSonarHost>hostsNotInThisMatrix=new ArrayList<PerfSonarHost>();

    private ActivityLogger activityLogger=null;

    public ServiceMatrixRecord(ParameterBag paramBag,   DbConnector inputDb, int id)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    try{
		this.activityLogger=new ActivityLogger(this.parameterBag,this.db);
	    }catch(Exception e){
		System.out.println(getClass().getName()+" ERROR failed to create activity logger, this may cause problems in future");
	    }
	    this.id=id;
	    this.load();
	}
    public ServiceMatrixRecord(ParameterBag paramBag,   DbConnector inputDb){
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    try{
		this.activityLogger=new ActivityLogger(this.parameterBag,this.db);
	     }catch(Exception e){
		System.out.println(getClass().getName()+" ERROR failed to create activity logger, this may cause problems in future");
	    }
	    this.id=-1;
	    this.serviceMatrixName="new service matrix";
	    this.serviceMatrixType="unknown service type";
	    this.schedulerName="";
    }
    public void unpackParameterBag(){
	this.serviceMatrixName=this.parameterBag.serviceMatrixName;
	this.serviceMatrixType=this.parameterBag.serviceMatrixType;
	this.schedulerName=this.parameterBag.schedulerName;
    }
    private void unpackResultSet(ResultSet rs){
	try{
	  while (rs.next ()){
	      this.id=rs.getInt("id");
	      this.serviceMatrixName=rs.getString("ServiceMatrixName");
	      this.serviceMatrixType=rs.getString("ServiceMatrixType");
	      this.schedulerName=rs.getString("SchedulerName");
	      this.readOnly=rs.getBoolean("ReadOnly");
	  }
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to unpack ServiceMatrixRecord");
	    System.out.println(getClass().getName()+" id="+this.id);
	    System.out.println(getClass().getName()+" "+e);
	}  
    }
    public void load(){
	String query="SELECT * FROM ServiceMatrices WHERE id=?";
	PreparedStatement getById=null;
	try{
	    getById	= db.getConn().prepareStatement(query);
	    getById.setInt(1,this.id);
	    ResultSet rs=getById.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" id="+this.id);		
	    System.out.println(getClass().getName()+" "+e);
	}	
	try{
	    getById.close();
	}catch (Exception e){  
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}

    }
    public int delete(){
	// delete this matrix
	// return 0 if ok, 1 if cannot be deleted
	if(this.readOnly){
	    return 1;
	}else{
	    activityLogger.log("delete matrix id="+this.id);
	    int result=0;
	    String query="DELETE FROM ServiceMatrices  WHERE id=?";
	    PreparedStatement queryPS=null;
	    try{
		queryPS	= db.getConn().prepareStatement(query);
		queryPS.setInt(1,this.id);
		queryPS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting service matrix from database");
		System.out.println(getClass().getName()+" "+query);		
		System.out.println(getClass().getName()+" id="+this.id);
		System.out.println(getClass().getName()+" "+e);
		result=1;
	    }
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }

	    // delete from matrix-host dependency table
	    String query2="DELETE FROM ServiceMatricesHosts  WHERE matrixId=?";
	    PreparedStatement query2PS=null;
	    try{
		query2PS	= db.getConn().prepareStatement(query2);
		query2PS.setInt(1,this.id);
		query2PS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting service matrix from ServiceMatricesHosts table");
		System.out.println(getClass().getName()+" "+query2);		
		System.out.println(getClass().getName()+" id="+this.id);
		System.out.println(getClass().getName()+" "+e);
		result=1;
	    }
	    try{
		query2PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query2);
		System.out.println(getClass().getName()+" "+e);
	    }


	    // delete the assciated services from Services table
	    String query3="DELETE FROM Services WHERE matrixId=?";
	    PreparedStatement query3PS=null;	    
	    try{
		query3PS	= db.getConn().prepareStatement(query3);
		query3PS.setInt(1,this.id);
		query3PS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting service matrix from Services table");
		System.out.println(getClass().getName()+" "+query3);		
		System.out.println(getClass().getName()+" id="+this.id);
		System.out.println(getClass().getName()+" "+e);
		result=1;
	    }
	    try{
		query3PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query3);
		System.out.println(getClass().getName()+" "+e);
	    }	    


	    // delete the assciated services from MetricRecordSummary table
	    String query4="DELETE FROM MetricRecordSummary WHERE matrixId=?";
	    PreparedStatement query4PS=null;	    
	    try{
		query4PS	= db.getConn().prepareStatement(query4);
		query4PS.setInt(1,this.id);
		query4PS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting service matrix from MetricRecordSummary table");
		System.out.println(getClass().getName()+" "+query4);		
		System.out.println(getClass().getName()+" id="+this.id);
		System.out.println(getClass().getName()+" "+e);
		result=1;
	    }
	    try{
		query4PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query4);
		System.out.println(getClass().getName()+" "+e);
	    }		    


	    // delete the assciated services from MetricRecord table
	    String query5="DELETE FROM MetricRecord WHERE matrixId=?";
	    PreparedStatement query5PS=null;
	     try{
		query5PS	= db.getConn().prepareStatement(query5);
		query5PS.setInt(1,this.id);
		query5PS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting service matrix from MetricRecord table");
		System.out.println(getClass().getName()+" "+query5);		
		System.out.println(getClass().getName()+" id="+this.id);
		System.out.println(getClass().getName()+" "+e);
		result=1;
	    }
	    try{
		query5PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query5);
		System.out.println(getClass().getName()+" "+e);
	    }	
	    return result;
	}
    }
    public void save(){
	String query="UPDATE ServiceMatrices SET  ServiceMatrixName=?, ReadOnly=false, SchedulerName=? WHERE id=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.serviceMatrixName);
	    queryPS.setString(2,this.schedulerName);
	    queryPS.setInt(3,this.id);
	    queryPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when saveing service matrix into database");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" serviceMatrixName="+this.serviceMatrixName);
	    System.out.println(getClass().getName()+" id="+this.id);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }
    public void insert(){
	activityLogger.log("create new service matrix name="+this.serviceMatrixName);

	String query="INSERT INTO ServiceMatrices (ServiceMatrixName,ServiceMatrixType,SchedulerName,ReadOnly) VALUES (?,?,?,false)";
	PreparedStatement insertQueryPS=null;
	try{
	    insertQueryPS=db.getConn().prepareStatement(query);
	    insertQueryPS.setString(1,this.serviceMatrixName);
	    insertQueryPS.setString(2,this.serviceMatrixType);	    
	    insertQueryPS.setString(3,this.schedulerName);	    
	    insertQueryPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when inserting service matrix into database");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" serviceMatrixName="+this.serviceMatrixName);
	    System.out.println(getClass().getName()+" serviceMatrixType="+this.serviceMatrixType);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	// now get index of the inserted object
	try{
	    ResultSet rs=insertQueryPS.getGeneratedKeys();
	    while (rs.next ()){
		this.id = rs.getInt(1);
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not obtain key of newly inserted object");
	    System.out.println(getClass().getName()+" "+e);
	}

	try{
	    insertQueryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}

	activityLogger.log("new service matrix id="+this.id);
    }
    public boolean isReadOnly(){
	return  this.readOnly;
    }
    public boolean getReadOnly(){
	return this.readOnly;
    }
    public String getSchedulerName(){
	return this.schedulerName;
    }
    public void setSchedulerName(String inputVar){
	this.schedulerName=inputVar;
    }
    public String getServiceMatrixName(){
	return this.serviceMatrixName;
    }
    public void setServiceMatrixName(String serviceMatrixName){
	this.serviceMatrixName=serviceMatrixName;
    }
    public boolean isThroughputMatrix(){
	if(this.getServiceMatrixType().indexOf("throughput")>-1){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isLatencyMatrix(){
	if(this.getServiceMatrixType().indexOf("latency")>-1){
	    return true;
	}else{
	    return false;
	}
    }
    
    public String getServiceMatrixType(){
	return this.serviceMatrixType;
    }
    public void setServiceMatrixType(String serviceMatrixType){
	this.serviceMatrixType=serviceMatrixType;
    }
    public boolean isAPD(){
	if(this.getServiceMatrixType().indexOf("apd")>-1){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isTracerouteMatrix(){
	if(this.getServiceMatrixType().indexOf("traceroute")>-1){
	    return true;
	}else{
	    return false;
	}
    }
    public int getId(){
	return this.id;
    }
    // public ServiceMatrix getServiceMatrix(){
    // 	ServiceMatrix serviceMatrix=new ServiceMatrix(this.parameterBag,this.db,this.getId());
    // 	return serviceMatrix;
    // }


    public HtmlLink getLinkToListOfServiceMatrices(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("List of Matrices")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToServiceMatrixDisplayPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Display Service Matrix")  );
	paramBagLocal.addParam("serviceMatrixName",this.getServiceMatrixName());
	paramBagLocal.addParam("id",Integer.toString(this.getId()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToServiceMatrixEditPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Edit Service Matrix")  );
	paramBagLocal.addParam("serviceMatrixName",this.getServiceMatrixName());
	paramBagLocal.addParam("id",Integer.toString(this.getId()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    } 
    public HtmlLink getLinkToServiceMatrixDeletePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Delete Service Matrix")  );
	paramBagLocal.addParam("serviceMatrixName",this.getServiceMatrixName());
	paramBagLocal.addParam("id",Integer.toString(this.getId()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }    
    public HtmlLink getLinkToConfirmServiceMatrixDeletePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Confirm Delete Service Matrix")  );
	paramBagLocal.addParam("serviceMatrixName",this.getServiceMatrixName());
	paramBagLocal.addParam("id",Integer.toString(this.getId()));

	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }   
    public HtmlLink getLinkToSelectHostsToAddPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Select Hosts to add to Service Matrix")  );
	//	paramBagLocal.addParam("serviceMatrixName",this.getServiceMatrixName());
	paramBagLocal.addParam("id",Integer.toString(this.getId()));

	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }   
    public HtmlLink getLinkToAddHostsPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Add Hosts to Service Matrix")  );
	paramBagLocal.addParam("serviceMatrixName",this.getServiceMatrixName());
	paramBagLocal.addParam("id",Integer.toString(this.getId()));

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

    private HtmlLink getLinkToCreateLatencyServiceMatrixPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Latency Service Matrix")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }
    private HtmlLink getLinkToSelectSchedulerPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Select Scheduler for Service Matrix")  );
	paramBagLocal.addParam("id",Integer.toString(this.getId()));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;	
    }

    public  List<HtmlTableCell>infoTableRow(){
	List<HtmlTableCell> result=new ArrayList<HtmlTableCell>();

	HtmlTableCell cell1=new  HtmlTableCell(this.getLinkToServiceMatrixDisplayPage(this.serviceMatrixName));
	cell1.alignLeft();
	result.add(cell1);

	HtmlTableCell cell2=new  HtmlTableCell(this.serviceMatrixType);
	cell2.alignLeft();
	result.add(cell2);

	HtmlTableCell cell3=new  HtmlTableCell(this.getLinkToServiceMatrixEditPage("edit matrix"));
	cell3.alignLeft();
	result.add(cell3);

	HtmlTableCell cell4=new  HtmlTableCell(this.getLinkToConfirmServiceMatrixDeletePage("delete matrix"));
	cell4.alignLeft();
	result.add(cell4);

	HtmlTableCell cell5=new  HtmlTableCell(this.getLinkToSelectHostsToAddPage("add/remove hosts"));
	cell5.alignLeft();
	result.add(cell5);

	HtmlTableCell cell6=new  HtmlTableCell(this.getLinkToSelectSchedulerPage("select scheduler"));
	cell6.alignLeft();
	result.add(cell6);

	return result;
    }

    public List<PerfSonarHost> getHostsInThisMatrix(){
	List<PerfSonarHost> list=new ArrayList<PerfSonarHost>();
	String query="SELECT * FROM ServiceMatricesHosts WHERE matrixId=?";
	PreparedStatement getById=null;
	try{
	    getById	= db.getConn().prepareStatement(query);
	    getById.setInt(1,this.id);
	    ResultSet rs=getById.executeQuery();
	    while(rs.next()){
		int hostId=rs.getInt("hostId");
		PerfSonarHost ph=new PerfSonarHost(this.parameterBag,this.db,hostId);
		list.add(ph);
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading database");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" id="+this.id);		
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getById.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	return list;
    }

    private void getHostsInfo(){
	ListOfPerfSonarHosts listOfHosts=new ListOfPerfSonarHosts(this.parameterBag,this.db);
	List<PerfSonarHost> listOfAllHosts=listOfHosts.getList();
	Iterator itr=listOfAllHosts.iterator();
	while(itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    if(currentHost.hostBelongsToMatrix(this.getId())){
		this.hostsInThisMatrix.add(currentHost);
	    }else{
		this.hostsNotInThisMatrix.add(currentHost);
	    }
	}
    }

    private String chooseMatrixNameForm(String destinationPage){
	String result="<form>";

	HtmlTable formTable=new HtmlTable(2);
	formTable.addCell("Service Matrix Name");
	formTable.addCell("<input type=\"text\" name=\"serviceMatrixName\" value=\"fill matrix name\" /><br>");

	//formTable.addCell("Scheduler");
	//ListOfSchedulers listOfSchedulers = new ListOfSchedulers(this.parameterBag,this.db);
	//formTable.addCell(listOfSchedulers.schedulerSelector(this.getSchedulerName()));	

	result=result+formTable.toHtml();

	//result=result+"<input type=\"hidden\" name=\"id\" value=\""+Integer.toString(this.id)+"\" />";
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress(destinationPage)+"\" />";
	result=result+"<input type=\"submit\" value=\"Create Matrix\" /><br>";
	result=result+"</form>";

	return result;
    }


    private String addHostsForm(){
	String form="<form>";

	Iterator itr=this.hostsNotInThisMatrix.iterator();
	while (itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    String hostName=currentHost.getHostName();
	    int hostId=currentHost.getId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+hostId+"\">"+hostName+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Hosts to Service Matrix")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Add Selected Hosts\" /><br>";
	form=form+"</form>";
	return form;
    }

    private String removeHostsForm(){
	String form="<form>";

	Iterator itr=this.hostsInThisMatrix.iterator();
	while (itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    String hostName=currentHost.getHostName();
	    int hostId=currentHost.getId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+hostId+"\">"+hostName+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Remove Hosts from Service Matrix")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Remove Selected Hosts\" /><br>";
	form=form+"</form>";
	return form;
    }
    private HtmlTable addHostsTable(){
	this.getHostsInfo();
	HtmlTable ht=new HtmlTable(2);

	ht.addCell("Hosts in Matrix");
	ht.addCell("Hosts not in Matrix");

	String form1=this.removeHostsForm();
	HtmlTableCell formCell1=new HtmlTableCell(form1);
	formCell1.alignLeft();
	ht.addCell(formCell1);

	String form=this.addHostsForm();
	HtmlTableCell formCell=new HtmlTableCell(form);
	formCell.alignLeft();
	ht.addCell(formCell);
	return ht;
    }
    private String selectSchedulerForm(){
	String result="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">" ;

	HtmlTable ht=new HtmlTable(2);

	ht.addCell("Scheduler Name");
	ListOfSchedulers listOfSchedulers = new ListOfSchedulers(this.parameterBag,this.db);

	ht.addCell(listOfSchedulers.schedulerSelector(this.getSchedulerName()));	

	result=result+"<br>"+ht.toHtml()+"<br>";

	result=result+"<input type=\"hidden\" name=\"id\" value=\""+Integer.toString(this.id)+"\" />";
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Update Scheduler for Service Matrix")+"\" />";
	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	return result;
    }

    private String editServiceMatrixForm(){
	String result="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">" ;
	HtmlTable formTable=new HtmlTable(2);
	formTable.addCell("Service Matrix Name");
	formTable.addCell("<input type=\"text\" name=\"serviceMatrixName\" value=\""+this.serviceMatrixName+"\" /><br>");

	formTable.addCell("Service Matrix Type");
	//String selector="<SELECT NAME=\"serviceMatrixType\">";
	//if (this.serviceMatrixType.equals("throughput")){
	//    selector=selector+"<OPTION VALUE=throughput SELECTED>Throughput";
	//    selector=selector+"<OPTION VALUE=latency>Latency";
	//}else{
	//    selector=selector+"<OPTION VALUE=throughput>Throughput";
	//    selector=selector+"<OPTION VALUE=latency SELECTED>Latency";	    
	//}
	//selector=selector+"</SELECT>";
	//formTable.addCell(selector);
	formTable.addCell(this.getServiceMatrixType());

	formTable.addCell("Scheduler");
	ListOfSchedulers listOfSchedulers = new ListOfSchedulers(this.parameterBag,this.db);
	formTable.addCell(listOfSchedulers.schedulerSelector(this.getSchedulerName()));	

	result=result+formTable.toHtml();
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+Integer.toString(this.id)+"\" />";
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Service Matrix")+"\" />";
	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	return result;
    }
    public String editServiceMatrixPage(){
	String result="<strong>Edit Service Matrix "+this.serviceMatrixName+" </strong><br>";
	result=result+this.editServiceMatrixForm();
	result=result+"<br>";
	return result;
    }
    public String saveServiceMatrixPage(){
	String result="<br><h3><strong>Save Service Matrix "+this.serviceMatrixName+" </strong></h3><br>";
	this.save();
	result=result+"<string>Service Matrix Has Been Saved</strong><br>";
	result=result+"<br>"+this.getLinkToListOfServiceMatrices("Go to list of matrices")+"<br>";
	return result;
    }
    public String confirmDeleteServiceMatrixPage(){
	String result="<strong>Service Matrix "+this.serviceMatrixName+" will be deleted</strong><br>";
	result=result+this.getLinkToServiceMatrixDeletePage("<strong>Click here to delete it</strong>")+"<br>or<br>"+this.getLinkToListOfServiceMatrices("<strong>click here to go to list of service matrices</strong>");
	return result;
    }
    public String deleteServiceMatrixPage(){
	String result="<strong>Delete Service Matrix "+this.serviceMatrixName+" </strong><br>";
	int deleteResult=this.delete();
	if(deleteResult==0){
	    result=result+"<string>Service Matrix Has Been Deleted</strong><br>";
	}else{
	    result=result+"<string>Failed to delete service matrix!</strong><br>";
	}
	result=result+this.getLinkToListOfServiceMatrices("<strong>Click here to go to list of service matrices</strong>");
	return result;
    }
    public String createServiceMatrixPage(){
	String result="<strong>Create Service Matrix</strong><br>";
	ServiceMatrixRecord newServiceMatrix= new ServiceMatrixRecord(this.parameterBag, this.db);
	newServiceMatrix.insert();
	result=result+"<strong>New Service Matrix has been created</strong><br>";
	result=result+newServiceMatrix.getLinkToServiceMatrixEditPage("Go here to edit the new service matrix").toHtml()+"<br>";
	result=result+"or<br>";
	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String createTracerouteServiceMatrixPage(){
	String result="<strong>Create Traceroute Service Matrix</strong><br>";
	this.setServiceMatrixType("traceroute");
	this.insert();
	result=result+"<strong>New Traceroute Service Matrix has been created</strong><br>";
	result=result+this.getLinkToServiceMatrixEditPage("Go here to edit the new service matrix").toHtml()+"<br>";
	result=result+"or<br>";
	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String createThroughputServiceMatrixPage(){
	String result="<strong>Create Throughput Service Matrix</strong><br>";
	this.setServiceMatrixType("throughput");
	this.insert();
	result=result+"<strong>New Throughput Service Matrix has been created</strong><br>";
	result=result+this.getLinkToServiceMatrixEditPage("Go here to edit the new service matrix").toHtml()+"<br>";
	result=result+"or<br>";
	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String createAPDThroughputServiceMatrixPage(){
	String result="<strong>Create APD Throughput Service Matrix</strong><br>";
	this.setServiceMatrixType("apd_throughput");
	this.insert();
	result=result+"<strong>New APD Throughput Service Matrix has been created</strong><br>";
	result=result+this.getLinkToServiceMatrixEditPage("Go here to edit the new service matrix").toHtml()+"<br>";
	result=result+"or<br>";
	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String createAPDLatencyServiceMatrixPage(){
	String result="<strong>Create APD Latency Service Matrix</strong><br>";
	this.setServiceMatrixType("apd_latency");
	this.insert();
	result=result+"<strong>New APD Latency Service Matrix has been created</strong><br>";
	result=result+this.getLinkToServiceMatrixEditPage("Go here to edit the new service matrix").toHtml()+"<br>";
	result=result+"or<br>";
	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String chooseNameForTracerouteServiceMatrixPage (){
	String result="<strong>Choose Traceroute Service Matrix</strong><br>";

	result=result+this.chooseMatrixNameForm("Create Traceroute Service Matrix");

	result=result+"<strong>or</strong><br>";

	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String chooseNameForThroughputServiceMatrixPage (){
	String result="<strong>Choose Throughput Service Matrix</strong><br>";

	result=result+this.chooseMatrixNameForm("Create Throughput Service Matrix");

	result=result+"<strong>or</strong><br>";

	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String chooseNameForAPDThroughputServiceMatrixPage (){
	String result="<strong>Choose APD Throughput Service Matrix</strong><br>";

	result=result+this.chooseMatrixNameForm("Create APD Throughput Service Matrix");

	result=result+"<strong>or</strong><br>";

	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String chooseNameForLatencyServiceMatrixPage (){
	String result="<strong>Choose Latency Service Matrix</strong><br>";

	result=result+this.chooseMatrixNameForm("Create Latency Service Matrix");

	result=result+"<strong>or</strong><br>";

	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String chooseNameForAPDLatencyServiceMatrixPage (){
	String result="<strong>Choose APD Latency Service Matrix</strong><br>";

	result=result+this.chooseMatrixNameForm("Create APD Latency Service Matrix");

	result=result+"<strong>or</strong><br>";

	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }
    public String createLatencyServiceMatrixPage(){
	String result="<strong>Create Latency Service Matrix</strong><br>";
	this.setServiceMatrixType("latency");
	this.insert();

	result=result+"<strong>New Latency Service Matrix has been created</strong><br>";
	result=result+this.getLinkToServiceMatrixEditPage("Go here to edit the new service matrix").toHtml()+"<br>";
	result=result+"or<br>";
	result=result+this.getLinkToListOfServiceMatrices("go to the list of matrices").toHtml();
	return result;
    }

    public void addHosts(List<PerfSonarHost>listOfHosts){

	activityLogger.log("add hosts to matrix id="+this.id);

	String query="INSERT INTO ServiceMatricesHosts (hostId,matrixId) VALUES(?,?)";

	Iterator itr=listOfHosts.iterator();
	while(itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    PreparedStatement queryPS=null;
	    try{
		queryPS= db.getConn().prepareStatement(query);
		queryPS.setInt(1,currentHost.getId());
		queryPS.setInt(2,this.getId());
		queryPS.executeUpdate();
		activityLogger.log("host "+currentHost.getHostName()+" added to matrix "+this.getServiceMatrixName()+" id="+this.getId());
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when inserting host id and matrix id into ServiceMatricesHosts");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" host id="+currentHost.getId());
		System.out.println(getClass().getName()+" matrix id="+this.getId());
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }
	}

	// add service matrix elements in Services table
	List<PerfSonarHost> listOfSourceHoststsInMatrix= this.getHostsInThisMatrix();
	List<PerfSonarHost> listOfDestinationHoststsInMatrix= this.getHostsInThisMatrix();
	Iterator sourceItr=listOfSourceHoststsInMatrix.iterator();
	while(sourceItr.hasNext()){
	    PerfSonarHost sourceHost=(PerfSonarHost)sourceItr.next();
	    String sourceHostName=sourceHost.getHostName();
	    Iterator destItr=listOfDestinationHoststsInMatrix.iterator();
	    while(destItr.hasNext()){
		PerfSonarHost destinationHost=(PerfSonarHost)destItr.next();
		String destinationHostName=destinationHost.getHostName();
		// do not create diagonal elements for throughput matrix
		if(!sourceHostName.equals(destinationHostName)||this.isLatencyMatrix()){
		    if(!this.isAPD()&& !this.isTracerouteMatrix()){
			String monitorHostname=destinationHostName;
			ServiceMatrixElement sme1 = new ServiceMatrixElement(this.parameterBag, this.db, this.id, sourceHost,destinationHost,destinationHost);
			if(sme1.hasBeenDefined()){
			    // do nothing
			}else{
			    System.out.println(getClass().getName()+" insert new service matrix element!");
			    sme1.insertNewElement();
			    System.out.println(getClass().getName()+" new service matrix element inserted");
			}
			ServiceMatrixElement sme2 = new ServiceMatrixElement(this.parameterBag, this.db, this.id, sourceHost,destinationHost,sourceHost);
			
			if(sme2.hasBeenDefined()){
			    
			}else{
			    System.out.println(getClass().getName()+" insert new service matrix element!");
			    sme2.insertNewElement();
			    System.out.println(getClass().getName()+" new service matrix element inserted");
			}
		    }else{ // this is APD or traceroute
			ServiceMatrixElement sme1 = new ServiceMatrixElement(this.parameterBag, this.db, this.id, sourceHost,destinationHost,destinationHost);
			if(sme1.hasBeenDefined()){
			    // do nothing
			}else{
			    System.out.println(getClass().getName()+" insert new service matrix element!");
			    sme1.insertNewElement();
			    System.out.println(getClass().getName()+" new service matrix element inserted");
			}
		    }
		}
	    }
	}
    }
    public void removeHosts(List<PerfSonarHost>listOfHosts){

	activityLogger.log("remove hosts from matrix id="+this.id);

	String query="DELETE FROM ServiceMatricesHosts WHERE hostId=? and matrixId=?";
	Iterator itr=listOfHosts.iterator();
	while(itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    String currentHostName=currentHost.getHostName();
	    boolean hostSuccesfullyRemoved=true;
	    // remove host from ServiceMatricesHosts Table
	    PreparedStatement queryPS=null;
	    try{
		queryPS= db.getConn().prepareStatement(query);
		queryPS.setInt(1,currentHost.getId());
		queryPS.setInt(2,this.getId());
		queryPS.executeUpdate();
		
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting host id and matrix id into ServiceMatricesHosts");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" host id="+currentHost.getId());
		System.out.println(getClass().getName()+" matrix id="+this.getId());
		System.out.println(getClass().getName()+" "+e);
		hostSuccesfullyRemoved=false;
	    }
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }
	    // remove host from Services table
	    String query2="DELETE FROM Services WHERE (HostName=? or HostName2=? or HostName3=? ) and matrixId=?";
	    PreparedStatement query2PS=null;
	    try{
		query2PS= db.getConn().prepareStatement(query2);
		
		query2PS.setString(1,currentHostName);
		query2PS.setString(2,currentHostName);
		query2PS.setString(3,currentHostName);
		query2PS.setInt(4,this.getId());
		query2PS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting hostName and matrix id from Services");
		System.out.println(getClass().getName()+" "+query2);
		System.out.println(getClass().getName()+" HostName="+currentHostName);
		System.out.println(getClass().getName()+" matrix id="+this.getId());
		System.out.println(getClass().getName()+" "+e);
		hostSuccesfullyRemoved=false;
	    }	    
	    try{
		query2PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query2);
		System.out.println(getClass().getName()+" "+e);
	    }
	    // remove host from MetricRecordSummary table
	    String query3="DELETE FROM MetricRecordSummary WHERE (HostName=? or HostName2=? or HostName3=? ) and matrixId=?";
	    PreparedStatement query3PS=null;
	    try{
		query3PS= db.getConn().prepareStatement(query3);

		query3PS.setString(1,currentHostName);
		query3PS.setString(2,currentHostName);
		query3PS.setString(3,currentHostName);
		query3PS.setInt(4,this.getId());
		query3PS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting hostName and matrix id from MetricRecordSummary");
		System.out.println(getClass().getName()+" "+query3);
		System.out.println(getClass().getName()+" HostName="+currentHostName);
		System.out.println(getClass().getName()+" matrix id="+this.getId());
		System.out.println(getClass().getName()+" "+e);
		hostSuccesfullyRemoved=false;
	    }
	    try{
		query3PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query3);
		System.out.println(getClass().getName()+" "+e);
	    }

	    // remove host from MetricRecord table
	    String query4="DELETE FROM MetricRecord WHERE (HostName=? or HostName2=? or HostName3=? ) and matrixId=?";
	    PreparedStatement query4PS=null;
	    try{
		query4PS= db.getConn().prepareStatement(query4);

		query4PS.setString(1,currentHostName);
		query4PS.setString(2,currentHostName);
		query4PS.setString(3,currentHostName);
		query4PS.setInt(4,this.getId());
		query4PS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting hostName and matrix id from MetricRecord");
		System.out.println(getClass().getName()+" "+query4);
		System.out.println(getClass().getName()+" HostName="+currentHostName);
		System.out.println(getClass().getName()+" matrix id="+this.getId());
		System.out.println(getClass().getName()+" "+e);
		hostSuccesfullyRemoved=false;
	    }	    
	    try{
		query4PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query4);
		System.out.println(getClass().getName()+" "+e);
	    }	    

	    if(hostSuccesfullyRemoved){
		activityLogger.log("host "+currentHost.getHostName()+" removed matrix "+this.getServiceMatrixName()+" id="+this.getId());
	    }else{
		activityLogger.log("error when removing host "+currentHost.getHostName()+" from matrix "+this.getServiceMatrixName()+" id="+this.getId());
	    }
	    
	}
    }
    public String addHostsPage(){
	String result="<H2>Add Hosts</h2><br>";
	List<PerfSonarHost>listOfHosts=new ArrayList<PerfSonarHost>();
	Iterator itr = this.parameterBag.listOfIds.iterator();
	while(itr.hasNext()){
	    Integer integerHostId=(Integer)itr.next();
	    int hostId=integerHostId.intValue();
	    PerfSonarHost currentHost=new PerfSonarHost(this.parameterBag,this.db,hostId);
	    listOfHosts.add(currentHost);
	    result=result+currentHost.getHostName()+"<br>";
	}
	this.addHosts(listOfHosts);
	result=result+this.getLinkToSelectHostsToAddPage("<strong>Go to add/remove hosts page</strong>");
	result=result+" or <br>";
	result=result+this.getLinkToListOfServiceMatrices("<strong>Click here to go to list of service matrices</strong>");
	result=result+" or <br>";
	result=result+this.getLinkToServiceMatrixDisplayPage("<strong>Click here to display matrix "+this.getServiceMatrixName()+"</strong>");
	return result;
    }
    public String removeHostsPage(){
	String result="<H2>Remove Hosts</h2><br>";
	List<PerfSonarHost>listOfHosts=new ArrayList<PerfSonarHost>();
	Iterator itr = this.parameterBag.listOfIds.iterator();
	while(itr.hasNext()){
	    Integer integerHostId=(Integer)itr.next();
	    int hostId=integerHostId.intValue();
	    PerfSonarHost currentHost=new PerfSonarHost(this.parameterBag,this.db,hostId);
	    listOfHosts.add(currentHost);
	    result=result+currentHost.getHostName()+"<br>";
	}
	this.removeHosts(listOfHosts);
	result=result+this.getLinkToSelectHostsToAddPage("<strong>Go to add/remove hosts page</strong>");
	result=result+" or <br>";
	result=result+this.getLinkToListOfServiceMatrices("<strong>Click here to go to list of service matrices</strong>");
	result=result+" or <br>";
	result=result+this.getLinkToServiceMatrixDisplayPage("<strong>Click here to display matrix "+this.getServiceMatrixName()+"</strong>");
	return result;
    }
    public String selectHostsToAddPage(){
	String result="<H2>Add/Remove Hosts to Matrix "+this.getServiceMatrixName()+"</h2><br>";
	result=result+this.addHostsTable().toHtml();
	return result;
    }

    public String selectSchedulerPage(){
	String result="<H2>Select Scheduler For Service Matrix "+this.getServiceMatrixName()+"</h2><br>";
	result=result+this.selectSchedulerForm();
	return result;
    }

    public boolean updateScheduler(){
	boolean result=true;
	this.save();
	// now update Services table
	String query2="UPDATE Services SET SchedulerName=? WHERE matrixId=?";
	PreparedStatement query2PS=null;
	try{
	    query2PS= db.getConn().prepareStatement(query2);
		
	    query2PS.setString(1,this.getSchedulerName());
	    query2PS.setInt(2,this.getId());
	    query2PS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating scheduler for matrix in Services");
		System.out.println(getClass().getName()+" "+query2);
		System.out.println(getClass().getName()+" SchedulerName="+this.getSchedulerName() );
		System.out.println(getClass().getName()+" matrix id="+this.getId());
		System.out.println(getClass().getName()+" "+e);
		result=false;
	    }	    
	    try{
		query2PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query2);
		System.out.println(getClass().getName()+" "+e);
	    }	
	return result;
    }
    public String updateSchedulerPage(){
	String result="<H2>New Update Scheduler For Service Matrix "+this.getServiceMatrixName()+"</h2><br>";
	this.setSchedulerName(this.parameterBag.schedulerName);
	boolean updateResult=this.updateScheduler();
	if(updateResult){
	    result=result+"New Scheduler="+this.getSchedulerName()+"<br>";
	}else{
	    result=result+"Scheduler update failed, check the logs!!!<br>";
	}
	return result;
    }


    public String toString(){
	String result=" to be filled";

	return result;
    }


}
