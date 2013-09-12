/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceResultDao;
import gov.bnl.racf.ps.dashboard3.dao.impl.PsServiceResultDaoHibernateImpl;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceResultNotFoundException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsServiceResultDaoHibernateSessionImpl implements PsServiceResultDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    // === main methods ===//
    @Override
    @Transactional
    public PsServiceResult create() {
        PsServiceResult psServiceResult = new PsServiceResult();
        this.insert(psServiceResult);
        return psServiceResult;
    }

    @Override
    @Transactional
    public void insert(PsServiceResult psServiceResult) {
        Session session = this.sessionStore.getSession();
        session.save(psServiceResult);
    }

    @Override
    @Transactional
    public PsServiceResult getById(int id) throws PsServiceResultNotFoundException {
        Session session = this.sessionStore.getSession();
        PsServiceResult result =
                (PsServiceResult) session.get(PsServiceResult.class, id);
        if (result != null) {
            return result;
        } else {
            throw new PsServiceResultNotFoundException(this.getClass().getName() + " service result not found id=" + id);
        }
    }

    @Override
    @Transactional
    public List<PsServiceResult> getAll() {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsServiceResult";
        List<PsServiceResult> results = session.createQuery(queryString).list();
        return results;
    }

    @Override
    @Transactional
    public List<PsServiceResult> getAllForServiceId(int service_id) {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsServiceResult where serviceId=:serviceId";
        Query query = session.createQuery(queryString);
        query.setParameter("serviceId", service_id);
        List<PsServiceResult> listOfResults = query.list();
        return listOfResults;
    }

    @Override
    @Transactional
    public List<PsServiceResult> getAllForServiceId(int service_id, Date timeAfter) {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsServiceResult where serviceId=:serviceId and time>:timeAfter";
        Query query = session.createQuery(queryString);
        query.setParameter("serviceId", service_id);
        query.setParameter("timeAfter", timeAfter);
        List<PsServiceResult> listOfResults = query.list();
        return listOfResults;
    }

    @Override
    @Transactional
    public void update(PsServiceResult psServiceResult) {
        Session session = this.sessionStore.getSession();
        session.update(psServiceResult);
    }

    @Override
    @Transactional
    public void delete(int id) {
        try {
            PsServiceResult psServiceResult = this.getById(id);
            this.delete(psServiceResult);
        } catch (PsObjectNotFoundException ex) {
            String message = "Failed to delete servcie result id=" + id + " ; object not found";
            Logger.getLogger(PsServiceResultDaoHibernateImpl.class.getName()).log(Level.WARNING, null, message);
            Logger.getLogger(PsServiceResultDaoHibernateImpl.class.getName()).log(Level.WARNING, null, ex);
        }

    }

    @Override
    @Transactional
    public void delete(PsServiceResult psServiceResult) {
        Session session = this.sessionStore.getSession();
        session.delete(psServiceResult);
    }

    @Override
    @Transactional
    public void deleteAllBefore(Date timeBefore) {
        Session session = this.sessionStore.getSession();
        String queryString = "delete from PsServiceResult where time<:timeBefore";
        Query query = session.createQuery(queryString);
        query.setParameter("timeBefore", timeBefore);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public int deleteForServiceId(int service_id) {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsServiceResult where serviceId=:service_id";
        Query query = session.createQuery(queryString);
        query.setParameter("service_id", service_id);
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public int deleteForServiceId(int service_id, Date timeBefore) {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsServiceResult where serviceId=:service_id and time<:timeBefore";
        Query query = session.createQuery(queryString);
        query.setParameter("service_id", service_id);
        query.setParameter("timeBefore", timeBefore);
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public int getResultsCount(Date tmin, Date tmax) {
        Session session = this.sessionStore.getSession();
        String queryString = "select count(*) from PsServiceResult where time between :tmin and :tmax";
        Query query = session.createQuery(queryString);
        query.setParameter("tmin", tmin);
        query.setParameter("tmax", tmax);

        return DataAccessUtils.intResult(query.list());
    }

    @Override
    @Transactional
    public Date getResultsTimeMin() {
        Session session = this.sessionStore.getSession();
        String queryString = "select min(time) from PsServiceResult";
        Query query = session.createQuery(queryString);
        
        Date result = null;
        Iterator iter = query.list().iterator();
        while (iter.hasNext()) {
            result = (Date) iter.next();
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteBetween(Date tmin, Date tmax) {
        Session session = this.sessionStore.getSession();
        String queryString = "delete from PsServiceResult where time between :tmin and :tmax";
        Query query = session.createQuery(queryString);
        query.setParameter("tmin", tmin);
        query.setParameter("tmax", tmax);
        int numberOfObjectsDeleted = query.executeUpdate();
                
        return numberOfObjectsDeleted;
    }
}
