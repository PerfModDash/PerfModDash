/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The class to delete a cloud from the data store.
 * @author siliu
 */
public class CloudDelete {
    /**
     * The method to execute the deletion of a cloud by providing the cloud id.
     * @param cloudid The cloud one would like to delete.
     * @return true if delete success, false otherwise
     */
    public static boolean executeCloudDelete(String cloudid){
        
        System.out.print("cloudid: "+ cloudid);
        
        DataStoreConfig cfg = new DataStoreConfig();
        String CLOUD = PsApi.CLOUD;
        
        String cloudURL = cfg.getProperty("storeURL") + CLOUD + '/' + cloudid;
        
        try {
				
		URL requestURL = new URL(cloudURL);
		HttpURLConnection cloud_conn = (HttpURLConnection)requestURL.openConnection();
				
		cloud_conn.setDoOutput(true);
				
                cloud_conn.setDoInput(true);
		cloud_conn.setInstanceFollowRedirects(false);
                cloud_conn.setUseCaches (false);
		cloud_conn.setRequestMethod("DELETE");
				
                int resp_code = cloud_conn.getResponseCode();
		String resp_message = cloud_conn.getResponseMessage();
                
                
                if(resp_code ==200){
                    System.out.println("Cloud deleted!");
			
                    BufferedReader reader = new BufferedReader(new InputStreamReader(cloud_conn.getInputStream()));
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
                
        }catch(IOException e) {
            e.printStackTrace();
            System.out.println("Failed to delete cloud!");
            return false;
	}
    
    
    
    }
    
}
