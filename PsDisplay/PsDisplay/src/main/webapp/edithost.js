/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    //alert("id="+id);
    if(id!=null){
        loadSelectedHost(id);
    }
}
   
function loadSelectedHost(id){
    var url = hostsUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        //alert(request.responseText);
        if (request.status == 200) {
            processHostData(request.responseText);
        }
    };
    request.send(null);
}

function processHostData(hostData){
    host = JSON.parse(hostData);
    fillEditHostForm(host);
}

function fillEditHostForm(host){
    fillBodyHeader("Edit Host");
    
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