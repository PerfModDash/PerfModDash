/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import java.util.List;

/**
 *
 * @author tomw
 */
public interface PsJobDao {
    
    public PsJob create();
    
    public void insert(PsJob psJob);
    
    public PsJob getById(int id);
    
    public List<PsJob> getAll();
    
    public void delete(PsJob psJob);
    
    public void delete(int id);
    
}
