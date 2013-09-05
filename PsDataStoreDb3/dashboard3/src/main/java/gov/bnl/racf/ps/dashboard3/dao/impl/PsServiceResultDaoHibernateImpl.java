/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceResultDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceResultNotFoundException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.springframework.dao.support.DataAccessUtils;
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
    public PsServiceResult getById(int id) throws PsServiceResultNotFoundException {
        PsServiceResult result = 
                (PsServiceResult) this.hibernateTemplate.get(PsServiceResult.class, id);
        if(result!=null){
            return result;
        }else{
            throw new PsServiceResultNotFoundException(this.getClass().getName()+" service result not found id="+id);
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
        String query="from PsServiceResult where serviceId=?";
        List<PsServiceResult>listOfResults = 
                this.hibernateTemplate.find(query, new Object[]{service_id});
        return listOfResults;
    }

    @Override
    public List<PsServiceResult> getAllForServiceId(int service_id, Date timeAfter) {
        String query="from PsServiceResult where serviceId=? and time>?";
        List<PsServiceResult>listOfResults = 
                this.hibernateTemplate.find(query, new Object[]{service_id,timeAfter});
        return listOfResults;
    }

    @Override
    public void update(PsServiceResult psServiceResult) {
        this.hibernateTemplate.update(psServiceResult);
    }

    @Override
    public void delete(int id) {
        try {
            PsServiceResult psServiceResult=this.getById(id);
            this.delete(psServiceResult);
        } catch (PsObjectNotFoundException ex) {
            String message="Failed to delete servcie result id="+id+" ; object not found";
            Logger.getLogger(PsServiceResultDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsServiceResultDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void delete(PsServiceResult psServiceResult) {
        this.hibernateTemplate.delete(psServiceResult);
    }

    @Override
    public void deleteAllBefore(Date timeBefore) {
        String query="delete from PsServiceResult where time<?";
        this.hibernateTemplate.bulkUpdate(query, timeBefore);
    }

    @Override
    public int deleteForServiceId(int service_id) {
        String query="from PsServiceResult where serviceId=?";
        return this.hibernateTemplate.bulkUpdate(query, new Object[]{service_id});
    }

    @Override
    public int deleteForServiceId(int service_id, Date timeBefore) {
        String query="from PsServiceResult where serviceId=? and time<?";
        return this.hibernateTemplate.bulkUpdate(query, new Object[]{service_id,timeBefore});
    }

    @Override
    public int getResultsCount(Date tmin, Date tmax) {
        String query = "select count(*) from PsServiceResult where time between ? and ?";
        
        return DataAccessUtils.intResult(this.hibernateTemplate.find(query, new Object[]{tmin,tmax}));
        
    }

    @Override
    public Date getResultsTimeMin() {
        String query = "select min(time) from PsServiceResult";
        
        Date result = null;
        Iterator iter = this.hibernateTemplate.find(query).iterator();
        while (iter.hasNext()) {
            result = (Date) iter.next();
        }
        return result;        
    }

    @Override
    public int deleteBetween(Date tmin, Date tmax) {
        String queryString = "delete from PsServiceResult where time between ? and ?";
        int numberOfObjectsDeleted = 
                this.hibernateTemplate.bulkUpdate(queryString, new Object[]{tmin,tmax});
        return numberOfObjectsDeleted;
    }
    
}
