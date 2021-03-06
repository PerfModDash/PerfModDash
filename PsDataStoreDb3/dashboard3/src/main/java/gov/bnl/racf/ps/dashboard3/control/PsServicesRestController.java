/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.dao.sessionimpl.SessionStore;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.operators.PsServiceOperator;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
@RequestMapping(value = "/services")
public class PsServicesRestController {

    // --- dependency injection ---//
    @Autowired
    private PsServiceOperator psServiceOperator;

    public void setPsServiceOperator(PsServiceOperator psServiceOperator) {
        this.psServiceOperator = psServiceOperator;
    }
    
    
    @Autowired
    SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    // --- main code starts here ---//
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String serviceGet() {
        sessionStore.start();
        
        List<PsService> listOfServices = this.psServiceOperator.getAll();
        JSONArray resultsJson =
                this.psServiceOperator.toJson(listOfServices, PsParameters.DETAIL_LEVEL_LOW);
        sessionStore.commit();
        return resultsJson.toString();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String servicesGetById(
            @PathVariable int id,
            @RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        try {
            sessionStore.start();
            
            PsService service = this.psServiceOperator.getById(id);
            if (detailLevel == null) {
                detailLevel = PsParameters.DETAIL_LEVEL_HIGH;
            }
            
            sessionStore.commit();
            return this.psServiceOperator.toJson(service, detailLevel).toString();

        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(PsServicesRestController.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(PsServicesRestController.class.getName()).log(Level.SEVERE, null, "service with id=" + id + " not found");
            sessionStore.rollback();
            throw new RuntimeException("service with id=" + id + " not found");
        }
    }

    /**
     * method to create new service. However in normal circumstances users are
     * not supposed to call this methid: all service creations should be handled
     * by high level host and matrix operations. This method is for experts only
     * and in the future we may require that only authorized users use it.
     *
     * @param requestBody
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String servicePost(@RequestBody String requestBody) {

        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            sessionStore.start();
            
            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert host object based on the request
            JSONObject jsonOutput = this.psServiceOperator.insertNewServiceFromJson(jsonInput);
            
            sessionStore.commit();
            
            // finally return the newly created host
            return jsonOutput.toString();

        } catch (ParseException ex) {
            String message = "Incomprehensible input: " + requestBody;
            Logger.getLogger(PsServicesRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsServicesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public String servicePut() {
        String message = "we are in servicePut()";
        //TODO finish the  servicePut() method
        return message;
    }

    /**
     * method to delete service with given id. However in normal circumstances
     * users are not supposed to call this methid: all service deletions should
     * be handled by high level host and matrix operations. This method is for
     * experts only and in the future we may require that only authorized users
     * use it.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public String serviceDelete(@PathVariable int id) {
        sessionStore.start();
        
        String message = "we are in serviceDelete()";
        this.psServiceOperator.delete(id);
        message = message + " service id=" + id + " deleted!";
        
        sessionStore.commit();
        return message;
    }
}
