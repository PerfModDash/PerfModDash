package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.Collections;



public class PerfSonarCloud implements Comparable
{  
    private ParameterBag parameterBag = null;
    private DbConnector db=null;
    
    private String cloudName=null;
    private ProbeStatus cloudStatus=null;
    private int cloudId=-1;
    private boolean readOnly=false;

    ListOfPerfSonarSites list=null;

    private List<PerfSonarSite>listOfAllSites=   new ArrayList<PerfSonarSite>();
    private List<PerfSonarSite>sitesInThisCloud=   new ArrayList<PerfSonarSite>();
    private List<PerfSonarSite>sitesNotInThisCloud=new ArrayList<PerfSonarSite>();

    private List<ServiceMatrix>matricesInThisCloud=   new ArrayList<ServiceMatrix>();

    private List<ServiceMatrixRecord>matrixRecordsInThisCloud=   new ArrayList<ServiceMatrixRecord>();
    private List<ServiceMatrixRecord>matrixRecordsNotInThisCloud=new ArrayList<ServiceMatrixRecord>();

    public PerfSonarCloud(ParameterBag paramBag,  DbConnector inputDb){
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.setCloudStatus(new ProbeStatus("UNDEFINED"));
    }
    public PerfSonarCloud(ParameterBag paramBag,  DbConnector inputDb,String cloudName ){

	    this.parameterBag=paramBag;
	    this.db=inputDb;

	    this.cloudName=cloudName;

	    this.setCloudStatus(new ProbeStatus("UNDEFINED"));
	    
	    String query="SELECT * FROM Clouds WHERE CloudName=?";
	    PreparedStatement queryPS=null;
	    try{
		queryPS=db.getConn().prepareStatement(query);
		queryPS.setString(1,this.getCloudName());
		ResultSet rs=queryPS.executeQuery();
		this.unpackResultSet(rs);
		rs.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when creating cloud name="+this.cloudName);
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }finally{
		try{
		    queryPS.close();
		}catch (Exception e){
		    System.out.println(getClass().getName()+"failed to close prepared statement");
		    System.out.println(getClass().getName()+" "+e);
		}
	    }

	    this.fillListOfSitesInThisCloud();


	}
    public PerfSonarCloud(ParameterBag paramBag,  DbConnector inputDb,int cloudId )
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;

	    this.cloudId=cloudId;

	    this.load();

	    this.setCloudStatus(new ProbeStatus("UNDEFINED"));

