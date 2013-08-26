/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public interface PsMatrixJson {

    public JSONObject toJson(PsMatrix matrix);
    public JSONObject toJson(PsMatrix matrix, String detailLevel);
    
    public JSONArray toJson(List<PsMatrix> listOfMatrices, String detailLevel);
    public JSONArray toJson(List<PsMatrix> listOfMatrices);
    
}
