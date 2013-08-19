/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.operators.PsHostOperator;
import gov.bnl.racf.ps.dashboard3.parameters.PsJspLibrary;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for GUI pages for hosts
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/gui_hosts")
public class PsHostsGuiController {

    @Autowired
    private PsHostOperator psHostOperator;

    public void setPsHostOperator(PsHostOperator psHostOperator) {
        this.psHostOperator = psHostOperator;
    }

    /**
     * controller to display list of hosts
     * @param sortingOrder
     * @param sortingVariable
     * @return 
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "sortingOrder", required = false) String sortingOrder,
            @RequestParam(value = "sortingVariable", required = false) String sortingVariable) {

        List<PsHost> listOfHosts = 
                this.psHostOperator.getAll(sortingVariable,sortingOrder);
        
        return new ModelAndView(PsJspLibrary.LIST_HOSTS, "listOfHosts", listOfHosts);       
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView list(
            @RequestParam(value = "id", required = false) int id) {

        String message="Hello we are in delete method id="+id;
        System.out.println(message);
        
        this.psHostOperator.delete(id);
        
        System.out.println("host with id="+id +" deleted");
        
        //return new ModelAndView(PsJspLibrary.LIST_HOSTS, "listOfHosts", listOfHosts);    
        //return new ModelAndView("/hello.jsp", "message", message); 
        return new ModelAndView("redirect:../list");
    }

    
}
