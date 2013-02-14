/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.*;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;

/**
 * utility class for deleting objects
 *
 * @author tomw
 */
public class PsObjectShredder {

    /**
     * delete host object
     *
     * @param session
     * @param host
     */
    public static void delete(Session session, PsHost host) {
        //first order of business is to remove this host from any
        // sites it might belong to
        List listOfAllSites = PsDataStore.getAllSites(session);
        Iterator iter = listOfAllSites.iterator();
        while (iter.hasNext()) {
            PsSite currentSite = (PsSite) iter.next();
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

        // first order of business is to remove all hosts from this matrix
        List<PsHost> allHostsInThisMatrix = matrix.getAllHosts();
        Iterator iter = allHostsInThisMatrix.iterator();
        while (iter.hasNext()) {
            PsHost hostToBeRemoved = (PsHost) iter.next();
            PsMatrixManipulator.removeHostFromMatrix(session, matrix, hostToBeRemoved);
        }
        // second order of business is to remove the matrix itself
        session.delete(matrix);
    }

    public static void delete(Session session, PsCloud cloud) {
        cloud.removeAllSites();
        cloud.removeAllMatrices();
        session.delete(cloud);
    }

    public static void delete(Session session, PsJob job) {
        if (job != null) {
            session.delete(job);
        }
    }

    public static void deletePsJob(Session session, int jobId) {
        //TODO replace this with HQL DELETE command
        PsJob job = (PsJob) PsDataStore.getJob(session, jobId);
        if (job != null) {
            delete(session, job);
        }
    }
}
