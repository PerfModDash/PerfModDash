/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsSiteDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import java.util.List;

/**
 *
 * @author tomw
 */
public class PsSiteDaoHibernateImpl implements PsSiteDao{

    @Override
    public PsSite create() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(PsSite site) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PsSite getById(int id) throws PsSiteNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PsSite> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(PsSite site) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(PsSite site) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
