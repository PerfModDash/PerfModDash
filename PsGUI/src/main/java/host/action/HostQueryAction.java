/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.action;

import host.operation.HostQuery;
import com.opensymphony.xwork2.Action;
import host.bean.Host;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import primitiveServ.bean.PrimitiveServ;
import primitiveServ.operation.PrimitiveServQuery;

/**
 * The action to invoke the background logic methods to query host in the data store by host id,
 * Standard behaviors like the get and set methods for HostQueryAction object properties are defined here.
 * @author siliu
 */
public class HostQueryAction {
    
    private String hostid;
    private Host one_host;
    private ArrayList services_on_host;
        
        
    public Host getOne_host() {
        return one_host;
    }

    public void setOne_host(Host one_host) {
        this.one_host = one_host;
    }

    public String getHostid() {
        return hostid;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }       
    
    public ArrayList getServices_on_host() {
        return services_on_host;
    }

    public void setServices_no_host(ArrayList services_on_host) {
        this.services_on_host = services_on_host;
    }
    
    public HostQueryAction() {
    }
    
    /**
     * The method to invoke the logical method to query host from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print("hostid: " + hostid);
        Host h = HostQuery.executeHostQuery(hostid);
        
        
        if(h != null){
     
            setOne_host(h);
            JSONArray services = one_host.getServices();
            
            String serviceID;
            String serviceType;
            PrimitiveServ serv;
            
            services_on_host = new ArrayList();
            
            if(! services.isEmpty()){
                
                for(int i=0 ; i<services.size(); i++){
                    
                        serviceID = (String)services.get(i);
                        serv = PrimitiveServQuery.executePrimitiveServQuery(serviceID);
                        serviceType = serv.getType();
                        services_on_host.add(serviceType);
                 
                }
            }else{
                
                services_on_host = null;
            }
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
        
        
       
    }
    
}
