/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public interface PsServiceTypeJson {
    public JSONObject toJson(PsServiceType serviceType);
    public JSONObject toJson(PsServiceType serviceType, String detailLevel);
    public JSONArray toJson(List<PsServiceType> listOfServiceTypes);
    public JSONArray toJson(List<PsServiceType> listOfServiceTypes,String detailLevel);
}
