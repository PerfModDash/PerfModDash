package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.Collections;

import java.util.Date;


public class AdminPage
{  
    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private int id=0;
    private String pageContent="<br><br>";


    public AdminPage(ParameterBag paramBag,  DbConnector inputDb, int id){
	this.parameterBag=paramBag;
	this.db=inputDb;
	this.id=id;

	// if(this.id==0){
	//     this.fillMainAdminPageWithLinks();
	// }
	if(this.id==1){
	    this.pageContent="<br>Reset all probes <br><br>";
	}
	if(this.id==2){
	    this.pageContent="<br><strong>Reset running probes</strong> <br><br>";
	    this.resetRunningProbes();
	}
	if(this.id==3){
	    this.pageContent="<br><strong>List running probes</strong> <br><br>";
	    this.listRunningProbes();
	}
	if(this.id==4){
	    this.pageContent="<br><strong>List overdue probes</strong> <br><br>";
	    this.listOverdueProbes();
	}
	if(this.id==5){
	    this.pageContent="<br><strong>Reset Selected Probes</strong> <br><br>";
	    this.resetSelectedProbes();
	}
	this.fillMainAdminPageWithLinks();
    }

    // --- links 
    public HtmlLink getLinkToResetSelectedProbesPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Admin Page")  );
	paramBagLocal.addParam("id","5" );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToResetAllProbesPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Admin Page")  );
	paramBagLocal.addParam("id","1" );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToResetRunningProbesPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Admin Page")  );
	paramBagLocal.addParam("id","2" );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToListRunningProbesPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Admin Page")  );
	paramBagLocal.addParam("id","3" );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public HtmlLink getLinkToListOverdueProbesPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Admin Page")  );
	paramBagLocal.addParam("id","4" );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    public void fillMainAdminPageWithLinks(){
	this.pageContent=this.pageContent+this.getLinkToResetAllProbesPage("<strong>Click here to reset all probes</strong>")+"<br><br>";
	this.pageContent=this.pageContent+this.getLinkToResetRunningProbesPage("<strong>Click here to reset running probes</strong>")+"<br><br>";
	this.pageContent=this.pageContent+this.getLinkToListRunningProbesPage("<strong>Click here to list running probes</strong>")+"<br><br>";
	this.pageContent=this.pageContent+this.getLinkToListOverdueProbesPage("<strong>Click here to list overdue probes</strong>")+"<br><br>";
    }


    private void resetSelectedProbes(){
	this.pageContent=this.pageContent+" The following probes will be reset:<BR>";

	String query="UPDATE Services SET ProbeRunning='N' where dbid=?";
	PreparedStatement queryPS=null;


	Iterator iter=	this.parameterBag.listOfIds.iterator();
	while(iter.hasNext()){
	    Integer dbidAsInteger=(Integer)iter.next();
	    int dbid=dbidAsInteger.intValue();
	    try{
		queryPS=db.getConn().prepareStatement(query);
		queryPS.setInt(1,dbid);
		queryPS.executeUpdate();
		this.pageContent=this.pageContent+dbid+"<BR>";
	    }catch(Exception e){
		System.out.println(new Date()+" "+getClass().getName()+" failed to execute query");
		System.out.println(new Date()+" "+getClass().getName()+" "+query);
		System.out.println(new Date()+" "+getClass().getName()+" dbid="+dbid);
		System.out.println(new Date()+" "+getClass().getName()+" e="+e);
	    }
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(new Date()+" "+getClass().getName()+" failed to close query");
	    System.out.println(new Date()+" "+getClass().getName()+" "+query);
	    System.out.println(new Date()+" "+getClass().getName()+" e="+e);
	}
	return ;
    }
    private void resetRunningProbes(){
	boolean resetResult=true;
	//String query="UPDATE Services SET ProbeRunning='N' WHERE ProbeRunning='Y'";
	String query="UPDATE Services SET ProbeRunning='N'";
	PreparedStatement queryPS=null;
	int numberOfProbesReset=-1;
	try{
	    queryPS=db.getConn().prepareStatement(query);
	    numberOfProbesReset=queryPS.executeUpdate();
	    Date date=new Date();
	    System.out.println(date+" "+getClass().getName()+" number of probes reset="+numberOfProbesReset);
	}catch(Exception e){
	    resetResult=false;
	    System.out.println(getClass().getName()+" failed to reset running probes");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);
	}finally{
	    try{
		queryPS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}
	if(resetResult){
	    this.pageContent=this.pageContent+"<br><strong>Runing probes have been reset</strong><br><br>";
	}else{
	    this.pageContent=this.pageContent+"<br><strong>Failed to reset the running probes, check the logs</strong><br><br>";
	}
    }

