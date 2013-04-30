<%-- 
    Document   : createdeleteclouds
    Created on : Apr 29, 2013, 11:55:05 AM
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

        <script src="createDeleteClouds.js">  </script>  

        <div id="body">
            <p id="bodyHeader"> </p>   
            <form>
            <table id="objects">
                    <tbody>
                        <tr><th></th><th></th></tr>
                        
                        <tr>
                            <td><button onclick="createNewCloud()" type="button">Create New Cloud</button></td><td><button onclick="deleteSelectedCloud()" type="button">Delete Selected Cloud</button></td>
                        </tr>
                            
                    </tbody>
                </table>

            <p id="cloudsTableDiv"></p>
            </form>
        </div>

        <%@ include file="tail.html" %>


    </body>
</html>
