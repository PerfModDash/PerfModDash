/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceTypeDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceTypeNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ObjectNotFoundException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsServiceTypeDaoHibernateImpl implements PsServiceTypeDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    //@Transactional
    @Override
    public PsServiceType create() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //@Transactional
    @Override
    public void insert(PsServiceType serviceType) {
        this.hibernateTemplate.save(serviceType);
      //  this.hibernateTemplate.persist(serviceType);
    }

    @Transactional
    @Override
    public PsServiceType getById(int id) throws PsServiceTypeNotFoundException {
        PsServiceType result = 
                (PsServiceType)this.hibernateTemplate.get(PsServiceType.class, id);
        return result;
    }

    @Transactional
    @Override
    public PsServiceType getByServiceTypeId(String serviceTypeId) throws PsServiceTypeNotFoundException{
        String query = "from PsServiceType where serviceTypeId=?";
        List<PsServiceType> listOfServiceTypes = 
                this.hibernateTemplate.find(query, new Object[]{serviceTypeId});
        if(listOfServiceTypes.isEmpty()){
            throw new PsServiceTypeNotFoundException();
        }else{
            if(listOfServiceTypes.size()>1){
                throw new RuntimeException("More than one service type with id="+serviceTypeId+" found!");
            }else{
                return (PsServiceType)listOfServiceTypes.get(0);
            }
        }
    }

    @Override
    public List<PsServiceType> getAll() {
        String query = "from PsServiceType";
        List<PsServiceType> resultList = this.hibernateTemplate.find(query);
        return resultList;
    }

    @Override
    public void update(PsServiceType serviceType) {
        this.hibernateTemplate.update(serviceType);
    }

    @Override
    public void delete(int id) {
        PsServiceType serviceType;
        try {
            serviceType = (PsServiceType)this.getById(id);
            this.delete(serviceType);
        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(PsServiceTypeDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, "failed to delete service type id="+id);
            Logger.getLogger(PsServiceTypeDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(PsServiceType serviceType) {
        this.hibernateTemplate.delete(serviceType);
    }

    @Override
    public boolean containsServiceType(String serviceTypeId) {
        boolean result=true;
        try {
            String query="from PsServiceType where serviceTypeId=?";
            PsServiceType psServiceType = this.getByServiceTypeId(serviceTypeId);
        } catch (PsObjectNotFoundException ex) {
            result=false;
        }
        return result;
    }
}
