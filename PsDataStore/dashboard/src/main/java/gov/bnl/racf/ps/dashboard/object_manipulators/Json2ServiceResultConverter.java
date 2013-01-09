/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsServiceResult;
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
        PsServiceResult result = new PsServiceResult();


        if (json.keySet().contains(PsServiceResult.ID)) {
            result.setId((String) json.get(PsServiceResult.ID));
        }

        if (json.keySet().contains(PsServiceResult.JOB_ID)) {
            result.setJob_id((String) json.get(PsServiceResult.JOB_ID));
        }


        if (json.keySet().contains(PsServiceResult.SERVICE_ID)) {
            result.setService_id((String) json.get(PsServiceResult.SERVICE_ID));
        }

        if (json.keySet().contains(PsServiceResult.STATUS)) {
            Long statusLong = (Long) json.get(PsServiceResult.STATUS);
            int status = statusLong.intValue();
            result.setStatus(status);
        }

        if (json.keySet().contains(PsServiceResult.MESSAGE)) {
            result.setMessage((String) json.get(PsServiceResult.MESSAGE));
        }

        if (json.keySet().contains(PsServiceResult.TIME)) {
            result.setTime(IsoDateConverter.isoDate2Date((String) json.get(PsServiceResult.TIME)));
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
}
