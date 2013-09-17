/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.dao.sessionimpl.SessionStore;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.operators.PsServiceTypeOperator;
import java.util.List;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
public class PsServiceTypesController {

    // --- dependency injection part ---///
    @Autowired
    private PsServiceTypeOperator psServiceTypeOperator;

    public void setPsServiceTypeOperator(PsServiceTypeOperator psServiceTypeOperator) {
        this.psServiceTypeOperator = psServiceTypeOperator;
    }
    
    @Autowired
    SessionStore sessionStore;

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
    

    // --- dependency injection ends ---//
    // --- body of class starts here ---//
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String serviceTypeGet() {
        sessionStore.start();
        
        String message = "we are in serviceTypeGet()";
        List<PsServiceType> listOfServiceTypes =
                this.psServiceTypeOperator.getAll();
        JSONArray listOfTypesJson = this.psServiceTypeOperator.toJson(listOfServiceTypes);
        
        sessionStore.commit();
        
        return listOfTypesJson.toString();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String serviceTypePost() {
        String message = "we are in serviceTypePost()";
        //TODO finish the serviceTypePost() method
        return message;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public String serviceTypePut() {
        String message = "we are in serviceTypePut()";
        //TODO finish the serviceTypePut() method
        return message;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public String serviceTypeDelete() {
        String message = "we are in serviceTypeDelete()";
        //TODO finish the serviceTypeDelete() method
        return message;
    }
}
