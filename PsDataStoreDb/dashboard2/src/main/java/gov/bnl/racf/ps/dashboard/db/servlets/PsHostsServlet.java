/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.servlets;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsHostManipulator;
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
        try {

//            out.println("<h1>GET GET GETServlet PsHostsServlet at " + request.getContextPath() + "</h1>");

            //boilerplate code to open session
            SessionFactory sessionFactory =
                    PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            if (parameters.size() > 0) {
                String idAsString = parameters.get(0);
                Integer hostIdInteger = Integer.parseInt(idAsString);
                int hostId = hostIdInteger.intValue();
                PsHost host = PsDataStore.getHost(session, hostId);
                JSONObject hostJson = JsonConverter.toJson(host);
                out.println(hostJson.toString());
            } else {

                List<PsHost> listOfHosts = PsDataStore.getAllHosts(session);
                JSONArray jsonArray = new JSONArray();
                for (PsHost host : listOfHosts) {
                    JSONObject hostJson = JsonConverter.toJson(host);
                    jsonArray.add(hostJson);
                }
                out.println(jsonArray.toString());
            }

            // commit transaction and close session
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            System.out.println(new Date() + " " + getClass().getName() + " " + e);
        } finally {

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

        try {
            //boilerplate code to open session
            SessionFactory sessionFactory =
                    PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            // parse data part of the code
            JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);

            if (jsonObject != null) {
                // the input data is a valid JSON object

                // create new host
                PsHost host = new PsHost();
                session.save(host);

                // fill the host with JSON parameters
                PsObjectUpdater.update(host, jsonObject);

                // convert host to json
                JSONObject finalHost = JsonConverter.toJson(host);

                // commit transaction and close session
                session.getTransaction().commit();
                session.close();

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
            // first order of business is to open session
            //boilerplate code to open session
            SessionFactory sessionFactory =
                    PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

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

                    JSONObject hostJson = JsonConverter.toJson(host);
                    out.println(hostJson.toString());
                }
            }else{
                if (parameters.size() == 2) {
                    int hostId = Integer.parseInt(parameters.get(0));
                    String userCommand = parameters.get(1);
                    out.println(hostId+" "+userCommand);
                    PsHost host = (PsHost)session.get(PsHost.class,hostId);
                    if(host==null){
                        out.println("host "+hostId+" not found");
                    }else{
                        out.println(JsonConverter.toJson(host).toString());
                        
                        // this is a valid host

                        if (PsApi.HOST_ADD_SERVICE_TYPE_COMMAND.equals(userCommand)) {
                            // user wanst to add services

                            // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);

                            // add those services
                            PsHostManipulator.addServiceTypes(session,host, jsonArray);
                        }
                        
                        if (PsApi.HOST_REMOVE_SERVICE_TYPE_COMMAND.equals(userCommand)) {
                            // user wanst to remove services

                            // unpack data content
                            JSONArray jsonArray =
                                    PostRequestDataExtractor.extractJsonArray(request);

                            // remove those services
                            PsHostManipulator.removeServiceTypes(session,host, jsonArray);
                        }
                        
                    }
                }
            }

            // commit transaction and close session
            session.getTransaction().commit();
            session.close();
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
            // first order of business is to open session
            //boilerplate code to open session
            SessionFactory sessionFactory =
                    PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            // second order of business is to unpack parameters from url
            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            //if there are parameters
            if (parameters.size() > 0) {
                String idAsString = parameters.get(0);
                Integer hostIdInteger = Integer.parseInt(idAsString);
                int hostId = hostIdInteger.intValue();
                PsHost host = PsDataStore.getHost(session, hostId);

                PsObjectShredder.delete(session,host);
            }

            // commit transaction and close session
            session.getTransaction().commit();
            session.close();
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
