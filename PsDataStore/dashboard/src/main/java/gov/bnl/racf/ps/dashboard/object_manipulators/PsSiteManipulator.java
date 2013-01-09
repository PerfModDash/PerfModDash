/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import java.util.Iterator;
import org.json.simple.JSONArray;

/**
 * Performs operations on PsSite objects
 * @author tomw
 */
public class PsSiteManipulator {
    /**
     * add host to a site
     * @param site
     * @param host 
     */
    public static void addHost(PsSite site, PsHost host){
        site.addHost(host);
    }
    /**
     * add host hostId to site
     * @param site
     * @param hostId 
     */
    public static void addHostId(PsSite site, String hostId){
        PsDataStore dataStore = PsDataStore.getDataStore();
        PsHost host = dataStore.getHost(hostId);
        addHost(site,host);
    }
    /**
     * add list of hosts defined by host ids in JSONArray
     * @param site
     * @param listOfHostIds 
     */
    public static void addHosts(PsSite site, JSONArray listOfHostIds){
        Iterator iter = listOfHostIds.iterator();
        while(iter.hasNext()){
            String hostId = (String)iter.next();
            addHostId(site,hostId);
        }
    }
    
    /**
     * remove host from site
     * @param site
     * @param host 
     */
    public static void removeHost(PsSite site, PsHost host){
        site.removeHost(host);
    }
    /**
     * remove host hostId from site
     * @param site
     * @param hostId 
     */
    public static void removeHostId(PsSite site, String hostId){
        PsDataStore dataStore = PsDataStore.getDataStore();
        PsHost host = dataStore.getHost(hostId);
        removeHost(site,host);
    }
    /**
     * remove list of hosts defined by host ids in JSONArray
     * @param site
     * @param listOfHostIds 
     */
    public static void removeHosts(PsSite site, JSONArray listOfHostIds){
        Iterator iter = listOfHostIds.iterator();
        while(iter.hasNext()){
            String hostId = (String)iter.next();
            removeHostId(site,hostId);
        }
    }
}
