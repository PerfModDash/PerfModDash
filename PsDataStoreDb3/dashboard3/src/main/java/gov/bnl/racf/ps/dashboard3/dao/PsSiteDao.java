/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import java.util.List;

/**
 * PsSite DAO interface
 *
 * @author tomw
 */
public interface PsSiteDao {

    public PsSite create();

    public void insert(PsSite site);

    public PsSite getById(int id) throws PsSiteNotFoundException;

    public List<PsSite> getAll();

    public void update(PsSite site);

    public void delete(int id);

    public void delete(PsSite site);
}
