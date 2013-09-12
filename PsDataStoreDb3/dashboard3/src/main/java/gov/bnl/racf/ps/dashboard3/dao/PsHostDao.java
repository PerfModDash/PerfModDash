/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsHostDao {

    @Transactional
    public PsHost create();

    @Transactional
    public void insert(PsHost host);

    @Transactional
    public PsHost getById(int id) throws PsHostNotFoundException;

    @Transactional
    public List<PsHost> getAll();

    @Transactional
    public void update(PsHost host);

    @Transactional
    public void delete(int id);

    @Transactional
    public void delete(PsHost host);
}
