/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;


import gov.bnl.racf.ps.dashboard.data_objects.PsCollector;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectCreator;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectShredder;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectUpdater;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * create new collector
 * pick details from JSON object, create new object and update it
 * return json object of the newly created collector
 * //TODO return codes in case of errors
 * //TODO cleanup
 * @author tomw
 */
//@WebServlet(name = "PsCreateCollectorServlet", urlPatterns = {"/createCollector"})
public class PsCreateCollectorServlet extends HttpServlet {

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
            
            PsCollector newCollector = PsObjectCreator.createNewCollector();
   
            String collectorName=request.getParameter("name");
            if(collectorName!=null && !"".equals(collectorName)){
                newCollector.setName(collectorName);
            }            
            
            String hostName=request.getParameter("hostName");
            if(hostName!=null && !"".equals(hostName)){
                newCollector.setHostname(hostName);
            }            
            
            String ipv4=request.getParameter("ipv4");
            if(ipv4!=null && !"".equals(ipv4)){
                newCollector.setIpv4(ipv4);
            }
           
            String ipv6=request.getParameter("ipv6");
            if(ipv6!=null && !"".equals(ipv6)){
                newCollector.setIpv6(ipv6);
            }
                       
            out.println(JsonConverter.toJson(newCollector).toString());
                                       
        }catch(Exception e){
            System.out.println(new Date()+" "+getClass().getName()+" "+e);                                   
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
