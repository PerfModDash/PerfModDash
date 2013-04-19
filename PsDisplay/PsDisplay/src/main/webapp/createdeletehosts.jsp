<%-- 
    Document   : createdeletehosts
    Created on : Apr 12, 2013, 1:04:05 PM
    Author     : tomw
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ include file="header.html" %>
    <body>

        <%@ include file="title.html" %>

        <%@ include file="leftNav.html" %>

        <%@ include file="topNav.html" %>

        <script src="createDeleteHosts.js">  </script>  

        <div id="body">
            <p id="bodyHeader"> </p>   
            <form>
            <table id="objects">
                    <tbody>
                        <tr><th></th><th></th></tr>
                        
                        <tr>
                            <td><button onclick="createNewHost()" type="button">Create New Host</button></td><td><button onclick="deleteSelectedHost()" type="button">Delete Selected Host</button></td>
                        </tr>
                            
                    </tbody>
                </table>

            <p id="hostsTableDiv"></p>
            </form>
        </div>

        <%@ include file="tail.html" %>


    </body>
</html>