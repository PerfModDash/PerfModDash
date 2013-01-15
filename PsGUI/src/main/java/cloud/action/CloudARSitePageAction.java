/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.Cloud;
import cloud.operation.CloudQuery;
import com.opensymphony.xwork2.Action;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import site.bean.Site;
import site.bean.SiteList;
import site.operation.SiteListQuery;
import site.operation.SiteQuery;


/**
 * The action to display the page to add or remove sites to or from cloud.
 * Standard behaviors like the get and set methods for CloudARSitePageAction object properties are defined here.
 * @author siliu
 */
public class CloudARSitePageAction {
    
    private String cloudid;
    private Cloud one_cloud;
    private SiteList site_list;
    private ArrayList sites_in_cloud;
    private ArrayList sites_not_in_cloud;
        
    
    public String getCloudid() {
        return cloudid;
    }

    public void setCloudid(String cloudid) {
        this.cloudid = cloudid;
    }
    
    public Cloud getOne_cloud() {
        return one_cloud;
    }

    public void setOne_cloud(Cloud one_cloud) {
        this.one_cloud = one_cloud;
    }
    
    public SiteList getSite_list(){
        return site_list;
    }
    
    public void setSite_list(SiteList site_list){
        this.site_list = site_list;
    }
    
    
    public ArrayList getSites_in_cloud() {
        return sites_in_cloud;
    }

    public void setSites_in_cloud(ArrayList sites_in_cloud) {
        this.sites_in_cloud = sites_in_cloud;
    }
    
    public ArrayList getSites_not_in_cloud() {
        return sites_not_in_cloud;
    }

    public void setSites_not_in_cloud(ArrayList sites_not_in_cloud) {
        this.sites_not_in_cloud = sites_not_in_cloud;
    }
    
    public CloudARSitePageAction() {
    }
    
    /**
     * The method to get the current site list in a cloud, and the current site list not in a cloud.
     * Display sites list in two groups: sites in cloud and sites not in cloud.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print("CloudARSitePageAction cloudid: " + cloudid);
        Cloud c = CloudQuery.executeCloudQuery(cloudid);
        setSite_list(SiteListQuery.executeSiteListQuery());
        
        if(c != null){
     
            setOne_cloud(c);
            
            JSONArray sites = one_cloud.getSites();
            
            
            String site_id;
            String site_name;
            Site s;
            
            sites_in_cloud = new ArrayList();
            sites_not_in_cloud = new ArrayList();
            
            if(! sites.isEmpty()){
                
                for(int i=0 ; i<sites.size(); i++){
                    
                        site_id = (String)sites.get(i);
                        s = SiteQuery.executeSiteQuery(site_id);
                        
                        site_name = s.getSitename();
                        sites_in_cloud.add(site_name); 
                }
                
                
                String s2;
                for(int i = 0; i< site_list.getSiteNames().size() ; i++){
                
                    s2 = (String) site_list.getSiteNames().get(i);
                
                    if(! sites_in_cloud.contains(s2)){
                        sites_not_in_cloud.add(s2);
                    }
                }
                
            }else{
                sites_not_in_cloud = site_list.getSiteNames();
            }
            
            
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}

    