/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.exceptions;

/**
 *
 * @author tomw
 */
public class PsUnknownServiceTypeException extends PsObjectNotFoundException{
     public PsUnknownServiceTypeException(String message) {
        super(message);
    }
}
