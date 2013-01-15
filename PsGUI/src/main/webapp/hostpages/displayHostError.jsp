<%-- 
    Document   : displayHostError
    Created on : Dec 4, 2012, 3:12:50 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display host error</title>
    </head>
    <body>
        <p>Failed to display this host detail information!</p>
        <br>
        <s:url action="queryHostList" var="aURL" />
        <s:a href="%{aURL}">return to the list of host</s:a>
    </body>
</html>
