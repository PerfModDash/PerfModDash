/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.Cloud;
import cloud.operation.CloudQuery;
import com.opensymphony.xwork2.Action;

/**
 * The action to display the delete cloud page.
 * Standard behaviors like the get and set methods for CloudDeletePageAction object properties are defined here.
 * @author siliu
 */
public class CloudDeletePageAction {
    
    private String cloudid;
    private Cloud one_cloud;
        
    
    public String getCloudid() {
        return cloudid;
    }

    public void setCloudid(String cloudid) {
        this.cloudid = cloudid;
    }
    
    public Cloud getOne_cloud() {
        return one_cloud;
    }

    public void setOne_cloud(Cloud one_cloud) {
        this.one_cloud = one_cloud;
    }
    
    public CloudDeletePageAction() {
    }
    
    /**
     * The method to invoke the logical method to query cloud that to be deleted.
     * Then display it on the interface.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print("CloudDeletePageAction cloudid: " + cloudid);
        Cloud c = CloudQuery.executeCloudQuery(cloudid);
        
        if( c!= null){
            
            setOne_cloud(c);
        
            return Action.SUCCESS;
            
        }else{
            
            return Action.ERROR;
            
        }
    }
}
