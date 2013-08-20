/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * object which contains description of a parameter
 * @author tomw
 */
@Entity
public class PsParameterInfo implements Serializable{    
    
    // parameter names in JSON
    public static final String UNIT="unit";
    public static final String DESCRIPTION="description";
    
    @Id
    @GeneratedValue
    private int id;
    
    private String unit;
    private String description;

    public PsParameterInfo() {
        
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    /**
     * generate parameter description object
     * @param description
     * @param unit 
     */
    public PsParameterInfo(String description, String unit){
        this.unit=unit;
        this.description=description;
    }
    
    
    
}
