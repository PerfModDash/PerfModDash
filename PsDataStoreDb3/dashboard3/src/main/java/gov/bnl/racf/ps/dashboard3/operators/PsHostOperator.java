/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsHostDao;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsHostJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * class for manipulating perfSonar hosts
 *
 * @author tomw
 */
public class PsHostOperator {

    private PsHostDao psHostDao;

    public void setPsHostDao(PsHostDao psHostDao) {this.psHostDao = psHostDao;}
    
    private PsHostJson psHostJson;

    public void setPsHostJson(PsHostJson psHostJson) {
        this.psHostJson = psHostJson;
    }
    

    public String test() {
        return "Test of PsHostOperator";
    }

    /**
     * create new (empty) host
     *
     * @return
     */
    public PsHost create() {
        return this.psHostDao.create();
    }

    /**
     * create new host
     *
     * @param host
     */
    public void create(PsHost host) {
        this.psHostDao.insert(host);
    }

    /**
     * get host by id
     *
     * @param id
     * @return PsHost
     * @throws PsObjectNotFoundException
     */
    public PsHost getById(int id) throws PsObjectNotFoundException {
        return this.psHostDao.getById(id);
    }

    /**
     * get list of all hosts
     *
     * @return
     */
    public List<PsHost> getAll() {
        return this.psHostDao.getAll();
    }
    
    
    public List<PsHost> getAll(String sortingVariable, String sortingOrder) {
        List<PsHost> listOfHosts = getAll();
        if(sortingVariable!=null){
            Collections.sort(listOfHosts, 
                    PsHost.selectPropertyComparator(sortingVariable));
            if (PsParameters.SORTING_ORDER_UP.equals(sortingOrder)){
                Collections.reverse(listOfHosts);
            }else{
                if(PsParameters.SORTING_ORDER_DOWN.equals(sortingOrder)){
                
            }else{
                    throw new RuntimeException("unknown host sorting order: " + sortingOrder);
                }
            }
        }
        
        return listOfHosts;
    }
    
    /**
     * update a host
     * @param host 
     */
    public void update(PsHost host) {
        this.psHostDao.update(host);
    }

    /**
     * delete a host
     *
     * @param host
     */
    public void delete(PsHost host) {

        //TODO expand this, to delete services on this host,
        //TODO remove it from matrices,sites
        this.psHostDao.delete(host);
    }

