<%-- 
    Document   : updateCloudSuccess
    Created on : Dec 13, 2012, 12:04:45 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>update cloud success</title>
    </head>
    <body>
        <h2>Cloud updated!</h2>
        <p>The updated cloud is:</p>
        cloud id:<s:property value="newCloud.cloudid" /><br>
        cloud name:<s:property value="newCloud.cloudname" /><br>
       
        <br>
        
        <s:url action="queryCloudList" var="aURL" />
        <s:a href="%{aURL}">return to the list of clouds</s:a>
    </body>
</html>
