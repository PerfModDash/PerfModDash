/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.exceptions;

/**
 *
 * @author tomw
 */
public class PsCloudNotFoundException extends PsObjectNotFoundException{
     public PsCloudNotFoundException(String message) {
        super(message);
    }
}
