<%-- 
    Document   : createSite
    Created on : Dec 12, 2012, 2:04:31 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create new site</title>
    </head>
    
    <script type="text/javascript">
     function onButton(){
         
     	if(CreateSiteForm.sitename.value == ""){
     	
     		alert("Please input the site name!");
     		CreateSiteForm.sitename.focus();
     		return false;
     	}
     }
    </script>
    
    <body>
        <div align="center">
         <h2>Create New Site</h2><br>
         <s:form name="CreateSiteForm" action="createSite" method="post" onsubmit="return onButton()">
             <table border="1"  align="center">
             <tbody>
                <tr>
                    <s:textfield key="sitename" />
                </tr>
                <tr>
                    <s:textfield key="description" />
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
