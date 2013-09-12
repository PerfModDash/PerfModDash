/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsSiteDao;
import gov.bnl.racf.ps.dashboard3.dao.impl.PsSiteDaoHibernateImpl;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsSiteDaoHibernateSessionImpl implements PsSiteDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    // === main methods ===//
    @Transactional
    @Override
    public PsSite create() {
        PsSite site = new PsSite();
        this.insert(site);
        return site;
    }

    @Override
    @Transactional
    public void insert(PsSite site) {
        Session session = this.sessionStore.getSession();
        session.save(site);
    }

    @Override
    @Transactional
    public PsSite getById(int id) throws PsSiteNotFoundException {
        Session session = this.sessionStore.getSession();
        PsSite site = (PsSite) session.get(PsSite.class, id);
        if (site == null) {
            throw new PsSiteNotFoundException(this.getClass().getName() + " site not dound id=" + id);
        } else {
            return site;
        }
    }

    @Override
    @Transactional
    public List<PsSite> getAll() {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsSite";
        return session.createQuery(queryString).list();
    }

    @Override
    @Transactional
    public void update(PsSite site) {
        Session session = this.sessionStore.getSession();
        session.update(site);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Session session = this.sessionStore.getSession();
        try {
            PsSite site = this.getById(id);
            session.delete(site);
        } catch (PsSiteNotFoundException ex) {
            String message = "site with id=" + id + " not found";
            Logger.getLogger(PsSiteDaoHibernateImpl.class.getName()).log(Level.WARNING, null, message);
            Logger.getLogger(PsSiteDaoHibernateImpl.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    @Override
    @Transactional
    public void delete(PsSite site) {
        Session session = this.sessionStore.getSession();
        session.delete(site);
    }
}
