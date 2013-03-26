/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    if(id==null){
        getClouds();
    }else{
        getSelectedCloud(id);
    }
}


//
// With XMLHttpRequest Level 2 (implemented in new versions of Firefox, Safari
// and Chrome) you can check progress and check for the "load" event with the
// onload event handler instead of checking the onreadystatechange
//
function getClouds() {
   
    var url = cloudsUrl;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {            
            processListOfCloudsResult(request.responseText);
        }
    };
    request.send(null);
}

function getSelectedCloud(id){
    var url = cloudsUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processSelectedCloud(request.responseText);
        }
    };
    request.send(null);
}

function processSelectedCloud(cloudResponse){
    var cloudJson = JSON.parse(cloudResponse);
    displaySelectedCloud(cloudJson);
}

function displaySelectedCloud(cloudJson){
    fillBodyHeader("Cloud:"+cloudJson.name);
    
    var cloudsTableDiv = document.getElementById("cloudsTableDiv");
    //cloudsTableDiv.innerHTML=JSON.stringify(cloudJson);
    
    var sitesLabelText = document.createTextNode("Sites");
    var sitesLabel = document.createElement("h2");
    sitesLabel.appendChild(sitesLabelText);
    cloudsTableDiv.appendChild(sitesLabel);
    
    listOfSitesJson=cloudJson.sites;
    var sitesInCloud=summaryTableForSitesList(listOfSitesJson)
    cloudsTableDiv.appendChild(sitesInCloud);
    
    
    var matricesLabelText = document.createTextNode("Matrices");
    var matricesLabel = document.createElement("h2");
    matricesLabel.appendChild(matricesLabelText);
    cloudsTableDiv.appendChild(matricesLabel);
    
    var listOfMatrices=cloudJson.matrices;
    
    var tableOfMatrices=getTableOfMatrices(listOfMatrices);
    cloudsTableDiv.appendChild(tableOfMatrices);
    
}

 
function processListOfCloudsResult(responsetext){
    var listOfCloudsJson = JSON.parse(responsetext);
    displayClouds(listOfCloudsJson);
} 
 

function displayClouds(cloudList){
    fillBodyHeader("Known PerfSonar Clouds");
    
    var cloudsTableDiv = document.getElementById("cloudsTableDiv");
    
    var listOfCloudsTable = summaryTableForCloudsList(cloudList);
    
    cloudsTableDiv.appendChild(listOfCloudsTable);
    
 
}






