/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;

import gov.bnl.racf.ps.dashboard.data_objects.PsSite;
import java.util.Iterator;
import java.util.Vector;


/**
 * Utility class with methods for querying list of sites
 * @author tomw
 */
public class PsSiteQuery {
    /**
     * get iterator for list of sites in data store
     * @return 
     */
    public static Iterator<PsSite> getSiteIterator(){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Iterator<PsSite> iter = psDataStore.siteIterator();
        return iter;
    }
    /**
     * return site identified by siteName, null if not found
     * @param siteName
     * @return 
     */
    public static PsSite getSiteByName(String siteName){
        Iterator<PsSite> iter = getSiteIterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.getName().equals(siteName)){
                return currentSite;
            }
        }
        return null;
    }
    
    /**
     * get all sites which contain host with hostName
     * @param hostName
     * @return 
     */
    public static Vector<PsSite> sitesWhichContainHostName(String hostName){
        Vector<PsSite> sites = new Vector<PsSite>();
        Iterator<PsSite> iter = getSiteIterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.containsHostName(hostName)){
                sites.add(currentSite);
            }
        }
        return sites;
    }
    /**
     * get list of sites with given status
     * @param status
     * @return 
     */
    public static Vector<PsSite> getSitesByStatus(int status){
        Iterator<PsSite> iter = getSiteIterator();
        Vector<PsSite> listOfSites = new Vector<PsSite>();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.getStatus()==status){
                listOfSites.add(currentSite);
            }
        }
        return listOfSites;
    }
}
