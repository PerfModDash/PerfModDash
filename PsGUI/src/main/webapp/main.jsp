<%-- 
    Document   : main
    Created on : Dec 18, 2012, 8:35:11 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PsGUI main page</title>
    </head>
    
        <frameset rows="10%,*">
            <frame src="<s:url action="head"/>" name="headFrame" scrolling="yes" noresize /> 
            <frameset cols="190,*" border="0"> 
                <frame src="<s:url action="navigator"/>" name="leftFrame" scrolling="yes" noresize />  
                <frame src="<s:url action="introduction"/>"  name="mainFrame" scrolling="yes" noresize /> 
            </frameset>
        </frameset>
        
   
</html>
