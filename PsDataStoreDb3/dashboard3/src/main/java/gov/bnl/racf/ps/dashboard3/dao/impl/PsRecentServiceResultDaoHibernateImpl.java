/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsRecentServiceResultDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsRecentServiceResultNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author tomw
 */
public class PsRecentServiceResultDaoHibernateImpl implements PsRecentServiceResultDao{

    // === dependency injection ===//
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
    
    //=== CRUD methods ===//
    
    @Override
    public PsRecentServiceResult create() {
        PsRecentServiceResult psRecentServiceResult = new PsRecentServiceResult();
        this.insert(psRecentServiceResult);
        return psRecentServiceResult;
    }

    @Override
    public void insert(PsRecentServiceResult psRecentServiceResult) {
        this.hibernateTemplate.save(psRecentServiceResult);
    }

    @Override
    public PsRecentServiceResult getbyId(int id) throws PsRecentServiceResultNotFoundException{
        PsRecentServiceResult psRecentServiceResult = 
                this.hibernateTemplate.get(PsRecentServiceResult.class, id);
        if(psRecentServiceResult==null){
            throw new PsRecentServiceResultNotFoundException("missing recent service result with id="+id);
        }else{
            return psRecentServiceResult;
        }
    }

    @Override
    public PsRecentServiceResult getbyServiceId(int serviceId) throws PsRecentServiceResultNotFoundException {
       
        String query = "from PsRecentServiceResult where service_id=?";
        List<PsRecentServiceResult> listOfRecentServiceResults = 
                this.hibernateTemplate.find(query, new Object[]{serviceId});
        
        PsRecentServiceResult recentResult = null;
        Iterator iter = listOfRecentServiceResults.iterator();
        while (iter.hasNext()) {
            recentResult = (PsRecentServiceResult) iter.next();
        }
        if(recentResult==null){
            throw new PsRecentServiceResultNotFoundException("missing recent service result with service id="+serviceId);
        }else{
            return recentResult;
        }
    }

    @Override
    public void delete(PsRecentServiceResult psRecentServiceResult) {
        this.hibernateTemplate.delete(psRecentServiceResult);
    }

    @Override
    public void delete(int id) {
        try {
            PsRecentServiceResult psRecentServiceResult=this.getbyId(id);
            this.delete(psRecentServiceResult);
        } catch (PsRecentServiceResultNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "PsRecentServiceResult not found, id="+id);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
