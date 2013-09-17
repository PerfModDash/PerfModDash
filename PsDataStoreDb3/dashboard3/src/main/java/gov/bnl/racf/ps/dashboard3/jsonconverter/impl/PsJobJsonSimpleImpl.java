/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsJobJson;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsJobJsonSimpleImpl implements PsJobJson {

    @Transactional
    @Override
    public JSONObject toJson(PsJob psJob) {
        JSONObject json = new JSONObject();

        json.put(PsJob.ID, psJob.getId()+"");
        json.put(PsJob.SERVICE_ID, psJob.getService_id()+"");
        json.put(PsJob.TYPE, psJob.getType());

        JSONObject parameters = serviceParametersAsJson(psJob);
        json.put(PsJob.PARAMETERS, parameters);

        return json;
    }
    
    private  JSONObject serviceParametersAsJson(PsJob job) {
        JSONObject serviceParameters = new JSONObject();

        Iterator iter = job.getParameters().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();

            Object value = job.getParameters().get(key);
            serviceParameters.put(key, value);
        }
        return serviceParameters;
    }

    @Transactional
    @Override
    public JSONObject toJson(PsJob psJob, String detailLevel) {
        return this.toJson(psJob);
    }

    @Transactional
    @Override
    public JSONArray toJson(List<PsJob> listOfJobs) {
        JSONArray listOfJobsJson = new JSONArray();
        Iterator iter = listOfJobs.iterator();
        while(iter.hasNext()){
            PsJob job = (PsJob)iter.next();
            JSONObject jobJson = this.toJson(job);
            listOfJobsJson.add(jobJson);
        }
        return listOfJobsJson;
    }

    @Transactional
    @Override
    public JSONArray toJson(List<PsJob> listOfJobs, String detailLevel) {
        return this.toJson(listOfJobs);
    }
}
