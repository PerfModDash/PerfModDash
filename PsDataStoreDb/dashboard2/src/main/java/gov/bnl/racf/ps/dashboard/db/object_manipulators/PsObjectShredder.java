/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import org.hibernate.Session;

/**
 * utility class for deleting objects
 * @author tomw
 */
public class PsObjectShredder {
    /**
     * delete host object
     * @param session
     * @param host 
     */
    public static void delete(Session session, PsHost host){
        session.delete(host);
        //TODO add deleting services associated with this host
    }
}
