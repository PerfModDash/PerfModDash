/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.bean;

/**
 * This class includes all basic attributes of the test parameters field in one matrix.
 * Standard behaviors like the get and set methods for Host object properties are defined here.
 * @author siliu
 */
public class TestParameters {
    
    private String warningThreshold;
    private String monitor;
    private String destinationHostId;
    private String timeRange;
    private String source;
    private String maHostId;
    private String url;
    private String sourceHostId;
    private String criticalThreshold;
    private String destination;
    
    public String getWarningThreshold(){
        return warningThreshold;
    }
    
    public void setWarningThreshold(String warningThreshold){
        this.warningThreshold = warningThreshold;
    }
	
	
    public String getMonitor(){
	return monitor;
    }
    
    public void setMonitor(String monitor){
	this.monitor = monitor;
    }
	
    public String getDestinationHostId(){
	return destinationHostId;
    }
    
    public void setDestinationHostId(String destinationHostId){
	this.destinationHostId = destinationHostId;
    }
    
    public String getTimeRange(){
        return timeRange;
    }
    
    public void setTimeRange(String timeRange){
	this.timeRange = timeRange;
    }
	
    public String getSource(){
	return source;
    }
    
    public void setSource(String source){
	this.source = source;
    }
    
    public String getSourceHostId(){
	return sourceHostId;
    }
    
    public void setSourceHostId(String sourceHostId){
	this.sourceHostId = sourceHostId;
    }
	
    public String getUrl(){
        return url;
    }
        
    public void setUrl(String url){
	this.url = url;
    }
	
    public String getMaHostId(){
	return maHostId;
    }
    
    public void setMaHostId(String maHostId){
	this.maHostId = maHostId;
    }
	
    public String getCriticalThreshold(){
	return criticalThreshold;
    }
    
    public void setCriticalThreshold(String criticalThreshold){
	this.criticalThreshold = criticalThreshold;
    }
    
    public String getDestination(){
        return destination;
    }
    
    public void setDestination(String destination){
	this.destination = destination;
    }
    
    
}
