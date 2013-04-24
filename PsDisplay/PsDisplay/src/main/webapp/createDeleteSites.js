/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
window.onload = init;

function init() {
    fillBodyHeader("Create or delete PerfSonar Site");
    getListOfSites();
}

function getListOfSites() {
    var url = sitesUrl;
   
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processListOfSites(request.responseText);
        }
    };
    request.send(null);
}
function processListOfSites(responseText){
    sessionStorage.setItem('sites',responseText);
  
    listOfSites = JSON.parse(sessionStorage.getItem('sites'));
   
    displaySitesCrud();
} 
function displaySitesCrud(){
    displaySitesCrudTable();
}
function displaySitesCrudSortedUp(){
    
    sessionStorage.setItem('siteSortingOrder',"up");
    
    displaySitesCrudTable();
} 

function displaySitesCrudSortedDown(){
        
    sessionStorage.setItem('siteSortingOrder',"down");
    
    displaySitesCrudTable();
} 

function displaySitesCrudTable(){
    
    listOfSites=JSON.parse(sessionStorage.getItem("sites"));
    
    var objectsTableDiv = document.getElementById("sitesTableDiv");
    // clear content of the division
    
    removeAllChildNodes(objectsTableDiv);

    sortingOrder = sessionStorage.getItem('siteSortingOrder');
    
    if(sortingOrder=="up"){
        listOfSites.sort(function(a, b)
        {
            nA=a.name;
            nB=b.name;
            return compareStrings(nA,nB);

        });
    }
    if(sortingOrder=="down"){
        listOfSites.sort(function(a, b)
        {
            nA=a.name;
            nB=b.name;
        
            return compareStrings(nA,nB);

        }); 
        listOfSites=invertJsonArray(listOfSites);
    }

    var objectsTable = listOfSitesCrudTable(listOfSites);
    
    objectsTableDiv.appendChild(objectsTable);
}

function createNewSite(){
    
    var url = sitesUrl;
    
    var request = new XMLHttpRequest();
    request.open("POST", url);
    request.onload = function() {
        if (request.status == 200) {
            getListOfSites();
        }
    };
    request.send(null);
}



function deleteSelectedSite(){
    
    site={};
    
    listOfRadioButtons = document.getElementsByName("id");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            site.id=currentButton.value;
            break;
        }
    }
            
    var request = new XMLHttpRequest();
    url=sitesUrl+site.id;
    
    request.open("DELETE", url);
    request.onload = function() {
        if (request.status == 200) {
            processDeleteSiteResponse(request.responseText);
        }else{
            alert("Site delete failed");
            alert(request.responseText);
        }
    };
    request.send(null);
}

function processDeleteSiteResponse(responseText){
    getListOfSites();
}