package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.Collections;



public class Scheduler implements Comparable
{  
    private ParameterBag parameterBag = null;
    private DbConnector db=null;
    
    private String schedulerName=null;
    private String schedulerIp="";
    private int schedulerId=-1;
    private String schedulerHost="";
    private boolean readOnly=false;

    public Scheduler(ParameterBag paramBag,  DbConnector inputDb){
	this.parameterBag=paramBag;
	this.db=inputDb;
	this.setReadOnly(false);
    }
    public Scheduler(ParameterBag paramBag,  DbConnector inputDb,String dummyInput, String dummyInput2,String schedulerIp ){
	this.parameterBag=paramBag;
	this.db=inputDb;
	
	this.setSchedulerIp(schedulerIp);

	String query="SELECT SchedulerId from Schedulers where SchedulerIp=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.getSchedulerIp());
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		this.setSchedulerId(rs.getInt("SchedulerId"));
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when obtaining id of scheduler on host="+this.getSchedulerHost());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}finally{
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }
	}	
	
	this.load();
    }
    public Scheduler(ParameterBag paramBag,  DbConnector inputDb,String dummyInput, String schedulerHost ){
	this.parameterBag=paramBag;
	this.db=inputDb;
	
	this.setSchedulerHost(schedulerHost);

	String query="SELECT SchedulerId from Schedulers where SchedulerHost=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.getSchedulerHost());
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		this.setSchedulerId(rs.getInt("SchedulerId"));
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when obtaining id of scheduler on host="+this.getSchedulerHost());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}finally{
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }
	}	
	
	this.load();
    }

    public Scheduler(ParameterBag paramBag,  DbConnector inputDb,String schedulerName ){
	this.parameterBag=paramBag;
	this.db=inputDb;
	
	this.setSchedulerName(schedulerName);

	String query="SELECT SchedulerId from Schedulers where SchedulerName=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.getSchedulerName());
	    ResultSet rs=queryPS.executeQuery();
	    while(rs.next()){
		this.setSchedulerId(rs.getInt("SchedulerId"));
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when obtaining id of scheduler name="+this.getSchedulerName());
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);
	}finally{
	    try{
		queryPS.close();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }
	}	
	
	this.load();
    }
    public Scheduler(ParameterBag paramBag,  DbConnector inputDb,int schedulerId ){
	this.parameterBag=paramBag;
	this.db=inputDb;
	
	this.setSchedulerId(schedulerId);
	
	this.load();
    }

    public void unpackParameterBag(){
	this.setSchedulerName(this.parameterBag.schedulerName);
	this.setSchedulerHost(this.parameterBag.hostName);
	this.setSchedulerIp(this.parameterBag.ip);
    }


    public void unpackResultSet(ResultSet rs){
	try{
	    while(rs.next()){
		this.setSchedulerId(rs.getInt("schedulerId"));
		this.setSchedulerName(rs.getString("SchedulerName"));
		this.setSchedulerHost(rs.getString("SchedulerHost"));
		this.setSchedulerIp(rs.getString("SchedulerIp"));
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
	String query="SELECT * FROM Schedulers WHERE schedulerId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getSchedulerId());
	    ResultSet rs=queryPS.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when loading scheduler id="+this.getSchedulerId());
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
	String insertCommand="INSERT INTO Schedulers (SchedulerName,SchedulerHost,SchedulerIp,ReadOnly) VALUES(?,?,?)";
	PreparedStatement insertCommandPS=null;
	try{
	    insertCommandPS=db.getConn().prepareStatement(insertCommand);
	    insertCommandPS.setString(1,this.getSchedulerName());
	    insertCommandPS.setString(2,this.getSchedulerHost());
	    insertCommandPS.setString(3,this.getSchedulerIp());
	    insertCommandPS.setBoolean(4,this.getReadOnly());
	    insertCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when inserting scheduler name="+this.getSchedulerName());
	    System.out.println(getClass().getName()+" "+insertCommand);
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	// get the index of the inserted object
	try{
	    ResultSet rs=insertCommandPS.getGeneratedKeys();
	    while (rs.next ()){
		this.setSchedulerId(rs.getInt(1));
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not obtain key of newly inserted scheduler");
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
	    String deleteCommand="DELETE FROM Schedulers WHERE schedulerId=?";
	    PreparedStatement deleteCommandPS=null;
	    try{
		deleteCommandPS=db.getConn().prepareStatement(deleteCommand);
		deleteCommandPS.setInt(1,this.getSchedulerId());
		deleteCommandPS.executeUpdate();
	    }catch (Exception e){
		System.out.println(getClass().getName()+" error occured when deleting scheduler id="+this.getSchedulerId()+" schedulerName="+this.getSchedulerName());
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
	    System.out.println(getClass().getName()+" cannot delete scheduler id="+this.getSchedulerId()+" schedulerName="+this.getSchedulerName());
	    System.out.println(getClass().getName()+" readOnly="+this.getReadOnly());
	    result=false;
	}
	return result;
    }
    public boolean save(){
	boolean result=true;
	String saveCommand="UPDATE Schedulers SET SchedulerName=?,SchedulerHost=?,ReadOnly=?,SchedulerIp=? WHERE schedulerId=?";
	PreparedStatement saveCommandPS=null;
	try{
	    saveCommandPS=db.getConn().prepareStatement(saveCommand);
	    saveCommandPS.setString(1,this.getSchedulerName());
	    saveCommandPS.setString(2,this.getSchedulerHost());

	    saveCommandPS.setBoolean(3,this.getReadOnly());
	    saveCommandPS.setString(4,this.getSchedulerIp());
	    saveCommandPS.setInt(5,this.getSchedulerId());
	    saveCommandPS.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when saving scheduler id="+this.getSchedulerId());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    saveCommandPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+saveCommand);
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }
    // --- links 
    public HtmlLink getLinkToManipulateSchedulersPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Manipulate Schedulers")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSchedulerId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToConfirmDeleteSchedulerPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Confirm Delete Scheduler")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSchedulerId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToDeleteSchedulerPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Delete Scheduler")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSchedulerId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink  getLinkToEditSchedulerPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Edit Scheduler")  );
	paramBagLocal.addParam("id",Integer.toString(this.getSchedulerId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }


    // --- get and set methods
    public String getSchedulerIp(){
	return this.schedulerIp;
    }
    public void setSchedulerIp(String inputVar){
	this.schedulerIp=inputVar;
    }
    public String getSchedulerName(){
	return this.schedulerName;
    }
    public void setSchedulerName(String inputVar){
	this.schedulerName=inputVar;
    }
    public int getSchedulerId(){
	return this.schedulerId;
    }
    public void setSchedulerId(int inputVar){
	this.schedulerId=inputVar;
    }
    public boolean getReadOnly(){
	return this.readOnly;
    }
    public void setReadOnly(boolean inputVar){
	this.readOnly=inputVar;
    }
    public String getSchedulerHost(){
	return this.schedulerHost;
    }
    public void setSchedulerHost(String inputVar){
	this.schedulerHost=inputVar;
    }




    public  List<HtmlTableCell>infoTableRow(){
	List<HtmlTableCell> result=new ArrayList<HtmlTableCell>();

	HtmlTableCell cell1=new  HtmlTableCell(this.getSchedulerName());
	cell1.alignLeft();
	result.add(cell1);

	HtmlTableCell cell2=new  HtmlTableCell(this.getLinkToEditSchedulerPage("Edit"));
	cell2.alignLeft();
	result.add(cell2);

	HtmlTableCell cell3=new  HtmlTableCell(this.getLinkToConfirmDeleteSchedulerPage("Delete"));
	cell3.alignLeft();
	result.add(cell3);

	return result;
    }

    public String confirmSchedulerDeletePage(){
	String result="<H2>Confirm Delete PerfSonar Scheduler</h2>";
	result=result+"<br>";
	result=result+"<strong>The scheduler "+this.getSchedulerName()+" id="+this.getSchedulerId()+" will be deleted</strong><br>";
	result=result+this.getLinkToDeleteSchedulerPage("<strong>Click here to delete it</strong>");
	result=result+" or ";
	result=result+this.getLinkToManipulateSchedulersPage("<strong>Click here to go to the list of schedulers</strong>");
	return result;						    
    }

    public String createSchedulerForm(){
	String result="<form>";
	HtmlTable formTable=new HtmlTable(2);

	formTable.addCell("Scheduler Name");
	formTable.addCell("<input type=\"text\" name=\"schedulerName\" value=\""+this.getSchedulerName()+"\" /><br>");


	formTable.addCell("Scheduler Host");
	formTable.addCell("<input type=\"text\" name=\"hostName\" value=\""+this.getSchedulerHost()+"\" /><br>");

	formTable.addCell("Scheduler Ip");
	formTable.addCell("<input type=\"text\" name=\"ip\" value=\""+this.getSchedulerIp()+"\" /><br>");

	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Insert New Scheduler")+"\" />";

	result=result+"<br>"+formTable.toHtml()+"<br>";

	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	
	return result;
    }
    public String editSchedulerForm(){
	String result="<form>";
	HtmlTable formTable=new HtmlTable(2);

	formTable.addCell("Scheduler Name");
	formTable.addCell("<input type=\"text\" name=\"schedulerName\" value=\""+this.getSchedulerName()+"\" /><br>");


	formTable.addCell("Scheduler Host");
	formTable.addCell("<input type=\"text\" name=\"hostName\" value=\""+this.getSchedulerHost()+"\" /><br>");

	formTable.addCell("Scheduler Ip");
	formTable.addCell("<input type=\"text\" name=\"ip\" value=\""+this.getSchedulerIp()+"\" /><br>");

	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save Scheduler")+"\" />";
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+this.getSchedulerId()+"\" />";

	result=result+"<br>"+formTable.toHtml()+"<br>";

	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	
	return result;
    }
    public String createSchedulerPage(){
	String result="<H2>Create New PerfSonar Scheduler</h2>";
	result=result+"<br>";
	result=result+this.createSchedulerForm();
	return result;
    }
    public String editSchedulerPage(){
	String result="<H2>Edit PerfSonar Scheduler</h2>";
	result=result+"<br>";
	result=result+this.editSchedulerForm();
	return result;
    }
    public String insertNewScheduler(){
	String result="";
	boolean insertResult=this.insert();
	if(insertResult){
	    result="New scheduler has been succesfully created";
	}else{
	    result="Error occured while creating new scheduler. Please check the logs<br>";
	}
	return result;
    }
    public String insertNewSchedulerPage(){
	String result="<H2>Insert New PerfSonar Scheduler</h2>";
	result=result+"<br>";
	result=result+this.insertNewScheduler();
	result=result+"<br><br>";
	result=result+this.getLinkToManipulateSchedulersPage("<strong>Click here to go to the list of schedulers</strong>");
	return result;
    }
    public String saveScheduler(){
	boolean saveResult=this.save();
	String result="<br>"+saveResult+"<br>";
	if(saveResult){
	    result="Scheduler saved<br>";
	}else{
	    result="Error occures when saving scheduler, check the logs<br>";
	}
	return result;
    }
    public String saveSchedulerPage(){
	String result="<H2>Save PerfSonar Scheduler</h2>";
	result=result+"<br>";
	result=result+this.saveScheduler();
	result=result+"<br><br>";
	result=result+this.getLinkToManipulateSchedulersPage("<strong>Click here to go to the list of schedulers</strong>");
	return result;
    }
    public String deleteSchedulerPage(){
	String result="<H2>Delete PerfSonar Scheduler "+this.getSchedulerName()+"</h2>";
	result=result+"<br>";
	if(this.delete()){
	    result=result+"The scheduler has been deleted";
	}else{
	    result=result+"The delete operation failed! Check the logs";
	}
	result=result+"<br><br>";
	result=result+this.getLinkToManipulateSchedulersPage("<strong>Click here to go to the list of schedulers</strong>");
	return result;
    }
    public String confirmDeleteSchedulerPage(){
	String result="<H2>The PerfSonar Scheduler "+this.getSchedulerName()+" will be deleted!</h2>";
	result=result+"<br>";
	result=result+"<strong>"+this.getLinkToDeleteSchedulerPage("Click here to delete it")+"</strong>";
	result=result+"<br>or<br>";
	result=result+this.getLinkToManipulateSchedulersPage("<strong>Click here to go to the list of schedulers</strong>");
	return result;
    }



    public int compareTo(Object obj) {
	Scheduler otherScheduler=(Scheduler)obj;
	String thisSchedulerName=this.getSchedulerName();
	String otherSchedulerName=otherScheduler.getSchedulerName();
	return thisSchedulerName.compareTo(otherSchedulerName);
    }
    

}
