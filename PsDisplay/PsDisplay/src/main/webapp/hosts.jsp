<%-- 
    Document   : index
    Created on : Mar 18, 2013, 9:30:06 AM
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

        <script src="loadHosts.js">  </script>  

        <div id="body">
            <p id="bodyHeader"></p>            

            <p id="hostsTableDiv"></p>

        </div>

        <%@ include file="tail.html" %>


    </body>
</html>