package gov.bnl.racf.exda;
import java.io.*;
import java.sql.*;
import java.lang.Class;
public class ActivityLogger
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private String userDn="unknown";

    private String hostName="";

    public ActivityLogger(ParameterBag inputParameterBag,DbConnector inputDb)
	{
	    this.parameterBag=(ParameterBag)inputParameterBag.clone();
	    this.db=inputDb;
	    this.userDn=this.parameterBag.user.getDN();
	}
 

    public void log(String logMessage){

	// log the activity of the host
	String logCommand="INSERT INTO ActionHistory (MetricName,HostName,dn,action,Timestamp) VALUES(?,?,?,?,NOW())";
	PreparedStatement insertLogCommand=null;
	try{
	    insertLogCommand=db.getConn().prepareStatement(logCommand);
	    insertLogCommand.setString(1,"");
	    insertLogCommand.setString(2,"");
	    insertLogCommand.setString(3,this.userDn);
	    insertLogCommand.setString(4,logMessage);
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

}

