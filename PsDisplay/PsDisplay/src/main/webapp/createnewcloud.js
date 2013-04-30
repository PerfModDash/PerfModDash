/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function createNewCloud(){
    
    cloud={};
    
    nameField = document.getElementById("nameField");
    cloud.name=nameField.value;
    
    
            
    var request = new XMLHttpRequest();
    url=cloudsUrl;
    
    request.open("POST", url);
    request.onload = function() {
        if (request.status == 200) {
            processCreateCloudResponse(request.responseText);
        }else{
            alert("Cloud create failed");
            alert(request.responseText);
        }
    };
    
    request.send(JSON.stringify(cloud));
}
function processCreateCloudResponse(responseText){
    window.location = urlToCrudClouds;
}



