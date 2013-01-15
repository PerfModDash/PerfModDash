<%-- 
    Document   : displayMatrixTestSuccess
    Created on : Dec 16, 2012, 2:08:28 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display matrix test detail page</title>
    </head>
    <body bgcolor="<s:if test="%{resultStatus == 0}">green</s:if>
          <s:elseif test="%{resultStatus == 1}">yellow</s:elseif>
          <s:elseif test="%{resultStatus == 2}">red</s:elseif>
          <s:elseif test="%{resultStatus == 3}">brown</s:elseif>
          <s:elseif test="%{resultStatus == 4}">grey</s:elseif>
          
          
          
          ">
       
        <div align="left">
            Status: <s:property value="resultStatus"/><br>
            Message: <s:property value="resultMessage"/><br>
            <br>
            Monitor: <s:property value="monitor"/><br>
            Source: <s:property value="source"/><br>
            Destination: <s:property value="destination"/><br>
            <br>
            Min: <s:property value="min"/><br>
            Max: <s:property value="max"/><br>
            Average: <s:property value="average"/><br>
            <br>
            Command: <s:property value="command"/><br>
            Count: <s:property value="count"/><br>
            Check interval: <s:property value="checkInterval"/><br>
            Next check time: <s:property value="nextCheckTime"/><br>
            
        
        <br>
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
        </div>
    </body>
</html>
