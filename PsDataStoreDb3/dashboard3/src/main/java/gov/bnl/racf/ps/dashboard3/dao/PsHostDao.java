/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.objects.PsHost;
import java.util.List;

/**
 *
 * @author tomw
 */
public interface PsHostDao {
    
    public PsHost create();
    public void create(PsHost host);
    
    public PsHost getById(int id) throws PsObjectNotFoundException;
    
    public List<PsHost> getAll();
    
    public void update(PsHost host);
    
    public void delete(int id);
    
    public void delete(PsHost host);        
    
}
