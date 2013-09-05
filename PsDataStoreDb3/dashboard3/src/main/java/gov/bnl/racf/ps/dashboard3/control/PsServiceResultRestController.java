/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

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

    
    
    private PsServiceOperator psServiceOperator;
    public void setPsServiceOperator(PsServiceOperator psServiceOperator) {
        this.psServiceOperator = psServiceOperator;
    }
    
   
    
    

    // === methods ===//
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String resultsGet(
            @RequestParam(value = PsParameters.SERVICE_HISTORY_TMIN, required = false) String tMin,
            @RequestParam(value = PsParameters.SERVICE_HISTORY_TMAX, required = false) String tMax) {
        JSONObject resultsCountJson = this.psServiceResultOperator.getResultsCount(tMin, tMax);
        return resultsCountJson.toString();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String resultsPost(@RequestBody String requestBody) {
        String message="";
        try {
            // first upload the result and get the corresponding service
            PsService service = this.psServiceResultOperator.uploadResult(requestBody);
            
            // second convert the service to json
            JSONObject serviceJson = 
                    this.psServiceOperator.toJson(service, PsParameters.DETAIL_LEVEL_HIGH);
            
            return serviceJson.toString();
            
        } catch (ParseException ex) {
            message=message+" error when parsing request body "+requestBody;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return message;
        } catch (PsServiceNotFoundException ex) {
            message=message+" result corresponds to service which was not found "+requestBody;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsServiceResultRestController.class.getName()).log(Level.SEVERE, null, ex);
            return message;
        }
    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String resultsDelete(
            @RequestParam(value = PsParameters.SERVICE_HISTORY_TMIN, required = false) String tMin,
            @RequestParam(value = PsParameters.SERVICE_HISTORY_TMAX, required = false) String tMax
            ) {
        JSONObject resultObject  = this.psServiceResultOperator.deleteResults(tMin,tMax);
        
        return resultObject.toString();
    }
}
