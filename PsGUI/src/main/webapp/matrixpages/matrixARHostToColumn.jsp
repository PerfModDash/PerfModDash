<%-- 
    Document   : matrixARHostToColumn
    Created on : Dec 17, 2012, 1:14:03 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add/remove hosts to/from matrix column page</title>
    </head>
    
    <script type="text/javascript">
    function onRemoveSelectedHostsFromColumnButton(){
 
  	var hosts_to_remove_column = new Array();
  	var flag = false;
  	hosts_to_remove_column = document.getElementsByName("hosts_to_remove_column");
 
	for(var i=0 ; i<hosts_to_remove_column.length ; i++){
  		if(hosts_to_remove_column[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select hosts to remove from column before click this button! ");
  	return false;
    }
  }
  
  function onAddSelectedHostsToColumnButton(){

  	var hosts_to_add_column = new Array();
  	var flag = false;
  	hosts_to_add_column = document.getElementsByName("hosts_to_add_column");
 
	for(var i=0 ; i<hosts_to_add_column.length ; i++){
  		if(hosts_to_add_column[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select hosts to add to column before click this button! ");
  		return false;
    }
  }
  </script>
  
  
    <body>
        <div align="center">
        <h2>Add or Remove Hosts in Matrix Column </h2>
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">hosts in matrix column</th>
            <th align="center">hosts not in matrix column</th>
        </tr>
        <tr>
            <td align="left">
                <s:form name="RemoveSelectedHostsFromColumnForm" action="matrixARHostToColumn" method="post" onsubmit="onRemoveSelectedHostsFromColumnButton()">
                    <s:hidden name="matrixId" value="%{matrixId}"></s:hidden>
                    <s:if test="%{hosts_in_matrix_column.isEmpty}">      
                    </s:if>
                    <s:else>
                     
                        <s:checkboxlist list="hosts_in_matrix_column" name="hosts_to_remove_column" theme="vertical-checkbox"/>
                           
                    </s:else>
                    <br>
                    <s:submit value="remove selected hosts from column" name="button" theme="simple"/>   
                </s:form>
            </td>
            <td align="left">
                <s:form name="AddSelectedHostsToColumnForm" action="matrixARHostToColumn" method="post" onsubmit="onAddSelectedHostsToColumnButton()">
                    <s:hidden name="matrixId" value="%{matrixId}"></s:hidden>
                    <s:if test="%{hosts_not_in_matrix_column.isEmpty()}">
                    </s:if>
                    <s:else>
                        <s:checkboxlist list="hosts_not_in_matrix_column" name="hosts_to_add_column" theme="vertical-checkbox"/> 
                    </s:else>
                         
                    <br>
                    <s:submit value="add selected hosts to column" name="button" theme="simple" />   
                </s:form>
                
            </td>
        </tr>
        
        </tbody>
        </table>
        <br>
        <br>
        <s:url action="queryMatrixList" var="aURL" />
        <s:a href="%{aURL}">return to the list of matrix</s:a>
        </div>
    </body>
</html>
