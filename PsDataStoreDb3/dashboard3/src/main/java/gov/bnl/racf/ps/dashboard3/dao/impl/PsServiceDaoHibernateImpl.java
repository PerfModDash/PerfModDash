/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import java.util.Collection;
import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author tomw
 */
public class PsServiceDaoHibernateImpl implements PsServiceDao {

    
    //=== Deepndency injection ===//
    private HibernateTemplate hibernateTemplate;
    
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * insert new service
     * @param service 
     */
    @Override
    public void insert(PsService service) {
        this.hibernateTemplate.save(service);
    }

    /**
     * find service with given id
     * @param id
     * @return
     * @throws PsObjectNotFoundException 
     */
    @Override
    public PsService getById(int id) throws PsObjectNotFoundException {
        return this.hibernateTemplate.get(PsService.class, id);
    }

    /**
     * get all services
     * @return 
     */
    @Override
    public List<PsService> getAll() {
        String query = "from PsService";
        return this.hibernateTemplate.find(query);
    }

    /**
     * update service object
     * @param service 
     */
    @Override
    public void update(PsService service) {
        this.hibernateTemplate.update(service);
    }

    /**
     * delete service with given id
     * @param id 
     */
    @Override
    public void delete(int id) {
        String query="delete from PsService where id=?";
        this.hibernateTemplate.bulkUpdate(query, id);
    }

    /**
     * delete service
     * @param service 
     */
    @Override
    public void delete(PsService service) {
        this.hibernateTemplate.delete(service);
    }

    /**
     * delete collection of services
     * @param servicesToBeDeleted 
     */
    @Override
    public void delete(Collection<PsService> servicesToBeDeleted) {
        this.hibernateTemplate.deleteAll(servicesToBeDeleted);
    }
    
}
