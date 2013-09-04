/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsMatrixDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author tomw
 */
public class PsMatrixDaoHibernateImpl implements PsMatrixDao {

    //=== dependency injection part
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    //=== main part ===//
    @Override
    public void insert(PsMatrix matrix) {
        this.hibernateTemplate.save(matrix);
    }

    @Override
    public PsMatrix getById(int id) throws PsMatrixNotFoundException {
        PsMatrix matrix = (PsMatrix) this.hibernateTemplate.get(PsMatrix.class, id);
        if (matrix != null) {
            return matrix;
        } else {
            throw new PsMatrixNotFoundException(this.getClass().getName()+" matrix not found id="+id);
        }
    }

    @Override
    public List<PsMatrix> getAll() {
        String query = "from PsMatrix";
        List<PsMatrix> listOfMatrices = this.hibernateTemplate.find(query);
        return listOfMatrices;
    }

    @Override
    public void update(PsMatrix matrix) {
        this.hibernateTemplate.update(matrix);
    }

    @Override
    public void delete(int id) {
        try {
            PsMatrix matrix=this.getById(id);
            this.delete(matrix);
        } catch (PsMatrixNotFoundException ex) {
            String message="cannot delete matrix id="+id+" , matrix not found";
            Logger.getLogger(PsMatrixDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsMatrixDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(PsMatrix matrix) {
        this.hibernateTemplate.delete(matrix);
    }

    
}
