/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;


import com.opensymphony.xwork2.Action;
import matrix.bean.MatrixList;
import matrix.operation.MatrixListQuery;

/**
 * The action to invoke the background logic methods to query the list of matrices in the data store,
 * Standard behaviors like the get and set methods for MatrixListQueryAction object properties are defined here.
 * @author siliu
 */
public class MatrixListQueryAction {
    
    private MatrixList matrix_list;
    
    public MatrixList getMatrix_list(){
        return matrix_list;
    }
    
    public void setMatrix_list(MatrixList matrix_list){
        this.matrix_list = matrix_list;
    }
    
    public MatrixListQueryAction() {
    }
    
    /**
     * The method to invoke the logical method to query matrix list in the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        MatrixList m = MatrixListQuery.executeMatrixListQuery();
        
        if(m != null){
            setMatrix_list(m);
            return Action.SUCCESS;
        
        }else{
             return Action.ERROR;
        }
    }
}
