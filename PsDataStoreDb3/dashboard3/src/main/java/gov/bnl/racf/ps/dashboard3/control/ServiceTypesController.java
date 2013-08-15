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
}
