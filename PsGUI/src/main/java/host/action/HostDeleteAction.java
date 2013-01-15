/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.action;

import host.operation.HostDelete;
import com.opensymphony.xwork2.Action;

/**
 * The action to invoke the background logic methods to delete host from the data store by host id,
 * Standard behaviors like the get and set methods for HostDeleteAction object properties are defined here.
 * @author siliu
 */
public class HostDeleteAction {
    
    private String hostid;
    
    public String getHostid(){	
	return hostid;
    }
    
    public void setHostid(String hostid){
        this.hostid = hostid;
    }
    
    public HostDeleteAction() {
    }
    
    /**
     * The method to invoke the logical method to delete host from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print("host delete action: " + hostid);
        
        boolean flag = HostDelete.executeHostDelete(hostid);
        
        if(flag == true){
            
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
       
    }
}
