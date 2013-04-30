/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    
    id = getUrlParameterByName("id");
    
    //first load sites
    xmlhttp=new XMLHttpRequest();
    xmlhttp.open("GET",sitesUrl,false);
    xmlhttp.send();
    sessionStorage.setItem('allSites',xmlhttp.responseText);
    
    //second load matrices
    xmlhttp=new XMLHttpRequest();
    xmlhttp.open("GET",matricesUrl,false);
    xmlhttp.send();
    sessionStorage.setItem('allMatrices',xmlhttp.responseText);
    
    if(id!=null){
        loadSelectedCloud(id);
    }
}
function loadSelectedCloud(id){
    var url = cloudsUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processCloudData(request.responseText);
        }
    };
    request.send(null);
}
function processCloudData(cloudData){
    
    cloud = JSON.parse(cloudData);
    fillEditCloudForm(cloud);    
    fillSitesInCloudTable(cloud);
    fillSitesNotInCloudTable(cloud);
    fillMatricesNotInCloudTable(cloud);
    fillMatricesInCloudTable(cloud);
    
}

function fillEditCloudForm(matrix){
    nameField=document.getElementById("nameField");
    nameField.setAttribute("value", matrix.name);
    
    cloudIdField=document.getElementById("cloudIdField");
    cloudIdField.setAttribute("value", matrix.id);
}
         
function updateMatrixData(){
    
    matrix={};
    var matrixIdField=document.getElementById("matrixIdField");  
    matrix.id=matrixIdField.value;
    
    var nameField = document.getElementById("nameField");  
    matrix.name=nameField.value;
    
    var request = new XMLHttpRequest();
    url=matricesUrl+matrix.id;
    
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processUpdateMatrixResponse(request.responseText);
        }else{
            alert("Matrix update failed");
            alert(request.responseText);
        }
    };
   
    request.send(JSON.stringify(matrix));
}

function goBackToListOfMatricesCrud(){
    window.location=urlToCrudMatrices;
}




//function fillEditMatrixTable(matrix){
//    var editMatrixTableDiv = document.getElementById("editMatrixTableDiv");
//    removeAllChildNodes(editMatrixTableDiv);
//    
//    matrixEditForm = document.createElement("form");
//    var matrixTable = tableForMatrixEdit(matrix);
//    matrixEditForm.appendChild(matrixTable);
//    
//    
//    editMatrixTableDiv.appendChild(matrixEditForm);
//}

function singleCellDeleteHostFromMatrixLink(rowNumber,host,cellUrl){
    var cell=document.createElement("td");
    
    radio = document.createElement("input"); 
    radio.setAttribute("type", "radio");
    radio.setAttribute("id", "hostIdToRemoveField");
    radio.setAttribute("name", "hostIdToRemoveField");
    radio.setAttribute("value", host.id);
    cell.appendChild(radio);
    
    cellText = rowNumber+":"+host.hostname;
    var cellTextElement=document.createTextNode(cellText);
    var bfNode = document.createElement("strong");
    bfNode.appendChild(cellTextElement);
    
    var cellLinkElement=document.createElement("a");
    cellLinkElement.appendChild(bfNode);
    cellLinkElement.setAttribute("href", cellUrl);
    cell.appendChild(cellLinkElement);
    return cell;
    
}



function processRemoveSelectedHostsFromMatrixData(matrixId,responseText){
    loadSelectedMatrix(matrixId);
}
function processAddSelectedHostsToMatrixData(matrixId,responseText){
    loadSelectedMatrix(matrixId);
}

function removeHostFromMatrixAndReDisplay(){
    // which host has been selected?
    listOfHostsIdsToBeRemoved = [];
    listOfRadioButtons = document.getElementsByName("hostIdToRemoveField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfHostsIdsToBeRemoved.push(currentButton.value);
        }
    }
    
    
    if(listOfHostsIdsToBeRemoved.length>0){
    
        matrix={};
        var matrixIdField=document.getElementById("matrixIdField");  
        matrix.id=matrixIdField.value;
    
        var url = matricesUrl+ matrix.id+ "/" + MATRIX_REMOVE_HOST_IDS;
        var request = new XMLHttpRequest();
        request.open("PUT", url);
        request.onload = function() {
            
            if (request.status == 200) {
                processRemoveSelectedHostsFromMatrixData(matrix.id, request.responseText);
            }
        };
    
        request.send(JSON.stringify(listOfHostsIdsToBeRemoved));
    }
}


