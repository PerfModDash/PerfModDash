/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.action;

import cloud.bean.Cloud;
import cloud.operation.CloudQuery;
import com.opensymphony.xwork2.Action;
import java.util.ArrayList;
import matrix.bean.Matrix;
import matrix.bean.MatrixList;
import matrix.operation.MatrixListQuery;
import matrix.operation.MatrixQuery;
import org.json.simple.JSONArray;

/**
 * The action to display the page to add or remove matrices to or from cloud.
 * Standard behaviors like the get and set methods for CloudARMatrixPageAction object properties are defined here.
 * @author siliu
 */
public class CloudARMatrixPageAction {
    
    private String cloudid;
    private Cloud one_cloud;
    private MatrixList matrix_list;
    private ArrayList matrices_in_cloud;
    private ArrayList matrices_not_in_cloud;
        
    
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
    
    public CloudARMatrixPageAction() {
    }
    
    public String execute() throws Exception {
        
        Cloud c = CloudQuery.executeCloudQuery(cloudid);
        
        setMatrix_list(MatrixListQuery.executeMatrixListQuery());
        
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
    }
}
