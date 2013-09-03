/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.exceptions;

/**
 * Exception raised when user refernces non existing perfsonar object
 * @author tomw
 */
public class PsObjectNotFoundException extends PsException {
    public PsObjectNotFoundException(String message) {
        super(message);
    }
}
