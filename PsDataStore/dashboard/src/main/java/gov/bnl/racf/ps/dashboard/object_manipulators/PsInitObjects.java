/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsServiceType;
import java.util.Iterator;
import org.json.simple.JSONObject;

/**
 * service class to initialize data objects, mostly for debugging
 * @author tomw
 */
public class PsInitObjects {
    public static void initServiceTypes(){
        Iterator<String> iter = PsServiceTypeFactory.listOfServiceTypes().iterator();
            while(iter.hasNext()){
                String typeName = (String)iter.next();
                
                PsServiceType type = PsServiceTypeFactory.createType(typeName);
            }
    }
    
}
