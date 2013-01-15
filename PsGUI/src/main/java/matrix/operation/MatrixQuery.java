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
import matrix.bean.Cell;
import matrix.bean.Matrix;
import matrix.bean.ResultParameters;
import matrix.bean.Test;
import matrix.bean.TestParameters;
import matrix.bean.TestResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * The class to query a matrix from the data store.
 * @author siliu
 */
public class MatrixQuery {
    
    /**
     * The method to execute the query of a matrix by providing the matrix id.
     * @param matrixId The matrix one would like to query.
     * @return The queried matrix object.
     */
    public static Matrix executeMatrixQuery(String matrixId){
        
        Matrix one_matrix = new Matrix();
        System.out.println("matrix id to query:" + matrixId);
        JSONParser parser = new JSONParser();
	DataStoreConfig cfg = new DataStoreConfig();
	String MATRIX = PsApi.MATRIX;
	String matrixURL = cfg.getProperty("storeURL") + MATRIX + '/' + matrixId;
        
        try{
            
            URL url = new URL(matrixURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));	
            Object obj = parser.parse(reader);
            JSONObject matrixObj = (JSONObject)obj;
            
            String matrixName = (String)matrixObj.get("name");
            JSONArray statusLabels = (JSONArray)matrixObj.get("statusLabels");
            String lastUpdateTime = (String) matrixObj.get("lastUpdateTime");
	    String detailLevel = (String) matrixObj.get("detailLevel");	
            String serviceTypeId = (String)matrixObj.get("serviceTypeId");
            JSONArray columns = (JSONArray)matrixObj.get("columns");
            JSONArray serviceNames = (JSONArray)matrixObj.get("serviceNames");
            JSONArray rows = (JSONArray)matrixObj.get("rows");  
            
            //get the detail information of matrix
            //1. matrix JSONArray of rows
            //2. row JSONArray of cells
            //3. cell of two tests(upper and lower)
            //4. test: result and parameters
            
            JSONArray matrix_list = (JSONArray)matrixObj.get("matrix");
            
            Cell[][] matrixCells;
             
            if(! matrix_list.isEmpty()){
                
                 //get the number of rows in a matrix
		int row_num = matrix_list.size();
				
		//get the JSONArry of cells in one row
		JSONArray row_list = (JSONArray) matrix_list.get(0);
				
		//get the number of cells in one row
		int cell_num_per_row = row_list.size();
                
                matrixCells = new Cell[row_num][cell_num_per_row];
                
                for(int row=0 ; row<row_num ; row++){
					
                    //get one row
                    row_list =  (JSONArray)matrix_list.get(row);
					
                    for(int c=0 ; c<cell_num_per_row ; c++){
						
			//get one cell
       			JSONArray cell_list = (JSONArray)row_list.get(c);
						
                        Cell cell = new Cell();
			Test[] test = new Test[2];
						
			JSONObject test_up_obj = (JSONObject)cell_list.get(0);
			JSONObject test_low_obj = (JSONObject)cell_list.get(1);
									
						
			if(test_up_obj.isEmpty() && test_low_obj.isEmpty()){
							
                            // the cross corner of the matrix			
                            matrixCells[row][c] = null;
							
			}else{
                            //cells not the corner in the matrix				
                            for(int t=0 ; t<2 ; t++){
								
				JSONObject test_obj = (JSONObject)cell_list.get(t);
				Test one_test = new Test();
									
                                String testId = (String) test_obj.get("id");
				String description = (String) test_obj.get("description");
				String testName = (String) test_obj.get("name");
				String runningSince = (String) test_obj.get("runningSince");
				String nextCheckTime = (String) test_obj.get("nextCheckTime");
				long checkInterval = (Long) test_obj.get("checkInterval");
				String prevCheckTime = (String) test_obj.get("prevCheckTime");
                                String type = (String) test_obj.get("type");
				boolean running = (Boolean)test_obj.get("running");
                                long timeout = (Long) test_obj.get("timeout");
                                
                                JSONObject testResult_obj = (JSONObject)test_obj.get("result");
				JSONObject testParameters_obj = (JSONObject)test_obj.get("parameters");
									
				TestResult testResult = new TestResult();  //get the result object of a test
									
				if(testResult_obj.isEmpty()){
					testResult = null;
				}else{
                                    String resultMessage = (String) testResult_obj.get("message");
                                    String resultId = (String) testResult_obj.get("id");
                                    String resultJobId = (String) testResult_obj.get("job-id");
                                    String resultTime = (String) testResult_obj.get("time");
                                    long resultStatus = (Long)testResult_obj.get("status");
                                    String resultServiceId= (String) testResult_obj.get("service-id");
                                    
                                    JSONObject resultParameters_obj = (JSONObject) testResult_obj.get("parameters");
										
                                    ResultParameters resultParameters = new ResultParameters();
										
                                    if(resultParameters_obj.isEmpty()){
                                        resultParameters = null;
                                    }else{
                                        String min = (String)resultParameters_obj.get("min");
					String max = (String)resultParameters_obj.get("max");
                                        int count = Integer.parseInt((String) resultParameters_obj.get("count"));					
                                        String command = (String)resultParameters_obj.get("command");
					String standardDeviation = (String)resultParameters_obj.get("standard-deviation");
					String average = (String)resultParameters_obj.get("average");
                                        
                                        //make resultParameters object
                                        resultParameters.setMin(min);
                                        resultParameters.setMax(max);
                                        resultParameters.setCount(count);
                                        resultParameters.setCommand(command);
                                        resultParameters.setStandardDeviation(standardDeviation);
                                        resultParameters.setAverage(average);
										
                                       
                                    }//resultParameters finished
                                    
                                    //make testResult object
                                    testResult.setMessage(resultMessage);
                                    testResult.setResultId(resultId);
                                    testResult.setJobId(resultJobId);
                                    testResult.setTime(resultTime);
                                    testResult.setStatus(resultStatus);
                                    testResult.setServiceId(resultServiceId);
                                    testResult.setParameters(resultParameters);
                                   
				}// testResult finished
									
				TestParameters testParameters = new TestParameters();  //get the result object of a test
									
				if(testParameters_obj.isEmpty()){
                                    
                                    testParameters = null;
										
				}else{
                                    String warningThreshold = (String) testParameters_obj.get("warningThreshold");
                                    String monitor = (String) testParameters_obj.get("monitor");
                                    String destinationHostId = (String) testParameters_obj.get("destination-host-id");
                                    String timeRange = (String) testParameters_obj.get("timeRange");
                                    String source = (String)testParameters_obj.get("source");
                                    String maHostId= (String) testParameters_obj.get("ma-host-id");
                                    String paraUrl = (String) testParameters_obj.get("url");
                                    String sourceHostId = (String) testParameters_obj.get("source-host-id");
                                    String criticalThreshold = (String)testParameters_obj.get("criticalThreshold");
                                    String destination= (String) testParameters_obj.get("destination");
				    
                                    
                                    //make testParameters object
                                    testParameters.setWarningThreshold(warningThreshold) ;
                                    testParameters.setMonitor(monitor) ;
                                    testParameters.setDestinationHostId(destinationHostId) ;
                                    testParameters.setTimeRange(timeRange) ;
                                    testParameters.setSource(source) ;
                                    testParameters.setMaHostId(maHostId);
                                    testParameters.setUrl(paraUrl) ;
                                    testParameters.setSourceHostId(sourceHostId) ;
                                    testParameters.setCriticalThreshold(criticalThreshold) ;
                                    testParameters.setDestination(destination) ;
				}// testParameters finished
									
				//get every test object; two in total
				one_test.setTestId(testId);
                                one_test.setTestName(testName);
                                one_test.setResult(testResult);
                                one_test.setDescription(description);
                                one_test.setRunningSince(runningSince);
                                one_test.setNextCheckTime(nextCheckTime);
                                one_test.setCheckInterval(checkInterval);
                                one_test.setParameters(testParameters);
                                one_test.setPrevCheckTime(prevCheckTime);
                                one_test.setType(type);
                                one_test.setRunning(running);
                                one_test.setTimeout(timeout);
                                
                                
				test[t] = one_test;
                                
                                //System.out.println(" Query test"+ t+ ": "+ test[t]);
                            } //loop tests in cell finished		
							
                        }//get one cell finished 
		
                        //make cell object
                        cell.setUpperCell(test[0]);
                        cell.setLowerCell(test[1]);
                    
                        // put one cell in matrix
                        matrixCells[row][c] = cell;
						
                    }// one row finshed
					
                }// matrix fished	
                
            }else{
            
                matrixCells = null;
            }
            
            one_matrix.setMatrixId(matrixId);
            one_matrix.setStatusLabels(statusLabels);
            one_matrix.setLastUpdateTime(lastUpdateTime);
            one_matrix.setDetailLevel(detailLevel);
            one_matrix.setMatrixCells(matrixCells);
            one_matrix.setMatrixName(matrixName);
            one_matrix.setColumns(columns);
            one_matrix.setServiceNames(serviceNames);
            one_matrix.setRows(rows);
        
        }catch(IOException e){
            e.printStackTrace();
            one_matrix = null;
	}catch(ParseException e){
            e.printStackTrace();
            one_matrix = null;
	}catch(ClassCastException e){
            e.printStackTrace();
            one_matrix = null;
	}
		
        
        return one_matrix; 
    }
    
}
