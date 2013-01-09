/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import java.util.Iterator;


/**
 * Class with utility methods for querying hosts in data store
 * @author tomw
 */
public class PsHostQuery {
    /** get iterator for list of hosts in data store
     * 
     * @return 
     */
    public static Iterator<PsHost> getHostIterator(){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Iterator<PsHost> iter = psDataStore.hostIterator();
        return iter;
    }
    /**
     * return host identified by hostname
     * return null if not found
     * @param hostname
     * @return 
     */
    public static PsHost getHostByName(String hostname){
       Iterator<PsHost> iter = getHostIterator(); 

       while(iter.hasNext()){
           PsHost currentHost = (PsHost)iter.next();
           if(currentHost.getHostname().equals(hostname)){
               return currentHost;
           }
       } 
       return null; 
    }
    /**
     * get host identified by its ipv4
     * return null if not found
     * @param ipv4
     * @return 
     */
    public static PsHost getHostByIpv4(String ipv4){
        Iterator<PsHost> iter = getHostIterator(); 
        while(iter.hasNext()){
           PsHost currentHost = (PsHost)iter.next();
           if(currentHost.getIpv4().equals(ipv4)){
               return currentHost;
           }
       } 
       return null; 
    }
    /**
     * get host identified by ipv6, null if not found
     * @param ipv6
     * @return 
     */
    public static PsHost getHostByIpv6(String ipv6){
        Iterator<PsHost> iter = getHostIterator(); 
        while(iter.hasNext()){
           PsHost currentHost = (PsHost)iter.next();
           if(currentHost.getIpv4().equals(ipv6)){
               return currentHost;
           }
       } 
       return null; 
    }
}
