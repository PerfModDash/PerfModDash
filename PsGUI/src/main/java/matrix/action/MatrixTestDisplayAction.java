/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;
import java.util.ArrayList;

/**
 * The action to display the test attributes.
 * Standard behaviors like the get and set methods for MatrixTestDisplayAction object properties are defined here.
 * @author siliu
 */
public class MatrixTestDisplayAction {
    
    private ArrayList test_detail;
    private String resultStatus;
    private String resultMessage;
    private String monitor;
    private String source;
    private String destination;
    private String resultTime;
    private String min;
    private String max;
    private String average;
    private String command;
    private String count;
    private String checkInterval;
    private String nextCheckTime;
                    
    
    
    public ArrayList getTest_detail(){
        
        return test_detail;
        
    }
    
    public void setTest_detail(ArrayList test_detail){
        
        this.test_detail = test_detail;
    
    }
    
    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }
    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
    public String getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(String checkInterval) {
        this.checkInterval = checkInterval;
    }
    
    public String getNextCheckTime() {
        return nextCheckTime;
    }

    public void setNextCheckTime(String nextCheckTime) {
        this.nextCheckTime = nextCheckTime;
    }
    
    public MatrixTestDisplayAction() {
    }
    
    /**
     * The method to get the value of test attributes.
     * @return SUCCESS 
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        String str1 = (String) test_detail.get(0);
        String str2 = str1.substring(1, str1.length()-2);
        String[] testArray = str2.split(",");
        
        resultStatus = testArray[0];
        resultMessage = testArray[1];
        monitor = testArray[2];
	source = testArray[3];
        destination = testArray[4];
	resultTime = testArray[5];
        min = testArray[6];
	max = testArray[7];
        average = testArray[8];
        command = testArray[9];
	count = testArray[10];
        checkInterval = testArray[11];
	nextCheckTime = testArray[12];
        return Action.SUCCESS;
    }
}
