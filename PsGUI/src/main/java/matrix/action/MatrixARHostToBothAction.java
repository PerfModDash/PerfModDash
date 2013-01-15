/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;
import host.bean.HostList;
import host.operation.HostListQuery;
import java.util.ArrayList;
import matrix.bean.Matrix;
import matrix.operation.MatrixARHost;
import matrix.operation.MatrixQuery;
import org.json.simple.JSONArray;

/**
 * The action to invoke the background logic methods to add or remove hosts to or from a matrix both rows and columns,
 * then display the hosts in one matrix.
 * Standard behaviors like the get and set methods for Action object properties are defined here.
 * @author siliu
 */
public class MatrixARHostToBothAction {
    
    private String matrixId;
    private String button;
    private ArrayList hosts_to_add_both;
    private ArrayList hosts_to_remove_both;
    private int button_flag;
    private JSONArray host_array;
    
    //to dislay the hosts in matrix after add/remove
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
    
    public ArrayList getHosts_to_add_both() {
        return hosts_to_add_both;
    }

    public void setHosts_to_add_both(ArrayList hosts_to_add_both) {
        this.hosts_to_add_both = hosts_to_add_both;
    }
    
    public ArrayList getHosts_to_remove() {
        return hosts_to_remove_both;
    }

    public void setHosts_to_remove_both(ArrayList hosts_to_remove_both) {
        this.hosts_to_remove_both = hosts_to_remove_both;
    }
    
    public JSONArray getHost_array() {
        return host_array;
    }

    public void setHost_array(JSONArray host_array) {
        this.host_array = host_array;
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
    
    public MatrixARHostToBothAction() {
    }
    
    /**
     * The method to get the host list that user checked on the display,which he/she would like to add or remove.
     * Then invoke the logical methods to execute the operation.
     * Then display the add or remove hosts from matrix both column and row page.
     * @return SUCCESS if the action is successful, ERROR otherwise
     * @throws Exception 
     */
    public String execute() throws Exception {
        
        setHost_list(HostListQuery.executeHostListQuery());
        JSONArray hostIds = host_list.getHostIds();
        JSONArray hostNames = host_list.getHostNames();
        
        host_array = new JSONArray();
        String element;
        int index;
        
        if(button.equals("remove selected hosts from matrix")){
            
            button_flag = 5;
		
            for(int m=0; m<hosts_to_remove_both.size(); m++){
                element = (String) hosts_to_remove_both.get(m);
                index = hostNames.indexOf(element);
		host_array.add(hostIds.get(index));
            }
			
	}else if(button.equals("add selected hosts to matrix")){
            
            button_flag = 6;
			
            for(int n=0; n<hosts_to_add_both.size(); n++){
		element = (String) hosts_to_add_both.get(n);
                index = hostNames.indexOf(element);
		host_array.add(hostIds.get(index));
            }
	}
        
        boolean flag = MatrixARHost.executeMatrixARHost(matrixId, button_flag, host_array);
        
        if(flag == true){
            
            Matrix m = MatrixQuery.executeMatrixQuery(matrixId);
            
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
        
        }else{
        
            return Action.ERROR;
        
        }
        
        
    }
}
