/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.dao.sessionimpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author tomw
 */
public class SessionStore {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
    
    @Autowired
    private HttpServletRequest httpServletRequest;

    public Session getSession() {
        Session session = (Session) httpServletRequest.getAttribute("hibernateSession");
        if (session == null) {
            SessionFactory sessionFactory = (SessionFactory) httpServletRequest.getSession().getAttribute("sessionFactory");
            if (sessionFactory == null) {
                sessionFactory = this.hibernateTemplate.getSessionFactory();
                HttpSession httpSession = httpServletRequest.getSession();
                httpSession.setAttribute("sessionFactory", sessionFactory);
            }
            session=sessionFactory.openSession();
            httpServletRequest.setAttribute("hibernateSession",session);
        }
        return session;
    }
    
    private void unsetSession(){
        httpServletRequest.setAttribute("hibernateSession",null);
    }
    
    public void closeSession(){
        Session session=getSession();
        session.close();
        unsetSession();
    }
    
    public void start(){
        this.getSession().beginTransaction();
    }
    
    public void commit(){
        this.getSession().getTransaction().commit();
        this.closeSession();
    }
    
    public void rollback(){
        this.getSession().getTransaction().rollback();
        this.closeSession();
    }
}
