/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function h1header(headerTxt){    
    var h1element=document.createElement("h1");
 
    var headerTextElement=document.createTextNode(headerTxt);
    h1element.appendChild(headerTextElement);
   
    return h1element;
}

function h2header(headerTxt){    
    var h1element=document.createElement("h2");
 
    var headerTextElement=document.createTextNode(headerTxt);
    h1element.appendChild(headerTextElement);
   
    return h1element;
}


function fillBodyHeader(headerContent) {
    var bodyHeader = document.getElementById("bodyHeader");
    var h1element=document.createElement("h1");
    bodyHeader.appendChild(h1element);
    
    var headerText=document.createTextNode(headerContent);
    h1element.appendChild(headerText);
    
    bodyHeader.appendChild(h1element);        
}

function status2color(status){
    var color="white";
    if(status=="0"){
        color="green";
    }
    if(status=="1"){
        color="yellow";
    }
    if(status=="2"){
        color="red";
    }
    if(status=="3"){
        color="brown";
    }
    return color;
}

function status2ShortText(status){
    var textObject=document.createElement("p");
    
    var statusText="---";
    if(status=="0"){
        statusText="OK";
        textObject.style.color="green";
    }
    if(status=="1"){
        statusText="WARN";
        textObject.style.color="yellow";
    }
    if(status=="2"){
        statusText="ERR";
        textObject.style.color="red";
    }
    if(status=="3"){
        statusText="UNKN";
        textObject.style.color="brown";
    }
    
    var textNode=document.createTextNode(statusText);
    textObject.appendChild(textNode);
    
    //return textObject;
    
    var bfObject=document.createElement("strong");
    bfObject.appendChild(textObject);
    
    return bfObject;
}

