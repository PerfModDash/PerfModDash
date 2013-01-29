/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.data_objects;

import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
import java.util.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

/**
 * Object describing perfSonar service matrix.
 *
 * @author tomw
 */
@Cacheable
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
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
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String detailLevel = "low";
    //@Lob
    //private String[] statusLabels = new String[5];
    @CollectionOfElements
    private List<String> statusLabels = new ArrayList<String>();
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    // max allowed matrix size
    private int maxNumberOfColumns = 20;
    private int maxNumberOfRows = 20;
    // 
    // rows
    private int numberOfRows = 0;
    @OneToMany
    @IndexColumn(name = "id")
    private PsHost[] hostsInRows = new PsHost[maxNumberOfRows];
    //
    // columns
    private int numberOfColumns = 0;
    @OneToMany
    @IndexColumn(name = "id")
    private PsHost[] hostsInColumns = new PsHost[maxNumberOfColumns];
    //
    private int maxNumberOfServiceNames = 2;
    private int numberOfServiceNames = 0;
    //@Lob
    //private String[] serviceNames = new String[2];
    @CollectionOfElements
    private List<String>  serviceNames= new ArrayList<String>();
    //
    //
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<PsService> services = new Vector<PsService>();
    @Lob
    private int[][][] matrixOfServiceIds = new int[maxNumberOfRows][maxNumberOfColumns][2];
    //@OneToMany
    //@IndexColumn(name = "id")
    //private PsService[][][] matrix = new PsService[maxNumberOfRows][maxNumberOfColumns][2];
    @ManyToOne
    private PsServiceType matrixType;

    /**
     * simple constructor, build empty matrix
     */
    public PsMatrix() {
        //initializeNumberOfServiceNames();
        initializeEmptyMatrix();
        //initializeStatusLabels();
    }

    /**
     * initialize empty matrix
     *
     * @param matrixType
     */
    public void initMatrix(PsServiceType matrixType) {

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

        this.matrixType = matrixType;
        this.name = name;
        initializeNumberOfServiceNames();
        initializeEmptyMatrix();
        initializeStatusLabels();
    }

    private void initializeNumberOfServiceNames() {
        if (this.matrixType.isTraceroute()) {
            this.numberOfServiceNames = 1;
            this.serviceNames.add("Traceroute Check");
        }
        if (this.matrixType.isLatency()) {
            this.numberOfServiceNames = 2;
            this.serviceNames.add("Packet Loss Check - Forward");
            this.serviceNames.add("Packet Loss Check - Reverse");
        }
        if (this.matrixType.isThroughput()) {
            this.numberOfServiceNames = 2;
            this.serviceNames.add("Throughput Check - Forward");
            this.serviceNames.add("Throughput Check - Reverse");
        }
    }
    
//     private void initializeNumberOfServiceNames() {
//        if (this.matrixType.isTraceroute()) {
//            this.numberOfServiceNames = 1;
//            this.serviceNames[0] = "Traceroute Check";
//        }
//        if (this.matrixType.isLatency()) {
//            this.numberOfServiceNames = 2;
//            this.serviceNames[0] = "Packet Loss Check - Forward";
//            this.serviceNames[1] = "Packet Loss Check - Reverse";
//        }
//        if (this.matrixType.isThroughput()) {
//            this.numberOfServiceNames = 2;
//            this.serviceNames[0] = "Throughput Check - Forward";
//            this.serviceNames[1] = "Throughput Check - Reverse";
//        }
//    }

//    public String[] getServiceNames() {
//        return serviceNames;
//    }

//    public void setServiceNames(String[] serviceNames) {
//        this.serviceNames = serviceNames;
//    }

    private void initializeStatusLabels() {
        if (PsApi.THROUGHPUT.equals(matrixType.getServiceTypeId())) {
            statusLabels.add("Throughput > 500Mbps");
            statusLabels.add("Throughput > 100Mbps and < 500 Mbps");
            statusLabels.add("Throughput < 100Mbps");
            statusLabels.add("Server Error Occurred");
            statusLabels.add("Timeout error occurred");
        } else {
            statusLabels.add("OK");
            statusLabels.add("WARNING");
            statusLabels.add("CRITICAL");
            statusLabels.add("Server error occured");
            statusLabels.add("Timeout error occurred");
        }
    }
