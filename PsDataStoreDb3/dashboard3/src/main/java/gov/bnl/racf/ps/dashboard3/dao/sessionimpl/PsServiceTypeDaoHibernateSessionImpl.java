/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceTypeDao;
import gov.bnl.racf.ps.dashboard3.dao.impl.PsServiceTypeDaoHibernateImpl;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceTypeNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsServiceTypeDaoHibernateSessionImpl implements PsServiceTypeDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    // === main methods ===//
    @Transactional
    @Override
    public PsServiceType create() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional
    @Override
    public void insert(PsServiceType serviceType) {
        Session session = this.sessionStore.getSession();
        session.save(serviceType);
    }

    @Transactional
    @Override
    public PsServiceType getById(int id) throws PsServiceTypeNotFoundException {
        Session session = this.sessionStore.getSession();
        PsServiceType result = (PsServiceType) session.get(PsServiceType.class, id);
        return result;
    }

    @Transactional
    @Override
    public PsServiceType getByServiceTypeId(String serviceTypeId) throws PsServiceTypeNotFoundException {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsServiceType where serviceTypeId=:serviceTypeId";

        Query query = session.createQuery(queryString);
        query.setParameter("serviceTypeId", serviceTypeId);

        List<PsServiceType> listOfServiceTypes = query.list();

        if (listOfServiceTypes.isEmpty()) {
            throw new PsServiceTypeNotFoundException(
                    this.getClass().getName() + " service type not found type id=" + serviceTypeId);
        } else {
            if (listOfServiceTypes.size() > 1) {
                throw new RuntimeException("More than one service type with id=" + serviceTypeId + " found!");
            } else {
                return (PsServiceType) listOfServiceTypes.get(0);
            }
        }
    }

    @Transactional
    @Override
    public List<PsServiceType> getAll() {
        Session session = this.sessionStore.getSession();
        String query = "from PsServiceType";
        List<PsServiceType> resultList = session.createQuery(query).list();
        return resultList;
    }

    @Transactional
    @Override
    public void update(PsServiceType serviceType) {
        Session session = this.sessionStore.getSession();
        session.update(serviceType);
    }

    @Transactional
    @Override
    public void delete(int id) {
        Session session = this.sessionStore.getSession();
        PsServiceType serviceType;
        try {
            serviceType = (PsServiceType) this.getById(id);
            session.delete(serviceType);
        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(PsServiceTypeDaoHibernateImpl.class.getName()).log(Level.WARNING, null, "failed to delete service type id=" + id);
            Logger.getLogger(PsServiceTypeDaoHibernateImpl.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    @Transactional
    @Override
    public void delete(PsServiceType serviceType) {
        Session session = this.sessionStore.getSession();
        session.delete(serviceType);
    }

    @Transactional
    @Override
    public boolean containsServiceType(String serviceTypeId) {
        boolean result = true;
        try {
            
            PsServiceType psServiceType = this.getByServiceTypeId(serviceTypeId);
        } catch (PsObjectNotFoundException ex) {
            result = false;
        }
        return result;
    }
}
