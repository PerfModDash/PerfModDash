/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceResultJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.List;
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
    public JSONObject toJson(PsServiceResult psServiceResult,
            String detailLevel) {
        throw new UnsupportedOperationException("method not implemented yet");
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
