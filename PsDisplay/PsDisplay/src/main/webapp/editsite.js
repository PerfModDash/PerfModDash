/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    
    //first load hosts
    xmlhttp=new XMLHttpRequest();
    xmlhttp.open("GET",hostsUrl,false);
    xmlhttp.send();
    sessionStorage.setItem('allHosts',xmlhttp.responseText);
    
    id = getUrlParameterByName("id");
    
    if(id!=null){
        loadSelectedSite(id);
    }
}

function loadSelectedSite(id){
    var url = sitesUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processSiteData(request.responseText);
        }
    };
    request.send(null);
}

function processSiteData(siteData){
    site = JSON.parse(siteData);
    fillEditSiteForm(site);
    fillSiteHostForm(site);
    
}

function fillSiteHostForm(site){
    fillHostsOnSiteForm(site);
    fillHostsNotOnSiteForm(site);
}
function fillHostsOnSiteForm(site){
    hostsInSiteTable = document.getElementById("hostsInSiteTable");
    
    removeAllChildNodes(hostsInSiteTable);
    
    for(i=0;i<site.hosts.length;i=i+1){
        host=site.hosts[i];
        
        row = document.createElement("tr");
        hostsInSiteTable.appendChild(row);
        
        cell0 = document.createElement("td");
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "hostIdToRemoveField");
        radio.setAttribute("name", "hostIdToRemoveField");
        radio.setAttribute("value", host.id);
        cell0.appendChild(radio);
        row.appendChild(cell0);
        
        cell0.appendChild(linkToEditHostPage(host));
    }
}

function fillHostsNotOnSiteForm(site){
    hostsNotInSiteTable = document.getElementById("hostsNotInSiteTable");
    
    removeAllChildNodes(hostsNotInSiteTable);
    
    listOfHosts = getListOfHostsNotInSite(site);
    
    for(i=0;i<listOfHosts.length;i=i+1){
        host=listOfHosts[i];
        
        row = document.createElement("tr");
        hostsNotInSiteTable.appendChild(row);
        
        cell0 = document.createElement("td");
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "hostIdToAddField");
        radio.setAttribute("name", "hostIdToAddField");
        radio.setAttribute("value", host.id);
        cell0.appendChild(radio);
        row.appendChild(cell0);
        
        //cell0.appendChild(document.createTextNode(host.hostname));
        cell0.appendChild(linkToEditHostPage(host));
    }
}

function fillEditSiteForm(site){
    
    nameField=document.getElementById("nameField");
    nameField.setAttribute("value", site.name);
    
    siteIdField=document.getElementById("siteIdField");
    siteIdField.setAttribute("value", site.id);
    
}



function saveSiteData(){
    
    site={};
    var siteIdField=document.getElementById("siteIdField");  
    site.id=siteIdField.value;
    
    var nameField = document.getElementById("nameField");  
    site.name=nameField.value;
    
    var request = new XMLHttpRequest();
    url=sitesUrl+site.id;
    
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processSaveSiteResponse(request.responseText);
        }else{
            alert("Site save failed");
            alert(request.responseText);
        }
    };
    request.send(JSON.stringify(site));
}

function processSaveSiteResponse(responseText){
//alert(responseText);
    
}

function cancelSiteEdit(){    
    window.location=urlToCrudSites;
}


function addSelectedHostsToSite(){
    site={};
    var siteIdField=document.getElementById("siteIdField");  
    site.id=siteIdField.value;
    
    var url = sitesUrl+ site.id+ "/" + addSelectedHostsToSiteCommand;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processAddSelectedHostsToSiteData(site.id, request.responseText);
        }
    };
    
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
function processAddSelectedHostsToSiteData(siteId,responseText){
    loadSelectedSite(siteId);
}

function removeSelectedHostsFromSite(){
    site={};
    var siteIdField=document.getElementById("siteIdField");  
    site.id=siteIdField.value;
    
    var url = sitesUrl+ site.id+ "/" + removeSelectedHostsFromSiteCommand;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processRemoveSelectedHostsFromSiteData(site.id, request.responseText);
        }
    };
    
    listOfHostsToBeRemoved = [];
    listOfRadioButtons = document.getElementsByName("hostIdToRemoveField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfHostsToBeRemoved.push(currentButton.value);
        }
    }
    
    request.send(JSON.stringify(listOfHostsToBeRemoved));
}

function processRemoveSelectedHostsFromSiteData(siteId,responseText){
    loadSelectedSite(siteId);
}