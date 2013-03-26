/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    if(id==null){
        getSites();
    }else{
        getSelectedSite(id);
   
    }
}


//
// With XMLHttpRequest Level 2 (implemented in new versions of Firefox, Safari
// and Chrome) you can check progress and check for the "load" event with the
// onload event handler instead of checking the onreadystatechange
//

function getSelectedSite(id){
    var url = sitesUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            displaySelectedSite(request.responseText);
        }
    };
    request.send(null);
}

function getSites() {
   
    var url = sitesUrl;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            displaySites(request.responseText);
        }
    };
    request.send(null);
}
 
function displaySelectedSite(responseText){
    siteJson=JSON.parse(responseText);
     
    fillBodyHeader("Details for Site "+siteJson.name);
     
    var sitesTableDiv = document.getElementById("sitesTableDiv");
     
    //sitesTableDiv.innerHTML=responseText;
     
    listOfHostsInSite = siteJson.hosts;
     
    for (var i = 0;
        i < listOfHostsInSite.length;
        i++) {
        var host = listOfHostsInSite[i]; 
        var textElement = document.createTextNode(host.hostname); 
        var hostNameElement = document.createElement("h3");
        hostNameElement.style.color="black";
        
        hostNameElement.appendChild(textElement);
        var linkElement = document.createElement("a");
        linkElement.appendChild(hostNameElement);
        linkElement.setAttribute("href", urlToDisplayHosts+"?id="+host.id);
        sitesTableDiv.appendChild(linkElement);
        
        listOfServices=host.services;
        servicesTable = boxedTableForServicesList(listOfServices);
        sitesTableDiv.appendChild(servicesTable);
     
    }
}
 

function displaySites(responseText){
    
    fillBodyHeader("Known PerfSonar Sites");
    
    var listOfSites = JSON.parse(responseText);
    
    var sitesTableDiv = document.getElementById("sitesTableDiv");
    
    objectsTable = summaryTableForSitesList(listOfSites);
    
    sitesTableDiv.appendChild(objectsTable);
}