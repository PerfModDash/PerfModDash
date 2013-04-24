<%-- 
    Document   : editsite
    Created on : Apr 24, 2013, 9:09:28 AM
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

        <script src="editsite.js">  </script>  

        <div id="body">
            <H1>Edit Site </H1>
            <p id="bodyHeader"></p>            

            <form id="editSiteForm">
                <input type="hidden" value="" id="siteIdField">
                <table id="objects">
                    <tbody>
                        <tr><th></th><th></th></tr>
                        <tr>
                            <td><strong>Site Name:</strong></td><td><input type="text" id="nameField" name="name" value=""></td>
                        </tr>
                        
                        <tr>
                            <td><button onclick="saveSiteData()" type="button">Save</button></td><td><button onclick="cancelSiteEdit()" type="button">Cancel</button></td>
                        </tr>

                    </tbody>
                </table>
            </form>

            <form>
                <table id="objects">
                    <tbody>
                        
                        <tr>
                            <th><button onclick="removeSelectedHostsFromSite()" type="button">Remove Selected Hosts</button></th>
                            <th><button onclick="addSelectedHostsToSite()" type="button">Add Selected Hosts</button></th>
                        </tr>
                        <tr>
                            <th>Hosts in Site</th><th>Hosts not in Site</th>
                        </tr>
                        <tr>
                            <td>
                                <table id="hostsInSiteTable"></table>
                            </td>
                            <td>
                                <table id="hostsNotInSiteTable"></table>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>

        </div>

        <%@ include file="tail.html" %>


    </body>
</html>