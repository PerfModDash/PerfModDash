<%-- 
    Document   : displayHost
    Created on : Dec 4, 2012, 3:12:36 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display host page</title>
    </head>
    <body>
        <h2 align="center">Host: <s:property value="hostid" /></h2>
        
        <table border="1"  align="center">
        <tbody>
        <tr>
            <td align="center">hostname</td>
            <td align="center"><s:property value="one_host.hostname" /></td>
        </tr>
        <tr>
            <td align="center">ipv4</td>
            <td align="center"><s:property value="one_host.ipv4" /></td>
        </tr>
        <tr>
            <td align="center">ipv6</td>
            <td align="center"><s:property value="one_host.ipv6" /></td>
        </tr>
        <tr>
            <td align="center">services</td>
            <td align="center">
                <s:if test="%{one_host.services.isEmpty()}">
                 no services       
                </s:if>
                <s:else>
                     <s:iterator value="services_on_host">
                         <s:property /><br>
                     </s:iterator>    
                </s:else>
            </td>
        </tr>
        </tbody>
        </table>
        
        <br>
        <s:url action="queryHostList" var="aURL" />
        <s:a href="%{aURL}">return to the list of host</s:a>
        
    </body>
</html>
