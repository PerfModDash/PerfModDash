/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;

import gov.bnl.racf.ps.dashboard.object_manipulators.PsServiceTypeFactory;
import java.util.Date;
import java.util.Vector;

/**
 * Object describing perfSonar service matrix.
 * @author tomw
 */
public class PsMatrix {

    // field names in JSON objects
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DETAIL_LEVEL = "detailLevel";
    public static final String STATUS_LABELS = "statusLabels";
    public static final String LAST_UPDATE_TIME = "lastUpdateTime";
    public static final String NUMBER_OF_ROWS = "numberOfRows";
    public static final String ROWS = "rows";
    public static final String NUMBER_OF_COLUMNS = "numberOfColumns";
    public static final String COLUMNS = "columns";
    public static final String MAX_NUMBER_OF_SERVICE_NAMES = "maxNumberOfServiceNames";
    public static final String NUMBER_OF_SERVICE_NAMES = "numberOfServiceNames";
    public static final String MATRIX = "matrix";
    public static final String SERVICE_NAMES = "serviceNames";
    public static final String SERVICE_TYPE_ID = "serviceTypeId";
   
    // I removed this, the REST constants will go to PsApi class
    //commands in REST intervace
    //public static final String ADD_HOSTS_COMMAND="addhosts";
    //public static final String REMOVE_HOSTS_COMMAND="removehosts";
    // identifier of id's opf hosts
    //public static final String HOSTS_IDS="ids";
    
    
    
    
    private String id;
    private String name;
    private String detailLevel = "low";
    private String[] statusLabels = new String[5];
    private Date lastUpdateTime;
    private int numberOfRows = 0;
    private PsHost[] rows = new PsHost[20];
    private int numberOfColumns = 0;
    private PsHost[] columns = new PsHost[20];
    private int maxNumberOfServiceNames = 2;
    private int numberOfServiceNames = 0;
    private String[] serviceNames = new String[2];
    private PsService[][][] matrix;
    private PsServiceType matrixType;
    // max allowed matrix size
    private int maxNumberOfColumns = 20;
    private int maxNumberOfRows = 20;

    /**
     * simple constructor, build empty matrix
     */
    public PsMatrix(PsServiceType matrixType) {
        id = PsIdGenerator.generateId();
        this.matrixType = matrixType;
        initializeNumberOfServiceNames();
        initializeEmptyMatrix();
        initializeStatusLabels();
    }

    /**
     * construct empty matrix with a given name
     *
     * @param name
     */
    public PsMatrix(PsServiceType matrixType, String name) {
        id = PsIdGenerator.generateId();
        this.matrixType = matrixType;
        this.name = name;
        initializeNumberOfServiceNames();
        initializeEmptyMatrix();
        initializeStatusLabels();
    }

    private void initializeNumberOfServiceNames() {
        if (this.matrixType.isTraceroute()) {
            this.numberOfServiceNames = 1;
            this.serviceNames[0] = "Traceroute Check";
        }
        if (this.matrixType.isLatency()) {
            this.numberOfServiceNames = 2;
            this.serviceNames[0] = "Packet Loss Check - Forward";
            this.serviceNames[1] = "Packet Loss Check - Reverse";
        }
        if (this.matrixType.isThroughput()) {
            this.numberOfServiceNames = 2;
            this.serviceNames[0] = "Throughput Check - Forward";
            this.serviceNames[1] = "Throughput Check - Reverse";
        }
    }

    public String[] getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(String[] serviceNames) {
        this.serviceNames = serviceNames;
    }

    private void initializeStatusLabels() {
        if (PsServiceTypeFactory.THROUGHPUT.equals(matrixType.getId())) {
            statusLabels[0] = "Throughput > 500Mbps";
            statusLabels[1] = "Throughput > 100Mbps and < 500 Mbps";
            statusLabels[2] = "Throughput < 100Mbps";
            statusLabels[3] = "Server Error Occurred";
            statusLabels[4] = "Timeout error occurred";
        } else {
            statusLabels[0] = "OK";
            statusLabels[1] = "WARNING";
            statusLabels[2] = "CRITICAL";
            statusLabels[3] = "Server error occured";
            statusLabels[4] = "Timeout error occurred";
        }
    }

    public PsHost[] getColumns() {
        return columns;
    }

    public void setColumns(PsHost[] columns) {
        this.columns = columns;
    }

    public String getDetailLevel() {
        return detailLevel;
    }

