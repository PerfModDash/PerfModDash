/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsMatrixDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.domainobjects.factories.PsMatrixFactory;
import gov.bnl.racf.ps.dashboard3.domainobjects.factories.PsServiceFactory;
import gov.bnl.racf.ps.dashboard3.exceptions.*;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsMatrixJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Placeholder for PsMatrix operator class
 *
 * @author tomw
 */
public class PsMatrixOperator {

    // === dependency injection part ===//
    private PsMatrixDao psMatrixDao;
   

    public void setPsMatrixDao(PsMatrixDao psMatrixDao) {
        this.psMatrixDao = psMatrixDao;
    }
    private PsMatrixJson psMatrixJson;

    public void setPsMatrixJson(PsMatrixJson psMatrixJson) {
        this.psMatrixJson = psMatrixJson;
    }
    private PsServiceTypeOperator psServiceTypeOperator;

    public void setPsServiceTypeOperator(PsServiceTypeOperator psServiceTypeOperator) {
        this.psServiceTypeOperator = psServiceTypeOperator;
    }
    private PsMatrixFactory psMatrixFactory;

    public void setPsMatrixFactory(PsMatrixFactory psMatrixFactory) {
        this.psMatrixFactory = psMatrixFactory;
    }
    private PsServiceFactory psServiceFactory;

    public void setPsServiceFactory(PsServiceFactory psServiceFactory) {
        this.psServiceFactory = psServiceFactory;
    }
    private PsServiceOperator psServiceOperator;

    public void setPsServiceOperator(PsServiceOperator psServiceOperator) {
        this.psServiceOperator = psServiceOperator;
    }
    private PsHostOperator psHostOperator;

    public void setPsHostOperator(PsHostOperator psHostOperator) {
        this.psHostOperator = psHostOperator;
    }
    
    private PsCloudOperator psCloudOperator;

    public void setPsCloudOperator(PsCloudOperator psCloudOperator) {
        this.psCloudOperator = psCloudOperator;
    }
    

    // === simple CRUD operations part ===//
    /**
     * get matrix by id
     *
     * @param id
     * @return
     * @throws PsMatrixNotFoundException
     */
    @Transactional
    public PsMatrix getById(int id) throws PsMatrixNotFoundException {
        return this.psMatrixDao.getById(id);
    }

    /**
     * get list of all matrices
     *
     * @return
     */
    @Transactional
    public List<PsMatrix> getAll() {
        return this.psMatrixDao.getAll();
    }

    /**
     * insert new matrix
     *
     * @param matrix
     */
    @Transactional
    public void insert(PsMatrix matrix) {
        this.psMatrixDao.insert(matrix);
    }

    /**
     * update matrix
     *
     * @param matrix
     */
    @Transactional
    public void update(PsMatrix matrix) {
        this.psMatrixDao.update(matrix);
    }

    /**
     * delete matrix with given id
     *
     * @param id
     */
    @Transactional
    public void delete(int id) {
        try {
            PsMatrix matrix = this.psMatrixDao.getById(id);
            this.delete(matrix);
        } catch (PsMatrixNotFoundException ex) {
            String message=" matrix with id not found, id="+id;
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
        }
    }

    /**
     * delete matrix
     *
     * @param matrix
     */
    @Transactional
    public void delete(PsMatrix matrix) {
        //1. remove all hosts from this matrix, this will also delete
        //   the corresponding services
        this.removeAllHosts(matrix);

        //2. remove this matrix from all clouds
        this.psCloudOperator.removeMatrixFromAllClouds(matrix);

        //3. delete the matrix
        this.psMatrixDao.delete(matrix);
    }

    // === JSON conversion part ===//
    /**
     * convert matrix to JSON using prescribe detail level
     *
     * @param matrix
     * @param detailLevel
     * @return
     */
    @Transactional
    public JSONObject toJson(PsMatrix matrix, String detailLevel) {
        return this.psMatrixJson.toJson(matrix, detailLevel);
    }

