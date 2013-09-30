/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.dao.sessionimpl.SessionStore;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceNotFoundException;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceJson;
import gov.bnl.racf.ps.dashboard3.operators.PsServiceOperator;
import gov.bnl.racf.ps.dashboard3.operators.PsServiceResultOperator;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/results")
public class PsServiceResultRestController {
    // === dependency injection ===//

    @Autowired
    private PsServiceResultOperator psServiceResultOperator;

    public void setPsServiceResultOperator(PsServiceResultOperator psServiceResultOperator) {
        this.psServiceResultOperator = psServiceResultOperator;
    }
    @Autowired
    SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
    
    @Autowired
    private PsServiceOperator psServiceOperator;

    public void setPsServiceOperator(PsServiceOperator psServiceOperator) {
        this.psServiceOperator = psServiceOperator;
    }

    // === methods ===//
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String resultsGet(
            @RequestParam(value = PsParameters.SERVICE_HISTORY_TMIN, required = false) String tMin,
            @RequestParam(value = PsParameters.SERVICE_HISTORY_TMAX, required = false) String tMax) {

        sessionStore.start();

        JSONObject resultsCountJson = this.psServiceResultOperator.getResultsCount(tMin, tMax);

        sessionStore.commit();

        return resultsCountJson.toString();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String resultsPost(@RequestBody String requestBody) {
        String message = "";
        try {
            sessionStore.start();

            // first upload the result and get the corresponding service
            PsService service = this.psServiceOperator.uploadResult(requestBody);

            // second convert the service to json
            JSONObject serviceJson =
                    this.psServiceOperator.toJson(service, PsParameters.DETAIL_LEVEL_HIGH);

            sessionStore.commit();

            return serviceJson.toString();

        } catch (ParseException ex) {
            message = message + " error when parsing request body " + requestBody;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsServiceNotFoundException ex) {
            message = message + " result corresponds to service which was not found " + requestBody;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsServiceResultRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public String resultsDelete(
            @RequestParam(value = PsParameters.SERVICE_HISTORY_TMIN, required = false) String tMin,
            @RequestParam(value = PsParameters.SERVICE_HISTORY_TMAX, required = false) String tMax) {

        sessionStore.start();
        JSONObject resultObject = this.psServiceResultOperator.deleteResults(tMin, tMax);
        sessionStore.commit();

        return resultObject.toString();
    }
}