    public void setDetailLevel(String detailLevel) {
        this.detailLevel = detailLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public PsHost[] getRows() {
        return rows;
    }

    public void setRows(PsHost[] rows) {
        this.rows = rows;
    }

    /**
     * set matrix element
     *
     * @param rowIndex
     * @param columnIndex
     * @param serviceIndex
     * @param service
     */
    public void setMatrixElement(
            int rowIndex, int columnIndex, int serviceIndex, PsService service) {
        this.matrix[rowIndex][columnIndex][serviceIndex] = service;
        PsService temp = this.matrix[rowIndex][columnIndex][serviceIndex];
    }

    /**
     * get matrix type
     *
     * @return
     */
    public PsServiceType getType() {
        return matrixType;
    }

    /**
     * get column number for host
     *
     * @param host
     * @return
     */
    public int getColumnNumberOfHost(PsHost host) {
        int result = -1;
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            if (columns[i].getId().equals(host.getId())) {
                return i;
            }
        }
        return result;
    }

    /**
     * get row number of a host
     *
     * @param host
     * @return
     */
    public int getRowNumberOfHost(PsHost host) {
        int result = -1;
        for (int i = 0; i < numberOfRows; i = i + 1) {
            if (rows[i].getId().equals(host.getId())) {
                return i;
            }
        }
        return result;
    }

    public String[] getStatusLabels() {
        return statusLabels;
    }

    public void setStatusLabels(String[] statusLabels) {
        this.statusLabels = statusLabels;
    }

    public int getNumberOfServiceNames() {
        return numberOfServiceNames;
    }

    /**
     * Initialize matrix, fill its elements with null
     */
    private void initializeEmptyMatrix() {

        matrix = new PsService[50][50][2];

        for (int i = 0; i < maxNumberOfColumns; i = i + 1) {
            for (int j = 0; j < maxNumberOfRows; j = j + 1) {
                for (int k = 0; k < maxNumberOfServiceNames; k = k + 1) {
                    matrix[i][j][k] = null;
                }
            }
        }
    }

