<%-- 
    Document   : index
    Created on : Mar 2, 2013, 9:30:06 AM
    Author     : tomw
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ include file="header.html" %>
    <body>

        <%@ include file="title.html" %>

        <%@ include file="leftNav.html" %>

        <%@ include file="topNav.html" %>

        <div id="body">
            <p id="bodyHeader"></p>    

            <script src="loadClouds.js"></script>

            <p id="cloudsTableDiv"></p>

        </div>

        <%@ include file="tail.html" %>


    </body>
</html>