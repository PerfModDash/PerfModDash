/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.servlets;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.JsonConverter;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsServiceTypeFactory;
import gov.bnl.racf.ps.dashboard.db.session_factory_store.PsSessionFactoryStore;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class InitServiceTypes extends HttpServlet {

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
            // first order of business is to open session
            //boilerplate code to open session
            SessionFactory sessionFactory =
                    PsSessionFactoryStore.getSessionFactoryStore().getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InitServiceTypes</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InitServiceTypes at " + request.getContextPath() + "</h1>");

            List<String> listOfServiceTypeIds = PsServiceTypeFactory.listOfServiceTypes();
            Iterator<String> iter = listOfServiceTypeIds.iterator();
            while (iter.hasNext()) {
                String serviceTypeId = (String) iter.next();
                out.println(serviceTypeId + " ");
                Query query = session.createQuery("from PsServiceType where serviceTypeId = :code ");
                query.setParameter("code", serviceTypeId);
                List list = query.list();
                if(list.isEmpty()){
                    out.println("missing<BR>");
                    PsServiceType type = PsServiceTypeFactory.createType(serviceTypeId);
                    session.save(type);
                    out.println("saved<BR>");
                    //JSONObject json = JsonConverter.toJson(type);
                    //out.println(json.toString()+"<BR>");
                }else{
                    out.println("exists<BR>");
                }
            }

            out.println("</body>");
            out.println("</html>");


            // commit transaction and close session
            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
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
