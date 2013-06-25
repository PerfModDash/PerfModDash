/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.servlets;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsCloud;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.*;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import gov.bnl.racf.ps.dashboard.db.utils.UrlUnpacker;
import gov.bnl.racf.ps.exceptionlogmanager.ExceptionLog;
import gov.bnl.racf.ps.exceptionlogmanager.ExceptionLogImpl;
import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
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
public class PsCloudsServlet extends HttpServlet {

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
            out.println("<title>Servlet PsCloudsServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsCloudsServlet at " + request.getContextPath() + "</h1>");
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
        //throw new UnsupportedOperationException("Method GET not yet implemented");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {



            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            if (parameters.size() > 0) {
                // get info about a particular cloud
                //get url parameters
                String detailLevel = request.getParameter(PsApi.DETAIL_LEVEL_PARAMETER);
                if (detailLevel == null || "".equals(detailLevel)) {
                    detailLevel = PsApi.DETAIL_LEVEL_HIGH;
                }

                String idAsString = parameters.get(0);
                Integer cloudIdInteger = Integer.parseInt(idAsString);
                int cloudId = cloudIdInteger.intValue();
                PsCloud cloud = PsDataStore.getCloud(session, cloudId);
                JSONObject cloudJson = JsonConverter.toJson(cloud, detailLevel);
                out.println(cloudJson.toString());
            } else {
                // get list of clouds
                //get url parameters
                String detailLevel = request.getParameter(PsApi.DETAIL_LEVEL_PARAMETER);
                if (detailLevel == null || "".equals(detailLevel)) {
                    detailLevel = PsApi.DETAIL_LEVEL_LOW;
                }

                List<PsCloud> listOfClouds = PsDataStore.getAllClouds(session);
                JSONArray jsonArray = new JSONArray();
                for (PsCloud cloud : listOfClouds) {
                    JSONObject cloudJson = JsonConverter.toJson(cloud, detailLevel);
                    jsonArray.add(cloudJson);
                }
                out.println(jsonArray.toString());
            }

            // commit transaction and close session
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " " + getClass().getName() + " " + e);
            Logger.getLogger(PsCloudsServlet.class).error(e);
            out.println("Error occured in " + getClass().getName() + " plase check the logs<BR>" + e);

            ExceptionLog eLog = new ExceptionLogImpl(getServletContext().getRealPath("/"));
            eLog.log(getClass().getName(), e);

        } finally {
            session.close();
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
        //throw new UnsupportedOperationException("Method POST not yet implemented");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            // parse data part of the code
            JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);

            if (jsonObject != null) {
                // the input data is a valid JSON object

                // create new cloud
                PsCloud cloud = PsObjectCreator.createNewCloud(session, jsonObject);

                // convert host to json
                JSONObject finalCloud = JsonConverter.toJson(cloud, PsApi.DETAIL_LEVEL_HIGH);

                out.println(finalCloud.toString());
            }
            // commit transaction and close session
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
            Logger.getLogger(PsCloudsServlet.class).error(e);
            out.println("Error occured in " + getClass().getName() + " plase check the logs<BR>" + e);

            ExceptionLog eLog = new ExceptionLogImpl(getServletContext().getRealPath("/"));
            eLog.log(getClass().getName(), e);

        } finally {
            session.close();
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //throw new UnsupportedOperationException("Method PUT not yet implemented");
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
                Integer cloudIdInteger = Integer.parseInt(idAsString);
                int cloudId = cloudIdInteger.intValue();
                PsCloud cloud = PsDataStore.getCloud(session, cloudId);

                //unpack json object from data part
                // parse data part of the code
                JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);
                if (jsonObject != null) {
                    //update the host
                    PsObjectUpdater.update(cloud, jsonObject);

                    // save the updated host, this is probably redundant
                    session.save(cloud);

                    JSONObject cloudJson = JsonConverter.toJson(cloud, PsApi.DETAIL_LEVEL_HIGH);
                    out.println(cloudJson.toString());
                } else {
                    out.println("JSON object is not valid");
                    Logger.getLogger(PsCloudsServlet.class).error("JSON object is not valid");
                }
            } else {
                if (parameters.size() == 2) {
                    int cloudId = Integer.parseInt(parameters.get(0));
                    String userCommand = parameters.get(1);
                    PsCloud cloud = (PsCloud) session.get(PsCloud.class, cloudId);
                    if (cloud == null) {
                        out.println("cloud " + cloudId + " not found");
                        Logger.getLogger(PsSitesServlet.class).warn("Cloud with id=" + cloudId + " not found");
                    } else {
                        // this is a valid cloud

                        // unpack data content
                        JSONArray jsonArray =
                                PostRequestDataExtractor.extractJsonArray(request);

                        boolean thisIsValidCommand = false;

                        if (PsApi.CLOUD_ADD_SITE_IDS.equals(userCommand)) {
                            thisIsValidCommand = true;
                            // user wants to add sites to cloud

                            // add those sites
                            PsCloudManipulator.addSites(session, cloud, jsonArray);
                        }

                        if (PsApi.CLOUD_REMOVE_SITE_IDS.equals(userCommand)) {
                            thisIsValidCommand = true;
                            // user wants to remove hosts from site

                            // remove those hosts
                            PsCloudManipulator.removeSites(session, cloud, jsonArray);
                        }
                        if (PsApi.CLOUD_ADD_MATRIX_IDS.equals(userCommand)) {
                            thisIsValidCommand = true;
                            // user wants to add sites to cloud

                            // add those sites
                            PsCloudManipulator.addMatrices(session, cloud, jsonArray);
                        }

                        if (PsApi.CLOUD_REMOVE_MATRIX_IDS.equals(userCommand)) {
                            thisIsValidCommand = true;
                            // user wants to remove hosts from site

                            // remove those hosts
                            PsCloudManipulator.removeMatrices(session, cloud, jsonArray);
                        }

                        if (!thisIsValidCommand) {
                            String errorMessage = getClass().getName() + " Unknown user command: " + userCommand;
                            throw new UnsupportedOperationException(errorMessage);
                        }
                        //save the changes to the site (actually this command should be redundant)
                        session.save(cloud);
                        out.println(JsonConverter.toJson(cloud, PsApi.DETAIL_LEVEL_HIGH).toString());
                    }
                }
            }

            // commit transaction and close session
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
            Logger.getLogger(PsSitesServlet.class).error(e);
            out.println("Error occured in " + getClass().getName() + " please check the logs <BR>" + e);

            ExceptionLog eLog = new ExceptionLogImpl(getServletContext().getRealPath("/"));
            eLog.log(getClass().getName(), e);

        } finally {
            session.close();
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
        //throw new UnsupportedOperationException("Method DELETE not yet implemented");
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
                Integer cloudIdInteger = Integer.parseInt(idAsString);
                int cloudId = cloudIdInteger.intValue();
                PsCloud cloud = PsDataStore.getCloud(session, cloudId);

                PsObjectShredder.delete(session, cloud);
            }

            // commit transaction and close session
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
            Logger.getLogger(PsCloudsServlet.class).error(e);
            out.println("Error occured in " + getClass().getName() + " please check the logs <BR>" + e);

            ExceptionLog eLog = new ExceptionLogImpl(getServletContext().getRealPath("/"));
            eLog.log(getClass().getName(), e);

        } finally {
            session.close();
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
