/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitiveServ.bean;

/**
 * This class includes all attributes of result field in a primitive service.
 * Standard behaviors like the get and set methods for Result object properties are defined here.
 * @author siliu
 */
public class Result {
    
    private String message;
    private String id;
    private String job_id;
    private String time;
    private long status;
    private String service_id;
    private ResultParameters parameters;
    
    
    public String getMessage(){	
	return message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getId(){	
	return id;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    
    public String getJob_id(){
	return job_id;
    }
	
    public void setJob_id(String job_id){
        this.job_id = job_id;
    }
    
     public String getTime(){	
	return time;
    }
    
    public void setTime(String time){
        this.time = time;
    }
    
    public long getStatus(){	
	return status;
    }
    
    public void setStatus(long status){
        this.status = status;
    }
    
    
    public String getService_id(){
	return service_id;
    }
	
    public void setService_id(String service_id){
        this.service_id = service_id;
    }
    
    
    public ResultParameters getResultParameters(){	
	return parameters;
    }
    
    public void setResultParameters(ResultParameters parameters){
        this.parameters = parameters;
    }
    
    
    
}