//    private void initializeStatusLabels() {
//        if (PsApi.THROUGHPUT.equals(matrixType.getServiceTypeId())) {
//            statusLabels[0] = "Throughput > 500Mbps";
//            statusLabels[1] = "Throughput > 100Mbps and < 500 Mbps";
//            statusLabels[2] = "Throughput < 100Mbps";
//            statusLabels[3] = "Server Error Occurred";
//            statusLabels[4] = "Timeout error occurred";
//        } else {
//            statusLabels[0] = "OK";
//            statusLabels[1] = "WARNING";
//            statusLabels[2] = "CRITICAL";
//            statusLabels[3] = "Server error occured";
//            statusLabels[4] = "Timeout error occurred";
//        }
//    }

    public int getId() {
        return id;
    }

    public PsHost[] getHostsInColumns() {
        return hostsInColumns;
    }

    public void setHostsInColumns(PsHost[] hostsInColumns) {
        this.hostsInColumns = hostsInColumns;
    }

    public PsHost[] getHostsInRows() {
        return hostsInRows;
    }

    public void setHostsInRows(PsHost[] hostsInRows) {
        this.hostsInRows = hostsInRows;
    }

    public PsServiceType getMatrixType() {
        return matrixType;
    }

    public void setMatrixType(PsServiceType matrixType) {
        this.matrixType = matrixType;
    }

    public String getDetailLevel() {
        return detailLevel;
    }

    public void setDetailLevel(String detailLevel) {
        this.detailLevel = detailLevel;
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
        int serviceId = service.getId();
        this.matrixOfServiceIds[rowIndex][columnIndex][serviceIndex] = serviceId;

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
            if (hostsInColumns[i].getId() == host.getId()) {
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
            if (hostsInRows[i].getId() == host.getId()) {
                return i;
            }
        }
        return result;
    }

    public List<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public List<String> getStatusLabels() {
        return statusLabels;
    }

    public void setStatusLabels(List<String> statusLabels) {
        this.statusLabels = statusLabels;
    }

    
    
    
    
    