    /**
     * convert matrix to JSON using default detail level
     *
     * @param matrix
     * @return
     */
    @Transactional
    public JSONObject toJson(PsMatrix matrix) {
        return this.psMatrixJson.toJson(matrix);
    }

    /**
     * convert list of matrices to JSONArray using default detail level
     *
     * @param listOfMatrices
     * @return
     */
    @Transactional
    public JSONArray toJson(List<PsMatrix> listOfMatrices) {
        return this.psMatrixJson.toJson(listOfMatrices);
    }

    /**
     * convert list of matrices to JSONArray using prescribed detail level
     *
     * @param listOfMatrices
     * @param detailLevel
     * @return
     */
    @Transactional
    public JSONArray toJson(List<PsMatrix> listOfMatrices, String detailLevel) {
        return this.psMatrixJson.toJson(listOfMatrices, detailLevel);
    }

    // === matrix operations ===//
    /**
     * based on JSONObject jsonInput create matrix with requested name and type
     * and insert it to database
     *
     * @param jsonInput
     * @return
     * @throws PsMissingMatrixNameException
     * @throws PsMissingServiceTypeException
     * @throws PsServiceTypeNotFoundException
     */
    @Transactional
    public PsMatrix insertNewMatrixFromJson(JSONObject jsonInput)
            throws PsMissingMatrixNameException, PsMissingServiceTypeException,
            PsServiceTypeNotFoundException {

        //1. get the matrix name and type

        String matrixName;
        if (jsonInput.containsKey(PsMatrix.NAME)) {
            matrixName = (String) jsonInput.get(PsMatrix.NAME);
        } else {
            throw new PsMissingMatrixNameException(this.getClass().getName()+
                    " json input is missing matrix name:"+jsonInput.toString());
        }
        String serviceTypeId;
        if (jsonInput.containsKey(PsMatrix.SERVICE_TYPE_ID)) {
            serviceTypeId = (String) jsonInput.get(PsMatrix.SERVICE_TYPE_ID);
        } else {
            throw new PsMissingServiceTypeException(this.getClass().getName()+
                    " json input is missing service type id:"+jsonInput.toString());
        }

        //2. get the requested service type
        PsServiceType type = this.psServiceTypeOperator.getServiceType(serviceTypeId);

        //3. create the matrix

        PsMatrix matrix = this.psMatrixFactory.createNewMatrix(type, matrixName);

        //4. set the matrix name and type
        matrix.setName(matrixName);
        matrix.setMatrixType(type);

        //5. insert the matrix into database
        this.psMatrixDao.insert(matrix);

        //6. return the result
        return matrix;
    }

    /**
     * based on JSONObject jsonInput update info about an existing matrix
     *
     * @param id
     * @param jsonInput
     * @return
     * @throws PsMatrixNotFoundException
     */
    @Transactional
    public PsMatrix updateMarixFromJson(int id, JSONObject jsonInput) throws PsMatrixNotFoundException {
        //1. get matrix from database
        PsMatrix matrix = this.getById(id);

        //2. update the matrix. For the time being we update only matrix name

        if (jsonInput.keySet().contains(PsMatrix.NAME)) {
            matrix.setName((String) jsonInput.get(PsMatrix.NAME));
        }

        //3. save the matrix
        this.update(matrix);

        //4. return updated matrix
        return matrix;
    }

