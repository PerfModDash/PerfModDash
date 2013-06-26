/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    if(id==null){
        getServices();
    }else{
        getSelectedService(id);
    }
}


//
// With XMLHttpRequest Level 2 (implemented in new versions of Firefox, Safari
// and Chrome) you can check progress and check for the "load" event with the
// onload event handler instead of checking the onreadystatechange
//

function getSelectedService(id){
    var url = servicesUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            parseSelectedServiceResult(request.responseText);
        }
    };
    request.send(null);
    
}


function parseSelectedServiceResult(serviceResult){
    serviceJson=JSON.parse(serviceResult);
    displaySelectedService(serviceJson);
}

function displaySelectedService(serviceJson){
    
    fillBodyHeader("Details of Service "+serviceJson.name);
    
    var id=serviceJson.id
    var objectsDiv =document.getElementById("servicesTableDiv");
    
    var serviceTable =fullTableForService(serviceJson);
    
    objectsDiv.appendChild(serviceTable);
    
    
    
        
}

function fullTableForService(objectJson){
    
    
    
    var id=objectJson.id
    var objectsDiv = document.getElementById("servicesTableDiv");
        
    var objectTable = document.createElement("table");
    objectTable.setAttribute("id", "objects");
    objectsDiv.appendChild(objectTable);
    
    var objectTblBody = document.createElement("tbody");
    objectTable.appendChild(objectTblBody);
    
    //  --- header row ---  //

    objectTblBody.appendChild(twoCellHeaderRow("Name",objectJson.name));
    
    // --- rows ---  //
    
    objectTblBody.appendChild(twoCellRow("Description",objectJson.description));
    objectTblBody.appendChild(twoCellRow("Type",objectJson.type));
    objectTblBody.appendChild(twoCellRow("Running",objectJson.running));
    objectTblBody.appendChild(twoCellRow("Running Since",objectJson.runningSince));
    objectTblBody.appendChild(twoCellRow("Last Check Time",objectJson.lastCheckTime));
    objectTblBody.appendChild(twoCellRow("Next Check Time",objectJson.nextCheckTime));
    objectTblBody.appendChild(twoCellRow("Check Interval",objectJson.checkInterval));
    objectTblBody.appendChild(twoCellRow("Timeout",objectJson.timeout));
    if(objectJson.hasOwnProperty("result")){
        objectTblBody.appendChild(twoCellRow("Message",objectJson.result.message));
        objectTblBody.appendChild(twoCellRow("Time",objectJson.result.time));
        objectTblBody.appendChild(twoCellStatusRow("Status",objectJson.result.status));
        if(objectJson.result.hasOwnProperty("parameters")){
            if(objectJson.result.parameters.hasOwnProperty("command")){
                objectTblBody.appendChild(twoCellRow("Command",objectJson.result.parameters.command));
            }
        }
    }
    if(objectJson.hasOwnProperty("parameters")){
        for (var key in objectJson.parameters) {
            if (objectJson.parameters.hasOwnProperty(key)) {
                objectTblBody.appendChild(twoCellRow(key,objectJson.parameters[key]));
            }
        }       
    }
    
    var historyRow = document.createElement("tr");
    objectTblBody.appendChild(historyRow);
    historyRow.appendChild(singleCelltext("History")); 
    historyRow.appendChild(cellWithHistoryLinks(objectJson.id));
    
    var plotRow = document.createElement("tr");
    objectTblBody.appendChild(plotRow);
    plotRow.appendChild(singleCelltext("Plot")); 
    plotRow.appendChild(cellWithPlotLinks(objectJson.id));
    
}



function getServices() {
   
    var url = servicesUrl;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            displayServices(request.responseText);
        }
    };
    request.send(null);
}
 

function displayServices(responseText){
    
   
    
    var listOfServices=JSON.parse(responseText)
    
    fillBodyHeader("Known PerfSonar Services");
   
    
    var objectsTableDiv = document.getElementById("servicesTableDiv");
    
    var objectsTable = summaryTableForServicesList(listOfServices);
    
        
    objectsTableDiv.appendChild(objectsTable);
    

}


