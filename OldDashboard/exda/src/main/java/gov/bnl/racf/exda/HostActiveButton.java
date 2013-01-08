package gov.bnl.racf.exda;
import java.io.*;
import java.sql.*;
import java.lang.Class;
public class HostActiveButton
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;
    private boolean active=true;

    private String hostName="";

    public HostActiveButton(ParameterBag inputParameterBag,DbConnector inputDb,String hostNameInput)
	{
	    parameterBag=(ParameterBag)inputParameterBag.clone();
	    db=inputDb;
	    hostName=hostNameInput;
	}
 
    public String toHtml(){
	checkActiveNow();

	String form="";

	form="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">";	
	if (ParameterBag.variableNotEmpty(parameterBag.page)){
	    form = form + "<input type=\"hidden\" name=\"page\" value=\"" +parameterBag.pageAddress("PerfSonar Host Active Form")+  "\">";
	}
	if (ParameterBag.variableNotEmpty(parameterBag.hostName)){
	    form = form + "<input type=\"hidden\" name=\"hostName\" value=\"" +hostName+  "\">";
	}
	if(active){
	    form = form + "<input type=\"hidden\" name=\"toggleActive\" value=\"Y\">";
	    form=form+"<input type=\"submit\" value=\"Mark host as inactive\" />";
	}else{
	    form = form + "<input type=\"hidden\" name=\"toggleActive\" value=\"Y\">";
	    form=form+"<input type=\"submit\" value=\"Mark host as active\" />";
	}
	form = form + "</form> ";
	
	return form;
    }
    private void checkActiveNow(){
	// check if service is currently active

	// first of all: is the service active?
	String getActiveQuery="SELECT Active FROM Hosts WHERE HostName=? ";	    
	String isActive=null;
	PreparedStatement getActiveSql=null;
	try{
	    getActiveSql=db.getConn().prepareStatement(getActiveQuery);
	    getActiveSql.setString(1,parameterBag.hostName);
	    ResultSet rs=getActiveSql.executeQuery();
	    while(rs.next()){
		this.active=rs.getBoolean("Active");
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to get Active info about HostName "+parameterBag.hostName);
	    System.out.println(getClass().getName()+" "+getActiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getActiveSql.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+getActiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
    }
    private boolean active2boolean(String Active){
	if (Active.equals("Y")){
	    return true;
	}else{
	    return false;
	}
    }

    public boolean isActive(){
	return active;
    }
    public void toggleActiveNow(){
	checkActiveNow();

	boolean newIsactive=!this.active;
	// update host status
	String updateIsactiveQuery="UPDATE Hosts  SET Active=? WHERE HostName=? ";
	PreparedStatement updateIsactiveSql=null;
	try{
	    updateIsactiveSql=db.getConn().prepareStatement(updateIsactiveQuery);
	    updateIsactiveSql.setBoolean(1,newIsactive);
	    updateIsactiveSql.setString(2,hostName);
	    updateIsactiveSql.executeUpdate();
	    this.active=newIsactive;
	    //System.out.println(getClass().getName()+" Swap Active ");
	    //System.out.println(getClass().getName()+" hostName= "+hostName);
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating active");
	    System.out.println(getClass().getName()+" "+updateIsactiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    updateIsactiveSql.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+updateIsactiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}	    

	// update primitive services
	updateIsactiveQuery="UPDATE Services  SET Active=? WHERE PrimitiveService='Y' and ( HostName=? or HostName2=? or HostName3=?)";
	PreparedStatement updateIsactiveSql2=null;
	try{
	    updateIsactiveSql2=db.getConn().prepareStatement(updateIsactiveQuery);
	    updateIsactiveSql2.setBoolean(1,newIsactive);
	    updateIsactiveSql2.setString(2,hostName);
	    updateIsactiveSql2.setString(3,hostName);
	    updateIsactiveSql2.setString(4,hostName);
	    updateIsactiveSql2.executeUpdate();
	    this.active=newIsactive;
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating active");
	    System.out.println(getClass().getName()+" "+updateIsactiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    updateIsactiveSql2.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+updateIsactiveQuery);
	    System.out.println(getClass().getName()+" "+e);
	}	    

	// log the activity of the host
	String logCommand="INSERT INTO ActionHistory (MetricName,HostName,dn,action,Timestamp) VALUES(?,?,?,?,NOW())";
	PreparedStatement insertLogCommand=null;
	try{
	    insertLogCommand=db.getConn().prepareStatement(logCommand);
	    insertLogCommand.setString(1,parameterBag.serviceName);
	    insertLogCommand.setString(2,parameterBag.hostName);
	    insertLogCommand.setString(3,parameterBag.user.getDN());
	    insertLogCommand.setString(4,"Active set to "+newIsactive);
	    insertLogCommand.executeUpdate();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when updating activity log file");
	    System.out.println(getClass().getName()+" "+logCommand);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    insertLogCommand.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+logCommand);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }    

    public String toString(){
	String result=toHtml();
	return result;
    }
}

