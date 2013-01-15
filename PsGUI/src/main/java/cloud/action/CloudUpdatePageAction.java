/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.Cloud;
import cloud.operation.CloudQuery;
import com.opensymphony.xwork2.Action;

/**
 * The action to display the update cloud page.
 * Standard behaviors like the get and set methods for CloudUpdatePageAction object properties are defined here.
 * @author siliu
 */
public class CloudUpdatePageAction {
    
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
    
    public CloudUpdatePageAction() {
    }
    
    /**
     * The method to invoke the logical method to query cloud that to be updated.
     * Then display it on the interface.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print("CloudUpdatePageAction cloudid: " + cloudid);
        Cloud c = CloudQuery.executeCloudQuery(cloudid);
        
        if( c!= null){
            
            setOne_cloud(c);
        
            return Action.SUCCESS;
            
        }else{
            
            return Action.ERROR;
            
        }
        
        
    }
}
