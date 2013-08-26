/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsMatrixDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsUnknownCommandException;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsMatrixJson;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * Placeholder for PsMatrix operator class
 * @author tomw
 */
public class PsMatrixOperator {

    // === dependency injection part ===//
    
    private PsMatrixDao psMatrixDao;

    public void setPsMatrixDao(PsMatrixDao psMatrixDao) {
        this.psMatrixDao = psMatrixDao;
    }
    
    private PsMatrixJson psMatrixJson;

    public void setPsMatrixJson(PsMatrixJson psMatrixJson) {
        this.psMatrixJson = psMatrixJson;
    }
    
    
    
    
    // === simple CRUD operations part ===//
    
    public PsMatrix getById(int id) throws PsMatrixNotFoundException{
        return this.psMatrixDao.getById(id);
    }
    
    
    public List<PsMatrix> getAll() {
        return this.psMatrixDao.getAll();
    }

    public void insert(PsMatrix matrix){
        this.psMatrixDao.insert(matrix);
    }
    
    public void update(PsMatrix matrix){
        this.psMatrixDao.update(matrix);
    }
    
    public void delete(int id) {
        this.psMatrixDao.delete(id);
    }
    
    public void delete(PsMatrix matrix) {
        this.psMatrixDao.delete(matrix);
    }

    // === JSON conversion part ===//
    
    public JSONObject toJson(PsMatrix matrix, String detailLevel) {
        return this.psMatrixJson.toJson(matrix, detailLevel);
    }
    
    public JSONObject toJson(PsMatrix matrix) {
        return this.psMatrixJson.toJson(matrix);
    }

    public JSONArray toJson(List<PsMatrix> listOfMatrices) {
       return this.psMatrixJson.toJson(listOfMatrices);
    }
    
    public JSONArray toJson(List<PsMatrix> listOfMatrices, String detailLevel) {
        return this.psMatrixJson.toJson(listOfMatrices, detailLevel);
    }

    // === matrix operations ===//
    
    public PsMatrix insertNewMatrixFromJson(JSONObject jsonInput) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public PsMatrix updateMarixFromJson(int id, JSONObject jsonInput) throws PsMatrixNotFoundException{
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public PsMatrix executeCommand(int id, String command, String requestBody)  
            throws PsMatrixNotFoundException,PsHostNotFoundException,PsUnknownCommandException,ParseException{
        throw new UnsupportedOperationException("Not yet implemented");
    }

    
    
}
