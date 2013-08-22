/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsRecentServiceResultJson;
import gov.bnl.racf.utils.IsoDateConverter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Converts PsRecentServiceResult objects to JSON representation
 * @author tomw
 */
public class PsRecentServiceResultJsonSimpleImpl implements PsRecentServiceResultJson{

    @Override
    public JSONObject toJson(PsRecentServiceResult result) {
        JSONObject json = new JSONObject();

        if (result != null) {
            json.put(PsRecentServiceResult.ID, ""+result.getId());
            json.put(PsRecentServiceResult.SERVICE_RESULT_ID, ""+result.getServiceResultId());
            json.put(PsRecentServiceResult.JOB_ID, ""+result.getJob_id());
            json.put(PsRecentServiceResult.SERVICE_ID, ""+result.getService_id());
            json.put(PsRecentServiceResult.STATUS, result.getStatus());
            json.put(PsRecentServiceResult.MESSAGE, result.getMessage());

            json.put(PsRecentServiceResult.TIME, IsoDateConverter.dateToString(result.getTime()));

            JSONObject parameters = new JSONObject();
            TreeMap<String, Object> treeMap = result.getParameters();
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                parameters.put(key, value);
            }
            json.put(PsRecentServiceResult.PARAMETERS, parameters);

        }

        return json;
    }

    /**
     * convert recent service result to JSON according to detailLevel. The detailLevel parameter is dummy - for now.
     * @param psRecentServiceResult
     * @param detailLevel
     * @return 
     */
    @Override
    public JSONObject toJson(PsRecentServiceResult psRecentServiceResult, String detailLevel) {
        return this.toJson(psRecentServiceResult);
    }

    /**
     * convert a list of recent results to JSONArray. The detailLevel parameter is ignored - for now.
     * @param listOfResults
     * @param detailLevel
     * @return 
     */
    @Override
    public JSONArray toJson(List<PsRecentServiceResult> listOfResults, String detailLevel) {
        return this.toJson(listOfResults);
    }

    /**
     * convert a list of recent results to JSONArray
     * @param listOfResults
     * @return 
     */
    @Override
    public JSONArray toJson(List<PsRecentServiceResult> listOfResults) {
        JSONArray resultJsonArray = new JSONArray();
        for(PsRecentServiceResult psRecentServiceResult : listOfResults){
            resultJsonArray.add(this.toJson(psRecentServiceResult));
        }
        return resultJsonArray;
    }
    
}