    /**
     * execute matrix command for matrix with given id
     *
     * @param id
     * @param command
     * @param requestBody
     * @return
     * @throws PsMatrixNotFoundException
     * @throws PsHostNotFoundException
     * @throws PsUnknownCommandException
     * @throws ParseException
     */
    @Transactional
    public PsMatrix executeCommand(int id, String command, String requestBody)
            throws PsMatrixNotFoundException, PsHostNotFoundException, PsUnknownCommandException, ParseException {
        //1. get requested matrix
        PsMatrix matrix = this.getById(id);


        boolean thisIsUnknownCommand = true;

        if (PsParameters.MATRIX_ADD_HOST_IDS.equals(command)) {
            // add hosts with given ids to the matrix
            thisIsUnknownCommand = false;

            JSONParser parser = new JSONParser();
            JSONArray hostIds = (JSONArray) parser.parse(requestBody);
            this.addHostIds(matrix, hostIds);
        }

        if (PsParameters.MATRIX_REMOVE_HOST_IDS.equals(command)) {
            // remove hosts with given ids from the matrix
            thisIsUnknownCommand = false;

            JSONParser parser = new JSONParser();
            JSONArray hostIds = (JSONArray) parser.parse(requestBody);

            // second order of business is to add those services to host
            this.removeHostIds(matrix, hostIds);

        }
        if (PsParameters.MATRIX_ADD_COLUMN_HOST_IDS.equals(command)) {
            thisIsUnknownCommand = false;

            JSONParser parser = new JSONParser();
            JSONArray hostIds = (JSONArray) parser.parse(requestBody);

            this.addHostIdsToColumns(matrix, hostIds);
        }
        if (PsParameters.MATRIX_REMOVE_COLUMN_HOST_IDS.equals(command)) {
            thisIsUnknownCommand = false;

            JSONParser parser = new JSONParser();
            JSONArray hostIds = (JSONArray) parser.parse(requestBody);

            this.removeHostIdsFromColumns(matrix, hostIds);
        }
        if (PsParameters.MATRIX_ADD_ROW_HOST_IDS.equals(command)) {
            thisIsUnknownCommand = false;

            JSONParser parser = new JSONParser();
            JSONArray hostIds = (JSONArray) parser.parse(requestBody);

            this.addHostIdsToRows(matrix, hostIds);

            //throw new UnsupportedOperationException("Not yet implemented");
        }
        if (PsParameters.MATRIX_REMOVE_ROW_HOST_IDS.equals(command)) {
            thisIsUnknownCommand = false;

            JSONParser parser = new JSONParser();
            JSONArray hostIds = (JSONArray) parser.parse(requestBody);

            this.removeHostIdsFromRows(matrix, hostIds);
        }
        if (PsParameters.MATRIX_REMOVE_ALL_HOSTS.equals(command)) {
            thisIsUnknownCommand = false;
            this.removeAllHosts(matrix);
        }


        if (thisIsUnknownCommand) {
            throw new PsUnknownCommandException(this.getClass().getName()+ " unknown command: "+command);
        }
        return matrix;
    }

