/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    if(id==null){
        getHosts();
    }else{
        getSelectedHost(id);
    }
}


//
// With XMLHttpRequest Level 2 (implemented in new versions of Firefox, Safari
// and Chrome) you can check progress and check for the "load" event with the
// onload event handler instead of checking the onreadystatechange
//

function getSelectedHost(id){
    var url = hostsUrl+id;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            parseSelectedHostResult(request.responseText);
        }
    };
    request.send(null);
    
}


function parseSelectedHostResult(hostResult){
    hostJson=JSON.parse(hostResult);
    displaySelectedHost(hostJson);
}

function displaySelectedHost(hostJson){
    
    fillBodyHeader("Details for host "+hostJson.hostname);
    
    var id=hostJson.id
    var objectsDiv =document.getElementById("hostsTableDiv");
    
    var hostTable =fullTableForHost(hostJson);
    
    objectsDiv.appendChild(hostTable);
    
    //  --- services running on this host --- //
    objectsDiv.appendChild(h2header("Services running on this host"));
    
    listOfServices=hostJson.services;
    
    objectsDiv.appendChild(summaryTableForServicesOnHostList(listOfServices));
    
        
}



function getHosts() {
         
    var url = hostsUrl;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processHosts(request.responseText);
        }
    };
    request.send(null);
}
 
function processHosts(responseText){
    //storeHostsInSessionStorage(responseText);
    sessionStorage.setItem('hosts',responseText);
  
    listOfHosts = JSON.parse(sessionStorage.getItem('hosts'));
  
    displayHosts(listOfHosts);
} 
 
function displayHostsSortedUp(){
  
    listOfHosts=JSON.parse( sessionStorage.getItem('hosts'));
    listOfHosts.sort(function(a, b)
    {
        nA=a.hostname;
        nB=b.hostname;
        return compareHostNames(nA,nB);

    });
    displayHostsTable(listOfHosts);
} 

function displayHostsSortedDown(){
    
    listOfHosts=JSON.parse( sessionStorage.getItem('hosts'));
    listOfHosts.sort(function(a, b)
    {
        nA=a.hostname;
        nB=b.hostname;
        
        return compareHostNames(nA,nB);

    });    
    listOfHosts=invertJsonArray(listOfHosts);
    displayHostsTable(listOfHosts);
} 
 

function displayHostsTable(objectList){
    var objectsTable = listOfHostsTable(objectList);
               
    var objectsTableDiv = document.getElementById("hostsTableDiv");
   

    if ( objectsTableDiv.hasChildNodes() )
    {
        while ( objectsTableDiv.childNodes.length >= 1 )
        {
            objectsTableDiv.removeChild( objectsTableDiv.firstChild );       
        } 
    }
    
    objectsTableDiv.appendChild(objectsTable);
}

function displayHosts(objectList){
    
    fillBodyHeader("Known PerfSonar Hosts");
    
    displayHostsTable(objectList);
            
}

