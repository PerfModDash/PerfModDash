/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.*;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import java.util.Vector;
import java.util.Iterator;

/**
 * utility class used to delete PS objects
 *
 * @author tomw
 */
public class PsObjectShredder {

    /**
     * delete collector
     *
     * @param collector
     */
    public static void deleteCollector(PsCollector collector) {
        deleteCollector(collector.getId());
    }

    /**
     * delete collector collectorId
     *
     * @param collectorId
     */
    public static void deleteCollector(String collectorId) {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        psDataStore.dropCollector(collectorId);
    }

    /**
     * delete a cloud from the data store cloud elements: sites and matrices are
     * not deleted
     *
     * @param cloud
     */
    public static void deleteCloud(PsCloud cloud) {
        deleteCloud(cloud.getId());
    }

    /**
     * delete a cloud cloudId from the data store cloud elements: sites and
     * matrices are not deleted
     *
     * @param cloudId
     */
    public static void deleteCloud(String cloudId) {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Iterator iter = psDataStore.cloudIterator();
        while (iter.hasNext()) {
            PsCloud currentCloud = (PsCloud) iter.next();
            if (currentCloud.getId().equals(cloudId)) {
                iter.remove();
            }
        }
    }

    /**
     * delete site from data store the site is also deleted from all clouds it
     * belongs to
     *
     * @param site
     */
    public static void deleteSite(PsSite site) {
        deleteSite(site.getId());
    }

    /**
     * delete site siteId from data store the site is also deleted from all
     * clouds it belongs to
     *
     * @param site
     */
    public static void deleteSite(String siteId) {
        //get data store
        PsDataStore psDataStore = PsDataStore.getDataStore();

        // first remove site from all clouds
        Iterator siteIter = psDataStore.cloudIterator();
        while (siteIter.hasNext()) {
            PsCloud currentCloud = (PsCloud) siteIter.next();
            currentCloud.removeSite(siteId);
        }

        // second: remove site from data store 
        psDataStore.dropSite(siteId);
    }

    /**
     * delete matrix. Also remove references to this matrix in sites. Also
     * delete all services which belong to this matrix
     *
     * @param matrix
     */
    public static void deleteMatrix(PsMatrix matrix) {
        deleteMatrix(matrix.getId());
    }

    /**
     * delete matrix. Also remove references to this matrix in sites. Also
     * delete all services which belong to this matrix
     *
     * @param matrixId
     */
    public static void deleteMatrix(String matrixId) {
        //get data store
        PsDataStore psDataStore = PsDataStore.getDataStore();

        PsMatrix matrix = psDataStore.getMatrix(matrixId);

        if (matrix != null) {

            // delete matrix from clouds
            Iterator cloudIterator = psDataStore.cloudIterator();
            while (cloudIterator.hasNext()) {
                PsCloud currentCloud = (PsCloud) cloudIterator.next();
                currentCloud.removeMatrix(matrixId);
            }

            //remove services from this matrix

            Vector<String> servicesToBeDeleted = matrix.getComponentServiceIds();
            //drop matrix from data store
            psDataStore.dropMatrix(matrixId);
            // drop current instance, to be sure that it is destroyed too
            matrix = null;

            // delete its component services
            psDataStore.removeServices(servicesToBeDeleted);

        }

    }

    /**
     * Delete service. This involves: delete service from any hosts which may
     * contain it. Also delete it from any matrix which may contain it. Then
     * delete the service itself from the list of services.
     *
     * @param service
     */
    public static void deleteService(PsService service) {
        deleteService(service.getId());
    }

    /**
     * Delete service. This involves: delete service from any hosts which may
     * contain it. Also delete it from any matrix which may contain it. Then
     * delete the service itself from the list of services.
     *
     * @param serviceId
     */
    public static void deleteService(String serviceId) {
        PsDataStore psDataStore = PsDataStore.getDataStore();

        //first delete service from all hosts
        Iterator hostIterator = psDataStore.hostIterator();
        while (hostIterator.hasNext()) {
            PsHost currentHost = (PsHost) hostIterator.next();
            currentHost.removeService(serviceId);
        }

        //second delete service from all matrices
        Iterator matrixIterator = psDataStore.matrixIterator();
        while (matrixIterator.hasNext()) {
            PsMatrix currentMatrix =
                    (PsMatrix) matrixIterator.next();
            currentMatrix.removeService(serviceId);
        }

        //third remove service from list of services
        psDataStore.dropService(serviceId);
    }

    /**
     * delete host. Also delete dependant services and matrix elements
     *
     * @param host
     */
    public static void deleteHost(PsHost host) {
        deleteHost(host.getId());
    }

    /**
     * delete host. Also remove all its dependent services and relevant matrix
     * elements
     *
     * @param hostId
     */
    public static void deleteHost(String hostId) {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsHost host = psDataStore.getHost(hostId);

        //first delete services involving this host
        Iterator serviceIterator = host.serviceIterator();
        while (serviceIterator.hasNext()) {
            PsService currentService = (PsService) serviceIterator.next();
            psDataStore.dropService(currentService);
        }
        host.removeAllServices();

        //second delete corresponding elements from matrices      
        Iterator matrixIterator = psDataStore.matrixIterator();
        while (matrixIterator.hasNext()) {
            PsMatrix currentMatrix =
                    (PsMatrix) matrixIterator.next();
            currentMatrix.removeHost(hostId);
        }

        //third delete the host from sites
        Iterator sitesIterator = psDataStore.siteIterator();
        while (sitesIterator.hasNext()) {
            PsSite currentSite = (PsSite) sitesIterator.next();
            currentSite.removeHost(host);
        }

        //delete host from list of hosts
        psDataStore.dropHost(hostId);

        // and finally delete current instance of the host, 
        //to be sure that it goes away
        host = null;

    }

    /**
     * remove service type with given id individual services with this service
     * type are not deleted //TODO think if this is good policy, maybe they
     * should be deleted too
     *
     * @param typeId
     */
    public static void deleteServiceType(String typeId) {
        PsDataStore dataStore = PsDataStore.getDataStore();
        Iterator<PsServiceType> iter = dataStore.serviceTypeIterator();
        while (iter.hasNext()) {
            PsServiceType currentType = (PsServiceType) iter.next();
            if (currentType.getId().equals(typeId)) {
                iter.remove();
            }
        }
    }

    /**
     * remove service type with given id individual services with this service
     * type are not deleted //TODO think if this is good policy, maybe they
     * should be deleted too
     *
     * @param type
     */
    public static void deleteServiceType(PsServiceType type) {
        deleteServiceType(type.getId());
    }
}
