/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.operation;

import config.DataStoreConfig;
import config.PsApi;
import host.bean.Host;
import host.bean.HostList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * The class to query the list of hosts (ids and names) in the data store.
 * @author siliu
 */
public class HostListQuery {
    /**
     * The method to execute the query of host list.
     * @return The HostList object.
     */
    public static HostList executeHostListQuery(){
        
        HostList host_list = new HostList();
        
        JSONParser parser = new JSONParser();
	DataStoreConfig cfg = new DataStoreConfig();
	String HOST = PsApi.HOST;
	String hostURL = cfg.getProperty("storeURL") + HOST;
        
        try{
            URL url = new URL(hostURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Object obj = parser.parse(reader);
            JSONArray hostIds= (JSONArray)obj;
            
            int hostNumber = hostIds.size();
			
	    Host one_host = new Host();
			
            JSONArray hostNames = new JSONArray();
            String host_id;
            String host_name;
            
            for(int i=0 ; i<hostIds.size() ; i++){
                
                host_id = (String) hostIds.get(i);
                one_host = HostQuery.executeHostQuery(host_id);
                host_name = one_host.getHostname();
                hostNames.add(host_name);
				
            }
            
            host_list.setHostIds(hostIds);
            host_list.setHostNames(hostNames);
            host_list.setHostNumber(hostNumber);
           
		
	}catch(IOException e){
            host_list = null;
            e.printStackTrace();
			
	}catch(ParseException e){
            host_list = null;
            e.printStackTrace();
			
	}
        
        return host_list;
	
    
    }
    
}
