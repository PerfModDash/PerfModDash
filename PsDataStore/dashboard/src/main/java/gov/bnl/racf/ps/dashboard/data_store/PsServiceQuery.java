/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;

import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import java.util.Iterator;
import java.util.Vector;


/**
 * Query the data store for services
 * @author tomw
 */
public class PsServiceQuery {
    /**
     * get iterator which runs on services in data store
     * @return 
     */
    public static Iterator<PsService> getServiceIterator(){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        return psDataStore.serviceIterator();
    }
    /**
     * get service with given id
     * @param id
     * @return 
     */
    public static PsService getServiceById(String id){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        return psDataStore.getService(id);
    }
    /** 
     * take a list of Ids and return list of services with those ids
     * @param listOfIds
     * @return 
     */
    public static Vector<PsService> getServicesByIds(Vector<String> listOfIds){
        Vector<PsService> services = new Vector<PsService>();
        Iterator<String> iter = listOfIds.iterator();
        while(iter.hasNext()){
            String currentId = (String)iter.next();
            PsService currentService = getServiceById(currentId);
            services.add(currentService);
        }
        return services;
    }
    /**
     * return service with a given name. Return null if not found
     * @param name
     * @return 
     */
    public static PsService getserviceByName(String name){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Iterator<PsService> iter = psDataStore.serviceIterator();
        while(iter.hasNext()){
            PsService currentService = (PsService)iter.next();
            if(currentService!=null){
                if(currentService.getName().equals(name)){
                    return currentService;
                }
            }
        }
        return null;                
    }
}
