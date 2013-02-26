/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.*;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import org.hibernate.Session;
import org.json.simple.JSONObject;

/**
 * static class for creating various objects and saving them to storage at the same time
 * @author tomw
 */
public class PsObjectCreator {
    public static PsService createNewService(Session session){
        PsService service = new PsService();
        session.save(service);
        return service;
    }
    
    public static PsMatrix createNewMatrix(Session session){
        PsMatrix matrix = new PsMatrix();
        session.save(matrix);
        return matrix;
    }
    public static PsMatrix createNewMatrix(Session session,String serviceTypeId, String matrixName){
        PsServiceType type = PsDataStore.getServiceType(session,serviceTypeId);
        PsMatrix matrix = new PsMatrix(type,matrixName);
        session.save(matrix);
        matrix.setMatrixType(type);
        matrix.setName(matrixName);
        return matrix;
    }
    public static PsCloud createNewCloud(Session session){
        PsCloud cloud = new PsCloud();
        session.save(cloud);
        return cloud;
    }
    
    public static PsCloud createNewCloud(Session session, JSONObject json){
        PsCloud cloud = createNewCloud(session);
        PsObjectUpdater.update(cloud, json);
        return cloud;
    }
    
    public static PsJob createNewJob(Session session){
        PsJob job = new PsJob();
        // Unlike other objects, we do not save the job in session.
        //
        // The reason for this policy is: client may request jobs with
        // setRunning=0 option. In this case we return only job information, but
        // do not set corresponding services to running. So no jobs start, therefore
        // we do not save the job object.
        //
        // The decision whether save or not the job object is done in the PsQueryJobs servlet
        // when the setRunning flag is decoded
        
        //session.save(job);
        return job;
    }
    
    public static PsRecentServiceResult createNewRecentServiceResult(Session session){
        PsRecentServiceResult recentResult = new PsRecentServiceResult();
        session.save(recentResult);
        return recentResult;
    }
   
}
