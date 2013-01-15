/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.bean;

import org.json.simple.JSONArray;

/**
 * This class includes all site ids, names and the total number of sites.
 * Standard behaviors like the get and set methods for SiteList object properties are defined here.
 * @author siliu
 */
public class SiteList {
    
    private JSONArray siteIds;
    private JSONArray siteNames;
    private int siteNumber;
    
    public JSONArray getSiteIds(){
        
	return siteIds;
    }
    
    public void setSiteIds(JSONArray siteIds){
        this.siteIds = siteIds;
    }
    
    public JSONArray getSiteNames(){
	return siteNames;
    }
    
    public void setSiteNames(JSONArray siteNames){
        this.siteNames = siteNames;
    }
    
    public int getSiteNumber(){
	return siteNumber;
    }
    
    public void setSiteNumber(int siteNumber){
        this.siteNumber = siteNumber;
    }
    
    
}
