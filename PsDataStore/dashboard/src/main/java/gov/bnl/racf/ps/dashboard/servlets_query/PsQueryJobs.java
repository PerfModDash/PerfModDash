/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_query;

import gov.bnl.racf.ps.dashboard.data_objects.PsJob;
import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsService2JobConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsServiceTypeFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
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
 * Query the datastore and return services which are scheduled to run (that is
 * have nextCheckTime<NOW() ).
 *
 * input: id: if present return job corresponding to serviceId=id
 *
 * type =return only jobs of this particular data type
 *
 * running: if set running=1 return running jobs, if set to 0 return not running
 * jobs
 *
 * setRunning: if 1 then all affected services have running flag set to true
 *
 *
 *
 *

 *
 * @author tomw
 */
//@WebServlet(name = "PsQueryJobs", urlPatterns = {"/jobs"})
public class PsQueryJobs extends HttpServlet {

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
//            out.println("<title>Servlet PsQueryJobs</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet PsQueryJobs at " + request.getContextPath() + "</h1>");

            PsDataStore dataStore = PsDataStore.getDataStore();

            // unpack parameters, one by one

            boolean setRunning = false;
            boolean running = true;
            String type = null;

            Vector<String> listOfServiceIds = new Vector<String>();

            Vector<PsService> listOfServices = new Vector<PsService>();

            for (Enumeration<String> e = request.getParameterNames();
                    e.hasMoreElements();) {
                String currentParameter = (String) e.nextElement();

                if ("type".equals(currentParameter)) {
                    String runningString = request.getParameter("running");
                    if ("1".equals(runningString)) {
                        running = true;
                    }
                    if ("0".equals(runningString)) {
                        running = false;
                    }
                }


                if ("running".equals(currentParameter)) {
                    String runningString = request.getParameter("running");
                    if ("1".equals(runningString)) {
                        running = true;
                    }
                    if ("0".equals(runningString)) {
                        running = false;
                    }
                }

                if ("setRunning".equals(currentParameter)) {
                    String runningString = request.getParameter("setRunning");
                    if ("1".equals(runningString)) {
                        setRunning = true;
                    }
                    if ("0".equals(runningString)) {
                        setRunning = false;
                    }
                }


                //TODO generalize it to multiple id's
                if ("id".equals(currentParameter)) {
                    String serviceId = request.getParameter("id");
                    listOfServiceIds.add(serviceId);
                }
            }

            // if user requested particular id, return the service of this id
            // else return services which are due
            if (listOfServiceIds.isEmpty()) {
                Iterator<PsService> iter = dataStore.serviceIterator();
                while (iter.hasNext()) {
                    PsService service = (PsService) iter.next();

                    boolean selectThisService = true;

                    if (!service.isDue()) {
                        selectThisService = false;
                    }

                    if (type != null) {
                        if (PsServiceTypeFactory.isKnownType(type)) {
                            if (service.getType().equals(type)) {
                                selectThisService = true;
                            } else {
                                selectThisService = false;
                            }
                        }
                    }

                    if (running) {
                        if (service.isRunning()) {
                            selectThisService = true;
                        }
                    } else {
                        if (!service.isRunning()) {
                            selectThisService = true;
                        }
                    }

                    if (selectThisService) {
                        listOfServices.add(service);
                    }
                }
            } else {
                Iterator<String> iter = listOfServiceIds.iterator();
                while (iter.hasNext()) {
                    String serviceId = (String) iter.next();
                    PsService service = dataStore.getService(serviceId);
                    listOfServices.add(service);
                }
            }

            // Ok, we have list of selected services. If setRunning=true
            // we have to set them to running

            // convert list of services to JSON oblects and load it to JSONarray
            JSONArray listOfJsonJobs = new JSONArray();

            Iterator<PsService> iter = listOfServices.iterator();
            while (iter.hasNext()) {
                PsService service = (PsService) iter.next();
                if (setRunning) {
                    service.setRunning(true);
                }
                PsJob job = PsService2JobConverter.buildJob(service);
                JSONObject json = JsonConverter.toJson(job);
                listOfJsonJobs.add(json);
            }
            
            
            JSONObject jsonObjectWrapper = new JSONObject();
            jsonObjectWrapper.put("jobs",listOfJsonJobs);
            out.println(jsonObjectWrapper.toString());




//            out.println("</body>");
//            out.println("</html>");
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
