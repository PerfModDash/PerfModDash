/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitiveServ.action;

import com.opensymphony.xwork2.Action;
import config.PsApi;
import host.bean.Host;
import host.operation.HostQuery;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import primitiveServ.bean.PrimitiveServ;
import primitiveServ.operation.PrimitiveServQuery;

/**
 * The action to display the page to add/remove primitive services to/from host.
 * Standard behaviors like the get and set methods for PrimitiveServARtoHostPageAction object properties are defined here.
 * @author siliu
 */
public class PrimitiveServARtoHostPageAction {
    
    private String hostid;
    private Host one_host;
    private ArrayList all_services;
    private ArrayList services_not_on_host;
    private ArrayList services_on_host;
        
    
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
    
    public ArrayList getAll_services() {
        return all_services;
    }

    public void setAll_services(ArrayList all_services) {
        this.all_services = all_services;
    }
    
    public ArrayList getServices_not_on_host() {
        return services_not_on_host;
    }

    public void setServices_not_no_host(ArrayList services_not_on_host) {
        this.services_not_on_host = services_not_on_host;
    }
    
    public ArrayList getServices_on_host() {
        return services_on_host;
    }

    public void setServices_no_host(ArrayList services_on_host) {
        this.services_on_host = services_on_host;
    }
    
    /**
     * The method to get the current primitive services list in a host, and the current primitive services list not in a host.
     * Display primitive services list in two groups: primitive services in host and primitive services not in host.
     * @return SUCCESS if the action is successful, ERROR otherwise
     */
    public PrimitiveServARtoHostPageAction() {
    }
    
    public String execute() throws Exception {
        
        Host h = HostQuery.executeHostQuery(hostid);
        
        if(h != null){
     
            setOne_host(h);
            
            all_services = new ArrayList();
            
            all_services.add(PsApi.BWCTL_PORT_4823);
            all_services.add(PsApi.BWCTL_PORT_8570);
            all_services.add(PsApi.CHECK_LOOKUP_SERVICE);
            all_services.add(PsApi.NDT_PORT_3001);
            all_services.add(PsApi.NDT_PORT_7123);
            all_services.add(PsApi.NPAD_PORT_8000);
            all_services.add(PsApi.NPAD_PORT_8001);
            all_services.add(PsApi.OWP_8569);
            all_services.add(PsApi.OWP_861);
            all_services.add(PsApi.PERFSONAR_PSB);
            
            
            services_on_host = new ArrayList();
            JSONArray services = one_host.getServices();
            
            String serviceID;
            String serviceType;
            PrimitiveServ serv;
            
            if(! services.isEmpty()){
                
                for(int i=0 ; i<services.size(); i++){
                    
                    serviceID = (String)services.get(i);
                    serv = PrimitiveServQuery.executePrimitiveServQuery(serviceID);
                    serviceType = serv.getType();
                    services_on_host.add(serviceType);
                 
                }
                
                services_not_on_host = new ArrayList();
                String s;
            
                for(int i = 0; i< all_services.size() ; i++){
                
                    s = (String) all_services.get(i);
                
                    if(! services_on_host.contains(s)){
                        services_not_on_host.add(s);
                    }
                }
          
            }else{
                //services_on_host.clear();
                services_not_on_host = all_services;
            
            }
            
            
            
            
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}
