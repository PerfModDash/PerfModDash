<%-- 
    Document   : primitiveServARtoHostError
    Created on : Dec 10, 2012, 3:50:51 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add or remove primitive services to host error</title>
    </head>
    <body>
        <h2>Failed to add or remove primitive services to this host!</h2>
         <s:url action="queryHostList" var="aURL" />
         <s:a href="%{aURL}">return to the list of host</s:a>
    </body>
</html>
