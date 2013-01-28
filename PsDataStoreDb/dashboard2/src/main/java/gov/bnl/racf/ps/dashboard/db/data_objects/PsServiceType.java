/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.data_objects;

import java.io.Serializable;
import java.util.TreeMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Provides a way to describe a category of services that run the same type of
 * probe and parameters share the same meaning.
 *
 * @author tomw
 */
@Entity
public class PsServiceType implements Serializable {

    // service types
    public static String BWCTL_PORT_4823 = "bwctl_port_4823";
    public static String BWCTL_PORT_8570 = "bwctl_port_8570";
    public static String CHECK_LOOKUP_SERVICE = "CheckLookupService";
    public static String NDT_PORT_3001 = "NDT_port_3001";
    public static String NDT_PORT_7123 = "NDT_port_7123";
    public static String NPAD_PORT_8000 = "NPAD_port_8000";
    public static String NPAD_PORT_8001 = "NPAD_port_8001";
    public static String OWP_861 = "owp_861";
    public static String OWP_8569 = "owp_8569";
    public static String PERFSONAR_PSB = "perfSONAR_pSB";
    public static String LATENCY = "latency";
    public static String THROUGHPUT = "throughput";
    public static String TRACEROUTE = "traceroute";
    // JSON parameter names
    public static final String ID = "id";
    public static final String INTERNAL_ID = "internalId";
    public static final String JOB_TYPE = "jobType";
    public static final String NAME = "name";
    public static final String SERVICE_PARAMETER_INFO = "serviceParameterInfo";
    public static final String RESULT_PARAMETER_INFO = "resultParameterInfo";
    
    
    //private String id = "unknown";
    @Id
    @GeneratedValue
    private int id;
    
    private String serviceTypeId;

    
    private String jobType;
    private String name;
    private TreeMap<String, PsParameterInfo> serviceParameterInfo;
    private TreeMap<String, PsParameterInfo> resultParameterInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
         setId(jobType2id(serviceTypeId));
    }
    
    /**
     * convert job type name to a unique integer id
     *
     * @param type
     * @return
     */
    public int jobType2id(String type) {
        int result = 0;
        if (BWCTL_PORT_4823.equals(type)) {
            result = 1;
        }
        if (BWCTL_PORT_8570.equals(type)) {
            result = 2;
        }
        if (CHECK_LOOKUP_SERVICE.equals(type)) {
            result = 3;
        }
        if (NDT_PORT_3001.equals(type)) {
            result = 4;
        }
        if (NDT_PORT_7123.equals(type)) {
            result = 5;
        }
        if (NPAD_PORT_8000.equals(type)) {
            result = 6;
        }
        if (NPAD_PORT_8001.equals(type)) {
            result = 7;
        }
        if (OWP_861.equals(type)) {
            result = 8;
        }
        if (OWP_8569.equals(type)) {
            result = 9;
        }
        if (PERFSONAR_PSB.equals(type)) {
            result = 10;
        }
        if (LATENCY.equals(type)) {
            result = 11;
        }
        if (THROUGHPUT.equals(type)) {
            result = 12;
        }
        if (TRACEROUTE.equals(type)) {
            result = 13;
        }
        return result;
    }

    public String id2jobType(int typeId) {
        String result = "";
        if (typeId == 1) {
            return BWCTL_PORT_4823;
        }
        if (typeId == 2) {
            return BWCTL_PORT_8570;
        }
        if (typeId == 3) {
            return CHECK_LOOKUP_SERVICE;
        }
        if (typeId == 4) {
            return NDT_PORT_3001;
        }
        if (typeId == 5) {
            return NDT_PORT_7123;
        }
        if (typeId == 6) {
            return NPAD_PORT_8000;
        }
        if (typeId == 7) {
            return NPAD_PORT_8001;
        }
        if (typeId == 8) {
            return OWP_861;
        }
        if (typeId == 9) {
            return OWP_8569;
        }
        if (typeId == 10) {
            return PERFSONAR_PSB;
        }
        if (typeId == 11) {
            return LATENCY;
        }
        if (typeId == 12) {
            return THROUGHPUT;
        }
        if (typeId == 13) {
            return TRACEROUTE;
        }
        return result;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TreeMap<String, PsParameterInfo> getResultParameterInfo() {
        return resultParameterInfo;
    }

    public void setResultParameterInfo(TreeMap<String, PsParameterInfo> resultParameterInfo) {
        this.resultParameterInfo = resultParameterInfo;
    }

    public TreeMap<String, PsParameterInfo> getServiceParameterInfo() {
        return serviceParameterInfo;
    }

    public void setServiceParameterInfo(TreeMap<String, PsParameterInfo> serviceParameterInfo) {
        this.serviceParameterInfo = serviceParameterInfo;
    }

    /**
     * add service parameter info
     *
     * @param parameterName
     * @param parameterInfo
     */
    public void addServiceParameterInfo(String parameterName, PsParameterInfo parameterInfo) {
        serviceParameterInfo.put(parameterName, parameterInfo);
    }

    /**
     * add result parameter info
     *
     * @param parameterName
     * @param parameterInfo
     */
    public void addResultParameterInfo(String parameterName, PsParameterInfo parameterInfo) {
        resultParameterInfo.put(parameterName, parameterInfo);
    }

    /**
     * simple constructor. id is generated internally
     */
    public PsServiceType(){
        //id=PsIdGenerator.generateId();
    }
    /**
     * Constructor. Name and jobType are taken as parameters id is generated
     * internally
     *
     * @param jobType
     * @param name
     */
    public PsServiceType(String jobType, String name) {
        //id=PsIdGenerator.generateId();
        this.jobType = jobType;
        this.name = name;
    }

    /**
     * check if type is matrix throughput type
     *
     * @return
     */
    public boolean isThroughput() {
        if (THROUGHPUT.equals(this.serviceTypeId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * check if this is matrix latency type
     *
     * @return
     */
    public boolean isLatency() {
        if (LATENCY.equals(this.serviceTypeId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * check if this is traceroute
     *
     * @return
     */
    public boolean isTraceroute() {
        if (TRACEROUTE.equals(this.serviceTypeId)) {
            return true;
        } else {
            return false;
        }
    }
}
