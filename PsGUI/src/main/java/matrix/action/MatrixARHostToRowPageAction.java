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
 * The action to display the page to add or remove hosts to or from matrix row.
 * Standard behaviors like the get and set methods for MatrixARHostToRowPageAction object properties are defined here.
 * @author siliu
 */
public class MatrixARHostToRowPageAction {
    
    private String matrixId;
    private Matrix one_matrix;
    private HostList host_list;
    private ArrayList hosts_in_matrix_row;
    private ArrayList hosts_not_in_matrix_row;
    
    
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
    
    public ArrayList getHosts_in_matrix_row() {
        return hosts_in_matrix_row;
    }

    public void setHosts_in_matrix_row(ArrayList hosts_in_matrix_row) {
        this.hosts_in_matrix_row = hosts_in_matrix_row;
    }
    
    public ArrayList getHosts_not_in_matrix_row() {
        return hosts_not_in_matrix_row;
    }

    public void setHosts_not_in_matrix_row(ArrayList hosts_not_in_matrix_row) {
        this.hosts_not_in_matrix_row = hosts_not_in_matrix_row;
    }
    
    public MatrixARHostToRowPageAction() {
    }
    
    /**
     * The method to get the current host list in a matrix row, and the current host list not in a matrix row.
     * Display host list in two groups: hosts in matrix row and hosts not in matrix row.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        System.out.print(" MatrixARHostToRowPageAction matrixId: " + matrixId);
        Matrix m = MatrixQuery.executeMatrixQuery(matrixId);
        setHost_list(HostListQuery.executeHostListQuery());
        
        if(m != null){
            
            setOne_matrix(m);
            
            JSONArray row_hosts = one_matrix.getRows();
            System.out.print(" MatrixARHostToRowPageAction row_hosts: " + row_hosts);
            
           
            String hostname;
            
            
            hosts_in_matrix_row = new ArrayList();
            hosts_not_in_matrix_row = new ArrayList();
            
            if(! row_hosts.isEmpty()){
                
                for(int i=0 ; i<row_hosts.size(); i++){
                    
                    hostname = (String)row_hosts.get(i);
                    hosts_in_matrix_row.add(hostname); 
                }
                
                
                String h2;
            
                for(int i = 0; i< host_list.getHostNames().size() ; i++){
                
                    h2 = (String) host_list.getHostNames().get(i);
                
                    if(! hosts_in_matrix_row.contains(h2)){
                        hosts_not_in_matrix_row.add(h2);
                    }
                }
                
                
            }else{
                
                //hosts_in_site = null;
                hosts_not_in_matrix_row = host_list.getHostNames();
                
            }
            
            
            return Action.SUCCESS;
        
        }else{
            return Action.ERROR;
        }
    }
}
