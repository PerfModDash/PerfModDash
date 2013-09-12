/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsCloudDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsCloudNotFoundException;
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
public class PsCloudDaoHibernateSessionImpl implements PsCloudDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    //=== main methods ===//
    @Override
    @Transactional
    public PsCloud create() {
        PsCloud newCloud = new PsCloud();
        this.insert(newCloud);
        return newCloud;
    }

    @Override
    @Transactional
    public void insert(PsCloud cloud) {
        Session session = this.sessionStore.getSession();
        session.save(cloud);
    }

    @Override
    @Transactional
    public PsCloud getById(int id) throws PsCloudNotFoundException {
        Session session = this.sessionStore.getSession();
        PsCloud cloud = (PsCloud) session.get(PsCloud.class, id);
        if (cloud == null) {
            throw new PsCloudNotFoundException(this.getClass().getName() + " cloud not found id=" + id);
        } else {
            return cloud;
        }
    }

    @Override
    @Transactional
    public List<PsCloud> getByName(String cloudName) {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsCloud where name=:cloudName";
        Query query = session.createQuery(queryString);
        query.setParameter("cloudName", cloudName);
        List<PsCloud> listOfResults = query.list();
        return listOfResults;
    }

    @Override
    @Transactional
    public List<PsCloud> getAll() {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsCloud";
        List<PsCloud> listOfResults = session.createQuery(queryString).list();
        return listOfResults;
    }

    @Override
    @Transactional
    public void update(PsCloud cloud) {
        Session session = this.sessionStore.getSession();
        session.update(cloud);
    }

    @Override
    @Transactional
    public void delete(int id) {
        try {
            PsCloud cloud = this.getById(id);
            this.delete(cloud);
        } catch (PsCloudNotFoundException ex) {
            String message = this.getClass().getName() + " failed to delete cloud, cloud not found, id=" + id;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void delete(PsCloud cloud) {
        Session session = this.sessionStore.getSession();
        session.delete(cloud);
    }
}
