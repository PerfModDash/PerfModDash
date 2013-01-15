<%-- 
    Document   : deleteHost
    Created on : Dec 6, 2012, 11:23:15 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>delete host</title>
    </head>
    <body>
        
        <h2>host delete page</h2>
        <br>
        <p>This is host is going to be deleted:
        
        
        <s:form name="DeleteHostForm" action="deleteHost" method="post">
        Host: <s:textfield name="hostid" /><s:property value="hostid" />
        </p>
            <s:submit value="delete" name="submit" theme="simple"/>   
        </s:form>
        <br>
        
        <s:url action="queryHostList" var="aURL" />
        <s:a href="%{aURL}">return to the list of host</s:a>
       
    </body>
</html>
