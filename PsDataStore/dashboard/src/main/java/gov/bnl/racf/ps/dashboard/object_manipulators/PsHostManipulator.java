/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import java.util.Iterator;
import java.util.Vector;
import org.json.simple.JSONArray;

/**
 * class for manipulating perfSonar hosts
 *
 * @author tomw
 */
public class PsHostManipulator {

    /**
     * add service to host
     *
     * @param host
     * @param service
     */
    public static void addService(PsHost host, PsService service) {
        host.addService(service);
    }

    /**
     * remove service from host
     *
     * @param host
     * @param service
     */
    public static void removeService(PsHost host, PsService service) {
        host.removeService(service);
        PsDataStore dataStore = PsDataStore.getDataStore();
        dataStore.dropService(service);
    }

    /**
     * remove serviceId from host
     *
     * @param host
     * @param serviceId
     */
    public static void removeService(PsHost host, String serviceId) {
        host.removeService(serviceId);
        PsDataStore dataStore = PsDataStore.getDataStore();
        dataStore.dropService(serviceId);
    }

    /**
     * remove all primitive services from the host
     * @param host 
     */
    public static void removeAllServices(PsHost host) {
        Vector<PsService> listOfServicesToBeRemoved = new Vector<PsService>();
        Iterator<PsService> iter = host.serviceIterator();
        while (iter.hasNext()) {
            listOfServicesToBeRemoved.add((PsService)iter.next());
        }
        // now we have list of services to be removed
        // loop over them and remove
        Iterator<PsService> iter2 = listOfServicesToBeRemoved.iterator();
        while (iter2.hasNext()) {
            PsService serviceToBeRemoved = (PsService)iter2.next();
            removeService(host,serviceToBeRemoved);
        }
    }

    /**
     * add service of type serviceTypeId to the host first check if the host
     * contains this service type if not create the service then add this
     * service to data store and add it to the host
     *
     * @param host
     * @param serviceTypeId
     */
    public static void addServiceType(PsHost host, String serviceTypeId) {
        PsDataStore dataStore = PsDataStore.getDataStore();
        if (PsServiceTypeFactory.isKnownType(serviceTypeId)) {
            if (PsServiceTypeFactory.isPrimitiveService(serviceTypeId)) {
                PsServiceType type = dataStore.getServiceType(serviceTypeId);
                // if service of this type is already associated to the host do nothing
                if (!host.hasServiceType(type)) {
                    PsService service = PsServiceFactory.createService(type, host);                    
                }
            }
        }
    }

    /**
     * remove service of a given type from the host also remove the service from
     * data store
     *
     * @param host
     * @param serviceTypeId
     */
    public static void removeServiceType(PsHost host, String serviceTypeId) {
        PsDataStore dataStore = PsDataStore.getDataStore();
        Iterator<PsService> iter = host.serviceIterator();
        while (iter.hasNext()) {
            PsService currentService = (PsService) iter.next();
            if (currentService.getType().equals(serviceTypeId)) {
                String serviceIdToRemove = currentService.getId();
                iter.remove();
                dataStore.dropService(serviceIdToRemove);
            }
        }
    }

    /**
     * takes host and adds latency primitive services the relevant services are
     * also added to the data store
     *
     * @param host
     */
    public static void addLatencyServices(PsHost host) {
        PsDataStore dataStore = PsDataStore.getDataStore();
        Iterator<PsServiceType> iter = dataStore.serviceTypeIterator();
        while (iter.hasNext()) {
            PsServiceType currentType = (PsServiceType) iter.next();
            if (PsServiceTypeFactory.isPrimitiveServiceLatency(currentType.getId())) {
                addServiceType(host, currentType.getId());
            }
        }
    }

    /**
     * takes host and adds throughput services to it the relevant services are
     * added to the data store
     *
     * @param host
     */
    public static void addThroughputServices(PsHost host) {
        PsDataStore dataStore = PsDataStore.getDataStore();
        Iterator<PsServiceType> iter = dataStore.serviceTypeIterator();
        while (iter.hasNext()) {
            PsServiceType currentType = (PsServiceType) iter.next();
            if (PsServiceTypeFactory.isPrimitiveServiceThroughput(currentType.getId())) {
                addServiceType(host, currentType.getId());
            }
        }
    }

    /**
     * takes host and adds all primitive services. To be used to build
     * multipurpose latency/bandwidth hosts the services are created and added
     * to the data store
     *
     * @param host
     */
    public static void addPrimitiveServices(PsHost host) {
        PsDataStore dataStore = PsDataStore.getDataStore();
        Iterator<PsServiceType> iter = dataStore.serviceTypeIterator();
        while (iter.hasNext()) {
            PsServiceType currentType = (PsServiceType) iter.next();
            addServiceType(host, currentType.getId());
        }
    }
    
    /**
     * take given host and add to it list of service types
     * stored in the input JSONArray object
     * @param host
     * @param listOfServices 
     */
    public static void addServiceTypes(PsHost host,JSONArray listOfServices){
        Iterator iter = listOfServices.iterator();
        while(iter.hasNext()){
            String serviceTypeId = (String)iter.next();
            addServiceType(host,serviceTypeId);
        }
    }
    
    
     /**
     * take given host and remove from it list of service types
     * stored in the input JSONArray object
     * @param host
     * @param listOfServices 
     */
    public static void removeServiceTypes(PsHost host,JSONArray listOfServices){
        Iterator iter = listOfServices.iterator();
        while(iter.hasNext()){
            String serviceTypeId = (String)iter.next();
            removeServiceType(host,serviceTypeId);
        }
    }
    
    /**
     * take given host and remove from it list of services
     * stored in the input JSONArray object
     * @param host
     * @param listOfServices 
     */
    public static void removeServices(PsHost host,JSONArray listOfServices){
        Iterator iter = listOfServices.iterator();
        while(iter.hasNext()){
            String serviceId = (String)iter.next();
            removeService(host,serviceId);
        }
    }
}
