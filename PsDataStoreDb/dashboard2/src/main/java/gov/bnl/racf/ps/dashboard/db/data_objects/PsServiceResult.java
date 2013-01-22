/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.data_objects;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * object representing result of a ps service
 * @author tomw
 */
@Cacheable
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class PsServiceResult implements Serializable {
    // field names in JSON
    public static final String ID="id";
    public static final String JOB_ID="job-id";
    public static final String SERVICE_ID="service-id";
    public static final String STATUS="status";
    public static final String MESSAGE="message";
    public static final String TIME="time";
    public static final String PARAMETERS="parameters";
    
    
    @Id
    @GeneratedValue
    private int id;
    private String job_id;
    private int service_id;

   
    private int status;
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    private TreeMap<String,Object> parameters = new TreeMap<String,Object>();
    
    
     public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TreeMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(TreeMap<String, Object> parameters) {
        this.parameters = parameters;
    }
    public void setParameter(String parameterName, Object parameterValue){
        this.parameters.put(parameterName, parameterValue);
    }
    public Object getParameter(String parameterName){
        return this.parameters.get(parameterName);
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PsServiceResult other = (PsServiceResult) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    
    
    
    
    
}
