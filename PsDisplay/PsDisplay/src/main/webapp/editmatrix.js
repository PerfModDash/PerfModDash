/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = init;

function init() {
    
    id = getUrlParameterByName("id");
    
    //first load hosts
    xmlhttp=new XMLHttpRequest();
    xmlhttp.open("GET",hostsUrl,false);
    xmlhttp.send();
    sessionStorage.setItem('allHosts',xmlhttp.responseText);
    
    if(id!=null){
        loadSelectedMatrix(id);
    }
}
function loadSelectedMatrix(id){
    var url = matricesUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processMatrixData(request.responseText);
        }
    };
    request.send(null);
}
function processMatrixData(matrixData){
    matrix = JSON.parse(matrixData);
    fillEditMatrixForm(matrix);
    fillEditMatrixTable(matrix);
    fillHostsNotInMatrixTable(matrix);
    
}

function fillEditMatrixForm(matrix){
    nameField=document.getElementById("nameField");
    nameField.setAttribute("value", matrix.name);
    
    matrixTypeField=document.getElementById("matrixTypeField");
    removeAllChildNodes(matrixTypeField);
    
    matrixTypeField.appendChild(document.createTextNode(matrix.serviceTypeId));
    
    matrixIdField=document.getElementById("matrixIdField");
    matrixIdField.setAttribute("value", matrix.id);
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

function fillHostsNotInMatrixTable(matrix){
    hostsNotInMatrixTable = document.getElementById("hostsNotInMatrixTable");
    removeAllChildNodes(hostsNotInMatrixTable);
    tbody = document.createElement("tbody");
    hostsNotInMatrixTable.appendChild(tbody);
    
    headerRow = document.createElement("tr");
    tbody.appendChild(headerRow);
    
    headerCell = document.createElement("th");
    headerRow.appendChild(headerCell);
    headerCell.appendChild(document.createTextNode("Host Name"));
    
    listOfHostsNotInMatrix = getListOfHostsNotInMatrix(matrix);
    
    for(i=0;i<listOfHostsNotInMatrix.length;i=i+1){
        host = listOfHostsNotInMatrix[i];
        newRow = document.createElement("tr");
        tbody.appendChild(newRow);
        
        newCell = document.createElement("td");
        newRow.appendChild(newCell);
        
        
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "hostIdToAddField");
        radio.setAttribute("name", "hostIdToAddField");
        radio.setAttribute("value", host.id);
        newCell.appendChild(radio);
        
        
        newCell.appendChild(document.createTextNode(host.hostname));
    }
    
}


function fillEditMatrixTable(matrix){
    var editMatrixTableDiv = document.getElementById("editMatrixTableDiv");
    removeAllChildNodes(editMatrixTableDiv);
    
    matrixEditForm = document.createElement("form");
    var matrixTable = tableForMatrixEdit(matrix);
    matrixEditForm.appendChild(matrixTable);
    
    
    editMatrixTableDiv.appendChild(matrixEditForm);
}

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


function addSelectedHostToMatrix(){
    
    matrix={};
    var matrixIdField=document.getElementById("matrixIdField");  
    matrix.id=matrixIdField.value;
    
    var url = matricesUrl+ matrix.id+ "/" + MATRIX_ADD_HOST_IDS;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processAddSelectedHostsToMatrixData(matrix.id, request.responseText);
        }
    };
    
    
    // which host has been selected?
    listOfHostsIdsToBeAdded = [];
    listOfRadioButtons = document.getElementsByName("hostIdToAddField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfHostsIdsToBeAdded.push(currentButton.value);
        }
    }
    
    request.send(JSON.stringify(listOfHostsIdsToBeAdded));
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


