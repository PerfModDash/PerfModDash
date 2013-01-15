<%-- 
    Document   : displayCloudError
    Created on : Dec 13, 2012, 10:09:45 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display cloud error</title>
    </head>
    <body>
        <p>Failed to display this cloud!</p>
        <br>
        <s:url action="displayCloudList" var="aURL" />
        <s:a href="%{aURL}">return to the list of clouds</s:a>
    </body>
</html>
