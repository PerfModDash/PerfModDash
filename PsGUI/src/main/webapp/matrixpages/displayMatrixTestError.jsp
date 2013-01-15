<%-- 
    Document   : displayMatrixTestError
    Created on : Dec 16, 2012, 2:08:43 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display matrix error</title>
    </head>
    <body>
        <p>Failed to display  the test detail!</p>
        <br>
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
    </body>
</html>