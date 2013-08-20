/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsParameterInfo;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsParameterInfoJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceTypeJson;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Convert the PsServiceType objects to json and back
 *
 * @author tomw
 */
public class PsServiceTypeJsonSimpleImpl implements PsServiceTypeJson {

    private PsParameterInfoJson psParameterInfoJson;

    public void setPsParameterInfoJson(PsParameterInfoJson psParameterInfoJson) {
        this.psParameterInfoJson = psParameterInfoJson;
    }
    
    @Override
    public JSONObject toJson(PsServiceType serviceType) {
        JSONObject json = new JSONObject();
        // warning: externally id means 'service type id'
        json.put(PsServiceType.ID, serviceType.getServiceTypeId());
        // internal id is the id in the database
        json.put(PsServiceType.INTERNAL_ID, int2String(serviceType.getId()));
        json.put(PsServiceType.JOB_TYPE, serviceType.getJobType());
        json.put(PsServiceType.NAME, serviceType.getName());

        // get serviceParameterInfo
        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                serviceType.getServiceParameterInfo();
        JSONObject serviceParameterInfoJson = new JSONObject();
        if (serviceParameterInfo != null) {
            Iterator keyIterator = serviceParameterInfo.keySet().iterator();
            while (keyIterator.hasNext()) {
                String currentParameterName = (String) keyIterator.next();
                PsParameterInfo currentParameterInfo =
                        serviceParameterInfo.get(currentParameterName);
                JSONObject currentParameterInfoAsJson = 
                        this.psParameterInfoJson.toJson(currentParameterInfo);
                serviceParameterInfoJson.put(currentParameterName, currentParameterInfoAsJson);
            }
        }
        json.put(PsServiceType.SERVICE_PARAMETER_INFO, serviceParameterInfoJson);

        // get result parameter info 
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                serviceType.getResultParameterInfo();
        JSONObject resultParameterInfoJson = new JSONObject();

        if (resultParameterInfo != null) {
            Iterator keyIterator2 = resultParameterInfo.keySet().iterator();
            while (keyIterator2.hasNext()) {
                String currentParameterName = (String) keyIterator2.next();
                PsParameterInfo currentParameterInfo =
                        resultParameterInfo.get(currentParameterName);
                JSONObject currentParameterInfoAsJson = 
                        this.psParameterInfoJson.toJson(currentParameterInfo);
                resultParameterInfoJson.put(currentParameterName, currentParameterInfoAsJson);
            }
        }
        json.put(PsServiceType.RESULT_PARAMETER_INFO, resultParameterInfoJson);
        return json;
    }

    @Override
    public JSONObject toJson(PsServiceType serviceType, String detailLevel) {
        // we ignore the detailLevel parameter
        return this.toJson(serviceType);
    }

    @Override
    public JSONArray toJson(List<PsServiceType> listOfServiceTypes) {
        JSONArray jsonArray = new JSONArray();
        Iterator iter  = listOfServiceTypes.iterator();
        while(iter.hasNext()){
            PsServiceType serviceType = (PsServiceType)iter.next();
            JSONObject serviceTypeJson = this.toJson(serviceType);
            jsonArray.add(serviceTypeJson);
        }
        return jsonArray;
    }

    @Override
    public JSONArray toJson(List<PsServiceType> listOfServiceTypes, String detailLevel) {
        JSONArray resultList = new JSONArray();
        Iterator iter = listOfServiceTypes.iterator();
        while(iter.hasNext()){
            PsServiceType serviceType = (PsServiceType)iter.next();
            resultList.add(this.toJson(serviceType,detailLevel));
        }
        return resultList;
    }
    
    public static String int2String(int intValue) {
        Integer integerVariable = new Integer(intValue);
        return integerVariable.toString();
    }
    
    
}
