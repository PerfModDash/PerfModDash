/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsServiceTypeFactory;
import java.util.TreeMap;

/**
 * Provides a way to describe a category of services that run the same type 
 * of probe and parameters share the same meaning.
 * @author tomw
 */
public class PsServiceType {
    // JSON parameter names
    public static final String ID="id";
    public static final String JOB_TYPE="jobType";
    public static final String NAME="name";
    public static final String SERVICE_PARAMETER_INFO="serviceParameterInfo";
    public static final String RESULT_PARAMETER_INFO="resultParameterInfo";
    
    
    private String id="unknown";
    private String jobType;
    private String name;
    private TreeMap<String,PsParameterInfo> serviceParameterInfo;
    private TreeMap<String,PsParameterInfo> resultParameterInfo;

    public String getId() {
        return id;
    }   
 public void setId(String id) {
        this.id = id;
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
     * @param parameterName
     * @param parameterInfo 
     */
    public void addServiceParameterInfo(String parameterName, PsParameterInfo parameterInfo){
        serviceParameterInfo.put(parameterName, parameterInfo);
    }
    
    /**
     * add result parameter info
     * @param parameterName
     * @param parameterInfo 
     */
    public void addResultParameterInfo(String parameterName, PsParameterInfo parameterInfo){
        resultParameterInfo.put(parameterName, parameterInfo);
    }
    
    /**
     * simple constructor. id is generated internally
     */
    public PsServiceType(){
        //id=PsIdGenerator.generateId();
    }
    /**
     * Constructor. Name and jobType are taken as parameters
     * id is generated internally
     * @param jobType
     * @param name 
     */
    public PsServiceType(String jobType, String name){
        //id=PsIdGenerator.generateId();
        this.jobType=jobType;
        this.name=name;
    }
    /**
     * check if type is matrix throughput type
     * @return 
     */
    public boolean isThroughput(){
        if(PsServiceTypeFactory.THROUGHPUT.equals(this.id)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * check if this is matrix latency type
     * @return 
     */
    public boolean isLatency(){
        if(PsServiceTypeFactory.LATENCY.equals(this.id)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * check if this is traceroute
     * @return 
     */
    public boolean isTraceroute(){
        if(PsServiceTypeFactory.TRACEROUTE.equals(this.id)){
            return true;
        }else{
            return false;
        }
    }
}
