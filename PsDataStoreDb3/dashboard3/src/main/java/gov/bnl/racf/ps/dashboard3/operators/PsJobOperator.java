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
    public PsJob create() {
        return this.psJobDao.create();
    }
    
    public void insert(PsJob psJob){
        this.psJobDao.insert(psJob);
    }
    
    public PsJob getById(int id){
        return this.getById(id);
    }
    
    public void delete(PsJob posJob){
        this.psJobDao.delete(posJob);
    }
    public void delete(int id){
        this.psJobDao.delete(id);
    }

    //=== JSON conversion methods ===//
    public JSONObject toJson(PsJob psJob) {
        return this.psJobJson.toJson(psJob);
    }

    public JSONObject toJson(PsJob psJob, String detailLevel) {
        return this.toJson(psJob);
    }

    public JSONArray toJson(List<PsJob> listOfJobs) {
        return this.psJobJson.toJson(listOfJobs);
    }

    public JSONArray toJson(List<PsJob> listOfJobs, String detailLevel) {
        return this.toJson(listOfJobs);
    }

    //=== main business methods ===//
    public List<PsJob> getJobs(boolean running, boolean setRunning) {

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

    public PsJob getJobById(int id) {
        return this.psJobDao.getById(id);
    }
}
