/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.action;

import com.opensymphony.xwork2.Action;
import org.json.simple.JSONObject;
import site.bean.Site;
import site.operation.SiteCreate;

/**
 * The action to invoke the background logic method to create new site.
 * Standard behaviors like the get and set methods for SiteCreateAction object properties are defined here.
 * @author siliu
 */
public class SiteCreateAction {
    
    private String sitename;
   // private long status;
    private String description;
    private Site newSite;
    
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
    
    public SiteCreateAction() {
    }
    
    /**
     * The method to invoke the logical method to create site in the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        JSONObject siteObj = new JSONObject();
        siteObj.put("name", sitename);
	//siteObj.put("status", 0);
	siteObj.put("description", description);
        //siteObj.put("hosts", null);
        
        Site s = SiteCreate.executeSiteCreate(siteObj);
        
        if(s != null){
            
            setNewSite(s);
           
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
