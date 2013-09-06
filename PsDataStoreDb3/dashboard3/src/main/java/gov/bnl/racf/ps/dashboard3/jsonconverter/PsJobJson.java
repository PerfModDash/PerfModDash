/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public interface PsJobJson {

    public JSONObject toJson(PsJob psJob);

    public JSONObject toJson(PsJob psJob, String detailLevel);

    public JSONArray toJson(List<PsJob> listOfJobs);

    public JSONArray toJson(List<PsJob> listOfJobs, String detailLevel);
}
