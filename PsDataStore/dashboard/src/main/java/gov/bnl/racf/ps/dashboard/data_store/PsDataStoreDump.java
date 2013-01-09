/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;

import gov.bnl.racf.ps.dashboard.data_objects.*;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import java.util.Iterator;
import org.json.simple.JSONObject;

/**
 * dumps content of data store to a string variable, normally
 * to be printed on web page for debugging purposed. 
 * Class should be used for development only, not to be used for production.
 * @author tomw
 */
public class PsDataStoreDump {
    public static String serviceTypes(){
        PsDataStore dataStore = PsDataStore.getDataStore();
        String result="";
        
        Iterator<PsServiceType> iter = dataStore.serviceTypeIterator();
        while(iter.hasNext()){
            PsServiceType type = (PsServiceType)iter.next();
            JSONObject json = JsonConverter.toJson(type);
            result=result+json.toString()+"<BR>----<BR>";
        }
        return result;
    }
    public static String hosts(){
        PsDataStore dataStore = PsDataStore.getDataStore();
        String result="";
        
        Iterator<PsHost> iter = dataStore.hostIterator();
        while(iter.hasNext()){
            PsHost host = (PsHost)iter.next();
            JSONObject json = JsonConverter.toJson(host);
            result=result+json.toString()+"<BR>----<BR>";
        }
        return result;
    }
    
    public static String services(){
        PsDataStore dataStore = PsDataStore.getDataStore();
        String result="";
        
        Iterator<PsService> iter = dataStore.serviceIterator();
        while(iter.hasNext()){
            PsService service = (PsService)iter.next();
            JSONObject json = JsonConverter.toJson(service);
            result=result+json.toString()+"<BR>----<BR>";
        }
        return result;
    }
    
    /**
     * dump the information about known sites
     * @return 
     */
    public static String sites(){
        PsDataStore dataStore = PsDataStore.getDataStore();
        String result="";
        
        Iterator<PsSite> iter = dataStore.siteIterator();
        while(iter.hasNext()){
            PsSite site = (PsSite)iter.next();
            JSONObject json = JsonConverter.toJson(site);
            result=result+json.toString()+"<BR>----<BR>";
        }
        return result;
    }
    
    public static String clouds(){
        PsDataStore dataStore = PsDataStore.getDataStore();
        String result="";
        
        Iterator<PsCloud> iter=dataStore.cloudIterator();
        while(iter.hasNext()){
            PsCloud currentCloud = (PsCloud)iter.next();
            JSONObject json = JsonConverter.toJson(currentCloud);
            result = result+json.toString()+"<BR>----<BR>";
        }
        return result;
    }
    
    
     public static String matrices(){
        PsDataStore dataStore = PsDataStore.getDataStore();
        String result="";
        
        Iterator<PsMatrix> iter=dataStore.matrixIterator();
        while(iter.hasNext()){
            PsMatrix currentMatrix = (PsMatrix)iter.next();
            JSONObject json = JsonConverter.toJson(currentMatrix);
            result = result+json.toString()+"<BR>----<BR>";
        }
        return result;
    }
    
}
