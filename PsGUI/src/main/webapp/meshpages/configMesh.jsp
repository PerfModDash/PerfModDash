<%-- 
    Document   : configMesh
    Created on : Jan 7, 2013, 5:13:13 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>mesh configuration</title>
    </head>
    
    <script type="text/javascript">
     function onButton(){
         
     	if(MeshFileURLForm.fileURL.value == ""){
     	
     		alert("Please input the mesh file URL!");
     		MeshFileURLForm.fileURL.focus();
     		return false;
     	}
     }
    </script>
    
    <body>
        <h1>Mesh Configuration</h1>
        
        <p>
            Please chose any of the following two methods to provide mesh configuration information.    
        </p>
        1. Upload file
        <s:form action="configMesh" namespace="/" method="POST" enctype="multipart/form-data">
            <table border="1"  align="center">
             <tbody>
                <tr>
                    <td>
                       <s:file name="fileUpload" label="Select a mesh file to upload" size="40" theme="simple"/>
                    </td>
                    <td>
                        <s:submit lable="submit" name="submit" theme="simple"/>
                    </td>
                </tr>
                
             </tbody>
             </table>
 
        </s:form>
        
        2. File URL
        <s:form name="MeshFileURLForm" action="configMesh" method="post" onsubmit="return onButton()" theme="simple">
             <table border="1"  align="center">
             <tbody>
                <tr>
                    <td>
                        <s:textfield key="fileURL" />
                    </td>
                    <td>
                        <s:submit lable="submit" name="submit" theme="simple"/>
                    </td>
                </tr>
                
             </tbody>
             </table>
                           
         </s:form>      
    </body>
</html>
