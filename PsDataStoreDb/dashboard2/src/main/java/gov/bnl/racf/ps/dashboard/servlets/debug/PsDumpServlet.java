/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets.debug;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsService;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsServiceType;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.JsonConverter;
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
public class PsDumpServlet extends HttpServlet {

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
            out.println("<title>Servlet PsDumpServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsDumpServlet at " + request.getContextPath() + "</h1>");

            out.println("<strong>ServiceTypes:</strong><br>");
            String hql1 = "FROM PsServiceType";
            Query query1 = session.createQuery(hql1);
            List results1 = query1.list();
            Iterator iter1 = results1.iterator();
            while (iter1.hasNext()) {
                PsServiceType serviceType = (PsServiceType) iter1.next();
                JSONObject json = JsonConverter.toJson(serviceType);
                out.println("<BR>");
                out.println(json.toString());
            }

            out.println("<BR><strong>Hosts:</strong><br>");
            String hql = "FROM PsHost";
            Query query = session.createQuery(hql);
            List results = query.list();
            Iterator iter = results.iterator();
            while (iter.hasNext()) {
                PsHost host = (PsHost) iter.next();
                JSONObject json = JsonConverter.toJson(host);
                out.println(json.toString());
            }
            
            out.println("<BR><strong>Services:</strong><br>");
            String hql2 = "FROM PsService";
            Query query2 = session.createQuery(hql2);
            List results2 = query2.list();
            Iterator iter2 = results2.iterator();
            while (iter2.hasNext()) {
                PsService service = (PsService) iter2.next();
                JSONObject json = JsonConverter.toJson(service);
                out.println(json.toString());
            }

            out.println("<BR><strong>Sites:</strong><br>");
            String hql3 = "FROM PsSite";
            Query query3 = session.createQuery(hql3);
            List results3 = query3.list();
            Iterator iter3 = results3.iterator();
            while (iter3.hasNext()) {
                PsSite site = (PsSite) iter3.next();
                JSONObject json = JsonConverter.toJson(site);
                out.println(json.toString());
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
