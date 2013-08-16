/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsHostDao;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.objects.PsHost;
import gov.bnl.racf.ps.dashboard3.operators.PsHostOperator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author tomw
 */
public class PsHostDaoHibernateImpl implements PsHostDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public PsHost create() {
        PsHost host = new PsHost();
        this.create(host);
        return host;
    }

    @Override
    public void create(PsHost host) {
        this.hibernateTemplate.save(host);
    }

    @Override
    public PsHost getById(int id) throws PsObjectNotFoundException {
        String query = "from PsHost where id=?";
        List<PsHost> listOfHosts= this.hibernateTemplate.find(query, new Object[]{id});
        if(listOfHosts.isEmpty()){
            throw new PsObjectNotFoundException();
        }else{
            if(listOfHosts.size()>1){
                 Logger.getLogger(getClass().getName()).log(Level.SEVERE, " more than one host with id found, id=" + id);
            } 
            return listOfHosts.get(0);
        }
    }

    @Override
    public List<PsHost> getAll() {
        String query = "from PsHost";
        List<PsHost>listOfHosts = this.hibernateTemplate.find(query);
        return listOfHosts;
    }

    @Override
    public void update(PsHost host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(PsHost host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
