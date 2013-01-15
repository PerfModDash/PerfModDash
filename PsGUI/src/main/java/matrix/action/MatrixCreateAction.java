/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;
import matrix.bean.Matrix;
import matrix.operation.MatrixCreate;
import org.json.simple.JSONObject;


/**
 * The action to invoke the background logic method to create new matrix.
 * Standard behaviors like the get and set methods for MatrixCreateAction object properties are defined here.
 * @author siliu
 */
public class MatrixCreateAction {
    
    private String matrixName;
    private String serviceTypeId;
    private Matrix newMatrix;
    
    public String getMatrixName() {
        return matrixName;
    }

    public void setMatrixName(String matrixName) {
        this.matrixName = matrixName;
    }
    
    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
    
    public Matrix getNewMatrix() {
        return newMatrix;
    }

    public void setNewMatrix(Matrix newMatrix) {
        this.newMatrix = newMatrix;
    }
    
    public MatrixCreateAction() {
    }
    
    /**
     * The method to invoke the logical method to create matrix in the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.println("matrixName:"+matrixName);
        System.out.println("serviceTypeId:"+serviceTypeId);
        
        JSONObject matrixObj = new JSONObject();
        matrixObj.put("name", matrixName);
        matrixObj.put("serviceTypeId", serviceTypeId);
	
        Matrix m = MatrixCreate.executeMatrixCreate(matrixObj);
        
        if(m != null){
            
            setNewMatrix(m);
           
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
