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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import site.bean.Site;

/**
 * The class to create a new site in the data store.
 * @author siliu
 */
public class SiteCreate {
    /**
     * The method to execute the creation of a new site by providing the new site json object.
     * @param siteObj The new site json object.
     * @return 
     */
    public static Site executeSiteCreate(JSONObject siteObj){
        
        Site newSite = new Site();
        
        DataStoreConfig cfg = new DataStoreConfig();
        String SITE = PsApi.SITE;
        String siteURL = cfg.getProperty("storeURL") + SITE;   
            
        try {
                
            URL url = new URL(siteURL);
			
            HttpURLConnection site_conn = (HttpURLConnection)url.openConnection();
			
            site_conn.setDoOutput(true);
            site_conn.setDoInput(true);
            site_conn.setInstanceFollowRedirects(false);
            site_conn.setUseCaches (false);
            site_conn.setRequestMethod("POST");
			
            DataOutputStream out = new DataOutputStream(site_conn.getOutputStream());
            out.writeBytes(siteObj.toJSONString());
            out.flush();
            out.close();
            
            int resp_code = site_conn.getResponseCode();
            String resp_message = site_conn.getResponseMessage();
            
            if(resp_code ==200){
                System.out.println("Site created!");
			
                BufferedReader reader = new BufferedReader(new InputStreamReader(site_conn.getInputStream()));
                /*
                String line = reader.readLine();
                while (line != null){
				
                    line = reader.readLine();
                    System.out.println(line);
            
                }
                */
                
                
                try{
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(reader);
                    JSONObject newSiteObj = (JSONObject)obj;

                    String siteid = (String)newSiteObj.get("id");
                    String sitename = (String)newSiteObj.get("name");
                    //long status = (Long) siteObj.get("status");
                    String description = (String)newSiteObj.get("description");
            
                    newSite.setSiteid(siteid);
                    newSite.setSitename(sitename);
                    //newSite.setStatus(status);
                    newSite.setDescription(description);
                
                
                }catch(ParseException e){
         
                    e.printStackTrace();
		
                }catch(ClassCastException e){
          
                    e.printStackTrace();
		
                }
                
                
                 
            }else{
                System.out.println("Failed to create site!");
                newSite = null;
            
            }
                

	} catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create site!");
            newSite = null;
	}
        return newSite;
    }
    
}
