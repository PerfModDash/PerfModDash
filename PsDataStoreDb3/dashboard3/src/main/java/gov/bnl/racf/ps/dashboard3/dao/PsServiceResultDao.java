/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tomw
 */
public interface PsServiceResultDao {
    public  PsServiceResult create();
    public void insert(PsServiceResult serviceResult);
    
    public PsServiceResult getById(int id) throws PsObjectNotFoundException;
    
    public List<PsServiceResult> getAll();
    
    public List<PsServiceResult> getAllForServiceId(int service_id);
    
    public List<PsServiceResult> getAllForServiceId(int service_id, Date timeAfter);
    
    public void update(PsServiceResult psServiceResult);
    
    public void delete(int id);
    
    public void delete(PsServiceResult psServiceResult);   
   
    public void deleteAllBefore(Date timeBefore);
    
    public void deleteForServiceId(int service_id);
    
    public void deleteForServiceId(int service_id, Date timeBefore);
    
   
}