    /**
     * add hosts with id's stored in JSONArray hostIds to matrix
     *
     * @param matrix
     * @param hostIds
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void addHostIds(PsMatrix matrix, JSONArray hostIds) throws PsHostNotFoundException {
        Iterator iter = hostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            this.addHostToMatrix(matrix, hostId);
        }
    }

    /**
     * add host with given id to matrix
     *
     * @param matrix
     * @param hostId
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void addHostToMatrix(PsMatrix matrix, int hostId) throws PsHostNotFoundException {
        PsHost host = this.psHostOperator.getById(hostId);
        this.addHostToMatrix(matrix, host);
    }

    /**
     * add host to matrix
     *
     * @param matrix
     * @param host
     */
    @Transactional
    public void addHostToMatrix(PsMatrix matrix, PsHost host) {
        if (matrix.isTraceroute()) {
            throw new UnsupportedOperationException("Traceroute matrix not yet implemented");
        }
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
                                psServiceFactory.createService(matrix.getType(),
                                rowHost, host, rowHost);
                        //save the service
                        this.psServiceOperator.insert(service1);

                        PsService service2 =
                                psServiceFactory.createService(matrix.getType(),
                                rowHost, host, host);
                        // save the service
                        this.psServiceOperator.insert(service2);


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
                                psServiceFactory.createService(matrix.getType(),
                                host, columnHost, host);
                        this.psServiceOperator.insert(service1);

                        PsService service2 =
                                psServiceFactory.createService(matrix.getType(),
                                host, columnHost, columnHost);
                        this.psServiceOperator.insert(service2);

                        // insert those services into matrix
                        matrix.addService(columnIndex, rowIndex, 0, service1);
                        matrix.addService(columnIndex, rowIndex, 1, service2);
                    }
                }

            }
            // update the matrix
            this.update(matrix);
        }
    }

    /**
     * remove hosts with ids store in JSONArray hostIds from matrix
     *
     * @param matrix
     * @param hostIds
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void removeHostIds(PsMatrix matrix, JSONArray hostIds) throws PsHostNotFoundException {
        Iterator iter = hostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            this.removeHostFromMatrix(matrix, hostId);
        }
    }

    /**
     * remove host with give id from matrix
     *
     * @param matrix
     * @param hostId
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void removeHostFromMatrix(PsMatrix matrix, int hostId) throws PsHostNotFoundException {
        PsHost host = this.psHostOperator.getById(hostId);
        this.removeHostFromMatrix(matrix, host);
    }

    /**
     * remove host from matrix
     *
     * @param matrix
     * @param host
     */
    @Transactional
    public void removeHostFromMatrix(PsMatrix matrix, PsHost host) {
        if (matrix.containsHost(host)) {
            List<PsService> servicesToBeDeleted = matrix.removeHost(host);
            this.update(matrix);
            this.psServiceOperator.delete(servicesToBeDeleted);
            servicesToBeDeleted.clear();
        }
    }

    /**
     * remove all hosts from matrix
     *
     * @param matrix
     */
    @Transactional
    public void removeAllHosts(PsMatrix matrix) {
        //1. build a list of hosts to be removed
        List<PsHost> hostsToBeRemoved = new ArrayList<PsHost>();
        for (PsHost currentHost : matrix.getAllHosts()) {
            hostsToBeRemoved.add(currentHost);
        }
        //2. remove those hosts
        for (PsHost hostToBeRemoved : hostsToBeRemoved) {
            this.removeHostFromMatrix(matrix, hostToBeRemoved);
        }
        //3. to be sure, clear the intermediate host list
        hostsToBeRemoved.clear();
        //4. save the updated matrix (it should have been saved by removeHostFromMatrix, 
        // but it does not hurt to do it again)
        this.update(matrix);
    }

    /**
     * add hosts with ids specified in JSONArray to columns of matrix
     *
     * @param matrix
     * @param hostIds
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void addHostIdsToColumns(PsMatrix matrix, JSONArray hostIds) throws PsHostNotFoundException {
        Iterator iter = hostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            this.addHostToMatrixColumn(matrix, hostId);
        }
    }

    /**
     * add host with given id to columns of matrix
     *
     * @param matrix
     * @param hostId
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void addHostToMatrixColumn(PsMatrix matrix, int hostId) throws PsHostNotFoundException {
        PsHost host = this.psHostOperator.getById(hostId);
        addHostToMatrixColumn(matrix, host);
    }

    /**
     * add host to columns of matrix
     *
     * @param matrix
     * @param host
     */
    @Transactional
    public void addHostToMatrixColumn(PsMatrix matrix, PsHost host) {
        if (!matrix.containsHostInColumn(host)) {
            // first of all add host to columns and rows
            matrix.addHostToColumn(host);

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
                        PsService service1 = psServiceFactory.createService(
                                matrix.getType(),
                                rowHost, host, rowHost);
                        PsService service2 = psServiceFactory.createService(
                                matrix.getType(),
                                rowHost, host, host);
                        // insert those services into matrix
                        matrix.addService(columnIndex, rowIndex, 0, service1);
                        matrix.addService(columnIndex, rowIndex, 1, service2);
                    }

                }
            }
            if (matrix.isTraceroute()) {
                throw new Error("Traceroute matrix not yet implemented");
            }
        }
    }

    /**
     * remove hosts with ids given in JAONArray from matrix columns
     *
     * @param matrix
     * @param hostIds
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void removeHostIdsFromColumns(PsMatrix matrix, JSONArray hostIds) throws PsHostNotFoundException {
        Iterator iter = hostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            this.removeHostFromMatrixColumn(matrix, hostId);
        }
    }

    /**
     * remove host with given id from matrix columns
     *
     * @param matrix
     * @param hostId
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void removeHostFromMatrixColumn(PsMatrix matrix, int hostId) throws PsHostNotFoundException {
        PsHost host = this.psHostOperator.getById(hostId);
        removeHostFromMatrixColumn(matrix, host);
    }

    /**
     * remove host from matrix columns
     *
     * @param matrix
     * @param host
     */
    @Transactional
    public void removeHostFromMatrixColumn(PsMatrix matrix, PsHost host) {
        if (matrix.containsHostInColumn(host)) {
            List<PsService> servicesToBeDeleted = matrix.removeHostFromColumns(host.getId());
            this.update(matrix);
            this.psServiceOperator.delete(servicesToBeDeleted);
            servicesToBeDeleted.clear();
        }
    }

    /**
     * add hosts with ids specified in JSONArray to rows of matrix
     * @param matrix
     * @param hostIds
     * @throws PsHostNotFoundException 
     */
    @Transactional
    public void addHostIdsToRows(PsMatrix matrix, JSONArray hostIds) throws PsHostNotFoundException {
        Iterator iter = hostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            this.addHostToMatrixRows(matrix, hostId);
        }
    }

    /**
     * add host with given id to row of matrix
     * @param matrix
     * @param hostId
     * @throws PsHostNotFoundException 
     */
    @Transactional
    public void addHostToMatrixRows(PsMatrix matrix, int hostId) throws PsHostNotFoundException {
        PsHost host = this.psHostOperator.getById(hostId);
        this.addHostToMatrixRow(matrix, host);
    }

    /**
     * add given host to row of matrix
     * @param matrix
     * @param host 
     */
    @Transactional
    public void addHostToMatrixRow(PsMatrix matrix, PsHost host) {
        if (!matrix.containsHostInRow(host)) {
            // first of all add host to rows
            matrix.addHostToRow(host);

            // ok, host has been added to matrix
            // now fill the relevant services

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
                        PsService service1 = psServiceFactory.createService(
                                matrix.getType(),
                                host, columnHost, host);
                        PsService service2 = psServiceFactory.createService(
                                matrix.getType(),
                                host, columnHost, columnHost);
                        // insert those services into matrix
                        matrix.addService(columnIndex, rowIndex, 0, service1);
                        matrix.addService(columnIndex, rowIndex, 1, service2);
                    }
                }

            }

            if (matrix.isTraceroute()) {
                throw new Error("Traceroute matrix not yet implemented");
            }
        }
    }

    /**
     * remove hosts whose ids are specified in JSONArray from rows of matrix
     * @param matrix
     * @param hostIds
     * @throws PsHostNotFoundException 
     */
    @Transactional
    public void removeHostIdsFromRows(PsMatrix matrix, JSONArray hostIds) throws PsHostNotFoundException {
        Iterator iter = hostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            this.removeHostFromMatrixRow(matrix, hostId);
        }
    }

    /**
     * remove host with given id from rows of matrix
     * @param matrix
     * @param hostId
     * @throws PsHostNotFoundException 
     */
    @Transactional
    public void removeHostFromMatrixRow(PsMatrix matrix, int hostId) throws PsHostNotFoundException {
        PsHost host = this.psHostOperator.getById(hostId);
        this.removeHostFromMatrixRow(matrix, host);
    }

    /**
     * remove host from rows of matrix
     * @param matrix
     * @param host 
     */
    @Transactional
    public void removeHostFromMatrixRow(PsMatrix matrix, PsHost host) {
        if (matrix.containsHostInRow(host)) {
            List<PsService> servicesToBeDeleted = matrix.removeHostFromRows(host.getId());
            this.update(matrix);
            this.psServiceOperator.delete(servicesToBeDeleted);
            servicesToBeDeleted.clear();
        }
    }
}
