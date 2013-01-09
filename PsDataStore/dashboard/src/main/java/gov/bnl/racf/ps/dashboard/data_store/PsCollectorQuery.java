/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;


import gov.bnl.racf.ps.dashboard.data_objects.PsCollector;
import java.util.Iterator;


/**
 * utility class for querying collector objects in data store
 *
 * @author tomw
 */
public class PsCollectorQuery {

    /**
     * get iterator over collectors in the data store
     *
     * @return
     */
    public static Iterator<PsCollector> getCollectorIterator() {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Iterator iter = psDataStore.collectorIterator();
        return iter;
    }
    /**
     * get collector with a give id, return null if not found
     * @param collectorId
     * @return 
     */
    public static PsCollector getCollectorById(String collectorId){
        Iterator iter = getCollectorIterator();
        while(iter.hasNext()){
            PsCollector currentCollector = (PsCollector)iter.next();
            if(currentCollector.getId().equals(collectorId)){
                return currentCollector;
            }
        }
        return null;
    }
    /**
     * return collector with a given name, null if not found
     * @param name
     * @return 
     */
    public static PsCollector getCollectorByName(String name) {
        Iterator iter = getCollectorIterator();
        while (iter.hasNext()) {
            PsCollector currentCollector = (PsCollector) iter.next();
            if (currentCollector.getName().equals(name)) {
                return currentCollector;
            }
        }
        return null;
    }
    /**
     * get collector identified by hostname, null if not found
     * @param hostname
     * @return 
     */
    public static PsCollector getCollectorByHostname(String hostname){
        Iterator iter = getCollectorIterator();
        while (iter.hasNext()) {
            PsCollector currentCollector = (PsCollector) iter.next();
            if (currentCollector.getHostname().equals(hostname)){
                return currentCollector;
            }
        }
        return null;
    }
    
   /**
     * get collector identified by ipv4, null if not found
     * @param ipv4
     * @return 
     */
    public static PsCollector getCollectorByIpv4(String ipv4){
        Iterator iter = getCollectorIterator();
        while (iter.hasNext()) {
            PsCollector currentCollector = (PsCollector) iter.next();
            if (currentCollector.getIpv4().equals(ipv4)){
                return currentCollector;
            }
        }
        return null;
    } 
    
    
    /**
     * get collector identified by ipv4, null if not found
     * @param ipv4
     * @return 
     */
    public static PsCollector getCollectorByIpv6(String ipv6){
        Iterator iter = getCollectorIterator();
        while (iter.hasNext()) {
            PsCollector currentCollector = (PsCollector) iter.next();
            if (currentCollector.getIpv6().equals(ipv6)){
                return currentCollector;
            }
        }
        return null;
    } 
    
}
