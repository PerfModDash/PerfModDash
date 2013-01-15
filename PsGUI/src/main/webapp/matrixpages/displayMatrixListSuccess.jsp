<%-- 
    Document   : displayMatrixListSuccess
    Created on : Dec 16, 2012, 2:07:27 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>list of matrices</title>
    </head>
    <body>
        <div align="center">
        <h2 align="center">List of Matrices</h2>
        
        Matrix Number :<s:property value="matrix_list.matrixNumber" /><BR>
        
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">Matrix</th>
            <th align="center">Type</th>
            <th colspan="4" align="center">Actions</th>
        </tr>
        
        <s:iterator value="matrix_list.matrixIds" status="MatrixListStatus">
        <tr>
            <td valign="top" width="300" align="left">
                <s:url action="queryMatrix" var="matrixTag">                   
                    <s:param name="matrixId"><s:property /></s:param>    
                </s:url>
                <a href="<s:property value="#matrixTag"/>"> <s:property value="matrix_list.matrixNames.get(#MatrixListStatus.index)"/></a>
            </td>
            
            <td valign="top">
                <s:property value="matrix_list.matrixTypes.get(#MatrixListStatus.index)" />
                
            </td>
            
            <td valign="top">
                <s:url action="updateMatrixPage" var="updateMatrixURL">
                    <s:param name="matrixId"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#updateMatrixURL"/>" >edit</a>
            </td>
            <td valign="top">
                <s:url action="deleteMatrixPage" var="deleteMatrixURL">
                    <s:param name="matrixId"><s:property/></s:param>
                </s:url>
                <a href="<s:property value="#deleteMatrixURL"/>" >delete</a>
            </td>
            <td valign="top">
                <s:url action="matrixARHostPage" var="matrixARHostURL">
                    <s:param name="matrixId"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#matrixARHostURL"/>" >add/remove hosts </a>
            </td>
             <td valign="top">
                <s:url action="selectSchedulerPage" var="selectSchedulerURL">
                    <s:param name="matrixId"><s:property /></s:param>
                </s:url>
                <a href="<s:property value="#selectSchedulerURL"/>" >selector scheduler </a>
            </td>
       </tr>
        </s:iterator>
        </tbody>
        </table>
        
        <br>
        <br>
            <s:url action="createMatrixPage" var="createThroughputMatrixURL" >
                <s:param name="serviceTypeId">throughput</s:param>
            </s:url>   
            <s:a href="%{createThroughputMatrixURL}">create a new throughput matrix</s:a>
            <br><br>
            
            <s:url action="createMatrixPage" var="createLatencyMatrixURL">
                <s:param name="serviceTypeId">latency</s:param>
            </s:url>
            <s:a href="%{createLatencyMatrixURL}">create a new latency matrix</s:a>
            <br><br>
            
            <s:url action="createMatrixPage" var="createTracerouteMatrixURL" >
                <s:param name="serviceTypeId">traceroute</s:param>
            </s:url>
            <s:a href="%{createTracerouteMatrixURL}">create a new traceroute matrix</s:a>
            <br><br>
            
            <s:url action="createMatrixPage" var="createAPDThroughputMatrixURL" >
                <s:param name="serviceTypeId">APD_throughput</s:param>
            </s:url>
            <s:a href="%{createAPDThroughputMatrixURL}">create a new APD throughput matrix</s:a>
            <br><br>
            
            <s:url action="createMatrixPage" var="createAPDLatencyMatrixURL" >
                <s:param name="serviceTypeId">APD_latency</s:param>
            </s:url>
            <s:a href="%{createAPDLatencyMatrixURL}">create a new APD latency matrix</s:a>
        </div>
    </body>
</html>
