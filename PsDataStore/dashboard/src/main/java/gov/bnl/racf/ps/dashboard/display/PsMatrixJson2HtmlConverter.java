/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.display;

import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_objects.PsServiceResult;
import gov.bnl.racf.ps.dashboard.html_utils.*;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Convert matrix Json representation of a matrix to a html table
 *
 * @author tomw
 */
public class PsMatrixJson2HtmlConverter {

    public static HtmlTable convert(JSONObject json) {
        // first order of business is to establish how many columns does the 
        // matrix have
        JSONArray columns = (JSONArray) json.get(PsMatrix.COLUMNS);
        int numberOfColumnsInMatrix = columns.size();
        int numberOfColumnsInTable = numberOfColumnsInMatrix + 1;

        // declare a table
        HtmlTable htmlTable = new HtmlTable();

        // declare a header row
        HtmlTableHeaderRow headerRow = new HtmlTableHeaderRow();

        HtmlText firstCellText = new HtmlText("---");
        HtmlTableHeaderCell firstHeaderCell = new HtmlTableHeaderCell(firstCellText);
        headerRow.addCell(firstHeaderCell);

        Iterator columnIterator = columns.iterator();
        int columnCounter = 0;
        while (columnIterator.hasNext()) {
            String columnName = (String) columnIterator.next();
            HtmlText columnText = new HtmlText("" + columnCounter);

            HtmlLink columnLink = new HtmlLink("httm://localhost",
                    "" + columnCounter,
                    columnName);

            columnCounter = columnCounter + 1;
            HtmlTableHeaderCell headerCell = new HtmlTableHeaderCell(columnLink);
            headerRow.addCell(headerCell);
        }
        htmlTable.addHeaderRow(headerRow);

        // let us extract matrix information
        JSONArray matrix = (JSONArray) json.get(PsMatrix.MATRIX);

        // now let us add rows

        // first order of business is : how many rows do we have?
        JSONArray rows = (JSONArray) json.get(PsMatrix.ROWS);
        int numberOfRows = rows.size();

        //loop over rows and keep adding them
        int rowCounter = -1;
        Iterator rowsIterator = rows.iterator();
        while (rowsIterator.hasNext()) {
            rowCounter = rowCounter + 1;

            // let us extract current row from JSON
            JSONArray jsonRow = (JSONArray) matrix.get(rowCounter);

            String rowName = (String) rowsIterator.next();
            HtmlTableRow htmlTableRow = new HtmlTableRow();

            //add first cell
            HtmlText firstCellInRowext = new HtmlText(rowName);
            HtmlTableCell firstCellInRow = new HtmlTableCell(firstCellInRowext);
            firstCellInRow.alignLeft();
            htmlTableRow.addCell(firstCellInRow);

            // let us loop over columns
            System.out.println("Point A");
            for (int columnIndex = 0; columnIndex < numberOfColumnsInMatrix; columnIndex++) {

                System.out.println("Point B");

                //extract column columnIndex from row
                JSONArray matrixElement = (JSONArray) jsonRow.get(columnIndex);

                System.out.println("Point C");

                //loop over matrix elements
                HtmlTable tableInCell = new HtmlTable();
                Iterator matrixElementIterator = matrixElement.iterator();

                System.out.println("Point D");

                while (matrixElementIterator.hasNext()) {
                    System.out.println("Point E");
                    JSONObject matrixElementJson =
                            (JSONObject) matrixElementIterator.next();
                    if (matrixElementJson.containsKey(PsService.RESULT)) {
                        JSONObject resultObject =
                                (JSONObject) matrixElementJson.get(PsService.RESULT);
                        System.out.println("result keys: ");
                        Iterator keys = resultObject.keySet().iterator();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            System.out.println("key=" + key);
                        }
                        String message = (String) resultObject.get(PsServiceResult.MESSAGE);

                        System.out.println("get status:");
                        System.out.println("result is " + resultObject.toString());

                        String status = "";
                        if (resultObject.keySet().contains(PsServiceResult.STATUS)) {
                            Integer statusInteger = (Integer) resultObject.get(PsServiceResult.STATUS);
                            status = statusInteger.toString();
                        }
                        System.out.println("status=" + status);

                        String pageUrl = "http://localhost";
                        
                        //String cellText = rowCounter + " " + columnIndex;
                        String cellText = "no data";
                        if (resultObject.keySet().contains(PsServiceResult.PARAMETERS)){
                            JSONObject parametersJson=
                                    (JSONObject)resultObject.get(PsServiceResult.PARAMETERS);
                            if(parametersJson.keySet().contains("average")){
                                String average=(String)parametersJson.get("average");
                                average=average.replace("Gbps","");
                                average=average.replace("pps","");
                                
                                cellText=
                                        average.substring(0,Math.min(4,average.length()));
                            }
                        }
                        
                        
                        
                        System.out.println("Point F");
                        HtmlLink link = new HtmlLink(pageUrl,
                                cellText,
                                message);
                        System.out.println("Point G");

                        HtmlColor cellColor =
                                PsStatus2HtmlColorConverter.convert(status);

                        HtmlTableCell cell = null;
                        if (cellColor == null) {
                            cell = new HtmlTableCell(link);
                        } else {
                            cell = new HtmlTableCell(link, cellColor);
                        }

                        HtmlTableRow row = new HtmlTableRow();
                        row.addCell(cell);
                        tableInCell.addRow(row);
                    } else {
                        HtmlTableCell cell = new HtmlTableCell("---");
                        HtmlTableRow row = new HtmlTableRow();
                        row.addCell(cell);
                        tableInCell.addRow(row);
                    }

                }
                tableInCell.setBorder("0");

                String cellContent = tableInCell.toHtml();
                HtmlText textOfCell = new HtmlText(cellContent);
                //HtmlText textOfCell=new HtmlText(rowCounter+" "+columnIndex);
                HtmlTableCell cell = new HtmlTableCell(textOfCell);
                htmlTableRow.addCell(cell);
            }

            // let us add the row to table
            htmlTable.addRow(htmlTableRow);
        }
        return htmlTable;
    }
}
