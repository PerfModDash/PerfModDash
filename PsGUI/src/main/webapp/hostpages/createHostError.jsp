<%-- 
    Document   : createHostError
    Created on : Dec 4, 2012, 7:35:38 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create host error</title>
    </head>
    <body>
        <h1>Failed to create host!</h1>
        <br>
        <s:url action="queryHostList" var="aURL" />
        <s:a href="%{aURL}">return to the list of host</s:a>
    </body>
</html>
