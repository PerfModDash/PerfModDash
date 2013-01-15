/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;

/**
 * The class to add or remove hosts to or from a site in the data store.
 * @author siliu
 */
public class SiteARHost {
    
    /**
     * The method to execute the operation of adding or removing hosts to or from a site in the data store 
     * by providing site id, button flag(action category), host list.
     * @param siteid The site that one would like to add or remove sites.
     * @param button_flag The action that one would like to take: 1 is "remove hosts from site"; 2 is "add hosts to site"
     * @param host_array The host list that one would like to add or remove.
     * @return true if add/remove success, false otherwise
     */
    public static boolean executeSiteARHost(String siteid, int button_flag, JSONArray host_array){
        
        DataStoreConfig cfg = new DataStoreConfig();
	String storeURL = cfg.getProperty("storeURL");
	String SITE = PsApi.SITE;
        
        String command = "";
        
        if(button_flag == 1){
			
            command = storeURL + SITE + "/" + siteid + "/" + PsApi.SITE_REMOVE_HOST_IDS;
			
	}else if(button_flag == 2){
            
            command = storeURL + SITE + "/" + siteid + "/" + PsApi.SITE_ADD_HOST_IDS;
			
	}
        
        try{
		
            URL commandURL = new URL(command);
            HttpURLConnection site_conn = (HttpURLConnection)commandURL.openConnection();
            site_conn.setDoOutput(true);
            site_conn.setDoInput(true);
            site_conn.setInstanceFollowRedirects(false);
            site_conn.setUseCaches (false);
            site_conn.setRequestMethod("PUT");
            
            DataOutputStream out = new DataOutputStream(site_conn.getOutputStream());
            out.writeBytes(host_array.toJSONString());
            out.flush();
            out.close();
            
            int resp_code = site_conn.getResponseCode();
            String resp_message = site_conn.getResponseMessage();
            
            if(resp_code == 200){
		
                System.out.println("Add/Remove hosts from site success!");
                return true;
                
            }else{
		System.out.println(resp_message  + "<br>");
		System.out.println("Add/Remove hosts from site failed!");
                return false;
            }
            
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Add/Remove hosts from site failed!");
            return false;
	}
    
    
    }
    
}
