/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;
import matrix.bean.Matrix;
import matrix.operation.MatrixQuery;

/**
 * The action to display the delete matrix page.
 * Standard behaviors like the get and set methods for MatrixDeletePageAction object properties are defined here.
 * @author siliu
 */
public class MatrixDeletePageAction {
    
    private String matrixId;
    private Matrix one_matrix;
    
    
    public String getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(String matrixId) {
        this.matrixId = matrixId;
    }
    
    public Matrix getOne_matrix() {
        return one_matrix;
    }

    public void setOne_matrix(Matrix one_matrix) {
        this.one_matrix = one_matrix;
    }
    
    public MatrixDeletePageAction() {
    }
    
    /**
     * The method to invoke the logical method to query matrix that to be deleted.
     * Then display it on the interface.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print(" MatrixDeletePageAction matrixId: " + matrixId);
        Matrix m = MatrixQuery.executeMatrixQuery(matrixId);
        
        if(m != null){
            
            setOne_matrix(m);
            
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
       
}
