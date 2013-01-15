/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.operation.CloudDelete;
import com.opensymphony.xwork2.Action;


/**
 * The action to invoke the background logic methods to delete cloud from the data store by cloud id,
 * Standard behaviors like the get and set methods for CloudDeleteAction object properties are defined here.
 * @author siliu
 */
public class CloudDeleteAction {
    
    private String cloudid;
    
    public String getCloudid() {
        return cloudid;
    }

    public void setCloudid(String cloudid) {
        this.cloudid = cloudid;
    }
    
    public CloudDeleteAction() {
    }
    
    /**
     * The method to invoke the logical method to delete cloud from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print("cloud delete action: " + cloudid);
        
        boolean flag = CloudDelete.executeCloudDelete(cloudid);
        
        if(flag == true){
            
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}
