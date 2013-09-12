/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsServiceDaoHibernateSessionImpl implements PsServiceDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
    // ===   main methods ===//

    /**
     * insert new service
     *
     * @param service
     */
    @Override
    @Transactional
    public void insert(PsService service) {
        Session session = this.sessionStore.getSession();
        session.save(service);
    }

    /**
     * find service with given id
     *
     * @param id
     * @return
     * @throws PsObjectNotFoundException
     */
    @Override
    @Transactional
    public PsService getById(int id) throws PsServiceNotFoundException {
        Session session = this.sessionStore.getSession();
        PsService service = (PsService) session.get(PsService.class, id);
        if (service == null) {
            throw new PsServiceNotFoundException("Service not dound, id=" + id);
        } else {
            return service;
        }
    }

    /**
     * get all services
     *
     * @return
     */
    @Override
    @Transactional
    public List<PsService> getAll() {
        String query = "from PsService";
        Session session = this.sessionStore.getSession();
        return session.createQuery(query).list();
    }

    /**
     * update service object
     *
     * @param service
     */
    @Override
    @Transactional
    public void update(PsService service) {
        Session session = this.sessionStore.getSession();
        session.update(service);
    }

    /**
     * delete service with given id
     *
     * @param id
     */
    @Override
    @Transactional
    public void delete(int id) {
        String query = "delete from PsService where id=?";
        Session session = this.sessionStore.getSession();
        session.createQuery(query).executeUpdate();
    }

    /**
     * delete service
     *
     * @param service
     */
    @Override
    @Transactional
    public void delete(PsService service) {
        Session session = this.sessionStore.getSession();
        session.delete(service);
    }

    /**
     * delete collection of services
     *
     * @param servicesToBeDeleted
     */
    @Override
    @Transactional
    public void delete(Collection<PsService> servicesToBeDeleted) {
        Session session = this.sessionStore.getSession();
        Iterator iter = servicesToBeDeleted.iterator();
        while (iter.hasNext()) {
            PsService service = (PsService)iter.next();
            session.delete(service);
        }
    }
}
