/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceResult;
import java.util.Iterator;
import org.json.simple.JSONObject;

/**
 * convert JSON object to PsServiceResult object 
 * 
 * //TODO raise exceptions of conversion fails
 *
 * @author tomw
 */
public class Json2ServiceResultConverter {

    /**
     * convert a JSONObject into PsService result object
     * @param json
     * @return 
     */
    public static PsServiceResult convert(JSONObject json) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("requestLogger");
        
        logger.debug("we are in Json2ServiceResultConverter");
        
        
        PsServiceResult result = new PsServiceResult();


        if (json.keySet().contains(PsServiceResult.ID)) {
            result.setId(toInt( (String)json.get(PsServiceResult.ID) ));
        }

        if (json.keySet().contains(PsServiceResult.JOB_ID)) {
            result.setJob_id(toInt( (String)json.get(PsServiceResult.JOB_ID)));
        }


        if (json.keySet().contains(PsServiceResult.SERVICE_ID)) {
            result.setService_id(toInt((String) json.get(PsServiceResult.SERVICE_ID)));
        }

        if (json.keySet().contains(PsServiceResult.STATUS)) {
            int status = toInt( (Long) json.get(PsServiceResult.STATUS));
            result.setStatus(status);
        }

        if (json.keySet().contains(PsServiceResult.MESSAGE)) {
            result.setMessage((String) json.get(PsServiceResult.MESSAGE));
        }

        if (json.keySet().contains(PsServiceResult.TIME)) {
            logger.debug("we are in Json2ServiceResultConverter and we are about to convert time variable from ISO formato to java");
            logger.debug("time="+(String) json.get(PsServiceResult.TIME));
            result.setTime(IsoDateConverter.isoDate2Date((String) json.get(PsServiceResult.TIME)));
            logger.debug("time converted");
        }

        if (json.keySet().contains(PsServiceResult.PARAMETERS)) {
            JSONObject parametersAsJson =
                    (JSONObject) json.get(PsServiceResult.PARAMETERS);
            Iterator iter = parametersAsJson.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                Object val = (Object) parametersAsJson.get(key);
                result.setParameter(key, val);
            }
        }

        return result;
    }
    private static int toInt(Long inputLong){
        int result = inputLong.intValue();
        return result;
    }
    private static int toInt(String inputString){
        int inputAsInt = Integer.parseInt(inputString);
        return inputAsInt;
    }
}
