<%-- 
    Document   : updateHostSuccess
    Created on : Dec 6, 2012, 11:29:50 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>update host success</title>
    </head>
    <body>
        <h1>Host updated!</h1>
        <p>The updated host information:</p>
        hostid:<s:property value="newHost.hostid" /><br>
        hostname:<s:property value="newHost.hostname" /><br>
        ipv4:<s:property value="newHost.ipv4" /><br>
        ipv6:<s:property value="newHost.ipv6" /><br>
        
        <br>
        
        <br>
        <s:url action="queryHostList" var="aURL" />
        <s:a href="%{aURL}">return to the list of host</s:a>
        
    </body>
</html>
