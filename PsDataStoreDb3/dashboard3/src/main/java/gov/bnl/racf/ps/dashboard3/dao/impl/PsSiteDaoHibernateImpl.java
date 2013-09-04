/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsSiteDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author tomw
 */
public class PsSiteDaoHibernateImpl implements PsSiteDao{

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
    
    private HibernateTemplate hibernateTemplate;
    

    @Override
    public PsSite create() {
        PsSite site = new PsSite();
        this.insert(site);
        return site;
    }

    @Override
    public void insert(PsSite site) {
        this.hibernateTemplate.save(site);
    }

    @Override
    public PsSite getById(int id) throws PsSiteNotFoundException {
        PsSite site = (PsSite)this.hibernateTemplate.get(PsSite.class, id);
        if(site==null){
            throw new PsSiteNotFoundException(this.getClass().getName()+" site not dound id="+id);
        }else{
            return site;
        }
    }

    @Override
    public List<PsSite> getAll() {
        String query="from PsSite";
        return this.hibernateTemplate.find(query);
    }

    @Override
    public void update(PsSite site) {
        this.hibernateTemplate.update(site);
    }

    @Override
    public void delete(int id) {
        try {
            PsSite site = this.getById(id);
            this.hibernateTemplate.delete(site);
        } catch (PsSiteNotFoundException ex) {
            String message="site with id="+id+" not found";
            Logger.getLogger(PsSiteDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsSiteDaoHibernateImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(PsSite site) {
        this.hibernateTemplate.delete(site);
    }
    
}