    private void listRunningProbes(){
	String query="SELECT dbid,LastCheckTime,NextCheckTime,ProbeRunning,SchedulerName,PrimitiveService FROM Services where ProbeRunning='Y' ORDER BY NextCheckTime ";
	this.listProbes(query);
    }
    private void listOverdueProbes(){
	String query="SELECT dbid,LastCheckTime,NextCheckTime,ProbeRunning,SchedulerName,PrimitiveService FROM Services where NextCheckTime<DATE_SUB(NOW(), INTERVAL 1 HOUR) ORDER BY NextCheckTime";
	this.listProbes(query);
    }


    public HtmlLink getLinkToProbeDetailPage(int dbid, boolean isPrimitive){
	String linkText = Integer.toString(dbid);
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	if(isPrimitive){
	    paramBagLocal.addParam("page",ParameterBag.pageAddress("perfSonar Primitive")  );
	}else{
	    paramBagLocal.addParam("page",ParameterBag.pageAddress("Matrix Element")  );
	}
	paramBagLocal.addParam("id",Integer.toString(dbid));
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    private void listProbes(String query){
	HtmlTable htmlTable=new HtmlTable(6);
	htmlTable.setBorder(0);
	htmlTable.setPadding(0);

	String form="<form>";
	form=form+"<input type=\"submit\" value=\"Reset Selected Probes\" /><br>";

	int numberOfRunningProbes=0;

	PreparedStatement queryPS=null;
	try{
	    queryPS=db.getConn().prepareStatement(query);

	    ResultSet rs=queryPS.executeQuery();
	     while(rs.next()){
		int dbid=rs.getInt("dbid");
		Timestamp lastCheckTime=rs.getTimestamp("LastCheckTime");
		Timestamp nextCheckTime=rs.getTimestamp("NextCheckTime");
		String probeRunning=rs.getString("ProbeRunning");
		String schedulerName=rs.getString("SchedulerName");
		String primitiveService=rs.getString("PrimitiveService");

		boolean isPrimitive=false;
		if(primitiveService.equals('y') || primitiveService.equals('Y')){
		    isPrimitive=true;
		}
		HtmlLink linkToDetails = this.getLinkToProbeDetailPage( dbid, isPrimitive ) ;

		htmlTable.addCell( "<INPUT NAME=\"listOfIds\" TYPE=\"CHECKBOX\" VALUE=\""+dbid+"\">"  );

		htmlTable.addCell(new HtmlTableCell(linkToDetails) );	

		//htmlTable.addCell(Integer.toString(dbid));
		htmlTable.addCell(lastCheckTime.toString());
		htmlTable.addCell(nextCheckTime.toString());
		htmlTable.addCell(probeRunning);
		htmlTable.addCell(schedulerName);
		numberOfRunningProbes=numberOfRunningProbes+1;
	     }
	     rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to obtain list of probes probes");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);
	}finally{
	    try{
		queryPS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}
	//this.pageContent=this.pageContent+"<br><br>"+query+"<br><br>";
	this.pageContent=this.pageContent+"Number of currently running probes="+numberOfRunningProbes+"<br><br>";
	if(numberOfRunningProbes>0){
	    form=form+htmlTable.toHtml();
	    //form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Reset Selected Probes Page")+"\" />";
	    form=form+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Admin Page")+"\" />";
	    form=form+"<input type=\"hidden\" name=\"id\" value=\""+ "5" +"\" />";
	    form=form+"</form>";
		
	    this.pageContent=this.pageContent+form;
	}
    }

    public String toHtml(){
	return this.pageContent;
    }
}
