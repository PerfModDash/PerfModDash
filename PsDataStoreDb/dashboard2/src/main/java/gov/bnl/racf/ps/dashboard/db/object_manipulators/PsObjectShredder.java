/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;

/**
 * utility class for deleting objects
 * @author tomw
 */
public class PsObjectShredder {
    /**
     * delete host object
     * @param session
     * @param host 
     */
    public static void delete(Session session, PsHost host){
        //first order of business is to remove this host from any
        // sites it might belong to
        List listOfAllSites = PsDataStore.getAllSites(session);
        Iterator iter = listOfAllSites.iterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            currentSite.removeHost(host);
        }
        
        
        // finally delete the host itself
        session.delete(host);
        //TODO add deleting services associated with this host
    }

    public static void delete(Session session, PsService service) {
        //throw new UnsupportedOperationException("Not yet implemented");
        session.delete(service);
    }
    public static void delete(Session session, PsSite site) {
        //throw new UnsupportedOperationException("Not yet implemented");
        //first order of business is to remove all hosts from the site
        site.removeAllHosts();
        // ok, now we can remove the site
        session.delete(site);
    }
    
    public static void delete(Session session, PsMatrix matrix) {
        //throw new UnsupportedOperationException("Not yet implemented");
        session.delete(matrix);
    }
}
