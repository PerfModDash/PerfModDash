/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsRecentServiceResultDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsRecentServiceResultNotFoundException;
import java.util.Iterator;
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
public class PsRecentServiceResultDaoHibernateSessionImpl implements PsRecentServiceResultDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    //=== CRUD methods ===//
    @Override
    @Transactional
    public PsRecentServiceResult create() {
        PsRecentServiceResult psRecentServiceResult = new PsRecentServiceResult();
        this.insert(psRecentServiceResult);
        return psRecentServiceResult;
    }

    @Override
    @Transactional
    public void insert(PsRecentServiceResult psRecentServiceResult) {
        Session session = this.sessionStore.getSession();
        session.save(psRecentServiceResult);
    }

    @Override
    @Transactional
    public PsRecentServiceResult getbyId(int id) throws PsRecentServiceResultNotFoundException {
        Session session = this.sessionStore.getSession();
        PsRecentServiceResult psRecentServiceResult =
                (PsRecentServiceResult) session.get(PsRecentServiceResult.class, id);
        if (psRecentServiceResult == null) {
            throw new PsRecentServiceResultNotFoundException("missing recent service result with id=" + id);
        } else {
            return psRecentServiceResult;
        }
    }

    @Override
    @Transactional
    public PsRecentServiceResult getbyServiceId(int serviceId) throws PsRecentServiceResultNotFoundException {
        Session session = this.sessionStore.getSession();

        String queryString = "from PsRecentServiceResult where service_id=:serviceId";
        Query query = session.createQuery(queryString);
        query.setParameter("serviceId", serviceId);
        List<PsRecentServiceResult> listOfRecentServiceResults = query.list();


        PsRecentServiceResult recentResult = null;
        Iterator iter = listOfRecentServiceResults.iterator();
        while (iter.hasNext()) {
            recentResult = (PsRecentServiceResult) iter.next();
        }
        if (recentResult == null) {
            throw new PsRecentServiceResultNotFoundException("missing recent service result with service id=" + serviceId);
        } else {
            return recentResult;
        }
    }

    @Override
    @Transactional
    public void delete(PsRecentServiceResult psRecentServiceResult) {
        Session session = this.sessionStore.getSession();
        session.delete(psRecentServiceResult);
    }

    @Override
    @Transactional
    public void delete(int id) {
        try {
            PsRecentServiceResult psRecentServiceResult = this.getbyId(id);
            this.delete(psRecentServiceResult);
        } catch (PsRecentServiceResultNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "PsRecentServiceResult not found, id=" + id);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
