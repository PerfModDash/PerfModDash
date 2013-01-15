<%-- 
    Document   : displaySiteListSuccess
    Created on : Dec 12, 2012, 11:40:13 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>list of sites</title>
    </head>
    
    <body>
        <h2 align="center">List of Sites</h2>
        <div align="center">
        Site Number :<s:property value="site_list.siteNumber" /><BR>
        
        
        
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">Site</th>
            <th colspan="3" align="center">Actions</th>
        </tr>
        <s:iterator value="site_list.siteIds" status="SiteListStatus">
        <tr>
            <td valign="top" width="300" align="left">
                <s:url action="querySite" var="siteTag">
                    <s:param name="siteid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#siteTag"/>"> <s:property value="site_list.siteNames.get(#SiteListStatus.index)"/></a>
            </td>
            <td valign="top">
                <s:url action="updateSitePage" var="updateSiteURL">
                    <s:param name="siteid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#updateSiteURL"/>" >edit</a>
            </td>
            <td valign="top">
                <s:url action="deleteSitePage" var="deleteSiteURL">
                    <s:param name="siteid"><s:property/></s:param>
                </s:url>
                <a href="<s:property value="#deleteSiteURL"/>" >delete</a>
            </td>
            <td valign="top">
                <s:url action="siteARHostPage" var="siteARHostURL">
                    <s:param name="siteid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#siteARHostURL"/>" >add_or_remove_hosts </a>
            </td>
       </tr>
        </s:iterator>
        </tbody>
        </table>
        </div>
        <br>
        <div align="center">
            <s:url action="createSitePage" var="createSiteURL" />
            <s:a href="%{createSiteURL}">create a new site</s:a>
        </div>
    </body>
</html>
