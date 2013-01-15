<%-- 
    Document   : matrixARHostError
    Created on : Dec 17, 2012, 1:27:08 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add/remove hosts to/from matrix row error</title>
    </head>
    <body>
        <h2>Failed to add/remove hosts to/from this matrix row!</h2>
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
    </body>
</html>
