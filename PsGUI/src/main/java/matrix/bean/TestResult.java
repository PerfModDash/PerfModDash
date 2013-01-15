/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.bean;

/**
 * This class includes all basic attributes of the test result field in one matrix.
 * Standard behaviors like the get and set methods for Host object properties are defined here.
 * @author siliu
 */
public class TestResult {
    
    private String resultId;
    private String message;
    private String jobId;
    private String time;
    private long status;
    private String serviceId;
    private ResultParameters parameters; 
    
    
    public String getResultId(){
        return resultId;
    }
    
    public void setResultId(String resultId){
        this.resultId = resultId;
    }
	
    public String getMessage(){
	return message;
    }
    
    public void setMessage(String message){
	this.message = message;
    }
	
    public String getJobId(){
	return jobId;
    }
    
    public void setJobId(String jobId){
	this.jobId = jobId;
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
	
    public String getServiceId(){
        return serviceId;
    }
        
    public void setServiceId(String serviceId){
	this.serviceId = serviceId;
    }
	
    public ResultParameters getParameters(){
	return parameters;
    }
    
    public void setParameters(ResultParameters parameters){
	this.parameters = parameters;
    }   
}
