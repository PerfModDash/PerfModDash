/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects.factories.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.domainobjects.factories.PsMatrixFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsMatrixFactorySimpleImpl implements PsMatrixFactory{
    @Override
    @Transactional
    public PsMatrix createNewMatrix(PsServiceType type, String matrixName) {
        PsMatrix matrix = new PsMatrix();
        matrix.setMatrixType(type);
        matrix.setName(matrixName);
        return matrix;
    }
    
}
