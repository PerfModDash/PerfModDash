/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.session_factory_store;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author tomw
 */
public class PsSessionFactoryStore {
    
    private static PsSessionFactoryStore psSessionFactoryStore=null;
    
    private SessionFactory sessionFactory = null;
 
    private PsSessionFactoryStore(){
        
    }
    /**
     * access to data is given by PsSessionFactory method
     * @return 
     */
    public static PsSessionFactoryStore getSessionFactoryStore(){
        if(psSessionFactoryStore==null){
            psSessionFactoryStore=new PsSessionFactoryStore();
        }
        return psSessionFactoryStore;
    }
    
    /**
     * get the sessionFactory, make sure that only one exists
     * @return 
     */
    public SessionFactory getSessionFactory(){
        if(sessionFactory==null){
            sessionFactory =  new AnnotationConfiguration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }
    
}
