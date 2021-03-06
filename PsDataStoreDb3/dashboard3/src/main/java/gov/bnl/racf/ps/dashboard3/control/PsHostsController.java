/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.operators.PsHostOperator;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * The REST hosts controller
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/hosts")
public class PsHostsController {

    @Autowired
    private PsHostOperator psHostOperator;

    public void setPsHostOperator(PsHostOperator psHostOperator) {
        this.psHostOperator = psHostOperator;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        String message = "we are in test()";

        PsHost host = new PsHost();
        host.setHostname("abc.com");
        host.setIpv4("123.456.78.90");
        this.psHostOperator.create(host);


        PsHost host2 = new PsHost();
        host2.setHostname("xyz.com");
        host2.setIpv4("444.444.44.44");
        host2.setIpv6("fakeIpv6");
        this.psHostOperator.create(host2);

        PsHost host3 = new PsHost();
        host3.setHostname("abcd.org");
        host3.setIpv4("555.555.55.55");
        host3.setIpv6("anotherFakeIpv6");
        this.psHostOperator.create(host3);

        return message;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String hostsGet(
            @RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {

        List<PsHost> listOfHosts = this.psHostOperator.getAll();

        if (detailLevel == null) {
            return this.psHostOperator.toJson(listOfHosts).toString();
        } else {
            return this.psHostOperator.toJson(listOfHosts, detailLevel).toString();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String hostsGetById(
            @PathVariable int id,
            @RequestParam(value = PsParameters.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        try {            
            PsHost host = this.psHostOperator.getById(id);
            if (detailLevel == null) {
                return this.psHostOperator.toJson(host).toString();
            } else {
                return this.psHostOperator.toJson(host, detailLevel).toString();
            }
        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(PsHostsController.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(PsHostsController.class.getName()).log(Level.SEVERE, null, "host with id="+id+" not found");
            throw new RuntimeException("host with id="+id+" not found");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String hostsPost(@RequestBody String requestBody) {
        String message = "we are in hostsPost, request body = " + requestBody;
        return message;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String hostsPut(@PathVariable Long id, @RequestBody String requestBody) {
        String message = "we are in hostsPut, id = " + id + " requestBody=" + requestBody;
        return message;
    }

    @RequestMapping(value = "/{id}/{command}", method = RequestMethod.PUT)
    @ResponseBody
    public String hostsPutCommand(@PathVariable Long id, @PathVariable String command) {
        String message = "we are in hostsPut, id = " + id + " command=" + command;
        return message;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String hostsDeleteById(@PathVariable int id) {
        String message = "we are in hostsDeleteById, id = " + id;
        return message;
    }
}