function fillSitesNotInCloudTable(cloud){
    sitesNotInCloudTable = document.getElementById("sitesNotInCloudTable");
    removeAllChildNodes(sitesNotInCloudTable);
    tbody = document.createElement("tbody");
    sitesNotInCloudTable.appendChild(tbody);
    
    headerRow = document.createElement("tr");
    tbody.appendChild(headerRow);
    
    headerCell = document.createElement("th");
    headerRow.appendChild(headerCell);
    headerCell.appendChild(document.createTextNode("Site"));
    
    listOfSitesNotInCloud = getListOfSitesNotInCloud(cloud);
    
    for(i=0;i<listOfSitesNotInCloud.length;i=i+1){
        site = listOfSitesNotInCloud[i];
        newRow = document.createElement("tr");
        tbody.appendChild(newRow);
        
        newCell = document.createElement("td");
        newRow.appendChild(newCell);
        
        
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "siteIdToAddField");
        radio.setAttribute("name", "siteIdToAddField");
        radio.setAttribute("value", site.id);
        newCell.appendChild(radio);
        
        
        newCell.appendChild(document.createTextNode(site.name));
    }
    
}

function fillSitesInCloudTable(cloud){
    sitesInCloudTable = document.getElementById("sitesInCloudTable");
    removeAllChildNodes(sitesInCloudTable);
    tbody = document.createElement("tbody");
    sitesInCloudTable.appendChild(tbody);
    
    headerRow = document.createElement("tr");
    tbody.appendChild(headerRow);
    
    headerCell = document.createElement("th");
    headerRow.appendChild(headerCell);
    headerCell.appendChild(document.createTextNode("Site"));
    
    listOfSitesInCloud = cloud.sites;
    
    for(i=0;i<listOfSitesInCloud.length;i=i+1){
        site = listOfSitesInCloud[i];
        newRow = document.createElement("tr");
        tbody.appendChild(newRow);
        
        newCell = document.createElement("td");
        newRow.appendChild(newCell);
        
        
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "siteIdToRemoveField");
        radio.setAttribute("name", "siteIdToRemoveField");
        radio.setAttribute("value", site.id);
        newCell.appendChild(radio);
        
        
        newCell.appendChild(document.createTextNode(site.name));
    }
}


function addSelectedSiteToCloud(){
    
    cloud={};
    var cloudIdField=document.getElementById("cloudIdField");  
    cloud.id=cloudIdField.value;
    
    var url = cloudsUrl+ cloud.id+ "/" + CLOUD_ADD_SITE_IDS;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processAddSelectedSitesToCloudData(cloud.id, request.responseText);
        }
    };
    
    
    // which site has been selected?
    listOfSiteIdsToBeAdded = [];
    listOfRadioButtons = document.getElementsByName("siteIdToAddField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfSiteIdsToBeAdded.push(currentButton.value);
        }
    }
    
    request.send(JSON.stringify(listOfSiteIdsToBeAdded));
}

function processAddSelectedSitesToCloudData(cloudId,responseText){
    loadSelectedCloud(cloudId);
}


function removeSelectedSiteFromCloud(){
    // which host has been selected?
    listOfSiteIdsToBeRemoved = [];
    listOfRadioButtons = document.getElementsByName("siteIdToRemoveField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfSiteIdsToBeRemoved.push(currentButton.value);
        }
    }
    
    
    if(listOfSiteIdsToBeRemoved.length>0){
    
        cloud={};
        var cloudIdField=document.getElementById("cloudIdField");  
        cloud.id=cloudIdField.value;
    
        var url = cloudsUrl+ cloud.id+ "/" + CLOUD_REMOVE_SITE_IDS;
        var request = new XMLHttpRequest();
        request.open("PUT", url);
        request.onload = function() {
            
            if (request.status == 200) {
                processRemoveSelectedSitesFromCloudData(cloud.id, request.responseText);
            }
        };
    
        request.send(JSON.stringify(listOfSiteIdsToBeRemoved));
    }
}

function processRemoveSelectedSitesFromCloudData(cloudId,responseText){
    loadSelectedCloud(cloudId);
}


