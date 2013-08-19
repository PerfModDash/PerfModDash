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
 * Class for managing REST interface to sites.
 * @author tomw
 */
@Controller
@RequestMapping(value = "/sites")
public class PsSitesRestController {
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String siteGet() {
        String message = "we are in siteGet()";
        //TODO finish the method    
        return message;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String sitePost() {
        String message = "we are in sitePost()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public String servicePut() {
        String message = "we are in sitePut()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String siteDelete() {
        String message = "we are siteDelete()";
        //TODO finish the method
        return message;
    }
    
    
}
