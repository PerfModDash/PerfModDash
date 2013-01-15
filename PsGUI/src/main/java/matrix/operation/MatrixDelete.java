/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The class to delete a matrix from the data store.
 * @author siliu
 */
public class MatrixDelete {
    /**
     * The method to execute the deletion of a matrix by providing the matrix id.
     * @param matrixId The matrix one would like to delete.
     * @return true if delete success, false otherwise
     */
    public static boolean executeMatrixDelete(String matrixId){
        
        DataStoreConfig cfg = new DataStoreConfig();
        String MATRIX = PsApi.MATRIX;
        String matrixURL = cfg.getProperty("storeURL") + MATRIX + '/' + matrixId;
        
        try{
            
            URL url = new URL(matrixURL);
            HttpURLConnection matrix_conn = (HttpURLConnection)url.openConnection();
				
            matrix_conn.setDoOutput(true);
            matrix_conn.setDoInput(true);
            matrix_conn.setInstanceFollowRedirects(false);
            matrix_conn.setUseCaches (false);
            matrix_conn.setRequestMethod("DELETE");
            
            
            int resp_code = matrix_conn.getResponseCode();
            String resp_message = matrix_conn.getResponseMessage();
            
            if(resp_code == 200){
                
                System.out.println("Matrix edited!");
		return true;	
					
					
            }else{
                System.out.println("Failed to delete matrix!");
                System.out.println(resp_message);
                return false;
					
            }
            
        }catch (IOException e) {
            System.out.println("Failed to delete matrix!");
	    e.printStackTrace();
            return false;
        }
    
    
    
    }
    
}