function fillMatricesNotInCloudTable(cloud){
    matricesNotInCloudTable = document.getElementById("matricesNotInCloudTable");
    removeAllChildNodes(matricesNotInCloudTable);
    tbody = document.createElement("tbody");
    matricesNotInCloudTable.appendChild(tbody);
    
    headerRow = document.createElement("tr");
    tbody.appendChild(headerRow);
    
    headerCell = document.createElement("th");
    headerRow.appendChild(headerCell);
    headerCell.appendChild(document.createTextNode("Matrix"));
    
    listOfMatricesNotInCloud = getListOfMatricesNotInCloud(cloud);
    
    for(i=0;i<listOfMatricesNotInCloud.length;i=i+1){
        matrix = listOfMatricesNotInCloud[i];
        newRow = document.createElement("tr");
        tbody.appendChild(newRow);
        
        newCell = document.createElement("td");
        newRow.appendChild(newCell);
        
        
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "matrixIdToAddField");
        radio.setAttribute("name", "matrixIdToAddField");
        radio.setAttribute("value", matrix.id);
        newCell.appendChild(radio);
        
        
        newCell.appendChild(document.createTextNode(matrix.name));
    }
    
}


function fillMatricesInCloudTable(cloud){
    matricesInCloudTable = document.getElementById("matricesInCloudTable");
    removeAllChildNodes(matricesInCloudTable);
    tbody = document.createElement("tbody");
    matricesInCloudTable.appendChild(tbody);
    
    headerRow = document.createElement("tr");
    tbody.appendChild(headerRow);
    
    headerCell = document.createElement("th");
    headerRow.appendChild(headerCell);
    headerCell.appendChild(document.createTextNode("Matrix"));
    
    listOfMatricesInCloud = cloud.matrices;
    
    for(i=0;i<listOfMatricesInCloud.length;i=i+1){
        matrix = listOfMatricesInCloud[i];
        newRow = document.createElement("tr");
        tbody.appendChild(newRow);
        
        newCell = document.createElement("td");
        newRow.appendChild(newCell);
        
        
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "matrixIdToRemoveField");
        radio.setAttribute("name", "matrixIdToRemoveField");
        radio.setAttribute("value", matrix.id);
        newCell.appendChild(radio);
        
        
        newCell.appendChild(document.createTextNode(matrix.name));
    }
    
}


function addSelectedMatrixToCloud(){
    
    cloud={};
    var cloudIdField=document.getElementById("cloudIdField");  
    cloud.id=cloudIdField.value;
    
    var url = cloudsUrl+ cloud.id+ "/" + CLOUD_ADD_MATRIX_IDS;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processAddSelectedMatrixToCloudData(cloud.id, request.responseText);
        }
    };
    
    
    // which site has been selected?
    listOfMatrixIdsToBeAdded = [];
    listOfRadioButtons = document.getElementsByName("matrixIdToAddField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfMatrixIdsToBeAdded.push(currentButton.value);
        }
    }
    
    request.send(JSON.stringify(listOfMatrixIdsToBeAdded));
}

function processAddSelectedMatrixToCloudData(cloudId,responseText){
    loadSelectedCloud(cloudId);
}


function removeSelectedMatrixFromCloud(){
    listOfMatrixIdsToBeRemoved = [];
    listOfRadioButtons = document.getElementsByName("matrixIdToRemoveField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfMatrixIdsToBeRemoved.push(currentButton.value);
        }
    }
    
    
    if(listOfMatrixIdsToBeRemoved.length>0){
    
        cloud={};
        var cloudIdField=document.getElementById("cloudIdField");  
        cloud.id=cloudIdField.value;
    
        var url = cloudsUrl+ cloud.id+ "/" + CLOUD_REMOVE_MATRIX_IDS;
        var request = new XMLHttpRequest();
        request.open("PUT", url);
        request.onload = function() {
            
            if (request.status == 200) {
                processRemoveSelectedMatrixFromCloudData(cloud.id, request.responseText);
            }
        };
    
        request.send(JSON.stringify(listOfMatrixIdsToBeRemoved));
    }
}


function processRemoveSelectedMatrixFromCloudData(cloudId,responseText){
    loadSelectedCloud(cloudId);
}