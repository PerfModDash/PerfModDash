/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsJobDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author tomw
 */
public class PsJobDaoHibernateImpl implements PsJobDao {

    // === Dependency injection ===//
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public PsJob create() {
        PsJob psJob = new PsJob();
        this.insert(psJob);
        return psJob;
    }

    @Override
    public void insert(PsJob psJob) {
        this.hibernateTemplate.save(psJob);
    }

    @Override
    public PsJob getById(int id) {
        PsJob psJob = this.hibernateTemplate.get(PsJob.class, id);
        return psJob;
    }

    @Override
    public List<PsJob> getAll() {
        String query = "from PsJob";
        List<PsJob> listOfPsJobs = this.hibernateTemplate.find(query);
        return listOfPsJobs;

    }

    @Override
    public void delete(PsJob psJob) {
        this.hibernateTemplate.delete(psJob);
    }

    @Override
    public void delete(int id) {
        PsJob psJob = this.getById(id);
        this.delete(psJob);
    }

    @Override
    public void deletePsJobForService(PsService service) {
        String query = "delete PsJob where service_id = ?";
        int result = this.hibernateTemplate.bulkUpdate(query, new Object[]{service.getId()});
    }
}
