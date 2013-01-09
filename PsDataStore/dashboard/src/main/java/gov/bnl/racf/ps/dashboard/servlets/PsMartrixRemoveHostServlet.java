/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.data_store.PsHostQuery;
import gov.bnl.racf.ps.dashboard.data_store.PsMatrixQuery;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsMatrixManipulator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Remove host from matrix. matrix is defined either by matrixName or matrixId.
 * host is defined either by hostName or hostId
 *
 * @author tomw
 */
//@WebServlet(name = "PsMartrixRemoveHostServlet", urlPatterns = {"/removeHostFromMatrix"})
public class PsMartrixRemoveHostServlet extends HttpServlet {

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

            if (matrix == null) {
                throw new Exception("Neither matrixId not matrixName parameter specified");
            }

            PsHost host = null;

            String hostId = request.getParameter("hostId");
            if (hostId != null) {
                if (!"".equals(hostId)) {
                    host = dataStore.getHost(hostId);
                }
            } else {
                String hostName = request.getParameter("hostName");
                if (hostName != null) {
                    if (!"".equals(hostName)) {
                        host = PsHostQuery.getHostByName(hostName);
                    }
                }
            }

            if (host == null) {
                throw new Exception("Neither hostId nor hostName parameter specified");
            }

            PsMatrixManipulator.removeHostFromMatrix(matrix, host);

            out.println(JsonConverter.toJson(matrix));



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
