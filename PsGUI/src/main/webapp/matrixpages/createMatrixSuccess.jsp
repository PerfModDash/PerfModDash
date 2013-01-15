<%-- 
    Document   : createMatrixSuccess
    Created on : Dec 16, 2012, 8:01:42 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create matrix success</title>
    </head>
    <body>
        <h2>Matrix created!</h2>
        <p>The new matrix is:</p>
        matrix name:<s:property value="newMatrix.matrixName" /><br>
       
        <br>
        
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
    </body>
</html>
