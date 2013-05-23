/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.servlets;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsHostManipulator;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsObjectCreator;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsObjectShredder;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsObjectUpdater;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import gov.bnl.racf.ps.dashboard.db.utils.UrlUnpacker;
import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
//@WebServlet(name = "PsHostsServlet", urlPatterns = {"/hosts"})
public class PsHostsServlet extends HttpServlet {

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
//            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());
//
//            if (parameters.size() > 0) {
//                String idAsString = parameters.get(0);
//                Integer hostIdInteger = Integer.parseInt(idAsString);
//                int hostId = hostIdInteger.intValue();
//                PsHost host = PsDataStore.getHost(hostId);
//                JSONObject hostJson = JsonConverter.toJson(host);
//                out.println(hostJson.toString());
//            } else {
//                out.println("No parameters");
//            }
//
//            out.println("</body>");
//            out.println("</html>");
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();
        try {

            session.beginTransaction();



            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            if (parameters.size() > 0) {
                // get info about a concreta host

                //get url parameters
                String detailLevel = request.getParameter(PsApi.DETAIL_LEVEL_PARAMETER);
                if (detailLevel == null || "".equals(detailLevel)) {
                    detailLevel = PsApi.DETAIL_LEVEL_HIGH;
                }

                String idAsString = parameters.get(0);
                Integer hostIdInteger = Integer.parseInt(idAsString);
                int hostId = hostIdInteger.intValue();
                PsHost host = PsDataStore.getHost(session, hostId);
                JSONObject hostJson = JsonConverter.toJson(host, detailLevel);
                out.println(hostJson.toString());
            } else {
                // get list of hosts

                //get url parameters
                String detailLevel = request.getParameter(PsApi.DETAIL_LEVEL_PARAMETER);
                if (detailLevel == null || "".equals(detailLevel)) {
                    detailLevel = PsApi.DETAIL_LEVEL_LOW;
                }

                List<PsHost> listOfHosts = PsDataStore.getAllHosts(session);
                JSONArray jsonArray = new JSONArray();
                for (PsHost host : listOfHosts) {
                    JSONObject hostJson = JsonConverter.toJson(host, detailLevel);
                    jsonArray.add(hostJson);
                }
                out.println(jsonArray.toString());
            }

            // commit transaction and close session
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " " + getClass().getName() + " " + e);
            Logger.getLogger(PsHostsServlet.class).error(e);
            e.printStackTrace(out);
            out.println("Error occured in " + getClass().getName() + " plase check the logs<BR>" + e);
        } finally {
            session.close();
            out.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * POST requests are used to create new hosts
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

        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            //boilerplate code to open session


            // parse data part of the codeJSONObject 
            JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);

            if (jsonObject != null) {
                // the input data is a valid JSON object

                // create new host
                PsHost host = PsObjectCreator.createNewHost(session);

                // fill the host with JSON parameters
                PsObjectUpdater.update(host, jsonObject);

                // convert host to json

                JSONObject finalHost = JsonConverter.toJson(host, PsApi.DETAIL_LEVEL_HIGH);

                out.println(finalHost.toString());

            }
            // commit transaction and close session
            session.getTransaction().commit();


        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
            e.printStackTrace();
        } finally {
            session.close();
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

        // first order of business is to open session
        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {


            // second order of business is to unpack parameters from url
            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            //if there are parameters
            if (parameters.size() == 1) {
                String idAsString = parameters.get(0);
                Integer hostIdInteger = Integer.parseInt(idAsString);
                int hostId = hostIdInteger.intValue();
                PsHost host = PsDataStore.getHost(session, hostId);

                //unpack json object from data part
                // parse data part of the code
                JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);
                if (jsonObject != null) {
                    //update the host
                    PsObjectUpdater.update(host, jsonObject);

                    // save the updated host
                    session.save(host);

                    JSONObject hostJson = JsonConverter.toJson(host, PsApi.DETAIL_LEVEL_HIGH);
                    out.println(hostJson.toString());
                }
            } else {
                if (parameters.size() == 2) {
                    int hostId = Integer.parseInt(parameters.get(0));
                    String userCommand = parameters.get(1);
                    out.println(hostId + " " + userCommand);
                    PsHost host = (PsHost) session.get(PsHost.class, hostId);
                    if (host == null) {
                        out.println("host " + hostId + " not found");
                    } else {
                        // this is a valid host

                        if (PsApi.HOST_ADD_SERVICE_TYPE_COMMAND.equals(userCommand)) {
                            // user wants to add services

                            // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);

                            // add those services
                            PsHostManipulator.addServiceTypes(session, host, jsonArray);
                        }

                        if (PsApi.HOST_ADD_ALL_SERVICES_COMMAND.equals(userCommand)) {
                            // user wants to add services

                            // add those services
                            PsHostManipulator.addPrimitiveServices(session, host);
                        }
                        if (PsApi.HOST_ADD_LATENCY_SERVICES_COMMAND.equals(userCommand)) {
                            // user wants to add services

                            // add those services
                            PsHostManipulator.addLatencyServices(session, host);
                        }
                        if (PsApi.HOST_ADD_THROUGHPUT_SERVICES_COMMAND.equals(userCommand)) {
                            // user wants to add services

                            // add those services
                            PsHostManipulator.addThroughputServices(session, host);
                        }

                        if (PsApi.HOST_REMOVE_SERVICE_TYPE_COMMAND.equals(userCommand)) {
                            // user wanst to remove services

                            // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);

                            // remove those services
                            PsHostManipulator.removeServiceTypes(session, host, jsonArray);
                        }

                        if (PsApi.HOST_REMOVE_SERVICE_ID_COMMAND.equals(userCommand)) {
                            // user wanst to remove services

                            // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);

                            // remove those services
                            PsHostManipulator.removeServices(session, host, jsonArray);
                        }
                        if (PsApi.HOST_REMOVE_ALL_SERVICES_COMMAND.equals(userCommand)) {
                            // user wants to remove services

                            // remove all services from host
                            PsHostManipulator.removeAllServices(session, host);
                        }


                        out.println(JsonConverter.toJson(host, PsApi.DETAIL_LEVEL_HIGH).toString());
                    }
                }
            }

            // commit transaction and close session
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
            session.close();
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

        // first order of business is to open session
        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();


        try {


            // second order of business is to unpack parameters from url
            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            //if there are parameters
            if (parameters.size() > 0) {
                String idAsString = parameters.get(0);
                Integer hostIdInteger = Integer.parseInt(idAsString);
                int hostId = hostIdInteger.intValue();
                PsHost host = PsDataStore.getHost(session, hostId);

                PsObjectShredder.delete(session, host);
            }

            // commit transaction and close session
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
            session.close();
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
