/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.operators.PsHostOperator;
import gov.bnl.racf.ps.dashboard3.operators.PsServiceTypeOperator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for performing utility operations, like initialisation of service
 * types or deleting entire datastore. It is meant to be used during development
 * process to help with debugging. It will be deleted when the datastore is
 * deployed for production. The corresponding url's do not form part of the
 * official API and should not be included in any documentation.
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/util")
public class UtilityController {

    @Autowired
    private PsServiceTypeOperator psServiceTypeOperator;

    public void setPsServiceTypeOperator(PsServiceTypeOperator psServiceTypeOperator) {
        this.psServiceTypeOperator = psServiceTypeOperator;
    }
    
    @Autowired
    private PsHostOperator psHostOperator;

    public void setPsHostOperator(PsHostOperator psHostOperator) {
        this.psHostOperator = psHostOperator;
    }

    /**
     * method to initialize the service types in database
     *
     * @return
     */
    @RequestMapping(value = "/initServiceTypes", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String initServiceTypes() {
        String message = "we are in initServiceTypes()<BR>";
        List<String> listOfServiceTypeNames = this.psServiceTypeOperator.initServiceTypes();
        for (String serviceTypeName : listOfServiceTypeNames) {
            message = message + " " + serviceTypeName;
        }
        return message;
    }

    /**
     * debug method to dump content of database in human readable format
     *
     * @return
     */
    @RequestMapping(value = "/dump", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String dump() {
        //TODO write the dump() method!!!
        String message = "";
        List<PsHost> listOfHosts = this.psHostOperator.getAll();
        message = message + "<H1>HOSTS</H1>" + this.psHostOperator.toJson(listOfHosts).toString();
        return message;
    }
}
