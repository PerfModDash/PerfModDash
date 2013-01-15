<%-- 
    Document   : deleteMatrix
    Created on : Dec 17, 2012, 10:34:41 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>delete matrix</title>
    </head>
    <body>
         
          <h2>matrix delete page</h2>
        <br>
        <p>This is matrix is going to be deleted:</p>
        <s:form name="DeleteMatrixForm" action="deleteMatrix" method="post">
            <s:hidden name="matrixId" value="%{matrixId}"></s:hidden>   
            Matrix: <s:property value="one_matrix.matrixName" />
            <br>
            <s:submit value="delete" name="submit" theme="simple"/>   
        </s:form>
        <br>
        
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
    </body>
</html>
