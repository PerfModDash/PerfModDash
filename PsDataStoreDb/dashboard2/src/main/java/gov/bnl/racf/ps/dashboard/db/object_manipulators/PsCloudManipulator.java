/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;


import gov.bnl.racf.ps.dashboard.db.data_objects.PsCloud;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import java.util.Iterator;
import org.hibernate.Session;
import org.json.simple.JSONArray;

/**
 * Class for performing operations on cloud objects
 * @author tomw
 */
public class PsCloudManipulator {

    /**
     * add site defined by siteId to the cloud
     *
     * @param cloud
     * @param siteId
     */
    public static void addSite(Session session, PsCloud cloud, int siteId) {
        PsSite site = PsDataStore.getSite(session,siteId);
        cloud.addSite(site);
    }

    /**
     * add site to cloud
     *
     * @param cloud
     * @param site
     */
    public static void addSite(PsCloud cloud, PsSite site) {
        cloud.addSite(site);
    }

    /**
     * add to the cloud matrix defined by matrixId
     *
     * @param cloud
     * @param matrixId
     */
    public static void addMatrix(Session session, PsCloud cloud, int matrixId) {
        PsMatrix matrix = PsDataStore.getMatrix(session, matrixId);
        cloud.addMatrix(matrix);
    }

    /**
     * remove site with siteId from the cloud
     *
     * @param cloud
     * @param siteId
     */
    public static void removeSite(Session session, PsCloud cloud, int siteId) {
        Iterator<PsSite> iter = cloud.sitesIterator();
        while (iter.hasNext()) {
            PsSite currentSite = (PsSite) iter.next();
            if (currentSite.getId()==siteId) {
                iter.remove();
            }
        }
    }

    /**
     * remove site from the cloud
     *
     * @param cloud
     * @param site
     */
    public static void removeSite(Session session, PsCloud cloud, PsSite site) {
        if (site != null) {
            int siteId = site.getId();
            removeSite(session, cloud, siteId);
        }
    }
    /**
     * add list of site id's to cloud
     * @param cloud
     * @param listOfSiteIds 
     */
    public static void addSites(Session session, PsCloud cloud, JSONArray listOfSiteIds){
        Iterator iter = listOfSiteIds.iterator();
        while(iter.hasNext()){
            String siteIdAsString = (String)iter.next();
            int siteId =Integer.parseInt(siteIdAsString);
            addSite(session,cloud,siteId);
        }
    }
     /**
     * remove list of site id's to cloud
     * @param cloud
     * @param listOfSiteIds 
     */
    public static void removeSites(Session session, PsCloud cloud, JSONArray listOfSiteIds){
        Iterator iter = listOfSiteIds.iterator();
        while(iter.hasNext()){
            String siteIdAsString = (String)iter.next();
            int siteId =Integer.parseInt(siteIdAsString);
            removeSite(session,cloud,siteId);
        }
    }

    public static void addMatrices(Session session, PsCloud cloud, JSONArray listOfMatrixIds){
        Iterator iter = listOfMatrixIds.iterator();
        while(iter.hasNext()){
            String matrixIdAsString = (String)iter.next();
            int matrixId =Integer.parseInt(matrixIdAsString);
            addMatrix(session,cloud,matrixId);
        }
    }
    
    public static void removeMatrices(Session session, PsCloud cloud, JSONArray listOfMatrixIds){
        Iterator iter = listOfMatrixIds.iterator();
        while(iter.hasNext()){
            String matrixIdAsString = (String)iter.next();
            int matrixId =Integer.parseInt(matrixIdAsString);
            removeMatrix(session,cloud,matrixId);
        }
    }
    
    /**
     * remove matrix matrixId from cloud
     *
     * @param cloud
     * @param matrixId
     */
    public static void removeMatrix(Session session, PsCloud cloud, int matrixId) {
        Iterator<PsMatrix> iter = cloud.matrixIterator();
        while (iter.hasNext()) {
            PsMatrix currentMatrix = (PsMatrix) iter.next();
            if (currentMatrix.getId()==matrixId) {
                iter.remove();
            }
        }
    }

    /**
     * remove matrix from cloud
     *
     * @param cloud
     * @param matrix
     */
    public static void removeMatrix(Session session,PsCloud cloud, PsMatrix matrix) {
        int matrixId = matrix.getId();
        removeMatrix(session, cloud, matrixId);
    }

    /**
     * delete cloud cloudId does not delete sites or matrices, they may belong to
     * different cloud
     *
     * @param cloudId
     */
    public static void deleteCloud(Session session, int cloudId) {
        PsCloud cloud = PsDataStore.getCloud(session, cloudId);
        deleteCloud(session, cloud);
    }

    /**
     * delete cloud. Do not delete its component sites and matrices
     *
     * @param cloud
     */
    public static void deleteCloud(Session session, PsCloud cloud) {
        PsObjectShredder.delete(session, cloud);
    }
}
