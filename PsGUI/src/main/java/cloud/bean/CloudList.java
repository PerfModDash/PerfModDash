/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.bean;

import org.json.simple.JSONArray;

/**
 * This class includes all cloud ids, names and the total number of clouds.
 * Standard behaviors like the get and set methods for CloudList object properties are defined here.
 * @author siliu
 */
public class CloudList {
    
    private JSONArray cloudIds;
    private JSONArray cloudNames;
    private int cloudNumber;
    
    /**
     * Get the list of cloud ids.
     * @return: The cloud id list
     */
    public JSONArray getCloudIds(){
        
	return cloudIds;
    }
    
    /**
     * Set the values of cloud id list
     * @param cloudIds
     */
    public void setCloudIds(JSONArray cloudIds){
        this.cloudIds = cloudIds;
    }
    
    /**
     * Get the list of cloud names.
     * @return: The cloud name list
     */
    public JSONArray getCloudNames(){
	return cloudNames;
    }
    
    /**
     * Set the values of cloud names.
     * @param cloudNames
     */
    public void setCloudNames(JSONArray cloudNames){
        this.cloudNames = cloudNames;
    }
    
    /**
     * Get the number of clouds.
     * @return: The cloud number
     */
    public int getCloudNumber(){
	return cloudNumber;
    }
    
    /**
     * Set the value of cloud number.
     * @param cloudNumber
     */
    public void setCloudNumber(int cloudNumber){
        this.cloudNumber = cloudNumber;
    }
    
}
