/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitiveServ.bean;

/**
 * This class includes all attributes of parameters field in a primitive service result field.
 * Standard behaviors like the get and set methods for ResultParameters object properties are defined here.
 * @author siliu
 */
public class ResultParameters {

    private String time;
    private String command;
    
    public String getTime(){	
	return time;
    }
    
    public void setTime(String time){
        this.time = time;
    }
    
    public String getCommand(){	
	return command;
    }
    
    public void setCommand(String command){
        this.command = command;
    }

    
}
