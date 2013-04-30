/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function summaryTableForCloudsList(listOfCloudsJson){
    var tableObject = document.createElement("table");
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    tableBody.appendChild(singleCellHeaderRow("Cloud Name"));
    for (var i = 0; i < listOfCloudsJson.length; i++) {
        var cloud= listOfCloudsJson[i];                        
        var linkUrl=urlToDisplayClouds+"?id="+cloud.id;
        cloudName="---";
        if (cloud.name!=null){
            cloudName=cloud.name;
        }
        tableBody.appendChild(singleCellLinkRow(cloudName,linkUrl));
    }   
    return tableObject;
}


function listOfCloudsCrudTable(objectList){
    
    var objectsTable = document.createElement("table");
    
    var tblBody = document.createElement("tbody");
    objectsTable.appendChild(tblBody);
    
    var objectsTableHeaderRow = document.createElement("tr");
    
    
    var header0 = document.createElement("th");
    objectsTableHeaderRow.appendChild(header0);
            
    var header1 = document.createElement("th");        
    var header1text = document.createTextNode("Cloud Name");
    header1.appendChild(header1text);
    objectsTableHeaderRow.appendChild(header1);
    
    var arrowDownLink = document.createElement("a");
    arrowDownLink.onclick=displayCloudsCrudSortedDown;
    var arrowDownImage= document.createElement("img")
    arrowDownImage.setAttribute("src","images/down.gif");
    arrowDownLink.appendChild(arrowDownImage);
    header1.appendChild(arrowDownLink);
    
    var arrowUpLink = document.createElement("a");
    arrowUpLink.onclick=displayCloudsCrudSortedUp;
    var arrowUpImage= document.createElement("img")
    arrowUpImage.setAttribute("src","images/up.gif");
    arrowUpLink.appendChild(arrowUpImage);
    header1.appendChild(arrowUpLink);
    
    
    
    tblBody.appendChild(objectsTableHeaderRow);
    
    objectsTable.setAttribute("id", "objects");
        
    for (var i = 0; i < objectList.length; i++) {
        var object = objectList[i];
        
        var row = document.createElement("tr"); 
        
        var cell0 = document.createElement("td");
        
        var radio = document.createElement("input"); 
        radio.setAttribute("type", "radio");
        radio.setAttribute("id", "cloudIdField");
        radio.setAttribute("name", "id");
        radio.setAttribute("value", object.id);
        cell0.appendChild(radio);
        
 
        
        row.appendChild(cell0);
        
        // --- second cell ---
        
        var cell1 = document.createElement("td");
        objectName="---";
        if (object.name!=null){
            objectName=object.name;
        }
        
        id=object.id;
        var cell1Link = document.createElement("a");
        
        cell1Link.setAttribute("href", urlToEditCloud+"?id="+id);
        var cell1Text=document.createTextNode(objectName);  
        cell1Link.appendChild(cell1Text);
        cell1.appendChild(cell1Link);
        
        row.appendChild(cell1);
        
        tblBody.appendChild(row);
    }
    return objectsTable;
}

function cloudContainsSite(cloud,site){
    for(j=0;j<cloud.sites.length;j=j+1){
        if(site.id==cloud.sites[j].id){
            return true;
        }
    }
    return false;
}
function getListOfSitesNotInCloud(cloud){
    listOfAllSites = JSON.parse(sessionStorage.getItem('allSites'));
    
    listOfSitesNotInCloud=[];
    for(i=0;i<listOfAllSites.length;i=i+1){
        site=listOfAllSites[i];
        if(!cloudContainsSite(cloud,site)){
            listOfSitesNotInCloud.push(site);
        }
    }
    return listOfSitesNotInCloud;
}

function cloudContainsMatrix(cloud,matrix){
    for(j=0;j<cloud.matrices.length;j=j+1){
        if(matrix.id==cloud.matrices[j].id){
            return true;
        }
    }
    return false;
}
function getListOfMatricesNotInCloud(cloud){
    listOfAllMatrices = JSON.parse(sessionStorage.getItem('allMatrices'));
    
    listOfMatricesNotInCloud=[];
    for(i=0;i<listOfAllMatrices.length;i=i+1){
        matrix=listOfAllMatrices[i];
        if(!cloudContainsMatrix(cloud,matrix)){
            listOfMatricesNotInCloud.push(matrix);
        }
    }
    return listOfMatricesNotInCloud;
}