    /**
     * check if matrix contains host in column
     * @param host
     * @return 
     */
    public boolean containsHostInColumn(PsHost host){
        return containsHostInColumn(host.getId());
    }
    /**
     * check if matrix contains host in columns
     * @param hostId
     * @return 
     */
    public boolean containsHostInColumn(String hostId){
        boolean result = false;
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            if (columns[i].getId().equals(hostId)) {
                result = true;
                return result;
            }
        }
        return result;
    }
    /**
     * check if matrix contains host in row
     * @param host
     * @return 
     */
    public boolean containsHostInRow(PsHost host){
        return containsHostInRow(host.getId());
    }
    /**
     * check if matrix contains host in row
     * @param hostId
     * @return 
     */
    public boolean containsHostInRow(String hostId){
        boolean result=false;
        for (int i = 0; i < numberOfRows; i = i + 1) {
            if (rows[i].getId().equals(hostId)) {
                result = true;
                return result;
            }
        }
        return result;
    }
    /**
     * check if matrix contains host hostId
     *
     * @param hostId
     * @return
     */
    public boolean containsHost(String hostId) {
        boolean result = false;
        boolean inColumn = containsHostInColumn(hostId);
        if(inColumn){
            return true;
        }
        
        boolean inRow = containsHostInRow(hostId);
        if(inRow){
            return true;
        }
        return result;
    }

    /**
     * check if matrix contains host
     *
     * @param host
     * @return
     */
    public boolean containsHost(PsHost host) {
        return containsHost(host.getId());
    }

    /**
     * add host to columns
     * @param host 
     */
    public void addHostToColumn(PsHost host){
        if(!containsHostInColumn(host)){
            columns[numberOfColumns] = host;
            numberOfColumns = numberOfColumns + 1;
        }
    }
    /**
     * add host to rows
     * @param host 
     */
    public void addHostToRow(PsHost host){
        if(!containsHostInRow(host)){
            rows[numberOfRows] = host;
            numberOfRows = numberOfRows + 1;
        }
    }
    
    /**
     * add new host to matrix. This method does not fill the matrix with the
     * corresponding services. This has to be done separately by the matrix
     * manipulator
     *
     * @param host
     */
    public void addHost(PsHost host) {
        addHostToColumn(host);
        addHostToRow(host);
    }

    /**
     * remove host from matrix Shrink the matrix accordingly
     *
     * @param host
     */
    public void removeHost(PsHost host) {
        removeHost(host.getId());
    }

    /**
     * remove host from matrix. Shrink matrix as needed
     *
     * @param hostId
     */
    public void removeHost(String hostId) {
        removeHostFromColumns(hostId);
        removeHostFromRows(hostId);
    }

    /**
     * remove host hostId from columns
     *
     * @param hostId
     */
    public void removeHostFromColumns(String hostId) {
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            if (columns[i].getId().equals(hostId)) {
                removeColumn(i);
            }
        }
    }

    /**
     * remove host hostId from rows
     *
     * @param hostId
     */
    public void removeHostFromRows(String hostId) {
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            if (columns[i].getId().equals(hostId)) {
                removeRow(i);
            }
        }
    }

    /**
     * remove columnNo from matrix
     *
     * @param columnNo
     */
    public void removeColumn(int columnNo) {
        for (int columnIndex = columnNo; columnIndex < numberOfColumns;
                columnIndex = columnIndex + 1) {
            columns[columnIndex] = columns[columnIndex + 1];  
            columns[columnIndex + 1]=null;

            for (int j = 0; j < numberOfRows; j = j + 1) {
                for (int k = 0; k < maxNumberOfServiceNames; k = k + 1) {
                    matrix[columnIndex][j][k] = matrix[columnIndex + 1][j][k]; 
                    matrix[columnIndex + 1][j][k]=null;
                }
            }
        }
        numberOfColumns = numberOfColumns - 1;
    }

    /**
     * remove row from matrix
     *
     * @param rowNo
     */
    public void removeRow(int rowNo) {
        System.out.println(new Date()+" "+getClass().getName()+" at entraNCE");
        System.out.println(new Date()+" "+getClass().getName()+" numberOfRows="+numberOfRows);
        for(int i=0;i<numberOfRows;i=i+1){
            PsHost currentHost = rows[i];
            if(currentHost==null){
                System.out.println(new Date()+" "+getClass().getName()+" host=null");
            }else{
                System.out.println(new Date()+" "+getClass().getName()+" host="+currentHost.getHostname());
            }
        }
        
        
        
        for (int rowIndex = rowNo; rowIndex < numberOfRows; rowIndex = rowIndex + 1) {
            this.rows[rowIndex] = this.rows[rowIndex + 1];
            this.rows[rowIndex+1]=null;

            for (int i = 0; i < this.numberOfColumns; i = i + 1) {
                for (int k = 0; k < maxNumberOfServiceNames; k = k + 1) {
                    matrix[i][rowIndex][k] = matrix[i][rowIndex + 1][k];      
                    matrix[i][rowIndex + 1][k]=null;
                }
            }
        }
        this.numberOfRows = this.numberOfRows - 1;
        System.out.println(new Date()+" "+getClass().getName()+" at exit");
        System.out.println(new Date()+" "+getClass().getName()+" numberOfRows="+numberOfRows);
        for(int i=0;i<numberOfRows;i=i+1){
            PsHost currentHost = rows[i];
            if(currentHost==null){
                System.out.println(new Date()+" "+getClass().getName()+" host=null");
            }else{
                System.out.println(new Date()+" "+getClass().getName()+" host="+currentHost.getHostname());
            }
        }
    }

    /**
     * add service no k into designated column and row
     *
     * @param hostColumn
     * @param hostRow
     * @param k
     * @param service
     */
    public void addService(PsHost hostColumn,
            PsHost hostRow,
            int k,
            PsService service) {
        addService(hostColumn.getId(), hostRow.getId(), k, service);
    }

    /**
     * add service to matrix in column and row defined by hosts with id
     * hostColumnId and hostRowId. Use service number k.
     *
     * @param hostColumnId
     * @param hostRowId
     * @param k
     * @param service
     */
    public void addService(String hostColumnId,
            String hostRowId,
            int k,
            PsService service) {
        int columnIndex = getHostColumn(hostColumnId);
        int rowIndex = getHostRow(hostRowId);
        if (columnIndex != -1 && rowIndex != -1) {
            matrix[columnIndex][rowIndex][k] = service;
        }

    }

    /**
     * get column index of this host, return -1 if none
     *
     * @param hostId
     * @return
     */
    private int getHostColumn(String hostId) {
        int result = -1;
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            if (columns[i].getId().equals(hostId)) {
                result = i;
                return result;
            }
        }
        return result;
    }

    /**
     * get row index of this host, -1 if none
     *
     * @param hostId
     * @return
     */
    private int getHostRow(String hostId) {
        int result = -1;
        for (int i = 0; i < numberOfRows; i = i + 1) {
            if (rows[i].getId().equals(hostId)) {
                result = i;
                return result;
            }
        }
        return result;
    }

    /**
     * get service from designated column and row. If there is more than one
     * service in that location choose service serviceNo
     *
     * @param columnHost
     * @param rowHost
     * @param serviceNo
     * @return
     */
    public PsService getService(PsHost columnHost,
            PsHost rowHost, int serviceNo) {
        return getService(columnHost.getId(), rowHost.getId(), serviceNo);

    }

    /**
     * return service which stands in matrix in column defined by host
     * columnHostId, row rowHostId as service number serviceNo
     *
     * @param columnHostId
     * @param rowHostId
     * @param serviceNo
     * @return
     */
    public PsService getService(String columnHostId,
            String rowHostId,
            int serviceNo) {
        int columnIndex = getHostColumn(columnHostId);
        int rowIndex = getHostRow(rowHostId);
        if (columnIndex != -1 && rowIndex != -1) {
            return matrix[columnIndex][rowIndex][serviceNo];
        } else {
            return null;
        }
    }

    /**
     * get element of service matrix corresponding to columnId, rowId and
     * serviceId (serviceId is the number of service in matrix, not the service
     * identifier!!!)
     *
     * @param columnId
     * @param rowId
     * @param serviceId
     * @return
     */
    public PsService getService(int columnId, int rowId, int serviceId) {
        if (columnId != -1 && rowId != -1) {
            return matrix[columnId][rowId][serviceId];
        } else {
            return null;
        }
    }

    /**
     * get host stored at location rowIndex in rows table
     *
     * @param rowIndex
     * @return
     */
    public PsHost getHostInRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex > numberOfRows - 1) {
            return null;
        } else {
            return rows[rowIndex];
        }
    }

    /**
     * get host stored at location columnIndex in array columns
     *
     * @param columnIndex
     * @return
     */
    public PsHost getHostInColumn(int columnIndex) {
        if (columnIndex < 0 || columnIndex > numberOfColumns - 1) {
            return null;
        } else {
            return columns[columnIndex];
        }
    }

    /**
     * remove service serviceId from the matrix
     *
     * @param serviceId
     */
    public void removeService(String serviceId) {
        if (serviceId != null) {
            for (int i = 0; i < maxNumberOfColumns; i = i + 1) {
                for (int j = 0; j < maxNumberOfRows; j = j + 1) {
                    for (int k = 0; k < maxNumberOfServiceNames; k = k + 1) {
                        if (matrix[i][j][k] != null) {
                            if (matrix[i][j][k].equals(serviceId)) {
                                matrix[i][j][k] = null;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * return a Vector containing all service Id's from this matrix
     *
     * @return
     */
    public Vector<String> getComponentServiceIds() {
        Vector<String> componentServiceIds = new Vector<String>();
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            for (int j = 0; j < numberOfRows; j = j + 1) {
                for (int k = 0; k < numberOfServiceNames; k = k + 1) {
                    if (matrix[i][j][k] != null) {
                        componentServiceIds.add(matrix[i][j][k].getId());
                    }
                }
            }
        }
        return componentServiceIds;
    }

    /**
     * check if this is throughput matrix
     *
     * @return
     */
    public boolean isThroughput() {
        return matrixType.isThroughput();
    }

    /**
     * check if this is latency matrix
     *
     * @return
     */
    public boolean isLatency() {
        return matrixType.isLatency();
    }

    /**
     * check if this is traceroute matrix
     *
     * @return
     */
    public boolean isTraceroute() {
        return matrixType.isTraceroute();
    }

    /**
     * get vector containing service id's of services from a given column
     *
     * @param columnNumber
     * @return
     */
    public Vector<String> getServiceIdsInColumn(int columnNumber) {
        Vector<String> listOfServiceIds = new Vector<String>();
        if (columnNumber > -1 && columnNumber < numberOfColumns) {
            for (int row = 0; row < numberOfRows; row = row + 1) {
                for (int serviceName = 0; serviceName < numberOfServiceNames; serviceName = serviceName + 1) {
                    PsService currentService = matrix[columnNumber][row][serviceName];
                    if (currentService != null) {
                        String serviceId = currentService.getId();
                        listOfServiceIds.add(serviceId);
                    }
                }
            }
        }
        return listOfServiceIds;
    }

    /**
     * get vector containing service id's of services from a given row
     *
     * @param columnNumber
     * @return
     */
    public Vector<String> getServiceIdsInRow(int rowNumber) {
        Vector<String> listOfServiceIds = new Vector<String>();
        if (rowNumber > -1 && rowNumber < numberOfRows) {
            for (int column = 0; column < numberOfColumns; column = column + 1) {
                for (int serviceName = 0; serviceName < numberOfServiceNames; serviceName = serviceName + 1) {
                    PsService currentService = matrix[column][rowNumber][serviceName];
                    if (currentService != null) {
                        String serviceId = currentService.getId();
                        listOfServiceIds.add(serviceId);
                    }
                }
            }
        }
        return listOfServiceIds;
    }
}
