/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.bean;

/**
 * This class includes all basic attributes of the test field in one matrix.
 * Standard behaviors like the get and set methods for Host object properties are defined here.
 * @author siliu
 */
public class Test {
    
    private String testId;
    private TestResult result;
    private String description;
    private String testName;
    private String runningSince;	
    private String nextCheckTime;
    private long checkInterval;
    private TestParameters parameters;
    private String prevCheckTime;
    private String type;
    private boolean running;
    private long timeout;
    
    
    public String getTestId(){
        return testId;
    }
    
    public void setTestId(String testId){
        this.testId = testId;
    }
	
    public TestResult getResult(){
        return result;
    }
    
    public void setResult(TestResult result){
        this.result = result;
    }
	
    public String getDescription(){
	return description;
    }
    
    public void setDescription(String description){
	this.description = description;
    }
	
    public String getTestName(){
	return testName;
    }
    
    public void setTestName(String testName){
	this.testName = testName;
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
	
    public TestParameters getParameters(){
	return parameters;
    }
    
    public void setParameters(TestParameters parameters){
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
