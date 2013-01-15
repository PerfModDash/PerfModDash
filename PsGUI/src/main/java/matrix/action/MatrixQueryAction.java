/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;
import matrix.bean.Matrix;
import matrix.operation.MatrixDisplay;
import matrix.operation.MatrixQuery;
import table.HtmlTable;

/**
 * The action to invoke the background logic methods to query matrix in the data store by cloud id and display the matrix using html tables,
 * Standard behaviors like the get and set methods for MatrixQueryAction object properties are defined here.
 * @author siliu
 */
public class MatrixQueryAction {
    
    private String matrixId;
    private Matrix one_matrix;
    private HtmlTable htmlTable;
    
    
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

    public HtmlTable getHtmlTable() {
        return htmlTable;
    }

    public void setHtmlTable(HtmlTable htmlTable) {
        this.htmlTable = htmlTable;
    }
    
    public MatrixQueryAction() {
    }
    
    /**
     * The method to invoke the logical method to query matrix from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print("MatrixQueryAction matrixId: " + matrixId);
         
        Matrix m = MatrixQuery.executeMatrixQuery(matrixId);
        
        if(m != null){
            
            setOne_matrix(m);
            HtmlTable table = MatrixDisplay.executeMatrixDisplay(one_matrix);
            setHtmlTable(table);
            
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
        
    }
}
