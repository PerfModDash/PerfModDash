/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsServiceResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class which creates job result objects
 *
 * @author tomw
 */
public class PsResultCreator {

    /**
     * create service result from a string representation of JSON object
     *
     * @param jsonAsString
     * @return
     */
    public static PsServiceResult create(String jsonAsString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonAsString);
        PsServiceResult result = PsResultCreator.create(json);
        return result;
    }

    /**
     * create service result from JSON object
     *
     * @param json
     */
    public static PsServiceResult create(JSONObject json) {
        PsServiceResult result = new PsServiceResult();
        
        PsObjectUpdater.update(result,json);
        
        return result;
    }
}
