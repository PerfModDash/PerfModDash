/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    if(id==null){
        getHosts();
    }else{
        getSelectedHost(id);
    }
}


//
// With XMLHttpRequest Level 2 (implemented in new versions of Firefox, Safari
// and Chrome) you can check progress and check for the "load" event with the
// onload event handler instead of checking the onreadystatechange
//

function getSelectedHost(id){
    var url = hostsUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            parseSelectedHostResult(request.responseText);
        }
    };
    request.send(null);
    
}


function parseSelectedHostResult(hostResult){
    hostJson=JSON.parse(hostResult);
    displaySelectedHost(hostJson);
}

function displaySelectedHost(hostJson){
    
    fillBodyHeader("Details for host "+hostJson.hostname);
    
    var id=hostJson.id
    var objectsDiv =document.getElementById("hostsTableDiv");
    
    var hostTable =fullTableForHost(hostJson);
    
    objectsDiv.appendChild(hostTable);
    
     //  --- services running on this host --- //
    objectsDiv.appendChild(h2header("Services running on this host"));
    
    listOfServices=hostJson.services;
    
    objectsDiv.appendChild(summaryTableForServicesOnHostList(listOfServices));
    
        
}



function getHosts() {
   
    var url = hostsUrl;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            displayHosts(request.responseText);
        }
    };
    request.send(null);
}
 

function displayHosts(responseText){
    
    fillBodyHeader("Known PerfSonar Hosts");
    
    
    var objectsTableDiv = document.getElementById("hostsTableDiv");
    
    var objectsTable = document.createElement("table");
    var tblBody = document.createElement("tbody");
    
    var objectsTableHeaderRow = document.createElement("tr");
    var header0 = document.createElement("th");
            
    var header0text = document.createTextNode("Host Name");
    header0.appendChild(header0text);
    objectsTableHeaderRow.appendChild(header0);
    
    tblBody.appendChild(objectsTableHeaderRow);
    
    objectsTable.appendChild(tblBody);
    
    objectsTable.setAttribute("id", "objects");
        
    objectsTableDiv.appendChild(objectsTable);
    
    var objectList = JSON.parse(responseText);
    for (var i = 0; i < objectList.length; i++) {
        var object = objectList[i];
        
        var row = document.createElement("tr");        
        var cell0 = document.createElement("td");
        objectName="---";
        if (object.hostname!=null){
            objectName=object.hostname;
        }
        //var cell0Text=document.createTextNode(objectName);        
        //cell0.appendChild(cell0Text);
        
        
        id=object.id;
        var cell0Link = document.createElement("a");
        cell0Link.setAttribute("href", urlToDisplayHosts+"?id="+id);
        var cell0Text=document.createTextNode(objectName);  
        cell0Link.appendChild(cell0Text);
        cell0.appendChild(cell0Link);
        
        row.appendChild(cell0);
        
        tblBody.appendChild(row);
        
    }
}

