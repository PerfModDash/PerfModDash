<%-- 
    Document   : createHost
    Created on : Dec 4, 2012, 7:34:55 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create new host</title>
    </head>
    
     <script type="text/javascript">
     function onButton(){
     	if(CreateHostForm.hostname.value == ""){
     	
     		alert("Please input the host name!");
     		CreateHostForm.hostname.focus();
     		return false;
     	}
     }
    </script>
  
  
    <body>
        <div align="center"><h1>Create New Host</h1><br>
         <s:form name="CreateHostForm" action="createHost" method="post" onsubmit="return onButton()">
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
