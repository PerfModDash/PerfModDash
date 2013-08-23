/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Interface for converting PsSite objects to JSON
 * @author tomw
 */
public interface PsSiteJson {
    public JSONObject toJson(PsSite site);
    public JSONObject toJson(PsSite site, String detailLevel);
    public JSONArray toJson(List<PsSite> listOfSites);
    public JSONArray toJson(List<PsSite> listOfSites,String detailLevel);
}
