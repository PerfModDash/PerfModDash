/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.servlets;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import gov.bnl.racf.ps.dashboard.servlets.debug.PsDumpServlet;
import gov.bnl.racf.ps.exceptionlogmanager.ExceptionLog;
import gov.bnl.racf.ps.exceptionlogmanager.ExceptionLogImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsServiceTypesServlet extends HttpServlet {

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
            out.println("<title>Servlet PsServiceTypesServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsServiceTypesServlet at " + request.getContextPath() + "</h1>");
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        //boilerplate code to open session
        SessionFactory sessionFactory =
                PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        
        try {
            
            JSONArray listOfServiceTypes = new JSONArray();
            
            String hql1 = "FROM PsServiceType";
            Query query1 = session.createQuery(hql1);
            List results1 = query1.list();
            Iterator iter1 = results1.iterator();
            while (iter1.hasNext()) {
                PsServiceType serviceType = (PsServiceType) iter1.next();
                JSONObject json = JsonConverter.toJson(serviceType);
                listOfServiceTypes.add(json);
            }
            out.println(listOfServiceTypes.toString());
            
        }catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
            Logger.getLogger(PsServiceTypesServlet.class).error(e);
            out.println("Error occured in " + getClass().getName() + " plase check the logs " + e);
            
            ExceptionLog eLog = new ExceptionLogImpl(getServletContext().getRealPath("/"));
            eLog.log(getClass().getName(), e);
           
        } finally {            
            out.close();
            session.close();
        }
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
