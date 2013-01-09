/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.data_store.PsHostQuery;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsHostManipulator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * delete all primitive services associated with a host with the given id or
 * hostName return JSON host object of the host
 *
 * @author tomw
 */
//@WebServlet(name = "PsDeleteAllServicesFromHost", urlPatterns = {"/hostDeleteAllServices"})
public class PsDeleteAllServicesFromHost extends HttpServlet {

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
//            /*
//             * TODO output your page here. You may use following sample code.
//             */
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet PsDeleteAllServicesFromHost</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet PsDeleteAllServicesFromHost at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
            
            PsDataStore dataStore = PsDataStore.getDataStore();

            PsHost host = null;

            String id = request.getParameter("id");
            if (id != null && !"".equals(id)) {
                host = dataStore.getHost(id);
                PsHostManipulator.removeAllServices(host);
                out.println(JsonConverter.toJson(host));
            }
            
            String hostName = request.getParameter("hostName");
            if (hostName != null && !"".equals(hostName)) {
                host = PsHostQuery.getHostByName(hostName);
                PsHostManipulator.removeAllServices(host);
                out.println(JsonConverter.toJson(host));
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
