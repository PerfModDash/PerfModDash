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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The class to create a new matrix in the data store.
 * @author siliu
 */
public class MatrixCreate {
    /**
     * The method to execute the creation of a new matrix by providing the new matrix json object.
     * @param matrixObj The new matrix json object.
     * @return The newly created matrix object.
     */
    public static Matrix executeMatrixCreate(JSONObject matrixObj){
    
        Matrix newMatrix = new Matrix();
        
        DataStoreConfig cfg = new DataStoreConfig();
        String MATRIX = PsApi.MATRIX;
        String matrixURL = cfg.getProperty("storeURL") + MATRIX;
        
        try{
            
            URL url = new URL(matrixURL);
            HttpURLConnection matrix_conn = (HttpURLConnection)url.openConnection();
				
            matrix_conn.setDoOutput(true);
            matrix_conn.setDoInput(true);
            matrix_conn.setInstanceFollowRedirects(false);
            matrix_conn.setUseCaches (false);
            matrix_conn.setRequestMethod("POST");
            
            DataOutputStream out = new DataOutputStream(matrix_conn.getOutputStream());
            out.writeBytes(matrixObj.toJSONString());
            out.flush();
            out.close();
				
            int resp_code = matrix_conn.getResponseCode();
            String resp_message = matrix_conn.getResponseMessage();
            
            if(resp_code == 200){
                System.out.println("Matrix created!");
			
                BufferedReader reader = new BufferedReader(new InputStreamReader(matrix_conn.getInputStream()));
                /*
                String line = reader.readLine();
                while (line != null){
				
                    line = reader.readLine();
                    System.out.println(line);
            
                }
                */ 
                
                try{
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(reader);
                    JSONObject newMatrixObj = (JSONObject)obj;

                    String matrixId = (String) newMatrixObj.get("id");
                    String matrixName = (String) newMatrixObj.get("name");
                    String serviceTypeId = (String) newMatrixObj.get("serviceTypeId");
                
                    newMatrix.setMatrixId(matrixId);
                    newMatrix.setMatrixName(matrixName);
                    newMatrix.setServiceTypeId(serviceTypeId);
                
                
                }catch(ParseException e){
         
                    e.printStackTrace();
		
                }catch(ClassCastException e){
          
                    e.printStackTrace();
		
                }
                
                
					
					
            }else{
                System.out.println("Failed to create matrix!");
                System.out.println(resp_message);
                newMatrix = null;
					
            }
        
        }catch (IOException e) {
            System.out.println("Failed to create matrix!");
            newMatrix = null;
	    e.printStackTrace();
        }
        
        
        return newMatrix;
    
    
    }
    
}
