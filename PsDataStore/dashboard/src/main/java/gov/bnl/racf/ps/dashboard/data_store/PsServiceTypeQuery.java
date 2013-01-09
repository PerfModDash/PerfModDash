/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;

import gov.bnl.racf.ps.dashboard.data_objects.PsServiceType;
import java.util.Iterator;
import java.util.Vector;


/**
 * queries data store for service types
 * @author tomw
 */
public class PsServiceTypeQuery {
    /** get iterator over service types
     * 
     * @return 
     */
     public static Iterator<PsServiceType> getServiceTypeIterator(){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Iterator<PsServiceType> iter = psDataStore.serviceTypeIterator();
        return iter;
    }
     /**
      * get service type with service type id
      * @param id
      * @return 
      */
    public static PsServiceType getServiceTypeById(String id){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        return psDataStore.getServiceType(id);
    }
    /**
     * return list of service types with given Id's
     * @param listOfIds
     * @return 
     */
    public static Vector<PsServiceType> getListOfServiceTypes(Vector<String> listOfIds){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Vector<PsServiceType> serviceTypes = new Vector<PsServiceType>();
        Iterator<String> iter = listOfIds.iterator();
        while(iter.hasNext()){
            String currentId = (String)iter.next();
            PsServiceType currentServiceType = psDataStore.getServiceType(currentId);
            if(currentServiceType!=null){
                serviceTypes.add(currentServiceType);
            }
        }
        return serviceTypes;
    }
    /**
     * get service type identified by job type, return null if not found
     * @param jobType
     * @return 
     */
    public static PsServiceType getServiceTypeByJobType(String jobType){
        Iterator<PsServiceType> iter = getServiceTypeIterator();
        while(iter.hasNext()){
            PsServiceType currentServiceType = (PsServiceType)iter.next();
            if(currentServiceType.getJobType().equals(jobType)){
                return currentServiceType;
            }
        }
        return null;                
    }
    /**
     * return service type identified by name
     * return null if not found
     * @param name
     * @return 
     */
    public static PsServiceType getServiceTypeByName(String name){
        Iterator<PsServiceType> iter = getServiceTypeIterator();
        while(iter.hasNext()){
            PsServiceType currentServiceType = (PsServiceType)iter.next();
            if(currentServiceType.getName().equals(name)){
                return currentServiceType;
            }
        }
        return null;
    }
}
