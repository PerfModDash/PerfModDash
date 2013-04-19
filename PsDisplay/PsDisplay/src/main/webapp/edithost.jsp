<%-- 
    Document   : edithost
    Created on : Apr 17, 2013, 11:10:56 AM
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

        <script src="edithost.js">  </script>  

        <div id="body">
            <p id="bodyHeader"></p>            

            <form id="editHostForm">
                <input type="hidden" value="" id="hostIdField">
                <table id="objects">
                    <tbody>
                        <tr><th></th><th></th></tr>
                        <tr>
                            <td><strong>Host Name:</strong></td><td><input type="text" id="hostnameField" name="hostname" value=""></td>
                        </tr>
                        <tr>
                            <td><strong>IPV4:</strong></td><td><input type="text" id="ipv4Field" name="ipv4" value=""></td>
                        </tr>
                        <tr>
                            <td><strong>IPV6:</strong></td><td><input type="text" id="ipv6Field" name="ipv6" value=""></td>
                        </tr>
                        <tr>
                            <td><button onclick="saveHostData()" type="button">Save</button></td><td><button onclick="cancelHostEdit()" type="button">Cancel</button></td>
                        </tr>

                    </tbody>
                </table>
            </form>

            <form>
                <table id="objects">
                    <tbody>
                        <tr>
                            <th><button onclick="addAllServices()" type="button">Add All Primitive Services</button></th>
                            <th><button onclick="removeAllServices()" type="button">Remove All Primitive Services</button></th>
                        </tr>
                        <tr>
                            <th><button onclick="addLatencyServices()" type="button">Add Latency Services</button></th>
                            <th><button onclick="addThroughputServices()" type="button">Add Throughput Services</button></th>
                        </tr>
                        <tr>
                            <th><button onclick="addSelectedServices()" type="button">Add Selected Services</button></th>
                            <th><button onclick="removeSelectedServices()" type="button">Remove Selected Services</button></th>
                        </tr>
                        <tr>
                            <th>Primitive Services on Host</th><th>Primitive Services Not On Host</th>
                        </tr>
                        <tr>
                            <td>
                                <table id="servicesOnHostTable"></table>
                            </td>
                            <td>
                                <table id="servicesNotOnHostTable"></table>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>

        </div>

        <%@ include file="tail.html" %>


    </body>
</html>