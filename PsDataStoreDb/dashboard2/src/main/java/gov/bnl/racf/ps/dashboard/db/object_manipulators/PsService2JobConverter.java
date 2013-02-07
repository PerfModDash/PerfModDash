/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsJob;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import org.hibernate.Session;



/**
 * Takes a PsService object and converts it into PsJob 
 * @author tomw
 */
public class PsService2JobConverter {
    public static PsJob buildJob(Session session,PsService service){
        PsJob job = PsObjectCreator.createNewJob(session);
        
        job.setService_id(service.getId());
        
        PsServiceType type = PsDataStore.getServiceType(session, service.getType());
        String jobType = type.getJobType();        
        job.setType(jobType);
        
        job.setParameters(service.getParameters());
        
        return job;
    }
}
