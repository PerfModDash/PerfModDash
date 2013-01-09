/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_debug;

import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.display.PsMatrixJson2HtmlConverter;
import gov.bnl.racf.ps.dashboard.html_utils.HtmlTable;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.servlets.rest.UrlUnpacker;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
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
//@WebServlet(name = "DisplayMatrixServlet", urlPatterns = {"/displayMatrix"})
public class DisplayMatrixServlet extends HttpServlet {

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


            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            PsDataStore dataStore = PsDataStore.getDataStore();

            if (parameters.size() > 0) {
                String matrixIdString = parameters.firstElement();
                PsMatrix matrix = dataStore.getMatrix(matrixIdString);
                JSONObject matrixAsJson = JsonConverter.toJson(matrix);
                Iterator keys = matrixAsJson.keySet().iterator();
//                while(keys.hasNext()){
//                    String key = (String)keys.next();
//                    out.println(key+"<BR>");
//                }
                JSONArray rows = (JSONArray)matrixAsJson.get("rows");
//                out.println("rows: "+rows.toString()+"<BR>");
                
                JSONArray columns = (JSONArray)matrixAsJson.get("rows");
//                out.println("columns : "+columns.toString()+"<BR>");
                
//                out.println(matrixAsJson.get("matrix").toString()+"<BR>");
                
                out.println("<BR>+++++++++++++++++++++++++++++++++<BR>");
                out.println("display mtrix html<BR>");
                
                HtmlTable table = PsMatrixJson2HtmlConverter.convert(matrixAsJson);
                System.out.println("We are back from PsMatrixJson2HtmlConverter");
                
                out.println(table.toHtml());
                System.out.println("Point B");
                out.println("<BR>+++++++++++++++++++++++++++++++++<BR>");
                
                out.println(matrixAsJson.get("matrix").toString()+"<BR>");
                System.out.println("Point C");
//                out.println(matrixAsJson.toString());
            } else {
                JSONArray listOfMatrixIds = new JSONArray();
                Iterator<PsMatrix> iter = dataStore.matrixIterator();
                while (iter.hasNext()) {
                    PsMatrix matrix = (PsMatrix) iter.next();
                    listOfMatrixIds.add(matrix.getId());
                }
                out.println(listOfMatrixIds.toString());
            }

        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
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
