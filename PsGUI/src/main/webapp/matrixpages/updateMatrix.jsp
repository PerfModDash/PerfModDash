<%-- 
    Document   : updateMatrix
    Created on : Dec 17, 2012, 10:05:15 AM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>edit matrix</title>
    </head>
    
    <script type="text/javascript">
     function onButton(){
     	if(UpdateMatrixForm.matrixName.value == ""){
     	
     		alert("Please input the matrix name!");
     		UpdateMatrixForm.matrixName.focus();
     		return false;
     	}
     }
    </script>
    
    <body>
        <div align="center">
         <h2>Edit matrix <s:property value="one_matrix.matrixName" /> </h2><br>
         <s:form name="UpdateMatrixForm" action="updateMatrix" method="post" onsubmit="return onButton()">
             <s:hidden name="matrixId" value="%{matrixId}"></s:hidden>
             <table border="1"  align="center">
             <tbody>
                <tr>
                    <s:textfield label="New name: " name="matrixName" />
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
