/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.servlets;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceResult;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.IsoDateConverter;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsObjectShredder;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import gov.bnl.racf.ps.dashboard.db.utils.UrlUnpacker;
import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
public class PsServicesServlet extends HttpServlet {

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
            out.println("<title>Servlet PsServicesServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsServicesServlet at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        //response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();

        try {

            session.beginTransaction();

            //out.println(IsoDateConverter.date2IsoDate(new Date()));

            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            if (parameters.size() > 0) {
                // get info about a concrete service

                //get url parameters
                String detailLevel = request.getParameter(PsApi.DETAIL_LEVEL_PARAMETER);
                if (detailLevel == null || "".equals(detailLevel)) {
                    // default detail level
                    detailLevel = PsApi.DETAIL_LEVEL_HIGH;
                }

                String idAsString = parameters.get(0);
                Integer serviceIdInteger = Integer.parseInt(idAsString);
                int serviceId = serviceIdInteger.intValue();
                PsService service = PsDataStore.getService(session, serviceId);

                if (parameters.size() == 1) {
                    JSONObject serviceJson = JsonConverter.toJson(service, detailLevel);
                    out.println(serviceJson.toString());
                }
                if (parameters.size() == 2) {
                    String command = parameters.get(1);
                    if (PsApi.SERVICE_HISTORY_COMMAND.equals(command)) {

                        if (request.getParameterMap().keySet().contains(PsApi.SERVICE_HISTORY_TAROUND)) {
                            
                            String tAroundString = request.getParameter(PsApi.SERVICE_HISTORY_TAROUND);
                            Date tAround=IsoDateConverter.isoDate2Date(tAroundString);
                            PsServiceResult result = PsDataStore.getResult(session,service,tAround);
                            
                            JSONObject resultJson = JsonConverter.toJson(result);
                            out.println(resultJson.toString());
                        }else{
                            
                            Date tmin = null;
                            Date tmax = null;

                            if (request.getParameterMap().keySet().contains(PsApi.SERVICE_HISTORY_HOURS_AGO)) {
                                tmax = new Date();
                                String hoursAgoString = request.getParameter(PsApi.SERVICE_HISTORY_HOURS_AGO);
                                long nHoursAgo = Long.parseLong(hoursAgoString);
                                long miliSecondsInHour = 3600 * 1000;
                                tmin = new Date(tmax.getTime() - nHoursAgo * miliSecondsInHour);
                            } else {

                                if (request.getParameterMap().keySet().contains(PsApi.SERVICE_HISTORY_TMIN)) {
                                    String tminString = request.getParameter(PsApi.SERVICE_HISTORY_TMIN);
                                    tmin = IsoDateConverter.isoDate2Date(tminString);
                                } else {
                                    long secondAfterBeginningOfWorld = 1;
                                    tmin = new Date(secondAfterBeginningOfWorld);
                                }
                                if (request.getParameterMap().keySet().contains(PsApi.SERVICE_HISTORY_TMAX)) {
                                    String tmaxString = request.getParameter(PsApi.SERVICE_HISTORY_TMAX);
                                    tmax = IsoDateConverter.isoDate2Date(tmaxString);
                                } else {
                                    tmax = new Date();
                                }
                            }

                            List<PsServiceResult> listOfResults =
                                    PsDataStore.getResults(session, service, tmin, tmax);

                            //out.println("nresults="+listOfResults.size());

                            JSONArray listOfResultsJson = new JSONArray();
                            JSONObject currentResultJson = null;
                            Iterator iter = listOfResults.iterator();
                            while (iter.hasNext()) {
                                PsServiceResult currentResult =
                                        (PsServiceResult) iter.next();
                                currentResultJson = JsonConverter.toJson(currentResult);
                                listOfResultsJson.add(currentResultJson);
                            }
                            out.println(listOfResultsJson.toString());
                        }

                    }
                }

            } else {
                // get list of services

                //get url parameters
                String detailLevel = request.getParameter(PsApi.DETAIL_LEVEL_PARAMETER);
                if (detailLevel == null || "".equals(detailLevel)) {
                    // default detail level
                    detailLevel = PsApi.DETAIL_LEVEL_LOW;
                }

                List<PsService> listOfServices = PsDataStore.getAllServices(session);
                JSONArray jsonArray = new JSONArray();
                for (PsService service : listOfServices) {
                    JSONObject serviceJson = JsonConverter.toJson(service, detailLevel);
                    jsonArray.add(serviceJson);
                }
                out.println(jsonArray.toString());
            }

            // commit transaction 
            session.getTransaction().commit();



        } catch (Exception e) {
            session.getTransaction().rollback();
            Logger.getLogger(PsServicesServlet.class).error("error occured: " + e);
            System.out.println(new Date() + " " + getClass().getName() + " error occured " + e);
            e.printStackTrace(out);

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
        Logger.getLogger(PsServicesServlet.class).error("POST method is not implemented yet");
        throw new ServletException("POST method noit implemented yet");
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
                Integer serviceIdInteger = Integer.parseInt(idAsString);
                int serviceId = serviceIdInteger.intValue();
                PsService service = PsDataStore.getService(session, serviceId);

                PsObjectShredder.delete(session, service);
            }

            // commit transaction and close session
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
            Logger.getLogger(PsServicesServlet.class).error(e);
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
