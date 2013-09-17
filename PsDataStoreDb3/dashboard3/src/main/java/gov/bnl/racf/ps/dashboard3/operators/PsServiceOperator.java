/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceDao;
import gov.bnl.racf.ps.dashboard3.dao.PsServiceResultDao;
import gov.bnl.racf.ps.dashboard3.dao.PsServiceTypeDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.*;
import gov.bnl.racf.ps.dashboard3.domainobjects.factories.PsServiceFactory;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceTypeNotFoundException;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceJson;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.timer.Timer;
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
    private PsServiceTypeDao psServiceTypeDao;

    public void setPsServiceTypeDao(PsServiceTypeDao psServiceTypeDao) {
        this.psServiceTypeDao = psServiceTypeDao;
    }
    private PsServiceResultOperator psServiceResultOperator;

    public void setPsServiceResultOperator(PsServiceResultOperator psServiceResultOperator) {
        this.psServiceResultOperator = psServiceResultOperator;
    }
    private PsJobOperator psJobOperator;

    public void setPsJobOperator(PsJobOperator psJobOperator) {
        this.psJobOperator = psJobOperator;
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
    public PsService getById(int id) throws PsServiceNotFoundException {
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
     * delete service by Id
     *
     * @param id
     */
    @Transactional
    public void delete(int id) {
        try {
            PsService service = this.getById(id);
            this.delete(service);
        } catch (PsServiceNotFoundException ex) {
            String message = " failed to delete service id=" + id;
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
        }
    }

    /**
     * delete service. Also delete corresponding history records
     *
     * @param serviceToBeDeleted
     */
    @Transactional
    public void delete(PsService serviceToBeDeleted) {
        int numberOfObjectsDeleted = this.psServiceResultOperator.deleteResultsForService(serviceToBeDeleted);
        this.psServiceDao.delete(serviceToBeDeleted);
    }

    /**
     * delete collection of services
     *
     * @param servicesToBeDeleted
     */
    @Transactional
    public void delete(Collection<PsService> servicesToBeDeleted) {
        Iterator iter = servicesToBeDeleted.iterator();
        while (iter.hasNext()) {
            PsService currentService = (PsService) iter.next();
            this.delete(currentService);
        }
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
     * //TODO implement the createService method
     *
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
     * //TODO implement createService method
     *
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
     *
     * @param service
     * @return
     */
    @Transactional
    public JSONObject toJson(PsService service) {
        return this.psServiceJson.toJson(service);
    }

    /**
     * convert service to JSON, use requested detailLevel
     *
     * @param service
     * @param detailLevel
     * @return
     */
    @Transactional
    public JSONObject toJson(PsService service, String detailLevel) {
        return this.psServiceJson.toJson(service, detailLevel);
    }

    /**
     * convert list of services to JSONArray
     *
     * @param listOfServices
     * @return
     */
    @Transactional
    public JSONArray toJson(List<PsService> listOfServices) {
        JSONArray resultJson = new JSONArray();
        for (PsService service : listOfServices) {
            resultJson.add(this.toJson(service));
        }
        return resultJson;
    }

    /**
     * convert list of services to JSONArray, use requested detail level
     *
     * @param listOfServices
     * @param detailLevel
     * @return
     */
    @Transactional
    public JSONArray toJson(List<PsService> listOfServices, String detailLevel) {
        JSONArray resultJson = new JSONArray();
        for (PsService service : listOfServices) {
            resultJson.add(this.toJson(service, detailLevel));
        }
        return resultJson;
    }

    /**
     * get JSON representation of service, build from it PsService object and
     * insert it to database. //TODO add insertNewServiceFromJson(), this is
     * lower priority since normally service creation is handled by high level
     * host and matrix commands
     *
     * @param jsonInput
     * @return
     */
    @Transactional
    public JSONObject insertNewServiceFromJson(JSONObject jsonInput) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Get result of service test. Find out to which service it corresponds,
     * update the service
     *
     * @param serviceResult
     * @return
     * @throws PsServiceNotFoundException
     */
    @Transactional
    public PsService updateServiceResult(PsServiceResult serviceResult) throws PsServiceNotFoundException {

        int serviceId = serviceResult.getService_id();

        PsService service = this.psServiceDao.getById(serviceId);

        if (service != null) {

            service.setRunning(false);
            Date prevCheckTime = serviceResult.getTime();
            service.setPrevCheckTime(prevCheckTime);

            int checkInterval = service.getCheckInterval();
            Date nextCheckTime = new Date(prevCheckTime.getTime()
                    + (long) checkInterval * Timer.ONE_SECOND);
            service.setNextCheckTime(nextCheckTime);

            PsRecentServiceResult recentResult =
                    this.psServiceResultOperator.toRecentServiceResult(serviceResult);

            service.setResult(recentResult);

            this.psServiceResultOperator.insert(serviceResult);

            this.update(service);
        }

        return service;
    }

    /**
     * take service id, find what is its recent result
     *
     * @param serviceId
     * @return
     * @throws PsServiceNotFoundException
     */
    @Transactional
    public PsRecentServiceResult getRecentResultForService(int serviceId) throws PsServiceNotFoundException {
        PsService service = this.getById(serviceId);
        PsRecentServiceResult psRecentServiceResult = service.getResult();
        return psRecentServiceResult;
    }

    @Transactional
    public PsJob buildJob(PsService service) {
        PsJob job = this.psJobOperator.create();

        job.setService_id(service.getId());

        try {
            PsServiceType type;
            type = this.psServiceTypeDao.getByServiceTypeId(service.getType());
            String jobType = type.getJobType();
            job.setType(jobType);
        } catch (PsServiceTypeNotFoundException ex) {
            job.setType("unknown");
            Logger.getLogger(PsServiceOperator.class.getName()).log(Level.SEVERE, null, ex);
        }

        job.setParameters(service.getParameters());

        return job;
    }
}
