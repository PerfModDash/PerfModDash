/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;

import gov.bnl.racf.ps.dashboard.data_objects.PsCloud;
import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_store.PsCloudQuery;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.data_store.PsMatrixQuery;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 * Remove matrix from a cloud.
 * 
 * arguments: cloudId = id of the cloud or cloudName -
 * name of the cloud
 *
 * matrixId = id of the matrix of matrixName - name of the matrix
 *
 * The matrix itself is not deleted.
 *
 * return JSONObject of the cloud
 * @author tomw
 */
//@WebServlet(name = "PsCloudRemoveMatrix", urlPatterns = {"/cloudRemoveMatrix"})
public class PsCloudRemoveMatrix extends HttpServlet {

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
        
         PsDataStore dataStore = PsDataStore.getDataStore();
        
        try {
            PsMatrix matrix = null;

            String matrixId = request.getParameter("matrixId");
            if (matrixId != null) {
                if (!"".equals(matrixId)) {
                    matrix = dataStore.getMatrix(matrixId);
                }
            } else {
                String matrixName = request.getParameter("matrixName");
                if (matrixName != null) {
                    if (!"".equals(matrixName)) {
                        matrix = PsMatrixQuery.getMatrixByName(matrixName);
                    }
                }
            }
            
            if(matrix==null){
                throw new Exception("Neither matrixId not matrixName parameter specified");
            }
            
            PsCloud cloud=null;
            
            String cloudId = request.getParameter("cloudId");
            if (cloudId != null) {
                if (!"".equals(cloudId)) {
                    cloud = dataStore.getCloud(cloudId);
                }
            } else {
                String cloudName = request.getParameter("cloudName");
                if (cloudName != null) {
                    if (!"".equals(cloudName)) {
                        cloud=PsCloudQuery.getCloudByName(cloudName);
                    }
                }
            }

            if(cloud==null){
                throw new Exception("Neither cloudId nor cloudName parameter specified");
            }
            
            cloud.removeMatrix(matrix);
            
            JSONObject cloudJson = JsonConverter.toJson(cloud);
            out.println(cloudJson);
            
            
            
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
