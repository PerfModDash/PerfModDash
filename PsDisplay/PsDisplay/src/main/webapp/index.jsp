<%-- 
    Document   : index
    Created on : Mar 2, 2013, 9:30:06 AM
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
        
        

       <div id="body">
            <H1>Welcome</H1>

            This is Perfsonar Dashboard main page<br>
            
            We will put some content here, later.<br>
            
            The time is now <%= new java.util.Date()%>
           
            The local ip is <%= InetAddress.getLocalHost().getHostAddress() %>

        </div>

 <%@ include file="tail.html" %>
        

    </body>
</html>