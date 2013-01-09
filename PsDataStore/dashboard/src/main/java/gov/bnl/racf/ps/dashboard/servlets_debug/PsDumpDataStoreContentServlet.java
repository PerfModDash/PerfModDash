/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_debug;

import gov.bnl.racf.ps.dashboard.data_store.PsDataStoreDump;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Dumps content of data store to the web page. To be used for debugging. Not to
 * be used for any purpose in production.
 *
 * @author tomw
 */
//@WebServlet(name = "PsDumpDataStoreContentServlet", urlPatterns = {"/dump"})
public class PsDumpDataStoreContentServlet extends HttpServlet {

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
            out.println("<title>Servlet PsDumpDataStoreContentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsDumpDataStoreContentServlet at " + request.getContextPath() + "</h1>");

            out.println("<h2>dump service types</h2>");
            out.println(PsDataStoreDump.serviceTypes());
            out.println("<BR>====================================<BR>");

            out.println("<h2>dump hosts</h2>");
            out.println(PsDataStoreDump.hosts());
            out.println("<BR>====================================<BR>");


            out.println("<h2>dump services</h2>");
            out.println(PsDataStoreDump.services());
            out.println("<BR>====================================<BR>");

            out.println("<h2>dump sites</h2>");
            out.println(PsDataStoreDump.sites());
            out.println("<BR>====================================<BR>");

            out.println("<h2>dump clouds</h2>");
            out.println(PsDataStoreDump.clouds());
            out.println("<BR>====================================<BR>");


            out.println("<h2>dump matrices</h2>");
            out.println(PsDataStoreDump.matrices());
            out.println("<BR>====================================<BR>");


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
