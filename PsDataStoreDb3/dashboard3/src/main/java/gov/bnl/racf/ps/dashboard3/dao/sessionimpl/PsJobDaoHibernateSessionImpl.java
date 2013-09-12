/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsJobDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsJobDaoHibernateSessionImpl implements PsJobDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    @Override
    @Transactional
    public PsJob create() {
        PsJob psJob = new PsJob();
        this.insert(psJob);
        return psJob;
    }

    @Override
    @Transactional
    public void insert(PsJob psJob) {
        Session session = this.sessionStore.getSession();
        session.save(psJob);
    }

    @Override
    @Transactional
    public PsJob getById(int id) {
        Session session = this.sessionStore.getSession();
        PsJob psJob = (PsJob) session.get(PsJob.class, id);
        return psJob;
    }

    @Override
    @Transactional
    public List<PsJob> getAll() {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsJob";
        List<PsJob> listOfPsJobs = session.createQuery(queryString).list();
        return listOfPsJobs;

    }

    @Override
    @Transactional
    public void delete(PsJob psJob) {
        Session session = this.sessionStore.getSession();
        session.delete(psJob);
    }

    @Override
    public void delete(int id) {
        PsJob psJob = this.getById(id);
        this.delete(psJob);
    }

    @Override
    @Transactional
    public void deletePsJobForService(PsService service) {
        Session session = this.sessionStore.getSession();
        String queryString = "delete PsJob where service_id = :serviceId";
        Query query = session.createQuery(queryString);
        query.setParameter("serviceId", service.getId());
        query.executeUpdate();
    }
}
