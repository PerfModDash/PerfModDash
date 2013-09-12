/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.exceptions.PsCloudNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO interface for PsCloud object
 * @author tomw
 */
public interface PsCloudDao {
    
    @Transactional
    public PsCloud create();
    @Transactional
    public void insert(PsCloud cloud);
    @Transactional
    public PsCloud getById(int id) throws PsCloudNotFoundException;
    @Transactional
    public List<PsCloud> getByName(String cloudName);
    @Transactional
    public List<PsCloud> getAll();
    @Transactional
    public void update(PsCloud cloud);
    @Transactional
    public void delete(int id);
    @Transactional
    public void delete(PsCloud cloud);
    
    
}
