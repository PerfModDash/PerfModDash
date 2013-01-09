/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import java.util.Iterator;

/**
 * utility class to initialize content of data store
 * Should be used for debugging dashboard code
 * Not to be used for production purposes
 * @author tomw
 */
public class PsObjectInitiator {
    
    public static String init(){
        String result="";
        Iterator<String> iter = PsServiceTypeFactory.listOfServiceTypes().iterator();
        while(iter.hasNext()){
            result=result+(String)iter.next()+" ";
        }
        return result;
    }
    
    
}
