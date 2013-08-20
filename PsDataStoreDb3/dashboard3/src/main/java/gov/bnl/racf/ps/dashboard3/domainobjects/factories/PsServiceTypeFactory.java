/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects.factories;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import java.util.ArrayList;


/**
 * Factory of service types, to be used for defining service types
 *
 * @author tomw
 */
public interface PsServiceTypeFactory {

    public  PsServiceType createType(String typeName) ;

    public  boolean isKnownType(String typeName); 
   
    public  boolean isMatrixType(String typeName);

    public  boolean isPrimitiveServiceThroughput(String typeName);

    public  boolean isPrimitiveServiceLatency(String typeName);
    
    public  boolean isPrimitiveService(String typeName);
    

    public  ArrayList<String> listOfServiceTypes();
    
    public  ArrayList<String> listOfPrimitiveServiceTypes();
    
    public  ArrayList<String> listOfPrimitiveThroughputServiceTypes();
    
    public  ArrayList<String> listOfPrimitiveLatencyServiceTypes();

   
}
