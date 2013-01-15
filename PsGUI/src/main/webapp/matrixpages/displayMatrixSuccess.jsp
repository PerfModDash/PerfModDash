<%-- 
    Document   : displayMatrixSuccess.jsp
    Created on : Dec 16, 2012, 2:07:59 PM
    Author     : siliu
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display matrix page</title>
    </head>
    <body>
        <div align="center">
            <s:property value="htmlTable" escape="false"/>
        <br>
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
        </div>
    </body>
</html>
