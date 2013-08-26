/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsMatrixJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsMatrixJsonSimpleImpl implements PsMatrixJson{

    /**
     * convert matrix to JSONObject
     * @param matrix
     * @return 
     */
    @Override
    public JSONObject toJson(PsMatrix matrix) {
        return this.toJson(matrix, PsParameters.DETAIL_LEVEL_LOW);
    }

    /**
     * convert matrix to JSONObject using requested detail level
     * @param matrix
     * @param detailLevel
     * @return 
     */
    @Override
    public JSONObject toJson(PsMatrix matrix, String detailLevel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * convert list of matrices to JSONArray using requested detail level
     * @param listOfMatrices
     * @param detailLevel
     * @return 
     */
    @Override
    public JSONArray toJson(List<PsMatrix> listOfMatrices, String detailLevel) {
        JSONArray resultList = new JSONArray();
        for(PsMatrix matrix : listOfMatrices){
            resultList.add(this.toJson(listOfMatrices, detailLevel));
        }
        return resultList;
    }

    /**
     * convert list of matrices to JSONArray
     * @param listOfMatrices
     * @return 
     */
    @Override
    public JSONArray toJson(List<PsMatrix> listOfMatrices) {
        JSONArray resultList = new JSONArray();
        for(PsMatrix matrix : listOfMatrices){
            resultList.add(this.toJson(listOfMatrices));
        }
        return resultList;
    }
    
}
