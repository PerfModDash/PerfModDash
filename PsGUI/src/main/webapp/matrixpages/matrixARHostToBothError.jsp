<%-- 
    Document   : matrixARHostToBothError
    Created on : Dec 17, 2012, 3:17:33 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add/remove hosts to/from matrix error</title>
    </head>
    <body>
        <h2>Failed to add/remove hosts to/from this matrix!</h2>
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
    </body>
</html>
