/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    fillBodyHeader("Create or Delete PerfSonar Cloud");
    getListOfClouds();
}

function getListOfClouds() {
    var url = cloudsUrl;
   
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processListOfClouds(request.responseText);
        }
    };
    request.send(null);
}
function processListOfClouds(responseText){
    sessionStorage.setItem('clouds',responseText);
  
    listOfClouds = JSON.parse(sessionStorage.getItem('clouds'));
   
    displayCloudsCrud();
} 
function displayCloudsCrud(){
    displayCloudsCrudTable();
}
function displayCloudsCrudSortedUp(){
    
    sessionStorage.setItem('cloudsSortingOrder',"up");
    
    displayCloudsCrudTable();
} 

function displayCloudsCrudSortedDown(){
        
    sessionStorage.setItem('cloudsSortingOrder',"down");
    
    displayCloudsCrudTable();
} 

function displayCloudsCrudTable(){
    
    listOfClouds=JSON.parse(sessionStorage.getItem("clouds"));
    
    var objectsTableDiv = document.getElementById("cloudsTableDiv");
    // clear content of the division
    
    removeAllChildNodes(objectsTableDiv);

    sortingOrder = sessionStorage.getItem('cloudsSortingOrder');
    
    if(sortingOrder=="up"){
        listOfClouds.sort(function(a, b)
        {
            nA=a.name;
            nB=b.name;
            return compareStrings(nA,nB);

        });
    }
    if(sortingOrder=="down"){
        listOfClouds.sort(function(a, b)
        {
            nA=a.name;
            nB=b.name;
        
            return compareStrings(nA,nB);

        }); 
        listOfClouds=invertJsonArray(listOfClouds);
    }

    var objectsTable = listOfCloudsCrudTable(listOfClouds);
    
    objectsTableDiv.appendChild(objectsTable);
}

function createNewCloud(){
    
    window.location=urlToCreateNewCloud;

}



function deleteSelectedCloud(){
    
    cloud={};
    
    listOfRadioButtons = document.getElementsByName("id");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            cloud.id=currentButton.value;
            break;
        }
    }
            
    var request = new XMLHttpRequest();
    url=cloudsUrl+cloud.id;
    
    request.open("DELETE", url);
    request.onload = function() {
        if (request.status == 200) {
            processDeleteCloudResponse(request.responseText);
        }else{
            alert("Cloud delete failed");
            alert(request.responseText);
        }
    };
    request.send(null);
}

function processDeleteCloudResponse(responseText){
    getListOfClouds();
}