/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects.factories;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;

/**
 *
 * @author tomw
 */
public interface PsServiceFactory {
    public  PsService createService(PsServiceType type, PsHost host);
    public  PsService createService(PsServiceType type, PsHost source,PsHost destination);
    public  PsService createService(PsServiceType type, PsHost source,PsHost destination,PsHost monitor);
}
