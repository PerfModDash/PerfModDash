/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import org.hibernate.Session;

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
        PsMatrix matrix = createNewMatrix(session);
        PsServiceType type = PsDataStore.getServiceType(session,serviceTypeId);
        matrix.setMatrixType(type);
        matrix.setName(matrixName);
        return matrix;
    }
}
