/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.*;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import org.json.simple.JSONObject;

/**
 * Utility class for creating new perfSonar objects
 * The created objects are stored in data store and an instance of the
 * object is returned to user. The object is empty, except of the object id
 * and it can be filled using PsObjectUpdater
 * @author tomw
 */
public class PsObjectCreator {
    /**
     * create new empty host. Store it in data store and return to user 
     * the handle. It can be later filled with parameters using PsObjectUpdater
     * @return 
     */
    public static PsHost createNewHost() {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        
        // create empty host
        PsHost newHost = new PsHost();
        // put itn into data store
        psDataStore.addHost(newHost);
        
        return newHost;
    }
    /**
     * create new host with a given hostName and ipV4
     * @param hostName
     * @param hostIpv4
     * @return 
     */
    public static PsHost createNewHost(String hostName,String hostIpv4) {
        PsHost host = createNewHost();
        host.setHostname(hostName);
        host.setIpv4(hostIpv4);
        return host;
    }
    
    /**
     * create new service object, return its handle to user.
     * @return 
     */
    public static PsService createNewService() {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsService newService = new PsService();
        psDataStore.addService(newService);
        return newService;
    }
    /**
     * create new empty site object. return its instance to user
     * @return 
     */
    public static PsSite createNewSite() {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsSite newSite = new PsSite();
        psDataStore.addSite(newSite);
        return newSite;
    }
    /**
     * create new site with a given siteName
     * @param siteName
     * @return 
     */
    public static PsSite createNewSite(String siteName){
        PsSite site = createNewSite();
        site.setName(siteName);
        return site;
    }
    /**
     * create new matrix, return its object to user
     * @return 
     */
    public static PsMatrix createNewMatrix(PsServiceType matrixType) {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsMatrix newMatrix = new PsMatrix(matrixType);
        psDataStore.addMatrix(newMatrix);
        return newMatrix;
    }
    /**
     * create new matrix with specified typeId and name
     * @param typeId
     * @param matrixName 
     */
    public static PsMatrix createNewMatrix(String typeId,String matrixName){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsServiceType type = psDataStore.getServiceType(typeId);
        PsMatrix matrix = new PsMatrix(type);
        matrix.setName(matrixName);
        psDataStore.addMatrix(matrix);
        return matrix;
    }
    /**
     * create new throughput matrix of given name
     * @param name
     * @return 
     */
    public static PsMatrix createNewThroughputMatrix(String name){
        String typeId = PsServiceTypeFactory.THROUGHPUT;
        PsMatrix matrix = createNewMatrix(typeId,name);
        return matrix;
    }
   /**
    * create new cloud, store it in data store and return the instance to user
    * @return 
    */    
    public static PsCloud createNewCloud() {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsCloud newCloud = new PsCloud();
        psDataStore.addCloud(newCloud);
        return newCloud;
    }
    /**
     * create new cloud with a given name
     * @param cloudName
     * @return 
     */
    public static PsCloud createNewCloud(String cloudName){
        PsCloud cloud = createNewCloud();
        cloud.setName(cloudName);
        return cloud;
    }
    public static PsCollector createNewCollector(){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsCollector newCollector = new PsCollector();
        psDataStore.addCollector(newCollector);
        return newCollector;
    }
    
    public static PsServiceType createNewServiceType(){
        PsDataStore psDataStore = PsDataStore.getDataStore();
        PsServiceType newType = new PsServiceType();
        psDataStore.addServiceType(newType);
        return newType;
    }
       
}
