/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.hibernate.Session;
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
    public static void addService(Session session, PsHost host, PsService service) {
        host.addService(service);
    }

    /**
     * remove service from host
     *
     * @param host
     * @param service
     */
    public static void removeService(Session session, PsHost host, PsService service) {
        host.removeService(service);
        session.delete(service);
    }

    /**
     * remove serviceId from host
     *
     * @param host
     * @param serviceId
     */
    public static void removeService(Session session, PsHost host, int serviceId) {
        host.removeService(serviceId);
        PsService service = (PsService) session.get(PsService.class, serviceId);
        session.delete(service);
    }

    /**
     * remove all primitive services from the host
     *
     * @param host
     */
    public static void removeAllServices(Session session, PsHost host) {
        Vector<PsService> listOfServicesToBeRemoved = new Vector<PsService>();
        Iterator<PsService> iter = host.serviceIterator();
        while (iter.hasNext()) {
            listOfServicesToBeRemoved.add((PsService) iter.next());
        }
        // now we have list of services to be removed
        // loop over them and remove
        Iterator<PsService> iter2 = listOfServicesToBeRemoved.iterator();
        while (iter2.hasNext()) {
            PsService serviceToBeRemoved = (PsService) iter2.next();
            removeService(session, host, serviceToBeRemoved);
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
    public static void addServiceType(Session session, PsHost host, String serviceTypeId) {

        if (PsServiceTypeFactory.isKnownType(serviceTypeId)) {
            if (PsServiceTypeFactory.isPrimitiveService(serviceTypeId)) {


                PsServiceType type = PsDataStore.getServiceType(session, serviceTypeId);
                if (type != null) {
                    // if service of this type is already associated to the host do nothing
                    if (!host.hasServiceType(type)) {
                        PsService service = 
                                PsServiceFactory.createService(session,type, host);
                        // service factory already saved the service to session 
                        // and added it to host, so no need to do it now.
                        
                    }
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
    public static void removeServiceType(Session session,
            PsHost host, String serviceTypeId) {
       
        Iterator<PsService> iter = host.serviceIterator();
        while (iter.hasNext()) {
            PsService currentService = (PsService) iter.next();
            if (currentService.getType().equals(serviceTypeId)) {
                session.delete(currentService);
                iter.remove();
            }
        }
    }

    /**
     * takes host and adds latency primitive services the relevant services are
     * also added to the data store
     *
     * @param host
     */
    public static void addLatencyServices(Session session,PsHost host) {
       
        Iterator<String> iter = 
                PsServiceTypeFactory.listOfPrimitiveLatencyServiceTypes().iterator();
        while (iter.hasNext()) {
            String currentTypeId = (String) iter.next();
                addServiceType(session,host, currentTypeId);
        }
    }

    /**
     * takes host and adds throughput services to it the relevant services are
     * added to the data store
     *
     * @param host
     */
    public static void addThroughputServices(Session session,PsHost host) {
        Iterator<String> iter = 
                PsServiceTypeFactory.listOfPrimitiveThroughputServiceTypes().iterator();
        while (iter.hasNext()) {
            String currentTypeId = (String) iter.next();
                addServiceType(session,host, currentTypeId);
        }
    }

    /**
     * takes host and adds all primitive services. To be used to build
     * multipurpose latency/bandwidth hosts the services are created and added
     * to the data store
     *
     * @param host
     */
    public static void addPrimitiveServices(Session session,PsHost host) {
        Iterator<String> iter = PsServiceTypeFactory.listOfPrimitiveServiceTypes().iterator();
        while (iter.hasNext()) {
            String currentTypeId = (String) iter.next();
            addServiceType(session,host, currentTypeId);
        }
    }

    /**
     * take given host and add to it list of service types stored in the input
     * JSONArray object
     *
     * @param host
     * @param listOfServices
     */
    public static void addServiceTypes(Session session, 
            PsHost host, JSONArray listOfServiceTypes) {
        Iterator iter = listOfServiceTypes.iterator();
        while (iter.hasNext()) {
            String serviceTypeId = (String) iter.next();
            addServiceType(session,host, serviceTypeId);
        }
    }

    /**
     * take given host and remove from it list of service types stored in the
     * input JSONArray object
     *
     * @param host
     * @param listOfServices
     */
    public static void removeServiceTypes(Session session, 
            PsHost host, JSONArray listOfServiceTypes) {
        Iterator iter = listOfServiceTypes.iterator();
        while (iter.hasNext()) {
            String serviceTypeId = (String) iter.next();
            removeServiceType(session,host, serviceTypeId);
        }
    }

    /**
     * take given host and remove from it list of services stored in the input
     * JSONArray object
     *
     * @param host
     * @param listOfServices
     */
    public static void removeServices(Session session, 
            PsHost host, JSONArray listOfServices) {
        Iterator iter = listOfServices.iterator();
        while (iter.hasNext()) {
            int serviceId = ((Integer) iter.next()).intValue();
            removeService(session, host, serviceId);
        }
    }
}
