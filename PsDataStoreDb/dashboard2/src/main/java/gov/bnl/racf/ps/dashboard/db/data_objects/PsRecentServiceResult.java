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
public class PsRecentServiceResult implements Serializable {
    // field names in JSON
    public static final String ID=PsServiceResult.ID;
    public static final String SERVICE_RESULT_ID="service_result_id";
    public static final String JOB_ID=PsServiceResult.JOB_ID;
    public static final String SERVICE_ID=PsServiceResult.SERVICE_ID;
    public static final String STATUS=PsServiceResult.STATUS;
    public static final String MESSAGE=PsServiceResult.MESSAGE;
    public static final String TIME=PsServiceResult.TIME;
    public static final String PARAMETERS=PsServiceResult.PARAMETERS;
    
    
    @Id
    @GeneratedValue
    private int id;
    private int job_id;
    private int serviceResultId;
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

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
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

    public int getServiceResultId() {
        return serviceResultId;
    }

    public void setServiceResultId(int serviceResultId) {
        this.serviceResultId = serviceResultId;
    }

    
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PsRecentServiceResult other = (PsRecentServiceResult) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
