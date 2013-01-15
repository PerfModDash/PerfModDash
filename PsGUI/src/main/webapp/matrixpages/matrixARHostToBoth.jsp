<%-- 
    Document   : matrixARHostToBoth
    Created on : Dec 17, 2012, 1:14:19 PM
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
    function onRemoveSelectedHostsFromBothButton(){
 
  	var hosts_to_remove_both = new Array();
  	var flag = false;
  	hosts_to_remove_both = document.getElementsByName("hosts_to_remove_column");
 
	for(var i=0 ; i<hosts_to_remove_both.length ; i++){
  		if(hosts_to_remove_both[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select hosts to remove from matrix before click this button! ");
  	return false;
    }
  }
  
  function onAddSelectedHostsToBothButton(){

  	var hosts_to_add_both = new Array();
  	var flag = false;
  	hosts_to_add_both = document.getElementsByName("hosts_to_add_both");
 
	for(var i=0 ; i<hosts_to_add_both.length ; i++){
  		if(hosts_to_add_both[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select hosts to add to matrix before click this button! ");
  		return false;
    }
  }
  </script>
  
  
    <body>
        <div align="center">
        <h2>Add or Remove Hosts in Matrix</h2>
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">hosts in matrix</th>
            <th align="center">hosts not in matrix</th>
        </tr>
        <tr>
            <td align="left">
                <s:form name="RemoveSelectedHostsFromBothForm" action="matrixARHostToBoth" method="post" onsubmit="onRemoveSelectedHostsFromBothButton()">
                    <s:hidden name="matrixId" value="%{matrixId}"></s:hidden>
                    <s:if test="%{hosts_in_matrix_both.isEmpty}">      
                    </s:if>
                    <s:else>
                     
                        <s:checkboxlist list="hosts_in_matrix_both" name="hosts_to_remove_both" theme="vertical-checkbox"/>
                           
                    </s:else>
                    <br>
                    <s:submit value="remove selected hosts from matrix" name="button" theme="simple"/>   
                </s:form>
            </td>
            <td align="left">
                <s:form name="AddSelectedHostsToBothForm" action="matrixARHostToBoth" method="post" onsubmit="onAddSelectedHostsToBothButton()">
                    <s:hidden name="matrixId" value="%{matrixId}"></s:hidden>
                    <s:if test="%{hosts_not_in_matrix_both.isEmpty()}">
                    </s:if>
                    <s:else>
                        <s:checkboxlist list="hosts_not_in_matrix_both" name="hosts_to_add_both" theme="vertical-checkbox"/> 
                    </s:else>
                         
                    <br>
                    <s:submit value="add selected hosts to matrix" name="button" theme="simple" />   
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

