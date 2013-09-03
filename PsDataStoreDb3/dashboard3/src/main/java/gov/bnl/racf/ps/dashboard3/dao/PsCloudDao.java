/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.exceptions.PsCloudNotFoundException;
import java.util.List;

/**
 * DAO interface for PsCloud object
 * @author tomw
 */
public interface PsCloudDao {
    
    public PsCloud create();
    
    public void insert(PsCloud cloud);
    
    public PsCloud getById(int id) throws PsCloudNotFoundException;
    
    public List<PsCloud> getByName(String cloudName);
    
    public List<PsCloud> getAll();
    
    public void update(PsCloud cloud);
    
    public void delete(int id);
    
    public void delete(PsCloud cloud);
    
    
}
