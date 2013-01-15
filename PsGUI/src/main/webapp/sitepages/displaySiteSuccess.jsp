<%-- 
    Document   : displaySiteSuccess
    Created on : Dec 12, 2012, 1:40:10 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display site page</title>
    </head>
    <body>
        <div align="center">
        <h2 align="center">Site: <s:property value="one_site.sitename" /></h2>
        
        <table border="1"  align="center">
        <tbody>
        <tr>
            <td align="center">siteid</td>
            <td align="center"><s:property value="one_site.siteid" /></td>
        </tr>
        <tr>
            <td align="center">status</td>
            <td align="center"><s:property value="one_site.status" /></td>
        </tr>
        <tr>
            <td align="center">description</td>
            <td align="center"><s:property value="one_site.description" /></td>
        </tr>
        <tr>
            <td align="center">hosts</td>
            <td align="center">
                <s:if test="%{one_site.hosts.isEmpty()}">
                 no hosts       
                </s:if>
                <s:else>
                     <s:iterator value="hosts_in_site">
                         <s:property /><br>
                     </s:iterator>    
                </s:else>
            </td>
        </tr>
        </tbody>
        </table>
        
        <br>
        <s:url action="querySiteList" var="aURL" />
        <s:a href="%{aURL}">return to the list of sites</s:a>
        </div>
    </body>
</html>
