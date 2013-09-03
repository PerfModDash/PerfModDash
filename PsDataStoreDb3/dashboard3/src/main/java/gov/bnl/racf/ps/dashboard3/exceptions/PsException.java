/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.exceptions;

/**
 * Top level exception for all perfsonar related exceptions
 *
 * @author tomw
 */
class PsException extends Exception {

    public PsException(String message) {
        super(message);
    }
}
