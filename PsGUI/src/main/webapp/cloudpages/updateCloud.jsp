<%-- 
    Document   : updateCloud
    Created on : Dec 13, 2012, 11:43:43 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>edit cloud</title>
    </head>
    
    <script type="text/javascript">
     function onButton(){
     	if(UpdateCloudForm.cloudname.value == ""){
     	
     		alert("Please input the cloud name!");
     		UpdateCloudForm.cloudname.focus();
     		return false;
     	}
     }
    </script>
    
    <body>
        <div align="center">
         <h2>Edit cloud: <s:property value="one_cloud.cloudname" /> </h2><br>
         <s:form name="UpdateCloudForm" action="updateCloud" method="post" onsubmit="return onButton()">
             <s:hidden name="cloudid" value="%{cloudid}"></s:hidden>
             <table border="1"  align="center">
             <tbody>
                <tr>
                    <s:textfield label="New name: " name="cloudname" />
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
