<%-- 
    Document   : createCloud
    Created on : Dec 13, 2012, 11:10:02 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create new cloud</title>
    </head>
    
    <script type="text/javascript">
     function onButton(){
         
     	if(CreateCloudForm.cloudname.value == ""){
     	
     		alert("Please input the cloud name!");
     		CreateCloudForm.cloudname.focus();
     		return false;
     	}
     }
    </script>
    
    <body>
        <div align="center">
         <h2>Create New Cloud</h2><br>
         <s:form name="CreateCloudForm" action="createCloud" method="post" onsubmit="return onButton()">
             <table border="1"  align="center">
             <tbody>
                <tr>
                    <s:textfield key="cloudname" />
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

