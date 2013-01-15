/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.action;

import com.opensymphony.xwork2.Action;
import site.operation.SiteDelete;

/**
 * The action to invoke the background logic methods to delete site from the data store by site id,
 * Standard behaviors like the get and set methods for SiteDeleteAction object properties are defined here.
 * @author siliu
 */
public class SiteDeleteAction {
    
    private String siteid;
        
    
    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }
    
    public SiteDeleteAction() {
    }
    
    /**
     * The method to invoke the logical method to delete site from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print("site delete action: " + siteid);
        
        boolean flag = SiteDelete.executeSiteDelete(siteid);
        
        if(flag == true){
            
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}
