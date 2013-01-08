package gov.bnl.racf.exda;

import java.sql.Timestamp;
import java.util.Date;
import java.sql.*;

public class AlarmHistoryRecord
{  
    private int id;
    private int alarmId;
    private Timestamp timestamp;
    private String Message;
    private boolean messageSent;
    private ProbeStatus status;
    private int consecutiveOK=0;
    private int consecutiveNotOK=0;
    private String comment = "";
    private String sendResult="";

    public AlarmHistoryRecord(){  }

    public AlarmHistoryRecord(ParameterBag parameterBag,DbConnector inputDb, int recordId){
	String query="SELECT * FROM AlarmHistory where id=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS= inputDb.getConn().prepareStatement(query);
	    queryPS.setInt(1,recordId);
	    ResultSet rs=queryPS.executeQuery();
	    this.unpackResultSet(rs);
	    rs.close();
	}catch(Exception e){
	    System.out.println((new Date())+" "+getClass().getName()+" failed to load alarm history record by query");
	    System.out.println((new Date())+" "+getClass().getName()+" query="+query);
	    System.out.println((new Date())+" "+getClass().getName()+" id="+recordId);	    
	    System.out.println((new Date())+" "+getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println((new Date())+" "+getClass().getName()+" failed to close prepared statement");
	    System.out.println((new Date())+" "+getClass().getName()+" "+query);
	    System.out.println((new Date())+" "+getClass().getName()+" "+e);	
	}	
    }

     private boolean unpackResultSet(ResultSet rs){
	boolean result=true;
	try{
	     while(rs.next()){
		 this.setId(rs.getInt("id"));
		 this.setAlarmId(rs.getInt("alarmId"));
		 this.setTimestamp(rs.getTimestamp("Timestamp"));
		 this.setMessage(rs.getString("Message"));
		 this.setMessageSent(rs.getBoolean("messageSent"));
		 this.setStatus( new ProbeStatus(rs.getString("status")));
		 this.setConsecutiveOK(rs.getInt("consecutiveOK"));
		 this.setConsecutiveNotOK(rs.getInt("consecutiveNotOK"));
		 this.setComment(rs.getString("comment"));
		 //this.setReceipients(rs.getString("receipients"));
		 this.setSendResult(rs.getString("sendResult"));
	     }
	}catch(Exception e){
	    result=false;
	    System.out.println((new Date())+" "+getClass().getName()+" error while unpacking result set");
	    System.out.println((new Date())+" "+getClass().getName()+" "+e);
	}	     
	return result;	
    }

    public void setId(int inputVar){
	this.id=inputVar;
    }
    public int getId(){
	return this.id;
    }
    public void setAlarmId(int inputVar){
	this.alarmId=inputVar;
    }
    public int getAlarmId(){
	return this.alarmId;
    }
    public void setTimestamp(Timestamp inputVar){
	this.timestamp=inputVar;
    }
    public Timestamp getTimestamp(){
	return this.timestamp;
    }    
    public void setMessage(String inputVar){
	this.Message=inputVar;
    }
    public String getMessage(){
	return this.Message;
    }  
    public void setMessageSent(boolean inputVar){
	this.messageSent=inputVar;
    }
    public boolean getMessageSent(){
	return this.messageSent;
    }  
    public void setStatus(ProbeStatus inputVar){
	this.status=inputVar;
    }
    public ProbeStatus getStatus(){
	return this.status;
    }    
    public void setConsecutiveOK(int inputVar){
	this.consecutiveOK=inputVar;
    }
    public int getConsecutiveOK(){
	return this.consecutiveOK;
    }       
    public void setConsecutiveNotOK(int inputVar){
	this.consecutiveNotOK=inputVar;
    }
    public int getConsecutiveNotOK(){
	return this.consecutiveNotOK;
    }  
    public void setComment(String inputVar){
	this.comment=inputVar;
    }
    public String getComment(){
	return this.comment;
    }     
    public void setSendResult(String inputVar){
	this.sendResult=inputVar;
    }
    public String getSendResult(){
	return this.sendResult;
    } 
    public String recordDetails(){
	String result="<strong>Status of history record </strong><BR>";
	result=result+"Time: "+this.getTimestamp()+"<BR>";
	result=result+this.getMessage().replace("\n","\n<BR>");
	return result;
    }
    public String messageDetails(){
	String result="<strong>Status alarm message:</strong><BR>";
	result=result+"Time: "+this.getTimestamp()+"<BR>";
	result=result+this.getSendResult().replace("\n","\n<BR>");
	return result;
    }

}
