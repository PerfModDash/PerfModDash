/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;

/**
 * object which contains description of a parameter
 * @author tomw
 */
public class PsParameterInfo {
    
    // parameter names in JSON
    public static final String UNIT="unit";
    public static final String DESCRIPTION="description";
    
    private String unit;
    private String description;

    /**
     * Empty constructor
     */
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
