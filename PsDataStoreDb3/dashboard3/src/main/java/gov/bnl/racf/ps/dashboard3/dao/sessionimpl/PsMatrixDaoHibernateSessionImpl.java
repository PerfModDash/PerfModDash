/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import gov.bnl.racf.ps.dashboard3.dao.PsMatrixDao;
import gov.bnl.racf.ps.dashboard3.dao.impl.PsMatrixDaoHibernateImpl;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsMatrixDaoHibernateSessionImpl implements PsMatrixDao {

    private SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
    

    //=== main part ===//
    @Override
    @Transactional
    public void insert(PsMatrix matrix) {
        Session session = this.sessionStore.getSession();
        session.save(matrix);
    }

    @Override
    @Transactional
    public PsMatrix getById(int id) throws PsMatrixNotFoundException {
        Session session = this.sessionStore.getSession();
        PsMatrix matrix = (PsMatrix) session.get(PsMatrix.class, id);
        if (matrix != null) {
            return matrix;
        } else {
            throw new PsMatrixNotFoundException(this.getClass().getName() + " matrix not found id=" + id);
        }
    }

    @Override
    @Transactional
    public List<PsMatrix> getAll() {
        Session session = this.sessionStore.getSession();
        String queryString = "from PsMatrix";
        List<PsMatrix> listOfMatrices = session.createQuery(queryString).list();
        return listOfMatrices;
    }

    @Override
    @Transactional
    public void update(PsMatrix matrix) {
        Session session = this.sessionStore.getSession();
        session.update(matrix);
    }

    @Override
    @Transactional
    public void delete(int id) {
        try {
            PsMatrix matrix = this.getById(id);
            this.delete(matrix);
        } catch (PsMatrixNotFoundException ex) {
            String message = "cannot delete matrix id=" + id + " , matrix not found";
            Logger.getLogger(PsMatrixDaoHibernateImpl.class.getName()).log(Level.WARNING, null, message);
            Logger.getLogger(PsMatrixDaoHibernateImpl.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    @Override
    @Transactional
    public void delete(PsMatrix matrix) {
        Session session = this.sessionStore.getSession();
        session.delete(matrix);
    }
}
