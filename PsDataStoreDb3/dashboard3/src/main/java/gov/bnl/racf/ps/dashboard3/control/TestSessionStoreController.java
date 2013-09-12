/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.control;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import gov.bnl.racf.ps.dashboard3.dao.sessionimpl.SessionStore;
import java.util.List;
import org.hibernate.Session;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author tomw
 */
@Controller
@RequestMapping(value = "/testSession")
public class TestSessionStoreController {

    @Autowired
    SessionStore sessionStore;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String testSessionGet() {

        sessionStore.start();

        Session session = sessionStore.getSession();

        PsHost host = (PsHost) session.get(PsHost.class, 2);

        sessionStore.commit();

        return host.toString();
    }
}
