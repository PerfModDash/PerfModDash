/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.data_store;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * interface to the data store
 *
 * @author tomw
 */
public class PsDataStore {

    /**
     * get host with the specified id
     *
     * @param hostId
     * @return
     */
    public static PsHost getHost(Session session, int hostId) {

        PsHost host = (PsHost) session.get(PsHost.class, hostId);

        return host;
    }

    /**
     * get a vector of all known hosts
     *
     * @return
     */
    public static List<PsHost> getAllHosts(Session session) {



        Query query2 = session.createQuery("from PsHost");
        query2.setCacheable(true);
        List resultList = query2.list();

        return resultList;
    }
    /**
     * get service type object of a given service type
     * @param session
     * @param serviceTypeId
     * @return 
     */
    public static PsServiceType getServiceType(Session session, String serviceTypeId) {
        Query query = session.createQuery("from PsServiceType where serviceTypeId = :code ");
        query.setParameter("code", serviceTypeId);
        List list = query.list();
        if(list.isEmpty()){
            return null;
        }else{
            return (PsServiceType)list.get(0);
        }
    }
    
    /**
     * get list of known service types
     * @param session
     * @return 
     */
    public static List<PsServiceType> listOfServiceTypes(Session session){
        Query query = session.createQuery("from PsServiceType");
        List list = query.list();
        return list;
    }
}
