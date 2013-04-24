<%-- 
    Document   : services
    Created on : Mar 21, 2013, 10:09:25 AM
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

        <script src="loadServices.js">  </script>  

        <div id="body">
            <p id="bodyHeader"></p>            

            <p id="servicesTableDiv"></p>

        </div>

        <%@ include file="tail.html" %>


    </body>
</html>