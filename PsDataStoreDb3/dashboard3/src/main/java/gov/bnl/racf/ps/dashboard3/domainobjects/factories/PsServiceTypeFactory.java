/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects.factories;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Factory of service types, to be used for defining service types
 *
 * @author tomw
 */
public interface PsServiceTypeFactory {

    @Transactional
    public PsServiceType createType(String typeName);

    @Transactional
    public boolean isKnownType(String typeName);

    @Transactional
    public boolean isMatrixType(String typeName);

    @Transactional
    public boolean isPrimitiveServiceThroughput(String typeName);

    @Transactional
    public boolean isPrimitiveServiceLatency(String typeName);

    @Transactional
    public boolean isPrimitiveService(String typeName);

    @Transactional
    public List<String> listOfServiceTypes();

    @Transactional
    public List<String> listOfPrimitiveServiceTypes();

    @Transactional
    public List<String> listOfPrimitiveThroughputServiceTypes();

    @Transactional
    public List<String> listOfPrimitiveLatencyServiceTypes();
}
