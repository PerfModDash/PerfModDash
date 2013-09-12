/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsHostDao;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import gov.bnl.racf.ps.dashboard3.operators.PsHostOperator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

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
    public PsHost create() {
        PsHost host = new PsHost();
        this.insert(host);
        return host;        
    }

    @Override
    public void insert(PsHost host) {
        this.hibernateTemplate.save(host);
    }

    @Override
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
    public List<PsHost> getAll() {
        String query = "from PsHost";
        List<PsHost> listOfHosts = this.hibernateTemplate.find(query);
        return listOfHosts;
    }

    @Override
    public void update(PsHost host) {
        this.hibernateTemplate.update(host);
    }

    @Override
    public void delete(int id) {
        try {
            PsHost host = getById(id);
            this.delete(host);
        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, " failed to delete host id=" + id);
            Logger.getLogger(PsHostDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(PsHost host) {
        this.hibernateTemplate.delete(host);
    }
}
