<%-- 
    Document   : navigator
    Created on : Dec 18, 2012, 8:35:41 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>navigator</title>
    </head>
    <body bgcolor="Green">
        <div align="center">  
        <h2>Navigator</h2>
        <ul>
            <li>
                <s:url action="queryHostList" var="hostURL"></s:url>
                <a href="<s:property value="#hostURL" />" target="mainFrame">List of Hosts</a>     
            </li>
            <br>
            <li>
                <s:url action="querySiteList" var="siteURL"></s:url>
                <a href="<s:property value="#siteURL" />" target="mainFrame">List of Sites</a>
                   
            </li>
            <br>
            <li>
                <s:url action="queryCloudList" var="cloudURL"></s:url>
                <a href="<s:property value="#cloudURL" />" target="mainFrame">List of Clouds</a>
                 
            </li>
            <br>
            <li>
                <s:url action="queryMatrixList" var="matrixURL"></s:url>
                <a href="<s:property value="#matrixURL" />" target="mainFrame">List of Matrices</a>
              
            </li>
            <br>
            <li>
                <s:url action="configMeshPage" var="meshURL"></s:url>
                <a href="<s:property value="#meshURL" />" target="mainFrame">Mesh Configuration</a>
              
            </li>
        
        </ul>
        
        </div>
        
        
    </body>
</html>
