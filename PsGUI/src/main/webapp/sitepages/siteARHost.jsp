<%-- 
    Document   : siteARHost
    Created on : Dec 12, 2012, 9:00:39 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add or remove hosts in site page</title>
    </head>
    
    <script type="text/javascript">
    function onRemoveSelectedHostsButton(){
 
  	var hosts_to_remove = new Array();
  	var flag = false;
  	hosts_to_remove = document.getElementsByName("hosts_to_remove");
 
	for(var i=0 ; i<hosts_to_remove.length ; i++){
  		if(hosts_to_remove[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select hosts to remove before click this button! ");
  	return false;
    }
  }
  
  function onAddSelectedHostsButton(){

  	var hosts_to_add = new Array();
  	var flag = false;
  	hosts_to_add = document.getElementsByName("hosts_to_add");
 
	for(var i=0 ; i<hosts_to_add.length ; i++){
  		if(hosts_to_add[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select hosts to add before click this button! ");
  		return false;
    }
  }
  </script>
  
  
    <body>
        <div align="center">
        <h2>Add or Remove Hosts in Site </h2>
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">hosts in site</th>
            <th align="center">hosts not in site</th>
        </tr>
        <tr>
            <td align="left">
                <s:form name="RemoveSelectedHostsForm" action="siteARHost" method="post" onsubmit="onRemoveSelectedHostsButton()">
                    <s:hidden name="siteid" value="%{siteid}"></s:hidden>
                    <s:if test="%{hosts_in_site == null}">      
                    </s:if>
                    <s:else>
                     
                        <s:checkboxlist list="hosts_in_site" name="hosts_to_remove" theme="vertical-checkbox"/>
                           
                    </s:else>
                    <br>
                    <s:submit value="remove selected hosts" name="button" theme="simple"/>   
                </s:form>
            </td>
            <td align="left">
                <s:form name="AddSelectedHostsForm" action="siteARHost" method="post" onsubmit="onAddSelectedHostsButton()">
                    <s:hidden name="siteid" value="%{siteid}"></s:hidden>
                    <s:if test="%{hosts_not_in_site.isEmpty()}">
                    </s:if>
                    <s:else>
                        <s:checkboxlist list="hosts_not_in_site" name="hosts_to_add" theme="vertical-checkbox"/> 
                    </s:else>
                         
                    <br>
                    <s:submit value="add selected hosts" name="button" theme="simple" />   
                </s:form>
                
            </td>
        </tr>
        
        </tbody>
        </table>
        <br>
        <br>
        <s:url action="querySiteList" var="aURL" />
        <s:a href="%{aURL}">return to the list of sites</s:a>
        </div>
    </body>
</html>
