/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;


import gov.bnl.racf.ps.dashboard.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectCreator;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectShredder;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectUpdater;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * create new service servlet
 * service details are taken from service parameter
 * they should be coded as json object
 * new service is then created and assigned id. Its parameters are filled
 * from json object.
 * Finally a json object of the new service is returned.
 * 
 * //TODO add authentication filter
 * //TODO add correct exit codes for errors
 * 
 * @author tomw
 */
@WebServlet(name = "PsCreateServiceServlet", urlPatterns = {"/createService"})
public class PsCreateServiceServlet extends HttpServlet {

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
            out.println("<title>Servlet PsCreateServiceServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsCreateServiceServlet at " + request.getContextPath() + "</h1>");
            
            PsDataStore dataStore = PsDataStore.getDataStore();
            
            PsService newService = PsObjectCreator.createNewService();
            
            String serviceString = request.getParameter("service");
            
            if(serviceString!=null && !"".equals(serviceString)){            
                JSONParser parser = new JSONParser();
                JSONObject json=null;
                try {
                    //unpack service string and convert it to json
                    json = (JSONObject) parser.parse(serviceString);
                    PsObjectUpdater.update(newService,json);
                }catch(Exception e){
                    
                    //TODO raise proper error code
                    Date date = new Date();                   
                    String currentClass = getClass().getName();
                    System.out.println(date+" "+currentClass+" service creation failed");
                    System.out.println(date+" "+currentClass+" service="+serviceString);
                    System.out.println(date+" "+currentClass+" e="+e);
                    PsObjectShredder.deleteService(newService);
                }
                
            }
            out.println(JsonConverter.psService2Json(newService).toString());
            
            
            
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
