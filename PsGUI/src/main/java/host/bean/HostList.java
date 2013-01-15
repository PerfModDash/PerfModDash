/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.bean;

import org.json.simple.JSONArray;

/**
 * This class includes all host ids, names and the total number of hosts.
 * Standard behaviors like the get and set methods for HostList object properties are defined here.
 * @author siliu
 */
public class HostList {
    
    private JSONArray hostIds;
    private JSONArray hostNames;
    private int hostNumber;
    
    public JSONArray getHostIds(){
        
	return hostIds;
    }
    
    public void setHostIds(JSONArray hostIds){
        this.hostIds = hostIds;
    }
    
    public JSONArray getHostNames(){
	return hostNames;
    }
    
    public void setHostNames(JSONArray hostNames){
        this.hostNames = hostNames;
    }
    
    public int getHostNumber(){
	return hostNumber;
    }
    
    public void setHostNumber(int hostNumber){
        this.hostNumber = hostNumber;
    }
    
}
