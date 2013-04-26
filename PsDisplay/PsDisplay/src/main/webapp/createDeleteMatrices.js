/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;

function init() {
    fillBodyHeader("Create or Delete PerfSonar Matrix");
    getListOfMatrices();
}

function getListOfMatrices() {
    var url = matricesUrl;
   
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            processListOfMatrices(request.responseText);
        }
    };
    request.send(null);
}
function processListOfMatrices(responseText){
    sessionStorage.setItem('matrices',responseText);
  
    listOfMatrices = JSON.parse(sessionStorage.getItem('matrices'));
   
    displayMatricesCrud();
} 
function displayMatricesCrud(){
    displayMatricesCrudTable();
}
function displayMatricesCrudSortedUp(){
    
    sessionStorage.setItem('matricesSortingOrder',"up");
    
    displayMatricesCrudTable();
} 

function displayMatricesCrudSortedDown(){
        
    sessionStorage.setItem('matricesSortingOrder',"down");
    
    displayMatricesCrudTable();
} 

function displayMatricesCrudTable(){
    
    listOfMatrices=JSON.parse(sessionStorage.getItem("matrices"));
    
    var objectsTableDiv = document.getElementById("matricesTableDiv");
    // clear content of the division
    
    removeAllChildNodes(objectsTableDiv);

    sortingOrder = sessionStorage.getItem('matricesSortingOrder');
    
    if(sortingOrder=="up"){
        listOfMatrices.sort(function(a, b)
        {
            nA=a.name;
            nB=b.name;
            return compareStrings(nA,nB);

        });
    }
    if(sortingOrder=="down"){
        listOfMatrices.sort(function(a, b)
        {
            nA=a.name;
            nB=b.name;
        
            return compareStrings(nA,nB);

        }); 
        listOfMatrices=invertJsonArray(listOfMatrices);
    }

    var objectsTable = listOfMatricesCrudTable(listOfMatrices);
    
    objectsTableDiv.appendChild(objectsTable);
}

function createNewMatrix(){
    
    window.location=urlToCreateNewMatrix;

}



function deleteSelectedMatrix(){
    
    matrix={};
    
    listOfRadioButtons = document.getElementsByName("id");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            matrix.id=currentButton.value;
            break;
        }
    }
            
    var request = new XMLHttpRequest();
    url=matricesUrl+matrix.id;
    
    request.open("DELETE", url);
    request.onload = function() {
        if (request.status == 200) {
            processDeleteMatrixResponse(request.responseText);
        }else{
            alert("Matrix delete failed");
            alert(request.responseText);
        }
    };
    request.send(null);
}

function processDeleteMatrixResponse(responseText){
    getListOfMatrices();
}