/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.Cloud;
import cloud.operation.CloudUpdate;
import com.opensymphony.xwork2.Action;
import org.json.simple.JSONObject;

/**
 * The action to invoke the background logic methods to update cloud name in the data store by cloud id,
 * Standard behaviors like the get and set methods for CloudUpdateAction object properties are defined here.
 * @author siliu
 */
public class CloudUpdateAction {
    
    private String cloudid;
    private String cloudname;
    private Cloud newCloud;
    
    public String getCloudid() {
        return cloudid;
    }

    public void setCloudid(String cloudid) {
        this.cloudid = cloudid;
    }
    
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
    
    public CloudUpdateAction() {
    }
    
    /**
     * The method to invoke the logical method to update cloud from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        JSONObject cloudObj = new JSONObject();
        cloudObj.put("id", cloudid);
        cloudObj.put("name", cloudname);
        
	System.out.println("Cloud update action:" + cloudid);
        
        Cloud c = CloudUpdate.executeCloudUpdate(cloudid,cloudObj);
        
        if(c != null){
            
            setNewCloud(c);
           
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
