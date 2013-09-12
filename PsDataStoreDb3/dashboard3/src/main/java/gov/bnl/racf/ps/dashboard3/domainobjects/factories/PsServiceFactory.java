/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects.factories;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsServiceFactory {
    @Transactional
    public  PsService createService(PsServiceType type, PsHost host);
    @Transactional
    public  PsService createService(PsServiceType type, PsHost source,PsHost destination);
    @Transactional
    public  PsService createService(PsServiceType type, PsHost source,PsHost destination,PsHost monitor);
}
