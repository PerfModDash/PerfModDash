/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.*;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * utility class for deleting objects
 *
 * @author tomw
 */
public class PsObjectShredder {

    /**
     * delete host object
     *
     * @param session
     * @param host
     */
    public static void delete(Session session, PsHost host) {
        //first order of business is to remove this host from any
        // sites it might belong to
        List listOfAllSites = PsDataStore.getAllSites(session);
        Iterator iter = listOfAllSites.iterator();
        while (iter.hasNext()) {
            PsSite currentSite = (PsSite) iter.next();
            currentSite.removeHost(host);
        }

        // second order of business is to emove this host from any matrix it might
        // belong to
        List listOfAllMatrices = PsDataStore.getAllMatrices(session);
        Iterator matrixIterator = listOfAllMatrices.iterator();
        while (matrixIterator.hasNext()) {
            PsMatrix currentMatrix = (PsMatrix) matrixIterator.next();
            if (currentMatrix.containsHost(host)) {
                currentMatrix.removeHost(host);
            }
        }

        // third order of business is to remove all primitive services
        // related to this host
        PsHostManipulator.removeAllServices(session, host);

        // finally delete the host itself
        session.delete(host);

    }

    public static void deleteJobsAssociatedWithService(Session session, PsService service) {
        Query query = session.createQuery("delete PsJob where service_id = :service_id");
        int service_id = service.getId();
        query.setParameter("service_id", service_id);
        int result = query.executeUpdate();
    }
    
    public static void deleteServiceResultsAssociatedWithService(Session session, PsService service) {
        Query query = session.createQuery("delete PsServiceResult where service_id = :service_id");
        int service_id = service.getId();
        query.setParameter("service_id", service_id);
        int result = query.executeUpdate();
    }

    public static void delete(Session session, PsService service) {
        //throw new UnsupportedOperationException("Not yet implemented");
        
        //first order of business is to remove jobs associated with this service
        deleteJobsAssociatedWithService(session, service);

        //second order of business is to delete service results asociated with this service
        deleteServiceResultsAssociatedWithService(session, service);
        
        // last order of business is to delete the service itself
        session.delete(service);
    }

    
    
    public static void delete(Session session, PsSite site) {
        //throw new UnsupportedOperationException("Not yet implemented");
        //first order of business is to remove all hosts from the site
        site.removeAllHosts();

        // second remove this site from all clouds
        List listOfAllClouds = PsDataStore.getAllClouds(session);
        Iterator cloudIterator = listOfAllClouds.iterator();
        while(cloudIterator.hasNext()){
            PsCloud currentCloud = (PsCloud)cloudIterator.next();
            PsCloudManipulator.removeSite(session, currentCloud, site);
        }

        // ok, now we can remove the site
        session.delete(site);
    }

    public static void delete(Session session, PsMatrix matrix) {
        //throw new UnsupportedOperationException("Not yet implemented");

        //first  zero order of business is to remove this matrix from all clouds
        List listOfAllClouds = PsDataStore.getAllClouds(session);
        Iterator cloudIterator = listOfAllClouds.iterator();
        while(cloudIterator.hasNext()){
            PsCloud currentCloud = (PsCloud)cloudIterator.next();
            PsCloudManipulator.removeMatrix(session, currentCloud, matrix);
        }

        // second order of business is to remove all hosts from this matrix
        List<PsHost> allHostsInThisMatrix = matrix.getAllHosts();
        Iterator iter = allHostsInThisMatrix.iterator();
        while (iter.hasNext()) {
            PsHost hostToBeRemoved = (PsHost) iter.next();
            PsMatrixManipulator.removeHostFromMatrix(session, matrix, hostToBeRemoved);
        }
        
        // last order of business is to remove the matrix itself
        session.delete(matrix);
    }

    public static void delete(Session session, PsCloud cloud) {
        cloud.removeAllSites();
        cloud.removeAllMatrices();
        session.delete(cloud);
    }

    public static void delete(Session session, PsJob job) {
        if (job != null) {
            session.delete(job);
        }
    }

    public static void deletePsJob(Session session, int jobId) {
        Query query = session.createQuery("delete PsJob where id = :job_id");
        query.setParameter("job_id", jobId);
        int result = query.executeUpdate();
    }
    
    public static void deletePsJob(Session session, PsService service) {
        Query query = session.createQuery("delete PsJob where service_id = :service_id");
        query.setParameter("service_id", service.getId());
        int result = query.executeUpdate();
    }
    

    public static void delete(Session session, PsServiceResult result) {
        if (result != null) {
            delete(session, result);
        }
    }
    
    /**
     * delete all clouds
     * @param session 
     */
    public static  void deleteAllClouds(Session session){
        List<PsCloud> listOfAllClouds = PsDataStore.getAllClouds(session);
        deleteListOfClouds(session, listOfAllClouds);
    }
    /**
     * delete list of clouds
     * @param session
     * @param listOfClouds 
     */
    public static  void deleteListOfClouds(Session session,List<PsCloud> listOfClouds){
        Iterator iter = listOfClouds.iterator();
        while(iter.hasNext()){
            PsCloud currentCloud = (PsCloud)iter.next();
            delete(session,currentCloud);
        }
    }
    /**
     * delete all matrices
     * @param session 
     */
    public static  void deleteAllMatrices(Session session){
        List<PsMatrix> listOfAllMatrices = PsDataStore.getAllMatrices(session);
        deleteListOfMatrices(session, listOfAllMatrices);
    }
    
    /**
     * delete matrices from a list
     * @param session
     * @param listOfMatrices 
     */
    public static  void deleteListOfMatrices(Session session,List<PsMatrix> listOfMatrices){
        Iterator iter = listOfMatrices.iterator();
        while(iter.hasNext()){
            PsMatrix currentMatrix = (PsMatrix)iter.next();
            delete(session,currentMatrix);
        }
    }
    public static  void deleteAllSites(Session session){
        List<PsSite> listOfAllSites = PsDataStore.getAllSites(session);
        deleteListOfSites(session, listOfAllSites);
    }
    /**
     * delete sites from specified list
     * @param session
     * @param listOfSites 
     */
    public static  void deleteListOfSites(Session session,List<PsSite> listOfSites){
        Iterator iter = listOfSites.iterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            delete(session,currentSite);
        }
    }
    /**
     * delete all hosts
     * @param session 
     */
    public static  void deleteAllHosts(Session session){
        List<PsHost> listOfAllHosts = PsDataStore.getAllHosts(session);
        deleteListOfHosts(session, listOfAllHosts);
    }
    /**
     * delete hosts form the specified list
     * @param session
     * @param listOfHosts 
     */
    public static  void deleteListOfHosts(Session session,List<PsHost> listOfHosts){
        Iterator iter = listOfHosts.iterator();
        while(iter.hasNext()){
            PsHost currentHost = (PsHost)iter.next();
            delete(session,currentHost);
        }
    }
    /**
     * delete all objects except service types
     * @param session 
     */
    public static  void deleteAllObjects(Session session){
        deleteAllClouds(session);
        deleteAllMatrices(session);
        deleteAllSites(session);
        deleteAllHosts(session);
    }
}
