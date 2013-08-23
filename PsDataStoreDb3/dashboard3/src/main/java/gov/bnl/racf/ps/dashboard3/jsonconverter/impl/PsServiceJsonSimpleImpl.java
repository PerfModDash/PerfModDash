/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsRecentServiceResultJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceResultJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import gov.bnl.racf.utils.IsoDateConverter;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsServiceJsonSimpleImpl implements PsServiceJson{

    

    
    // ---  dependency injection part --- //
    private PsRecentServiceResultJson psRecentServiceResultJson;

    public void setPsRecentServiceResultJson(PsRecentServiceResultJson psRecentServiceResultJson) {
        this.psRecentServiceResultJson = psRecentServiceResultJson;
    }
    
    // --- main code of the class ---//
    
    /**
     * convert service to JSON
     * @param service
     * @return 
     */
    @Transactional
    @Override
    public JSONObject toJson(PsService service) {
        return this.toJson(service, PsParameters.DETAIL_LEVEL_LOW);
    }

    /**
     * convert service to JSON using requested detail level
     * @param service
     * @param detailLevel
     * @return 
     */
    @Transactional
    @Override
    public JSONObject toJson(PsService service, String detailLevel) {
        JSONObject json = new JSONObject();

        if (service != null) {

            json.put(PsService.ID, ""+service.getId());
            json.put(PsService.NAME, service.getName());

            if (!PsParameters.DETAIL_LEVEL_LOW.equals(detailLevel)) {

                json.put(PsService.TYPE, service.getType());
                json.put(PsService.DESCRIPTION, service.getDescription());

                // add service parameters

                if (service.getParameters() != null) {
                    JSONObject serviceParameters = serviceParametersAsJson(service);
                    json.put(PsService.PARAMETERS, serviceParameters);
                }


                json.put(PsService.CHECK_INTERVAL, new Integer(service.getCheckInterval()));
                json.put(PsService.RUNNING, service.isRunning());
                json.put(PsService.PREV_CHECK_TIME,
                        IsoDateConverter.dateToString(service.getPrevCheckTime()));
                json.put(PsService.NEXT_CHECK_TIME,
                        IsoDateConverter.dateToString(service.getNextCheckTime()));
                json.put(PsService.RUNNING_SINCE,
                        IsoDateConverter.dateToString(service.getRunningSince()));
                json.put(PsService.TIMEOUT, service.getTimeout());


                // add result
                PsRecentServiceResult result = service.getResult();
                JSONObject jsonResult = this.psRecentServiceResultJson.toJson(result);
                json.put(PsService.RESULT, jsonResult);
            }

        }

        return json;
    }

    /**
     * convert list of services to JSONArray
     * @param listOfServices
     * @return 
     */
    @Transactional
    @Override
    public JSONArray toJson(List<PsService> listOfServices) {
        JSONArray jsonArray=new JSONArray();
        
        for(PsService psService : listOfServices){
            jsonArray.add(this.toJson(psService));
        }
        
        return jsonArray;
    }

    /**
     * convert list of services to JSONArray using requested detail level
     * @param listOfServices
     * @param detailLevel
     * @return 
     */
    @Transactional
    @Override
    public JSONArray toJson(List<PsService> listOfServices, String detailLevel) {
        JSONArray jsonArray=new JSONArray();
        
        for(PsService psService : listOfServices){
            jsonArray.add(this.toJson(psService, PsParameters.DETAIL_LEVEL_LOW));
        }
        
        return jsonArray;
    }

    @Transactional
    @Override
    public JSONObject serviceParametersAsJson(PsService service) {
       JSONObject serviceParameters = new JSONObject();

        Iterator iter = service.getParameters().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();

            Object value = service.getParameters().get(key);

            //check if parameter name indicates that it is an object id
            if (thisIsObjectId(key)) {
                serviceParameters.put(key, value + "");
            } else {
                serviceParameters.put(key, value);
            }
        }
        return serviceParameters;
    }
    
    
    //--- private utility methods ---//
    //TODO add the PsMatrix part when working on psmatrix
    private static boolean thisIsObjectId(String key) {

        if (PsService.PARAMETER_HOST_ID.equals(key)) {
            return true;
        } else {
//            if (PsMatrix.PARAMETER_DESTINATION_HOST_ID.equals(key)) {
//                return true;
//            } else {
//                if (PsMatrix.PARAMETER_SOURCE_HOST_ID.equals(key)) {
//                    return true;
//                } else {
//                    if (PsMatrix.PARAMETER_MA_HOST_ID.equals(key)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
        }
        return false;
    }
}
