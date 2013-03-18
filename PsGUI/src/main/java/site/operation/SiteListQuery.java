/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import site.bean.SiteList;

/**
 * The class to query the list of sites (ids and names) in the data store.
 * @author siliu
 */
public class SiteListQuery {
    
    /**
     * The method to execute the query of site list.
     * @return The SiteList object.
     */
    public static SiteList executeSiteListQuery(){
        
        SiteList site_list = new SiteList();
        
        JSONParser parser = new JSONParser();
	DataStoreConfig cfg = new DataStoreConfig();
	String SITE = PsApi.SITE;
        String siteURL = cfg.getProperty("storeURL") + SITE;
        
        try{
            URL url = new URL(siteURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Object obj = parser.parse(reader);
            /*
            JSONArray siteIds= (JSONArray)obj;
            int siteNumber = siteIds.size();
            
            
            Site one_site;
            JSONArray siteNames = new JSONArray();
            String site_id;
            String site_name;
            
            for(int i=0 ; i<siteIds.size() ; i++){
                
                site_id = (String) siteIds.get(i);
                one_site = SiteQuery.executeSiteQuery(site_id);
                site_name = one_site.getSitename();
                siteNames.add(site_name);
				
            }
            */
            
            JSONArray siteObjects = (JSONArray)obj;
            
            int siteNumber = siteObjects.size();
            JSONArray siteIds = new JSONArray();
            JSONArray siteNames = new JSONArray();
            
            JSONObject siteObj = new JSONObject();
            String site_id;
            String site_name;
            
            for(int i=0; i<siteNumber; i++){
            
                siteObj = (JSONObject) siteObjects.get(i);
                site_id = (String) siteObj.get("id");
                site_name = (String) siteObj.get("name");
                
                siteIds.add(site_id);
                siteNames.add(site_name);
            
            }
            
            
            site_list.setSiteIds(siteIds);
            site_list.setSiteNames(siteNames);
            site_list.setSiteNumber(siteNumber);
			
		
	}catch(IOException e){
            site_list = null;
            e.printStackTrace();
			
	}catch(ParseException e){
            
            site_list = null;
            e.printStackTrace();
	
	}
        
        return site_list;
    
    
    }
    
}
