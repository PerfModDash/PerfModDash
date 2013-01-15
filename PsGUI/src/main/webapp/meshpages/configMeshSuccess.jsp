<%-- 
    Document   : configMeshSuccess
    Created on : Jan 7, 2013, 5:13:38 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>mesh configuration success</title>
    </head>
    <body>
        <div align="center">
            <s:iterator value="htmlTables" status="HtmlTablesStatus">
                <li>
                    <s:property escape="false"/>
                </li>

            </s:iterator>
            
            
        </div>
    </body>
</html>
