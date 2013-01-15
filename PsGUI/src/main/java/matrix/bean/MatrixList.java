/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.bean;

import org.json.simple.JSONArray;

/**
 * This class includes all matrix ids, names, types and the total number of matrices.
 * Standard behaviors like the get and set methods for HostList object properties are defined here.
 * @author siliu
 */
public class MatrixList {
    
    private JSONArray matrixIds;
    private JSONArray matrixNames;
    private JSONArray matrixTypes;
    private int matrixNumber;
    
    public JSONArray getMatrixIds(){
        
	return matrixIds;
    }
    
    public void setMatrixIds(JSONArray matrixIds){
        this.matrixIds = matrixIds;
    }
    
    public JSONArray getMatrixNames(){
	return matrixNames;
    }
    
    public void setMatrixNames(JSONArray matrixNames){
        this.matrixNames = matrixNames;
    }
    
    public JSONArray getMatrixTypes(){
	return matrixTypes;
    }
    
    public void setMatrixTypes(JSONArray matrixTypes){
        this.matrixTypes = matrixTypes;
    }
    
    public int getMatrixNumber(){
	return matrixNumber;
    }
    
    public void setMatrixNumber(int matrixNumber){
        this.matrixNumber = matrixNumber;
    }
    
}
