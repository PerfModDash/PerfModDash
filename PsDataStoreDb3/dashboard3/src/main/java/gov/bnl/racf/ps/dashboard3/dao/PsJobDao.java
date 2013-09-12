/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsJobDao {

    @Transactional
    public PsJob create();

    @Transactional
    public void insert(PsJob psJob);

    @Transactional
    public PsJob getById(int id);

    @Transactional
    public List<PsJob> getAll();

    @Transactional
    public void delete(PsJob psJob);

    @Transactional
    public void delete(int id);

    @Transactional
    public void deletePsJobForService(PsService service);
}
