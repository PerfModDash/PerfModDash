/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets.rest;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.*;
import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
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
 *
 * @author tomw
 */
//@WebServlet(name = "PsSiteServletRest", urlPatterns = {"/sites_rest"})
public class PsSiteServletRest extends HttpServlet {

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
            out.println("<title>Servlet PsSiteServletRest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsSiteServletRest at " + request.getContextPath() + "</h1>");




            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            PsDataStore dataStore = PsDataStore.getDataStore();

            if (parameters.size() > 0) {
                String siteIdString = parameters.firstElement();
                PsSite site = dataStore.getSite(siteIdString);
                out.println(JsonConverter.toJson(site).toString());
            } else {
                JSONArray listOfSiteIds = new JSONArray();
                Iterator<PsSite> iter = dataStore.siteIterator();
                while (iter.hasNext()) {
                    PsSite site = (PsSite) iter.next();
                    listOfSiteIds.add(site.getId());
                }
                out.println(listOfSiteIds.toString());
            }
        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
        }

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);

            if (jsonObject != null) {
                // the input data is a valid JSON object

                // create new site
                PsSite site = PsObjectCreator.createNewSite();

                // fill the host with JSON parameters
                PsObjectUpdater.update(site, jsonObject);

                // convert host to json
                JSONObject finalSite = JsonConverter.toJson(site);

                out.println(finalSite.toString());
            }
        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>PUT</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            if (parameters.size() == 1) {

                JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);
                if (jsonObject != null) {
                    // the input data is a valid JSON object

                    // does the JSON object has valid id?
                    boolean jsonHasValidId = false;
                    if (jsonObject.keySet().contains(PsSite.ID)) {
                        if (jsonObject.get(PsSite.ID) != null && !"".equals(jsonObject.get(PsSite.ID))) {
                            jsonHasValidId = true;
                        }
                    }
                    if (jsonHasValidId) {
                        PsDataStore dataStore = PsDataStore.getDataStore();
                        String siteId = (String) jsonObject.get(PsSite.ID);
                        PsSite site = dataStore.getSite(siteId);

                        // fill the host with JSON parameters
                        PsObjectUpdater.update(site, jsonObject);

                        // convert host to json
                        JSONObject finalSite = JsonConverter.toJson(site);
                        // return updated host representation to client
                        out.println(finalSite.toString());
                    }
                } else {
                    out.println("Failed to extract JSON object from request");
                }
            }else{
                if (parameters.size() == 2) {
                    // there are two parameters. The first one is site Id
                    // and the second one is command

                    PsDataStore dataStore = PsDataStore.getDataStore();

                    String siteId = parameters.firstElement();
                    String userCommand = parameters.get(1);
                    
                    // does the command refer to a valid host?

                    PsSite site = dataStore.getSite(siteId);

                    if (site != null) {
                        // the command refers to a valid site
                        
                        // extract JSONArray of parameters
                        // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);
                        if(PsApi.SITE_ADD_HOST_IDS.equals(userCommand)){
                            PsSiteManipulator.addHosts(site, jsonArray);
                        }
                        if(PsApi.SITE_REMOVE_HOST_IDS.equals(userCommand)){
                            PsSiteManipulator.removeHosts(site, jsonArray);
                        }
                    }
                    JSONObject finalSite = JsonConverter.toJson(site);
                    out.println(finalSite.toString());
                }
            }

        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>DELETE</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {


            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            if (parameters.size() > 0) {
                String siteIdString = parameters.firstElement();
                PsObjectShredder.deleteSite(siteIdString);
                out.println("site " + siteIdString + " deleted");
            }

        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
        }
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
