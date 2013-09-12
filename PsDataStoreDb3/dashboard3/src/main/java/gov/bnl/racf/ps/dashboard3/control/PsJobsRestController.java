/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsJob;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsJobJson;
import gov.bnl.racf.ps.dashboard3.operators.PsJobOperator;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * controller for PsJobs
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/jobs")
public class PsJobsRestController {

    // === Dependency injection ===//
    @Autowired
    private PsJobOperator psJobOperator;

    public void setPsJobOperator(PsJobOperator psJobOperator) {
        this.psJobOperator = psJobOperator;
    }
   

    //=== Main methods ===//
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String jobsGet(
            //@RequestParam(value = PsParameters.ID, required = false) int id,
            @RequestParam(value = PsParameters.SET_RUNNING, required = false) String setRunningString,
            @RequestParam(value = PsParameters.RUNNING, required = false) String runningString) {
        // unpack and validate input parameters
        boolean setRunning = false;
        if ("1".equals(setRunningString)) {
            setRunning = true;
        }
        boolean running = true;
        if ("0".equals(runningString)) {
            running = false;
        }

        List<PsJob> listOfJobs = this.psJobOperator.getJobs(running, setRunning);

        JSONArray listOfJobsJson = this.psJobOperator.toJson(listOfJobs);

        JSONObject jsonObjectWrapper = new JSONObject();
        jsonObjectWrapper.put("jobs", listOfJobsJson);

        return jsonObjectWrapper.toString();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String jobGetById(@PathVariable int id ) {
        PsJob psJob = this.psJobOperator.getJobById(id);
        
        JSONObject psJobAsJson = this.psJobOperator.toJson(psJob);
        JSONArray listOfJobsJson = new JSONArray();
        listOfJobsJson.add(psJobAsJson);
        
        JSONObject jsonObjectWrapper = new JSONObject();
        jsonObjectWrapper.put("jobs", listOfJobsJson);

        return jsonObjectWrapper.toString();
    }
}
