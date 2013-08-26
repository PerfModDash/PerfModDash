/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import java.util.List;

/**
 * Interface for the PsMatrix Dao class
 * @author tomw
 */
public interface PsMatrixDao {

    public void insert(PsMatrix matrix);

    public PsMatrix getById(int id) throws PsMatrixNotFoundException;

    public List<PsMatrix> getAll();

    public void update(PsMatrix matrix);

    public void delete(int id);

    public void delete(PsMatrix matrix);
}
