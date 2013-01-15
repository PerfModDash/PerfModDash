/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.action;

import com.opensymphony.xwork2.Action;
import org.json.simple.JSONObject;
import site.bean.Site;
import site.operation.SiteUpdate;

/**
 * The action to invoke the background logic methods to update site name in the data store by site id,
 * Standard behaviors like the get and set methods for SiteUpdateAction object properties are defined here.
 * @author siliu
 */
public class SiteUpdateAction {
    
    private String siteid;
    private String sitename;
    //private long status;
    private String description;
    private Site newSite;
    
    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }
    
    public String getSitename(){	
	return sitename;
    }
    
    public void setSitename(String sitename){
        this.sitename = sitename;
    }
    
    /*
    public long getStatus(){
	return status;
    }
	
    public void setStatus(long status){
        this.status = status;
    }
    */
    public String getDescription(){
	return description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public Site getNewSite(){
	return newSite;
    }
    
    public void setNewSite(Site newSite){
        this.newSite = newSite;
    }
    
    public SiteUpdateAction() {
    }
    
    /**
     * The method to invoke the logical method to update site from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.println("Site update action:" + siteid);
        JSONObject siteObj = new JSONObject();
        siteObj.put("id", siteid);
        siteObj.put("name", sitename);
	siteObj.put("status", 0);
	siteObj.put("description", description);
        siteObj.put("hosts", null);
        
        
        Site s = SiteUpdate.executeSiteUpdate(siteid,siteObj);
        
        if(s != null){
            
            setNewSite(s);
           
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
