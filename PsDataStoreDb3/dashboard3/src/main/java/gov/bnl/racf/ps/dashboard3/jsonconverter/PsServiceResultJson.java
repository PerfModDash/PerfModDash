/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * interface for converting service results to json
 *
 * @author tomw
 */
public interface PsServiceResultJson {

    @Transactional
    public JSONObject toJson(PsServiceResult psServiceResult);

    @Transactional
    public JSONObject toJson(PsServiceResult psServiceResult,
            String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsServiceResult> listOfResults,
            String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsServiceResult> listOfResults);
}
