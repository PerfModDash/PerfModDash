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
import primitiveServ.operation.PrimitiveServARtoHost;
import primitiveServ.operation.PrimitiveServQuery;

/**
 * The action to invoke the background logic methods to add or remove primitive services from a host,
 * then display the primitive services in one host.
 * Standard behaviors like the get and set methods for PrimitiveServARtoHostAction object properties are defined here.
 * @author siliu
 */
public class PrimitiveServARtoHostAction {
    
    
    private String hostid;
    private String button;
    private ArrayList services_to_add;
    private ArrayList services_to_remove;
    private int button_flag;
    private JSONArray serv_array;
    
    //to display the host primitive services after add or remove
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
    
    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
    
    public int getButton_flag() {
        return button_flag;
    }

    public void setButton_flag(int button_flag) {
        this.button_flag = button_flag;
    }
    
    public ArrayList getServices_to_add() {
        return services_to_add;
    }

    public void setServices_to_add(ArrayList services_to_add) {
        this.services_to_add = services_to_add;
    }
    
    public ArrayList getServices_to_remove() {
        return services_to_remove;
    }

    public void setServices_to_remove(ArrayList services_to_remove) {
        this.services_to_remove = services_to_remove;
    }
    
    public JSONArray getServ_array() {
        return serv_array;
    }

    public void setServ_array(JSONArray serv_array) {
        this.serv_array = serv_array;
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
     * The method to get the primitive services list that user checked on the display,which he/she would like to add or remove.
     * Then invoke the logical methods to execute the operation.
     * Then display the add or remove primitive services from host page.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    
    public String execute() throws Exception {
        
        serv_array = new JSONArray();
        
        if(button.equals("add all throughput primitive services")){
            
            button_flag = 1;
            serv_array = null;
			
	}else if(button.equals("add all latency primitive services")){
            
            button_flag = 2;
            serv_array = null;
			
	}else if(button.equals("add all primitive services")){
            
            button_flag = 3;
            serv_array = null;
			
	}else if(button.equals("remove all primitive services")){
            
            button_flag = 4;
	    serv_array = null;
            
	}else if(button.equals("remove selected primitive services")){
            
            button_flag = 5;
		
            for(int m=0; m<services_to_remove.size(); m++){
		serv_array.add(services_to_remove.get(m));
            }
			
	}else if(button.equals("add selected primitive services")){
            
            button_flag = 6;
			
            for(int n=0; n<services_to_add.size(); n++){
		serv_array.add(services_to_add.get(n));
            }
	}
        
        
        boolean flag = PrimitiveServARtoHost.executePrimitiveServARtoHost(hostid, button_flag, serv_array);
        
        if(flag == true){
            
        
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
                services_not_on_host = new ArrayList();
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
            
        }else{
            
            return Action.ERROR;
        }
        
    }
}
