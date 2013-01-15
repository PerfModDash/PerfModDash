/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.action;

import com.opensymphony.xwork2.Action;
import host.bean.Host;
import host.bean.HostList;
import host.operation.HostListQuery;
import host.operation.HostQuery;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import site.bean.Site;
import site.operation.SiteARHost;
import site.operation.SiteQuery;

/**
 * The action to invoke the background logic methods to add or remove hosts from a site,
 * then display the hosts in one site.
 * Standard behaviors like the get and set methods for SiteARHostAction object properties are defined here.
 * @author siliu
 */
public class SiteARHostAction {
    
    private String siteid;
    private String button;
    private ArrayList hosts_to_add;
    private ArrayList hosts_to_remove;
    private int button_flag;
    private JSONArray host_array;
    
    
    //to display the host list after add and remove to the site
    private Site one_site;
    private HostList host_list;
    private ArrayList hosts_in_site;
    private ArrayList hosts_not_in_site;
    
    
    
    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
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
    
    public ArrayList getHosts_to_add() {
        return hosts_to_add;
    }

    public void setHosts_to_add(ArrayList hosts_to_add) {
        this.hosts_to_add = hosts_to_add;
    }
    
    public ArrayList getHosts_to_remove() {
        return hosts_to_remove;
    }

    public void setHosts_to_remove(ArrayList hosts_to_remove) {
        this.hosts_to_remove = hosts_to_remove;
    }
    
    public JSONArray getHost_array() {
        return host_array;
    }

    public void setHost_array(JSONArray host_array) {
        this.host_array = host_array;
    }
    
    public Site getOne_site() {
        return one_site;
    }

    public void setOne_site(Site one_site) {
        this.one_site = one_site;
    }
    
    public HostList getHost_list(){
        return host_list;
    }
    
    public void setHost_list(HostList host_list){
        this.host_list = host_list;
    }
    
    public ArrayList getHosts_in_site() {
        return hosts_in_site;
    }

    public void setHosts_in_site(ArrayList hosts_in_site) {
        this.hosts_in_site = hosts_in_site;
    }
    
    public ArrayList getHosts_not_in_site() {
        return hosts_not_in_site;
    }

    public void setHosts_not_in_site(ArrayList hosts_not_in_site) {
        this.hosts_not_in_site = hosts_not_in_site;
    }
    
    
    public SiteARHostAction() {
    }
    
    /**
     * The method to get the host list that user checked on the display,which he/she would like to add or remove.
     * Then invoke the logical methods to execute the operation.
     * Then display the add or remove hosts from site page.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        setHost_list(HostListQuery.executeHostListQuery());
        JSONArray hostIds = host_list.getHostIds();
        JSONArray hostNames = host_list.getHostNames();
        
        
        host_array = new JSONArray();
        String element;
        int index;
        
        if(button.equals("remove selected hosts")){
            
            button_flag = 1;
		
            for(int m=0; m<hosts_to_remove.size(); m++){
                element = (String) hosts_to_remove.get(m);
                index = hostNames.indexOf(element);
		host_array.add(hostIds.get(index));
            }
			
	}else if(button.equals("add selected hosts")){
            
            button_flag = 2;
			
            for(int n=0; n<hosts_to_add.size(); n++){
		element = (String) hosts_to_add.get(n);
                index = hostNames.indexOf(element);
		host_array.add(hostIds.get(index));
            }
	}
        
        
        boolean flag = SiteARHost.executeSiteARHost(siteid, button_flag, host_array);
        if(flag == true){
            
            Site s = SiteQuery.executeSiteQuery(siteid);
            
            if(s != null){
     
                setOne_site(s);
            
                JSONArray hosts = one_site.getHosts();
            
                String hostid;
                String hostname;
                Host h;
            
                hosts_in_site = new ArrayList();
            
                if(! hosts.isEmpty()){
                
                    for(int i=0 ; i<hosts.size(); i++){
                    
                        hostid = (String)hosts.get(i);
                        h = HostQuery.executeHostQuery(hostid);
                        
                        hostname = h.getHostname();
                        hosts_in_site.add(hostname); 
                    }
                
                    hosts_not_in_site = new ArrayList();
                    String h2;
            
                    for(int i = 0; i< host_list.getHostNames().size() ; i++){
                
                        h2 = (String) host_list.getHostNames().get(i);
                
                        if(! hosts_in_site.contains(h2)){
                            hosts_not_in_site.add(h2);
                        }
                    }
                
                
                }else{
                
                    //hosts_in_site.clear();
                    hosts_not_in_site = host_list.getHostNames();
                
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
