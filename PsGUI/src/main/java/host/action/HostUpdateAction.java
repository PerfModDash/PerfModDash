/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.action;

import host.operation.HostUpdate;
import com.opensymphony.xwork2.Action;
import host.bean.Host;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;

/**
 * The action to invoke the background logic method to update host in the data store by host id,
 * Standard behaviors like the get and set methods for HostUpdateAction object properties are defined here.
 * @author siliu
 */
public class HostUpdateAction {
    private String hostid;
    private String hostname;
    private String ipv4;
    private String ipv6;
    private Host newHost;
    
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
    
    public Host getNewHost(){
	return newHost;
    }
    
    public void setNewHost(Host newHost){
        this.newHost = newHost;
    }
    
    
    public HostUpdateAction() {
    }
    
    /**
     * The method to invoke the logical method to update host from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    
    public String execute() throws Exception {
        
        JSONObject hostObj = new JSONObject();
       
        hostObj.put("id", hostid);
        hostObj.put("hostname", hostname);
	hostObj.put("ipv4", ipv4);
	hostObj.put("ipv6", ipv6);
        
        
        Host h = HostUpdate.executeHostUpdate(hostObj,hostid);
        
        if(h != null){
            
            setNewHost(h);
            
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
