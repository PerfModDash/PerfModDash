/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh.action;

import com.opensymphony.xwork2.Action;
import java.io.File;
import matrix.bean.Matrix;
import matrix.operation.MatrixDisplay;
import mesh.bean.Mesh;
import mesh.operation.MeshConfig;
import mesh.operation.MeshGet;
import table.HtmlTable;

/**
 * The action to invoke the background logic method to create new mesh.
 * Standard behaviors like the get and set methods for MeshConfigAction object properties are defined here.
 * @author siliu
 */
public class MeshConfigAction {

    private File fileUpload;
    private String fileURL;
    private Mesh one_mesh;
    private Matrix[] matrices;
    private HtmlTable[] htmlTables;
    
    
    public File getFileUpload() {
        return fileUpload;
    }
 
    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }
    
    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
    
    public Mesh getOne_mesh() {
        return one_mesh;
    }

    public void setOne_mesh(Mesh one_mesh) {
        this.one_mesh = one_mesh;
    }
    
    
    
    public Matrix[] getMatrices() {
        return matrices;
    }

    public void setMatrices(Matrix[] matrices) {
        this.matrices = matrices;
    }

    public HtmlTable[] getHtmlTables() {
        return htmlTables;
    }

    public void setHtmlTables(HtmlTable[] htmlTables) {
        this.htmlTables = htmlTables;
    }
    
    
    public MeshConfigAction() {
    }
    
    /**
     * The method to invoke the logical method to create mesh in the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        Mesh m = MeshGet.executeMeshGet(fileURL);
        
        if(m != null){
            
            Mesh newMesh = MeshConfig.executeMeshConfig(m);
            setOne_mesh(newMesh);
            
            if(one_mesh != null){
                
                matrices = one_mesh.getMatrices();
                htmlTables = new HtmlTable[matrices.length];
                
                System.out.println("matrices length: "+ matrices.length +"\n");
                
                for(int i=0; i<matrices.length; i++){
                    
                    if(matrices[i] != null){
                        Matrix matrix_to_display = matrices[i];
                        
                        //System.out.print("matrix" + i + " : "+ matrices[i]+"\n");
                    
                        htmlTables[i] = MatrixDisplay.executeMatrixDisplay(matrix_to_display);
                    
                    }
                
                }
                return Action.SUCCESS;
            }else{
                return Action.ERROR;
            }
        
        }else{
            return Action.ERROR;
        
        }
    }
}
