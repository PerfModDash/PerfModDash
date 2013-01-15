<%-- 
    Document   : updateMatrixSuccess
    Created on : Dec 17, 2012, 10:05:32 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>update matrix success</title>
    </head>
    <body>
        <h2>Matrix updated!</h2>
        <p>The updated matrix is:</p>
        matrix id<s:property value="newMatrix.matrixId" /><br>
        matrix name:<s:property value="newMatrix.matrixName" /><br>
       
        <br>
        
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
    </body>
</html>