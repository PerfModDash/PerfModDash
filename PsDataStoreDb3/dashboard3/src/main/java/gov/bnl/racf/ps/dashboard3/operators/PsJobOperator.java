/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsJobDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsJobJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * Placeholder for PsJob operator
 *
 * @author tomw
 */
public class PsJobOperator {

    //=== dependency injection ===//
    private PsJobDao psJobDao;

    public void setPsJobDao(PsJobDao psJobDao) {
        this.psJobDao = psJobDao;
    }
    private PsJobJson psJobJson;

    public void setPsJobJson(PsJobJson psJobJson) {
        this.psJobJson = psJobJson;
    }
    private PsServiceOperator psServiceOperator;

    public void setPsServiceOperator(PsServiceOperator psServiceOperator) {
        this.psServiceOperator = psServiceOperator;
    }

    //=== simple CRUD methods ===//
    @Transactional
    public PsJob create() {
        return this.psJobDao.create();
    }

    @Transactional
    public void insert(PsJob psJob) {
        this.psJobDao.insert(psJob);
    }

    @Transactional
    public PsJob getById(int id) {
        return this.getById(id);
    }

    @Transactional
    public void delete(PsJob posJob) {
        this.psJobDao.delete(posJob);
    }

    @Transactional
    public void delete(int id) {
        this.psJobDao.delete(id);
    }

    //=== JSON conversion methods ===//
    @Transactional
    public JSONObject toJson(PsJob psJob) {
        return this.psJobJson.toJson(psJob);
    }

    @Transactional
    public JSONObject toJson(PsJob psJob, String detailLevel) {
        return this.toJson(psJob);
    }

    @Transactional
    public JSONArray toJson(List<PsJob> listOfJobs) {
        return this.psJobJson.toJson(listOfJobs);
    }

    @Transactional
    public JSONArray toJson(List<PsJob> listOfJobs, String detailLevel) {
        return this.toJson(listOfJobs);
    }

    //=== main business methods ===//
    @Transactional
    public List<PsJob> getJobs(boolean running, boolean setRunning) {

        if (running) {
            // user requested only currently running jobs
            List<PsJob> listOfPsJobs = this.psJobDao.getAll();
            // alternatively we should be able to get services with running flag set to true,
            // then get jobs which corrrespind to those services, but that would be harder
            return listOfPsJobs;
        } else {
            // user asks for list of jobs which are not yet running
            List<PsService> listOfAllServices = this.psServiceOperator.getAll();

            Vector<PsService> listOfServices = new Vector<PsService>();

            Iterator<PsService> iter = listOfAllServices.iterator();
            while (iter.hasNext()) {
                PsService service = (PsService) iter.next();

                boolean selectThisService = true;

                if (!service.isDue()) {
                    selectThisService = false;
                }

                if (running) {
                    if (service.isRunning()) {
                        selectThisService = true;
                    }
                } else {
                    if (!service.isRunning()) {
                        selectThisService = true;
                    }
                }

                if (selectThisService) {
                    listOfServices.add(service);
                }
            }

            // convert list of services to PsJobs
            List<PsJob> listOfPsJobs = this.makeJobsFromServices(listOfServices, setRunning);
            return listOfPsJobs;
        }


    }

    @Transactional
    private List<PsJob> makeJobsFromServices(Vector<PsService> listOfServices, boolean setRunning) {
        // Ok, we have list of selected services. If setRunning=true
        // we have to set them to running

        List<PsJob> listOfJobs = new ArrayList<PsJob>();

        Iterator<PsService> iter = listOfServices.iterator();
        while (iter.hasNext()) {
            PsService service = (PsService) iter.next();
            PsJob job = this.psServiceOperator.buildJob(service);

            if (setRunning) {
                service.startRunning();
                // if service is set to running, then we need to persist the job
                this.psJobDao.insert(job);
            }
            listOfJobs.add(job);
        }
        return listOfJobs;
    }

    @Transactional
    public PsJob getJobById(int id) {
        return this.psJobDao.getById(id);
    }

    @Transactional
    void deletePsJobForService(PsService service) {
        this.psJobDao.deletePsJobForService(service);
    }
}
