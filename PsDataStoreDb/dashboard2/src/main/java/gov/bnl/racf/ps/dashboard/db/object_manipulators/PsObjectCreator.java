/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import org.hibernate.Session;

/**
 * static class for creating various objects and saving them to storage at the same time
 * @author tomw
 */
public class PsObjectCreator {
    public static PsService createNewService(Session session){
        PsService service = new PsService();
        session.save(service);
        return service;
    }
}
