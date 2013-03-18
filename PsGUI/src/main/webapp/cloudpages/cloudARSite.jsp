<%-- 
    Document   : cloudARSite
    Created on : Dec 13, 2012, 3:41:37 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add or remove sites from cloud page</title>
    </head>
    
    <script type="text/javascript">
    function onRemoveSelectedSitesButton(){
 
  	var sites_to_remove = new Array();
  	var flag = false;
  	sites_to_remove = document.getElementsByName("sites_to_remove");
 
	for(var i=0 ; i<sites_to_remove.length ; i++){
            if(sites_to_remove[i].checked){
  		flag = true;
 		break;
            }
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select sites to remove before click this button! ");
  	return false;
    }
  }
  
  function onAddSelectedSitesButton(){

  	var sites_to_add = new Array();
  	var flag = false;
  	sites_to_add = document.getElementsByName("sites_to_add");
 
	for(var i=0 ; i<sites_to_add.length ; i++){
  		if(sites_to_add[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select sites to add before click this button! ");
  	return false;
    }
  }
   </script>
  
  
    <body>
        <div align="center">
        <h2>Add or Remove Sites in Cloud </h2>
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">sites in cloud</th>
            <th align="center">sites not in cloud</th>
        </tr>
        <tr>
            <td align="left">
                <s:form name="RemoveSelectedSitesForm" action="cloudARSite" method="post" onsubmit="onRemoveSelectedSitesButton()">
                    <s:hidden name="cloudid" value="%{cloudid}"></s:hidden>
                    <s:if test="%{sites_in_cloud == null}">      
                    </s:if>
                    <s:else>
                     
                        <s:checkboxlist list="sites_in_cloud" name="sites_to_remove" theme="vertical-checkbox"/>
                           
                    </s:else>
                    <br>
                    <s:submit value="remove selected sites" name="button" theme="simple"/>   
                </s:form>
            </td>
            <td align="left">
                <s:form name="AddSelectedSitesForm" action="cloudARSite" method="post" onsubmit="onAddSelectedSitesButton()">
                    <s:hidden name="cloudid" value="%{cloudid}"></s:hidden>
                    <s:if test="%{sites_not_in_cloud.isEmpty()}">
                    </s:if>
                    <s:else>
                        <s:checkboxlist list="sites_not_in_cloud" name="sites_to_add" theme="vertical-checkbox"/> 
                    </s:else>
                         
                    <br>
                    <s:submit value="add selected sites" name="button" theme="simple" />   
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
