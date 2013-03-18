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
import matrix.operation.MatrixQuery;
import org.json.simple.JSONArray;
import site.bean.Site;
import site.operation.SiteQuery;

/**
 * The action to invoke the background logic methods to query cloud in the data store by cloud id.
 * Standard behaviors like the get and set methods for CloudQueryAction object properties are defined here.
 * @author siliu
 */
public class CloudQueryAction {
    
    private String cloudid;
    private Cloud one_cloud;
    private ArrayList sites_in_cloud;
    private ArrayList matrices_in_cloud;
        
    
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

    public ArrayList getSites_in_cloud() {
        return sites_in_cloud;
    }

    public void setSites_in_cloud(ArrayList sites_in_cloud) {
        this.sites_in_cloud = sites_in_cloud;
    }
    
    public ArrayList getMatrices_in_cloud() {
        return matrices_in_cloud;
    }

    public void setMatrices_in_cloud(ArrayList matrices_in_cloud) {
        this.matrices_in_cloud = matrices_in_cloud;
    }
    
    
    public CloudQueryAction() {
    }
    
    /**
     * The method to invoke the logical method to query cloud from the data store.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print("CloudQueryAction cloudid: " + cloudid);
        Cloud c = CloudQuery.executeCloudQuery(cloudid);
        
        
        if(c != null){
     
            setOne_cloud(c);
            
            JSONArray sites = one_cloud.getSites();
            JSONArray matrices = one_cloud.getMatrices();
           
            sites_in_cloud = new ArrayList();
            
            if(sites != null){
                
                for(int i=0 ; i<sites.size(); i++){
                    
                        String site_id = (String)sites.get(i);
                        Site s = SiteQuery.executeSiteQuery(site_id);
                        
                        String site_name = s.getSitename();
                        sites_in_cloud.add(site_name); 
                }
            }
            
            matrices_in_cloud = new ArrayList();
            
            if(matrices != null){
                
               for(int i=0 ; i<matrices.size(); i++){
                    
                        String matrix_id = (String)matrices.get(i);
                        Matrix m = MatrixQuery.executeMatrixQuery(matrix_id);
                        
                        String matrix_name = m.getMatrixName();
                        matrices_in_cloud.add(matrix_name); 
                }
                
            }
            return Action.SUCCESS;
        }else{
            return Action.ERROR;
        }
    }
}
