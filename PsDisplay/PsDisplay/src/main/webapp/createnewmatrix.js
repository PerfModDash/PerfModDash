/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function createNewMatrix(){
    
    matrix={};
    
    nameField = document.getElementById("nameField");
    matrix.name=nameField.value;
    
    listOfRadioButtons = document.getElementsByName("matrixType");
    for(i=0;i<listOfRadioButtons.length;i=i+1){
        currentButton = listOfRadioButtons[i];
        if(currentButton.checked){
            matrix.serviceTypeId=currentButton.value;
            break;
        }
    }
            
    var request = new XMLHttpRequest();
    url=matricesUrl;
    
    alert("url="+url);
    
    request.open("POST", url);
    request.onload = function() {
        if (request.status == 200) {
            processCreateMatrixResponse(request.responseText);
        }else{
            alert("Matrix create failed");
            alert(request.responseText);
        }
    };
    
   
    alert(JSON.stringify(matrix));
    
    request.send(JSON.stringify(matrix));
}
function processCreateMatrixResponse(responseText){
    alert("response = "+responseText);
    window.location = urlToCrudMatrices;
}
