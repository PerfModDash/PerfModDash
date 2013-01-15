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
 * The action to display the page to add or remove hosts to or from matrix columns.
 * Standard behaviors like the get and set methods for MatrixARHostToColumnPageAction object properties are defined here.
 * @author siliu
 */
public class MatrixARHostToColumnPageAction {
    
    private String matrixId;
    private Matrix one_matrix;
    private HostList host_list;
    private ArrayList hosts_in_matrix_column;
    private ArrayList hosts_not_in_matrix_column;
    
    
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
    
    public ArrayList getHosts_in_matrix_column() {
        return hosts_in_matrix_column;
    }

    public void setHosts_in_matrix_column(ArrayList hosts_in_matrix_column) {
        this.hosts_in_matrix_column = hosts_in_matrix_column;
    }
    
    public ArrayList getHosts_not_in_matrix_column() {
        return hosts_not_in_matrix_column;
    }

    public void setHosts_not_in_matrix_column(ArrayList hosts_not_in_matrix_column) {
        this.hosts_not_in_matrix_column = hosts_not_in_matrix_column;
    }
    
    public MatrixARHostToColumnPageAction() {
    }
    
    /**
     * The method to get the current host list in a matrix column, and the current host list not in a matrix column.
     * Display host list in two groups: hosts in matrix column and hosts not in matrix column.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        System.out.print(" MatrixARHostToColumnPageAction matrixId: " + matrixId);
        Matrix m = MatrixQuery.executeMatrixQuery(matrixId);
        setHost_list(HostListQuery.executeHostListQuery());
        
        if(m != null){
            
            setOne_matrix(m);
            
            JSONArray column_hosts = one_matrix.getColumns();
           
            
            String hostname;
            
            
            hosts_in_matrix_column = new ArrayList();
            hosts_not_in_matrix_column = new ArrayList();
            
            if(! column_hosts.isEmpty()){
                
                for(int i=0 ; i<column_hosts.size(); i++){
                    
                        hostname = (String)column_hosts.get(i);
                        hosts_in_matrix_column.add(hostname); 
                }
                
                
                String h2;
            
                for(int i = 0; i< host_list.getHostNames().size() ; i++){
                
                    h2 = (String) host_list.getHostNames().get(i);
                
                    if(! hosts_in_matrix_column.contains(h2)){
                        hosts_not_in_matrix_column.add(h2);
                    }
                }
                
                
            }else{
                
                //hosts_in_site = null;
                hosts_not_in_matrix_column = host_list.getHostNames();
                
            }
            
            
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
