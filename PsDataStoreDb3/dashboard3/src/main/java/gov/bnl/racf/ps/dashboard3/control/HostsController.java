/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.jsonconverter.Ps2Json;
import gov.bnl.racf.ps.dashboard3.objects.PsHost;
import gov.bnl.racf.ps.dashboard3.operators.PsHostOperator;
import gov.bnl.racf.ps.dashboard3.parameters.PsApi;
import java.util.List;
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
public class HostsController {

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
        this.psHostOperator.create(host);

        return message;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String hostsGet(
            @RequestParam(value = PsApi.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {

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
            @PathVariable Long id,
            @RequestParam(value = PsApi.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        String message = "we are in hostsGetById id=" + id + " detailLevel=" + detailLevel;
        return message;
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
        String message = "we are in hostsPut, id = " + id;
        return message;
    }
}
