/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.Cloud;
import cloud.operation.CloudCreate;
import com.opensymphony.xwork2.Action;
import org.json.simple.JSONObject;

/**
 * The action to invoke the background logic method to create new cloud.
 * Standard behaviors like the get and set methods for CloudCreateAction object properties are defined here.
 * @author siliu
 */
public class CloudCreateAction {
    
    private String cloudname;
    private Cloud newCloud;
    
    public String getCloudname(){	
	return cloudname;
    }
    
    public void setCloudname(String cloudname){
        this.cloudname = cloudname;
    }
    
    public Cloud getNewCloud() {
        return newCloud;
    }

    public void setNewCloud(Cloud newCloud) {
        this.newCloud = newCloud;
    }
    
    
    public CloudCreateAction() {
    }
    
    /**
     * The method to invoke the logical method to create cloud in the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        JSONObject cloudObj = new JSONObject();
        cloudObj.put("name", cloudname);
	
        
        Cloud c = CloudCreate.executeCloudCreate(cloudObj);
        
        if(c != null){
            
            setNewCloud(c);
           
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
