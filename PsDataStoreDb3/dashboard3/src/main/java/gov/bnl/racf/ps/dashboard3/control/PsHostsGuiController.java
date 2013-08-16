/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.objects.PsHost;
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
@RequestMapping(value = "/gui/hosts")
public class PsHostsGuiController {

    @Autowired
    private PsHostOperator psHostOperator;

    public void setPsHostOperator(PsHostOperator psHostOperator) {
        this.psHostOperator = psHostOperator;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "sortingOrder", required = false) String sortingOrder,
            @RequestParam(value = "sortingVariable", required = false) String sortingVariable) {

        List<PsHost> listOfHosts = this.psHostOperator.getAll();
        
        return new ModelAndView(PsJspLibrary.LIST_HOSTS, "listOfHosts", listOfHosts);
    }

    
}
