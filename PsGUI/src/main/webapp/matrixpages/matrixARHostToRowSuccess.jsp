<%-- 
    Document   : MatrixARHostSuccess
    Created on : Dec 17, 2012, 1:26:35 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add/remove hosts to/from matrix row success</title>
    </head>
    
    <script type="text/javascript">
    function onRemoveSelectedHostsFromRowButton(){
 
  	var hosts_to_remove_row = new Array();
  	var flag = false;
  	hosts_to_remove_row = document.getElementsByName("hosts_to_remove_row");
 
	for(var i=0 ; i<hosts_to_remove_row.length ; i++){
  		if(hosts_to_remove_row[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select hosts to remove from row before click this button! ");
  	return false;
    }
  }
  
  function onAddSelectedHostsToRowButton(){

  	var hosts_to_add_row = new Array();
  	var flag = false;
  	hosts_to_add_row = document.getElementsByName("hosts_to_add_row");
 
	for(var i=0 ; i<hosts_to_add_row.length ; i++){
  		if(hosts_to_add_row[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select hosts to add to row before click this button! ");
  		return false;
    }
  }
  </script>
  
  
    <body>
        <div align="center">
        <h2>Add or Remove Hosts in Matrix Row </h2>
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">hosts in matrix row</th>
            <th align="center">hosts not in matrix row</th>
        </tr>
        <tr>
            <td align="left">
                <s:form name="RemoveSelectedHostsFromRowForm" action="siteARHost" method="post" onsubmit="onRemoveSelectedHostsFromRowButton()">
                    <s:hidden name="matrixId" value="%{matrixId}"></s:hidden>
                    <s:if test="%{hosts_in_matrix_row.isEmpty()}">      
                    </s:if>
                    <s:else>
                     
                        <s:checkboxlist list="hosts_in_matrix_row" name="hosts_to_remove_row" theme="vertical-checkbox"/>
                           
                    </s:else>
                    <br>
                    <s:submit value="remove selected hosts from row" name="button" theme="simple"/>   
                </s:form>
            </td>
            <td align="left">
                <s:form name="AddSelectedHostsToRowForm" action="siteARHost" method="post" onsubmit="onAddSelectedHostsToRowButton()">
                    <s:hidden name="matrixId" value="%{matrixId}"></s:hidden>
                    <s:if test="%{hosts_not_in_matrix_row.isEmpty()}">
                    </s:if>
                    <s:else>
                        <s:checkboxlist list="hosts_not_in_matrix_row" name="hosts_to_add_row" theme="vertical-checkbox"/> 
                    </s:else>
                         
                    <br>
                    <s:submit value="add selected hosts to row" name="button" theme="simple" />   
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
