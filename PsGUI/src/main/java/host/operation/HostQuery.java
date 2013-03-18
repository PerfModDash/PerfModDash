/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package host.operation;

import config.DataStoreConfig;
import config.PsApi;
import host.bean.Host;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The class to query a host from the data store.
 * @author siliu
 */
public class HostQuery {
    /**
     * The method to execute the query of a host by providing the host id.
     * @param hostid The host one would like to query.
     * @return The queried host object.
     */
    public static Host executeHostQuery(String hostid){
        
        
        Host one_host  = new Host();
        System.out.print("hostid: " + hostid);
        one_host.setHostid(hostid);
        
	JSONParser parser = new JSONParser();
        
	DataStoreConfig cfg = new DataStoreConfig();	
	String HOST = PsApi.HOST;
        String detailLevel = PsApi.DETAIL_LEVEL_PARAMETER;
        String medium = PsApi.DETAIL_LEVEL_MEDIUM;
	String hostURL = cfg.getProperty("storeURL") + HOST + '/' + hostid + "?" + detailLevel + "=" + medium;
        
		
	try{
            URL url = new URL(hostURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
            Object obj = parser.parse(reader);
			
            JSONObject hostObject = (JSONObject)obj;
			
            String hostname = (String) hostObject.get("hostname");
            String ipv4 = (String) hostObject.get("ipv4");
            String ipv6 = (String) hostObject.get("ipv6");
            
            //get the id of the primitive services
            JSONArray serviceObjList = (JSONArray)hostObject.get("services");
            JSONArray services = new JSONArray();
            JSONObject serviceObj = new JSONObject();
            String service_id;
            
            for(int i=0 ; i<serviceObjList.size() ; i++){
                serviceObj = (JSONObject) serviceObjList.get(i);
                service_id = (String) serviceObj.get("id");
                services.add(service_id);
            }
            
            one_host.setHostname(hostname);
            one_host.setIpv4(ipv4);
            one_host.setIpv6(ipv6);
            one_host.setServices(services);
			
            }catch(IOException e){
                one_host = null;
		e.printStackTrace();
		
			
            }catch(ParseException e){
                one_host = null;
		e.printStackTrace();
		
            }catch(ClassCastException e){
                one_host = null;
		e.printStackTrace();
		
            }
       
        return one_host;
    
    
    }
    
}
