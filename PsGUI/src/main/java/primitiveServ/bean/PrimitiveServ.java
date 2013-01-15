/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitiveServ.bean;

/**
 * This class includes all attributes of a primitive service.
 * Standard behaviors like the get and set methods for PrimitiveServ object properties are defined here.
 * @author siliu
 */
public class PrimitiveServ {
    
    private String id;
    private Result result;
    private String description;
    private String name;
    private String runningSince;
    private String nextCheckTime;
    private long checkInterval;
    private Parameters parameters;
    private String prevCheckTime;
    private String type;
    private boolean running;
    private long timeout;
    
    
    public String getId(){	
	return id;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public Result getResult(){	
	return result;
    }
    
    public void setResult(Result result){
        this.result = result;
    }
    
    
    public String getDescription(){
	return description;
    }
	
    public void setDescription(String description){
        this.description = description;
    }
    
     public String getName(){	
	return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getRunningSince(){	
	return runningSince;
    }
    
    public void setRunningSince(String runningSince){
        this.runningSince = runningSince;
    }
    
    
    public String getNextCheckTime(){
	return nextCheckTime;
    }
	
    public void setNextCheckTime(String nextCheckTime){
        this.nextCheckTime = nextCheckTime;
    }
    
     public long getCheckInterval(){	
	return checkInterval;
    }
    
    public void setCheckInterval(long checkInterval){
        this.checkInterval = checkInterval;
    }
    
    public Parameters getParameters(){	
	return parameters;
    }
    
    public void setParameters(Parameters parameters){
        this.parameters = parameters;
    }
    
    
    public String getPrevCheckTime(){
	return prevCheckTime;
    }
	
    public void setPrevCheckTime(String prevCheckTime){
        this.prevCheckTime = prevCheckTime;
    }
    
     public String getType(){	
	return type;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public boolean getRunning(){	
	return running;
    }
    
    public void setRunning(boolean running){
        this.running = running;
    }
    
    
    public long getTimeout(){
	return timeout;
    }
	
    public void setTimeout(long timeout){
        this.timeout = timeout;
    }
    
}
