/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitiveServ.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;


/**
 * The class to add or remove primitive services to or from a host in the data store.
 * @author siliu
 */
public class PrimitiveServARtoHost {
    
    /**
     * The method to execute the operation of adding or removing primitive services to or from a host in the data store 
     * by providing host id, button flag(action category), service list.
     * @param hostid The host that one would like to add or remove primitive services.
     * @param button_flag The action that one would like to take: 1 is "remove primitive services from host"; 2 is "add primitive services to host"
     * @param serv_array The primitive service list that one would like to add or remove.
     * @return true if add/remove success, false otherwise
     */
    public static boolean executePrimitiveServARtoHost(String hostid,int button_flag, JSONArray serv_array){
        
        DataStoreConfig cfg = new DataStoreConfig();
	String storeURL = cfg.getProperty("storeURL");
	String HOST = PsApi.HOST;
        
        String command = "";
        
        if(button_flag == 1){
			
            command = storeURL + HOST + "/" + hostid + "/" + PsApi.HOST_ADD_THROUGHPUT_SERVICES_COMMAND;
			
	}else if(button_flag == 2){
            
            command = storeURL + HOST + "/" + hostid + "/" + PsApi.HOST_ADD_LATENCY_SERVICES_COMMAND;
			
	}else if(button_flag == 3){
            
            command = storeURL + HOST + "/" + hostid + "/" + PsApi.HOST_ADD_ALL_SERVICES_COMMAND;
			
	}else if(button_flag == 4){
            
            command = storeURL + HOST + "/" + hostid + "/" + PsApi.HOST_REMOVE_ALL_SERVICES_COMMAND;
			
	}else if(button_flag == 5){
            
            command = storeURL + HOST + "/" + hostid + "/" + PsApi.HOST_REMOVE_SERVICE_TYPE_COMMAND;;
			
	}else if(button_flag == 6){
            
            command = storeURL + HOST + "/" + hostid + "/" + PsApi.HOST_ADD_SERVICE_TYPE_COMMAND;
			
	}
        
        
        try{
            URL url = new URL(command);
			
            HttpURLConnection host_conn = (HttpURLConnection)url.openConnection();
			
            host_conn.setDoOutput(true);
            host_conn.setDoInput(true);
            host_conn.setInstanceFollowRedirects(false);
            host_conn.setUseCaches (false);
            host_conn.setRequestMethod("PUT");
			
            if(button_flag == 5){
                DataOutputStream out1 = new DataOutputStream(host_conn.getOutputStream());
                out1.writeBytes(serv_array.toJSONString());
                out1.flush();
                out1.close();
             }else if(button_flag == 6){
                DataOutputStream out2 = new DataOutputStream(host_conn.getOutputStream());
                out2.writeBytes(serv_array.toJSONString());
                out2.flush();
                out2.close();

             }
			
            int resp_code = host_conn.getResponseCode();
            String resp_message = host_conn.getResponseMessage();
			
            if(resp_code == 200){
			
		System.out.println(resp_message + "<br>");
                return true;
                
            }else{
		System.out.println(resp_message  + "<br>");
		System.out.println("services change failed!");
                return false;
            }
	}catch(IOException e){
            e.printStackTrace();
            
            System.out.println("services change failed!<br>");
            return false;
	}
		
    }    
    
}
