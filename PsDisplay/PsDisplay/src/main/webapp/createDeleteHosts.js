/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    
    fillBodyHeader("Create or delete PerfSonar Hosts");
    
   
    getListOfHosts();
}


//
// With XMLHttpRequest Level 2 (implemented in new versions of Firefox, Safari
// and Chrome) you can check progress and check for the "load" event with the
// onload event handler instead of checking the onreadystatechange
//


function deleteSelectedHostAndRefreshDisplay(id){
    var url = hostsUrl+id;
    alert("url="+url);   
    var request = new XMLHttpRequest();
    request.open("DELETE", url);
    request.onload = function() {
        alert(request.responseText);
        if (request.status == 200) {
            getListOfHosts();
        }
    };
    request.send(null);
}


//function createNewHostAndRefreshDisplay(){
//    var url = hostsUrl;
//    alert("url="+url);   
//    var request = new XMLHttpRequest();
//    request.open("POST", url);
//    request.onload = function() {
//        alert(request.responseText);
//        if (request.status == 200) {
//            getListOfHosts();
//        }
//    };
//    request.send(null);
//}


function getListOfHosts() {
         
    var url = hostsUrl;
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processListOfHosts(request.responseText);
        }
    };
    request.send(null);
}
 
function processListOfHosts(responseText){
    sessionStorage.setItem('hosts',responseText);
  
    listOfHosts = JSON.parse(sessionStorage.getItem('hosts'));
    
   
    displayHostsCrud(listOfHosts);
} 
 
function displayHostsCrudSortedUp(){
    
    sessionStorage.setItem('hostSortingOrder',"up");
    
    displayHostsCrudTable(listOfHosts);
} 

function displayHostsCrudSortedDown(){
      
    sessionStorage.setItem('hostSortingOrder',"down");
    
    displayHostsCrudTable(listOfHosts);
} 
 

function displayHostsCrudTable(objectList){
    var objectsTableDiv = document.getElementById("hostsTableDiv");
    // clear content of the division
    if ( objectsTableDiv.hasChildNodes() )
    {
        while ( objectsTableDiv.childNodes.length >= 1 )
        {
            objectsTableDiv.removeChild( objectsTableDiv.firstChild );       
        } 
    }

    hostSortingOrder = sessionStorage.getItem('hostSortingOrder');
    
    if(hostSortingOrder=="up"){
        listOfHosts.sort(function(a, b)
        {
            nA=a.hostname;
            nB=b.hostname;
            return compareHostNames(nA,nB);

        });
    }
    if(hostSortingOrder=="down"){
        listOfHosts.sort(function(a, b)
        {
            nA=a.hostname;
            nB=b.hostname;
        
            return compareHostNames(nA,nB);

        }); 
        listOfHosts=invertJsonArray(listOfHosts);
    }

    var objectsTable = listOfHostsCrudTable(listOfHosts);
    
    objectsTableDiv.appendChild(objectsTable);
}

function displayHostsCrud(objectList){
    displayHostsCrudTable(objectList);
}

function createNewHost(){
    var url = hostsUrl;
    alert("url="+url);   
    var request = new XMLHttpRequest();
    request.open("POST", url);
    request.onload = function() {
        alert(request.responseText);
        if (request.status == 200) {
            getListOfHosts();
        }
    };
    request.send(null);
}


function deleteSelectedHost(){
    
    host={};
    
    listOfRadioButtons = document.getElementsByName("id");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            host.id=currentButton.value;
            break;
        }
    }
            
    var request = new XMLHttpRequest();
    url=hostsUrl+host.id;
    
    request.open("DELETE", url);
    request.onload = function() {
        if (request.status == 200) {
            processDeleteHostResponse(request.responseText);
        }else{
            alert("Host delete failed");
            alert(request.responseText);
        }
    };
    request.send(null);
}

function processDeleteHostResponse(responseText){
    getListOfHosts();
}