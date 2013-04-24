/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    
    if(id!=null){
        loadSelectedHost(id);
    }
}
   
function loadSelectedHost(id){
    var url = hostsUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processHostData(request.responseText);
        }
    };
    request.send(null);
}

function processHostData(hostData){
    host = JSON.parse(hostData);
    fillEditHostForm(host);
    fillHostServicesForm(host);
    
}

function fillHostServicesForm(host){
    fillServicesOnHostForm(host);
    fillServicesNotOnHostForm(host);
}

function fillServicesOnHostForm(host){
    servicesOnHostTable = document.getElementById("servicesOnHostTable");
    
    removeAllChildNodes(servicesOnHostTable);
    
    for(i=0;i<host.services.length;i=i+1){
        serviceType = host.services[i].type;
        row = document.createElement("tr");
        servicesOnHostTable.appendChild(row);
        
        cell0 = document.createElement("td");
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "serviceTypeToRemoveField");
        radio.setAttribute("name", "serviceTypeToRemoveField");
        radio.setAttribute("value", serviceType);
        cell0.appendChild(radio);
        row.appendChild(cell0);
        
        cell0.appendChild(document.createTextNode(serviceType));
    }
}

function fillServicesNotOnHostForm(host){
    
    listOfServices = listOfPrimitiveServicesNotOnHost(host);
   
    
    servicesNotOnHostTable = document.getElementById("servicesNotOnHostTable");
    
    removeAllChildNodes(servicesNotOnHostTable);
    
    for(i=0;i<listOfServices.length;i=i+1){
        serviceType = listOfServices[i].id;
        row = document.createElement("tr");
        servicesNotOnHostTable.appendChild(row);
        
        cell0 = document.createElement("td");
        radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "serviceTypeToAddField");
        radio.setAttribute("name", "serviceTypeToAddField");
        radio.setAttribute("value", serviceType);
                       
        
        cell0.appendChild(radio);
        row.appendChild(cell0);
        
        cell0.appendChild(document.createTextNode(serviceType));
    }
}

function fillEditHostForm(host){
    
    
    hostnameField=document.getElementById("hostnameField");
    hostnameField.setAttribute("value", host.hostname);
    
    ipv4Field=document.getElementById("ipv4Field");
    ipv4Field.setAttribute("value", host.ipv4);
    
    ipv6Field=document.getElementById("ipv6Field");
    ipv6Field.setAttribute("value", host.ipv6);
    
    hostIdField=document.getElementById("hostIdField");
    hostIdField.setAttribute("value", host.id);
    
}

function saveHostData(){
    
    host={};
    var hostIdField=document.getElementById("hostIdField");  
    host.id=hostIdField.value;
    
    var hostnameField = document.getElementById("hostnameField");  
    host.hostname=hostnameField.value;
    
    var hostipv4Field = document.getElementById("ipv4Field");
    host.ipv4=hostipv4Field.value;
    
    var hostipv6Field = document.getElementById("ipv6Field");
    host.ipv6=hostipv6Field.value;
    
    var request = new XMLHttpRequest();
    url=hostsUrl+host.id;
    
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processSaveHostResponse(request.responseText);
        }else{
            alert("Host save failed");
            alert(request.responseText);
        }
    };
    request.send(JSON.stringify(host));
}
function processSaveHostResponse(responseText){
//alert(responseText);
    
}



function cancelHostEdit(){    
    window.location=urlToCrudHosts;
}

function addAllServices(){
    host={};
    var hostIdField=document.getElementById("hostIdField");  
    host.id=hostIdField.value;
    
    var url = hostsUrl+ host.id+ "/" + addAllServicesToHostCommand;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        //alert(request.responseText);
        if (request.status == 200) {
            processAddServicesToHostData(host.id, request.responseText);
        }
    };
    request.send(null);
        
}


function addSelectedServices(){
    host={};
    var hostIdField=document.getElementById("hostIdField");  
    host.id=hostIdField.value;
    
    var url = hostsUrl+ host.id+ "/" + addSelectedServiceTypeCommand;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processAddSelectedServicesToHostData(host.id, request.responseText);
        }
    };
    
    listOfServiceTypesToBeAdded = [];
    listOfRadioButtons = document.getElementsByName("serviceTypeToAddField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfServiceTypesToBeAdded.push(currentButton.value);
        }
    }
    
    request.send(JSON.stringify(listOfServiceTypesToBeAdded));
}


function removeSelectedServices(){
    host={};
    var hostIdField=document.getElementById("hostIdField");  
    host.id=hostIdField.value;
    
    var url = hostsUrl+ host.id+ "/" + removeSelectedServiceTypeCommand;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processRemoveSelectedServicesToHostData(host.id, request.responseText);
        }
    };
    
    listOfServiceTypesToBeRemoved = [];
    listOfRadioButtons = document.getElementsByName("serviceTypeToRemoveField");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            listOfServiceTypesToBeRemoved.push(currentButton.value);
        }
    }
    
    request.send(JSON.stringify(listOfServiceTypesToBeRemoved));
}

function addThroughputServices(){
    host={};
    var hostIdField=document.getElementById("hostIdField");  
    host.id=hostIdField.value;
    
    var url = hostsUrl+ host.id+ "/" + addThroughputServicesToHostCommand;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        if (request.status == 200) {
            processAddThroughputServicesToHostData(host.id, request.responseText);
        }
    };
    request.send(null);
}
function addLatencyServices(){
    host={};
    var hostIdField=document.getElementById("hostIdField");  
    host.id=hostIdField.value;
    
    var url = hostsUrl+ host.id+ "/" + addLatencyServicesToHostCommand;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        //alert(request.responseText);
        if (request.status == 200) {
            processAddLatencyServicesToHostData(host.id, request.responseText);
        }
    };
    request.send(null);
}

function removeAllServices(){
     host={};
    var hostIdField=document.getElementById("hostIdField");  
    host.id=hostIdField.value;
    
    var url = hostsUrl+ host.id+ "/" + removeAllServicesFromHostCommand;
    var request = new XMLHttpRequest();
    request.open("PUT", url);
    request.onload = function() {
        //alert(request.responseText);
        if (request.status == 200) {
            processRemoveServicesFromHostData(host.id, request.responseText);
        }
    };
    request.send(null);
        
}

function processRemoveSelectedServicesToHostData(hostId,responseText){
  
    loadSelectedHost(hostId);
}
function processAddSelectedServicesToHostData(hostId,responseText){
    loadSelectedHost(hostId);
}
function processAddThroughputServicesToHostData(hostId,responseText){
    loadSelectedHost(hostId);
}
function processAddLatencyServicesToHostData(hostId,responseText){
    loadSelectedHost(hostId);
}

function processRemoveServicesFromHostData(hostId,responseText){
    loadSelectedHost(hostId);
}

function processAddServicesToHostData(hostId,responseText){
    loadSelectedHost(hostId);
}

function myFunction(){
    var authorField=document.getElementById("authorField");                
    var author=authorField.value;
    var subject=document.getElementById("subjectField").value;
    var content=document.getElementById("contentField").value;
                
    alert(author+" "+subject+" "+content);
                
    var oMyForm = new FormData();
    oMyForm.append("author", author);
    oMyForm.append("subject", subject);
    oMyForm.append("content", content);
                
    var url = baseUrl+createAnnouncementUrl;
    alert(url);
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processCreateAnnouncementResponse(request.responseText);
        }else{
            alert("FAIL");
            alert(request.responseText);
        }
    };
    alert("sending form");
    request.send(oMyForm);
    alert("form sent");
                
                
}