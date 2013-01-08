package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.Collections;



public class PerfSonarSite implements Comparable
{  
    private String externalUrl=null;
    private String cloudName=null;

    private int siteId=0;
    private String siteName=null;
    private String siteDescription=null;
    private boolean readOnly=false;

    private ProbeStatus siteStatus=null;

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private List<PerfSonarHost>hostsInThisSite=new ArrayList<PerfSonarHost>();
    private List<PerfSonarHost>hostsNotInThisSite=new ArrayList<PerfSonarHost>();

    private ActivityLogger activityLogger=null;
    
    ListOfPrimitiveServices list =null;

    public PerfSonarSite(ParameterBag paramBag,  DbConnector inputDb){
	this.parameterBag=paramBag;
	this.db=inputDb;

	this.activityLogger=new ActivityLogger(this.parameterBag,this.db);

	this.siteName="new site";

	this.setSiteStatus(new ProbeStatus("UNDEFINED"));

    }
    public PerfSonarSite(ParameterBag paramBag,  DbConnector inputDb,String siteName )
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;

	    this.activityLogger=new ActivityLogger(this.parameterBag,this.db);

	    this.siteName=siteName;

	    this.setSiteStatus(new ProbeStatus("UNDEFINED"));

	    String query="SELECT * FROM Sites WHERE SiteName=?";
	    PreparedStatement queryPS=null;
	    try{
		queryPS=db.getConn().prepareStatement(query);
		queryPS.setString(1,this.getSiteName());
		ResultSet rs=queryPS.executeQuery();
		this.unpackResultSet(rs);
		rs.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when creating site name="+this.getSiteName());
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }	

	    //this.list = new ListOfPrimitiveServices(parameterBag,db);

