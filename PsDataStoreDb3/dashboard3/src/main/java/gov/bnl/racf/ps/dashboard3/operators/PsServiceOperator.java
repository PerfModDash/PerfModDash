/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.domainobjects.factories.PsServiceFactory;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceJson;
import java.util.Collection;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsServiceOperator {

    // --- dependency injection part --- //
    
    private PsServiceFactory psServiceFactory;

    public void setPsServiceFactory(PsServiceFactory psServiceFactory) {
        this.psServiceFactory = psServiceFactory;
    }
    
    private PsServiceDao psServiceDao;

    public void setPsServiceDao(PsServiceDao psServiceDao) {
        this.psServiceDao = psServiceDao;
    }

    private PsServiceJson psServiceJson;

    public void setPsServiceJson(PsServiceJson psServiceJson) {
        this.psServiceJson = psServiceJson;
    }

   

    // --- class code starts here --- //
    // --- simple CRUD methods go first --- //
    /**
     * insert a new service
     *
     * @param service
     */
    @Transactional
    public void insert(PsService service) {
        this.psServiceDao.insert(service);
    }

    /**
     * get PsService object by its id
     *
     * @param id
     * @return
     * @throws PsObjectNotFoundException
     */
    @Transactional
    public PsService getById(int id) throws PsObjectNotFoundException {
        return this.psServiceDao.getById(id);
    }

    /**
     * get all services
     *
     * @return
     */
    @Transactional
    public List<PsService> getAll() {
        return this.psServiceDao.getAll();
    }

    /**
     * update service
     *
     * @param service
     */
    @Transactional
    public void update(PsService service) {
        this.psServiceDao.update(service);
    }

    /**
     * delete service by Id //TODO delete service history too
     *
     * @param id
     */
    @Transactional
    public void delete(int id) {
        this.psServiceDao.delete(id);
    }

    /**
     * delete service //TODO delete service history too
     *
     * @param serviceToBeDeleted
     */
    @Transactional
    public void delete(PsService serviceToBeDeleted) {
        this.psServiceDao.delete(serviceToBeDeleted);
    }

    /**
     * delete collection of services //TODO delete service history too
     *
     * @param servicesToBeDeleted
     */
    @Transactional
    public void delete(Collection<PsService> servicesToBeDeleted) {
        this.psServiceDao.delete(servicesToBeDeleted);
    }

   
    
    
    //--- methods for creation of services --//
    /**
     * create primitive service of given type running on a given host
     *
     * @param type
     * @param host
     * @return
     */
    @Transactional
    public PsService createService(PsServiceType type, PsHost host) {
        // first order of business is to call service factor to create the service
        PsService service = this.psServiceFactory.createService(type, host);
        // second order of business is to insert this service to database
        this.psServiceDao.insert(service);
        //third order of business its to return the service to the caller
        return service;
    }

    /**
     * //TODO implement this method
     * @param type
     * @param source
     * @param destination
     * @return 
     */
    @Transactional
    public PsService createService(PsServiceType type, PsHost source, PsHost destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * //TODO implement this method
     * @param type
     * @param source
     * @param destination
     * @param monitor
     * @return 
     */
    @Transactional
    public PsService createService(PsServiceType type, PsHost source, PsHost destination, PsHost monitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    // === JSON conversion methods ===//
    /**
     * convert service to JSON
     * @param service
     * @return 
     */
    @Transactional
    public JSONObject toJson(PsService service){
        return this.psServiceJson.toJson(service);
    }
    /**
     * convert service to JSON, use requested detailLevel
     * @param service
     * @param detailLevel
     * @return 
     */
    @Transactional
    public JSONObject toJson(PsService service,String detailLevel){
        return this.psServiceJson.toJson(service,detailLevel);
    }
    /**
     * convert list of services to JSONArray
     * @param listOfServices
     * @return 
     */
    @Transactional
    public JSONArray toJson(List<PsService>listOfServices){
        JSONArray resultJson = new JSONArray();
        for(PsService service:listOfServices){
            resultJson.add(this.toJson(service));
        }
        return resultJson;
    }
    /**
     * convert list of services to JSONArray, use requested detail level
     * @param listOfServices
     * @param detailLevel
     * @return 
     */
    @Transactional
    public JSONArray toJson(List<PsService>listOfServices,String detailLevel){
        JSONArray resultJson = new JSONArray();
        for(PsService service:listOfServices){
            resultJson.add(this.toJson(service,detailLevel));
        }
        return resultJson;
    }

    /**
     * get JSON representation of service, build from it PsService object and insert it to database.
     * //TODO implement it later, this is lower priority since
     * normally service creation is handled by high level host and matrix commands
     * @param jsonInput
     * @return 
     */
    @Transactional
    public JSONObject insertNewServiceFromJson(JSONObject jsonInput) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
