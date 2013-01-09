/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsJob;
import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;

/**
 * Takes a PsService object and converts it into PsJob 
 * @author tomw
 */
public class PsService2JobConverter {
    public static PsJob buildJob(PsService service){
        PsJob job = new PsJob();
        
        job.setService_id(service.getId());
        
        PsDataStore dataStore = PsDataStore.getDataStore();
        
        PsServiceType type = dataStore.getServiceType(service.getType());
        String jobType = type.getJobType();        
        job.setType(jobType);
        
        job.setParameters(service.getParameters());
        
        
        return job;
    }
}
