/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceResult;

/**
 *
 * @author tomw
 */
public class PsServiceResult2RecentServiceResult {
    public static PsRecentServiceResult copy(PsServiceResult serviceResult){
        PsRecentServiceResult recentResult = new PsRecentServiceResult();
        
        recentResult.setJob_id(serviceResult.getJob_id());
        recentResult.setMessage(serviceResult.getMessage());
        recentResult.setParameters(serviceResult.getParameters());
        recentResult.setServiceResultId(serviceResult.getId());
        recentResult.setService_id(serviceResult.getService_id());
        recentResult.setStatus(serviceResult.getStatus());
        recentResult.setTime(recentResult.getTime());
        
        return recentResult;
    }
}
