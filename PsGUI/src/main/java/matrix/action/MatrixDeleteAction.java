/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;
import matrix.operation.MatrixDelete;

/**
 * The action to invoke the background logic methods to delete matrix from the data store by matrix id,
 * Standard behaviors like the get and set methods for MatrixDeleteAction object properties are defined here.
 * @author siliu
 */
public class MatrixDeleteAction {
    
    private String matrixId;
    
    
    public String getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(String matrixId) {
        this.matrixId = matrixId;
    }
    
    public MatrixDeleteAction() {
    }
    
    /**
     * The method to invoke the logical method to delete matrix from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print("matrix delete action: " + matrixId);
        
        boolean flag = MatrixDelete.executeMatrixDelete(matrixId);
                
        if(flag == true){
            
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}
