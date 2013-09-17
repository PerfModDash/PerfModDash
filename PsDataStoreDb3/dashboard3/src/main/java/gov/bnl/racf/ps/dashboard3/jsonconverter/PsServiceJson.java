/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * Convert PsService to JOSN 
 *
 * @author tomw
 */
public interface PsServiceJson {

    @Transactional
    public JSONObject toJson(PsService service);

    @Transactional
    public JSONObject toJson(PsService service, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsService> listOfServices);

    @Transactional
    public JSONArray toJson(List<PsService> listOfServices, String detailLevel);

    @Transactional
    public JSONObject serviceParametersAsJson(PsService service);
}