	    //this.list.addBySiteName(siteName);
	}
    public PerfSonarSite(ParameterBag paramBag,  DbConnector inputDb,int siteId )
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;

	    this.setSiteStatus(new ProbeStatus("UNDEFINED"));

	    this.activityLogger=new ActivityLogger(this.parameterBag,this.db);

	    this.siteId=siteId;

	    this.load();

	    //this.list = new ListOfPrimitiveServices(parameterBag,db);

	    //this.list.addBySiteName(siteName);
	}
    public void unpackResultSet(ResultSet rs){
	try{
	    while(rs.next()){
		this.setSiteId(rs.getInt("siteId"));
		this.setSiteName(rs.getString("SiteName"));
		this.setReadOnly(rs.getBoolean("ReadOnly"));
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error occured when unpacking result set");
	    System.out.println(getClass().getName()+" "+e);
	}	
    }
    public void unpackParameterBag(){
	this.setSiteName(this.parameterBag.siteName);
    }
    // links
    public HtmlLink  getLinkToAlarmsForSitePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Site Alarms")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSiteId())  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToSiteServices(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Site Services")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSiteId())  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToListOfSites(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Manipulate Sites")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToDisplaySitePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Display Site")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSiteId())  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToCreateSitePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create Site")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToConfirmDeleteSitePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Confirm Delete Site")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSiteId())  );

	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToDeleteSitePage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Delete Site")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSiteId())  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToAddRemoveHostsPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Add Remove Hosts to Site")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSiteId())  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    // get and set methods
    public ProbeStatus getSiteStatus(){
	return this.siteStatus;
    }
    public void setSiteStatus(ProbeStatus inputVar){
	this.siteStatus=inputVar;
    }
    public void updateSiteStatus(ProbeStatus inputVar){
	if(inputVar.statusLevel()>this.siteStatus.statusLevel()){
	    this.siteStatus=inputVar;
	}
    }
    public void setSiteDescription(String inputVar){
	this.siteDescription=inputVar;
    }
    public String getSiteDescription(){
	return this.siteDescription;
    }
    public void setSiteName(String inputVar){
	this.siteName=inputVar;
    }
    public String getSiteName(){
	return this.siteName;
    }
    public void setSiteId(int inputVar){
	this.siteId=inputVar;
    }
    public int getSiteId(){
	return this.siteId;
    }
    public void setReadOnly(boolean inputVar){
	this.readOnly=inputVar;
    }
    public boolean getReadOnly(){
	return this.readOnly;
    }
    //========
    public List<Integer> getListOfHostIds(){
	// get list of hostid's for this site
	List<Integer> resultList = new ArrayList<Integer>();
	resultList.clear();

	String query = "SELECT * FROM SitesHosts where siteId=?";
	PreparedStatement queryPS=null;
	ResultSet rs=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getSiteId());
	    rs=queryPS.executeQuery();
	    while(rs.next()){
		int hostId=rs.getInt("hostId");
		resultList.add(new Integer(hostId));
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load hostId by query");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" siteId="+this.getSiteId());	    
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    rs.close();
	    queryPS.close();
	}catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement or result set");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	}

	return resultList;
    }
    public void getHostsInfo(){
	ListOfPerfSonarHosts listOfHosts=new ListOfPerfSonarHosts(this.parameterBag,this.db);
	List<PerfSonarHost> listOfAllHosts=listOfHosts.getList();
	Iterator itr=listOfAllHosts.iterator();
	ProbeStatus ps=new ProbeStatus("UNDEFINED");
	while(itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    if(currentHost.hostBelongsToSite(this.getSiteId())){
		this.hostsInThisSite.add(currentHost);
		this.updateSiteStatus(currentHost.getHostPrimitivesStatus());
	    }else{
		this.hostsNotInThisSite.add(currentHost);
	    }
	}
    }
    public void fillHostsStatusInfo(){
	Iterator itr=	hostsInThisSite.iterator();
	while(itr.hasNext()){
	    PerfSonarHost perfSonarHost=(PerfSonarHost)itr.next();

	    perfSonarHost.fillListsOfPrimitiveServicesRecords();

	    perfSonarHost.fillPrimitiveStatus();

	    this.updateSiteStatus(perfSonarHost.getHostPrimitivesStatus());
	}
    }

    // === save,delete,insert ...
    public boolean delete(){
	boolean result=true;

	if(!this.getReadOnly()){
	    activityLogger.log("delete site id="+this.getSiteId());
	    
	    
	    String query="DELETE FROM Sites WHERE siteId=?";
	    PreparedStatement queryPS=null;
	    try{
		queryPS=db.getConn().prepareStatement(query);
		queryPS.setInt(1,this.getSiteId());
		queryPS.executeUpdate();
		activityLogger.log("deleted site id="+this.getSiteId());
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting site id="+this.getSiteId());
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
		result=false;
	    }
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }
	    // delete host dependencies
	    String query2="DELETE FROM SitesHosts where siteId=?";
	    PreparedStatement query2PS=null;
	    try{
		query2PS=db.getConn().prepareStatement(query2);
		query2PS.setInt(1,this.getSiteId());
		query2PS.executeUpdate();
		activityLogger.log("deleted hosts dependencies for site id="+this.getSiteId());
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting host dependencies for site id="+this.getSiteId());
		System.out.println(getClass().getName()+" "+query2);
		System.out.println(getClass().getName()+" "+e);
		result=false;
	    }	
	     try{
		query2PS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+query2);
		System.out.println(getClass().getName()+" "+e);
		result=false;
	    }
	}else{
	    System.out.println(getClass().getName()+" site "+this.getSiteName()+" is read only, I cannot delete it");
	    result=false;
	}
	return result;
    }
    public void save(){
	String query="UPDATE Sites SET SiteName=?,ReadOnly=?,Description=?  WHERE siteId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.getSiteName());
	    queryPS.setBoolean(2,this.getReadOnly());
	    queryPS.setString(3,this.getSiteDescription());
	    queryPS.executeUpdate();
	    activityLogger.log("updated site id="+this.getSiteId());
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating site id="+this.getSiteId());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}	
    }
    public void load(){
	String query="SELECT * FROM Sites WHERE siteId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getSiteId());
	    ResultSet rs=queryPS.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when creating site id="+this.getSiteId());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}	
    }
    public boolean insert(){
	boolean result=true;
	String query="INSERT INTO Sites (SiteName,ReadOnly,Description) VALUES(?,?,?)";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.getSiteName());
	    queryPS.setBoolean(2,this.getReadOnly());
	    queryPS.setString(3,this.getSiteDescription());
	    queryPS.executeUpdate();
	    activityLogger.log("created site id="+this.getSiteId());
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when inserting site name="+this.getSiteName());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	// get the index of the inserted object
	try{
	    ResultSet rs=queryPS.getGeneratedKeys();
	    while (rs.next ()){
		this.setSiteId(rs.getInt(1));
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not obtain key of newly inserted site");
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }

    public HtmlTableCell shortStatusCell(){


	HtmlLink link=this.getLinkToSiteServices(this.getSiteName());


	this.getHostsInfo();

	this.fillHostsStatusInfo();
	HtmlTableCell cell=new HtmlTableCell(link, this.getSiteStatus().color());

	return cell;	
    }

    public String  servicesGroupedByHost(){

	this.getHostsInfo();

	String result="<H1>Detailed status of site "+this.getSiteName()+"</H1><br>";

	Iterator itr = hostsInThisSite.iterator();
	while(itr.hasNext()){
	    PerfSonarHost perfsonarHost=(PerfSonarHost)itr.next();
	    HtmlLink link=new HtmlLink(perfsonarHost.getExternalUrl()  ,  perfsonarHost.getHostName()); 
	    result=result+"<strong>Host: "+link.toHtml()+"</strong><br>";
	    HtmlTable htmlTable = perfsonarHost.tableOfPrimitiveServices();
	    result=result+htmlTable.toHtml();
	}

	return result;
    }


    public  List<HtmlTableCell>infoTableRow(){
	List<HtmlTableCell> result=new ArrayList<HtmlTableCell>();
	HtmlTableCell cell1=new  HtmlTableCell(this.getLinkToDisplaySitePage(this.getSiteName()));
	cell1.alignLeft();
	result.add(cell1);

	HtmlTableCell cell3=new  HtmlTableCell(this.getLinkToConfirmDeleteSitePage("delete site"));
	cell3.alignLeft();
	result.add(cell3);

	HtmlTableCell cell4=new  HtmlTableCell(this.getLinkToAddRemoveHostsPage("add/remove hosts"));
	cell4.alignLeft();
	result.add(cell4);

	return result;
    }
    public String createNewSiteForm(){
	String result="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">" ;
	HtmlTable formTable=new HtmlTable(2);
	formTable.addCell("Site Name");
	formTable.addCell("<input type=\"text\" name=\"siteName\" value=\""+this.getSiteName()+"\" /><br>");

	result=result+formTable.toHtml();

	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Insert New Site")+"\" />";
	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	return result;
    }
    public String createNewSitePage(){
	String result="<H1>Create New PerfSonarSite</H1>";

	result=result+"<br>";
	result=result+this.createNewSiteForm();

	return result;
    }

    public String insertNewSitePage(String newSiteName){
	String result="<H1>New PerfSonarSite</H1>";
	result=result+"<br>";

	this.unpackParameterBag();

	this.setSiteName(newSiteName);

	boolean insertResult=this.insert();
	if(insertResult){
	    result=result+"<strong>Site "+this.getSiteName()+" has been created</strong>";
	}else{
	    result=result+"<strong>Failed to create site "+this.getSiteName()+" </strong>"; 
	}
	result=result+"<br>";
	result=result+this.getLinkToListOfSites("<strong>Click here to return to the list of sites</strong>");
	return result;
    }

    public String deleteSitePage(){
	String result="<H1>The PerfSonarSite "+this.getSiteName()+" is being deleted deleted...</H1>";
	result=result+"<br>";
	boolean deleteResult=true;
	if(!this.getReadOnly()){
	    deleteResult=this.delete();
	}else{
	    deleteResult=false;
	    result=result+"<strong>The site "+this.getSiteName()+" is read only.</strong><br><br>";
	}
	if(deleteResult){
	    result=result+"<strong>... done!!!</strong>";
	}else{
	    result=result+"<strong>... failed to delete the site, check the logs!!!</strong>";
	}
	result=result+"<br>";
	result=result+this.getLinkToListOfSites("<strong>Click here to return to the list of sites</strong>");	
	return result;
    }
    public String confirmDeleteSitePage(){
	String result="<H1>The PerfSonarSite "+this.getSiteName()+" will be deleted</H1>";
	result=result+"<br>";

	result=result+this.getLinkToListOfSites("<strong>Click here to return to the list of sites</strong>");

	result=result+"<br>or<br>";
	
	result=result+this.getLinkToDeleteSitePage("<strong>Click here to delete this site</strong>")+"<br>";

	result=result+"<br>";
	return result;
    }
    private String addHostsForm(){
	String form="<form>";

	List<PerfSonarHost>temporaryList=new ArrayList<PerfSonarHost>();

	Iterator itr1=this.hostsNotInThisSite.iterator();
	while (itr1.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr1.next();
	    currentHost.setSortingCriteria("hostName");
	    temporaryList.add(currentHost);
	}
	Collections.sort(temporaryList);

	Iterator itr=temporaryList.iterator();
	while (itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    String hostName=currentHost.getHostName();
	    int hostId=currentHost.getId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+hostId+"\">"+hostName+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add Hosts to Site")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getSiteId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Add Selected Hosts\" /><br>";
	form=form+"</form>";
	return form;
    }

    private String removeHostsForm(){
	String form="<form>";
	List<PerfSonarHost>temporaryList=new ArrayList<PerfSonarHost>();

	Iterator itr1=this.hostsInThisSite.iterator();
	while (itr1.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr1.next();
	    currentHost.setSortingCriteria("hostName");
	    temporaryList.add(currentHost);
	}
	Collections.sort(temporaryList);

	Iterator itr=temporaryList.iterator();
	while (itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    String hostName=currentHost.getHostName();
	    int hostId=currentHost.getId();
	    form=form+"<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+hostId+"\">"+hostName+"<br>";
	}
	form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Remove Hosts from Site")+"\" />";
	form=form+"<input type=\"hidden\" name=\"id\" value=\""+this.getSiteId()+"\" />";
	form=form+"<input type=\"submit\" value=\"Remove Selected Hosts\" /><br>";
	form=form+"</form>";
	return form;
    }
    private HtmlTable addRemoveHostsTable(){
	this.getHostsInfo();
	HtmlTable ht=new HtmlTable(2);

	ht.addCell("Hosts in Site");
	ht.addCell("Hosts not in Site");

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
    public String addRemoveHostsPage(){
	String result="<H1>Add or Remove Hosts to the PerfSonar Site "+this.getSiteName()+" </H1>";
	result=result+"<br>";
	result=result+this.addRemoveHostsTable().toHtml();
	result=result+"<br>";
	result=result+this.getLinkToListOfSites("<strong>Click here to return to the list of sites</strong>");
	return result;
    }

    public void addHosts(List<PerfSonarHost>listOfHosts){

	activityLogger.log("add hosts to site id="+this.getSiteId());

	String query="INSERT INTO SitesHosts (hostId,siteId) VALUES(?,?)";

	Iterator itr=listOfHosts.iterator();
	while(itr.hasNext()){
	    PerfSonarHost currentHost=(PerfSonarHost)itr.next();
	    PreparedStatement queryPS= null;
	    try{
		queryPS= db.getConn().prepareStatement(query);
		queryPS.setInt(1,currentHost.getId());
		queryPS.setInt(2,this.getSiteId());
		queryPS.executeUpdate();
		activityLogger.log("host "+currentHost.getHostName()+" added to site "+this.getSiteName()+" id="+this.getSiteId());
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when inserting host id and site id into SitesHosts");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" host id="+currentHost.getId());
		System.out.println(getClass().getName()+" site id="+this.getSiteId());
		System.out.println(getClass().getName()+" "+e);
	    }
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when closing prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);
	    }
	}
    }

    public void removeHosts(List<PerfSonarHost>listOfHosts){
	if(!this.getReadOnly()){

	    activityLogger.log("remove hosts from site "+this.getSiteName()+" id="+this.getSiteId());

	    String query="DELETE FROM SitesHosts WHERE hostId=? and siteId=?";
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
		    queryPS.setInt(2,this.getSiteId());
		    queryPS.executeUpdate();
		}catch (Exception e){
		    System.out.println(getClass().getName()+" error occured when deleting host id and site id from SitesHosts");
		    System.out.println(getClass().getName()+" "+query);
		    System.out.println(getClass().getName()+" host id="+currentHost.getId());
		    System.out.println(getClass().getName()+" site id="+this.getSiteId());
		    System.out.println(getClass().getName()+" "+e);
		    hostSuccesfullyRemoved=false;
		}
		try{
		queryPS.close();
		}catch (Exception e){
		    System.out.println(getClass().getName()+" error occured when closing prepared statement");
		    System.out.println(getClass().getName()+" "+query);
		    System.out.println(getClass().getName()+" "+e);
		}
		
		if(hostSuccesfullyRemoved){
		    activityLogger.log("host "+currentHost.getHostName()+" removed site "+this.getSiteName()+" id="+this.getSiteId());
		}else{
		    activityLogger.log("error when removing host "+currentHost.getHostName()+" from site "+this.getSiteName()+" id="+this.getSiteId());
		}
		
	    }
	}else{
	    System.out.println(getClass().getName()+" Cannot remove hosts, site is read only. sitename="+this.getSiteName());   
	}
    }


    public String addHostsPage(){
	String result="<H1>Add Hosts to the PerfSonar Site "+this.getSiteName()+" </H1>";
	result=result+"<br>";

	if(!this.getReadOnly()){
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
	}else{
	    result=result+"<strong>Site is read only, you cannot add hosts</strong>";
	}
	
	result=result+"<br>";
	result=result+this.getLinkToListOfSites("<strong>Click here to return to the list of sites</strong>");
	return result;
    }
    public String removeHostsPage(){
	String result="<H1>remove Hosts from the PerfSonar Site "+this.getSiteName()+" </H1>";
	result=result+"<br>";

	if(!this.getReadOnly()){
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
	}else{
	    result=result+"<strong>Site is read only, you cannot remove hosts</strong>";
	}
	result=result+"<br>";
	result=result+this.getLinkToListOfSites("<strong>Click here to return to the list of sites</strong>");
	return result;
    }

    public HtmlTableCell shortAlarmStatusCell(){
	HtmlTableCell cell = new HtmlTableCell(this.getLinkToAlarmsForSitePage(this.getSiteName()));
	return cell;
    }
    public HtmlTable shortAlarmStatusTable(){
	HtmlTable ht = new HtmlTable(1);
	ht.addCell(this.shortAlarmStatusCell());
	return ht;
    }
    
    public String alarmsForSitePage(){
	String result="";
	result="<H2>Alarms for site "+this.getSiteName()+"</H2>";
	return result;
    }

    public List<Integer> getListOfIdsOfPrimitiveServicesFromThisSite(){
	// return list of dbid's for primitive services for this site
	List<Integer> resultList=new ArrayList<Integer>();
	resultList.clear();

	String query="SELECT * FROM PrimitiveServicesHosts WHERE hostId=?";
	PreparedStatement queryPS=null;
	int hostId=-1;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    Iterator iter=this.getListOfHostIds().iterator();
	    while(iter.hasNext()){
		Integer hostIdInteger=(Integer)iter.next();
		hostId = hostIdInteger.intValue();

		queryPS.setInt(1,hostId);

		ResultSet rs=queryPS.executeQuery();

		while(rs.next()){
		    int dbid=rs.getInt("dbid");
		    resultList.add(new Integer(dbid));
		}
		rs.close();
	    }
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load service ids for host");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" hostId="+hostId);	    
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	}
	return resultList;
    }



    public int compareTo(Object obj) {
	PerfSonarSite otherSite=(PerfSonarSite)obj;
	String thisSiteName=getSiteName().replace("BNL","AAAAABNL");
	String otherSiteName=otherSite.getSiteName().replace("BNL","AAAABNL");
	return thisSiteName.compareTo(otherSiteName);
    }
    

}
