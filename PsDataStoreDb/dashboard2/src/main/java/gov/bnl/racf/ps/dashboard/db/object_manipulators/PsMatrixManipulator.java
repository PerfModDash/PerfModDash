/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.hibernate.Session;
import org.json.simple.JSONArray;

/**
 * Class for performing operations on matrix objects
 *
 * @author tomw
 */
public class PsMatrixManipulator {

    /**
     * add host with hostId to matrix with matrixId
     *
     * @param session
     * @param matrixId
     * @param hostId
     */
    public static void addHostToMatrix(Session session, int matrixId, int hostId) {


        PsMatrix matrix = PsDataStore.getMatrix(session, matrixId);
        if (matrix == null) {
            throw new Error("Matrix with id=" + matrixId + " does not exist!");
        }

        PsHost host = PsDataStore.getHost(session, hostId);
        if (host == null) {
            throw new Error("Host with id=" + hostId + " does not exist!");
        }

        addHostToMatrix(session, matrix, host);

    }

    /**
     * add host to matrix
     *
     * @param session
     * @param matrix
     * @param host
     */
    public static void addHostToMatrix(Session session, PsMatrix matrix, PsHost host) {
        if (!matrix.containsHost(host)) {
            // first of all add host to columns and rows
            matrix.addHost(host);

            // ok, host has been added to matrix
            // now fill the relevant services
            if (matrix.isThroughput() || matrix.isLatency()) {
                // let us fill column first
                int columnIndex = matrix.getColumnNumberOfHost(host);
                for (int rowIndex = 0;
                        rowIndex < matrix.getNumberOfRows(); rowIndex = rowIndex + 1) {
                    // TODO for latency services we should create the diagonal
                    // services as well, to be done later.
                    if (columnIndex != rowIndex) {

                        PsHost rowHost = matrix.getHostInRow(rowIndex);
                        //create  services
                        PsService service1 =
                                PsServiceFactory.createService(
                                session,
                                matrix.getType(),
                                rowHost, host, rowHost);
                        PsService service2 =
                                PsServiceFactory.createService(
                                session,
                                matrix.getType(),
                                rowHost, host, host);
                        // insert those services into matrix
                        matrix.addService(columnIndex, rowIndex, 0, service1);
                        matrix.addService(columnIndex, rowIndex, 1, service2);
                    }

                }
            }
            if (matrix.isThroughput() || matrix.isLatency()) {
                //let us add row
                int rowIndex = matrix.getRowNumberOfHost(host);
                for (int columnIndex = 0;
                        columnIndex < matrix.getNumberOfColumns();
                        columnIndex = columnIndex + 1) {
                    // TODO for latency services we should create the diagonal
                    // services as well, to be done later.
                    if (columnIndex != rowIndex) {
                        PsHost columnHost = matrix.getHostInColumn(columnIndex);
                        //create  services
                        PsService service1 =
                                PsServiceFactory.createService(
                                session,
                                matrix.getType(),
                                host, columnHost, host);
                        PsService service2 =
                                PsServiceFactory.createService(
                                session,
                                matrix.getType(),
                                host, columnHost, columnHost);
                        // insert those services into matrix
                        matrix.addService(columnIndex, rowIndex, 0, service1);
                        matrix.addService(columnIndex, rowIndex, 1, service2);
                    }
                }

            }
//        if (matrix.isLatency()) {
//            throw new Error("Latency matrix not yet implemented");
//        }
            if (matrix.isTraceroute()) {
                throw new Error("Traceroute matrix not yet implemented");
            }
        }
    }

    /**
     * remove host hostId from matrix matrixId. Delete all relevant matrix
     * services
     *
     * @param session
     * @param matrixId
     * @param hostId
     */
    public static void removeHostFromMatrix(Session session, int matrixId, int hostId) {

        PsMatrix matrix = PsDataStore.getMatrix(session, matrixId);
        if (matrix == null) {
            throw new Error("Matrix with id=" + matrixId + " does not exist!");
        }

        PsHost host = PsDataStore.getHost(session, hostId);
        if (host == null) {
            throw new Error("Host with id=" + hostId + " does not exist!");
        }

        removeHostFromMatrix(session, matrix, host);
    }

    /**
     * add hostId to matrix
     *
     * @param session
     * @param matrix
     * @param hostId
     */
    public static void addHostToMatrix(Session session, PsMatrix matrix, int hostId) {
        PsHost host = PsDataStore.getHost(session, hostId);
        addHostToMatrix(session, matrix, host);
    }

    /**
     * remove hostId from matrix
     *
     * @param session
     * @param matrix
     * @param hostId
     */
    public static void removeHostFromMatrix(Session session,
            PsMatrix matrix, int hostId) {
        PsHost host = PsDataStore.getHost(session, hostId);
        removeHostFromMatrix(session, matrix, host);
    }

    /**
     * remove host from matrix. Delete all relevant matrix services.
     *
     * @param session
     * @param matrix
     * @param host
     */
    public static void removeHostFromMatrix(
            Session session, PsMatrix matrix, PsHost host) {

        if (matrix.containsHost(host)) {
            List<PsService> servicesToBeDeleted = matrix.removeHost(host);
            PsDataStore.removeServices(session, servicesToBeDeleted);
            servicesToBeDeleted.clear();
        }
        // we are done                        
    }

    /**
     * add list of host Ids to matrix
     *
     * @param session
     * @param matrix
     * @param listOfHostIds
     */
    public static void addHostIdsToMatrix(Session session, PsMatrix matrix, JSONArray listOfHostIds) {
        Iterator iter = listOfHostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            addHostToMatrix(session, matrix, hostId);
        }
    }

    /**
     * add list of host id's to matrixId
     *
     * @param session
     * @param matrixId
     * @param listOfHostIds
     */
    public static void addHostIdsToMatrix(Session session, int matrixId, JSONArray listOfHostIds) {

        PsMatrix matrix = PsDataStore.getMatrix(session, matrixId);
        addHostIdsToMatrix(session, matrix, listOfHostIds);
    }

    /**
     * remove list of host Ids from matrix
     *
     * @param session
     * @param matrix
     * @param listOfHostIds
     */
    public static void removeHostIdsFromMatrix(Session session, PsMatrix matrix, JSONArray listOfHostIds) {
        Iterator iter = listOfHostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            removeHostFromMatrix(session, matrix, hostId);
        }
    }

    /**
     * remove list of host id's from matrixId
     *
     * @param session
     * @param matrixId
     * @param listOfHostIds
     */
    public static void removeHostIdsFromMatrix(Session session, int matrixId, JSONArray listOfHostIds) {
        PsMatrix matrix = PsDataStore.getMatrix(session, matrixId);
        removeHostIdsFromMatrix(session, matrix, listOfHostIds);
    }
}
