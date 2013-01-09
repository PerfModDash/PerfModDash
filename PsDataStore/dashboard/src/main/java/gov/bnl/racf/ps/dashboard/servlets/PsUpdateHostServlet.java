/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;


import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectUpdater;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Update host. Post JSON object as parameter host. The object should contain host id. 
 * The code will find host id in data store, unpack the json object and then update the
 * host accordingly.
 * 
 * The update will only affect host name, ip and so on. Adding and removing services 
 * to the host has to be done diffrently //TODO add servlet for that
 * //TODO add authentication filter
 * //TODO add proper exit codes
 * //TODO cleanup
 * @author tomw
 */
@WebServlet(name = "PsUpdateHostServlet", urlPatterns = {"/updateHost"})
public class PsUpdateHostServlet extends HttpServlet {

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
            out.println("<title>Servlet PsUpdateHostServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsUpdateHostServlet at " + request.getContextPath() + "</h1>");
            
            String hostString = request.getParameter("host");
            JSONObject json = null;
            JSONParser parser = new JSONParser();
                        
            
            
            if (hostString != null && "".equals(hostString)) {
                try {
                    //unpack host string and convert it to json
                    json = (JSONObject) parser.parse(hostString);
                    
                    PsDataStore dataStore = PsDataStore.getDataStore();
                    
                    String hostId = (String)json.get("id");
                    
                    PsHost host = dataStore.getHost(hostId);
                    
                    PsObjectUpdater.update(host, json);
                    
                    JSONObject newJson = JsonConverter.psHost2Json(host);
                    
                    out.println(newJson.toString());
                                        
                }catch(Exception e){
                    // parsing host failed, delete the host
                    
                    //TODO raise error code etc
                }
            }
            
            
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
