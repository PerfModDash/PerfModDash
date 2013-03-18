/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.Cloud;
import cloud.operation.CloudARMatrix;
import cloud.operation.CloudQuery;
import com.opensymphony.xwork2.Action;
import java.util.ArrayList;
import matrix.bean.Matrix;
import matrix.bean.MatrixList;
import matrix.operation.MatrixListQuery;
import matrix.operation.MatrixQuery;
import org.json.simple.JSONArray;

/**
 * The action to invoke the background logic methods to add or remove matrices from a cloud,
 * then display the matrices in one cloud.
 * Standard behaviors like the get and set methods for CloudARMatrixAction object properties are defined here.
 * @author siliu
 */
public class CloudARMatrixAction {
    
    private String cloudid;
    private String button;
    private ArrayList matrices_to_add;
    private ArrayList matrices_to_remove;
    private int button_flag;
    private JSONArray matrix_array;
    
    
    //to display the matrix list after add and remove to the clouds
    private Cloud one_cloud;
    private MatrixList matrix_list;
    private ArrayList matrices_in_cloud;
    private ArrayList matrices_not_in_cloud;
    
    
    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
    
    public int getButton_flag() {
        return button_flag;
    }

    public void setButton_flag(int button_flag) {
        this.button_flag = button_flag;
    }
    
    
    public ArrayList getMatrices_to_add() {
        return matrices_to_add;
    }

    public void setMatrices_to_add(ArrayList matrices_to_add) {
        this.matrices_to_add = matrices_to_add;
    }
    
    public ArrayList getMatrices_to_remove() {
        return matrices_to_remove;
    }

    public void setMatrices_to_remove(ArrayList matrices_to_remove) {
        this.matrices_to_remove = matrices_to_remove;
    }
    
    public JSONArray getMatrix_array() {
        return matrix_array;
    }

    public void setMatrix_array(JSONArray matrix_array) {
        this.matrix_array = matrix_array;
    }
         
    
    public String getCloudid() {
        return cloudid;
    }

    public void setCloudid(String cloudid) {
        this.cloudid = cloudid;
    }
    
    public Cloud getOne_cloud() {
        return one_cloud;
    }

    public void setOne_cloud(Cloud one_cloud) {
        this.one_cloud = one_cloud;
    }
    
    public MatrixList getMatrix_list(){
        return matrix_list;
    }
    
    public void setMatrix_list(MatrixList matrix_list){
        this.matrix_list = matrix_list;
    }
    
    
    public ArrayList getMatrices_in_cloud() {
        return matrices_in_cloud;
    }

    public void setMatrices_in_cloud(ArrayList matrices_in_cloud) {
        this.matrices_in_cloud = matrices_in_cloud;
    }
    
    public ArrayList getMatrices_not_in_cloud() {
        return matrices_not_in_cloud;
    }

    public void setMatrices_not_in_cloud(ArrayList matrices_not_in_cloud) {
        this.matrices_not_in_cloud = matrices_not_in_cloud;
    }
    
    public CloudARMatrixAction() {
    }
    
    /**
     * The method to get the matrix list that user checked on the display,which he/she would like to add or remove.
     * Then invoke the logical methods to execute the operation.
     * Then display the add or remove matrices from cloud page.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        
        setMatrix_list(MatrixListQuery.executeMatrixListQuery());
        JSONArray matrixIds = matrix_list.getMatrixIds() ;
        JSONArray matrixNames = matrix_list.getMatrixNames(); 
        
        matrix_array= new JSONArray();
        String element;
        int index;
        
        if(button.equals("remove selected matrices")){
            
            button_flag = 1;
		
            for(int i=0; i<matrices_to_remove.size(); i++){
                element = (String) matrices_to_remove.get(i);
                index = matrixNames.indexOf(element);
		matrix_array.add(matrixIds.get(index));
            }
			
	}else if(button.equals("add selected matrices")){
            
            button_flag = 2;
			
            for(int n=0; n< matrices_to_add.size(); n++){
                
		element = (String) matrices_to_add.get(n);
                index = matrixNames.indexOf(element);
		matrix_array.add(matrixIds.get(index));
            }
	}
        
        boolean flag = CloudARMatrix.executeCloudARMatrix(cloudid, button_flag, matrix_array);
        
        if(flag == true ){
        
            Cloud c = CloudQuery.executeCloudQuery(cloudid);
            if(c != null){
     
            setOne_cloud(c);
            
            JSONArray matrices = one_cloud.getMatrices();
            
            
            String matrix_id;
            String matrix_name;
            Matrix m;
            
            matrices_in_cloud = new ArrayList();
            matrices_not_in_cloud = new ArrayList();
            
            if( matrices != null){
                
                for(int i=0 ; i<matrices.size(); i++){
                    
                        matrix_id = (String)matrices.get(i);
                        m = MatrixQuery.executeMatrixQuery(matrix_id);
                        
                        matrix_name = m.getMatrixName();
                        matrices_in_cloud.add(matrix_name); 
                }
                
                
                String m2;
                for(int i = 0; i< matrix_list.getMatrixNames().size() ; i++){
                
                    m2 = (String) matrix_list.getMatrixNames().get(i);
                
                    if(! matrices_in_cloud.contains(m2)){
                        matrices_not_in_cloud.add(m2);
                    }
                }
                
            }else{
                matrices_not_in_cloud = matrix_list.getMatrixNames();
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
