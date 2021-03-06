/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsRecentServiceResultJson {

    @Transactional
    public JSONObject toJson(PsRecentServiceResult psRecentServiceResult);

    @Transactional
    public JSONObject toJson(PsRecentServiceResult psRecentServiceResult,
            String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsRecentServiceResult> listOfResults,
            String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsRecentServiceResult> listOfResults);
}
