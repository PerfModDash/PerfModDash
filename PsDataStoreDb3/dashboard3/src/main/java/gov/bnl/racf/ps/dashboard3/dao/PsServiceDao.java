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

/**
 * DAO interface for PsService class
 * //TODO provide implementation of this class
 * @author tomw
 */
public interface PsServiceDao {
    // there is no create() mthod - this is on purpose. 
    // service creation should be done via the PsServiceOperator calls
    // only and created classes can then be inserted.
    
    public void insert(PsService service);
    
    public PsService getById(int id) throws PsServiceNotFoundException;
    
    public List<PsService> getAll();
    
    public void update(PsService service);
    
    public void delete(int id);
    
    public void delete(PsService service);  
    
    public void delete(Collection<PsService> servicesToBeDeleted);
}
