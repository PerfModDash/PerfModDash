/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;


import gov.bnl.racf.ps.dashboard.data_objects.*;
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
 * Edit object. It takes two parameters: object and json. Object should be: 
 * host,service,cloud,site,collector,matrix,serviceType,collector. json is a string 
 * containing json representation of this object.
 * The code parses the string, builds a json object and extracts its id. 
 * Then depending on the object parameter it obtains relevant object from data 
 * store and modifies it according to json object.
 * 
 * If successful the code returns json representation of the updated object.
 * @author tomw
 */
@WebServlet(name = "PsEditObjectServlet", urlPatterns = {"/editObject"})
public class PsEditObjectServlet extends HttpServlet {

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
            out.println("<title>Servlet PsEditObjectServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsEditObjectServlet at " + request.getContextPath() + "</h1>");
            
            String object = request.getParameter("object");
            String jsonString = request.getParameter("json");
            
            if(object!=null && !"".equals(object) && jsonString!=null && !"".equals(jsonString)){
                JSONObject json = null;
                JSONParser parser = new JSONParser();
                try {
                    //unpack host string and convert it to json
                    json = (JSONObject) parser.parse(jsonString);
                    
                    PsDataStore dataStore = PsDataStore.getDataStore();
                    
                    String id = (String)json.get("id");
                    
                    if (object.equals("host")) {
                        PsHost host = dataStore.getHost(id);
                        PsObjectUpdater.edit(host, json);
                        JSONObject newJson = JsonConverter.psHost2Json(host);
                        out.println(newJson.toString());
                    }
                    if (object.equals("service")) {
                        PsService service = dataStore.getService(id);
                        PsObjectUpdater.edit(service, json);
                        JSONObject newJson = JsonConverter.toJson(service);
                        out.println(newJson.toString());
                    }
                    if (object.equals("cloud")) {
                        PsCloud cloud = dataStore.getCloud(id);
                        PsObjectUpdater.edit(cloud, json);
                        JSONObject newJson = JsonConverter.toJson(cloud);
                        out.println(newJson.toString());
                    }
                    if (object.equals("collector")) {
                        PsCollector collector = dataStore.getCollector(id);
                        PsObjectUpdater.edit(collector, json);
                        JSONObject newJson = JsonConverter.toJson(collector);
                        out.println(newJson.toString());
                    }
                    if (object.equals("site")) {
                        PsSite site = dataStore.getSite(id);
                        PsObjectUpdater.edit(site, json);
                        JSONObject newJson = JsonConverter.toJson(site);
                        out.println(newJson.toString());
                    }
                    if (object.equals("serviceType")) {
                        PsServiceType type = dataStore.getServiceType(id);
                        PsObjectUpdater.edit(type, json);
                        JSONObject newJson = JsonConverter.toJson(type);
                        out.println(newJson.toString());
                    }
                    if (object.equals("matrix")) {
                        PsMatrix matrix = dataStore.getMatrix(id);
                        PsObjectUpdater.edit(matrix, json);
                        JSONObject newJson = JsonConverter.toJson(matrix);
                        out.println(newJson.toString());
                    }
                                        
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
