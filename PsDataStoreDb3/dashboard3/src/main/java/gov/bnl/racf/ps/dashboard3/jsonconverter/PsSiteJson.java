/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface for converting PsSite objects to JSON
 *
 * @author tomw
 */
public interface PsSiteJson {

    @Transactional
    public JSONObject toJson(PsSite site);

    @Transactional
    public JSONObject toJson(PsSite site, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsSite> listOfSites);

    @Transactional
    public JSONArray toJson(List<PsSite> listOfSites, String detailLevel);
}
