<%-- 
    Document   : siteARHostError
    Created on : Dec 12, 2012, 9:01:08 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add or remove hosts to site error</title>
    </head>
    <body>
        <h2>Failed to add or remove hosts to this site!</h2>
        <s:url action="querySiteList" var="aURL" />
        <s:a href="%{aURL}">return to the list of sites</s:a>
    </body>
</html>
