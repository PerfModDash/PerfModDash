/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;

import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectCreator;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsServiceTypeFactory;
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
 *
 * @author tomw
 */
//@WebServlet(name = "PsCreateMatrixThroughputServlet", urlPatterns = {"/createThroughputMatrix"})
public class PsCreateMatrixThroughputServlet extends HttpServlet {

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




        System.out.println(new Date() + " " + getClass().getName() + " Point of entry!");
        System.out.flush();

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet PsCreateMatrixThroughputServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet PsCreateMatrixThroughputServlet at " + request.getContextPath() + "</h1>");


        try {
            /*
             * TODO output your page here. You may use following sample code.
             */


            //try {

            System.out.println(new Date() + " " + getClass().getName() + " Point A");
            PsDataStore dataStore = PsDataStore.getDataStore();

            PsServiceType throughputType =
                    dataStore.getServiceType(PsServiceTypeFactory.THROUGHPUT);

            PsMatrix newMatrix = PsObjectCreator.createNewMatrix(throughputType);

            String matrixName = "";
            matrixName = request.getParameter("name");
            if (matrixName != null && !"".equals(matrixName)) {
                newMatrix.setName(matrixName);
            }
            out.println("Matrix created, convert it to JSON<BR>");
            
            JSONObject json = JsonConverter.toJson(newMatrix);

            out.println(json.toString());
            out.println("Matrix printed<BR>");



        } catch (Exception e) {
            System.out.println(new Date() + " " + getClass().getName() + " " + e);


        } finally {

            out.println("</body>");
            out.println("</html>");
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
