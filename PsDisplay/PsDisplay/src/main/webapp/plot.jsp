<%-- 
    Document   : plot
    Created on : Jun 26, 2013, 10:22:25 AM
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

        <script src="dygraph-combined.js">  </script>  
        
        <script src="plotHistory.js">  </script>  


        <div id="body"></div>
        <script type="text/javascript">
            //            g = new Dygraph(
            //
            //            // containing div
            //            document.getElementById("body"),
            //           
            //            // CSV or path to a CSV file.
            //            "Date,Temperature\n" +
            //                "2008-05-07,75\n" +
            //                "2008-05-08,70\n" +
            //                "2008-05-09,80\n"
            //             );
            //                 g = new Dygraph(

            // containing div
            //            document.getElementById("body"),
            //           
            //            // CSV or path to a CSV file.
            //            "data.csv",{}
            //             );
            //var dataArray=
//            g = new Dygraph(
//            document.getElementById("body"),
//            [ 
//                [new Date("2013/6/20 20:13:15"),10,100,120], 
//                [new Date("2013/6/21 19:34:23"),20,80,100], 
//                [new Date("2013/6/22 4:5:32"),50,60,80], 
//                [new Date("2013/6/23 16:40:20"),70,80,84] ], 
//            { labels: [ "time", "min", "mean","max" ] }
//        );
           
            
         
                 
        </script>




        <%@ include file="tail.html" %>


    </body>
</html>