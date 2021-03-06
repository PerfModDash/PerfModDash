/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.data_store;

import gov.bnl.racf.ps.dashboard.db.data_objects.*;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsObjectShredder;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import java.util.*;
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
     * get host with given host name. return null if not found
     *
     * @param session
     * @param hostName
     * @return
     */
    public static PsHost getHostByName(Session session, String hostName) {
        Query query = session.createQuery("from PsHost where hostname=:parameter ");
        query.setParameter("parameter", hostName);
        List resultList = query.list();

        if (resultList.isEmpty()) {
            return null;
        } else {
            return (PsHost) resultList.get(0);
        }
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
     *
     * @param session
     * @param serviceTypeId
     * @return
     */
    public static PsServiceType getServiceType(Session session, String serviceTypeId) {
        Query query = session.createQuery("from PsServiceType where serviceTypeId = :code ");
        query.setParameter("code", serviceTypeId);
        List list = query.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return (PsServiceType) list.get(0);
        }
    }

    /**
     * get list of known service types
     *
     * @param session
     * @return
     */
    public static List<PsServiceType> listOfServiceTypes(Session session) {
        Query query = session.createQuery("from PsServiceType");
        List list = query.list();
        return list;
    }

    /**
     * get service identified by a given service id
     *
     * @param session
     * @param serviceId
     * @return
     */
    public static PsService getService(Session session, int serviceId) {
        PsService service = (PsService) session.get(PsService.class, serviceId);
        return service;
    }

    public static List<PsService> getAllServices(Session session) {
        Query query2 = session.createQuery("from PsService");
        query2.setCacheable(true);
        List resultList = query2.list();

        return resultList;
    }

    /**
     * get site object with a given site id
     *
     * @param session
     * @param siteId
     * @return
     */
    public static PsSite getSite(Session session, int siteId) {
        //throw new UnsupportedOperationException("Not yet implemented");
        PsSite site = (PsSite) session.get(PsSite.class, siteId);
        return site;
    }

    public static PsSite getSiteByName(Session session, String name) {
        Query query = session.createQuery("from PsSite where name=:parameter ");
        query.setParameter("parameter", name);
        List resultList = query.list();

        if (resultList.isEmpty()) {
            return null;
        } else {
            return (PsSite) resultList.get(0);
        }
    }

    /**
     * get list of all sites
     *
     * @param session
     * @return
     */
    public static List<PsSite> getAllSites(Session session) {
        //throw new UnsupportedOperationException("Not yet implemented");
        Query query = session.createQuery("from PsSite");
        query.setCacheable(true);
        List resultList = query.list();

        return resultList;
    }

    /**
     * get matrix of a given id
     *
     * @param session
     * @param matrixId
     * @return
     */
    public static PsMatrix getMatrix(Session session, int matrixId) {
        PsMatrix matrix = (PsMatrix) session.get(PsMatrix.class, matrixId);
        return matrix;
    }

    /**
     * return matrix of given name and type return null if not found
     *
     * @param session
     * @param name
     * @param type
     * @return
     */
    public static PsMatrix getMatrixByNameAndType(Session session, String name, PsServiceType type) {
        Query query = session.createQuery("from PsMatrix where name=:namepar and matrixType=:typepar");
        query.setParameter("namepar", name);
        query.setParameter("typepar", type);
        query.setCacheable(true);
        List resultList = query.list();

        if (resultList.size() > 0) {
            return (PsMatrix) resultList.get(0);
        } else {
            return null;
        }
    }

    /**
     * search matrices which particular name
     *
     * @param session
     * @param name
     * @return
     */
    public static List<PsMatrix> getMatrixByName(Session session, String name) {
        Query query = session.createQuery("from PsMatrix where name like :namepar");
        query.setParameter("namepar", name);
        query.setCacheable(true);
        List resultList = query.list();

        return resultList;
    }

    public static boolean containsMatrixOfNameAndType(Session session, String name, PsServiceType type) {
        PsMatrix matrix = getMatrixByNameAndType(session, name, type);
        if (matrix == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * return a list of all matrices
     *
     * @param session
     * @return
     */
    public static List<PsMatrix> getAllMatrices(Session session) {
        //throw new UnsupportedOperationException("Not yet implemented");
        Query query = session.createQuery("from PsMatrix");
        query.setCacheable(true);
        List resultList = query.list();

        return resultList;
    }

    public static void removeServices(Session session, Vector<Integer> serviceIdsToBeDeleted) {
        Iterator iter = serviceIdsToBeDeleted.iterator();
        while (iter.hasNext()) {
            Integer serviceIdInteger = (Integer) iter.next();
            int serviceId = serviceIdInteger.intValue();
            //TODO this may be replaced by a delete query in HQL
            PsService service = PsDataStore.getService(session, serviceId);
            PsObjectShredder.delete(session, service);
        }
    }

    public static void removeServices(Session session, List<PsService> servicesToBeDeleted) {
        Iterator iter = servicesToBeDeleted.iterator();
        while (iter.hasNext()) {
            PsService service = (PsService) iter.next();
            PsObjectShredder.delete(session, service);
        }
    }

    public static PsCloud getCloud(Session session, int cloudId) {
        //throw new UnsupportedOperationException("Not yet implemented");
        PsCloud cloud = (PsCloud) session.get(PsCloud.class, cloudId);
        return cloud;
    }

    public static List<PsCloud> getAllClouds(Session session) {
        //throw new UnsupportedOperationException("Not yet implemented");
        Query query = session.createQuery("from PsCloud");
        query.setCacheable(true);
        List resultList = query.list();

        return resultList;
    }

    public static PsJob getJob(Session session, int jobId) {
        //throw new UnsupportedOperationException("Not yet implemented");
        PsJob job = (PsJob) session.get(PsJob.class, jobId);
        return job;
    }

    public static PsRecentServiceResult getRecentResultForService(Session session, int serviceId) {
        Query query = session.createQuery("from PsRecentServiceResult where service_id=:parameter");
        query.setParameter("parameter", serviceId);
        query.setCacheable(true);
        List resultList = query.list();
        PsRecentServiceResult recentResult = null;
        Iterator iter = resultList.iterator();
        while (iter.hasNext()) {
            recentResult = (PsRecentServiceResult) iter.next();
        }
        return recentResult;
    }

    public static Date getResultsTimeMin(Session session) {
        Query query = session.createQuery("select min(time) from PsServiceResult");
        Date result = null;
        Iterator iter = query.list().iterator();
        while (iter.hasNext()) {
            result = (Date) iter.next();
        }
        return result;
    }

    public static int getResultsCount(Session session) {
        int result = ((Long) session.createQuery("select count(*) from PsServiceResult").iterate().next()).intValue();
        return result;
    }

    public static int getResultsCount(Session session, Date tmin, Date tmax) {
        Query query = session.createQuery("select count(*) from PsServiceResult where time between :time_start and :time_end");
        query.setParameter("time_start", tmin);
        query.setParameter("time_end", tmax);
        int result = ((Long) query.iterate().next()).intValue();
        return result;
    }

    public static PsServiceResult getResult(Session session, PsService service, Date tAround) {
        PsServiceResult resultBefore = getResultBefore(session, service, tAround);
        PsServiceResult resultAfter = getResultAfter(session, service, tAround);
        
        if(resultBefore==null){
            return resultAfter;
        }
        
        if(resultAfter==null){
            return resultBefore;
        }

        Date tBefore = resultBefore.getTime();
        Date tAfter = resultAfter.getTime();

        long tMilisBefore = tBefore.getTime();
        long tMilisAfter = tAfter.getTime();
        long tMilisAround = tAround.getTime();

        if (tMilisAfter - tMilisAround < tMilisAround - tMilisBefore) {
            return resultAfter;
        } else {
            return resultBefore;
        }
    }

    private static PsServiceResult getResultBefore(Session session, PsService service, Date tAround) {
        List<PsServiceResult> resultsBefore = getResultsBefore(session, service, tAround);
        if (resultsBefore.isEmpty()) {
            return null;
        } else {
            Collections.sort(resultsBefore);
            return resultsBefore.get(0);
        }
    }

    private static PsServiceResult getResultAfter(Session session, PsService service, Date tAround) {
        List<PsServiceResult> resultsAfter = getResultsAfter(session, service, tAround);
        if (resultsAfter.isEmpty()) {
            return null;
        } else {
            Collections.sort(resultsAfter);
            return resultsAfter.get(resultsAfter.size() - 1);
        }
    }

    public static List<PsServiceResult> getResultsBefore(Session session, PsService service, Date timeEnd) {
        long secondAfterBeginningOfWorld = 1;
        Date timeStart = new Date(secondAfterBeginningOfWorld);
        return getResults(session, service, timeStart, timeEnd);
    }

    public static List<PsServiceResult> getResultsAfter(Session session, PsService service, Date timeStart) {
        long secondAfterBeginningOfWorld = 1;
        Date timeEnd = new Date();
        return getResults(session, service, timeStart, timeEnd);
    }

    public static List<PsServiceResult> getResults(Session session, PsService service) {
        Query query = session.createQuery("from PsServiceResult where service_id=:parameter");
        int serviceId = service.getId();
        query.setParameter("parameter", serviceId);
        List resultList = query.list();

        Collections.sort(resultList);

        return resultList;
    }

    public static List<PsServiceResult> getResults(Session session, PsService service, Date timeStart, Date timeEnd) {
        //Query query = session.createQuery("from PsServiceResult where service_id=:parameter and time between :time_start and :time_end");
        Query query = session.createQuery("from PsServiceResult where service_id=:parameter and time>=:time_start and time<=:time_end");
        int serviceId = service.getId();
        query.setParameter("parameter", serviceId);
        query.setParameter("time_start", timeStart);
        query.setParameter("time_end", timeEnd);
        List resultList = query.list();

        Collections.sort(resultList);

        return resultList;
    }

    /**
     * get cloud by a given name
     *
     * @param session
     * @param cloudName
     * @return
     */
    public static PsCloud getCloudByName(Session session, String cloudName) {
        Query query = session.createQuery("from PsCloud where name=:parameter ");
        query.setParameter("parameter", cloudName);
        List resultList = query.list();

        if (resultList.isEmpty()) {
            return null;
        } else {
            return (PsCloud) resultList.get(0);
        }
    }
}
