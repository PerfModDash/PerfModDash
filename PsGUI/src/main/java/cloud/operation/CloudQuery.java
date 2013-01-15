/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.operation;

import cloud.bean.Cloud;
import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The class to query a cloud from the data store.
 * @author siliu
 */
public class CloudQuery {
    /**
     * The method to execute the query of a cloud by providing the cloud id.
     * @param cloudid The cloud one would like to query.
     * @return The queried cloud object
     */
    public static Cloud executeCloudQuery(String cloudid){
        
        Cloud one_cloud = new Cloud();
        
        JSONParser parser = new JSONParser();
	DataStoreConfig cfg = new DataStoreConfig();

	String CLOUD = PsApi.CLOUD;
	String cloudURL = cfg.getProperty("storeURL") + CLOUD + '/' + cloudid;
        
        
        try{
            URL url = new URL(cloudURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
            Object obj = parser.parse(reader);

            JSONObject cloudObj = (JSONObject)obj;
            
            String cloudname = (String) cloudObj.get("name");
            long status = (Long)cloudObj.get("status");
            JSONArray sites = (JSONArray)cloudObj.get("sites");
            JSONArray matrices = (JSONArray)cloudObj.get("matrices");
            
            one_cloud.setCloudid(cloudid);
            one_cloud.setCloudname(cloudname);
            one_cloud.setStatus(status);
            one_cloud.setSites(sites);
            one_cloud.setMatrices(matrices);
            
      
			
	}catch(IOException e){
            
            one_cloud = null;
            e.printStackTrace();
			
        }catch(ParseException e){
            one_cloud = null;
            e.printStackTrace();
			
	}catch(ClassCastException e){
            one_cloud = null;
            e.printStackTrace();
			
	}
    
        return one_cloud;

    }
    
}
