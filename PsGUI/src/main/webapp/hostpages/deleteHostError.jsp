<%-- 
    Document   : deleteHostError
    Created on : Dec 6, 2012, 11:24:47 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>delete host error</title>
    </head>
    <body>
        <h2>Failed to delete host!</h2>
        
        <br>
        <s:url action="queryHostList" var="aURL" />
        <s:a href="%{aURL}">return to the list of host</s:a>
    </body>
</html>
