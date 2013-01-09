/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_query;

import gov.bnl.racf.ps.dashboard.data_objects.PsCollector;
import gov.bnl.racf.ps.dashboard.data_store.PsCollectorQuery;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * servlet to execute queries of collector objects
 * returns JSONObject of a collector
 * the collector can be specified by id,name,ipv4,ipv6 or hostname
 * 
 *
 * @author tomw
 */
//@WebServlet(name = "PsQueryCollectorServlet", urlPatterns = {"/collectors"})
public class PsQueryCollectorServlet extends HttpServlet {

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

            String collectorId = request.getParameter("id");
            if (collectorId != null && !"".equals(collectorId)) {
                PsCollector collector = dataStore.getCollector(collectorId);
                JSONObject collectorJson = JsonConverter.toJson(collector);
                out.println(collectorJson.toString());
            }

            String collectorName = request.getParameter("name");
            if (collectorName != null && !"".equals(collectorName)) {
                PsCollector collector = PsCollectorQuery.getCollectorByName(collectorName);
                JSONObject collectorJson = JsonConverter.toJson(collector);
                out.println(collectorJson.toString());
            }

            String collectorIpv4 = request.getParameter("ipv4");
            if (collectorIpv4 != null && !"".equals(collectorIpv4)) {
                PsCollector collector = PsCollectorQuery.getCollectorByIpv4(collectorIpv4);
                JSONObject collectorJson = JsonConverter.toJson(collector);
                out.println(collectorJson.toString());
            }

            String collectorIpv6 = request.getParameter("ipv6");
            if (collectorIpv6 != null && !"".equals(collectorIpv6)) {
                PsCollector collector = PsCollectorQuery.getCollectorByIpv6(collectorIpv6);
                JSONObject collectorJson = JsonConverter.toJson(collector);
                out.println(collectorJson.toString());
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
