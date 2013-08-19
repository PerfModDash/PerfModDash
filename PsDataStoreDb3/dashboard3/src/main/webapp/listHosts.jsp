<%-- 
    Document   : listOfEntries
    Created on : Aug 11, 2013, 9:32:59 PM
    Author     : tomw
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>

    <head>
        <title>List of Hosts</title>
        <!--        <link href="styles.css" rel="Stylesheet" type="text/css"/>-->
    </head>

    <body>

        <div id="entries">
            <H1>Hosts</h1>
<!--             <img src="../../images/leftArrow.png">
              <img src="../../images/downArrow.png">-->
            <table>
                <tr>
                    <td>

                    </td>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    id
                                </td>
                                <td>
                                    <form method="GET">
                                        <input type="hidden" name="sortingOrder" value="down"/>
                                        <input type="hidden" name="sortingVariable" value="id"/>
                                        <input type="image" src="../../images/downArrow.png" alt="Sort Down" title="Sort Down"/>
                                    </form>
                                </td>
                                <td>
                                    <form method="GET">
                                        <input type="hidden" name="sortingOrder" value="up"/>
                                        <input type="hidden" name="sortingVariable" value="id"/>
                                        <input type="image" src="../../images/upArrow.png" alt="Sort Up" />
                                    </form>
                                </td>
                            <tr>
                        </table>
                    </td>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    Hostname
                                </td>
                                <td>
                                    <form method="GET">
                                        <input type="hidden" name="sortingOrder" value="down"/>
                                        <input type="hidden" name="sortingVariable" value="hostname"/>
                                        <input type="image" src="../../images/downArrow.png" alt="Sort Down" title="Sort Down"/>
                                    </form>
                                </td><td>
                                    <form method="GET">
                                        <input type="hidden" name="sortingOrder" value="up"/>
                                        <input type="hidden" name="sortingVariable" value="hostname"/>
                                        <input type="image" src="../../images/upArrow.png" alt="Sort Up" />
                                    </form>
                                </td>
                            <tr>
                        </table>
                    </td>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    ipv4
                                </td>
                                <td>
                                    <form method="GET">
                                        <input type="hidden" name="sortingOrder" value="down"/>
                                        <input type="hidden" name="sortingVariable" value="ipv4"/>
                                        <input type="image" src="../../images/downArrow.png" alt="Sort Down" title="Sort Down"/>
                                    </form>
                                </td><td>
                                    <form method="GET">
                                        <input type="hidden" name="sortingOrder" value="up"/>
                                        <input type="hidden" name="sortingVariable" value="ipv4"/>
                                        <input type="image" src="../../images/upArrow.png" alt="Sort Up" />
                                    </form>
                                </td>
                            <tr>
                        </table>
                    </td>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    ipv6
                                </td>
                                <td>
                                    <form method="GET">
                                        <input type="hidden" name="sortingOrder" value="down"/>
                                        <input type="hidden" name="sortingVariable" value="ipv6"/>
                                        <input type="image" src="../../images/downArrow.png" alt="Sort Down" title="Sort Down"/>
                                    </form>
                                </td><td>
                                    <form method="GET">
                                        <input type="hidden" name="sortingOrder" value="up"/>
                                        <input type="hidden" name="sortingVariable" value="ipv6"/>
                                        <input type="image" src="../../images/upArrow.png" alt="Sort Up" />
                                    </form>
                                </td>
                            <tr>
                        </table>
                    </td>
                </tr>
                <c:forEach items="${listOfHosts}" var="host">

                    <tr>
                        <td>
                            <table>
                                <tr>
                                    <td>
                                        <form method="post" action="delete/">
                                            <input type="hidden" name="id" value="${host.id}"/>                                
                                            <input type="image" src="../../images/delete-icon.png" alt="Delete" title="Delete"/>
                                        </form>
                                    </td>
                                    <td>
                                        <form method="get" action="showEntry">
                                            <input type="hidden" name="id" value="${host.id}"/>                                
                                            <input type="image" src="../../images/zoom-icon.png" alt="Show" title="Show"/>
                                        </form>
                                    </td>
                                    <td>
                                        <form method="get" action="edit/">
                                            <input type="hidden" name="id" value="${host.id}"/>                                
                                            <input type="image" src="../../images/edit-icon.png" alt="Edit" title="Edit"/>
                                        </form>
                                    </td>
                                </tr>
                            </table>

                        </td>
                        <td>${host.id}</td><td>${host.hostname}</td><td>${host.ipv4}</td><td>${host.ipv6}</td>
                    </tr>

                </c:forEach>

            </table>

            <form action="create/" method="GET">               
                <input type="submit" value="Create New Host"/>
            </form>
        </div>


    </body>

</html>
