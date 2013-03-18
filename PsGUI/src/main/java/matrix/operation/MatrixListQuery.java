/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import matrix.bean.MatrixList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The class to query the list of matrices (ids,names and types) in the data store.
 * @author siliu
 */
public class MatrixListQuery {
    /**
     * The method to execute the query of matrix list.
     * @return The MatrixList object.
     */
    public static MatrixList executeMatrixListQuery(){
        
        MatrixList matrix_list = new MatrixList();
        
        JSONParser parser = new JSONParser();
	DataStoreConfig cfg = new DataStoreConfig();
	String MATRIX = PsApi.MATRIX;
        String detailLevel = PsApi.DETAIL_LEVEL_PARAMETER;
        String medium = PsApi.DETAIL_LEVEL_MEDIUM;
        String matrixURL = cfg.getProperty("storeURL") + MATRIX + "?" + detailLevel + "=" + medium;
    
        try{
            URL url = new URL(matrixURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Object obj = parser.parse(reader);
            
            /*
            JSONArray matrixIds= (JSONArray)obj;
            int matrixNumber = matrixIds.size();
         
            Matrix one_matrix;
            JSONArray matrixNames = new JSONArray();
            JSONArray matrixTypes = new JSONArray();  //serviceTypeId field???
            String matrix_id;
            String matrix_name;
            String matrix_type;
            
            for(int i=0 ; i<matrixNumber ; i++){
                
                matrix_id = (String) matrixIds.get(i);
                one_matrix = MatrixQuery.executeMatrixQuery(matrix_id);
                matrix_name = one_matrix.getMatrixName();
                matrix_type = one_matrix.getServiceTypeId();
                
                matrixNames.add(matrix_name);
		matrixTypes.add(matrix_type);		
            }
            */
            JSONArray matrixObjects = (JSONArray)obj;
            
            int matrixNumber = matrixObjects.size();
            JSONArray matrixIds = new JSONArray();
            JSONArray matrixNames = new JSONArray();
            JSONArray matrixTypes = new JSONArray();
            
            JSONObject matrixObj = new JSONObject();
            String matrix_id;
            String matrix_name;
            String matrix_type;
            
            for(int i=0; i<matrixNumber; i++){
            
                matrixObj = (JSONObject) matrixObjects.get(i);
                matrix_id = (String) matrixObj.get("id");
                matrix_name = (String) matrixObj.get("name");
                matrix_type = (String) matrixObj.get("serviceTypeId");
                
                matrixIds.add(matrix_id);
                matrixNames.add(matrix_name);
                matrixTypes.add(matrix_type);
            }
            
            
            matrix_list.setMatrixIds(matrixIds);
            matrix_list.setMatrixNames(matrixNames);
            matrix_list.setMatrixTypes(matrixTypes);
            matrix_list.setMatrixNumber(matrixNumber);
            
         
	}catch(IOException e){
            matrix_list = null;
            e.printStackTrace();
			
	}catch(ParseException e){
            matrix_list = null;
            e.printStackTrace();
			
	}
    
        return matrix_list;
    
    }
    
}
