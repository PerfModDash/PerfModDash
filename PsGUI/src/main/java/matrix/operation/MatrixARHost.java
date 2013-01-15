/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;

/**
 * The class to add or remove hosts to or from a matrix in the data store.
 * @author siliu
 */
public class MatrixARHost {
    /**
     * The method to execute the operation of adding or removing hosts to or from a matrix in the data store 
     * by providing matrix id, button flag(action category), host list
     * @param matrixId The matrix that one would like to add or remove hosts.
     * @param button_flag The action that one would like to take: 1 is "remove hosts from matrix"; 2 is "add hosts to matrix"
     * @param host_array The host list that one would like to add or remove.
     * @return true if add/remove success, false otherwise
     */
    public static boolean executeMatrixARHost(String matrixId, int button_flag, JSONArray host_array){
    
        DataStoreConfig cfg = new DataStoreConfig();
        String storeURL = cfg.getProperty("storeURL");
	String MATRIX = PsApi.MATRIX;
        
        String command = "";
        
        if(button_flag == 1){
			
            command = storeURL + MATRIX + "/" + matrixId + "/" + PsApi.MATRIX_REMOVE_ROW_HOST_IDS;
			
	}else if(button_flag == 2){
            
            command = storeURL + MATRIX + "/" + matrixId + "/" + PsApi.MATRIX_ADD_ROW_HOST_IDS;
			
	}else if(button_flag == 3){
            
            command = storeURL + MATRIX + "/" + matrixId + "/" + PsApi.MATRIX_REMOVE_COLUMN_HOST_IDS;
			
	}else if(button_flag == 4){
            
            command = storeURL + MATRIX + "/" + matrixId + "/" + PsApi.MATRIX_ADD_COLUMN_HOST_IDS;
			
	}else if(button_flag == 5){
            
            command = storeURL + MATRIX + "/" + matrixId + "/" + PsApi.MATRIX_REMOVE_HOST_IDS;
			
	}else if(button_flag == 6){
            
            command = storeURL + MATRIX + "/" + matrixId + "/" + PsApi.MATRIX_ADD_HOST_IDS;
			
	}
        
        try{
		
            URL commandURL = new URL(command);
            HttpURLConnection matrix_conn = (HttpURLConnection)commandURL.openConnection();
            matrix_conn.setDoOutput(true);
            matrix_conn.setDoInput(true);
            matrix_conn.setInstanceFollowRedirects(false);
            matrix_conn.setUseCaches (false);
            matrix_conn.setRequestMethod("PUT");
            
            DataOutputStream out = new DataOutputStream(matrix_conn.getOutputStream());
            out.writeBytes(host_array.toJSONString());
            out.flush();
            out.close();
            
            int resp_code = matrix_conn.getResponseCode();
            String resp_message = matrix_conn.getResponseMessage();
            
            if(resp_code == 200){
		
                System.out.println("Add/Remove hosts from matrix success!");
                return true;
                
            }else{
		System.out.println(resp_message  + "<br>");
		System.out.println("Add/Remove hosts from matrix failed!");
                return false;
            }
            
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Add/Remove hosts from matrix failed!");
            return false;
	}
        
    
    }
    
}
