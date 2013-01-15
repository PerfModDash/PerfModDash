/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;
import matrix.bean.Matrix;
import matrix.operation.MatrixUpdate;
import org.json.simple.JSONObject;

/**
 * The action to invoke the background logic methods to update matrix name in the data store by matrix id,
 * Standard behaviors like the get and set methods for MatrixUpdateAction object properties are defined here.
 * @author siliu
 */
public class MatrixUpdateAction {
    
    private String matrixId;
    private String matrixName;
    private Matrix newMatrix;
    
    public String getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(String matrixId) {
        this.matrixId = matrixId;
    }
    
    public String getMatrixName() {
        return matrixName;
    }

    public void setMatrixName(String matrixName) {
        this.matrixName = matrixName;
    }
    
    public Matrix getNewMatrix() {
        return newMatrix;
    }

    public void setNewMatrix(Matrix newMatrix) {
        this.newMatrix = newMatrix;
    }
    
    public MatrixUpdateAction() {
    }
    
    /**
     * The method to invoke the logical method to update matrix from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print(" MatrixUpdateAction matrixId: " + matrixId);
        
        JSONObject matrixObj = new JSONObject();
        matrixObj.put("name", matrixName);
        matrixObj.put("id", matrixId);
        
        Matrix m = MatrixUpdate.executeMatrixUpdate(matrixId, matrixObj);
        
        if(m != null){
            setNewMatrix(m);
            
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
        
        
    }
}
