/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.racf.bnl.ps.dashboard3.PsApi.PsApi;
import org.json.simple.JSONObject;
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

//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView hostsGet(
//            @RequestParam(value = PsApi.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
//        String message = "we are in hostsGet detailLevel=" + detailLevel;
//        
//        JSONObject json = new JSONObject();
//        json.put("abcd","456");
//        
//        message=json.toString();
//        
//        return new ModelAndView("/hello.jsp", "message", message);
//    }
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String hostsGet(
            @RequestParam(value = PsApi.DETAIL_LEVEL_PARAMETER, required = false) String detailLevel) {
        String message = "we are in hostsGet detailLevel=" + detailLevel;

        //JSONObject json = new JSONObject();
        //json.put("abcd","456");

        //message=json.toString();

        return message;
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
