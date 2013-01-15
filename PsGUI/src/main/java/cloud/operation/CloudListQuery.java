/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.operation;

import cloud.bean.Cloud;
import cloud.bean.CloudList;
import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The class to query the list of clouds (ids and names) in the data store.
 * @author siliu
 */
public class CloudListQuery {
    /**
     * The method to execute the query of cloud list.
     * @return: The cloud list object.
     */
    public static CloudList executeCloudListQuery(){
        
        CloudList cloud_list = new CloudList();
        
        JSONParser parser = new JSONParser();
	DataStoreConfig cfg = new DataStoreConfig();
	String CLOUD = PsApi.CLOUD;
	String cloudURL = cfg.getProperty("storeURL") + CLOUD;
        
        try{
            URL url = new URL(cloudURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Object obj = parser.parse(reader);
            
            JSONArray cloudIds= (JSONArray)obj;
            int cloudNumber = cloudIds.size();
            
            Cloud one_cloud;
            JSONArray cloudNames = new JSONArray();
            String cloud_id;
            String cloud_name;
            
            for(int i=0 ; i<cloudNumber ; i++){
                
                cloud_id = (String) cloudIds.get(i);
                one_cloud = CloudQuery.executeCloudQuery(cloud_id);
                cloud_name = one_cloud.getCloudname();
                cloudNames.add(cloud_name);
				
            }
            
            cloud_list.setCloudIds(cloudIds);
            cloud_list.setCloudNames(cloudNames);
            cloud_list.setCloudNumber(cloudNumber);
            
         
	}catch(IOException e){
            cloud_list = null;
            e.printStackTrace();
			
	}catch(ParseException e){
            cloud_list = null;
            e.printStackTrace();
			
	}
    
        return cloud_list;
    }
    
}
