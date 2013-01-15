<%-- 
    Document   : primitiveServARtoHost
    Created on : Dec 10, 2012, 3:49:58 PM
    Author     : siliu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>add or remove primitive services to host page</title>
    </head>
    
    
    <script type="text/javascript">
    function onRemoveSelectedServicesButton(){
 
  	var onHostArray = new Array();
  	var flag = false;
  	onHostArray = document.getElementsByName("services_to_remove");
 
	for(var i=0 ; i<onHostArray.length ; i++){
  		if(onHostArray[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select services to remove before click this button! ");
  		return false;
    }
  }
  
  function onAddSelectedServicesButton(){

  	var notHostArray = new Array();
  	var flag = false;
  	notHostArray = document.getElementsByName("services_to_add");
 
	for(var i=0 ; i<notHostArray.length ; i++){
  		if(notHostArray[i].checked){
  			flag = true;
  			break;
  		}
    }
    
    if(flag == true){
    	return true;
    }else{
    	alert("Please select services to add before click this button! ");
  		return false;
    }
  }
  </script>
    
    
    <body>
        
        <div align="center">
        <h2>Add or Remove Primitive Services to Host </h2>
        <table border="1"  align="center">
        <tbody>
        <tr>
            <th align="center">Primitive Services running on this host</th>
            <th align="center">Primitive Services not running on this host</th>
        </tr>
        <tr>
            <td align="left">
                <s:form name="RemoveSelectedServicesForm" action="primitiveServARtoHost" method="post" onsubmit="onRemoveSelectedServicesButton()">
                    <s:hidden name="hostid" value="%{hostid}"></s:hidden>
                    <s:if test="%{services_on_host.isEmpty()}">      
                    </s:if>
                    <s:else>
                     
                        <s:checkboxlist list="services_on_host" name="services_to_remove" theme="vertical-checkbox"/>
                           
                    </s:else>
                    <br>
                    <s:submit value="remove selected primitive services" name="button" theme="simple"/>   
                </s:form>
            </td>
            <td align="left">
                <s:form name="AddSelectedServicesForm" action="primitiveServARtoHost" method="post" onsubmit="onAddSelectedServicesButton()">
                    <s:hidden name="hostid" value="%{hostid}"></s:hidden>
                    <s:if test="%{services_not_on_host.isEmpty()}">
                    
                    </s:if>
                    <s:else>
                        <s:checkboxlist list="services_not_on_host" name="services_to_add" theme="vertical-checkbox"/> 
                    </s:else>
                         
                    <br>
                    <s:submit value="add selected primitive services" name="button" theme="simple" />   
                </s:form>
                
            </td>
        </tr>
        
        
        </tbody>
        </table>
        </div>
        
        
        <br />
        <br />
        
        
        <br>
        <div align="center">
            <p>Please click the button to add or remove primitive services directly:</p>
        <table border="0"  align="center">
         <tbody>
         <tr>
            <td colspan="2">
                <s:form name="AddThroughputServicesForm" action="primitiveServARtoHost" method="post">
                    <s:hidden name="hostid" value="%{hostid}"></s:hidden>
                    <s:submit value="add all throughput primitive services" name="button" theme="simple"/>   
                </s:form>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <s:form name="AddLatencyServicesForm" action="primitiveServARtoHost" method="post">
                    <s:hidden name="hostid" value="%{hostid}"></s:hidden>
                    <s:submit value="add all latency primitive services" name="button" theme="simple"/>   
                </s:form>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <s:form name="AddAllServicesForm" action="primitiveServARtoHost" method="post">
                    <s:hidden name="hostid" value="%{hostid}"></s:hidden>
                    <s:submit value="add all primitive services" name="button" theme="simple"/>   
                </s:form>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <s:form name="RemoveAllServicesForm" action="primitiveServARtoHost" method="post">
                    <s:hidden name="hostid" value="%{hostid}"></s:hidden>
                    <s:submit value="remove all primitive services" name="button" theme="simple"/>   
                </s:form>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <s:url action="queryHostList" var="aURL" />
                <s:a href="%{aURL}">return to the list of host</s:a>
            </td>
        </tr>
            </tbody>
        </table>
        </div>
    </body>
</html>
