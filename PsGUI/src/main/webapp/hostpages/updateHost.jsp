<%-- 
    Document   : updateHost
    Created on : Dec 6, 2012, 11:29:35 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>edit host</title>
    </head>
    
    <script type="text/javascript">
     function onButton(){
     	if(UpdateHostForm.hostname.value == ""){
     	
     		alert("Please input the host name!");
     		UpdateHostForm.hostname.focus();
     		return false;
     	}
     }
    </script>
    
    <body>
        <div align="center"><h1>Host edit page:</h1><br>
         <s:form name="UpdateHostForm" action="updateHost" method="post" onsubmit="return onButton()">
             <s:hidden name="hostid" value="%{hostid}"></s:hidden>
             <table border="1"  align="center">
             <tbody>
                 
                <tr>
                    <s:textfield key="hostname" />
                </tr>
                <tr>
                    <s:textfield key="ipv4" />
                </tr>
                <tr>
                    <s:textfield key="ipv6" />
                </tr>
                
             </tbody>
             </table>
             <br>
             <div align="center">
                 <s:submit lable="submit" name="submit" theme="simple"/>
                 <s:reset lable="reset" name="reset" theme="simple"/>
             </div>
              
                           
         </s:form>      
           
        </div>
    </body>
</html>
