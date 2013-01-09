/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets_debug;

import gov.bnl.racf.ps.dashboard.data_objects.*;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 * Servlet to initialize data in the data store
 * To be used when debugging the dashboard code,
 * not to be used for production
 * should be called only once per session
 * @author tomw
 */
//@WebServlet(name = "PsInitDataServlet", urlPatterns = {"/initData"})
public class PsInitDataServlet extends HttpServlet {

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
            out.println("<title>Servlet PsInitDataServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsInitDataServlet at XXXXX " + request.getContextPath() + "</h1>");
            
            //String s = PsObjectInitiator.init();
            
            PsDataStore dataStore = PsDataStore.getDataStore();
           
            dataStore.clearAll();
            
            out.println("<BR>==== init service types =====<BR>");
            
            PsInitObjects.initServiceTypes();
            
            out.println("<br>======================================<BR>");
            
            out.println("<BR>Create new host...<BR>");
            
            PsHost host = PsObjectCreator.createNewHost();
            
            host.setHostname("psmsu01.aglt2.org");
            host.setIpv4("192.41.236.31");
            out.println("<BR>Convert host to JSON and print...<BR>");
            out.println(JsonConverter.toJson(host));
            
            out.println("<BR>^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^<BR>");
            
            
            PsHostManipulator.addLatencyServices(host);
            
            
            // create next Hosts
            out.println("host1<BR>");
            PsHost psmsu02 = PsObjectCreator.createNewHost();
            psmsu02.setHostname("psmsu02.aglt2.org");
            psmsu02.setIpv4("192.41.236.32");
            PsHostManipulator.addThroughputServices(psmsu02);
            
            out.println("host2<BR>");
            PsHost psum01 = PsObjectCreator.createNewHost();
            psum01.setHostname("psum01.aglt2.org");
            psum01.setIpv4("192.41.230.19");
            PsHostManipulator.addLatencyServices(psum01);
            
            
            out.println("host3<BR>");
            PsHost psum02 = PsObjectCreator.createNewHost();
            psum02.setHostname("psum02.aglt2.org");
            psum02.setIpv4("192.41.230.20");
            PsHostManipulator.addThroughputServices(psum02);
            
            // create site AGLT2
            
            out.println("create site<BR>");
            
            PsSite aglt2 = PsObjectCreator.createNewSite();
            aglt2.setName("AGLT2");
            aglt2.setDescription("USAtlas University of Michigan Tier2 Site");
            aglt2.addHost(host);
            aglt2.addHost(psmsu02);
            aglt2.addHost(psum01);
            aglt2.addHost(psum02);
            
            out.println("print site<BR>");
            
            out.println(JsonConverter.toJson(aglt2));
            
            out.println("<BR>******************************<BR>");
            
            
            //=========== create site BNL ===================
            
            // hosts first
            PsHost lhcperfmon = 
                    PsObjectCreator.createNewHost("lhcperfmon.bnl.gov",
                                                  "192.12.15.26");           
            PsHostManipulator.addLatencyServices(lhcperfmon);
            
            PsHost lhcmon=PsObjectCreator.createNewHost("lhcmon.bnl.gov",
                    "192.12.15.23");
            PsHostManipulator.addThroughputServices(lhcmon);
            
            //create new site
            PsSite bnl = PsObjectCreator.createNewSite("BNL");
            bnl.addHost(lhcperfmon);
            bnl.addHost(lhcmon);
            
            // create MWT2
            PsHost iut2_net1=PsObjectCreator.createNewHost("iut2-net1.iu.edu","149.165.225.223");
            PsHostManipulator.addLatencyServices(iut2_net1);
            
            PsHost iut2_net2=PsObjectCreator.createNewHost("iut2-net2.iu.edu","149.165.225.224");
            PsHostManipulator.addThroughputServices(iut2_net2);
            
            PsHost uct2_net1=PsObjectCreator.createNewHost("uct2-net1.uchicago.edu","128.135.158.216"); 
            PsHostManipulator.addLatencyServices(uct2_net1);
            
            PsHost uct2_net2=PsObjectCreator.createNewHost("uct2-net2.uchicago.edu","128.135.158.219)");
            PsHostManipulator.addThroughputServices(uct2_net2);
            
            PsHost mwt2_ps01=PsObjectCreator.createNewHost("mwt2-ps01.campuscluster.illinois.edu","72.36.88.28");
            PsHostManipulator.addLatencyServices(mwt2_ps01);
            
            PsHost mwt2_ps02=PsObjectCreator.createNewHost("mwt2-ps02.campuscluster.illinois.edu","72.36.88.27");        
            PsHostManipulator.addThroughputServices(mwt2_ps02);
            
            PsSite mwt2 = PsObjectCreator.createNewSite("MWT2");
            mwt2.addHost(iut2_net1);
            mwt2.addHost(iut2_net2);
            mwt2.addHost(uct2_net1);
            mwt2.addHost(uct2_net2);
            mwt2.addHost(mwt2_ps01);
            mwt2.addHost(mwt2_ps02);
            
            // =========== create new matrix, AAA ===============//
            PsMatrix matrix = PsObjectCreator.createNewThroughputMatrix("AAA");
            PsMatrixManipulator.addHostToMatrix(matrix,lhcmon);
            PsMatrixManipulator.addHostToMatrix(matrix,iut2_net1);
            
            
            out.println("<BR>");
            out.println("Matrix:<BR>");
            JSONObject jsonMatrix = JsonConverter.toJson(matrix);
            out.println(jsonMatrix.toString());
            out.println("<BR>");
            // ============ create new cloud, USATLAS ===========//
            
            PsCloud usatlas = PsObjectCreator.createNewCloud("USATLAS");
            usatlas.addSite(bnl);
            usatlas.addSite(aglt2);
            usatlas.addSite(mwt2);
            
            
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