	    this.fillListOfSitesInThisCloud();

	}

    public void fillListOfSitesInThisCloud(){
	List<PerfSonarSite>listOfAllSites=   new ArrayList<PerfSonarSite>();

	ListOfPerfSonarSites listOfPerfSonarSites =new ListOfPerfSonarSites(this.parameterBag,this.db);
	listOfAllSites=listOfPerfSonarSites.getList();


	Iterator itr = listOfAllSites.iterator();
	while(itr.hasNext()){
	    PerfSonarSite currentSite=(PerfSonarSite)itr.next();
	    if(this.cloudContainsSite(currentSite)){
		sitesInThisCloud.add(currentSite);
		currentSite.getHostsInfo();
		currentSite.fillHostsStatusInfo();
		this.updateCloudStatus(currentSite.getSiteStatus());
	    }else{
		sitesNotInThisCloud.add(currentSite);
	    }
	}



    }


    public void unpackResultSet(ResultSet rs){
	try{
	    while(rs.next()){
		this.setCloudId(rs.getInt("cloudId"));
		this.setCloudName(rs.getString("CloudName"));
		this.setReadOnly(rs.getBoolean("ReadOnly"));
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error occured when unpacking result set");
	    System.out.println(getClass().getName()+" "+e);
	}
    }

    public boolean load(){
	// load cloud with current id from database
	boolean result=true;
	String query="SELECT * FROM Clouds WHERE cloudId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getCloudId());
	    ResultSet rs=queryPS.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when loading cloud id="+this.getCloudId());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }
	}

	return result;
    }
    public boolean insert(){
	boolean result=true;
	String insertCommand="INSERT INTO Clouds (CloudName,ReadOnly) VALUES(?,?)";
	PreparedStatement insertCommandPS=null;
	try{
	    insertCommandPS=db.getConn().prepareStatement(insertCommand);
	    insertCommandPS.setString(1,this.getCloudName());
	    insertCommandPS.setBoolean(2,this.getReadOnly());
	    insertCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when inserting cloud name="+this.getCloudName());
	    System.out.println(getClass().getName()+" "+insertCommand);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	// get the index of the inserted object
	try{
	    ResultSet rs=insertCommandPS.getGeneratedKeys();
	    while (rs.next ()){
		this.setCloudId(rs.getInt(1));
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not obtain key of newly inserted cloud");
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    insertCommandPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+insertCommand);
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }
    public boolean delete(){
	boolean result=true;
	if(!this.getReadOnly()){
	    String deleteCommand="DELETE FROM Clouds WHERE cloudId=?";
	    PreparedStatement deleteCommandPS=null;
	    try{
		deleteCommandPS=db.getConn().prepareStatement(deleteCommand);
		deleteCommandPS.setInt(1,this.getCloudId());
		deleteCommandPS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting cloud id="+this.getCloudId()+" cloudName="+this.getCloudName());
		System.out.println(getClass().getName()+" "+e);
		result=false;
	    }finally{
		try{
		    deleteCommandPS.close();
		}catch (Exception e){
		    System.out.println(getClass().getName()+" failed to close prepared statement");
		    System.out.println(getClass().getName()+" "+deleteCommand);
		    System.out.println(getClass().getName()+" "+e);
		}
	    }
	}else{
	    System.out.println(getClass().getName()+" cannot delete cloud id="+this.getCloudId()+" cloudName="+this.getCloudName());
	    System.out.println(getClass().getName()+" readOnly="+this.getReadOnly());
	    result=false;
	}
	return result;
    }
    public void save(){
	String saveCommand="UPDATE Clouds SET CloudName=?,ReadOnly=? WHERE cloudId=?";
	PreparedStatement saveCommandPS=null;
	try{
	    saveCommandPS=db.getConn().prepareStatement(saveCommand);
	    saveCommandPS.setString(1,this.getCloudName());
	    saveCommandPS.setBoolean(2,this.getReadOnly());
	    saveCommandPS.setInt(3,this.getCloudId());
	    saveCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when saving cloud id="+this.getCloudId());
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    saveCommandPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+saveCommand);
	    System.out.println(getClass().getName()+" "+e);
	}
    }
    // --- links 
    public HtmlLink getLinkToManipulateCloudsPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Manipulate Clouds")  );
	paramBagLocal.addParam("cloudName",this.getCloudName() );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToDisplayCloudPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Cloud Overview")  );
	//paramBagLocal.addParam("cloudName",this.getCloudName() );
	paramBagLocal.addParam("id",Integer.toString(this.getCloudId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToConfirmDeleteCloudPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Confirm Delete Cloud")  );
	paramBagLocal.addParam("cloudName",this.getCloudName() );
	paramBagLocal.addParam("id",Integer.toString(this.getCloudId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToDeleteCloudPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Delete Cloud")  );
	paramBagLocal.addParam("cloudName",this.getCloudName() );
	paramBagLocal.addParam("id",Integer.toString(this.getCloudId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToAddRemoveSitesPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Add remove Sites to Cloud Page")  );
	paramBagLocal.addParam("id",Integer.toString(this.getCloudId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToAddRemoveMatricesPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Add Remove Matrices to Cloud Page")  );
	paramBagLocal.addParam("id",Integer.toString(this.getCloudId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }


    // --- get and set methods
    public  List<PerfSonarSite> getListOfSites(){
	return this.sitesInThisCloud;
    }
    public  List<ServiceMatrix> getListOfMatrices(){
	List<ServiceMatrix> list=new ArrayList<ServiceMatrix>();
	String query="SELECT * FROM MatricesClouds where cloudId=?";
	PreparedStatement queryPS=null;
	String failurePoint="1";
	int currentMatrixid=-100;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getCloudId());
	    failurePoint="2";
	    ResultSet rs=queryPS.executeQuery();
	    failurePoint="3";
	    while(rs.next()){
		currentMatrixid=rs.getInt("matrixId");
		failurePoint="4";
		ServiceMatrix serviceMatrix=new ServiceMatrix(this.parameterBag,this.db,currentMatrixid);
		failurePoint="5";
		list.add(serviceMatrix);
		failurePoint="6";
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed get list of matrices for cloud cloudId="+this.getCloudId());
	    System.out.println(getClass().getName()+" failurePoint="+failurePoint);
	    System.out.println(getClass().getName()+" currentMatrixid="+currentMatrixid);
	    System.out.println(getClass().getName()+" this.parameterBag="+this.parameterBag);
	    System.out.println(getClass().getName()+" this.db="+this.db);
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	
	return list;
    }
    public String getCloudName(){
	return this.cloudName;
    }
    public void setCloudName(String inputVar){
	this.cloudName=inputVar;
    }
    public int getCloudId(){
	return this.cloudId;
    }
    public void setCloudId(int inputVar){
	this.cloudId=inputVar;
    }
    public boolean getReadOnly(){
	return this.readOnly;
    }
    public void setReadOnly(boolean inputVar){
	this.readOnly=inputVar;
    }

    public ProbeStatus getCloudStatus(){
	return cloudStatus;
    }
    public void setCloudStatus(ProbeStatus inputVar){
	this.cloudStatus=inputVar;
    }
    public void updateCloudStatus(ProbeStatus inputStatus){
	if(this.cloudStatus.statusLevel()<inputStatus.statusLevel()){
	    this.setCloudStatus(inputStatus);
	}
    }


    public HtmlTableCell shortStatusCell(){
	// ParameterBag paramBagLocal=new ParameterBag();
	// paramBagLocal.setRequestUri(parameterBag.requestUri);
	// paramBagLocal.addParam("page",ParameterBag.pageAddress("Cloud Overview")  );
	// paramBagLocal.addParam("cloudName",cloudName);
	// String linkToDetailPage=paramBagLocal.makeLink();
	// HtmlLink link=new HtmlLink(linkToDetailPage,cloudName );


	HtmlLink link=this.getLinkToDisplayCloudPage(this.getCloudName());
	HtmlTableCell cell=new HtmlTableCell(link,this.getCloudStatus().color());
	return cell;	
    }


    public ListOfPerfSonarSites getListOfPerfSonarSites(){
	return list;
    }

    public  List<HtmlTableCell>infoTableRow(){
	List<HtmlTableCell> result=new ArrayList<HtmlTableCell>();
	HtmlTableCell cell1=new  HtmlTableCell(this.getLinkToDisplayCloudPage(this.getCloudName()));
	cell1.alignLeft();
	result.add(cell1);

	HtmlTableCell cell3=new  HtmlTableCell(this.getLinkToConfirmDeleteCloudPage("delete cloud"));
	cell3.alignLeft();
	result.add(cell3);

	HtmlTableCell cell4=new  HtmlTableCell(this.getLinkToAddRemoveSitesPage("add/remove sites"));
	cell4.alignLeft();
	result.add(cell4);

	HtmlTableCell cell5=new  HtmlTableCell(this.getLinkToAddRemoveMatricesPage("add/remove service matrices"));
	cell5.alignLeft();
	result.add(cell5);

	return result;
    }

    public ProbeStatus status(){
	return list.listStatus();
    }
    public ProbeStatus cloudStatus(){
	return list.listStatus();
    }

    public String confirmCloudDeletePage(){
	String result="<H2>Confirm Delete PerfSonar Cloud</h2>";
	result=result+"<br>";
	result=result+"<strong>The cloud "+this.getCloudName()+" id="+this.getCloudId()+" will be deleted</strong><br>";
	result=result+this.getLinkToDeleteCloudPage("<strong>Click here to delete it</strong>");
	result=result+" or ";
	result=result+this.getLinkToManipulateCloudsPage("<strong>Click here to go to the list of clouds</strong>");
	return result;						    
    }

    public String deleteCloudPage(){
	String result="<H2>Delete PerfSonar Cloud "+this.getCloudName()+"</h2>";
	result=result+"<br>";
	if(this.delete()){
	    result=result+"The cloud has been deleted";
	}else{
	    result=result+"The delete operation failed!";
	}
	result=result+"<br><br>";
	result=result+this.getLinkToManipulateCloudsPage("<strong>Click here to go to the list of clouds</strong>");
	return result;
    }

    public boolean cloudContainsSite(PerfSonarSite site){
	boolean result=false;
	int siteId=site.getSiteId();
	int numberOfRecordsFound=0;
	String query="SELECT * FROM SitesClouds where cloudId=? and siteId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getCloudId());
	    queryPS.setInt(2,siteId);
	    ResultSet rs=queryPS.executeQuery();
	    numberOfRecordsFound=0;
	    while(rs.next()){
		numberOfRecordsFound=numberOfRecordsFound+1;
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when creating cloud name="+this.cloudName);
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}

	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	if(numberOfRecordsFound>0){
	    result=true;
	}else{
	    result=false;
	}
	return result;
    }

    private boolean cloudContainsMatrix(ServiceMatrixRecord currentMatrix){
	boolean result=false;
	int matrixId=currentMatrix.getId();
	int numberOfRecordsFound=0;
	String query="SELECT * FROM MatricesClouds where cloudId=? and matrixId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getCloudId());
	    queryPS.setInt(2,matrixId);
	    ResultSet rs=queryPS.executeQuery();
	    numberOfRecordsFound=0;
	    while(rs.next()){
		numberOfRecordsFound=numberOfRecordsFound+1;
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured checking whether matrixId="+matrixId+" belongs to cloud="+this.cloudName);
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" matrixId="+matrixId);
	    System.out.println(getClass().getName()+" cloudId="+this.getCloudId());
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured while closing prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}	    
	if(numberOfRecordsFound>0){
	    result=true;
	}else{
	    result=false;
	}


	return result;
    }

    private void getMatricesInfo(){
	ListOfServiceMatrices listOfServiceMatrices =new ListOfServiceMatrices(this.parameterBag,this.db);
	List<ServiceMatrixRecord> listOfAllMatrices=listOfServiceMatrices.getList();


	Iterator itr = listOfAllMatrices.iterator();
	while(itr.hasNext()){
	    ServiceMatrixRecord currentMatrix=(ServiceMatrixRecord)itr.next();
	    if(this.cloudContainsMatrix(currentMatrix)){
		matrixRecordsInThisCloud.add(currentMatrix);
	    }else{
		matrixRecordsNotInThisCloud.add(currentMatrix);
	    }
	}
    }
    private void getSitesInfo(){

	List<PerfSonarSite>listOfAllSites=   new ArrayList<PerfSonarSite>();

	ListOfPerfSonarSites listOfPerfSonarSites =new ListOfPerfSonarSites(this.parameterBag,this.db);
	listOfAllSites=listOfPerfSonarSites.getList();


	Iterator itr = listOfAllSites.iterator();
	while(itr.hasNext()){
	    PerfSonarSite currentSite=(PerfSonarSite)itr.next();
	    if(this.cloudContainsSite(currentSite)){
		sitesInThisCloud.add(currentSite);
	    }else{
		sitesNotInThisCloud.add(currentSite);
	    }
	}
    }
    private String addMatricesForm(){
	String form="<form>";

	Iterator itr=this.matrixRecordsNotInThisCloud.iterator();
	while (itr.hasNext()){
	    ServiceMatrixRecord currentMatrix=(ServiceMatrixRecord)itr.next();
	    String matrixName=currentMatrix.getServiceMatrixName();
	    int matrixId=currentMatrix.getId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+matrixId+"\">"+matrixName+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Matrices To Cloud Page")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getCloudId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Add Selected Matrices\" /><br>";
	form=form+"</form>";
	return form;
    }
    private String addSitesForm(){
	String form="<form>";

	Iterator itr=this.sitesNotInThisCloud.iterator();
	while (itr.hasNext()){
	    PerfSonarSite currentSite=(PerfSonarSite)itr.next();
	    String siteName=currentSite.getSiteName();
	    int siteId=currentSite.getSiteId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+siteId+"\">"+siteName+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Sites To Cloud Page")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getCloudId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Add Selected Sites\" /><br>";
	form=form+"</form>";
	return form;
    }
    private String removeMatricesForm(){
	String form="<form>";

	Iterator itr=this.matrixRecordsInThisCloud.iterator();
	while (itr.hasNext()){
	    ServiceMatrixRecord currentMatrix=(ServiceMatrixRecord)itr.next();
	    String matrixName=currentMatrix.getServiceMatrixName();
	    int matrixId=currentMatrix.getId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+matrixId+"\">"+matrixName+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Remove Matrices From Cloud Page")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getCloudId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Remove Selected Matrices\" /><br>";
	form=form+"</form>";
	return form;
    }
    private String removeSitesForm(){
	String form="<form>";

	Iterator itr=this.sitesInThisCloud.iterator();
	while (itr.hasNext()){
	    PerfSonarSite currentSite=(PerfSonarSite)itr.next();
	    String siteName=currentSite.getSiteName();
	    int siteId=currentSite.getSiteId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+siteId+"\">"+siteName+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Remove Sites From Cloud Page")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getCloudId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Remove Selected Sites\" /><br>";
	form=form+"</form>";
	return form;
    }
    public HtmlTable addRemoveMatricesToCloudTable(){
	this.getMatricesInfo();
	HtmlTable ht=new HtmlTable(2);

	ht.addCell("Matrices in Cloud");
	ht.addCell("Matrices not in Cloud");

	String form1=this.removeMatricesForm();
	HtmlTableCell formCell1=new HtmlTableCell(form1);
	formCell1.alignLeft();
	ht.addCell(formCell1);

	String form=this.addMatricesForm();
	HtmlTableCell formCell=new HtmlTableCell(form);
	formCell.alignLeft();
	ht.addCell(formCell);
	return ht;
    }
    public HtmlTable addRemoveSitesToCloudTable(){
	HtmlTable ht=new HtmlTable(2);

	ht.addCell("Sites in Cloud");
	ht.addCell("Sites not in Cloud");

	String form1=this.removeSitesForm();
	HtmlTableCell formCell1=new HtmlTableCell(form1);
	formCell1.alignLeft();
	ht.addCell(formCell1);

	String form=this.addSitesForm();
	HtmlTableCell formCell=new HtmlTableCell(form);
	formCell.alignLeft();
	ht.addCell(formCell);
	return ht;
    }
    public String addRemoveSitesToCloudPage(){
	String result="<H1>Add or Remove Sites to Cloud "+this.getCloudName()+"</H1>";
	result=result+"<br>";
	result=result+this.addRemoveSitesToCloudTable().toHtml();
	return result;
    }
    public boolean removeSite(PerfSonarSite site){
	boolean result=true;
	String query="DELETE FROM SitesClouds WHERE cloudId=? AND siteId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getCloudId());
	    queryPS.setInt(2,site.getSiteId());
	    
	    queryPS.executeUpdate();
	    
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when adding site="+site.getSiteName()+" to cloud "+this.getCloudName());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" siteId="+site.getSiteId());
	    System.out.println(getClass().getName()+" cloudId="+this.getCloudId());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}

	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	
	return result;
    }
    public boolean removeMatrix(ServiceMatrixRecord serviceMatrixRecord){
	boolean result=true;
	String query="DELETE FROM MatricesClouds WHERE cloudId=? AND matrixId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getCloudId());
	    queryPS.setInt(2,serviceMatrixRecord.getId());
	    
	    queryPS.executeUpdate();
	    
	}catch (Exception e){
	     System.out.println(getClass().getName()+" error occured when removing matrix="+serviceMatrixRecord.getServiceMatrixName()+" from cloud "+this.getCloudName());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" matrixId="+serviceMatrixRecord.getId());
	    System.out.println(getClass().getName()+" cloudId="+this.getCloudId());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}

	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	
	return result;
    }
    public boolean addMatrix(ServiceMatrixRecord serviceMatrixRecord){
	boolean result=true;
	String query="INSERT INTO MatricesClouds (cloudId,matrixId) VALUES (?,?)";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getCloudId());
	    queryPS.setInt(2,serviceMatrixRecord.getId());
	    
	    queryPS.executeUpdate();
	    
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when adding matrix="+serviceMatrixRecord.getServiceMatrixName()+" to cloud "+this.getCloudName());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" matrixId="+serviceMatrixRecord.getId());
	    System.out.println(getClass().getName()+" cloudId="+this.getCloudId());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	
	return result;
    }
    public boolean addSite(PerfSonarSite site){
	boolean result=true;
	String query="INSERT INTO SitesClouds (cloudId,siteId) VALUES (?,?)";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getCloudId());
	    queryPS.setInt(2,site.getSiteId());
	    queryPS.executeUpdate();
	    
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when adding site="+site.getSiteName()+" to cloud "+this.getCloudName());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" siteId="+site.getSiteId());
	    System.out.println(getClass().getName()+" cloudId="+this.getCloudId());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}

	return result;
    }
    public String addSitesToCloudPage(){
	String result="<H1>Add Sites to Cloud "+this.getCloudName()+"</H1>";
	result=result+"<br>";
	result=result+"<strong>The following sites will be added to the cloud</strong><br>";
	Iterator itr = this.parameterBag.listOfIds.iterator();
	while(itr.hasNext()){
	    Integer integerSiteId=(Integer)itr.next();
	    int siteId=integerSiteId.intValue();
	    PerfSonarSite currentSite=new PerfSonarSite(this.parameterBag,this.db,siteId);
	    boolean addResult=this.addSite(currentSite);
	    if(addResult){
		result=result+currentSite.getSiteName()+"  ... added!<br>";
	    }else{
		result=result+currentSite.getSiteName()+" ... failed! Check the logs.<br>";
	    }
	}
	result=result+this.getLinkToManipulateCloudsPage("<strong>Click here to go to the list of clouds</strong>")+" <strong>or</strong> ";
	result=result+this.getLinkToAddRemoveSitesPage("<strong>Click here to go to the add/remove sites page</strong><br>");
	return result;
    }
    public String addMatricesToCloudPage(){
	String result="<H1>Add Matrices to Cloud "+this.getCloudName()+"</H1>";
	result=result+"<br>";
	result=result+"<strong>The following matrices will be added to the cloud</strong><br>";
	Iterator itr = this.parameterBag.listOfIds.iterator();
	while(itr.hasNext()){
	    Integer integerMatrixId=(Integer)itr.next();
	    int matrixId=integerMatrixId.intValue();
	    ServiceMatrixRecord currentMatrix=new ServiceMatrixRecord(this.parameterBag,this.db,matrixId);
	    boolean addResult=this.addMatrix(currentMatrix);
	    if(addResult){
		result=result+currentMatrix.getServiceMatrixName()+"  ... added!<br>";
	    }else{
		result=result+currentMatrix.getServiceMatrixName()+" ... failed! Check the logs.<br>";
	    }
	}
	result=result+this.getLinkToManipulateCloudsPage("<strong>Click here to go to the list of clouds</strong>")+" <strong>or</strong> ";
	result=result+this.getLinkToAddRemoveMatricesPage("<strong>Click here to go to the add/remove matrices page</strong><br>");
	return result;
    }
    public String removeMatricesFromCloudPage(){
	String result="<H1>Remove Matrices from Cloud "+this.getCloudName()+"</H1>";
	result=result+"<br>";
	result=result+"<strong>The following matrices will be removed from the cloud</strong><br>";
	Iterator itr = this.parameterBag.listOfIds.iterator();
	while(itr.hasNext()){
	    Integer integerMatrixId=(Integer)itr.next();
	    int matrixId=integerMatrixId.intValue();
	    ServiceMatrixRecord currentMatrix=new ServiceMatrixRecord(this.parameterBag,this.db,matrixId);
	    boolean addResult=this.removeMatrix(currentMatrix);
	    if(addResult){
		result=result+currentMatrix.getServiceMatrixName()+"  ... removed!<br>";
	    }else{
		result=result+currentMatrix.getServiceMatrixName()+" ... failed! Check the logs.<br>";
	    }
	}
	result=result+this.getLinkToManipulateCloudsPage("<strong>Click here to go to the list of clouds</strong>")+" <strong>or</strong> ";
	result=result+this.getLinkToAddRemoveMatricesPage("<strong>Click here to go to the add/remove matrices page</strong><br>");
	return result;
    }
    public String removeSitesFromCloudPage(){
	String result="<H1>Remove Sites from Cloud "+this.getCloudName()+"</H1>";
	result=result+"<br>";
	result=result+"<strong>The following sites will be removed from the cloud</strong><br>";
	Iterator itr = this.parameterBag.listOfIds.iterator();
	while(itr.hasNext()){
	    Integer integerSiteId=(Integer)itr.next();
	    int siteId=integerSiteId.intValue();
	    PerfSonarSite currentSite=new PerfSonarSite(this.parameterBag,this.db,siteId);
	    boolean removeResult=this.removeSite(currentSite);
	    if(removeResult){
		result=result+currentSite.getSiteName()+"  ... removed!<br>";
	    }else{
		result=result+currentSite.getSiteName()+" ... failed! Check the logs.<br>";
	    }
	}
	result=result+this.getLinkToManipulateCloudsPage("<strong>Click here to go to the list of clouds</strong>")+" <strong>or</strong> ";
	result=result+this.getLinkToAddRemoveSitesPage("<strong>Click here to go to the add/remove sites page</strong><br>");
	return result;
    }
    public String addRemoveMatricesToCloudPage(){
	String result="<H1>Add or Remove Service Matrices from Cloud "+this.getCloudName()+"</H1>";
	result=result+"<br>";
	result=result+this.addRemoveMatricesToCloudTable().toHtml();
	return result;	
    }

    public String cloudOverviewPage(){
	String result="";
	
	String baseDirectory = ParameterStore.getParameterStore().getBaseDirectory();
	String cacheFileName=baseDirectory+this.getCloudName()+".txt";
	File cacheFile=new File(cacheFileName);

	boolean reloadPage=false;

	// does cache file exists?
	if(cacheFile.exists()){
	    long modifiedTime = cacheFile.lastModified();
	    long currentTime = System.currentTimeMillis();

	    long maxCacheAgeInMinutes = 5;
	    if((currentTime-modifiedTime)/1000/60>maxCacheAgeInMinutes){
		reloadPage=true;
	    }
	}else{
	    reloadPage=true;
	}
	
	if(reloadPage){
	    result=reloadCloudOverviewPage();
	    try{
		Writer output = null;
		String text = result;
		output = new BufferedWriter(new FileWriter(cacheFile));
		output.write(text);
		output.close();
		System.out.println(getClass().getName()+"file "+cacheFile+" has been written");  
	    }catch(Exception e){	    
		System.out.println(getClass().getName()+" failed to write output file "+cacheFile );
		System.out.println(getClass().getName()+" "+e);
	    } 
	}else{
	    try{
		BufferedReader reader = new BufferedReader(new FileReader(cacheFileName));
		String line = null;
		while ((line = reader.readLine()) != null) {
		    result=result+line;
		}
		reader.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to read file "+cacheFileName);
		System.out.println(getClass().getName()+" "+e);
	    }	    
	    
	}
	return result;
    }

    public String reloadCloudOverviewPage(){
	String result="<H1>Cloud "+this.getCloudName()+"</H1>";
	result=result+"<center><strong>Sites of "+this.getCloudName()+" cloud</strong></center>";
	result=result+"<br>";

	HtmlTable tableOfCloudSites=new HtmlTable(6);
	Iterator iter1=this.getListOfSites().iterator();
	while(iter1.hasNext()){
	    PerfSonarSite perfsonarSite=(PerfSonarSite)iter1.next();
	    HtmlTableCell cell=perfsonarSite.shortStatusCell();
	    tableOfCloudSites.addCell(cell);
	}
	result=result+"<center>"+tableOfCloudSites.toHtml()+"</center>";

	result=result+"<br>";

	
	HtmlTable tableForServiceMatrices=new HtmlTable(2);
	Iterator iter2=this.getListOfMatrices().iterator();
	while(iter2.hasNext()){
	    ServiceMatrix serviceMatrix=(ServiceMatrix)iter2.next();
	    
	    HtmlTable ht=new HtmlTable(1);
	    String tableName=serviceMatrix.getServiceMatrixName();
	    HtmlTableCell cell1=new HtmlTableCell("<strong>"+tableName+"</strong>");
	    cell1.alignLeft();
	    ht.addCell(cell1);

	    String table=serviceMatrix.htmlTable().toHtml();
	    HtmlTableCell cell2 = new HtmlTableCell(table);
	    ht.addCell(cell2);

	    String explanation=serviceMatrix.getExplanation();
	    HtmlTableCell cell3 = new HtmlTableCell(explanation);
	    cell3.alignLeft();
	    ht.addCell(cell3);

	    HtmlTableCell cell=new HtmlTableCell(ht.toHtml());
	    tableForServiceMatrices.addCell(cell);
	}

	result=result+"<br>";
	result=result+"<center>"+tableForServiceMatrices.toHtml()+"</center>";
	result=result+"<br>";
	
	return result;
    }

    public int compareTo(Object obj) {
	PerfSonarCloud otherSite=(PerfSonarCloud)obj;
	String thisCloudName=getCloudName();
	String otherCloudName=otherSite.getCloudName();
	return thisCloudName.compareTo(otherCloudName);
    }
    

}
