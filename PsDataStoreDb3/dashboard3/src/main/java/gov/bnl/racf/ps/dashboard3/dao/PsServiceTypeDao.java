/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceTypeNotFoundException;
import java.util.List;

/**
 * Interface which defines DAO for serviceTypes
 * @author tomw
 */
public interface PsServiceTypeDao {
    public PsServiceType create();
    public void          insert(PsServiceType serviceType);
    public PsServiceType getById(int id) throws PsServiceTypeNotFoundException;
    public PsServiceType getByServiceTypeId(String serviceTypeId)throws PsServiceTypeNotFoundException;
    public List<PsServiceType> getAll();
    public void update(PsServiceType serviceType);
    public void delete(int id);
    public void delete(PsServiceType serviceType);  
    
    public boolean containsServiceType(String serviceTypeId);
    
}
