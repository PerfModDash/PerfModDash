/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;

import java.util.TreeMap;

/**
 * Implements the Job object as defined in the design document
 * //TODO this class needs some thinking, it is far from ready
 * I still need to discuss details with Andy
 * @author tomw
 */
public class PsJob {
    
    // field names in JSON objects
    public static final String ID="id";
    public static final String SERVICE_ID="service-id";
    public static final String TYPE="type";
    public static final String PARAMETERS="parameters";
    public static final String RUNNING="running";
    
    
    private String id="";
    private String service_id="";
    private String type="";
   
    
    private TreeMap<String,Object> parameters = new TreeMap<String,Object> ();
    
    boolean running = false;

    public String getId() {
        return id;
    }    

    public TreeMap<String,Object> getParameters() {
        return parameters;
    }

    public void setParameters(TreeMap<String,Object> parameters) {
        this.parameters = parameters;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Constructor. Does nothing except of generating unique job is
     */
    public PsJob(){
         id=PsIdGenerator.generateId();
    }
    
    
}
