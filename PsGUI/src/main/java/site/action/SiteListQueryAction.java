/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.action;

import com.opensymphony.xwork2.Action;
import site.bean.SiteList;
import site.operation.SiteListQuery;

/**
 * The action to invoke the background logic methods to query the list of sites in the data store,
 * Standard behaviors like the get and set methods for SiteListQueryAction object properties are defined here.
 * @author siliu
 */
public class SiteListQueryAction {
    
    private SiteList site_list;
    
    public SiteList getSite_list(){
        return site_list;
    }
    
    public void setSite_list(SiteList site_list){
        this.site_list = site_list;
    }
    
    public SiteListQueryAction() {
    }
    
    /**
     * The method to invoke the logical method to query site list in the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        SiteList s = SiteListQuery.executeSiteListQuery();
        
        if(s != null){
            setSite_list(s);
            return Action.SUCCESS;
        
        }else{
             return Action.ERROR;
        }
        
    }
}
