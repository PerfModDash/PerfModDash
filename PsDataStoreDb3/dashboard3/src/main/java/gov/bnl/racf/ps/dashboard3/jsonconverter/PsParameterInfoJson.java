/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsParameterInfo;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Convert PsParemeterInfo objects to Json
 * @author tomw
 */
public interface PsParameterInfoJson {
    public JSONObject toJson(PsParameterInfo parameterInfo);
    public JSONObject toJson(PsParameterInfo parameterInfo, String detailLevel);
    public JSONArray toJson(List<PsParameterInfo> listOfParameters);
    public JSONArray toJson(List<PsParameterInfo> listOfParameters,String detailLevel);
}
