/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.dao.sessionimpl.SessionStore;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.exceptions.PsCloudNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsUnknownCommandException;
import gov.bnl.racf.ps.dashboard3.operators.PsCloudOperator;
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
 * Class for managing REST interface to cloud objects 
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/clouds")
public class PsCloudsRestController {

    // === Dependency injection ===//
    @Autowired
    private PsCloudOperator psCloudOperator;
    @Autowired
    SessionStore sessionStore;

    //=== Main methods ===//
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String cloudGet(@RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {

        sessionStore.start();

        List<PsCloud> listOfClouds = this.psCloudOperator.getAll();

        JSONArray listOfCloudsJson;

        if (detailLevel == null) {
            listOfCloudsJson = this.psCloudOperator.toJson(listOfClouds,PsParameters.DETAIL_LEVEL_LOW);
        } else {
            listOfCloudsJson = this.psCloudOperator.toJson(listOfClouds, detailLevel);
        }

        sessionStore.commit();

        return listOfCloudsJson.toString();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String cloudGetById(
            @PathVariable int id,
            @RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        try {
            sessionStore.start();


            PsCloud cloud = this.psCloudOperator.getById(id);

            JSONObject cloudJson;
            if (detailLevel == null) {
                cloudJson = this.psCloudOperator.toJson(cloud);
            } else {
                cloudJson = this.psCloudOperator.toJson(cloud, detailLevel);
            }

            sessionStore.commit();

            return cloudJson.toString();
        } catch (PsCloudNotFoundException ex) {
            String message = "We are in " + this.getClass().getName() + " missing cloud id=" + id;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);

            sessionStore.rollback();
            return message;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String cloudPost(@RequestBody String requestBody) {
        String message = "we are in cloudPost(). ";

        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            sessionStore.start();


            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert site object based on the request
            PsCloud newCloud = this.psCloudOperator.insertNewCloudFromJson(jsonInput);

            JSONObject jsonOutput = this.psCloudOperator.toJson(newCloud);


            sessionStore.commit();
            // finally return the newly created host
            return jsonOutput.toString();

        } catch (ParseException ex) {
            message = "Incomprehensible input: " + requestBody;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public String cloudPut(@PathVariable int id, @RequestBody String requestBody) {
        String message = "we are in cloudPut(), id = " + id + " requestBody=" + requestBody;
        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            sessionStore.start();
            
            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert site object based on the request
            PsCloud cloud = this.psCloudOperator.updateCloudFromJson(id, jsonInput);

            JSONObject jsonOutput = this.psCloudOperator.toJson(cloud);
            
            sessionStore.commit();
            
            // finally return the newly created cloud
            return jsonOutput.toString();

        } catch (ParseException ex) {
            message = message + " Incomprehensible input: " + requestBody;
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsCloudNotFoundException ex) {
            message = message + " Unknown cloud with id=" + id;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }

    }

    @RequestMapping(value = "/{id}/{command}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public String cloudPutCommand(@PathVariable int id, @PathVariable String command, @RequestBody String requestBody) {
        String message = "we are in cloudPut, id = " + id
                + " command=" + command + " requestBody=" + requestBody;
        try {
            sessionStore.start();

            PsCloud updatedCloud = this.psCloudOperator.executeCommand(id, command, requestBody);

            JSONObject cloudAsJson =
                    this.psCloudOperator.toJson(updatedCloud, PsParameters.DETAIL_LEVEL_HIGH);

            sessionStore.commit();
            return cloudAsJson.toString();


        } catch (PsCloudNotFoundException ex) {
            message = message + " cloud not found, id=" + id;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsMatrixNotFoundException ex) {
            message = message + " matrix not found";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsSiteNotFoundException ex) {
            message = message + " host not found";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsUnknownCommandException ex) {
            message = message + " unknown command";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (ParseException ex) {
            message = message + " error parsing request body";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public String cloudDelete(@PathVariable int id) {
        sessionStore.start();
        String message = "we are cloudDelete()";
        this.psCloudOperator.delete(id);
        message = message + " Ordered deletion of cloud id=" + id;
        sessionStore.commit();
        return message;
    }
}
