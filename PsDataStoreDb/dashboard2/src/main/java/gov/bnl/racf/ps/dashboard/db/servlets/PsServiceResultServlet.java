/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.servlets;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceResult;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.*;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import gov.bnl.racf.ps.dashboard.db.utils.UrlUnpacker;
import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tomw
 */
public class PsServiceResultServlet extends HttpServlet {

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
            out.println("<title>Servlet PsServiceResultServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsServiceResultServlet at " + request.getContextPath() + "</h1>");
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

        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();

        try {

            session.beginTransaction();

            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            /*
             * TODO output your page here. You may use following sample code.
             */
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet PsServiceResultServlet</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet PsServiceResultServlet GET at " + request.getContextPath() + "</h1>");

            Date tmin = null;
            Date tmax = null;

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


            JSONObject resultsObject = new JSONObject();
            
            int numberOfRecords = PsDataStore.getResultsCount(session, tmin, tmax);
            resultsObject.put("numberOfRecords", numberOfRecords);
            
            Date minTime = PsDataStore.getResultsTimeMin(session);
            resultsObject.put("minTime",IsoDateConverter.date2IsoDate(minTime));
            
            out.println(resultsObject.toString());


//            out.println("</body>");
//            out.println("</html>");
        } catch (Exception e) {
            session.getTransaction().rollback();
            Logger.getLogger(PsServiceResultServlet.class).error("error occured: " + e);
            System.out.println(new Date() + " " + getClass().getName() + " error occured " + e);
            e.printStackTrace(out);


        } finally {
            out.close();
            session.close();
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
        PrintWriter out = response.getWriter();

        // first order of business is to open session
        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {

            org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("requestLogger");

            logger.debug("we start processing results request");

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PsUploadResultsServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsUploadResultsServlet at " + request.getContextPath() + "</h1>");

            // results are posted in data part of the request, therefore we must
            // open the data reader

            String inputDataString = "";

            BufferedReader br = null;
            try {
                String sCurrentLine;
                br = request.getReader();

                while ((sCurrentLine = br.readLine()) != null) {
                    out.println(sCurrentLine + "<BR>");
                    inputDataString = inputDataString + sCurrentLine;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            logger.debug("input data in JSON is " + inputDataString);

            //convert input data string to JSON
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(inputDataString);
            } catch (ParseException ex) {
                out.println("Error occured while parsing json data<BR>");
                out.println(ex);
                java.util.logging.Logger.getLogger(PsServiceResultServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            // convert json to service result
            logger.debug("we converted input data string to JSON, now convert JSON to service result object");
            PsServiceResult serviceResult =
                    Json2ServiceResultConverter.convert(json);
            logger.debug("JSON object converted to service result");
            // update the corresponding service
            PsService service = PsServiceUpdater.update(session, serviceResult);

            // remove corresponding job
            //PsObjectShredder.deletePsJob(session, serviceResult.getJob_id());
            if (service != null) {
                PsObjectShredder.deletePsJob(session, service);
            }

            if (service != null) {

                out.println(JsonConverter.toJson(service));
            } else {
                out.println(new Date() + " Error in " + getClass().getName()
                        + " cannot associate result with service " + json);
            }


            // commit transaction 
            session.getTransaction().commit();

            out.println("</body>");
            out.println("</html>");
            logger.debug("we finished processing results request");
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
            org.apache.log4j.Logger.getLogger(PsServiceResultServlet.class).error(e);
            out.println("Error occured in " + getClass().getName() + " please check the logs <BR>" + e);
        } finally {
            session.close();
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();

        try {

            session.beginTransaction();

            ArrayList<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            

            Date tmin = null;
            Date tmax = null;

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


            int numberOfRecordsDeleted = PsServiceResultManipulator.deleteResults(session, tmin, tmax);
            JSONObject resultObject = new JSONObject();
            resultObject.put("numberOfRecordsDeleted",numberOfRecordsDeleted);
            out.println( resultObject );


            
        } catch (Exception e) {
            session.getTransaction().rollback();
            Logger.getLogger(PsServiceResultServlet.class).error("error occured: " + e);
            System.out.println(new Date() + " " + getClass().getName() + " error occured " + e);
            e.printStackTrace(out);


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
