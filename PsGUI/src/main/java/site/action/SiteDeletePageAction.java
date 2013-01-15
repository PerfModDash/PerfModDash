/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.action;

import com.opensymphony.xwork2.Action;
import site.bean.Site;
import site.operation.SiteQuery;

/**
 * The action to display the delete site page.
 * Standard behaviors like the get and set methods for SiteDeletePageAction object properties are defined here.
 * @author siliu
 */
public class SiteDeletePageAction {
    
    private String siteid;
    private Site one_site;
        
    
    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }
    
     public Site getOne_site() {
        return one_site;
    }

    public void setOne_site(Site one_site) {
        this.one_site = one_site;
    }
    
    public SiteDeletePageAction() {
    }
    
    /**
     * The method to invoke the logical method to query site that to be deleted.
     * Then display it on the interface.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        Site s = SiteQuery.executeSiteQuery(siteid);
        
        if(s != null){
            
            setOne_site(s);
            
            return Action.SUCCESS;
            
        }else{
            
            return Action.ERROR;
            
        }
    }   
}
