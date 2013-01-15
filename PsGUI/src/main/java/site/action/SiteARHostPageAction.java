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
import site.operation.SiteQuery;

/**
 * The action to display the page to add/remove hosts to/from site.
 * Standard behaviors like the get and set methods for SiteARHostPageAction object properties are defined here.
 * @author siliu
 */
public class SiteARHostPageAction {
    
    private String siteid;
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
    
    
    public SiteARHostPageAction() {
    }
    
    /**
     * The method to get the current host list in a site, and the current host list not in a site.
     * Display host list in two groups: hosts in site and hosts not in site.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print("siteARHostPage siteid: " + siteid);
        Site s = SiteQuery.executeSiteQuery(siteid);
        
        setHost_list(HostListQuery.executeHostListQuery());
        
        if(s != null){
     
            setOne_site(s);
            
            JSONArray hosts = one_site.getHosts();
            
            String hostid;
            String hostname;
            Host h;
            
            hosts_in_site = new ArrayList();
            hosts_not_in_site = new ArrayList();
            
            if(! hosts.isEmpty()){
                
                for(int i=0 ; i<hosts.size(); i++){
                    
                        hostid = (String)hosts.get(i);
                        h = HostQuery.executeHostQuery(hostid);
                        
                        hostname = h.getHostname();
                        hosts_in_site.add(hostname); 
                }
                
                
                String h2;
            
                for(int i = 0; i< host_list.getHostNames().size() ; i++){
                
                    h2 = (String) host_list.getHostNames().get(i);
                
                    if(! hosts_in_site.contains(h2)){
                        hosts_not_in_site.add(h2);
                    }
                }
                
                
            }else{
                
                //hosts_in_site = null;
                hosts_not_in_site = host_list.getHostNames();
                
            }
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}
