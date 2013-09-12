/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsMatrixJson {

    @Transactional
    public JSONObject toJson(PsMatrix matrix);

    @Transactional
    public JSONObject toJson(PsMatrix matrix, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsMatrix> listOfMatrices, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsMatrix> listOfMatrices);
}
