/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsRecentServiceResultNotFoundException;

/**
 *
 * @author tomw
 */
public interface PsRecentServiceResultDao {
    
    public PsRecentServiceResult create();
    
    public void insert(PsRecentServiceResult psRecentServiceResult);
    
    public PsRecentServiceResult getbyId(int id) throws PsRecentServiceResultNotFoundException;
    
    public PsRecentServiceResult getbyServiceId(int serviceId)  throws PsRecentServiceResultNotFoundException;
    
    public void delete(PsRecentServiceResult psRecentServiceResult);
    
    public void delete(int id);
    
}
