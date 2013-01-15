<%-- 
    Document   : displaySiteError
    Created on : Dec 12, 2012, 1:40:26 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display site error</title>
    </head>
    <body>
        <p>Failed to display this site!</p>
        <br>
        <s:url action="displaySiteList" var="aURL" />
        <s:a href="%{aURL}">return to the list of sites</s:a>
    </body>
</html>
