/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsRecentServiceResultNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsRecentServiceResultDao {

    @Transactional
    public PsRecentServiceResult create();

    @Transactional
    public void insert(PsRecentServiceResult psRecentServiceResult);

    @Transactional
    public PsRecentServiceResult getbyId(int id) throws PsRecentServiceResultNotFoundException;

    @Transactional
    public PsRecentServiceResult getbyServiceId(int serviceId) throws PsRecentServiceResultNotFoundException;

    @Transactional
    public void delete(PsRecentServiceResult psRecentServiceResult);

    @Transactional
    public void delete(int id);
}
