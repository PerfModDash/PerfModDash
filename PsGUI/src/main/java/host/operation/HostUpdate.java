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

/**
 * The class to update a host attributes in the data store.
 * @author siliu
 */
public class HostUpdate {
    
    /**
     * The method to execute the update of a host by providing the host id and the new host json object.
     * @param hostObj The updated new host json object
     * @param hostid  The host one would like to update
     * @return The updated host object
     * @throws IOException 
     */
    public static Host executeHostUpdate(JSONObject hostObj,String hostid) throws IOException{
        
        Host newHost = new Host();
        System.out.print("hostid: "+hostid);
        
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
            host_conn.setRequestMethod("PUT");
				
            DataOutputStream out = new DataOutputStream(host_conn.getOutputStream());
            out.writeBytes(hostObj.toJSONString());
            out.flush();
            out.close();
				
            int resp_code = host_conn.getResponseCode();
            String resp_message = host_conn.getResponseMessage();
				
            if(resp_code == 200){
                
                System.out.println("Host modified!");
                
		BufferedReader reader = new BufferedReader(new InputStreamReader(host_conn.getInputStream()));
                String line = reader.readLine();
		while (line != null){
                    line = reader.readLine();
                    System.out.println(line);
		}
                
                String hostname = (String) hostObj.get("hostname");
                String ipv4 = (String) hostObj.get("ipv4");
                String ipv6 = (String) hostObj.get("ipv6");
            
                newHost.setHostid(hostid);
                newHost.setHostname(hostname);
                newHost.setIpv4(ipv4);
                newHost.setIpv6(ipv6);
                
              
            }else{
		System.out.println("Failed to modify host!");
                newHost = null;
            }
            
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to modify host!");
            newHost = null;
        }
        
        return newHost;
    }
    
}
