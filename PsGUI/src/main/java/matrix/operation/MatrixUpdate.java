/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import matrix.bean.Matrix;
import org.json.simple.JSONObject;

/**
 * The class to update a matrix attributes in the data store.
 * @author siliu
 */
public class MatrixUpdate {
    
    /**
     * The method to execute the update of a matrix by providing the matrix id and the new matrix json object.
     * @param matrixId The matrix one would like to update. 
     * @param matrixObj The updated new matrix json object.
     * @return The updated matrix object
     */
    public static Matrix executeMatrixUpdate(String matrixId,JSONObject matrixObj){
        
        Matrix newMatrix = new Matrix();
        
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
            matrix_conn.setRequestMethod("PUT");
            
            DataOutputStream out = new DataOutputStream(matrix_conn.getOutputStream());
            out.writeBytes(matrixObj.toJSONString());
            out.flush();
            out.close();
				
            int resp_code = matrix_conn.getResponseCode();
            String resp_message = matrix_conn.getResponseMessage();
            
            if(resp_code == 200){
                
                System.out.println("Matrix edited!");
			
                BufferedReader reader = new BufferedReader(new InputStreamReader(matrix_conn.getInputStream()));
                String line = reader.readLine();
                while (line != null){
				
                    line = reader.readLine();
                    System.out.println(line);
            
                }
                
                String matrixName = (String) matrixObj.get("name");
                String serviceTypeId = (String) matrixObj.get("serviceTypeId");
               
                newMatrix.setMatrixName(matrixName);
                newMatrix.setServiceTypeId(serviceTypeId);
					
					
            }else{
                System.out.println("Failed to edit matrix!");
                System.out.println(resp_message);
                newMatrix = null;
					
            }
        
        }catch (IOException e) {
            System.out.println("Failed to edit matrix!");
            newMatrix = null;
	    e.printStackTrace();
        }
        
        return newMatrix;
    
    
    
    }
    
}
