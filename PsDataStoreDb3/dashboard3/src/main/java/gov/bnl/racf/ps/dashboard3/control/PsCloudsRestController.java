/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class for managing REST interface to cloud objects
 * //TODO: finish methods
 * @author tomw
 */
@Controller
@RequestMapping(value = "/clouds")
public class PsCloudsRestController {
    
    // === Dependency injection ===//
    
    
    //=== Main methods ===//
    
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String cloudGet() {
        String message = "we are in cloudGet()";
        //TODO finish the method    
        return message;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String cloudPost() {
        String message = "we are in cloudPost()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public String cloudPut() {
        String message = "we are in cloudPut()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String cloudDelete() {
        String message = "we are cloudDelete()";
        //TODO finish the method
        return message;
    }
}
