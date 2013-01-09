/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_debug;

import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_objects.PsServiceResult;
import gov.bnl.racf.ps.dashboard.html_utils.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
@WebServlet(name = "TestHtml", urlPatterns = {"/TestHtml"})
public class TestHtml extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TestHtml</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestHtml at " + request.getContextPath() + "</h1>");

            // declare a table
            HtmlTable htmlTable = new HtmlTable();
            int numberOfColumns = 3;
            int numberOfRows = 3;

            // declare a header row
            HtmlTableHeaderRow headerRow = new HtmlTableHeaderRow();

            HtmlText firstCellText = new HtmlText("---");
            HtmlTableHeaderCell firstHeaderCell = new HtmlTableHeaderCell(firstCellText);
            headerRow.addCell(firstHeaderCell);


            for (int column = 0; column < numberOfColumns; column = column + 1) {

                HtmlText columnText = new HtmlText("column " + column);
                HtmlTableHeaderCell headerCell = new HtmlTableHeaderCell(columnText);
                headerRow.addCell(headerCell);
            }
            // Ok, we have created fors row of table, let us add it to table
            htmlTable.addHeaderRow(headerRow);



            // now let us add rows


            //loop over rows and keep adding them

            for (int row = 0; row < numberOfRows; row = row + 1) {

                String rowName = "row " + row;

                // create new row in the table
                HtmlTableRow htmlTableRow = new HtmlTableRow();

                //add first cell
                HtmlText firstCellInRowext = new HtmlText(rowName);
                HtmlTableCell firstCellInRow = new HtmlTableCell(firstCellInRowext);
                firstCellInRow.alignLeft();
                htmlTableRow.addCell(firstCellInRow);

                // let us loop over columns

                for (int column = 1; column < numberOfColumns; column++) {
                    // cell in table should in fact contain a small html table
                    // with one column and two rows, which will display
                    // one service above another
                    HtmlTable tableInCell = new HtmlTable();


                    // first service 
                    String url1 = "http://yahoo.com";
                    String cellText1 = "yahoo";
                    String message1 = "this is link to yahoo";
                    HtmlLink link1 = new HtmlLink(url1,
                            cellText1,
                            message1);
                    // let us make one cell green
                    HtmlColor cellColor1 = new HtmlColor(HtmlColor.GREEN);
                    HtmlTableCell upperCell = new HtmlTableCell(link1, cellColor1);
                    HtmlTableRow upperRow = new HtmlTableRow();
                    upperRow.addCell(upperCell);
                    tableInCell.addRow(upperRow);

                    // second service
                    String url2 = "http://google.com";
                    String cellText2 = "google";
                    String message2 = "this is link to google";
                    HtmlLink link2 = new HtmlLink(url2,
                            cellText2,
                            message2);
                    // let us make one cell red
                    HtmlColor cellColor2 = new HtmlColor(HtmlColor.RED);
                    HtmlTableCell lowerCell = new HtmlTableCell(link2, cellColor2);
                    HtmlTableRow lowerRow = new HtmlTableRow();
                    lowerRow.addCell(lowerCell);
                    tableInCell.addRow(lowerRow);

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
                }
                htmlTable.addRow(htmlTableRow);
            }

            out.println(htmlTable.toHtml());




            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
