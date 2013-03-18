/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.Cloud;
import cloud.operation.CloudARSite;
import cloud.operation.CloudQuery;
import com.opensymphony.xwork2.Action;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import site.bean.Site;
import site.bean.SiteList;
import site.operation.SiteListQuery;
import site.operation.SiteQuery;

/**
 * The action to invoke the background logic methods to add or remove sites from a cloud,
 * then display the sites in one cloud.
 * Standard behaviors like the get and set methods for CloudARSiteAction object properties are defined here.
 * @author siliu
 */
public class CloudARSiteAction {
    
    private String cloudid;
    private String button;
    private ArrayList sites_to_add;
    private ArrayList sites_to_remove;
    private int button_flag;
    private JSONArray site_array;
    
    
    //to display the site list after add and remove to the clouds
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
    
    
    public ArrayList getSites_to_add() {
        return sites_to_add;
    }

    public void setSites_to_add(ArrayList sites_to_add) {
        this.sites_to_add = sites_to_add;
    }
    
    public ArrayList getSites_to_remove() {
        return sites_to_remove;
    }

    public void setSites_to_remove(ArrayList sites_to_remove) {
        this.sites_to_remove = sites_to_remove;
    }
    
    public JSONArray getSite_array() {
        return site_array;
    }

    public void setSite_array(JSONArray site_array) {
        this.site_array = site_array;
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
    
    public CloudARSiteAction() {
    }
    
    /**
     * The method to get the site list that user checked on the display,which he/she would like to add or remove.
     * Then invoke the logical methods to execute the operation.
     * Then display the add or remove sites from cloud page.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        setSite_list(SiteListQuery.executeSiteListQuery());
        
        JSONArray siteIds = site_list.getSiteIds();
        JSONArray siteNames = site_list.getSiteNames();
        
        site_array = new JSONArray();
        String element;
        int index;
        
        if(button.equals("remove selected sites")){
            
            button_flag = 1;
		
            for(int m=0; m<sites_to_remove.size(); m++){
                element = (String) sites_to_remove.get(m);
                index = siteNames.indexOf(element);
		site_array.add(siteIds.get(index));
            }
			
	}else if(button.equals("add selected sites")){
            
            button_flag = 2;
			
            for(int n=0; n<sites_to_add.size(); n++){
                
		element = (String) sites_to_add.get(n);
                index = siteNames.indexOf(element);
		site_array.add(siteIds.get(index));
            }
	}
        
        boolean flag = CloudARSite.executeCloudARSite(cloudid, button_flag, site_array);
        
        if(flag == true){
            
            Cloud c = CloudQuery.executeCloudQuery(cloudid);
            
            if(c != null){
     
                setOne_cloud(c);
            
                JSONArray sites = one_cloud.getSites();
            
            
                String site_id;
                String site_name;
                Site s;
            
                sites_in_cloud = new ArrayList();
                sites_not_in_cloud = new ArrayList();
            
                if( sites != null){
                
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
        
        }else{
            
            return Action.ERROR;
        
        }
        
        
        
    }
}
