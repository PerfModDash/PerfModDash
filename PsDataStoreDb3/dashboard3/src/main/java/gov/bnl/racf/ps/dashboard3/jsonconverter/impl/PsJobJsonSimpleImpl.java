/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsJobJson;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsJobJsonSimpleImpl implements PsJobJson{

    @Override
    public JSONObject toJson(PsJob psJob) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject toJson(PsJob psJob, String detailLevel) {
        return this.toJson(psJob);
    }

    @Override
    public JSONArray toJson(List<PsJob> listOfJobs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONArray toJson(List<PsJob> listOfJobs, String detailLevel) {
        return this.toJson(listOfJobs);
    }
    
}
