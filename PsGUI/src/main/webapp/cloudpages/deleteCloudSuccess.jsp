<%-- 
    Document   : deleteCloudSuccess
    Created on : Dec 13, 2012, 2:42:09 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>delete cloud success</title>
    </head>
    <body>
        <h2>Cloud deleted!</h2>
        
        <br>
        <s:url action="queryCloudList" var="aURL" />
        <s:a href="%{aURL}">return to the list of clouds</s:a>
    </body>
</html>
