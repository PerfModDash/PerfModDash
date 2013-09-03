/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsCloudDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.exceptions.PsCloudNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsUnknownCommandException;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * placeholder for PsCloud operator
 *  //TODO supply the PsCloud operator code
 * @author tomw
 */
public class PsCloudOperator {
    
    // === dependency injection ===//
    PsCloudDao psCloudDao;

    public void setPsCloudDao(PsCloudDao psCloudDao) {
        this.psCloudDao = psCloudDao;
    }
    
    
    // === CRUD methods ===//
    
    
    // === JSON methods ===/

    public List<PsCloud> getAll() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public JSONArray toJson(List<PsCloud> listOfClouds) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public JSONArray toJson(List<PsCloud> listOfClouds, String detailLevel) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public PsCloud getById(int id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public JSONObject toJson(PsCloud cloud){
         throw new UnsupportedOperationException("Not yet implemented");
    }
    public JSONObject toJson(PsCloud cloud,String detailLevel){
         throw new UnsupportedOperationException("Not yet implemented");
    }

    public PsCloud insertNewCloudFromJson(JSONObject jsonInput) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public PsCloud updateCloudFromJson(int id, JSONObject jsonInput) throws PsCloudNotFoundException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public PsCloud executeCommand(int id, String command, String requestBody) 
            throws 
            PsMatrixNotFoundException,PsSiteNotFoundException,
            PsUnknownCommandException,ParseException{
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void delete(int id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
