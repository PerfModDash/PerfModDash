<%-- 
    Document   : createdeletesites
    Created on : Apr 23, 2013, 2:08:44 PM
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

        <script src="createDeleteSites.js">  </script>  

        <div id="body">
            <p id="bodyHeader"> </p>   
            <form>
            <table id="objects">
                    <tbody>
                        <tr><th></th><th></th></tr>
                        
                        <tr>
                            <td><button onclick="createNewSite()" type="button">Create New Site</button></td><td><button onclick="deleteSelectedSite()" type="button">Delete Selected Site</button></td>
                        </tr>
                            
                    </tbody>
                </table>

            <p id="sitesTableDiv"></p>
            </form>
        </div>

        <%@ include file="tail.html" %>


    </body>
</html>