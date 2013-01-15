/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.bean;

import org.json.simple.JSONArray;

/**
 * This class includes all basic attributes in one site.
 * Standard behaviors like the get and set methods for Site object properties are defined here.
 * @author siliu
 */
public class Site {
  
    private String siteid;
    private String sitename;
    private long status;
    private String description;
    private JSONArray hosts;
    
    public String getSiteid(){	
	return siteid;
    }
    
    public void setSiteid(String siteid){
        this.siteid = siteid;
    }
    
    public String getSitename(){	
	return sitename;
    }
    
    public void setSitename(String sitename){
        this.sitename = sitename;
    }
    
    
    public long getStatus(){
	return status;
    }
	
    public void setStatus(long status){
        this.status = status;
    }
    
    public String getDescription(){
	return description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public JSONArray getHosts(){
	return hosts;
    }
    
    public void setHosts(JSONArray hosts){
        this.hosts = hosts;
    }
    
    
}
