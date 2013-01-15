/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.bean;

/**
 * This class includes all basic attributes of the result parameters field in one matrix.
 * Standard behaviors like the get and set methods for Host object properties are defined here.
 * @author siliu
 */
public class ResultParameters {
    
    private String min;
    private String max;
    private int count;
    private String command;
    private String standardDeviation;
    private String average;
    
    
    public String getMin(){
        return min;
    }
    
    public void setMin(String min){
        this.min = min;
    }
	
    public String getMax(){
	return max;
    }
    
    public void setMax(String max){
	this.max = max;
    }
    
    public int getCount(){
	return count;
    }
    
    public void setCount(int count){
	this.count = count;
    }
    
    public String getCommand(){
	return command;
    }
    
    public void setCommand(String command){
	this.command = command;
    }
    
    public String getStandardDeviation(){
        return standardDeviation;
    }
    
    public void setStandardDeviation(String standardDeviation){
	this.standardDeviation = standardDeviation;
    }
	
    public String getAverage(){
        return average;
    }
        
    public void setAverage(String average){
	this.average = average;
    }
    
}
