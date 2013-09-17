/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceNotFoundException;
import java.util.Collection;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO interface for PsService class 
 *
 * @author tomw
 */
public interface PsServiceDao {
    // there is no create() mthod - this is on purpose. 
    // service creation should be done via the PsServiceOperator calls
    // only and created classes can then be inserted.

    @Transactional
    public void insert(PsService service);

    @Transactional
    public PsService getById(int id) throws PsServiceNotFoundException;

    @Transactional
    public List<PsService> getAll();

    @Transactional
    public void update(PsService service);

    @Transactional
    public void delete(int id);

    @Transactional
    public void delete(PsService service);

    @Transactional
    public void delete(Collection<PsService> servicesToBeDeleted);
}
