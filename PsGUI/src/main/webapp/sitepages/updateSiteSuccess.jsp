<%-- 
    Document   : updateSiteSuccess
    Created on : Dec 12, 2012, 3:21:18 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>update site success</title>
    </head>
    <body>
        <h2>Site updated!</h2>
        <p>The updated site information:</p>
        site id:<s:property value="newSite.siteid" /><br>
        site name:<s:property value="newSite.sitename" /><br>
        status:<s:property value="newSite.status" /><br>
        description:<s:property value="newSite.description" /><br>
        
        <br>
        
        <s:url action="querySiteList" var="aURL" />
        <s:a href="%{aURL}">return to the list of sites</s:a>
    </body>
</html>
