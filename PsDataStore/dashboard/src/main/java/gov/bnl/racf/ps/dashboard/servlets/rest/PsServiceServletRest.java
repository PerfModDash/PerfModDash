/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets.rest;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectCreator;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectShredder;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectUpdater;
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
//@WebServlet(name = "PsServiceServletRest", urlPatterns = {"/services_rest"})
public class PsServiceServletRest extends HttpServlet {

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
            out.println("<title>Servlet PsServiceServletRest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsServiceServletRest at " + request.getContextPath() + "</h1>");
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            PsDataStore dataStore = PsDataStore.getDataStore();

            if (parameters.size() > 0) {
                String serviceIdString = parameters.firstElement();
                PsService service = dataStore.getService(serviceIdString);
                out.println(JsonConverter.toJson(service).toString());
            } else {
                JSONArray listOfServiceIds = new JSONArray();
                Iterator<PsService> iter = dataStore.serviceIterator();
                while (iter.hasNext()) {
                    PsService service = (PsService) iter.next();
                    listOfServiceIds.add(service.getId());
                }
                out.println(listOfServiceIds.toString());
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

                // create new service
                PsService service = PsObjectCreator.createNewService();

                // fill the host with JSON parameters
                PsObjectUpdater.update(service, jsonObject);

                // convert service to json
                JSONObject finalService = JsonConverter.toJson(service);

                out.println(finalService.toString());
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
                // there is only one argument, namely host id
                // the data part of the request should contain JSON object
                // of the service to be updated

                JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);
                if (jsonObject != null) {
                    // the input data is a valid JSON object

                    // does the JSON object has valid id?
                    boolean jsonHasValidId = false;
                    if (jsonObject.keySet().contains(PsService.ID)) {
                        if (jsonObject.get(PsService.ID) != null && !"".equals(jsonObject.get(PsService.ID))) {
                            jsonHasValidId = true;
                        }
                    }
                    if (jsonHasValidId) {
                        PsDataStore dataStore = PsDataStore.getDataStore();
                        String serviceId = (String) jsonObject.get(PsService.ID);
                        PsService service = dataStore.getService(serviceId);

                        // fill the host with JSON parameters
                        PsObjectUpdater.update(service, jsonObject);

                        // convert service to json
                        JSONObject finalService = JsonConverter.toJson(service);
                        // return updated service representation to client
                        out.println(finalService.toString());
                    }
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
                String serviceIdString = parameters.firstElement();
                PsObjectShredder.deleteService(serviceIdString);
                out.println("service  " + serviceIdString + " deleted");
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
