/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceResultNotFoundException;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsServiceResultDao {

    @Transactional
    public PsServiceResult create();

    @Transactional
    public void insert(PsServiceResult serviceResult);

    @Transactional
    public PsServiceResult getById(int id) throws PsServiceResultNotFoundException;

    @Transactional
    public List<PsServiceResult> getAll();

    @Transactional
    public List<PsServiceResult> getAllForServiceId(int service_id);

    @Transactional
    public List<PsServiceResult> getAllForServiceId(int service_id, Date timeAfter);

    @Transactional
    public void update(PsServiceResult psServiceResult);

    @Transactional
    public void delete(int id);

    @Transactional
    public void delete(PsServiceResult psServiceResult);

    @Transactional
    public void deleteAllBefore(Date timeBefore);

    @Transactional
    public int deleteBetween(Date tmin, Date tmax);

    @Transactional
    public int deleteForServiceId(int service_id);

    @Transactional
    public int deleteForServiceId(int service_id, Date timeBefore);

    @Transactional
    public int getResultsCount(Date tmin, Date tmax);

    @Transactional
    public Date getResultsTimeMin();
}
