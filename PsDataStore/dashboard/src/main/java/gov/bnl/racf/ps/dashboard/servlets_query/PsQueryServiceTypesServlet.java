/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_query;

import gov.bnl.racf.ps.dashboard.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.data_store.PsServiceTypeQuery;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
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
//@WebServlet(name = "PsQueryServiceTypesServlet", urlPatterns = {"/serviceTypes"})
public class PsQueryServiceTypesServlet extends HttpServlet {

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
            out.println("<title>Servlet PsQueryServiceTypesServlet</title>");
            out.println("</head>");


            PsDataStore psDataStore = PsDataStore.getDataStore();

            JSONArray listOfServiceTypes = new JSONArray();

            boolean parametersFound = false;
            for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
                String currentParameter = (String) e.nextElement();
                if ("id".equals(currentParameter)
                        || "jobType".equals(currentParameter)
                        || "name".equals(currentParameter)) {
                    parametersFound = true;
                }
            }

            if (parametersFound) {
                //search service types based on input parameters

                // get service types based on id
                String[] listOfIds = request.getParameterValues("id");
                if (listOfIds != null) {
                    for (int i = 0; i < listOfIds.length; i = i + 1) {
                        String id = listOfIds[i];
                        PsServiceType serviceType = psDataStore.getServiceType(id);
                        if (serviceType != null) {
                            JSONObject json = JsonConverter.psServiceType2Json(serviceType);
                            listOfServiceTypes.add(json);
                        }
                    }
                }

                // get service types based on jobType
                String listOfJobTypes[] = request.getParameterValues("jobType");
                if (listOfJobTypes != null) {
                    if (listOfJobTypes.length > 0) {
                        for (int i = 0; i < listOfJobTypes.length; i = i + 1) {
                            String currentJobType = listOfJobTypes[i];
                            PsServiceType currentServiceType =
                                    PsServiceTypeQuery.getServiceTypeByJobType(currentJobType);
                            JSONObject json = JsonConverter.toJson(currentServiceType);
                            listOfServiceTypes.add(json);
                        }
                    }
                }

                //get service types based on name
                String[] listOfNames = request.getParameterValues("name");
                if (listOfNames != null) {
                    if (listOfNames.length > 0) {
                        for (int i = 0; i < listOfNames.length; i = i + 1) {
                            String currentName = listOfNames[i];
                            PsServiceType currentServiceType =
                                    PsServiceTypeQuery.getServiceTypeByName(currentName);
                            
                            JSONObject json = JsonConverter.toJson(currentServiceType);
                            listOfServiceTypes.add(json);
                        }
                    }
                }
            }else{
                //get all service types
                Iterator iter = PsServiceTypeQuery.getServiceTypeIterator();
                while(iter.hasNext()){
                    PsServiceType currentType = (PsServiceType)iter.next();
                    JSONObject json = JsonConverter.toJson(currentType);
                    listOfServiceTypes.add(json);
                }
            }
            out.println(listOfServiceTypes.toString());



            out.println("<body>");
            //out.println("<h1>Servlet PsQueryServiceTypesServlet at " + request.getContextPath() + "</h1>");
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
