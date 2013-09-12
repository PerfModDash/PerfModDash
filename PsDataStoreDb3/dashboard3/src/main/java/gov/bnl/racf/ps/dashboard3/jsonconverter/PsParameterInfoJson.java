/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsParameterInfo;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * Convert PsParemeterInfo objects to Json
 *
 * @author tomw
 */
public interface PsParameterInfoJson {

    @Transactional
    public JSONObject toJson(PsParameterInfo parameterInfo);

    @Transactional
    public JSONObject toJson(PsParameterInfo parameterInfo, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsParameterInfo> listOfParameters);

    @Transactional
    public JSONArray toJson(List<PsParameterInfo> listOfParameters, String detailLevel);
}
