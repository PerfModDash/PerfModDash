/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.dao.sessionimpl.SessionStore;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsUnknownCommandException;
import gov.bnl.racf.ps.dashboard3.operators.PsHostOperator;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * The REST hosts controller
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/hosts")
public class PsHostsRestController {

    @Autowired
    private PsHostOperator psHostOperator;

    public void setPsHostOperator(PsHostOperator psHostOperator) {
        this.psHostOperator = psHostOperator;
    }
    @Autowired
    SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    /**
     * test method, for development only. Create by hand a couple of hosts
     * //TODO delete this method before moving to production
     *
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String test() {

        sessionStore.start();

        String message = "we are in test()";

        PsHost host = new PsHost();
        host.setHostname("abc.com");
        host.setIpv4("123.456.78.90");
        this.psHostOperator.insert(host);


        PsHost host2 = new PsHost();
        host2.setHostname("xyz.com");
        host2.setIpv4("444.444.44.44");
        host2.setIpv6("fakeIpv6");
        this.psHostOperator.insert(host2);

        PsHost host3 = new PsHost();
        host3.setHostname("abcd.org");
        host3.setIpv4("555.555.55.55");
        host3.setIpv6("anotherFakeIpv6");
        this.psHostOperator.insert(host3);

        sessionStore.commit();

        return message;
    }

    /**
     * REST call to get list of all hosts
     *
     * @param detailLevel
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String hostsGet(
            @RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {

        sessionStore.start();

        List<PsHost> listOfHosts = this.psHostOperator.getAll();

        String result;
        if (detailLevel == null) {
            result = this.psHostOperator.toJson(listOfHosts).toString();
        } else {
            result = this.psHostOperator.toJson(listOfHosts, detailLevel).toString();
        }
        sessionStore.commit();
        return result;
    }

    /**
     * REST call to get a specific host
     *
     * @param id
     * @param detailLevel
     * @return hostAsJsonString
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String hostsGetById(
            @PathVariable int id,
            @RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        try {
            sessionStore.start();
            PsHost host = this.psHostOperator.getById(id);
            if (detailLevel == null) {
                detailLevel = PsParameters.DETAIL_LEVEL_HIGH;
            }
            sessionStore.commit();
            return this.psHostOperator.toJson(host, detailLevel).toString();

        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(PsHostsRestController.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(PsHostsRestController.class.getName()).log(Level.SEVERE, null, "host with id=" + id + " not found");
            sessionStore.rollback();
            throw new RuntimeException("host with id=" + id + " not found");
        }
    }

    /**
     * REST call to create a new host //TODO add HttpException
     *
     * @param requestBody
     * @return hostAsJsonString
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String hostsPost(@RequestBody String requestBody) {

        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            sessionStore.start();

            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert host object based on the request

            PsHost host = this.psHostOperator.insertNewHostFromJson(jsonInput);

            // convert this host to JSON
            JSONObject jsonOutput =
                    this.psHostOperator.toJson(host, PsParameters.DETAIL_LEVEL_HIGH);

            sessionStore.commit();

            // finally return the newly created host
            return jsonOutput.toString();

        } catch (ParseException ex) {
            String message = this.getClass().getName() + " Incomprehensible input: " + requestBody;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }
    }

    /**
     * REST call to modify the selected host
     *
     * @param id
     * @param requestBody
     * @return hostAsJsonString
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public String hostsPut(@PathVariable int id, @RequestBody String requestBody) {
        String message = "we are in hostsPut, id = " + id + " requestBody=" + requestBody;
        // first order of business is to convert request body to JSON 
        JSONParser parser = new JSONParser();
        JSONObject jsonInput;
        try {
            sessionStore.start();

            jsonInput = (JSONObject) parser.parse(requestBody);
            //second order of business is to build and insert host object based on the request
            JSONObject jsonOutput = this.psHostOperator.updateHostFromJson(id, jsonInput);

            sessionStore.commit();

            // finally return the newly created host
            return jsonOutput.toString();

        } catch (ParseException ex) {
            message = "Incomprehensible input: " + requestBody;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        } catch (PsObjectNotFoundException ex) {
            message = "Unknown host with id=" + id;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, message);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            sessionStore.rollback();
            return message;
        }
    }

    /**
     * REST call to perform high level operation on host
     *
     * @param id
     * @param command
     * @return hostAsJsonString
     */
    @RequestMapping(value = "/{id}/{command}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public String hostsPutCommand(@PathVariable int id, @PathVariable String command, @RequestBody String requestBody) {
        String message = "we are in hostsPut, id = " + id
                + " command=" + command + " requestBody=" + requestBody;
        try {
            sessionStore.start();

            PsHost updatedHost = this.psHostOperator.executeCommand(id, command, requestBody);

            JSONObject hostAsJson =
                    this.psHostOperator.toJson(updatedHost, PsParameters.DETAIL_LEVEL_HIGH);

            sessionStore.commit();
            return hostAsJson.toString();


        } catch (PsObjectNotFoundException ex) {
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

    /**
     * REST call to delete the selected host
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public String hostsDeleteById(@PathVariable int id) {
        sessionStore.start();
        String message = "we are in hostsDeleteById, delete host id = " + id;
        this.psHostOperator.delete(id);
        message = message + " Ordered deletion of host id=" + id;
        sessionStore.commit();
        return message;
    }
}
