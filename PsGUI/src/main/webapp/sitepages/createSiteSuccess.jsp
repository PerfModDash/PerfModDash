<%-- 
    Document   : createSiteSuccess
    Created on : Dec 12, 2012, 2:04:45 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create site success</title>
    </head>
    <body>
        <h2>Site created!</h2>
        <p>The new site information:</p>
        site name:<s:property value="newSite.sitename" /><br>
        status:<s:property value="newSite.status" /><br>
        description:<s:property value="newSite.description" /><br>
        
        <br>
        
        <s:url action="querySiteList" var="aURL" />
        <s:a href="%{aURL}">return to the list of sites</s:a>
    </body>
</html>
