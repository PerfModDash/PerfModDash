/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * //TODO implement this class
 * @author tomw
 */
public class PsRecentServiceResultOperator {
    /**
     * convert recent service result to JSON, default (high) detail level
     * @param psRecentServiceResult
     * @return JSONObject
     */
    public JSONObject toJson(PsRecentServiceResult psRecentServiceResult){
        return this.toJson(psRecentServiceResult, PsParameters.DETAIL_LEVEL_HIGH);
    }
    /**
     * convert recent result to JSON at requested detail level
     * @param psRecentServiceResult
     * @param detailLevel
     * @return 
     */
    public JSONObject toJson(PsRecentServiceResult psRecentServiceResult,
            String detailLevel){
        throw new UnsupportedOperationException("method not implemented yet");
    }
    
    /**
     * convert list of recent results into JSONArray at requested detail level
     * @param listOfRecentResults
     * @param detailLevel
     * @return 
     */
    public JSONArray toJson(List<PsRecentServiceResult> listOfRecentResults,
            String detailLevel){
        JSONArray listOfResultsJson = new JSONArray();
        for(PsRecentServiceResult recentResult : listOfRecentResults){
            listOfResultsJson.add(this.toJson(listOfRecentResults, detailLevel));
        }
        return listOfResultsJson;
    }
    /**
     * convert list of recent results into JSONArray at default (high) detail level
     * @param listOfRecentResults
     * @return 
     */
    public JSONArray toJson(List<PsRecentServiceResult> listOfRecentResults){
        return this.toJson(listOfRecentResults, PsParameters.DETAIL_LEVEL_HIGH);
    }
}
