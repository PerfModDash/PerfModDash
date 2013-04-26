<%-- 
    Document   : createnewmatrix
    Created on : Apr 25, 2013, 11:11:17 AM
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

        <script src="createnewmatrix.js">  </script>  

        <div id="body">
            <H1>Create New Matrix </H1>
            <p id="bodyHeader"></p>            

            <form id="createMatrixForm">

                <table id="objects">
                    <tbody>
                        <tr><th></th><th></th></tr>
                        <tr>
                            <td><strong>Matrix Name:</strong></td><td><input type="text" id="nameField" name="name" value=""></td>
                        </tr>
                        <tr>
                            <td><strong>Matrix Type:</strong></td>
                            <td>
                                <label> 
                                    <input type="radio" name="matrixType" value="throughput" id="matrixType"> Throughput
                                </label> <br> 
                                <label> 
                                    <input type="radio" name="matrixType" value="latency" id="matrixType">Latency
                                </label>
                            </td>
                        </tr>
                        <tr>
                            <td><button onclick="createNewMatrix()" type="button">Create</button></td>
                            <td><button onclick="returnToListofMatricesCrud()" type="button">Cancel</button></td>
                        </tr>

                    </tbody>
                </table>
            </form>



        </div>

        <%@ include file="tail.html" %>


    </body>
</html>