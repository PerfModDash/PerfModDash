/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/services")
public class PsServicesRestController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String serviceGet() {
        String message = "we are in serviceGet()";
        //TODO finish the method    
        return message;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String servicePost() {
        String message = "we are in servicePost()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public String servicePut() {
        String message = "we are in servicePut()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String serviceDelete() {
        String message = "we are in serviceDelete()";
        //TODO finish the method
        return message;
    }
}
