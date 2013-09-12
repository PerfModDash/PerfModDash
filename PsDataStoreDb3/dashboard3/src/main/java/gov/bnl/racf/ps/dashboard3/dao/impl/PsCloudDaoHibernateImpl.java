/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.impl;

import gov.bnl.racf.ps.dashboard3.dao.PsCloudDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsCloudNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * //TODO finish this class
 *
 * @author tomw
 */
public class PsCloudDaoHibernateImpl implements PsCloudDao {

    // === dependency injection ===//
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    //=== main methods ===//
    @Override
    public PsCloud create() {
        PsCloud newCloud = new PsCloud();
        this.insert(newCloud);
        return newCloud;
    }

    @Override
    public void insert(PsCloud cloud) {
        this.hibernateTemplate.save(cloud);
    }

    @Override
    public PsCloud getById(int id) throws PsCloudNotFoundException {
        PsCloud cloud = this.hibernateTemplate.get(PsCloud.class, id);
        if (cloud == null) {
            throw new PsCloudNotFoundException(this.getClass().getName() + " cloud not found id=" + id);
        } else {
            return cloud;
        }
    }

    @Override
    public List<PsCloud> getByName(String cloudName) {
        String query = "from PsCloud where name=?";
        List<PsCloud> listOfResults = this.hibernateTemplate.find(query, cloudName);
        return listOfResults;
    }

    @Override
    public List<PsCloud> getAll() {
        String query = "from PsCloud";
        List<PsCloud> listOfResults = this.hibernateTemplate.find(query);
        return listOfResults;
    }

    @Override
    @Transactional
    public void update(PsCloud cloud) {
        this.hibernateTemplate.merge(cloud);

        Iterator<PsMatrix> matricesIterator = cloud.matrixIterator();
        while (matricesIterator.hasNext()) {
            PsMatrix currentMatrix = (PsMatrix) matricesIterator.next();
            Iterator hostsIterator = currentMatrix.getAllHosts().iterator();
            while (hostsIterator.hasNext()) {
                PsHost currentHost = (PsHost) hostsIterator.next();
                currentHost = this.hibernateTemplate.get(PsHost.class, currentHost.getId());
                System.out.println("saving matrix host "+currentHost.toString());
                this.hibernateTemplate.merge(currentHost);
                this.hibernateTemplate.update(currentHost);
                System.out.println("saved matrix host "+currentHost.toString());
            }
        }
        
        Iterator sitesIterator = cloud.sitesIterator();
        while(sitesIterator.hasNext()){
            PsSite currentSite = (PsSite)sitesIterator.next();
            Iterator hostsOnSiteIterator = currentSite.getHosts().iterator();
            while(hostsOnSiteIterator.hasNext()){
                PsHost currentHost = (PsHost)hostsOnSiteIterator.next();
                currentHost = this.hibernateTemplate.get(PsHost.class, currentHost.getId());
                System.out.println("saving site host "+currentHost.toString());
                this.hibernateTemplate.merge(currentHost);
                this.hibernateTemplate.update(currentHost);
                System.out.println("saved site host "+currentHost.toString());
            }
            
        }
        this.hibernateTemplate.update(cloud);
    }

    @Override
    public void delete(int id) {
        try {
            PsCloud cloud = this.getById(id);
            this.delete(cloud);
        } catch (PsCloudNotFoundException ex) {
            String message = this.getClass().getName() + " failed to delete cloud, cloud not found, id=" + id;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(PsCloud cloud) {
        this.hibernateTemplate.delete(cloud);
    }
}