//    public String[] getStatusLabels() {
//        return statusLabels;
//    }
//
//    public void setStatusLabels(String[] statusLabels) {
//        this.statusLabels = statusLabels;
//    }

    public int getNumberOfServiceNames() {
        return numberOfServiceNames;
    }

    /**
     * Initialize matrix, fill its elements with null
     */
    private void initializeEmptyMatrix() {

        for (int i = 0; i < maxNumberOfColumns; i = i + 1) {
            for (int j = 0; j < maxNumberOfRows; j = j + 1) {
                for (int k = 0; k < maxNumberOfServiceNames; k = k + 1) {
                    matrixOfServiceIds[i][j][k] = -1;
                }
            }
        }
    }

    /**
     * check if matrix contains host in column
     *
     * @param host
     * @return
     */
    public boolean containsHostInColumn(PsHost host) {
        return containsHostInColumn(host.getId());
    }

    /**
     * check if matrix contains host in columns
     *
     * @param hostId
     * @return
     */
    public boolean containsHostInColumn(int hostId) {
        boolean result = false;
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            if (hostsInColumns[i].getId() == hostId) {
                result = true;
                return result;
            }
        }
        return result;
    }

    /**
     * check if matrix contains host in row
     *
     * @param host
     * @return
     */
    public boolean containsHostInRow(PsHost host) {
        return containsHostInRow(host.getId());
    }

    /**
     * check if matrix contains host in row
     *
     * @param hostId
     * @return
     */
    public boolean containsHostInRow(int hostId) {
        boolean result = false;
        for (int i = 0; i < numberOfRows; i = i + 1) {
            if (hostsInRows[i].getId() == hostId) {
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
    public boolean containsHost(int hostId) {
        boolean result = false;
        boolean inColumn = containsHostInColumn(hostId);
        if (inColumn) {
            return true;
        }

        boolean inRow = containsHostInRow(hostId);
        if (inRow) {
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
     *
     * @param host
     */
    public void addHostToColumn(PsHost host) {
        if (!containsHostInColumn(host)) {
            hostsInColumns[numberOfColumns] = host;
            numberOfColumns = numberOfColumns + 1;
        }
    }

    /**
     * add host to rows
     *
     * @param host
     */
    public void addHostToRow(PsHost host) {
        if (!containsHostInRow(host)) {
            hostsInRows[numberOfRows] = host;
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
    public List<PsService> removeHost(PsHost host) {
        return removeHost(host.getId());
    }

    /**
     * remove host from matrix. Shrink matrix as needed
     *
     * @param hostId
     */
    public List<PsService> removeHost(int hostId) {
        List<PsService> servicesToBeRemoved = new ArrayList<PsService>();
        
        List<PsService> servicesDeletedFromColumn = removeHostFromColumns(hostId);
        if(!servicesDeletedFromColumn.isEmpty()){
            servicesToBeRemoved.addAll(servicesDeletedFromColumn);
        }
        
        List<PsService> servicesDeletedFromRow = removeHostFromRows(hostId);
        if(!servicesDeletedFromRow.isEmpty()){
            servicesToBeRemoved.addAll(servicesDeletedFromRow);
        }
        return servicesToBeRemoved;
    }

    /**
     * remove host hostId from columns
     *
     * @param hostId
     */
    public List<PsService> removeHostFromColumns(int hostId) {
        List<PsService> servicesToBeRemoved = new ArrayList<PsService>();
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            if (hostsInColumns[i].getId() == hostId) {
                List<PsService> servicesFromThisColumn = removeColumn(i);
                if (!servicesFromThisColumn.isEmpty()) {
                    servicesToBeRemoved.addAll(servicesFromThisColumn);
                }
            }
        }
        return servicesToBeRemoved;
    }

    /**
     * remove host hostId from rows
     *
     * @param hostId
     */
    public List<PsService> removeHostFromRows(int hostId) {
        List<PsService> servicesToBeRemoved = new ArrayList<PsService>();
        for (int i = 0; i < numberOfRows; i = i + 1) {
            if (hostsInRows[i].getId() == hostId) {
                List<PsService> servicesFromThisRow = removeRow(i);
                if (!servicesFromThisRow.isEmpty()) {
                    servicesToBeRemoved.addAll(servicesFromThisRow);
                }
            }
        }
        return servicesToBeRemoved;
    }

    /**
     * remove columnNo from matrix
     *
     * @param columnNo
     */
    public List<PsService> removeColumn(int columnNo) {
        List<PsService> servicesRemoved = new ArrayList<PsService>();

        for (int columnIndex = columnNo; columnIndex < numberOfColumns;
                columnIndex = columnIndex + 1) {
            hostsInColumns[columnIndex] = hostsInColumns[columnIndex + 1];
            hostsInColumns[columnIndex + 1] = null;

            for (int j = 0; j < numberOfRows; j = j + 1) {
                for (int k = 0; k < maxNumberOfServiceNames; k = k + 1) {
                    int serviceId = matrixOfServiceIds[columnIndex][j][k];
                    if (serviceId != -1) {
                        PsService serviceToBeRemoved = removeServiceFromList(serviceId);
                        if (serviceToBeRemoved != null) {
                            servicesRemoved.add(serviceToBeRemoved);
                        }
                    }
                    matrixOfServiceIds[columnIndex][j][k] = matrixOfServiceIds[columnIndex + 1][j][k];
                    matrixOfServiceIds[columnIndex + 1][j][k] = -1;
                }
            }
        }
        numberOfColumns = numberOfColumns - 1;

        return servicesRemoved;
    }

    /**
     * remove row from matrix
     *
     * @param rowNo
     */
    public List<PsService> removeRow(int rowNo) {
        List<PsService> servicesRemoved = new ArrayList<PsService>();

        for (int i = 0; i < numberOfRows; i = i + 1) {
            PsHost currentHost = hostsInRows[i];
            if (currentHost == null) {
                System.out.println(new Date() + " " + getClass().getName() + " host=null");
            } else {
                System.out.println(new Date() + " " + getClass().getName() + " host=" + currentHost.getHostname());
            }
        }

        for (int rowIndex = rowNo; rowIndex < numberOfRows; rowIndex = rowIndex + 1) {
            this.hostsInRows[rowIndex] = this.hostsInRows[rowIndex + 1];
            this.hostsInRows[rowIndex + 1] = null;

            for (int i = 0; i < this.numberOfColumns; i = i + 1) {
                for (int k = 0; k < maxNumberOfServiceNames; k = k + 1) {

                    int serviceId = matrixOfServiceIds[i][rowIndex][k];
                    if (serviceId != -1) {
                        PsService serviceToBeRemoved = removeServiceFromList(serviceId);
                        if (serviceToBeRemoved != null) {
                            servicesRemoved.add(serviceToBeRemoved);
                        }
                    }
                    matrixOfServiceIds[i][rowIndex][k] = matrixOfServiceIds[i][rowIndex + 1][k];
                    matrixOfServiceIds[i][rowIndex + 1][k] = -1;

                }
            }
        }
        this.numberOfRows = this.numberOfRows - 1;

        return servicesRemoved;
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
    public void addService(int hostColumnId,
            int hostRowId,
            int k,
            PsService service) {
        int columnIndex = getHostColumn(hostColumnId);
        int rowIndex = getHostRow(hostRowId);
        if (columnIndex != -1 && rowIndex != -1) {
            services.add(service);
            matrixOfServiceIds[columnIndex][rowIndex][k] = service.getId();
        }

    }

    /**
     * get column index of this host, return -1 if none
     *
     * @param hostId
     * @return
     */
    private int getHostColumn(int hostId) {
        int result = -1;
        for (int i = 0; i < numberOfColumns; i = i + 1) {
            if (hostsInColumns[i].getId() == hostId) {
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
    private int getHostRow(int hostId) {
        int result = -1;
        for (int i = 0; i < numberOfRows; i = i + 1) {
            if (hostsInRows[i].getId() == hostId) {
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
    public PsService getService(int columnHostId,
            int rowHostId,
            int serviceNo) {
        int columnIndex = getHostColumn(columnHostId);
        int rowIndex = getHostRow(rowHostId);
        if (columnIndex != -1 && rowIndex != -1) {
            int serviceId = matrixOfServiceIds[columnIndex][rowIndex][serviceNo];
            return getServiceById(serviceId);
        } else {
            return null;
        }
    }

    /**
     * return service from this matric by Id. Null if service not found.
     *
     * @param serviceId
     * @return
     */
    public PsService getServiceById(int serviceId) {
        Iterator iter = services.iterator();
        while (iter.hasNext()) {
            PsService currentService = (PsService) iter.next();
            if (currentService.getId() == serviceId) {
                return currentService;
            }
        }
        return null;
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
    public PsService getServiceByRowAndColumnNumber(int columnId, int rowId, int serviceId) {
        if (columnId != -1 && rowId != -1) {
            int serviceIdentifier = matrixOfServiceIds[columnId][rowId][serviceId];
            return getServiceById(serviceIdentifier);
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
            return hostsInRows[rowIndex];
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
            return hostsInColumns[columnIndex];
        }
    }

    /**
     * remove service serviceId from the matrix
     *
     * @param serviceId
     */
    public List<PsService> removeService(int serviceId) {
        List<PsService> servicesRemoved = new ArrayList<PsService>();
        if (serviceId != -1) {
            for (int i = 0; i < maxNumberOfColumns; i = i + 1) {
                for (int j = 0; j < maxNumberOfRows; j = j + 1) {
                    for (int k = 0; k < maxNumberOfServiceNames; k = k + 1) {
                        if (matrixOfServiceIds[i][j][k] != -1) {
                            if (matrixOfServiceIds[i][j][k] == serviceId) {
                                matrixOfServiceIds[i][j][k] = -1;
                                PsService serviceRemoved = removeServiceFromList(serviceId);
                                if (serviceRemoved != null) {
                                    servicesRemoved.add(serviceRemoved);
                                }
                            }
                        }
                    }
                }
            }
        }
        return servicesRemoved;
    }

    /**
     * return a Vector containing all service Id's from this matrix
     *
     * @return
     */
    public Vector<Integer> getComponentServiceIds() {
        Vector<Integer> componentServiceIds = new Vector<Integer>();
        Iterator iter = this.services.iterator();
        while (iter.hasNext()) {
            PsService currentService = (PsService) iter.next();
            Integer serviceIdInteger = new Integer(currentService.getId());
            componentServiceIds.add(serviceIdInteger);
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
    public Vector<Integer> getServiceIdsInColumn(int columnNumber) {
        Vector<Integer> listOfServiceIds = new Vector<Integer>();
        if (columnNumber > -1 && columnNumber < numberOfColumns) {
            for (int row = 0; row < numberOfRows; row = row + 1) {
                for (int serviceName = 0; serviceName < numberOfServiceNames; serviceName = serviceName + 1) {
                    if (matrixOfServiceIds[columnNumber][row][serviceName] != -1) {
                        Integer serviceIdInteger = new Integer(matrixOfServiceIds[columnNumber][row][serviceName]);
                        listOfServiceIds.add(serviceIdInteger);
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
    public Vector<Integer> getServiceIdsInRow(int rowNumber) {
        Vector<Integer> listOfServiceIds = new Vector<Integer>();
        if (rowNumber > -1 && rowNumber < numberOfRows) {
            for (int column = 0; column < numberOfColumns; column = column + 1) {
                for (int serviceName = 0; serviceName < numberOfServiceNames; serviceName = serviceName + 1) {
                    if(matrixOfServiceIds[column][rowNumber][serviceName]!=-1){
                        int serviceId = matrixOfServiceIds[column][rowNumber][serviceName];
                        Integer serviceIdInteger=new Integer(serviceId);
                        listOfServiceIds.add(serviceIdInteger);
                    }
                }
            }
        }
        return listOfServiceIds;
    }

    /**
     * take id of a service, remove service with this id from list of services
     * in the matrix. Return this service object. If service is not found return
     * null
     *
     * @param serviceId
     * @return
     */
    public PsService removeServiceFromList(int serviceId) {
        Iterator iter = services.iterator();
        while (iter.hasNext()) {
            PsService currentService = (PsService) iter.next();
            if (currentService.getId() == serviceId) {
                iter.remove();
                return currentService;
            }
        }
        return null;
    }
    
    
}
