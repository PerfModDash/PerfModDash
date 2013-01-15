/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import site.bean.Site;

/**
 * The class to update a site attributes in the data store.
 * @author siliu
 */
public class SiteUpdate {
    
    /**
     * The method to execute the update of a site by providing the site id and the new site json object.
     * @param siteid The site one would like to update
     * @param siteObj The updated new site json object
     * @return The updated site object
     */
    public static Site executeSiteUpdate(String siteid,JSONObject siteObj){
        
        Site newSite = new Site();
        System.out.print("siteid: "+ siteid);
        
        DataStoreConfig cfg = new DataStoreConfig();
        String SITE = PsApi.SITE;
        String siteURL = cfg.getProperty("storeURL") + SITE + '/' + siteid;   
            
        try {
                
            URL url = new URL(siteURL);
			
            HttpURLConnection site_conn = (HttpURLConnection)url.openConnection();
			
            site_conn.setDoOutput(true);
            site_conn.setDoInput(true);
            site_conn.setInstanceFollowRedirects(false);
            site_conn.setUseCaches (false);
            site_conn.setRequestMethod("PUT");
			
            DataOutputStream out = new DataOutputStream(site_conn.getOutputStream());
            out.writeBytes(siteObj.toJSONString());
            out.flush();
            out.close();
            
            int resp_code = site_conn.getResponseCode();
            String resp_message = site_conn.getResponseMessage();
            
            if(resp_code ==200){
                System.out.println("Site edited!");
			
                BufferedReader reader = new BufferedReader(new InputStreamReader(site_conn.getInputStream()));
                String line = reader.readLine();
                while (line != null){
				
                    line = reader.readLine();
                    System.out.println(line);
            
                }
                
                
                String sitename = (String) siteObj.get("name");
                //long status = (Long) siteObj.get("status");
                String description = (String) siteObj.get("description");
            
                newSite.setSiteid(siteid);
                newSite.setSitename(sitename);
                //newSite.setStatus(resp_code);
                newSite.setDescription(description);
                
                 
            }else{
                System.out.println("Failed to edit site!");
                newSite = null;
            
            }
                

	} catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to edit site!");
            newSite = null;
	}
        
        
        return newSite;
    
    
    
    }
    
}
