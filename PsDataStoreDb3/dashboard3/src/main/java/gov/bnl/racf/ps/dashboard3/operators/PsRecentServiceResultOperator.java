/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsRecentServiceResultJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 * @author tomw
 */
public class PsRecentServiceResultOperator {

    //=== dependency injection ===//
    private PsRecentServiceResultJson psRecentServiceResultJson;

    public void setPsRecentServiceResultJson(PsRecentServiceResultJson psRecentServiceResultJson) {
        this.psRecentServiceResultJson = psRecentServiceResultJson;
    }

    //=== main code ===//
    /**
     * convert recent service result to JSON, default (high) detail level
     *
     * @param psRecentServiceResult
     * @return JSONObject
     */
    @Transactional
    public JSONObject toJson(PsRecentServiceResult psRecentServiceResult) {
        return this.toJson(psRecentServiceResult, PsParameters.DETAIL_LEVEL_HIGH);
    }

    /**
     * convert recent result to JSON at requested detail level
     *
     * @param psRecentServiceResult
     * @param detailLevel
     * @return
     */
    @Transactional
    public JSONObject toJson(PsRecentServiceResult psRecentServiceResult,
            String detailLevel) {
        return this.psRecentServiceResultJson.toJson(psRecentServiceResult, detailLevel);
    }

    /**
     * convert list of recent results into JSONArray at requested detail level
     *
     * @param listOfRecentResults
     * @param detailLevel
     * @return
     */
    @Transactional
    public JSONArray toJson(List<PsRecentServiceResult> listOfRecentResults,
            String detailLevel) {
        JSONArray listOfResultsJson = new JSONArray();
        for (PsRecentServiceResult recentResult : listOfRecentResults) {
            listOfResultsJson.add(this.toJson(listOfRecentResults, detailLevel));
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
    @Transactional
    public JSONArray toJson(List<PsRecentServiceResult> listOfRecentResults) {
        return this.toJson(listOfRecentResults, PsParameters.DETAIL_LEVEL_HIGH);
    }
}
