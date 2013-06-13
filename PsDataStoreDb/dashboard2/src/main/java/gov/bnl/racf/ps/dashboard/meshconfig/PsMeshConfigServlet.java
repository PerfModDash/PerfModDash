/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.meshconfig;

import gov.bnl.racf.ps.dashboard.db.servlets.PostRequestDataExtractor;
import gov.bnl.racf.ps.dashboard.db.servlets.PsHostsServlet;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsMeshConfigServlet extends HttpServlet {

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
            out.println("<title>Servlet PsMeshConfigServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsMeshConfigServlet at " + request.getContextPath() + "</h1>");
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        
        StringBuffer outBuffer = new StringBuffer();
        JSONObject resultObject = new JSONObject();
        
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PsMeshConfigServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("I will extract data");
            out.println("<h1>Servlet PsMeshConfigServlet at " + request.getContextPath() + "</h1>");
           
            //String jsonString = PostRequestDataExtractor.extractString(request);

            JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);

            MeshConfigurator configurator = new MeshConfigurator();
            configurator.setJson(jsonObject);
            
            configurator.setOut(out);
            configurator.setOutBuffer(outBuffer);
            configurator.setSession(session);
            

            
            configurator.configure();
            
            // commit transaction and close session
            session.getTransaction().commit();
            
            
            resultObject.put("status",0);
            resultObject.put("output",outBuffer.toString());
            
            out.println(resultObject.toString());
            
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            System.out.println(new Date() + " " + getClass().getName() + " " + e);
            session.getTransaction().rollback();
            Logger.getLogger(getClass()).error(e);
            e.printStackTrace(out);
            out.println("Error occured in " + getClass().getName() + " plase check the logs<BR>" + e);
            resultObject.put("status",999);
            resultObject.put("output",outBuffer.toString());
            out.println(resultObject.toString());
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
