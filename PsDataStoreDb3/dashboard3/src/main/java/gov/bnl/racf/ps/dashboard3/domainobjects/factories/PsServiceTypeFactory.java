/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects.factories;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import java.util.List;


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
    

    public  List<String> listOfServiceTypes();
    
    public  List<String> listOfPrimitiveServiceTypes();
    
    public  List<String> listOfPrimitiveThroughputServiceTypes();
    
    public  List<String> listOfPrimitiveLatencyServiceTypes();

   
}
