/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceResult;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import org.hibernate.Session;

/**
 *
 * @author tomw
 */
public class PsServiceResult2RecentServiceResult {

    public static PsRecentServiceResult copy(Session session, PsServiceResult serviceResult) {
        int serviceId = serviceResult.getService_id();
        PsRecentServiceResult recentResult = PsDataStore.getRecentResultForService(session, serviceId);
        if (recentResult == null) {
            recentResult = PsObjectCreator.createNewRecentServiceResult(session);
        }

        recentResult.setJob_id(serviceResult.getJob_id());
        recentResult.setMessage(serviceResult.getMessage());
        recentResult.setParameters(serviceResult.getParameters());
        recentResult.setServiceResultId(serviceResult.getId());
        recentResult.setService_id(serviceResult.getService_id());
        recentResult.setStatus(serviceResult.getStatus());
        recentResult.setTime(serviceResult.getTime());

        return recentResult;
    }
}
