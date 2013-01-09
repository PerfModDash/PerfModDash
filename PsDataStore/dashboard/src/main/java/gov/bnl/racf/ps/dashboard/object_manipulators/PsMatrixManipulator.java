/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import org.json.simple.JSONArray;

/**
 * Class for performing operations on matrix objects
 * @author tomw
 */
public class PsMatrixManipulator {

    /**
     * add host with hostId to matrix with matrixId
     * @param matrixId
     * @param hostId 
     */
    public static void addHostToMatrix(String matrixId, String hostId){
        PsDataStore dataStore = PsDataStore.getDataStore();
        
        PsMatrix matrix = dataStore.getMatrix(matrixId);
        if(matrix==null){
            throw new Error("Matrix with id="+matrixId+" does not exist!");
        }
        
        PsHost host = dataStore.getHost(hostId);
        if(host==null){
            throw new Error("Host with id="+hostId+" does not exist!");
        }
        
        addHostToMatrix(matrix,host);
        
    }
    
    /**
     * add host to matrix
     *
     * @param matrix
     * @param host
     */
    public static void addHostToMatrix(PsMatrix matrix, PsHost host) {
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
                    PsHost rowHost=matrix.getRows()[rowIndex];
                    //create  services
                    PsService service1 = 
                            PsServiceFactory.createService(
                            matrix.getType(),
                            rowHost,host,rowHost);
                    PsService service2 = 
                            PsServiceFactory.createService(
                            matrix.getType(),
                            rowHost,host,host);
                    // insert those services into matrix
                    matrix.setMatrixElement(rowIndex, columnIndex, 0, service1);
                    matrix.setMatrixElement(rowIndex, columnIndex, 1, service2);
                }

            }
        }
        if (matrix.isThroughput() || matrix.isLatency()) {
            //let us add row
            int rowIndex = matrix.getRowNumberOfHost(host);
            for(int columnIndex=0;
                    columnIndex<matrix.getNumberOfColumns();
                    columnIndex=columnIndex+1){
                // TODO for latency services we should create the diagonal
                // services as well, to be done later.
                if (columnIndex != rowIndex) {
                    PsHost columnHost=matrix.getColumns()[columnIndex];
                    //create  services
                    PsService service1 = 
                            PsServiceFactory.createService(
                            matrix.getType(),
                            host,columnHost,host);
                    PsService service2 = 
                            PsServiceFactory.createService(
                            matrix.getType(),
                            host,columnHost,columnHost);
                    // insert those services into matrix
                    matrix.setMatrixElement(rowIndex, columnIndex, 0, service1);
                    matrix.setMatrixElement(rowIndex, columnIndex, 1, service2);
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
    
    /**
     * remove host hostId from matrix matrixId. Delete all relevant matrix services
     * @param matrixId
     * @param hostId 
     */
    public static void removeHostFromMatrix(String matrixId, String hostId){
        PsDataStore dataStore = PsDataStore.getDataStore();
        
        PsMatrix matrix = dataStore.getMatrix(matrixId);
        if(matrix==null){
            throw new Error("Matrix with id="+matrixId+" does not exist!");
        }
        
        PsHost host = dataStore.getHost(hostId);
        if(host==null){
            throw new Error("Host with id="+hostId+" does not exist!");
        }
        
        removeHostFromMatrix(matrix,host);
    }
    /**
     * add hostId to matrix
     * @param matrix
     * @param hostId 
     */
    public static void addHostToMatrix(PsMatrix matrix, String hostId){
        PsDataStore dataStore = PsDataStore.getDataStore();
        PsHost host = dataStore.getHost(hostId);
        addHostToMatrix(matrix,host);
    }
    /**
     * remove hostId from matrix
     * @param matrix
     * @param hostId 
     */
    public static void removeHostFromMatrix(PsMatrix matrix, String hostId){
        PsDataStore dataStore = PsDataStore.getDataStore();
        PsHost host = dataStore.getHost(hostId);
        removeHostFromMatrix(matrix,host);
    }
    
    /**
     * remove host from matrix. Delete all relevant matrix services.
     * @param matrix
     * @param host 
     */
    public static void removeHostFromMatrix(PsMatrix matrix, PsHost host){
        
        PsDataStore dataStore = PsDataStore.getDataStore();
        
        //first order of business is to find out which column belongs to host
        int columnNumber = matrix.getColumnNumberOfHost(host);
        if(columnNumber>-1){
        Vector<String> serviceIdsToBeDeleted = 
                matrix.getServiceIdsInColumn(columnNumber);
        matrix.removeColumn(columnNumber);
        dataStore.removeServices(serviceIdsToBeDeleted);
        }
        
        //second order of business is to remove row
        System.out.println(new Date()+" removeHostFromMatrix remove host row "+host.getHostname());
        int rowNumber = matrix.getRowNumberOfHost(host);
        System.out.println(new Date()+" removeHostFromMatrix rowNumber="+rowNumber);
        if(rowNumber>-1){
            Vector<String> serviceIdsToBeDeleted = 
                matrix.getServiceIdsInRow(rowNumber);
            matrix.removeRow(rowNumber);
            dataStore.removeServices(serviceIdsToBeDeleted);
        }
        // we are done                        
    }
    /**
     * add list of host Ids to matrix
     * @param matrix
     * @param listOfHostIds 
     */
    public static void addHostIdsToMatrix(PsMatrix matrix, JSONArray listOfHostIds){
        Iterator iter = listOfHostIds.iterator();
        while(iter.hasNext()){
            String hostId = (String)iter.next();
            addHostToMatrix(matrix,hostId);
        }
    }
    
    /**
     * add list of host id's to matrixId
     * @param matrixId
     * @param listOfHostIds 
     */
    public static void addHostIdsToMatrix(String matrixId, JSONArray listOfHostIds){
        PsDataStore dataStore = PsDataStore.getDataStore();
        
        PsMatrix matrix = dataStore.getMatrix(matrixId);
        addHostIdsToMatrix(matrix,listOfHostIds);
    }
    /**
     * remove list of host Ids from matrix
     * @param matrix
     * @param listOfHostIds 
     */
    public static void removeHostIdsFromMatrix(PsMatrix matrix, JSONArray listOfHostIds){
        Iterator iter = listOfHostIds.iterator();
        while(iter.hasNext()){
            String hostId = (String)iter.next();
            removeHostFromMatrix(matrix,hostId);
        }
    }
    /**
     * remove list of host id's from matrixId
     * @param matrixId
     * @param listOfHostIds 
     */
    public static void removeHostIdsFromMatrix(String matrixId, JSONArray listOfHostIds){
        PsDataStore dataStore = PsDataStore.getDataStore();
        PsMatrix matrix = dataStore.getMatrix(matrixId);
        removeHostIdsFromMatrix(matrix,listOfHostIds);
    }
    
}
