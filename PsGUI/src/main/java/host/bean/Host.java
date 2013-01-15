/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.bean;

import org.json.simple.JSONArray;

/**
 * This class includes all basic attributes in one host.
 * Standard behaviors like the get and set methods for Host object properties are defined here.
 * @author siliu
 */
public class Host {
    
    private String hostid;
    private String hostname;
    private String ipv4;
    private String ipv6;
    private JSONArray services;
    
    
    public String getHostid(){	
	return hostid;
    }
    
    public void setHostid(String hostid){
        this.hostid = hostid;
    }
    
    public String getHostname(){	
	return hostname;
    }
    
    public void setHostname(String hostname){
        this.hostname = hostname;
    }
    
    
    public String getIpv4(){
	return ipv4;
    }
	
    public void setIpv4(String ipv4){
        this.ipv4 = ipv4;
    }
    
    public String getIpv6(){
	return ipv6;
    }
    
    public void setIpv6(String ipv6){
        this.ipv6 = ipv6;
    }
    
    public JSONArray getServices(){
	return services;
    }
    
    public void setServices(JSONArray services){
        this.services = services;
    }
	
}
