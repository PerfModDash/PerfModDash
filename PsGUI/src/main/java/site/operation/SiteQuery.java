/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.operation;

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
import site.bean.Site;

/**
 * The class to query a site from the data store.
 * @author siliu
 */
public class SiteQuery {
    
    /**
     * The method to execute the query of a site by providing the site id.
     * @param siteid The site one would like to query.
     * @return The queried site object.
     */
    public static Site executeSiteQuery(String siteid){
        
        Site one_site  = new Site();
        System.out.print("siteid: " + siteid);
        
        one_site.setSiteid(siteid);
        
	JSONParser parser = new JSONParser();
        
	DataStoreConfig cfg = new DataStoreConfig();	
	String SITE = PsApi.SITE;
	String siteURL = cfg.getProperty("storeURL") + SITE + '/' + siteid;
        
		
	try{
            URL url = new URL(siteURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
            Object obj = parser.parse(reader);
			
            JSONObject siteObj = (JSONObject)obj;
			
            String sitename = (String) siteObj.get("name");
            long status = (Long)siteObj.get("status");
            String description = (String) siteObj.get("description");
            JSONArray hosts = (JSONArray)siteObj.get("hosts");
            
            one_site.setSitename(sitename);
            one_site.setStatus(status);
            one_site.setDescription(description);
            one_site.setHosts(hosts);
			
        }catch(IOException e){
            one_site = null;
            e.printStackTrace();
		
			
        }catch(ParseException e){
            one_site = null;
            e.printStackTrace();
		
        }catch(ClassCastException e){
            one_site = null;
            e.printStackTrace();
		
        }
       
        return one_site;
    
    
    
    
    }
    
}
