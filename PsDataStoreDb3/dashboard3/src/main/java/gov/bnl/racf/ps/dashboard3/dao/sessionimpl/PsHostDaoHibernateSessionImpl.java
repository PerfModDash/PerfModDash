/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsHostDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;

/**
 *
 * @author tomw
 */
public class PsHostDaoHibernateSessionImpl implements PsHostDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
    // ===   main methods ===//

    @Override
    public PsHost create() {
        PsHost host = new PsHost();
        Session session = this.sessionStore.getSession();
        session.save(host);
        return host;
    }

    @Override
    public void insert(PsHost host) {
        Session session = this.sessionStore.getSession();
        session.save(host);
    }

    @Override
    public PsHost getById(int id) throws PsHostNotFoundException {
        Session session = this.sessionStore.getSession();
        PsHost host = (PsHost)session.get(PsHost.class, id);
        return host;
    }

    @Override
    public List<PsHost> getAll() {
        String queryString = "from PsHost";
        Session session = this.sessionStore.getSession();
        List<PsHost>listOfResults = session.createQuery(queryString).list();
        return listOfResults;
    }

    @Override
    public void update(PsHost host) {
        Session session = this.sessionStore.getSession();
        session.update(host);
    }

    @Override
    public void delete(int id) {
        try {
            Session session = this.sessionStore.getSession();
            PsHost host = (PsHost)this.getById(id);
            session.delete(host);
        } catch (PsHostNotFoundException ex) {
            String message="host with id="+id+" not found";
            Logger.getLogger(PsHostDaoHibernateSessionImpl.class.getName()).log(Level.WARNING, null, message);
            Logger.getLogger(PsHostDaoHibernateSessionImpl.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    @Override
    public void delete(PsHost host) {
        Session session = this.sessionStore.getSession();
        session.delete(host);
    }
}
