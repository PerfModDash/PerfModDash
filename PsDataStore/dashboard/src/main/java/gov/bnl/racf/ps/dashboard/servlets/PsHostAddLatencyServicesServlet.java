/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsHostManipulator;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectCreator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * take host with given id and add to it latency primitive services the relevant
 * services are created and added to datastore if no host id is specified a new
 * host is created from scratch and is fitted with latency services.
 *
 * @author tomw
 */
//@WebServlet(name = "PsHostAddLatencyServicesServlet", urlPatterns = {"/hostAddLatencyServices"})
public class PsHostAddLatencyServicesServlet extends HttpServlet {

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
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet PsHostAddLatencyServicesServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet PsHostAddLatencyServicesServlet at " + request.getContextPath() + "</h1>");

            PsDataStore dataStore = PsDataStore.getDataStore();

            PsHost host = null;

            String id = request.getParameter("id");

            if (id != null && !"".equals(id)) {
                host = dataStore.getHost(id);
                PsHostManipulator.addLatencyServices(host);
            } else {
                if (request.getParameter("hostName") != null
                        && request.getParameter("ipv4") != null) {
                    String hostName= request.getParameter("hostName");
                    String ipv4 = request.getParameter("ipv4");
                    host = PsObjectCreator.createNewHost(hostName, ipv4);
                    PsHostManipulator.addLatencyServices(host);
                }
            }
            out.println(JsonConverter.toJson(host));

        }catch (Exception e){
            System.out.println(new Date()+" "+getClass().getName()+" "+e);
//            out.println("</body>");
//            out.println("</html>");
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
