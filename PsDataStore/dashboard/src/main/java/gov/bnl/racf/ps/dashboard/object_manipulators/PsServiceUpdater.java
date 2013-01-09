/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_objects.PsServiceResult;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import java.util.Date;
import javax.management.timer.Timer;


/**
 * Utility class for uploading results of a service
 * @author tomw
 */
public class PsServiceUpdater {
    /**
     * take a result object, obtain from it service id
     * get this service from data store
     * update its fields
     * @param result 
     */
    public static void update(PsServiceResult result){
        PsDataStore dataStore  = PsDataStore.getDataStore();
        
        String serviceId = result.getService_id();
        
        PsService service = dataStore.getService(serviceId);
       
        service.setRunning(false);
        Date prevCheckTime = result.getTime();
        service.setPrevCheckTime(prevCheckTime);
        
        int checkInterval = service.getCheckInterval();
        Date nextCheckTime = new Date(prevCheckTime.getTime()+
                (long)checkInterval*Timer.ONE_SECOND);
        service.setNextCheckTime(nextCheckTime);
        
        service.setResult(result);
                
    }
}
