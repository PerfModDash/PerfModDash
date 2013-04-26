<%-- 
    Document   : createdeletematrices
    Created on : Apr 25, 2013, 10:26:53 AM
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

        <script src="createDeleteMatrices.js">  </script>  

        <div id="body">
            <p id="bodyHeader"> </p>   
            <form>
            <table id="objects">
                    <tbody>
                        <tr><th></th><th></th></tr>
                        
                        <tr>
                            <td><button onclick="createNewMatrix()" type="button">Create New Matrix</button></td><td><button onclick="deleteSelectedMatrix()" type="button">Delete Selected Matrix</button></td>
                        </tr>
                            
                    </tbody>
                </table>

            <p id="matricesTableDiv"></p>
            </form>
        </div>

        <%@ include file="tail.html" %>


    </body>
</html>
