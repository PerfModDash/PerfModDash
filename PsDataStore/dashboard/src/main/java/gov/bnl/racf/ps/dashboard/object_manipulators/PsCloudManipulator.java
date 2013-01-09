/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsCloud;
import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import java.util.Iterator;
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
    public static void addSite(PsCloud cloud, String siteId) {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsSite site = psDataStore.getSite(siteId);
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
    public static void addMatrix(PsCloud cloud, String matrixId) {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsMatrix matrix = psDataStore.getMatrix(matrixId);
        cloud.addMatrix(matrix);
    }

    /**
     * remove site with siteId from the cloud
     *
     * @param cloud
     * @param siteId
     */
    public static void removeSite(PsCloud cloud, String siteId) {
        Iterator<PsSite> iter = cloud.sitesIterator();
        while (iter.hasNext()) {
            PsSite currentSite = (PsSite) iter.next();
            if (currentSite.getId().equals(siteId)) {
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
    public static void removeSite(PsCloud cloud, PsSite site) {
        if (site != null) {
            String siteId = site.getId();
            removeSite(cloud, siteId);
        }
    }
    /**
     * add list of site id's to cloud
     * @param cloud
     * @param listOfSiteIds 
     */
    public static void addSites(PsCloud cloud, JSONArray listOfSiteIds){
        Iterator iter = listOfSiteIds.iterator();
        while(iter.hasNext()){
            String siteId = (String)iter.next();
            addSite(cloud,siteId);
        }
    }
     /**
     * remove list of site id's to cloud
     * @param cloud
     * @param listOfSiteIds 
     */
    public static void removeSites(PsCloud cloud, JSONArray listOfSiteIds){
        Iterator iter = listOfSiteIds.iterator();
        while(iter.hasNext()){
            String siteId = (String)iter.next();
            removeSite(cloud,siteId);
        }
    }

    /**
     * remove matrix matrixId from cloud
     *
     * @param cloud
     * @param matrixId
     */
    public static void removeMatrix(PsCloud cloud, String matrixId) {
        Iterator<PsMatrix> iter = cloud.matrixIterator();
        while (iter.hasNext()) {
            PsMatrix currentMatrix = (PsMatrix) iter.next();
            if (currentMatrix.getId().equals(matrixId)) {
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
    public static void removeMatrix(PsCloud cloud, PsMatrix matrix) {
        String matrixId = matrix.getId();
        removeMatrix(cloud, matrixId);
    }

    /**
     * delete cloud cloudId does not delete sites or clouds, they may belong to
     * different cloud
     *
     * @param cloudId
     */
    public static void deleteCloud(String cloudId) {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Iterator<PsCloud> iter = psDataStore.cloudIterator();
        while (iter.hasNext()) {
            PsCloud currentCloud = (PsCloud) iter.next();
            if (currentCloud.getId().equals(cloudId)) {
                iter.remove();
            }
        }
    }

    /**
     * delete cloud. Do not delete its component sites and matrices
     *
     * @param cloud
     */
    public static void deleteCloud(PsCloud cloud) {
        String cloudId = cloud.getId();
        deleteCloud(cloudId);
    }
}
