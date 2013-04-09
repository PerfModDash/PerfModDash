/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function summaryTableForServicesOnHostList(listOfServices){
    var tableObject = document.createElement("table");
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    tableBody.appendChild(twoCellHeaderRow("Service", "Status"));
    
    for (var i = 0; i < listOfServices.length; i++) {
        var service = listOfServices[i];                
                
        targetUrl=urlToDisplayServices+"?id="+service.id;
        tableBody.appendChild(twoCellLinkStatusRow(service.description,targetUrl,service.result.status));        
    }
    
    return tableObject;    
}


function summaryTableForServicesList(listOfServices){
  
    var tableObject = document.createElement("table");
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    tableBody.appendChild(singleCellHeaderRow("Service"));
    
    for (var i = 0; i < listOfServices.length; i++) {
        var service = listOfServices[i];                        
        var linkUrl=urlToDisplayServices+"?id="+service.id;
        tableBody.appendChild(singleCellLinkRow(service.name,linkUrl));
    }
    
    return tableObject;
    
}

function boxedTableForServicesList(listOfServices){
   
    var tableObject = document.createElement("table");
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    var maxCellsInRow=5;
    var cellCounter=0;
    
    var row = document.createElement("tr");
    for (var i = 0; i < listOfServices.length; i++) {
        
        cellCounter=cellCounter+1;
        if(cellCounter==maxCellsInRow){
            tableBody.appendChild(row);
            row = document.createElement("tr");
            cellCounter=0;
        }
            
        var service = listOfServices[i];
         
        var serviceUrl=urlToDisplayServices+"?id="+service.id
         
        cell = singleCellLinkStatus(service.type,serviceUrl,service.result.status);
        row.appendChild(cell);
        
        
    }
    tableBody.appendChild(row);
    
    return tableObject;
}

function cellWithHistoryLinks(id){
    var cell = document.createElement("td");
    
    cell.appendChild(linkToServiceHistory(id,"1"));
    cell.appendChild(linkToServiceHistory(id,"6"));
    cell.appendChild(linkToServiceHistory(id,"12"));
    cell.appendChild(linkToServiceHistory(id,"24"));
    cell.appendChild(linkToServiceHistory(id,"48"));
    cell.appendChild(linkToServiceHistory(id,""));
    return cell;
}

function linkToServiceHistory(id,hoursAgo){
    linkUrl=urlToDisplayHistory+"?id="+id;
    linkText="all history";
    if(""!=hoursAgo){
        linkUrl=linkUrl+"&hoursAgo="+hoursAgo;
        linkText=hoursAgo+" hours ago, "
    }
    
    var link = document.createElement("a");
    link.setAttribute("href", linkUrl);
    
    var textNode=document.createTextNode(linkText); 
    link.appendChild(textNode);
    
    return link;
}