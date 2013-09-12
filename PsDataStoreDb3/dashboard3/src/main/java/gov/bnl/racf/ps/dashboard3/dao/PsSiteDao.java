/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * PsSite DAO interface
 *
 * @author tomw
 */
public interface PsSiteDao {

    @Transactional
    public PsSite create();

    @Transactional
    public void insert(PsSite site);

    @Transactional
    public PsSite getById(int id) throws PsSiteNotFoundException;

    @Transactional
    public List<PsSite> getAll();

    @Transactional
    public void update(PsSite site);

    @Transactional
    public void delete(int id);

    @Transactional
    public void delete(PsSite site);
}
