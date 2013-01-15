<%-- 
    Document   : displayCloudListSuccess
    Created on : Dec 13, 2012, 9:37:13 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>list of clouds</title>
    </head>
    <body>
        <h2 align="center">List of Clouds</h2>
        <div align="center">
        Cloud Number :<s:property value="cloud_list.cloudNumber" /><BR>
        
        
        
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">Cloud</th>
            <th colspan="4" align="center">Actions</th>
        </tr>
        
        <s:iterator value="cloud_list.cloudIds" status="CloudListStatus">
        <tr>
            <td valign="top" width="300" align="left">
                <s:url action="queryCloud" var="cloudTag">
                    <s:param name="cloudid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#cloudTag"/>"> <s:property value="cloud_list.cloudNames.get(#CloudListStatus.index)"/></a>
            </td>
            
            <td valign="top">
                <s:url action="updateCloudPage" var="updateCloudURL">
                    <s:param name="cloudid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#updateCloudURL"/>" >edit</a>
            </td>
            <td valign="top">
                <s:url action="deleteCloudPage" var="deleteCloudURL">
                    <s:param name="cloudid"><s:property/></s:param>
                </s:url>
                <a href="<s:property value="#deleteCloudURL"/>" >delete</a>
            </td>
            <td valign="top">
                <s:url action="cloudARSitePage" var="cloudARSiteURL">
                    <s:param name="cloudid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#cloudARSiteURL"/>" >add/remove sites </a>
            </td>
             <td valign="top">
                <s:url action="cloudARMatrixPage" var="cloudARMatrixURL">
                    <s:param name="cloudid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#cloudARMatrixURL"/>" >add/remove matrices </a>
            </td>
       </tr>
        </s:iterator>
        </tbody>
        </table>
        </div>
        <br>
        <div align="center">
            <s:url action="createCloudPage" var="createCloudURL" />
            <s:a href="%{createCloudURL}">create a new cloud</s:a>
        </div>
    </body>
</html>
