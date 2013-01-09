/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets.rest;

import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import gov.bnl.racf.ps.dashboard.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.object_manipulators.*;
import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
//@WebServlet(name = "PsMatrixServletRest", urlPatterns = {"/matrices_rest"})
public class PsMatrixServletRest extends HttpServlet {

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
            out.println("<title>Servlet PsMatrixServletRest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PsMatrixServletRest at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {


            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            PsDataStore dataStore = PsDataStore.getDataStore();

            if (parameters.size() > 0) {
                String matrixIdString = parameters.firstElement();
                PsMatrix matrix = dataStore.getMatrix(matrixIdString);
                out.println(JsonConverter.toJson(matrix).toString());
            } else {
                JSONArray listOfMatrixIds = new JSONArray();
                Iterator<PsMatrix> iter = dataStore.matrixIterator();
                while (iter.hasNext()) {
                    PsMatrix matrix = (PsMatrix) iter.next();
                    listOfMatrixIds.add(matrix.getId());
                }
                out.println(listOfMatrixIds.toString());
            }

        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);

            // no parameters, this is host create request
            if (parameters.isEmpty()) {

                if (jsonObject != null) {

                    String matrixName = (String) jsonObject.get(PsMatrix.NAME);
                    String serviceTypeId = (String) jsonObject.get(PsMatrix.SERVICE_TYPE_ID);

                    // the input data is a valid JSON object

                    // create new matrix
                    PsMatrix matrix =
                            PsObjectCreator.createNewMatrix(serviceTypeId, matrixName);

                    // convert host to json
                    JSONObject finalMatrix = JsonConverter.toJson(matrix);

                    out.println(finalMatrix.toString());
                }
            }

        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>PUT</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        PsDataStore dataStore = PsDataStore.getDataStore();
        System.out.println("We are in DoPut");

        try {

            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            if (parameters.isEmpty()) {
                JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);

                if (jsonObject != null) {

                    String matrixId = (String) jsonObject.get(PsMatrix.ID);

                    // the input data is a valid JSON object

                    PsMatrix matrix = dataStore.getMatrix(matrixId);

                    PsObjectUpdater.update(matrix, jsonObject);

                    // convert matrix to json
                    JSONObject finalMatrix = JsonConverter.toJson(matrix);

                    out.println(finalMatrix.toString());
                }
            } else {
                // add/remove hosts
                if (parameters.size() == 2) {

                    System.out.println("We are in two parameters mode");
                    String matrixId = parameters.elementAt(0);
                    String command = parameters.elementAt(1);
                    System.out.println("matrixId="+matrixId);
                    System.out.println("command="+command);
                    System.out.println("PsApi.MATRIX_ADD_HOST_IDS="+PsApi.MATRIX_ADD_HOST_IDS);

                    JSONArray jsonArray =
                            PostRequestDataExtractor.extractJsonArray(request);
                    PsMatrix matrix = dataStore.getMatrix(matrixId);
                    System.out.println("jsonArray="+jsonArray.toString());

                    if (PsApi.MATRIX_ADD_HOST_IDS.equals(command)) {
                        PsMatrixManipulator.addHostIdsToMatrix(matrix, jsonArray);
                    }
                    if (PsApi.MATRIX_REMOVE_HOST_IDS.equals(command)) {
                        PsMatrixManipulator.removeHostIdsFromMatrix(matrix, jsonArray);
                    }

                    // convert matrix to json
                    JSONObject finalMatrix = JsonConverter.toJson(matrix);
                    out.println(finalMatrix.toString());

                }
//                // only two parameters, additional parameters come in 
//                // data part of the request
//                if (parameters.size() == 2) {
//                    String matrixId = parameters.elementAt(0);
//                    String command = parameters.elementAt(1);
//
//                    PsMatrix matrix = dataStore.getMatrix(matrixId);
//                    
//                    if (matrix != null) {
//
//                        if (PsApi.MATRIX_ADD_HOST_IDS.equals(command)) {
//                            JSONArray jsonArray =
//                                    PostRequestDataExtractor.extractJsonArray(request);
//                            PsMatrixManipulator.addHostIdsToMatrix(matrix,jsonArray);
//                        }
//                        if (PsApi.MATRIX_REMOVE_HOST_IDS.equals(command)) {
//                            JSONArray jsonArray =
//                                    PostRequestDataExtractor.extractJsonArray(request);
//                            PsMatrixManipulator.removeHostIdsFromMatrix(matrix,jsonArray);
//                        }
//                    }


//                    JSONObject jsonObject = PostRequestDataExtractor.extractJson(request);
//
//                    if (jsonObject != null) {
//                        if (PsMatrix.ADD_HOSTS_COMMAND.equals(command)) {
//                            if(jsonObject.containsKey(PsMatrix.HOSTS_IDS)){
//                                JSONArray listOfHostIds=
//                                        (JSONArray)jsonObject.get(PsMatrix.HOSTS_IDS);
//                                Iterator<String> hostIdIterator=listOfHostIds.iterator();
//                                while(hostIdIterator.hasNext()){
//                                    String hostId = (String)hostIdIterator.next();
//                                    PsMatrixManipulator.addHostToMatrix(matrixId, hostId);
//                                }
//                            }
//                        }
//                        if (PsMatrix.REMOVE_HOSTS_COMMAND.equals(command)) {
//                            if(jsonObject.containsKey(PsMatrix.HOSTS_IDS)){
//                                JSONArray listOfHostIds=(JSONArray)jsonObject.get(PsMatrix.HOSTS_IDS);
//                                Iterator<String> hostIdIterator=listOfHostIds.iterator();
//                                while(hostIdIterator.hasNext()){
//                                    String hostId = (String)hostIdIterator.next();
//                                    PsMatrixManipulator.removeHostFromMatrix(matrixId, hostId);
//                                }
//                            }
//                        }
//                    }

            }

        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>DELETE</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {


            Vector<String> parameters = UrlUnpacker.unpack(request.getPathInfo());

            if (parameters.size() > 0) {
                String matrixIdString = parameters.firstElement();

                PsObjectShredder.deleteMatrix(matrixIdString);
            }

        } catch (Exception e) {
            System.out.println(new Date() + " Error in " + getClass().getName() + " " + e);
        } finally {
            out.close();
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
