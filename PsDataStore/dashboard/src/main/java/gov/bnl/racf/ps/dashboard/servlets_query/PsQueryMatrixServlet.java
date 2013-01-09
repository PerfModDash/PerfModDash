/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_query;

import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.data_store.PsMatrixQuery;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * if no parameters: return JSONArray of id's of known matrices otherwise:
 * return JSON object of matrix with specified id or name
 *
 * @author tomw
 */
//@WebServlet(name = "PsQueryMatrixServlet", urlPatterns = {"/matrices"})
public class PsQueryMatrixServlet extends HttpServlet {

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

            PsDataStore dataStore = PsDataStore.getDataStore();

            String matrixId = request.getParameter("id");
            String matrixName = request.getParameter("name");

            if (matrixId == null && matrixName == null) {
                // return list of id's
                JSONArray jsonArray = new JSONArray();
                Iterator<PsMatrix> iter = PsMatrixQuery.getMatrixIterator();
                while (iter.hasNext()) {
                    PsMatrix currentMatrix = (PsMatrix) iter.next();
                    String currentMatrixId = currentMatrix.getId();
                    jsonArray.add(currentMatrixId);
                }
                out.println(jsonArray.toString());
            } else {
                if (matrixId != null) {
                    PsMatrix matrix = PsMatrixQuery.getMatrixById(matrixId);
                    JSONObject matrixAsJson = JsonConverter.toJson(matrix);
                    out.println(matrixAsJson.toString());
                }
                if (matrixName != null) {
                    PsMatrix matrix = PsMatrixQuery.getMatrixByName(matrixName);
                    JSONObject matrixAsJson = JsonConverter.toJson(matrix);
                    out.println(matrixAsJson.toString());
                }
            }

        } catch (Exception e) {
            System.out.println(new Date() + " " + getClass().getName() + " " + e);
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
