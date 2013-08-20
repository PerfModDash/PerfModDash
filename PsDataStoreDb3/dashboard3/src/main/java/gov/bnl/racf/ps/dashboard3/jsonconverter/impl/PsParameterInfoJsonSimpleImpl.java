/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsParameterInfo;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsParameterInfoJson;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsParameterInfoJsonSimpleImpl implements PsParameterInfoJson {

    /**
     * convert parameter info object to json
     * @param parameterInfo
     * @return 
     */
    @Override
    public JSONObject toJson(PsParameterInfo parameterInfo) {
        JSONObject json = new JSONObject();
        json.put(PsParameterInfo.DESCRIPTION, parameterInfo.getDescription());
        json.put(PsParameterInfo.UNIT, parameterInfo.getUnit());
        return json;
    }

    /**
     * convert parameter info object to json
     * The detail level parameter is ignored at this stage
     * @param parameterInfo
     * @param detailLevel
     * @return 
     */
    @Override
    public JSONObject toJson(PsParameterInfo parameterInfo, String detailLevel) {
        return this.toJson(parameterInfo);
    }

    /**
     * convert list of parameters to JSONArray
     * @param listOfParameters
     * @return 
     */
    @Override
    public JSONArray toJson(List<PsParameterInfo> listOfParameters) {
        JSONArray parameterListjson = new JSONArray();
        Iterator iter = listOfParameters.iterator();
        while (iter.hasNext()) {
            PsParameterInfo parameterInfo = (PsParameterInfo) iter.next();
            parameterListjson.add(this.toJson(parameterInfo));
        }
        return parameterListjson;
    }

    /**
     * convert list of parameters to JSONArray
     * @param listOfParameters
     * @return 
     */
    @Override
    public JSONArray toJson(List<PsParameterInfo> listOfParameters, String detailLevel) {
        JSONArray parameterListjson = new JSONArray();
        Iterator iter = listOfParameters.iterator();
        while (iter.hasNext()) {
            PsParameterInfo parameterInfo = (PsParameterInfo) iter.next();
            parameterListjson.add(this.toJson(parameterInfo, detailLevel));
        }
        return parameterListjson;
    }
}
