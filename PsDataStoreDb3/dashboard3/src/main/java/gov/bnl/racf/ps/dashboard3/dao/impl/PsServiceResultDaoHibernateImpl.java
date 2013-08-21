/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceResultDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import java.util.Date;
import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author tomw
 */
public class PsServiceResultDaoHibernateImpl implements PsServiceResultDao{

     private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
    
    
    @Override
    public PsServiceResult create() {
        PsServiceResult psServiceResult = new PsServiceResult();
        this.insert(psServiceResult);
        return psServiceResult;
    }

    @Override
    public void insert(PsServiceResult psServiceResult) {
        this.hibernateTemplate.save(psServiceResult);
    }

    @Override
    public PsServiceResult getById(int id) throws PsObjectNotFoundException {
        PsServiceResult result = 
                (PsServiceResult) this.hibernateTemplate.get(PsServiceResult.class, id);
        if(result!=null){
            return result;
        }else{
            throw new PsObjectNotFoundException();
        }
    }

    @Override
    public List<PsServiceResult> getAll() {
        String query="from PsServiceResult";
        List<PsServiceResult>results=this.hibernateTemplate.find(query);
        return results;
    }

    @Override
    public List<PsServiceResult> getAllForServiceId(int service_id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PsServiceResult> getAllForServiceId(int service_id, Date timeAfter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(PsServiceResult psServiceResult) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(PsServiceResult psServiceResult) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteAllBefore(Date timeBefore) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteForServiceId(int service_id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteForServiceId(int service_id, Date timeBefore) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
