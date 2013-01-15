/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.operation;

import cloud.bean.Cloud;
import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;

/**
 * The class to update the name of a cloud in the data store.
 * @author siliu
 */
public class CloudUpdate {
    /**
     * The method to execute the update of a cloud by providing the cloud id.
     * @param cloudid The cloud one would like to update
     * @param cloudObj The updated new cloud object
     * @return The updated cloud object
     */
    public static Cloud executeCloudUpdate(String cloudid,JSONObject cloudObj){
        
        Cloud newCloud = new Cloud();
        System.out.print("Cloud Update: "+ cloudid);
        
        DataStoreConfig cfg = new DataStoreConfig();
	String CLOUD = PsApi.CLOUD;
        String cloudURL = cfg.getProperty("storeURL") + CLOUD + '/' + cloudid;
        
        try {
				
            URL url = new URL(cloudURL);
            HttpURLConnection cloud_conn = (HttpURLConnection)url.openConnection();
				
            cloud_conn.setDoOutput(true);
            cloud_conn.setDoInput(true);
            cloud_conn.setInstanceFollowRedirects(false);
            cloud_conn.setUseCaches (false);
            cloud_conn.setRequestMethod("PUT");
            
            DataOutputStream out = new DataOutputStream(cloud_conn.getOutputStream());
            out.writeBytes(cloudObj.toJSONString());
            out.flush();
            out.close();
				
            int resp_code = cloud_conn.getResponseCode();
            String resp_message = cloud_conn.getResponseMessage();
				
            if(resp_code == 200){
                System.out.println("Cloud edited!");
			
                BufferedReader reader = new BufferedReader(new InputStreamReader(cloud_conn.getInputStream()));
                String line = reader.readLine();
                while (line != null){
				
                    line = reader.readLine();
                    System.out.println(line);
            
                }
                
                String cloudname = (String) cloudObj.get("name");
                
                newCloud.setCloudid(cloudid);
                newCloud.setCloudname(cloudname);
                
					
            }else{
                System.out.println("Failed to edit cloud!");
                newCloud = null;
					
            }	
				
	} catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to edit cloud!");
            newCloud = null;
	}
        
        return newCloud;
        
    }
    
}
