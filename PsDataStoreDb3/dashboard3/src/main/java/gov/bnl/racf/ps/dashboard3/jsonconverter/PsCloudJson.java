/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public interface PsCloudJson {
     public JSONObject toJson(PsCloud cloud);
    public JSONObject toJson(PsCloud cloud, String detailLevel);
    public JSONArray toJson(List<PsCloud> listOfClouds);
    public JSONArray toJson(List<PsCloud> listOfClouds,String detailLevel);
}
