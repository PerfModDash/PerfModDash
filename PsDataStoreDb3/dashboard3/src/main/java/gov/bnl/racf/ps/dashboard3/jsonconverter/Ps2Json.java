/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.objects.PsHost;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public interface Ps2Json {
    public JSONObject toJson(PsHost host);
    public JSONObject toJson(PsHost host, String detailLevel);
    public JSONArray toJson(List<PsHost> listOfHosts);
    public JSONArray toJson(List<PsHost> listOfHosts,String detailLevel);
}
