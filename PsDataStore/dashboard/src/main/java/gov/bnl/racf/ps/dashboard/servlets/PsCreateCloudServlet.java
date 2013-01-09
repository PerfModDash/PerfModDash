/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets;

import gov.bnl.racf.ps.dashboard.data_objects.PsCloud;
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
 * create a cloud. If parameter name is specified - it will give the cloud the
 * requested name. Returns JSON object of the cloud.
 *
 * @author tomw
 */
//@WebServlet(name = "PsCreateCloudServlet", urlPatterns = {"/createCloud"})
public class PsCreateCloudServlet extends HttpServlet {

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
//            out.println("<title>Servlet PsCreateCloudServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet PsCreateCloudServlet at " + request.getContextPath() + "</h1>");

            PsCloud newCloud = PsObjectCreator.createNewCloud();

            if (request.getParameter("name") != null
                    && !"".equals(request.getParameter("name"))) {
                String cloudName = request.getParameter("name").trim();
                newCloud.setName(cloudName);
            }

//            String cloudString = request.getParameter("cloud");
//            
//            if(cloudString!=null && !"".equals(cloudString)){            
//                JSONParser parser = new JSONParser();
//                JSONObject json=null;
//                try {
//                    //unpack site string and convert it to json
//                    json = (JSONObject) parser.parse(cloudString);
//                    PsObjectUpdater.update(newCloud,json);
//                }catch(Exception e){
//                    
//                    //TODO raise proper error code
//                    Date date = new Date();                   
//                    String currentClass = getClass().getName();
//                    System.out.println(date+" "+currentClass+" cloud creation failed");
//                    System.out.println(date+" "+currentClass+" cloud="+cloudString);
//                    System.out.println(date+" "+currentClass+" e="+e);
//                    PsObjectShredder.deleteCloud(newCloud);
//                }
//                
//            }
            out.println(JsonConverter.toJson(newCloud).toString());




//            out.println("</body>");
//            out.println("</html>");
        } catch (Exception e) {
            System.out.println(new Date() + " " + getClass().getName() + " " + e);
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
