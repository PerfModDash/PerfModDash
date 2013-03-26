/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    if(id==null){
        getMatrices();
    }else{
        getSelectedMatrix(id);
   
    }
}


//
// With XMLHttpRequest Level 2 (implemented in new versions of Firefox, Safari
// and Chrome) you can check progress and check for the "load" event with the
// onload event handler instead of checking the onreadystatechange
//
function getMatrices() {
   
    var url = matricesUrl;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processListOfMatricesResult(request.responseText);
        }
    };
    request.send(null);
}

function getSelectedMatrix(id) {
   
    var url = matricesUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processSelectedMatrixResult(request.responseText);
        }
    };
    request.send(null);
}

function processListOfMatricesResult(matrixData){
    var listOfMatrices=JSON.parse(matrixData);
    displayMatrices(listOfMatrices);
}

function processSelectedMatrixResult(matrixData){
    var matrixJson=JSON.parse(matrixData);
    displaySelectedMatrix(matrixJson);
}

function displaySelectedMatrix(matrixJson){
    fillBodyHeader("Matrix:"+matrixJson.name);
    
    var matricesTableDiv = document.getElementById("matricesTableDiv");
    
    var matrixTable = tableForMatrix(matrixJson);
    matricesTableDiv.appendChild(matrixTable);
    
    //matricesTableDiv.innerHTML=JSON.stringify(matrixJson);
}
 

function displayMatrices(objectList){
    
    fillBodyHeader("Known PerfSonar Matrices");
    
    var matricesTableDiv = document.getElementById("matricesTableDiv");
    
    var objectsTable = document.createElement("table");
    var tblBody = document.createElement("tbody");
    
    var objectsTableHeaderRow = document.createElement("tr");
    var header0 = document.createElement("th");
    var header0text = document.createTextNode("Matrix Name");
    header0.appendChild(header0text);
    objectsTableHeaderRow.appendChild(header0);
    
    tblBody.appendChild(objectsTableHeaderRow);
    
    objectsTable.appendChild(tblBody);
    
    objectsTable.setAttribute("id", "objects");
        
    matricesTableDiv.appendChild(objectsTable);
        
    for (var i = 0; i < objectList.length; i++) {
        var object = objectList[i];
        
        var id=object.id;
        
        objectName="---";
        if (object.name!=null && object.name!=""){
            objectName=object.name;
        }
        var objectUrl=urlToDisplayMatrices+"?id="+id;
        
        var row = singleCellLinkRow(objectName,objectUrl);
      
        tblBody.appendChild(row);
        
    }
}






