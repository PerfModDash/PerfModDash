/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.operation;

import config.DataStoreConfig;
import config.PsApi;
import host.bean.Host;
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
 * The class to create a new host in the data store.
 * @author siliu
 */
public class HostCreate {
    /**
     * The method to execute the creation of a new host by providing the new host json object.
     * @param hostObj The new host json object.
     * @return The newly created host object.
     */
    public static Host executeHostCreate(JSONObject hostObj){
        
        Host newHost = new Host();
        
        DataStoreConfig cfg = new DataStoreConfig();
        String HOST = PsApi.HOST;
        String hostURL = cfg.getProperty("storeURL") + HOST;   
            
        try {
                
            URL url = new URL(hostURL);
			
            HttpURLConnection host_conn = (HttpURLConnection)url.openConnection();
			
            host_conn.setDoOutput(true);
            host_conn.setDoInput(true);
            host_conn.setInstanceFollowRedirects(false);
            host_conn.setUseCaches (false);
            host_conn.setRequestMethod("POST");
			
            DataOutputStream out = new DataOutputStream(host_conn.getOutputStream());
            out.writeBytes(hostObj.toJSONString());
            out.flush();
            out.close();
            
            int resp_code = host_conn.getResponseCode();
            String resp_message = host_conn.getResponseMessage();
            
            if(resp_code ==200){
                System.out.println("Host created!");
                
			
                BufferedReader reader = new BufferedReader(new InputStreamReader(host_conn.getInputStream()));
                /*
                String line = reader.readLine();
                
                while (line != null){
                    
		    System.out.println(line);	
                    line = reader.readLine();       
            
                }
                */
                
                try{
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(reader);
                    JSONObject newHostObj = (JSONObject)obj;
                
                    //System.out.println(newHostObj);
                    
                    String hostid = (String)newHostObj.get("id");
                    System.out.println("hostid: " + hostid);
                    String hostname = (String) newHostObj.get("hostname");
                    String ipv4 = (String) newHostObj.get("ipv4");
                    String ipv6 = (String) newHostObj.get("ipv6");
            
                    newHost.setHostid(hostid);
                    newHost.setHostname(hostname);
                    newHost.setIpv4(ipv4);
                    newHost.setIpv6(ipv6);
                
                
                }catch(ParseException e){
         
                    e.printStackTrace();
		
                }catch(ClassCastException e){
          
                    e.printStackTrace();
		
                }
                
                
                 
            }else{
                System.out.println("Failed to create host!");
                newHost = null;
            
            }
                

	} catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create host!");
             newHost = null;
	}
        return newHost;
    }
}
