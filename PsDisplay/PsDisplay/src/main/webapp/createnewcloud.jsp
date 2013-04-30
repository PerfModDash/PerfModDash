<%-- 
    Document   : createnewcloud
    Created on : Apr 29, 2013, 12:27:35 PM
    Author     : tomw
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.net.InetAddress;" %>
<!DOCTYPE html>
<html>
    <%@ include file="header.html" %>
    <body>

        <%@ include file="title.html" %>

        <%@ include file="leftNav.html" %>

        <%@ include file="topNav.html" %>

        <script src="createnewcloud.js">  </script>  

        <div id="body">
            <H1>Create New Cloud </H1>
            <p id="bodyHeader"></p>            

            <form id="createCloudForm">

                <table id="objects">
                    <tbody>
                        <tr><th></th><th></th></tr>
                        <tr>
                            <td><strong>Cloud Name:</strong></td><td><input type="text" id="nameField" name="name" value=""></td>
                        </tr>
                        
                        <tr>
                            <td><button onclick="createNewCloud()" type="button">Create New Cloud</button></td>
                            <td><button onclick="returnToListofCloudsCrud()" type="button">Cancel, Return to the List of Clouds</button></td>
                        </tr>

                    </tbody>
                </table>
            </form>



        </div>

        <%@ include file="tail.html" %>


    </body>
</html>