    /**
     * delete host based on id
     */
    public void delete(int id) {
        PsHost host;
        try {
            host = this.psHostDao.getById(id);
            this.psHostDao.delete(host);
        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(PsHostOperator.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(PsHostOperator.class.getName()).log(Level.SEVERE, " Failed to delete host id=" + id);
        }
    }

    /**
     * convert PsHost into JsonArray
     * @param host
     * @return 
     */
    public JSONObject toJson(PsHost host){
        return this.psHostJson.toJson(host);
    }
    /**
     * convert PsHost into JsonArray
     * @param host
     * @param detailLevel
     * @return 
     */
    public JSONObject toJson(PsHost host,String detailLevel){
        return this.psHostJson.toJson(host, detailLevel);
    }
    /**
     * convert list of hosts to JSONArray
     * @param listOfHosts
     * @return 
     */
    public JSONArray toJson(List<PsHost> listOfHosts) {
        return this.psHostJson.toJson(listOfHosts);
    }
   /**
    * convert list of hosts to JSONArray
    * @param listOfHosts
    * @param detailLevel
    * @return 
    */
    public JSONArray toJson(List<PsHost> listOfHosts,String detailLevel) {
        return this.psHostJson.toJson(listOfHosts,detailLevel);
    }
    /**
     * add service to host
     *
     * @param host
     * @param service
     */
//    public static void addService(Session session, PsHost host, PsService service) {
//        host.addService(service);
//    }
    /**
     * remove service from host
     *
     * @param host
     * @param service
     */
//    public static void removeService(Session session, PsHost host, PsService service) {
//        host.removeService(service);
//        session.delete(service);
//    }
    /**
     * remove serviceId from host
     *
     * @param host
     * @param serviceId
     */
//    public static void removeService(Session session, PsHost host, int serviceId) {
//        host.removeService(serviceId);
//        PsService service = (PsService) session.get(PsService.class, serviceId);
//        session.delete(service);
//    }
    /**
     * remove all primitive services from the host
     *
     * @param host
     */
//    public static void removeAllServices(Session session, PsHost host) {
//        Vector<PsService> listOfServicesToBeRemoved = new Vector<PsService>();
//        Iterator<PsService> iter = host.serviceIterator();
//        while (iter.hasNext()) {
//            listOfServicesToBeRemoved.add((PsService) iter.next());
//        }
//        // now we have list of services to be removed
//        // loop over them and remove
//        Iterator<PsService> iter2 = listOfServicesToBeRemoved.iterator();
//        while (iter2.hasNext()) {
//            PsService serviceToBeRemoved = (PsService) iter2.next();
//            removeService(session, host, serviceToBeRemoved);
//        }
//    }
    /**
     * add service of type serviceTypeId to the host first check if the host
     * contains this service type if not create the service then add this
     * service to data store and add it to the host
     *
     * @param host
     * @param serviceTypeId
     */
//    public static void addServiceType(Session session, PsHost host, String serviceTypeId) {
//
//        if (PsServiceTypeFactory.isKnownType(serviceTypeId)) {
//            if (PsServiceTypeFactory.isPrimitiveService(serviceTypeId)) {
//
//
//                PsServiceType type = PsDataStore.getServiceType(session, serviceTypeId);
//                if (type != null) {
//                    // if service of this type is already associated to the host do nothing
//                    if (!host.hasServiceType(type)) {
//                        PsService service = 
//                                PsServiceFactory.createService(session,type, host);
//                        // service factory already saved the service to session 
//                        // and added it to host, so no need to do it now.
//                        
//                    }
//                }
//            }
//        }
//    }
    /**
     * remove service of a given type from the host also remove the service from
     * data store
     *
     * @param host
     * @param serviceTypeId
     */
//    public static void removeServiceType(Session session,
//            PsHost host, String serviceTypeId) {
//       
//        Iterator<PsService> iter = host.serviceIterator();
//        while (iter.hasNext()) {
//            PsService currentService = (PsService) iter.next();
//            if (currentService.getType().equals(serviceTypeId)) {
//                session.delete(currentService);
//                iter.remove();
//            }
//        }
//    }
    /**
     * takes host and adds latency primitive services the relevant services are
     * also added to the data store
     *
     * @param host
     */
//    public static void addLatencyServices(Session session,PsHost host) {
//       
//        Iterator<String> iter = 
//                PsServiceTypeFactory.listOfPrimitiveLatencyServiceTypes().iterator();
//        while (iter.hasNext()) {
//            String currentTypeId = (String) iter.next();
//                addServiceType(session,host, currentTypeId);
//        }
//    }
    /**
     * takes host and adds throughput services to it the relevant services are
     * added to the data store
     *
     * @param host
     */
//    public static void addThroughputServices(Session session,PsHost host) {
//        Iterator<String> iter = 
//                PsServiceTypeFactory.listOfPrimitiveThroughputServiceTypes().iterator();
//        while (iter.hasNext()) {
//            String currentTypeId = (String) iter.next();
//                addServiceType(session,host, currentTypeId);
//        }
//    }
    /**
     * takes host and adds all primitive services. To be used to build
     * multipurpose latency/bandwidth hosts the services are created and added
     * to the data store
     *
     * @param host
     */
//    public static void addPrimitiveServices(Session session,PsHost host) {
//        Iterator<String> iter = PsServiceTypeFactory.listOfPrimitiveServiceTypes().iterator();
//        while (iter.hasNext()) {
//            String currentTypeId = (String) iter.next();
//            addServiceType(session,host, currentTypeId);
//        }
//    }
    /**
     * take given host and add to it list of service types stored in the input
     * JSONArray object
     *
     * @param host
     * @param listOfServices
     */
//    public static void addServiceTypes(Session session, 
//            PsHost host, JSONArray listOfServiceTypes) {
//        Iterator iter = listOfServiceTypes.iterator();
//        while (iter.hasNext()) {
//            String serviceTypeId = (String) iter.next();
//            addServiceType(session,host, serviceTypeId);
//        }
//    }
    /**
     * take given host and remove from it list of service types stored in the
     * input JSONArray object
     *
     * @param host
     * @param listOfServices
     */
//    public static void removeServiceTypes(Session session, 
//            PsHost host, JSONArray listOfServiceTypes) {
//        Iterator iter = listOfServiceTypes.iterator();
//        while (iter.hasNext()) {
//            String serviceTypeId = (String) iter.next();
//            removeServiceType(session,host, serviceTypeId);
//        }
//    }
    /**
     * take given host and remove from it list of services identified by their
     * ID's stored in the input JSONArray object
     *
     * service ids are in JSONArray represented by strings.
     *
     * @param session
     * @param host
     * @param listOfServiceIds
     */
//    public static void removeServices(Session session, 
//            PsHost host, JSONArray listOfServiceIds) {
//        Iterator iter = listOfServiceIds.iterator();
//        while (iter.hasNext()) {
//            String serviceIdString = (String)iter.next();
//            int serviceId = Integer.parseInt(serviceIdString);
//            removeService(session, host, serviceId);
//        }
//    }
    
    /**
     * Get json object describing a host. Based on this create and persost a PsHost object. 
     * If succesful, return JSONObject of the new host (it will include id).
     * //TODO make a custom exception
     * //TODO make this operation transactional
     * @param jsonInput
     * @return 
     */
    public JSONObject insertNewHostFromJson(JSONObject jsonInput){
        // first order of business is to create new host
        PsHost newHost = this.psHostDao.create();
        //second order of business is to update the host object
        this.update(newHost, jsonInput);
        //third order of business is to save the updated object
        this.psHostDao.update(newHost);
        //last order of business is to convert the result to JSON and return JSON object
        JSONObject jsonOutput = this.psHostJson.toJson(newHost);
        return jsonOutput;
    }
    
    
     /**
     * update host object with values from JSON
     * If JSON has valid id and it does not match the host id throw an exception
     *
     * @param host
     * @param json
     */
    public  void update(PsHost host, JSONObject json) {
        // first order of business is to compare id

        boolean jsonHasValidId = false;
        if (json.keySet().contains(PsHost.ID)) {
            if (json.get(PsHost.ID) != null && !"".equals(json.get(PsHost.ID))) {
                jsonHasValidId = true;
            }
        }
        
        boolean idTestPassed=false;
        
        if (jsonHasValidId) {
            int hostId = host.getId();
            String hostIdAsString = (String) json.get(PsHost.ID);
           
            int hostIdInJson  = Integer.parseInt(hostIdAsString);
            if (hostId != hostIdInJson) {
                System.out.println("ERROR: host id and json id do not match");
                idTestPassed=false;
            }else{
                idTestPassed=true;
            }
        }else{
            idTestPassed=true;
        }

        if (!idTestPassed) {
            System.out.println("ERROR: failed the id test.");
            //TODO create a custom exception to handle this type of problem
            throw new RuntimeException("the json object and host object do not agree on id "+json.toString());
        } else {
            if (json.keySet().contains(PsHost.HOSTNAME)) {
                host.setHostname((String) json.get(PsHost.HOSTNAME));
            }
            if (json.keySet().contains(PsHost.IPV4)) {
                host.setIpv4((String) json.get(PsHost.IPV4));
            }
            if (json.keySet().contains(PsHost.IPV6)) {
                host.setIpv6((String) json.get(PsHost.IPV6));
            }
            // We do not handle services here. 
            // Adding and removeing services is done by dedicated command from 
            // PsApi
        }

    }
    
}
