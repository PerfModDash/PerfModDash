/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.action;

import com.opensymphony.xwork2.Action;
import host.bean.Host;
import host.operation.HostQuery;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import site.bean.Site;
import site.operation.SiteQuery;

/**
 * The action to display the update site page.
 * Standard behaviors like the get and set methods for SiteUpdatePageAction object properties are defined here.
 * @author siliu
 */
public class SiteUpdatePageAction {
    
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
    
    public SiteUpdatePageAction() {
    }
    
    /**
     * The method to invoke the logical method to query site that to be updated.
     * Then display it on the interface.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print("SiteUpdatePageAction siteid: " + siteid);
        Site s = SiteQuery.executeSiteQuery(siteid);
        
        if(s != null){
     
            setOne_site(s);
            
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}
