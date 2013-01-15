<%-- 
    Document   : cloudARSiteError
    Created on : Dec 13, 2012, 3:42:09 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add or remove sites to cloud error</title>
    </head>
    <body>
        <h2>Failed to add or remove sites to this cloud!</h2>
        <s:url action="queryCloudList" var="aURL" />
        <s:a href="%{aURL}">return to the list of clouds</s:a>
    </body>
</html>
