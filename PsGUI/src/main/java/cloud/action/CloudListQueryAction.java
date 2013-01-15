/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.CloudList;
import cloud.operation.CloudListQuery;
import com.opensymphony.xwork2.Action;


/**
 * The action to invoke the background logic methods to query the list of clouds in the data store,
 * Standard behaviors like the get and set methods for CloudListQueryAction object properties are defined here.
 * @author siliu
 */
public class CloudListQueryAction {
    
    private CloudList cloud_list;
    
    public CloudList getCloud_list(){
        return cloud_list;
    }
    
    public void setCloud_list(CloudList cloud_list){
        this.cloud_list = cloud_list;
    }
    
   
    public CloudListQueryAction() {
    }
    
    /**
     * The method to invoke the logical method to query cloud list in the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
     public String execute() throws Exception {
        
        CloudList c = CloudListQuery.executeCloudListQuery();
        
        if(c != null){
            setCloud_list(c);
            return Action.SUCCESS;
        
        }else{
             return Action.ERROR;
        }
        
    }
}
