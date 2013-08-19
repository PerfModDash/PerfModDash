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
 * Controller for managing REST interface to matrices
 * @author tomw
 */
@Controller
@RequestMapping(value = "/matrices")
public class PsMatrixRestController {
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String matrixGet() {
        String message = "we are in matrixGet()";
        //TODO finish the method    
        return message;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String matrixPost() {
        String message = "we are in matrixPost()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public String matrixPut() {
        String message = "we are in matrixPut()";
        //TODO finish the method
        return message;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String matrixDelete() {
        String message = "we are matrixDelete()";
        //TODO finish the method
        return message;
    }
}
