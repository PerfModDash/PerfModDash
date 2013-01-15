/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.action;

import com.opensymphony.xwork2.Action;
import host.bean.Host;
import host.operation.HostQuery;
import site.bean.Site;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import site.operation.SiteQuery;

/**
 * The action to invoke the background logic methods to query site in the data store by site id.
 * Standard behaviors like the get and set methods for SiteQueryAction object properties are defined here.
 * @author siliu
 */
public class SiteQueryAction {
    
    private String siteid;
    private Site one_site;
    private ArrayList hosts_in_site;
        
    
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

    public ArrayList getHosts_in_site() {
        return hosts_in_site;
    }

    public void setHosts_in_site(ArrayList hosts_in_site) {
        this.hosts_in_site = hosts_in_site;
    }
    
    public SiteQueryAction() {
    }
    
    /**
     * The method to invoke the logical method to query site from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print("SiteQueryAction siteid: " + siteid);
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
            }else{
                
                hosts_in_site = null;
            }
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}
