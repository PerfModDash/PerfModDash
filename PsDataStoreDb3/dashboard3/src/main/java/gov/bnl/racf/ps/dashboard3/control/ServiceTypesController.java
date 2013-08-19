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
 * Controller to manage service types. REST interface
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/servicetypes")
public class ServiceTypesController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String serviceTypeGet() {
        String message = "we are in serviceTypeGet()";
        //TODO finish the method    
        return message;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String serviceTypePost() {
        String message = "we are in serviceTypePost()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public String serviceTypePut() {
        String message = "we are in serviceTypePut()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String serviceTypeDelete() {
        String message = "we are in serviceTypeDelete()";
        //TODO finish the method
        return message;
    }
}
