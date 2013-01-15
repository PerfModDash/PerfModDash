/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.action;

import com.opensymphony.xwork2.Action;
import host.bean.HostList;
import host.operation.HostListQuery;

/**
 * The action to invoke the background logic methods to query the list of hosts in the data store,
 * Standard behaviors like the get and set methods for HostListQueryAction object properties are defined here.
 * @author siliu
 */
public class HostListQueryAction {
    
    private HostList host_list;
    
    
    
    public HostList getHost_list(){
        return host_list;
    }
    
    public void setHost_list(HostList host_list){
        this.host_list = host_list;
    }
    
    public HostListQueryAction() {
    }
    
    /**
     * The method to invoke the logical method to query host list in the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        HostList h = HostListQuery.executeHostListQuery();
        
        if(h != null){
            setHost_list(h);
            return Action.SUCCESS;
        
        }else{
             return Action.ERROR;
        }
    }
}
