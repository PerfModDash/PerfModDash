/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_query;

import gov.bnl.racf.ps.dashboard.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.data_store.PsSiteQuery;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * query sites. Return sites with given id or name or which contain a host with
 * a given name. If no parameter is given - return JSONArray with list of site
 * id's
 *
 * @author tomw
 */
//@WebServlet(name = "PsQuerySitesServlet", urlPatterns = {"/sites"})
public class PsQuerySitesServlet extends HttpServlet {

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
//            out.println("<title>Servlet PsQuerySitesServlet</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet PsQuerySitesServlet at " + request.getContextPath() + "</h1>");

            PsDataStore psDataStore = PsDataStore.getDataStore();

            JSONArray listOfSites = new JSONArray();

            if (request.getParameter("id") == null
                    && request.getParameter("name") == null
                    && request.getParameter("hosts") == null) {
                Iterator<PsSite> iter = psDataStore.siteIterator();
                while(iter.hasNext()){
                    PsSite currentSite = (PsSite)iter.next();
                    String siteId = currentSite.getId();
                    listOfSites.add(siteId);
                }
            } else {

                // get sites based on id
                String[] listOfIds = request.getParameterValues("id");
                if (listOfIds != null) {
                    if (listOfIds.length > 0) {
                        for (int i = 0; i < listOfIds.length; i = i + 1) {
                            String id = listOfIds[i];
                            PsSite site = psDataStore.getSite(id);
                            if (site != null) {
                                JSONObject json = JsonConverter.psSite2Json(site);
                                listOfSites.add(json);
                            }
                        }
                    }
                }

                //get sites based on name
                String[] listOfNames = request.getParameterValues("name");
                if (listOfNames != null) {
                    if (listOfNames.length > 0) {
                        for (int i = 0; i < listOfNames.length; i = i + 1) {
                            String name = listOfNames[i];
                            PsSite currentSite = PsSiteQuery.getSiteByName(name);
                            if (currentSite != null) {
                                JSONObject json =
                                        JsonConverter.psSite2Json(currentSite);
                                listOfSites.add(json);
                            }
                        }
                    }
                }

                // get sites based by hosts
                String[] listOfHosts = request.getParameterValues("hosts");
                if (listOfHosts != null) {
                    if (listOfHosts.length > 0) {
                        for (int i = 0; i < listOfHosts.length; i = i + 1) {
                            String hostName = listOfHosts[i];
                            Vector<PsSite> sitesByHostName =
                                    PsSiteQuery.sitesWhichContainHostName(hostName);
                            Iterator iter = sitesByHostName.iterator();
                            while (iter.hasNext()) {
                                PsSite currentSite = (PsSite) iter.next();
                                if (currentSite != null) {
                                    JSONObject json =
                                            JsonConverter.psSite2Json(currentSite);
                                    listOfSites.add(json);
                                }
                            }

                        }
                    }
                }
            }

            out.println(listOfSites.toString());
//            out.println("</body>");
//            out.println("</html>");
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
