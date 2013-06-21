/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.exceptionlogmanager;

import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class ExceptionRecord {
    private Date time;
    private String message;
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
    public ExceptionRecord(Date time, String classsName, Exception e){
        this.time=time;
        this.className=classsName;
        this.message=e.toString();
    }
    public ExceptionRecord(JSONObject json){
        this.time=(Date)json.get("time");
        this.className=(String)json.get("className");
        this.message=(String)json.get("message");
    }
    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("time",this.getTime());
        json.put("message",this.getMessage());
        json.put("className",this.getClassName());
        return json;
    }
    
    
}
