/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsHostDao;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.objects.PsHost;
import java.util.List;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(PsHost host) {
        this.hibernateTemplate.save(host);
    }

    @Override
    public PsHost getById(int id) throws PsObjectNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PsHost> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(PsHost host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(PsHost host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
