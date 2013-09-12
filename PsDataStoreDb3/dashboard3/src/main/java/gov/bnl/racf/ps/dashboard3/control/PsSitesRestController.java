/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.dao.sessionimpl.SessionStore;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsUnknownCommandException;
import gov.bnl.racf.ps.dashboard3.operators.PsSiteOperator;
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
 * Class for managing REST interface to sites.
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/sites")
public class PsSitesRestController {

    // === Dependency injection ===//
    @Autowired
    private PsSiteOperator psSiteOperator;

    public void setPsSiteOperator(PsSiteOperator psSiteOperator) {
        this.psSiteOperator = psSiteOperator;
    }
    @Autowired
    SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    // === main methods ===/
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String siteGet() {

        List<PsSite> listOfSites = this.psSiteOperator.getAll();
        JSONArray listOfSitesJson = this.psSiteOperator.toJson(listOfSites);
        return listOfSitesJson.toString();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String siteGetById(
            @PathVariable int id,
            @RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        try {
            sessionStore.start();

            PsSite site = this.psSiteOperator.getById(id);
            if (detailLevel == null) {
                detailLevel = PsParameters.DETAIL_LEVEL_HIGH;
            }

            sessionStore.commit();

            return this.psSiteOperator.toJson(site, detailLevel).toString();

        } catch (PsSiteNotFoundException ex) {
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, "host with id=" + id + " not found");
            sessionStore.rollback();
            throw new RuntimeException("site with id=" + id + " not found");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String sitePost(@RequestBody String requestBody) {
        String message;

        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            sessionStore.start();


            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert site object based on the request
            PsSite newSite = this.psSiteOperator.insertNewSiteFromJson(jsonInput);

            JSONObject jsonOutput = this.psSiteOperator.toJson(newSite);

            sessionStore.commit();

            // finally return the newly created host
            return jsonOutput.toString();

        } catch (ParseException ex) {
            message = "Incomprehensible input: " + requestBody;
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public String sitePut(@PathVariable int id, @RequestBody String requestBody) {

        String message = "we are in sitePut, id = " + id + " requestBody=" + requestBody;
        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            sessionStore.start();

            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert site object based on the request
            PsSite site = this.psSiteOperator.updateSiteFromJson(id, jsonInput);

            JSONObject jsonOutput = this.psSiteOperator.toJson(site);

            sessionStore.commit();

            // finally return the newly created site
            return jsonOutput.toString();

        } catch (ParseException ex) {
            message = "Incomprehensible input: " + requestBody;
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsObjectNotFoundException ex) {
            message = "Unknown host with id=" + id;
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }
    }

    @RequestMapping(value = "/{id}/{command}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public String sitePutCommand(@PathVariable int id, @PathVariable String command, @RequestBody String requestBody) {
        String message = "we are in sitePut, id = " + id
                + " command=" + command + " requestBody=" + requestBody;
        try {
            sessionStore.start();

            PsSite updatedSite = this.psSiteOperator.executeCommand(id, command, requestBody);

            JSONObject siteAsJson =
                    this.psSiteOperator.toJson(updatedSite, PsParameters.DETAIL_LEVEL_HIGH);

            sessionStore.commit();

            return siteAsJson.toString();

        } catch (PsHostNotFoundException ex) {
            message = message + " host not found";
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsSiteNotFoundException ex) {
            message = message + " host not found";
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsUnknownCommandException ex) {
            message = message + " unknown command";
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (ParseException ex) {
            message = message + " error parsing request body";
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(PsSitesRestController.class.getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public String siteDeleteById(@PathVariable int id) {
        sessionStore.start();
        String message = "we are in siteDeleteById, delete site id = " + id;
        this.psSiteOperator.delete(id);
        message = message + " Ordered deletion of site id=" + id;
        sessionStore.commit();
        return message;
    }
}
