<%-- 
    Document   : history
    Created on : Apr 8, 2013, 10:33:37 AM
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

        <script src="loadHistory.js">  </script>  

        <div id="body">
            <p id="bodyHeader"></p>            

            <p id="historyTableDiv"></p>

        </div>

        <%@ include file="tail.html" %>


    </body>
</html>