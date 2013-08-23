/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import java.util.List;

/**
 *
 * @author tomw
 */
public interface PsHostDao {
    
    public PsHost create();
    public void insert(PsHost host);
    
    public PsHost getById(int id) throws PsHostNotFoundException;
    
    public List<PsHost> getAll();
    
    public void update(PsHost host);
    
    public void delete(int id);
    
    public void delete(PsHost host);        
    
}
