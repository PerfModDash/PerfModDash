/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsCloudJson {

    @Transactional
    public JSONObject toJson(PsCloud cloud);

    @Transactional
    public JSONObject toJson(PsCloud cloud, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsCloud> listOfClouds);

    @Transactional
    public JSONArray toJson(List<PsCloud> listOfClouds, String detailLevel);
}
