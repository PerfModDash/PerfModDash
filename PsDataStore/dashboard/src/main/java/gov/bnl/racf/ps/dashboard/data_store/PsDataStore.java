/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;

import gov.bnl.racf.ps.dashboard.data_objects.*;
import java.util.Vector;
import java.util.Iterator;

/**
 * The object which stores perfSonar Data
 * Implemented as java singleton object
 * @author tomw
 */
public class PsDataStore {
    private static PsDataStore psDataStore=null;
    
    private Vector<PsCloud> clouds = new Vector<PsCloud>();
    private Vector<PsMatrix> matrices = new Vector<PsMatrix>();
    private Vector<PsHost> hosts= new Vector<PsHost>();
    private Vector<PsService> services = new Vector<PsService>();
    private Vector<PsSite> sites = new Vector<PsSite>();
    private Vector<PsCollector> collectors = new Vector<PsCollector>();
    private Vector<PsServiceType> serviceTypes = new Vector<PsServiceType>();
    
    /**
     * private constructor, never to be called by user explicitly
     */
    private PsDataStore(){
        
    }
    /**
     * access to data is given by getDataStore method
     * @return 
     */
    public static PsDataStore getDataStore(){
        if(psDataStore==null){
            psDataStore=new PsDataStore();
        }
        return psDataStore;
    }
    /**
     * delete all data in the data store. To be used at the debugging stage
     * Most likely this method will be deleted in the future.
     */
    public void clearAll(){
        clouds.clear();
        matrices.clear();
        hosts.clear();
        services.clear();
        sites.clear();
        collectors.clear();
        serviceTypes.clear();        
    }
    /**
     * get hosts iterator
     * @return 
     */
    public Iterator<PsHost> hostIterator(){
        return hosts.iterator();
    }
    /**
     * return service types iterator
     * @return 
     */
    public Iterator<PsServiceType> serviceTypeIterator(){
        return serviceTypes.iterator();
    }
    /**
     * get services iterator
     * @return 
     */
    public Iterator<PsService> serviceIterator(){
        return services.iterator();
    }
    /**
     * get sites iterator
     * @return 
     */
    public  Iterator<PsSite> siteIterator(){
        return sites.iterator();
    }
    /**
     * return matrices iterator
     * @return 
     */
    public  Iterator<PsMatrix> matrixIterator(){
        return matrices.iterator();
    }
    /**
     * return clouds iterator
     * @return 
     */
    public  Iterator<PsCloud> cloudIterator(){
        return clouds.iterator();
    }
    /**
     * get iterator over known collectors
     * @return 
     */
    public  Iterator<PsCollector> collectorIterator(){
        return collectors.iterator();
    }
    /**
     * get host of given id
     * @param id
     * @return 
     */
    public  PsHost getHost(String id){
        PsHost emptyHost=null;
        Iterator<PsHost> iter = hostIterator();
        while(iter.hasNext()){
            PsHost currentHost = (PsHost)iter.next();
            if(currentHost.getId().equals(id)){
                return currentHost;
            }
        }
        return emptyHost;
    }
    /**
     * drop host from data store. 
     * The host is removed from data store, however the dependencies are not,
     * To cleanly delete host //TODO finish description
     * @param id 
     */
    public  void dropHost(String hostId){
        Iterator<PsHost> iter = hostIterator();
        while(iter.hasNext()){
            PsHost currentHost = (PsHost)iter.next();
            if(currentHost.getId().equals(hostId)){
                iter.remove();
            }
        }
    }
    /**
     * drop host with hostId from data store
     * The host is removed, however the dependencies are not
     * //TODO finish description
     * @param host 
     */
    public  void dropHost(PsHost host){
        String hostId=host.getId();
        dropHost(hostId);        
    }
    /**
     * return service type with serviceTypeId
     * return null if not found
     * @param serviceTypeId
     * @return 
     */
    public  PsServiceType getServiceType(String serviceTypeId){        
        Iterator iter = serviceTypeIterator();
        while(iter.hasNext()){
            PsServiceType currentServiceType = (PsServiceType)iter.next();
            //System.out.println(
            //    "PsDataStore.getServiceType "+currentServiceType.getId()+" "+serviceTypeId);
            if(currentServiceType.getId().equals(serviceTypeId)){
                return currentServiceType;
            }
        }
        return null;
    }
    /**
     * get service type of a given name
     * @param serviceTypeName
     * @return 
     */
    public PsServiceType getServiceTypeByName(String serviceTypeName){
        Iterator iter = serviceTypeIterator();
        while(iter.hasNext()){
            PsServiceType currentServiceType = (PsServiceType)iter.next();
            
            if(currentServiceType.getName().equals(serviceTypeName)){
                return currentServiceType;
            }
        }
        return null;
    }
    /**
     * get service with given id
     * @param id
     * @return 
     */
    public  PsService getService(String serviceId){
        PsService emptyService=null;
        Iterator iter = serviceIterator();
        while(iter.hasNext()){
            PsService currentService = (PsService)iter.next();
            if(currentService.getId().equals(serviceId)){
                return currentService;
            }
        }
        return emptyService;
    }
    /**
     * drop specified service //TODO finish coding
     * @param id 
     */
    public  void dropService(String serviceId){
        Iterator<PsService> iter = serviceIterator();
        while(iter.hasNext()){
            PsService currentService = (PsService)iter.next();
            if(currentService.getId().equals(serviceId)){
                iter.remove();
            }
        }
    }
    /**
     * drop specified service
     * @param service 
     */
    public  void dropService(PsService service){
        String serviceId = service.getId();
        dropService(serviceId);
    }
    /**
     * get collector with a given id, null if not found
     * @param collectorId
     * @return 
     */
    public  PsCollector getCollector(String collectorId){
        Iterator<PsCollector> iter = collectorIterator();
        while(iter.hasNext()){
            PsCollector currentCollector = (PsCollector)iter.next();
            if(currentCollector.getId().equals(collectorId)){
                return currentCollector;
            }
        }
        return null;
    }
    /**
     * get site with given id
     * @param id
     * @return 
     */
    public  PsSite getSite(String id){
        PsSite emptySite=null;
        Iterator iter = siteIterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.getId().equals(id)){
                return currentSite;
            }
        }
        return emptySite;
    }
    /**
     * drop specified site 
     * dependencies are not updated
     * //TODO finish design
     * @param id 
     */
    public  void dropSite(String siteId){
        Iterator<PsSite> iter = siteIterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.getId().equals(siteId)){
                iter.remove();
            }
        }
    }
    /**
     * drop the site
     * dependencies are not updated
     * @param site 
     */
    public void dropSite(PsSite site){
        String siteId=site.getId();
        dropSite(siteId);
    }
    /**
     * get matrix with given id
     * @param id 
     */
    public PsMatrix getMatrix(String id){
        PsMatrix emptyMatrix=null;
        Iterator iter = matrixIterator();
        while(iter.hasNext()){
            PsMatrix currentMatrix = (PsMatrix)iter.next();
            if(currentMatrix.getId().equals(id)){
                return currentMatrix;
            }
        }
        return emptyMatrix;
    }
    /**
     * drop specified matrix 
     * dependencies are not removed
     * //TODO finish design
     * @param id 
     */
    public void dropMatrix(String matrixId){
        Iterator<PsMatrix> iter = matrixIterator();
        while(iter.hasNext()){
            PsMatrix currentMatrix = (PsMatrix)iter.next();
            if(currentMatrix.getId().equals(matrixId)){
                iter.remove();
            }
        }
    }
    public void dropMatrix(PsMatrix matrix){
        String matrixId = matrix.getId();
        dropMatrix(matrixId);
    }
    
    /**
     * drop service type of a given id. Only the type is removed, 
     * all services of this type are unaffected.
     * @param serviceTypeId 
     */
    public void dropServiceType(String serviceTypeId){
        Iterator<PsServiceType> iter = serviceTypeIterator();
        while(iter.hasNext()){
            PsServiceType currentType = (PsServiceType)iter.next();
            if(currentType.getId().equals(serviceTypeId)){
                iter.remove();
            }
        }
    }
    /**
     * drop service type of a given type. Only the type is removed, 
     * all services of this type are unaffected.
     * @param serviceTypeId 
     */
    public void dropServiceType(PsServiceType type){
        dropServiceType(type.getId());
    }
    /**
     * delete all service types from the data store. Does not delete
     * services.
     */
    public void deleteAllServiceTypes(){
        serviceTypes.clear();
    }
    
    
    /**
     * get cloud with given id
     * @param id
     * @return 
     */
    public PsCloud getCloud(String id){        
        Iterator iter = cloudIterator();
        while(iter.hasNext()){
            PsCloud currentCloud = (PsCloud)iter.next();
            if(currentCloud.getId().equals(id)){
                return currentCloud;
            }
        }
        return null;
    }
    /**
     * drop specified cloud 
     * dependencies are not removed
     * //TODO finish design
     * @param id 
     */
    public void dropCloud(String id){
        Iterator<PsCloud> iter = cloudIterator();
        while(iter.hasNext()){
            PsCloud currentCloud = (PsCloud)iter.next();
            iter.remove();
        }
    }
    /**
     * drop cloud identified by cloudId
     * dependencies are not removed
     * //TODO finish the design
     * @param cloud 
     */
    public void dropCloud(PsCloud cloud){
        String cloudId = cloud.getId();
        dropCloud(cloudId);
    }
    /**
     * add new host to data store
     * @param host 
     */
    public void addHost(PsHost host){
        hosts.add(host);
    }
    /**
     * add new service to store
     * @param service 
     */
    public void addService(PsService service){
        services.add(service);
    }
    /**
     * add new site to store
     * @param site 
     */
    public void addSite(PsSite site){
        sites.add(site);
    }
    /**
     * add new matrix to store
     * @param matrix 
     */
    public void addMatrix(PsMatrix matrix){
        matrices.add(matrix);
    }
    /**
     * add new cloud to store
     * @param cloud 
     */
    public void addCloud(PsCloud cloud){
        clouds.add(cloud);
    }
    /**
     * add new collector to data store
     * @param collector 
     */
    public void addCollector(PsCollector collector){
        collectors.add(collector);
    }
    /**
     * drop collector from data store
     * @param collector 
     */
    public void dropCollector(PsCollector collector){
        dropCollector(collector.getId());
    }
    /** 
     * drop collector collectorId from data store
     * @param collectorId 
     */
    public void dropCollector(String collectorId){
        Iterator iter = collectorIterator();
        while(iter.hasNext()){
            PsCollector currentCollector = (PsCollector)iter.next();
            if(currentCollector.getId().equals(collectorId)){
                iter.remove();
                currentCollector=null;
            }
        }
    }
    /**
     * add new service type to the data store
     * @param type 
     */
    public void addServiceType(PsServiceType type){
        serviceTypes.add(type);
    }
    
    /**
     * remove list of serives with listed id's.
     * The method does not check for service associacions, onlu removes them 
     * from list of services.
     * 
     * @param listOfServiceIds 
     */
    public void removeServices(Vector<String> listOfServiceIds){
        Iterator<String> iter = listOfServiceIds.iterator();
        while(iter.hasNext()){
            String serviceId = (String)iter.next();
            dropService(serviceId);
        }
    }
}
