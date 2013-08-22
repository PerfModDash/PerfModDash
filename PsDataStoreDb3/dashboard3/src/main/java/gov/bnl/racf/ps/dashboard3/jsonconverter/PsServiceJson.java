/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Convert PsService to JOSN
 * //TODO provide implementation of this class
 * @author tomw
 */
public interface PsServiceJson {
    public JSONObject toJson(PsService service);
    public JSONObject toJson(PsService service, String detailLevel);
    public JSONArray toJson(List<PsService> listOfServices);
    public JSONArray toJson(List<PsService> listOfServices,String detailLevel);
    
    public JSONObject serviceParametersAsJson(PsService service);
}
