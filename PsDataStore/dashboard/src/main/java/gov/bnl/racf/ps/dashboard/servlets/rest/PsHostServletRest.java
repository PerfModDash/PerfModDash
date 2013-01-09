/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets.rest;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.*;
import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Handle re
 * @author tomw
 */
public class PsHostServletRest extends HttpServlet {

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
            out.println("<title>Servlet PsHostServletRest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsHostServletRest at " + request.getContextPath() + "</h1>");
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
                String hostIdString = parameters.firstElement();
                PsHost host = dataStore.getHost(hostIdString);
                out.println(JsonConverter.toJson(host).toString());
            } else {
                JSONArray listOfHostIds = new JSONArray();
                Iterator<PsHost> iter = dataStore.hostIterator();
                while (iter.hasNext()) {
                    PsHost host = (PsHost) iter.next();
                    listOfHostIds.add(host.getId());
                }
                out.println(listOfHostIds.toString());
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

                // create new host
                PsHost host = PsObjectCreator.createNewHost();

                // fill the host with JSON parameters
                PsObjectUpdater.update(host, jsonObject);

                // convert host to json
                JSONObject finalHost = JsonConverter.toJson(host);

                out.println(finalHost.toString());
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
                // of the host to be updated

                JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);
                if (jsonObject != null) {
                    // the input data is a valid JSON object

                    // does the JSON object has valid id?
                    boolean jsonHasValidId = false;
                    if (jsonObject.keySet().contains(PsHost.ID)) {
                        if (jsonObject.get(PsHost.ID) != null && !"".equals(jsonObject.get(PsHost.ID))) {
                            jsonHasValidId = true;
                        }
                    }
                    if (jsonHasValidId) {
                        PsDataStore dataStore = PsDataStore.getDataStore();
                        String hostId = (String) jsonObject.get(PsHost.ID);
                        PsHost host = dataStore.getHost(hostId);

                        // fill the host with JSON parameters
                        PsObjectUpdater.update(host, jsonObject);

                        // convert host to json
                        JSONObject finalHost = JsonConverter.toJson(host);
                        // return updated host representation to client
                        out.println(finalHost.toString());
                    }
                }
            } else {
                if (parameters.size() == 2) {
                    // there are two parameters. The first one is host Id
                    // and the second one is command

                    PsDataStore dataStore = PsDataStore.getDataStore();

                    String hostId = parameters.firstElement();
                    String userCommand = parameters.get(1);

                    // does the command refer to a valid host?

                    PsHost host = dataStore.getHost(hostId);

                    if (host != null) {
                        // this is a valid host

                        if (PsApi.HOST_ADD_SERVICE_TYPE_COMMAND.equals(userCommand)) {
                            // user wanst to add services

                            // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);

                            // add those services
                            PsHostManipulator.addServiceTypes(host, jsonArray);
                        }
                        if (PsApi.HOST_REMOVE_SERVICE_ID_COMMAND.equals(userCommand)) {
                            // user wants to remove services

                            // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);

                            // remove those services
                            PsHostManipulator.removeServices(host, jsonArray);
                        }

                        if (PsApi.HOST_REMOVE_SERVICE_TYPE_COMMAND.equals(userCommand)) {
                            // user wants to remove services

                            // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);

                            // remove those services
                            PsHostManipulator.removeServiceTypes(host, jsonArray);
                        }

                        if (PsApi.HOST_ADD_ALL_SERVICES_COMMAND.equals(userCommand)) {
                            // add all primitive services
                            PsHostManipulator.addPrimitiveServices(host);
                        }

                        if (PsApi.HOST_REMOVE_ALL_SERVICES_COMMAND.equals(userCommand)) {
                            // remove all services from host
                            PsHostManipulator.removeAllServices(host);
                        }

                        if (PsApi.HOST_ADD_LATENCY_SERVICES_COMMAND.equals(userCommand)) {
                            // add latency services
                            PsHostManipulator.addLatencyServices(host);
                        }
                        if (PsApi.HOST_ADD_THROUGHPUT_SERVICES_COMMAND.equals(userCommand)) {
                            // add latency services
                            PsHostManipulator.addThroughputServices(host);
                        }
                    }
                    // convert host to json
                    JSONObject finalHost = JsonConverter.toJson(host);
                    // return updated host representation to client
                    out.println(finalHost.toString());
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
                String hostIdString = parameters.firstElement();
                PsObjectShredder.deleteHost(hostIdString);
                out.println("host " + hostIdString + " deleted");
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
