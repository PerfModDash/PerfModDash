/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;

/**
 *
 * @author siliu
 */
public class CloudARMatrix {
    
    /**
     * The method to execute the operation of adding or removing matrices to or from a cloud in the data store 
     * by providing cloud id, button flag(action category), matrices list
     * @param cloudid The cloud that one would like to add or remove sites.
     * @param button_flag The action that one would like to take: 1 is "remove matrices from cloud"; 2 is "add matrices to cloud"
     * @param matrix_array The matrix list that one would like to add or remove.
     * @return true if add/remove success, false otherwise
     */
    
    public static boolean executeCloudARMatrix(String cloudid, int button_flag, JSONArray matrix_array){
    
        DataStoreConfig cfg = new DataStoreConfig();
	String storeURL = cfg.getProperty("storeURL");	
	String CLOUD = PsApi.CLOUD;
        
        
        String command = "";
    
        if(button_flag == 1){
			
            command = storeURL + CLOUD + "/" + cloudid + "/" + PsApi.CLOUD_REMOVE_MATRIX_IDS;
			
	}else if(button_flag == 2){
            
            command = storeURL + CLOUD + "/" + cloudid + "/" + PsApi.CLOUD_ADD_MATRIX_IDS;
			
	}
        
        try{
		
            URL commandURL = new URL(command);
            HttpURLConnection cloud_conn = (HttpURLConnection)commandURL.openConnection();
			
            cloud_conn.setDoOutput(true);
            cloud_conn.setDoInput(true);
            cloud_conn.setInstanceFollowRedirects(false);
            cloud_conn.setUseCaches (false);
            cloud_conn.setRequestMethod("PUT");
			
            DataOutputStream out = new DataOutputStream(cloud_conn.getOutputStream());
            out.writeBytes(matrix_array.toJSONString());
            out.flush();
            out.close();
			
            int resp_code = cloud_conn.getResponseCode();
            String resp_message = cloud_conn.getResponseMessage();
            
            if(resp_code == 200){
			
		System.out.println(resp_message + "<br>");
                System.out.println("Add/Remove matrices from cloud success!");
                return true;
                
            }else{
		System.out.println(resp_message  + "<br>");
		System.out.println("Add/Remove matrices from cloud failed!");
                return false;
            }
                        
         }catch(IOException e){
             
            e.printStackTrace();
            System.out.println("Add/Remove matrices from cloud failed!");
            return false;
	}
    
    }
    
    
}
