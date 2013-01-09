/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_query;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.data_store.PsHostQuery;
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
 * Servlet for querying hosts in data store, follows design document
 * specifications returns list of hosts in JSON format
 * 
 * hosts can be queried by id,hostname,ipv4 and ipv6
 * If no query parameter is given the servlet will return
 * JSONArray of all available host id's
 *
 * //TODO proper exit codes for error conditions
 *
 * @author tomw
 */
//@WebServlet(name = "PsQueryHostsServlet", urlPatterns = {"/hosts"})
public class PsQueryHostsServlet extends HttpServlet {

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
//            out.println("<title>Servlet PsQueryHostsServlet</title>");
//            out.println("</head>");
//            out.println("<body>");
            //out.println("<h1>Servlet PsQueryHostsServlet at " + request.getContextPath() + "</h1>");

            PsDataStore psDataStore = PsDataStore.getDataStore();

            JSONArray listOfHosts = new JSONArray();

            // if there arre no parameters - return list of host id's
            if (request.getParameter("id") == null
                    && request.getParameter("hostname") == null
                    && request.getParameter("ipv4") == null
                    && request.getParameter("ipv6") == null) {
                Iterator<PsHost> iter = psDataStore.hostIterator();
                while(iter.hasNext()){
                    PsHost currentHost = (PsHost)iter.next();
                    String hostId = currentHost.getId();
                    listOfHosts.add(hostId);
                }
            } else {

                // get hosts based on id
                String[] listOfIds = request.getParameterValues("id");
                if (listOfIds != null) {
                    for (int i = 0; i < listOfIds.length; i = i + 1) {
                        String id = listOfIds[i];
                        PsHost host = psDataStore.getHost(id);
                        if (host != null) {
                            JSONObject json = JsonConverter.psHost2Json(host);
                            listOfHosts.add(json);
                        }
                    }
                }

                //get hosts based on host name
                String[] listOfHostnames = request.getParameterValues("hostname");
                if (listOfHostnames != null) {
                    if (listOfHostnames.length > 0) {
                        for (int i = 0; i < listOfHostnames.length; i = i + 1) {
                            String hostname = listOfHostnames[i];
                            PsHost host = PsHostQuery.getHostByName(hostname);
                            JSONObject json = JsonConverter.psHost2Json(host);
                            listOfHosts.add(json);
                        }
                    }
                }

                //get hosts based on host ipv4
                String[] listOfIpv4s = request.getParameterValues("ipv4");
                if (listOfIpv4s != null) {
                    if (listOfIpv4s.length > 0) {
                        for (int i = 0; i < listOfIpv4s.length; i = i + 1) {
                            String ipv4 = listOfIpv4s[i];
                            PsHost host = PsHostQuery.getHostByIpv4(ipv4);
                            JSONObject json = JsonConverter.psHost2Json(host);
                            listOfHosts.add(json);
                        }
                    }
                }

                //get hosts based on host ipv6
                String[] listOfIpv6s = request.getParameterValues("ipv6");
                if (listOfIpv6s != null) {
                    if (listOfIpv6s.length > 0) {
                        for (int i = 0; i < listOfIpv6s.length; i = i + 1) {
                            String ipv6 = listOfIpv6s[i];
                            PsHost host = PsHostQuery.getHostByIpv6(ipv6);
                            JSONObject json = JsonConverter.psHost2Json(host);
                            listOfHosts.add(json);
                        }
                    }
                }
            }
            out.println(listOfHosts.toString());




//            out.println("</body>");
//            out.println("</html>");
        
        }catch(Exception e){
            System.out.println(new Date()+" "+getClass().getName()+" "+e);
        }finally {
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
