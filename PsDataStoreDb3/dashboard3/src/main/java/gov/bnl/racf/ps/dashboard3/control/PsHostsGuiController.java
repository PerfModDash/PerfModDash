/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.exceptions.PsObjectNotFoundException;
import gov.bnl.racf.ps.dashboard3.operators.PsHostOperator;
import gov.bnl.racf.ps.dashboard3.parameters.PsJspLibrary;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for GUI pages for hosts
 * 
 * //TODO persost the sorting criteria in session
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

        this.psHostOperator.delete(id);
        
        return new ModelAndView("redirect:../list");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        PsHost newHost = new PsHost();
        return new ModelAndView(PsJspLibrary.EDIT_HOST, "host", newHost);
             
    }
    
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView insertNewHost(PsHost host, Errors result) {
        if (result.hasErrors()) {
            //re-present
            return new ModelAndView(PsJspLibrary.EDIT_HOST, "host", host);
        } else {
            this.psHostOperator.insert(host);
            return new ModelAndView("redirect:../list");
        }
    }
    
    
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam(value = "id") int id) {
       
        PsHost host;
        try {
            host = this.psHostOperator.getById(id);
        } catch (PsObjectNotFoundException ex) {
            Logger.getLogger(PsHostsGuiController.class.getName()).log(Level.SEVERE, null, ex);
            String message = "Host id="+id+" not found";
            return new ModelAndView(PsJspLibrary.MESSAGE, "message", message);     
        }
        return new ModelAndView(PsJspLibrary.EDIT_HOST, "host", host);
             
    }
    
    
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView save(PsHost host, Errors result) {
        if (result.hasErrors()) {
            //re-present
            return new ModelAndView(PsJspLibrary.EDIT_HOST, "host", host);
        } else {
            this.psHostOperator.update(host);
            return new ModelAndView("redirect:../list");
        }
    }
}
