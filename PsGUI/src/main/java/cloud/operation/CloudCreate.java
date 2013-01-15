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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 * The class to create a new cloud in the data store.
 * @author siliu
 */
public class CloudCreate {
    /**
     * The method to execute the creation of a new cloud by providing the new cloud json object.
     * @param cloudObj The new cloud json object.
     * @return The newly created cloud object.
     */
    public static Cloud executeCloudCreate(JSONObject cloudObj){
    
        Cloud newCloud = new Cloud();
        
	DataStoreConfig cfg = new DataStoreConfig();
	String CLOUD = PsApi.CLOUD;
	String cloudURL = cfg.getProperty("storeURL") + CLOUD;
        
        try {
				
            URL url = new URL(cloudURL);
            HttpURLConnection cloud_conn = (HttpURLConnection)url.openConnection();
				
            cloud_conn.setDoOutput(true);
            cloud_conn.setDoInput(true);
            cloud_conn.setInstanceFollowRedirects(false);
            cloud_conn.setUseCaches (false);
            cloud_conn.setRequestMethod("POST");
            
            DataOutputStream out = new DataOutputStream(cloud_conn.getOutputStream());
            out.writeBytes(cloudObj.toJSONString());
            out.flush();
            out.close();
				
            int resp_code = cloud_conn.getResponseCode();
            String resp_message = cloud_conn.getResponseMessage();
				
            if(resp_code == 200){
                System.out.println("Cloud created!");
			
                BufferedReader reader = new BufferedReader(new InputStreamReader(cloud_conn.getInputStream()));
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
                    JSONObject newCloudObj = (JSONObject)obj;

                    String cloudid = (String) newCloudObj.get("id");
                    String cloudname = (String) newCloudObj.get("name");
                
                    newCloud.setCloudid(cloudid);
                    newCloud.setCloudname(cloudname);
                
                
                }catch(ParseException e){
         
                    e.printStackTrace();
		
                }catch(ClassCastException e){
          
                    e.printStackTrace();
		
                }
                
               
                			
            }else{
                System.out.println("Failed to create cloud!");
                newCloud = null;
					
            }	
				
	} catch (IOException e) {
            System.out.println("Failed to create cloud!");
            newCloud = null;
	    e.printStackTrace();
        }
    
        return newCloud;
    
    }
    
}
