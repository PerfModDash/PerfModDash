<%-- 
    Document   : createHostSuccess
    Created on : Dec 4, 2012, 7:35:18 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create host success</title>
    </head>
    <body>
        <h2>Host created!</h2>
        <p>The new host information:</p>
        hostname:<s:property value="newHost.hostname" /><br>
        ipv4:<s:property value="newHost.ipv4" /><br>
        ipv6:<s:property value="newHost.ipv6" /><br>
        
        <br>
        
        <s:url action="queryHostList" var="aURL" />
        <s:a href="%{aURL}">return to the list of host</s:a>
    </body>
</html>
