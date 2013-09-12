/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsJobJson {

    @Transactional
    public JSONObject toJson(PsJob psJob);

    @Transactional
    public JSONObject toJson(PsJob psJob, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsJob> listOfJobs);

    @Transactional
    public JSONArray toJson(List<PsJob> listOfJobs, String detailLevel);
}
