<%-- 
    Document   : editHost.jsp
    Created on : Aug 19, 2013,
    Author     : tomw
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <title>Edit Host</title>
    </head>

    <body>

        <div id="editHost">
            
            <form:form commandName="host">
                <p><label><fmt:message key="host.hostname"/></label> <form:input  path="hostname"/></p> 
                <p><label><fmt:message key="host.ipv4"/></label> <form:input path="ipv4"/></p> 
                <p><label><fmt:message key="host.ipv6"/></label> <form:input path="ipv6"/></p> 
               
                

                <input type="submit" action="create/" value="Save"/>
            </form:form>		   	  
        </div>


    </body>
</html>
