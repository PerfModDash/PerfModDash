<%-- 
    Document   : displayHostList
    Created on : Dec 4, 2012, 12:34:54 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>list of hosts</title>
    </head>
    <body>
        <div align="center">
        <h2 align="center">List of Hosts</h2>
        <p align="center">Host Number :<s:property value="host_list.hostNumber" /></p>   
        
        <table border="1"  align="center">
        <tbody>
        <tr>
            <td align="center"><b>Host</b></td>
            <td colspan="3" align="center"><b>Actions</b></td>
        </tr>
        <s:iterator value="host_list.hostIds" status="HostListStatus">
        <tr>
            <td valign="top" width="300" align="left">
                <s:url action="queryHost" var="hostTag">
                    <s:param name="hostid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#hostTag"/>" > <s:property value="host_list.hostNames.get(#HostListStatus.index)"/></a>
            </td>
            <td valign="top">
                <s:url action="updateHostPage" var="editHostURL">
                    <s:param name="hostid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#editHostURL"/>" >edit</a>
            </td>
            <td valign="top">
                <s:url action="deleteHostPage" var="deleteHostURL">
                    <s:param name="hostid"><s:property/></s:param>
                </s:url>
                <a href="<s:property value="#deleteHostURL"/>" >delete</a>
            </td>
            <td valign="top">
                <s:url action="primitiveServARtoHostPage" var="servicesHostURL">
                    <s:param name="hostid"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#servicesHostURL"/>" >add_or_remove_primitive_services </a>
            </td>
       </tr>
        </s:iterator>
        </tbody>
        </table>
        </div>
        <br>
        <div align="center">
            <s:url action="createHostPage" var="createHostURL" />
            <s:a href="%{createHostURL}">create a new host</s:a>
        </div>
       

    </body>
</html>
