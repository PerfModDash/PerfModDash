/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import java.util.Date;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author tomw
 */
public class PsServiceResultManipulator {

    public static int deleteResults(Session session, Date tmin, Date tmax) {
        String queryString = "delete from PsServiceResult where time between :time_start and :time_end";
        Query query = session.createQuery(queryString);
        query.setParameter("time_start", tmin);
        query.setParameter("time_end", tmax);
        int result = query.executeUpdate();
        return result;
    }
}
