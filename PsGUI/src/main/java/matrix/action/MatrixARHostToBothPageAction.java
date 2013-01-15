/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;
import host.bean.Host;
import host.bean.HostList;
import host.operation.HostListQuery;
import host.operation.HostQuery;
import java.util.ArrayList;
import matrix.bean.Matrix;
import matrix.operation.MatrixQuery;
import org.json.simple.JSONArray;

/**
 * The action to display the page to add or remove hosts to or from both columns and rows.
 * Standard behaviors like the get and set methods for MatrixARHostToBothPageAction object properties are defined here.
 * @author siliu
 */
public class MatrixARHostToBothPageAction {
    
    private String matrixId;
    private Matrix one_matrix;
    private HostList host_list;
    private ArrayList hosts_in_matrix_both;
    private ArrayList hosts_not_in_matrix_both;
    
    
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
    
    
    public HostList getHost_list(){
        return host_list;
    }
    
    public void setHost_list(HostList host_list){
        this.host_list = host_list;
    }
    
    public ArrayList getHosts_in_matrix_both() {
        return hosts_in_matrix_both;
    }

    public void setHosts_in_matrix_both(ArrayList hosts_in_matrix_both) {
        this.hosts_in_matrix_both = hosts_in_matrix_both;
    }
    
    public ArrayList getHosts_not_in_matrix_both() {
        return hosts_not_in_matrix_both;
    }

    public void setHosts_not_in_matrix_both(ArrayList hosts_not_in_matrix_both) {
        this.hosts_not_in_matrix_both = hosts_not_in_matrix_both;
    }
    
    public MatrixARHostToBothPageAction() {
    }
    
    /**
     * The method to get the current host list in a matrix, and the current host list not in a matrix.
     * Display host list in two groups: hosts in matrix and hosts not in matrix.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print(" MatrixDeletePageAction matrixId: " + matrixId);
        Matrix m = MatrixQuery.executeMatrixQuery(matrixId);
        setHost_list(HostListQuery.executeHostListQuery());
        
        if(m != null){
            
            setOne_matrix(m);
            
            JSONArray column_hosts = one_matrix.getColumns();
            JSONArray row_hosts = one_matrix.getRows();
            JSONArray matrix_hosts = row_hosts;
            matrix_hosts.retainAll(column_hosts);  // hosts in both rows and columns
            
            
            String hostname;
          
            
            hosts_in_matrix_both = new ArrayList();
            hosts_not_in_matrix_both = new ArrayList();
            
            if(! matrix_hosts.isEmpty()){
                
                for(int i=0 ; i<matrix_hosts.size(); i++){
                    
                        hostname = (String)matrix_hosts.get(i);
                       
                        hosts_in_matrix_both.add(hostname); 
                }
                
                
                String h2;
            
                for(int i = 0; i< host_list.getHostNames().size() ; i++){
                
                    h2 = (String) host_list.getHostNames().get(i);
                
                    if(! hosts_in_matrix_both.contains(h2)){
                        hosts_not_in_matrix_both.add(h2);
                    }
                }
                
                
            }else{
                
                //hosts_in_site = null;
                hosts_not_in_matrix_both = host_list.getHostNames();
                
            }
            
            
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
