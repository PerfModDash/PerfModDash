/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import java.util.Iterator;
import org.hibernate.Session;
import org.json.simple.JSONArray;

/**
 * Performs operations on PsSite objects
 * @author tomw
 */
public class PsSiteManipulator {
   
    /**
     * 
     * @param session
     * @param site
     * @param host 
     */
    public static void addHost(Session session,PsSite site, PsHost host){
        site.addHost(host);
    }
    
    /**
     * add host hostId to site
     * @param session
     * @param site
     * @param hostId 
     */
    public static void addHostId(Session session,PsSite site, int hostId){
        PsHost host = PsDataStore.getHost(session,hostId);
        addHost(session,site,host);
    }
   /**
    * add list of hosts defined by host ids in JSONArray
    * @param session
    * @param site
    * @param listOfHostIds 
    */
    public static void addHosts(Session session,PsSite site, JSONArray listOfHostIds){
        Iterator iter = listOfHostIds.iterator();
        while(iter.hasNext()){
            String hostIdString  = (String)iter.next();
            int hostId = Integer.parseInt(hostIdString);
            addHostId(session, site,hostId);
        }
    }
    
    /**
     * remove host from site
     * @param session
     * @param site
     * @param host 
     */
    public static void removeHost(Session session,PsSite site, PsHost host){
        site.removeHost(host);
    }
    /**
     * remove host hostId from site
     * @param session
     * @param site
     * @param hostId 
     */
    public static void removeHostId(Session session,PsSite site, int hostId){
        PsHost host = PsDataStore.getHost(session,hostId);
        removeHost(session,site,host);
    }
    /**
     * remove list of hosts defined by host ids in JSONArray
     * @param session
     * @param site
     * @param listOfHostIds 
     */
    public static void removeHosts(Session session,PsSite site, JSONArray listOfHostIds){
        Iterator iter = listOfHostIds.iterator();
        while(iter.hasNext()){
            String hostIdString  = (String)iter.next();
            int hostId = Integer.parseInt(hostIdString);
            removeHostId(session,site,hostId);
        }
    }
}
