/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;

/**
 * The action to display the choices to add or remove hosts to or from matrix.
 * Standard behaviors like the get and set methods for Action object properties are defined here.
 * @author siliu
 */
public class MatrixARHostPageAction {
    
    private String matrixId;
    
    
    public String getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(String matrixId) {
        this.matrixId = matrixId;
    }
    
    
    public MatrixARHostPageAction() {
    }
    
    /**
     * The method to display the different entrances to add/remove hosts from matrix
     * @return SUCCESS
     * @throws Exception 
     */
    
    public String execute() throws Exception {
        return Action.SUCCESS;
    }
}
