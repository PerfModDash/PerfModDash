/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.action;

import com.opensymphony.xwork2.Action;
import host.bean.Host;
import host.operation.HostQuery;

/**
 * The action to display the update host page.
 * Standard behaviors like the get and set methods for HostUpdatePageAction object properties are defined here.
 * @author siliu
 */
public class HostUpdatePageAction {
    private String hostid;
    private Host one_host;
    
    public String getHostid(){	
	return hostid;
    }
    
    public void setHostid(String hostid){
        this.hostid = hostid;
    }
    
    public Host getOne_host() {
        return one_host;
    }

    public void setOne_host(Host one_host) {
        this.one_host = one_host;
    }
    
    
    public HostUpdatePageAction() {
    }
    
    /**
     * The method to invoke the logical method to query host that to be updated.
     * Then display it on the interface.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print("hostid: " + hostid);
        Host h = HostQuery.executeHostQuery(hostid);
        
        if(h != null){
            setOne_host(h);
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
        
    }
}
