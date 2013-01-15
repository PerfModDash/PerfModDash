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
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The class to delete a site from the data store.
 * @author siliu
 */
public class SiteDelete {
    
    /**
     * The method to execute the deletion of a site by providing the site id.
     * @param siteid The site one would like to delete.
     * @return true if delete success, false otherwise
     */
    public static boolean executeSiteDelete(String siteid){
        
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
            site_conn.setRequestMethod("DELETE");
            
            int resp_code = site_conn.getResponseCode();
            String resp_message = site_conn.getResponseMessage();
            
            if(resp_code ==200){
                System.out.println("Site deleted!");
			
                BufferedReader reader = new BufferedReader(new InputStreamReader(site_conn.getInputStream()));
                String line = reader.readLine();
                while (line != null){
				
                    line = reader.readLine();
                    System.out.println(line);
            
                }
                
                return true;   
                 
            }else{
                System.out.println(resp_message);
		System.out.println("Failed to delete site!");
                return false;
            
            }
                

	} catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to delete site!");
            return false;
	}
    
    
    
    }
    
}
