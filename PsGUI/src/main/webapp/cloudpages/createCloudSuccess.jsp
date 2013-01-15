<%-- 
    Document   : createCloudSuccess
    Created on : Dec 13, 2012, 11:10:17 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create cloud success</title>
    </head>
    <body>
        <h2>Cloud created!</h2>
        <p>The new cloud is:</p>
        cloud name:<s:property value="newCloud.cloudname" /><br>
       
        <br>
        
        <s:url action="queryCloudList" var="aURL" />
        <s:a href="%{aURL}">return to the list of clouds</s:a>
    </body>
</html>
