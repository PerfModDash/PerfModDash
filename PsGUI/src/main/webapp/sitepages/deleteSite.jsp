<%-- 
    Document   : deleteSite
    Created on : Dec 12, 2012, 6:51:14 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>delete site</title>
    </head>
    <body>
         
          <h2>site delete page</h2>
        <br>
        <p>This is site is going to be deleted:
        
        
        <s:form name="DeleteSiteForm" action="deleteSite" method="post">
            <s:hidden name="siteid" value="%{siteid}"></s:hidden>   
            Site: <s:property value="one_site.sitename" />
            </p>
            <s:submit value="delete" name="submit" theme="simple"/>   
        </s:form>
        <br>
        
        <s:url action="querySiteList" var="aURL" />
        <s:a href="%{aURL}">return to the list of sites</s:a>
    </body>
</html>
