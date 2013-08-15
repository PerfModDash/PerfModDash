/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class used for debugging purposes. To be 
 * deleted from production release
 * @author tomw
 */
@Controller
@RequestMapping(value = "/well")
public class HelloController {

    //@Autowired
    //private HttpServletRequest request;

    @RequestMapping(method = RequestMethod.GET)
    //public ModelAndView helloWorldGet(@RequestBody String requestBody) {
    public ModelAndView helloWorldGet() {
        System.out.println("we are in helloWorldGet()");
        //System.out.println("RequestBody="+requestBody);
//        try {
//            String dataPart = extractString();
//            System.out.println("datapart=" + dataPart);
//        } catch (IOException ex) {
//            System.out.println("failed to read dataPart");
//            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return new ModelAndView("/success.jsp");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView helloWorldPost(@RequestBody String requestBody) {
        System.out.println("we are in helloWorldPost()");
        System.out.println("RequestBody="+requestBody);
//        try {
//            String dataPart = extractString();
//            System.out.println("datapart=" + dataPart);
//        } catch (IOException ex) {
//            System.out.println("failed to read dataPart");
//            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return new ModelAndView("/success.jsp");
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView helloWorldPut(@RequestBody String requestBody) {
        System.out.println("we are in helloWorldPut()");
        System.out.println("RequestBody="+requestBody);
//        try {
//            String dataPart = extractString();
//            System.out.println("datapart=" + dataPart);
//        } catch (IOException ex) {
//            System.out.println("failed to read dataPart");
//            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return new ModelAndView("/success.jsp");
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView helloWorldDelete(@RequestBody String requestBody) {
        System.out.println("we are in helloWorldDelete()");
        System.out.println("RequestBody="+requestBody);
//        try {
//            String dataPart = extractString();
//            System.out.println("datapart=" + dataPart);
//        } catch (IOException ex) {
//            System.out.println("failed to read dataPart");
//            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return new ModelAndView("/success.jsp");
    }
    

//    public String extractString() throws IOException {
//        String jb = "";
//        String line = null;
//        try {
//            BufferedReader reader = request.getReader();
//            while ((line = reader.readLine()) != null) {
//                jb = jb + line;
//            }
//        } catch (Exception e) {
//            System.out.println(new Date() + " " + e);
//        }
//
//
//        return jb;
//    }
}
