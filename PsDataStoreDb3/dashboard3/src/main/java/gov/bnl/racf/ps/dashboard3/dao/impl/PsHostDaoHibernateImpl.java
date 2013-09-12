/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsHostDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsHostDaoHibernateImpl implements PsHostDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    @Transactional
    public PsHost create() {
        PsHost host = new PsHost();
        this.insert(host);
        return host;        
    }

    @Override
    @Transactional
    public void insert(PsHost host) {
        this.hibernateTemplate.save(host);
    }

    @Override
    @Transactional
    public PsHost getById(int id) throws PsHostNotFoundException {
        try {
            PsHost resultHost = (PsHost) this.hibernateTemplate.get(PsHost.class, id);
            resultHost = this.hibernateTemplate.merge(resultHost);
            return resultHost;
        } catch (Exception e) {
            throw new PsHostNotFoundException(this.getClass().getName()+" host not found id="+id);
        }
    }

    @Override
    @Transactional
    public List<PsHost> getAll() {
        String query = "from PsHost";
        List<PsHost> listOfHosts = this.hibernateTemplate.find(query);
        return listOfHosts;
    }

    @Override
    @Transactional
    public void update(PsHost host) {
        this.hibernateTemplate.update(host);
    }

    @Override
    @Transactional
    public void delete(int id) {
        try {
            PsHost host = getById(id);
            this.delete(host);
        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, " failed to delete host id={0}", id);
            Logger.getLogger(PsHostDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void delete(PsHost host) {
        this.hibernateTemplate.delete(host);
    }
}
