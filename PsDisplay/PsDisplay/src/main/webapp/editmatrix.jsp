<%-- 
    Document   : editmatrix
    Created on : Apr 25, 2013, 1:06:28 PM
    Author     : tomw
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.net.InetAddress;" %>
<!DOCTYPE html>
<html>
    <%@ include file="header.html" %>
    <body>

        <%@ include file="title.html" %>

        <%@ include file="leftNav.html" %>

        <%@ include file="topNav.html" %>

        <script src="editmatrix.js">  </script>  

        <div id="body">
            <H1>Edit Matrix </H1>
            <p id="bodyHeader"></p> 

            <table>
                <tbody><tr><td>    


                            <table>
                                <tbody>
                                    <tr><td>

                                            <form id="editMatrixForm">
                                                <input type="hidden" value="" id="matrixIdField">
                                                <table id="objects">
                                                    <tbody>
                                                        <tr><th></th><th></th></tr>
                                                        <tr>
                                                            <td><strong>Matrix Name:</strong></td><td><input type="text" id="nameField" name="name" value=""></td>
                                                        </tr>
                                                        <tr>
                                                            <td><strong>Matrix Type:</strong></td>
                                                            <td>
                                                                <strong id="matrixTypeField"></strong>
                                                            </td>
                                                        </tr>

                                                        <tr>
                                                            <td><button onclick="updateMatrixData()" type="button">Save</button></td><td><button onclick="goBackToListOfMatricesCrud()" type="button">Go Back to List Of Matrices</button></td>
                                                        </tr>

                                                    </tbody>
                                                </table>
                                            </form>
                                        </td></tr>
                                    <tr><td>


                                            <p id="editMatrixTableDiv"></p>
                                        </td></tr>
                                </tbody>
                            </table>
                        </td><td>

                            <form>
                                <table id="objects">
                                    <tbody>
                                        <tr>
                                            <th>Hosts not in Matrix</th>
                                        </tr>
                                        <tr>
                                            <th><button onclick="addSelectedHostToMatrix()" type="button">Add Selected Host to Matrix</button></th>
                                        </tr>

                                        <tr>
                                            <td>
                                                <table id="hostsNotInMatrixTable">

                                                </table>
                                            </td>
                                        </tr>

                                    </tbody>
                                </table>
                            </form>
                        </td></tr>
                </tbody>
            </table>

            <!--   <form id="editMatrixForm">
                            <input type="hidden" value="" id="matrixIdField">
                            <table id="objects">
                                <tbody>
                                    <tr><th></th><th></th></tr>
                                    <tr>
                                        <td><strong>Matrix Name:</strong></td><td><input type="text" id="nameField" name="name" value=""></td>
                                    </tr>
                                    <tr>
                                        <td><strong>Matrix Type:</strong></td>
                                        <td>
                                            <strong id="matrixTypeField"></strong>
                                        </td>
                                    </tr>
            
                                    <tr>
                                        <td><button onclick="updateMatrixData()" type="button">Save</button></td><td><button onclick="goBackToListOfMatricesCrud()" type="button">Go Back to List Of Matrices</button></td>
                                    </tr>
            
                                </tbody>
                            </table>
                        </form>
                       
                        
                        <p id="editMatrixTableDiv"></p>
                        
                        <form>
                            <table id="objects">
                                <tbody>
                                    <tr>
                                        <th>Hosts not in Matrix</th>
                                    </tr>
                                    <tr>
                                         <th><button onclick="addSelectedHostToMatrix()" type="button">Add Selected Host to Matrix</button></th>
                                    </tr>
                                    
                                    <tr>
                                        <td>
                                            <table id="hostsNotInMatrixTable">
            
                                            </table>
                                        </td>
                                    </tr>
            
                                </tbody>
                            </table>
                        </form>-->
        </div>

        <%@ include file="tail.html" %>


    </body>
</html>
