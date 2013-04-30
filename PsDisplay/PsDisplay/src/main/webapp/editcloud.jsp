<%-- 
    Document   : editcloud
    Created on : Apr 29, 2013, 1:36:24 PM
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

        <script src="editcloud.js">  </script>  

        <div id="body">
            <H1>Edit Cloud </H1>
            <p id="bodyHeader"></p> 


            <table>
                <tbody>
                    <tr><td>

                            <form id="editCloudForm">
                                <input type="hidden" value="" id="cloudIdField">
                                <table id="objects">
                                    <tbody>
                                        <tr><th></th><th></th></tr>
                                        <tr>
                                            <td><strong>Cloud Name:</strong></td><td><input type="text" id="nameField" name="name" value=""></td>
                                        </tr>


                                        <tr>
                                            <td><button onclick="updateCloudData()" type="button">Save</button></td><td><button onclick="goBackToListOfCloudsCrud()" type="button">Go Back to List Of Clouds</button></td>
                                        </tr>

                                    </tbody>
                                </table>
                            </form>
                        </td></tr>
                    <tr><td>


                            <p id="editCloudTableDiv"></p>
                        </td></tr>
                </tbody>
            </table>


            <table>
                <tbody><tr>
                        <td valign="top">    
                            <form>
                                <table id="objects">
                                    <tbody>
                                        <tr>
                                            <th>Sites in Cloud</th>
                                        </tr>
                                        <tr>
                                            <th><button onclick="removeSelectedSiteFromCloud()" type="button">Remove Selected Site from Cloud</button></th>
                                        </tr>

                                        <tr>
                                            <td>
                                                <table id="sitesInCloudTable">

                                                </table>
                                            </td>
                                        </tr>

                                    </tbody>
                                </table>
                            </form>
                        </td>
                        <td valign="top">
                            <form>
                                <table id="objects">
                                    <tbody>
                                        <tr>
                                            <th>Sites not in Cloud</th>
                                        </tr>
                                        <tr>
                                            <th><button onclick="addSelectedSiteToCloud()" type="button">Add Selected Site to Cloud</button></th>
                                        </tr>

                                        <tr>
                                            <td>
                                                <table id="sitesNotInCloudTable">

                                                </table>
                                            </td>
                                        </tr>

                                    </tbody>
                                </table>
                            </form>
                        </td>
                    </tr>
                </tbody>
            </table>
            
            
            <table>
                <tbody><tr>
                        <td valign="top">    
                            <form>
                                <table id="objects">
                                    <tbody>
                                        <tr>
                                            <th>Matrices in Cloud</th>
                                        </tr>
                                        <tr>
                                            <th><button onclick="removeSelectedMatrixFromCloud()" type="button">Remove Selected Matrix from Cloud</button></th>
                                        </tr>

                                        <tr>
                                            <td>
                                                <table id="matricesInCloudTable">

                                                </table>
                                            </td>
                                        </tr>

                                    </tbody>
                                </table>
                            </form>
                        </td>
                        <td valign="top">
                            <form>
                                <table id="objects">
                                    <tbody>
                                        <tr>
                                            <th>Matrices not in Cloud</th>
                                        </tr>
                                        <tr>
                                            <th><button onclick="addSelectedMatrixToCloud()" type="button">Add Selected Matrix to Cloud</button></th>
                                        </tr>

                                        <tr>
                                            <td>
                                                <table id="matricesNotInCloudTable">

                                                </table>
                                            </td>
                                        </tr>

                                    </tbody>
                                </table>
                            </form>
                        </td>
                    </tr>
                </tbody>
            </table>

        </div>

        <%@ include file="tail.html" %>


    </body>
</html>
