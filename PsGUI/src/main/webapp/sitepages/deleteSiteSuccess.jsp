<%-- 
    Document   : deleteSiteSuccess
    Created on : Dec 12, 2012, 6:51:30 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>delete site success</title>
    </head>
    <body>
        <h2>Site deleted!</h2>
        
        <br>
        <s:url action="querySiteList" var="aURL" />
        <s:a href="%{aURL}">return to the list of sites</s:a>
    </body>
</html>
