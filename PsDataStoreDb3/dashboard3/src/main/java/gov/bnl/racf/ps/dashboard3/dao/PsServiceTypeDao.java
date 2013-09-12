/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceTypeNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface which defines DAO for serviceTypes
 *
 * @author tomw
 */
public interface PsServiceTypeDao {

    @Transactional
    public PsServiceType create();

    @Transactional
    public void insert(PsServiceType serviceType);

    @Transactional
    public PsServiceType getById(int id) throws PsServiceTypeNotFoundException;

    @Transactional
    public PsServiceType getByServiceTypeId(String serviceTypeId) throws PsServiceTypeNotFoundException;

    @Transactional
    public List<PsServiceType> getAll();

    @Transactional
    public void update(PsServiceType serviceType);

    @Transactional
    public void delete(int id);

    @Transactional
    public void delete(PsServiceType serviceType);

    @Transactional
    public boolean containsServiceType(String serviceTypeId);
}
