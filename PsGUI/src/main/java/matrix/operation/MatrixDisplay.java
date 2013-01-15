/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.operation;

import java.util.ArrayList;
import matrix.bean.Cell;
import matrix.bean.Matrix;
import matrix.bean.ResultParameters;
import matrix.bean.Test;
import matrix.bean.TestParameters;
import matrix.bean.TestResult;
import org.json.simple.JSONArray;
import table.HtmlColor;
import table.HtmlLink;
import table.HtmlTable;
import table.HtmlTableCell;
import table.HtmlTableHeaderCell;
import table.HtmlTableHeaderRow;
import table.HtmlTableRow;
import table.HtmlText;

/**
 * The class to display a matrix as a HTML table.
 * @author siliu
 */
public class MatrixDisplay {
    /**
     * The method to execute the display of a matrix in HTML table by providing the matrix object.
     * @param one_matrix The matrix that should be displayed.
     * @return HtmlTable object
     */
    public static HtmlTable executeMatrixDisplay(Matrix one_matrix){
        
        //declare the table to display the matrix
        HtmlTable htmlTable = new HtmlTable();
        
        String matrixId = one_matrix.getMatrixId();
        String matrixName = one_matrix.getMatrixName();
        JSONArray statusLabels = one_matrix.getStatusLabels();
        String lastUpdateTime = one_matrix.getLastUpdateTime();
        String detailLevel = one_matrix.getDetailLevel();
        String serviceTypeId = one_matrix.getServiceTypeId();
        JSONArray columns = one_matrix.getColumns();
        JSONArray serviceNames = one_matrix.getServiceNames();
        JSONArray rows = one_matrix.getRows();
        Cell[][] matrixCells = one_matrix.getMatrixCells();
        
        Cell one_cell;
	Test[] test = new Test[2];
        
        if(matrixCells != null){
            
            int row_num = matrixCells.length;
            int cell_num_per_row = (matrixCells[0]).length;
				
            // get the columes and rows in the table
            int numberOfColumns = columns.size();
            int numberOfRows = rows.size();
		        
            // declare a header row
            HtmlTableHeaderRow headerRow = new HtmlTableHeaderRow();
	    HtmlText firstCellText = new HtmlText("---");	        
            HtmlTableHeaderCell firstHeaderCell = new  HtmlTableHeaderCell(firstCellText);
            headerRow.addCell(firstHeaderCell);
		        
            for (int column = 0; column < numberOfColumns; column = column + 1){
		     /*
		            HtmlText columnText = new HtmlText(columns[column]);
		            HtmlTableHeaderCell headerCell = new  HtmlTableHeaderCell(columnText);
		            headerRow.addCell(headerCell);
                        */
                String columnText = (String) columns.get(column);
                String columnURL = "http://" + columnText + "/toolkit/";
                HtmlLink columnLink = new HtmlLink(columnURL,columnText);
                HtmlTableHeaderCell headerCell = new  HtmlTableHeaderCell(columnLink);
                headerRow.addCell(headerCell);
             }
		        
            // Ok, we have created first row of table, let us add it to table	        
            htmlTable.addHeaderRow(headerRow);
				
				
            for(int i=0; i<row_num ; i++){

                // create new row in the table
	        HtmlTableRow htmlTableRow = new HtmlTableRow();

                //add first cell
	        //HtmlText firstCellInRowText = new HtmlText(rowName)
                String rowName = (String) rows.get(i);
                String rowURL = "http://" + rowName + "/toolkit/";
		HtmlLink rowLink = new HtmlLink(rowURL,rowName);
		            
                HtmlTableCell firstCellInRow = new  HtmlTableCell(rowLink);
                firstCellInRow.alignLeft();
                htmlTableRow.addCell(firstCellInRow);
							
		for(int j=0; j<cell_num_per_row ; j++){
						
                    // cell in table should in fact contain a small html table
		    // with one column and two rows, which will display
		    // one service above another
                    HtmlTable tableInCell = new HtmlTable();
                    one_cell = matrixCells[i][j];
		    //out.println(one_cell + "<br>");
						
                    test[0] = one_cell.getUpperCell();
                    test[1] = one_cell.getLowerCell();
						
                    if(test[0] == null && test[1] == null){
							
                        //out.println("This is the cross corner!");				
                        HtmlText cornerCellText = new HtmlText("---");
					        
                        HtmlTableCell cornerCell = new  HtmlTableCell(cornerCellText);
		        HtmlTableRow cornerCellRow = new HtmlTableRow();
                        cornerCellRow.addCell(cornerCell);					        
                        tableInCell.addRow(cornerCellRow);
      		        tableInCell.addRow(cornerCellRow);
								
                    }else{
                        
			for(int t=0 ; t<2 ; t++){
                            
                            System.out.println("test"+ t+ ": "+ test[t]);
                            
                            if(test[t] != null){
								
                            String testId = test[t].getTestId();
                            String description = test[t].getDescription();
                            String testName = test[t].getTestName();
                            String runningSince = test[t].getRunningSince();
                            String nextCheckTime = test[t].getNextCheckTime();
                            long checkInterval = test[t].getCheckInterval();
                            String prevCheckTime = test[t].getPrevCheckTime();
                            String type = (String) test[t].getType();
                            boolean running = test[t].getRunning();
                            long timeout = test[t].getTimeout();
                            TestResult testResult = test[t].getResult();
			    TestParameters testParameters = test[t].getParameters();
								
								
                            if( testResult != null){
				String resultMessage = testResult.getMessage();
                                String resultId = testResult.getResultId();
                                String resultJobId = testResult.getJobId();
                                String resultTime = testResult.getTime();
                                long resultStatus = testResult.getStatus();
                                String resultServiceId= testResult.getServiceId();
				ResultParameters resultParameters = testResult.getParameters();					
				
									
				if(testParameters != null){
										
                                    String warningThreshold = testParameters.getWarningThreshold();
                                    String monitor = testParameters.getMonitor() ;
                                    String destinationHostId = testParameters.getDestinationHostId() ;
                                    String timeRange = testParameters.getTimeRange();
                                    String source = testParameters.getSource();
                                    String maHostId= testParameters.getMaHostId();
                                    String paraUrl = testParameters.getUrl();
                                    String sourceHostId = testParameters.getSourceHostId();
                                    String criticalThreshold = testParameters.getCriticalThreshold();
                                    String destination= testParameters.getDestination();
                                    
                                    
                                    String min = "0.00";
                                    String max = "0.00";
                                    int count = 0;
                                    String command = null;
                                    String standardDeviation = null;
                                    String average = "0.00" ;
										
                                    if(resultParameters != null){
                                        
                                        //cut min, max, average to the length of two characterator behind "."
					String min_original = resultParameters.getMin();
                                        int min_comma_index = min_original.indexOf(".");
                                        min = min_original.substring(0, min_comma_index+3);
											
					String max_original = resultParameters.getMax();
                                        int max_comma_index = max_original.indexOf(".");
                                        max = max_original.substring(0, max_comma_index+3);
											
					String ave_original = resultParameters.getAverage();
                                        int ave_comma_index = ave_original.indexOf(".");
                                        average = ave_original.substring(0, ave_comma_index+3);
											
                                        count = resultParameters.getCount();
                                        command = resultParameters.getCommand();
                                        standardDeviation = resultParameters.getStandardDeviation();
                                        
                                        
                                        
                                    }else{
                                        System.out.println("The test result parameter is null!");
                                    }// resultParameters object end 
                                    
                                        
                                        //send test result information to the test url to display in a new page
                                        //put test result which would like to display in an array list as a parameters to pass
                                    
                                    ArrayList test_result = new ArrayList();
                                    
                                    test_result.add(resultStatus);						
                                    test_result.add(resultMessage);						
                                    test_result.add(monitor);
                                    test_result.add(source);
                                    test_result.add(destination);
                                    test_result.add(resultTime);
                                    test_result.add(min);
                                    test_result.add(max);
                                    test_result.add(average);
                                    test_result.add(command);
                                    test_result.add(count);
                                    test_result.add(checkInterval);
                                    test_result.add(nextCheckTime);
                                    
                                    // display a test in one cell
                                    String url = "displayMatrixTest?test_detail=" + test_result;
                                    String cellText = average;
                                    String linkMessage = "Initiator: " + monitor + "; From: " + source  + "; To: " + destination ;
                                    HtmlLink link = new HtmlLink(url,cellText, linkMessage);
                                            
                                    // let us make one cell green
                                    HtmlColor cellColor = null;
                                    if(resultStatus == 0){
                                        
                                        cellColor = new HtmlColor(HtmlColor.GREEN);
                                        
                                    }else if (resultStatus == 1){
						
                                        cellColor = new HtmlColor(HtmlColor.YELLOW);
											
                                    }else if(resultStatus == 2){
											
                                        cellColor = new HtmlColor(HtmlColor.RED);
											
                                    }else if(resultStatus == 3){
											
                                        cellColor = new HtmlColor(HtmlColor.BROWN);
											
                                    }else if(resultStatus == 4){
											
                                        cellColor = new HtmlColor(HtmlColor.GREY);
                                    }
                                     
																
                                    HtmlTableCell halfCell = new HtmlTableCell(link, cellColor);		                
                                    HtmlTableRow one_row = new HtmlTableRow();
                                    one_row.addCell(halfCell);
                                    tableInCell.addRow(one_row);	
				}else{
				//out.println("test parameters is null!");
                                System.out.println("testParameters: Not all services are accounted for yet, reload matrix in a few minutes.");
                                }					
                            }else{
				//out.println("test result is null!");
                                System.out.println("testResult: Not all services are accounted for yet, reload matrix in a few minutes.");
                            }
                          }//test not null					
                        }//deal with two tests in one cell 
                    }//	 not cross corner cell 
						
                    // ok, we have the small table which contains two services
                    // we need to make its border invisible
                    tableInCell.setBorder("0"); 		                
		                
                    // now we need to make a cell in the  big table which will contain the
                    // small table
                    String cellContent = tableInCell.toHtml();
                    HtmlText textOfCell = new HtmlText(cellContent);
                    //HtmlText textOfCell=new HtmlText(rowCounter+" "+columnIndex);
                    HtmlTableCell cell = new HtmlTableCell(textOfCell);
                    htmlTableRow.addCell(cell);
						
                }//cells per row end
            
                htmlTable.addRow(htmlTableRow);
            }// rows per matrix end
            
            
				
        }else{
				
            System.out.println("This Matrix is empty!");
        }
        
        return htmlTable;
    
    }
    
}
