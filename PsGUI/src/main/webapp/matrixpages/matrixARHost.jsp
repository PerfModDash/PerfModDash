<%-- 
    Document   : matrixARHost
    Created on : Dec 17, 2012, 1:13:36 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add or remove hosts in matrix page</title>
    </head>
    <body>
        <div align="center">
        <h2>Add or Remove Hosts in Matrix </h2>
        <p> Please click the link below to add/remove hosts in a matrix: </p>
        <br>
        <br>
        <s:url action="matrixARHostToRowPage" var="matrixARHostToRowPageTag">
           <s:param name="matrixId"><s:property  value="matrixId"/></s:param>
        </s:url>
        <a href="<s:property value="#matrixARHostToRowPageTag"/>" >add/remove hosts to/from matrix row</a>
       
        <br>
        <br>
        
        <s:url action="matrixARHostToColumnPage" var="matrixARHostToColumnPageTag">
           <s:param name="matrixId"><s:property  value="matrixId"/></s:param>
        </s:url>
        <a href="<s:property value="#matrixARHostToColumnPageTag"/>" >add/remove hosts to/from matrix column</a>
       
        <br>
        <br>
        
        <s:url action="matrixARHostToBothPage" var="matrixARHostToBothPageTag">
           <s:param name="matrixId"><s:property  value="matrixId"/></s:param>
        </s:url>
        <a href="<s:property value="#matrixARHostToBothPageTag"/>" >add/remove hosts to/from matrix</a>
       
        <br>
        <br>
           
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
        </div>
    </body>
</html>
