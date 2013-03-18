<%-- 
    Document   : cloudARMatrix
    Created on : Feb 28, 2013, 1:22:44 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add or remove matrices from cloud page</title>
    </head>
    
    
<script type="text/javascript">
    function onRemoveSelectedMatricesButton(){
 
  	var matrices_to_remove = new Array();
  	var flag = false;
  	matrices_to_remove = document.getElementsByName("matrices_to_remove");
 
	for(var i=0 ; i<matrices_to_remove.length ; i++){
            if(matrices_to_remove[i].checked){
  		flag = true;
 		break;
            }
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select matrices to remove before click this button! ");
  	return false;
    }
  }
  
  function onAddSelectedMatricesButton(){

  	var matrices_to_add = new Array();
  	var flag = false;
  	matrices_to_add = document.getElementsByName("matrices_to_add");
 
	for(var i=0 ; i<matrices_to_add.length ; i++){
  		if(matrices_to_add[i].checked){
  			flag = true;
  			break;
  		}
        }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select matrices to add before click this button! ");
  	return false;
    }
  }
   </script>
  
  
    <body>
        <div align="center">
        <h2>Add or Remove Matrices in Cloud </h2>
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">matrices in cloud</th>
            <th align="center">matrices not in cloud</th>
        </tr>
        <tr>
            <td align="left">
                <s:form name="RemoveSelectedMatricesForm" action="cloudARMatrix" method="post" onsubmit="onRemoveSelectedMatricesButton()">
                    <s:hidden name="cloudid" value="%{cloudid}"></s:hidden>
                    <s:if test="%{matrices_in_cloud == null}">      
                    </s:if>
                    <s:else>
                     
                        <s:checkboxlist list="matrices_in_cloud" name="matrices_to_remove" theme="vertical-checkbox"/>
                           
                    </s:else>
                    <br>
                    <s:submit value="remove selected matrices" name="button" theme="simple"/>   
                </s:form>
            </td>
            <td align="left">
                <s:form name="AddSelectedMatricesForm" action="cloudARMatrix" method="post" onsubmit="onAddSelectedMatricesButton()">
                    <s:hidden name="cloudid" value="%{cloudid}"></s:hidden>
                    <s:if test="%{matrices_not_in_cloud.isEmpty()}">
                    </s:if>
                    <s:else>
                        <s:checkboxlist list="matrices_not_in_cloud" name="matrices_to_add" theme="vertical-checkbox"/> 
                    </s:else>
                         
                    <br>
                    <s:submit value="add selected matrices" name="button" theme="simple" />   
                </s:form>
                
            </td>
        </tr>
        
        </tbody>
        </table>
        <br>
        <br>
        <s:url action="queryCloudList" var="aURL" />
        <s:a href="%{aURL}">return to the list of clouds</s:a>
        </div>
    </body>
</html>
