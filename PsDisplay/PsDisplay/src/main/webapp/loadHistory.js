/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    hoursAgo=getUrlParameterByName("hoursAgo");
    getHistory(id,hoursAgo);
}


//
// With XMLHttpRequest Level 2 (implemented in new versions of Firefox, Safari
// and Chrome) you can check progress and check for the "load" event with the
// onload event handler instead of checking the onreadystatechange
//

function getHistory(id,hoursAgo){
    var url = servicesUrl+id+"/history";
    if(hoursAgo!=null){
        url=url+"?hoursAgo="+hoursAgo;
    }
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            parseHistoryResult(request.responseText);
        }
    };
    request.send(null);
    
}


function parseHistoryResult(historyResult){
    historyJson=JSON.parse(historyResult);
    displayHistory(historyJson);
}

function displayHistory(historyJson){
    
    serviceId = historyJson[0]["service-id"];
    
    fillBodyHeader("History of service id="+serviceId);
    
    
    var objectsDiv =document.getElementById("historyTableDiv");
    
    historyLinkTable = getHistoryLinkTable(serviceId);
    
    objectsDiv.appendChild(historyLinkTable);
    
    table = historyTable(historyJson);
    objectsDiv.appendChild(table);
    
    
//objectsDiv.innerHTML=JSON.stringify(historyJson);
    
//var serviceTable =fullTableForService(serviceJson);
    
//objectsDiv.appendChild(serviceTable);
}

