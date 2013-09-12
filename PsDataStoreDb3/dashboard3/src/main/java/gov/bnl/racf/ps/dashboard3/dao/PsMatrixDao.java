/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface for the PsMatrix Dao class
 *
 * @author tomw
 */
public interface PsMatrixDao {

    @Transactional
    public void insert(PsMatrix matrix);

    @Transactional
    public PsMatrix getById(int id) throws PsMatrixNotFoundException;

    @Transactional
    public List<PsMatrix> getAll();

    @Transactional
    public void update(PsMatrix matrix);

    @Transactional
    public void delete(int id);

    @Transactional
    public void delete(PsMatrix matrix);
}
