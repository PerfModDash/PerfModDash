/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;


import gov.bnl.racf.ps.dashboard.data_objects.PsCloud;
import gov.bnl.racf.ps.dashboard.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.JsonConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Add sites to a cloud
 * arguments:
 * cloud = json representation of a cloud, must contain id
 * sites = json array of json objects representing sites
 * adds the sites to the cloud
 * if ok returns json representation of the cloud
 * //TODO authentication servlet filter
 * //TODO proper error exit codes
 * //TODO cleanup and logging
 * @author tomw
 */
@WebServlet(name = "PsCloudAddSitesServlet", urlPatterns = {"/cloudAddSites"})
public class PsCloudAddSitesServlet extends HttpServlet {

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
            out.println("<title>Servlet PsCloudAddSitesServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsCloudAddSitesServlet at " + request.getContextPath() + "</h1>");
            
            String cloudJson = request.getParameter("cloud");
            String sitesJson = request.getParameter("sites");
            
            if(cloudJson!=null && !"".equals(cloudJson) 
                    && sitesJson!=null && !"".equals(sitesJson) ){
                JSONObject jsonCloud = null;
                JSONArray jsonArray = null;
                JSONParser parser = new JSONParser();
                PsDataStore dataStore = PsDataStore.getDataStore();
                
                 try {                                          
                    //unpack host string and convert it to json
                     
                    jsonCloud=(JSONObject)parser.parse(cloudJson);
                    
                    String cloudId = (String)jsonCloud.get("id");
                    
                    PsCloud cloud = dataStore.getCloud(cloudId);                     
                     
                    jsonArray = (JSONArray) parser.parse(sitesJson);
                    Iterator iter = jsonArray.iterator();
                    while(iter.hasNext()){
                        JSONObject siteJson = (JSONObject)iter.next();
                        String id = (String)siteJson.get("id");
                        PsSite site = dataStore.getSite(id);
                        cloud.addSite(site);
                    }   
                    out.println(JsonConverter.toJson(cloud).toString());
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
