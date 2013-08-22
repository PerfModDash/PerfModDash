/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceResultJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import gov.bnl.racf.utils.IsoDateConverter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsServiceResultJsonSimpleImpl implements PsServiceResultJson {

    /**
     * convert service result to JSON, default (high) detail level
     *
     * @param psServiceResult
     * @return JSONObject
     */
    public JSONObject toJson(PsServiceResult psServiceResult) {
        return this.toJson(psServiceResult, PsParameters.DETAIL_LEVEL_HIGH);
    }

    /**
     * convert recent result to JSON at requested detail level
     *
     * @param psRecentServiceResult
     * @param detailLevel
     * @return
     */
    public JSONObject toJson(PsServiceResult result,
            String detailLevel) {
        JSONObject json = new JSONObject();

        if (result != null) {
            json.put(PsServiceResult.ID, result.getId()+"");
            json.put(PsServiceResult.JOB_ID, result.getJob_id()+"");
            json.put(PsServiceResult.SERVICE_ID, result.getService_id()+"");
            json.put(PsServiceResult.STATUS, result.getStatus());
            json.put(PsServiceResult.MESSAGE, result.getMessage());

            json.put(PsServiceResult.TIME, IsoDateConverter.dateToString(result.getTime()));

            JSONObject parameters = new JSONObject();
            TreeMap<String, Object> treeMap = result.getParameters();
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                parameters.put(key, value);
            }
            json.put(PsServiceResult.PARAMETERS, parameters);

        }

        return json;
        
    }

    /**
     * convert list of recent results into JSONArray at requested detail level
     *
     * @param listOfRecentResults
     * @param detailLevel
     * @return
     */
    public JSONArray toJson(List<PsServiceResult> listOfResults,
            String detailLevel) {
        JSONArray listOfResultsJson = new JSONArray();
        for (PsServiceResult recentResult : listOfResults) {
            listOfResultsJson.add(this.toJson(listOfResults, detailLevel));
        }
        return listOfResultsJson;
    }

    /**
     * convert list of recent results into JSONArray at default (high) detail
     * level
     *
     * @param listOfRecentResults
     * @return
     */
    public JSONArray toJson(List<PsServiceResult> listOfResults) {
        return this.toJson(listOfResults, PsParameters.DETAIL_LEVEL_HIGH);
    }
}
