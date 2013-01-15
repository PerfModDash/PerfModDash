/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The class to delete a host from the data store.
 * @author siliu 
 */
public class HostDelete {
   /**
    * The method to execute the deletion of a host by providing the host id.
    * @param hostid The host one would like to delete.
    * @return true if delete success, false otherwise
    */
    public static boolean executeHostDelete(String hostid){
        
        System.out.print("execute:"+ hostid);
        DataStoreConfig cfg = new DataStoreConfig();
        String HOST = PsApi.HOST;
        String hostURL = cfg.getProperty("storeURL") + HOST + '/' + hostid;
        
        try {
				
            URL url = new URL(hostURL);
            HttpURLConnection host_conn = (HttpURLConnection)url.openConnection();
				
            host_conn.setDoOutput(true);
            host_conn.setDoInput(true);
            host_conn.setInstanceFollowRedirects(false);
            host_conn.setUseCaches (false);
            host_conn.setRequestMethod("DELETE");
				
            int resp_code = host_conn.getResponseCode();
            String resp_message = host_conn.getResponseMessage();
				
            if(resp_code == 200){
		System.out.println("Host deleted");
                
		BufferedReader reader = new BufferedReader(new InputStreamReader(host_conn.getInputStream()));
                String line = reader.readLine();
		while (line != null){
                    line = reader.readLine();
                    System.out.println(line);
		}		
		return true;		
				
            }else{
		System.out.println(resp_message);
		System.out.println("Failed to delete host!");
                return false;
            }
				
	} catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to delete host!");
            return false;
	}	

    
    }
    
}
