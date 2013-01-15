/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.bean;

import org.json.simple.JSONArray;

/**
 * This class includes all basic attributes in one cloud.
 * Standard behaviors like the get and set methods for Cloud object properties are defined here.
 * @author siliu
 */
public class Cloud {
    
    private String cloudid;
    private String cloudname;
    private long status;
    private JSONArray sites;
    private JSONArray matrices;
    
    /**
     * Get the id of cloud id
     * @return
     */
    public String getCloudid(){	
	return cloudid;
    }
    
    /**
     * Set the value of cloud id 
     * @param cloudid
     */
    public void setCloudid(String cloudid){
        this.cloudid = cloudid;
    }
    
    /**
     * Get the name of a cloud.
     * @return
     */
    public String getCloudname(){	
	return cloudname;
    }
    
    /**
     * Set the value of cloud name
     * @param cloudname
     */
    public void setCloudname(String cloudname){
        this.cloudname = cloudname;
    }
    
    
    /**
     * Get the status of a cloud
     * @return
     */
    public long getStatus(){
	return status;
    }
	
    /**
     * Set the value of cloud status
     * @param status
     */
    public void setStatus(long status){
        this.status = status;
    }
    
    
    /**
     * Get the sites in a cloud
     * @return
     */
    public JSONArray getSites(){
	return sites;
    }
    
    /**
     * Set the value of cloud sites
     * @param sites
     */
    public void setSites(JSONArray sites){
        this.sites = sites;
    }
    
    /**
     * Get the matrices in a cloud
     * @return
     */
    public JSONArray getMatrices(){
	return matrices;
    }
    
    /**
     * Set the value of cloud matrices
     * @param matrices
     */
    public void setMatrices(JSONArray matrices){
        this.matrices = matrices;
    }
}
