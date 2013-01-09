/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_query;

import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.data_store.PsServiceQuery;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
//@WebServlet(name = "PsQueryServicesServlet", urlPatterns = {"/services"})
public class PsQueryServicesServlet extends HttpServlet {

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
//            out.println("<title>Servlet PsQueryServicesServlet</title>");
//            out.println("</head>");
//            out.println("<body>");
            //out.println("<h1>Servlet PsQueryServicesServlet at " + request.getContextPath() + "</h1>");

            // get services with given id
            PsDataStore psDataStore = PsDataStore.getDataStore();

            JSONArray listOfServices = new JSONArray();

            // if neither id nor name parameter is given - return list
            // of service id's

            if (request.getParameter("id") == null && request.getParameter("name") == null) {
                Iterator<PsService> iter = psDataStore.serviceIterator();
                while(iter.hasNext()){
                    PsService currentService = (PsService)iter.next();
                    listOfServices.add(currentService.getId());
                }
            } else {

                // get services based on id
                String[] listOfIds = request.getParameterValues("id");
                if (listOfIds != null) {
                    for (int i = 0; i < listOfIds.length; i = i + 1) {
                        String id = listOfIds[i];
                        PsService service = psDataStore.getService(id);
                        if (service != null) {
                            JSONObject json = JsonConverter.psService2Json(service);
                            listOfServices.add(json);
                        }
                    }
                }

                //get services with a given name            
                String[] listOfNames = request.getParameterValues("name");
                if (listOfNames != null) {
                    if (listOfNames.length > 0) {
                        for (int i = 0; i < listOfNames.length; i = i + 1) {
                            String name = listOfNames[i];
                            PsService service = PsServiceQuery.getserviceByName(name);
                            if (service != null) {
                                JSONObject json = JsonConverter.psService2Json(service);
                                listOfServices.add(json);
                            }
                        }
                    }
                }
            }

            out.println(listOfServices.toString());

//            out.println("</body>");
//            out.println("</html>");
        }catch(Exception e){
            System.out.println(new Date()+" "+getClass().getName()+" "+e);
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
