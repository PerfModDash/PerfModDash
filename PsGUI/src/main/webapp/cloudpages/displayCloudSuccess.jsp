<%-- 
    Document   : displayCloudSuccess
    Created on : Dec 13, 2012, 10:09:32 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display cloud page</title>
    </head>
    <body>
        <div align="center">
        <h2 align="center">cloud: <s:property value="one_cloud.cloudname" /></h2>
        
        <table border="1"  align="center">
        <tbody>
        <tr>
            <td align="center">cloud id</td>
            <td align="center"><s:property value="one_cloud.cloudid" /></td>
        </tr>
        <tr>
            <td align="center">status</td>
            <td align="center"><s:property value="one_cloud.status" /></td>
        </tr>
     
        <tr>
            <td align="center">sites</td>
            <td align="center">
                <s:if test="%{one_cloud.sites.isEmpty()}">
                 no hosts       
                </s:if>
                <s:else>
                     <s:iterator value="sites_in_cloud">
                         <s:property /><br>
                     </s:iterator>    
                </s:else>
            </td>
        </tr>
        
        <tr>
            <td align="center">matrices</td>
            <td align="center">
                <s:if test="%{one_cloud.matrices.isEmpty()}">
                 no matrix       
                </s:if>
                <s:else>
                     <s:iterator value="matrices_in_cloud">
                         <s:property /><br>
                     </s:iterator>    
                </s:else>
            </td>
        </tr>
        
        </tbody>
        </table>
        
        <br>
        <s:url action="queryCloudList" var="aURL" />
        <s:a href="%{aURL}">return to the list of clouds</s:a>
        </div>
    </body>
</html>

