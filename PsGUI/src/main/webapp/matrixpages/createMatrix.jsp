<%-- 
    Document   : createMatrix
    Created on : Dec 16, 2012, 8:01:15 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create new matrix</title>
    </head>
    
    <script type="text/javascript">
     function onButton(){
         
     	if(CreateMatrixForm.matrixName.value == ""){
     	
     		alert("Please input the matrix name!");
     		CreateMatrixForm.matrixName.focus();
     		return false;
     	}
     }
    </script>
    
    <body>
        <div align="center">
         <h2>Create New <s:property value="serviceTypeId" /> Matrix</h2><br>
         <s:form name="CreateMatrixForm" action="createMatrix" method="post" onsubmit="return onButton()">
             <s:hidden name="serviceTypeId" value="%{serviceTypeId}"></s:hidden>
             <table border="1"  align="center">
             <tbody>
                <tr>
                    <s:textfield key="matrixName" />
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
