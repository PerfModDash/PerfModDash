/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    operation=getUrlParameterByName("operation");
   
    if(operation=="delete"){
        id = getUrlParameterByName("id");
        if(id!=null){
            deleteSelectedHostAndRefreshDisplay(id);
        }
    }
    if(operation=="create"){
        createNewHostAndRefreshDisplay();
    }
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


function createNewHostAndRefreshDisplay(){
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
  
    listOfHosts=JSON.parse( sessionStorage.getItem('hosts'));
    listOfHosts.sort(function(a, b)
    {
        nA=a.hostname;
        nB=b.hostname;
        return compareHostNames(nA,nB);

    });
    displayHostsCrudTable(listOfHosts);
} 

function displayHostsCrudSortedDown(){
    
    listOfHosts=JSON.parse( sessionStorage.getItem('hosts'));
    listOfHosts.sort(function(a, b)
    {
        nA=a.hostname;
        nB=b.hostname;
        
        return compareHostNames(nA,nB);

    });    
    listOfHosts=invertJsonArray(listOfHosts);
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
    
    
    createNewHostButton = createNewHostForm();
    objectsTableDiv.appendChild(createNewHostButton);
    
    
    form = hostsCrudForm(objectList);
    objectsTableDiv.appendChild(form);
}

function displayHostsCrud(objectList){
    
    fillBodyHeader("Create or delete PerfSonar Hosts");
    
    
    displayHostsCrudTable(objectList);
            
}

