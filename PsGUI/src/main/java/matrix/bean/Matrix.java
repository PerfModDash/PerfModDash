/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.bean;

import org.json.simple.JSONArray;

/**
 * This class includes all basic attributes in one matrix.
 * Standard behaviors like the get and set methods for Host object properties are defined here.
 * @author siliu
 */
public class Matrix {
    
    private String matrixId;
    private String matrixName;
    private JSONArray statusLabels;
    private String lastUpdateTime;
    private String detailLevel;
    private String serviceTypeId;
    private JSONArray columns;
    private JSONArray serviceNames;
    private JSONArray rows;
    private Cell[][] matrixCells;
    
    public String getMatrixId(){	
	return matrixId;
    }
    
    public void setMatrixId(String matrixId){
        this.matrixId = matrixId;
    }
    
    public String getMatrixName(){
	return matrixName;
    }
    
    public void setMatrixName(String matrixName){
        this.matrixName = matrixName;
    }
    
    public JSONArray getStatusLabels(){
	return statusLabels;
    }
    
    public void setStatusLabels(JSONArray statusLabels){
        this.statusLabels = statusLabels;
    }
    
    public String getLastUpdateTime(){	
	return lastUpdateTime;
    }
    
    public void setLastUpdateTime(String lastUpdateTime){
        this.lastUpdateTime = lastUpdateTime;
    }
    
    
    public String getDetailLevel(){
	return detailLevel;
    }
	
    public void setDetailLevel(String detailLevel){
        this.detailLevel = detailLevel;
    }
    
    public String getServiceTypeId(){
	return serviceTypeId;
    }
	
    public void setServiceTypeId(String serviceTypeId){
        this.serviceTypeId = serviceTypeId;
    }
    
    
    public JSONArray getColumns(){
	return columns;
    }
    
    public void setColumns(JSONArray columns){
        this.columns = columns;
    }
    
    public JSONArray getServiceNames(){
	return serviceNames;
    }
    
    public void setServiceNames(JSONArray serviceNames){
        this.serviceNames = serviceNames;
    }
    
    public JSONArray getRows(){
	return rows;
    }
    
    public void setRows(JSONArray rows){
        this.rows = rows;
    }
    
    public Cell[][] getMatrixCells(){
	return matrixCells;
    }
    
    public void setMatrixCells(Cell[][] matrixCells){
        this.matrixCells = matrixCells;
    }
    
    
    
}
