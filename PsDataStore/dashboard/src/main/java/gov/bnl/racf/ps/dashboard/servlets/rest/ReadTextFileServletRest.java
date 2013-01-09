/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets.rest;

import gov.bnl.racf.ps.dashboard.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsInitObjects;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsMatrixManipulator;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsObjectCreator;
import gov.bnl.racf.ps.dashboard.object_manipulators.PsServiceTypeFactory;
import java.io.*;
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
 *
 * @author tomw
 */
//@WebServlet(name = "ReadTextFileServletRest", urlPatterns = {"/readFile"})
public class ReadTextFileServletRest extends HttpServlet {

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
            out.println("<title>Servlet ReadTextFileServletRest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReadTextFileServletRest at " + request.getContextPath() + "</h1>");

            String realPath = getServletContext().getRealPath("/WEB-INF/");

            String fileName = realPath + "/usatlas.json";

            String jsonObjectAsString = "";
            
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                jsonObjectAsString =  sb.toString();
                //out.println(jsonObjectAsString);
            } catch (Exception e) {
                System.out.println("failed to read file e=" + e);
            } finally {
                br.close();
            }
            out.println("<BR>=======================================<BR>");
            
            
            // parse input data into JSON object
            JSONObject jsonObject = null;
            try {
                JSONParser parser = new JSONParser();

                jsonObject = (JSONObject) parser.parse(jsonObjectAsString);
                //out.println("<BR>========== JSONObject decoded ==========================<BR>");
                //out.println(jsonObject.toString());

            } catch (Exception e) {
                // crash and burn
                throw new IOException("Error parsing JSON request string");
            }
            
            out.println("<BR>==============================<BR>");
            
            // now we will initialize hosts and services based on the input JSON object
            
            // first of all, let us clear data store
            PsDataStore dataStore = PsDataStore.getDataStore();           
            dataStore.clearAll();
            
            //let us create standard service types
            PsInitObjects.initServiceTypes();
            
            JSONArray tests = (JSONArray)jsonObject.get("tests");
            
            Iterator iter=tests.iterator();
            while(iter.hasNext()){
                out.println("<BR>-------------<BR>");
                JSONObject json = (JSONObject)iter.next();
                String description = (String)json.get("description");
                out.println("<BR>"+json.toString()+"<BR>");
                out.println("<BR>"+description+"<BR>");
                JSONObject parameters = (JSONObject)json.get("parameters");
                out.println("<BR>"+parameters.toString()+"<BR>");
                String serviceType=(String)parameters.get("type");
                out.println("<BR>"+serviceType+"<BR>");
                
                PsMatrix matrix = null;
                
                if("perfsonarbuoy/bwctl".equals(serviceType)){
                    out.println("<BR>bandwidth<BR>");
                    
                    // create new matrix
                    matrix =PsObjectCreator.createNewMatrix(
                            PsServiceTypeFactory.THROUGHPUT,
                            description);
                    
                }
                if("perfsonarbuoy/owamp".equals(serviceType)){
                    out.println("<BR>latency<BR>");
                    
                    // create new matrix
                    matrix =PsObjectCreator.createNewMatrix(
                            PsServiceTypeFactory.LATENCY,
                            description);
                }
                if(matrix!=null){
                    JSONObject members = (JSONObject)json.get("members");
                    out.println("<BR>"+members.toString()+"<BR>");
                    
                    JSONArray listOfHosts = (JSONArray)members.get("members");
                    out.println("<BR>"+listOfHosts.toString()+"<BR>");
                    
                    Iterator hostIter = listOfHosts.iterator();
                    while(hostIter.hasNext()){
                        String hostName = (String)hostIter.next();
                        //out.println("<BR>"+hostName+"<BR>");
                        PsHost host = PsObjectCreator.createNewHost();
                        host.setHostname(hostName);
                        PsMatrixManipulator.addHostToMatrix(matrix,host);
                    }
                }
            }
            
            
            //out.println(tests.toString());
              
//            Iterator iter = jsonObject.keySet().iterator();
//            while(iter.hasNext()){
//                String key=(String)iter.next();
//                
//                out.println("<BR>========== "+key+" ==================<BR>");
//                JSONObject json = (JSONObject)jsonObject.get(key);
//                out.println(json.toString());
//            }
            
                
            

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
