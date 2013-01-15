<%-- 
    Document   : deleteCloud
    Created on : Dec 13, 2012, 2:41:54 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>delete cloud</title>
    </head>
    <body>
         
          <h2>cloud delete page</h2>
        <br>
        <p>This is cloud is going to be deleted:</p>
        <s:form name="DeleteCloudForm" action="deleteCloud" method="post">
            <s:hidden name="cloudid" value="%{cloudid}"></s:hidden>   
            Cloud: <s:property value="one_cloud.cloudname" />
            <br>
            <s:submit value="delete" name="submit" theme="simple"/>   
        </s:form>
        <br>
        
        <s:url action="queryCloudList" var="aURL" />
        <s:a href="%{aURL}">return to the list of clouds</s:a>
    </body>
</html>
