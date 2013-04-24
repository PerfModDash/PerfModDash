/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function testHost(){
    alert("hello hosts");
   
}

function fullTableOfSite(siteJson){
    
}

function siteContainsHost(site,host){
    for(j=0;j<site.hosts.length;j=j+1){
        if(host.id==site.hosts[j].id){
            return true;
        }
    }
    return false;
}
function getListOfHostsNotInSite(site){
    listOfAllHosts = JSON.parse(sessionStorage.getItem('allHosts'));
    
    listOfHostsNotInSite=[];
    for(i=0;i<listOfAllHosts.length;i=i+1){
        host=listOfAllHosts[i];
        if(!siteContainsHost(site,host)){
            listOfHostsNotInSite.push(host);
        }
    }
    return listOfHostsNotInSite;
}

function summaryTableForSitesList(listOfSitesJson){
   
    var tableObject = document.createElement("table");
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    tableBody.appendChild(singleCellHeaderRow("Name"));
    
    for (var i = 0; i < listOfSitesJson.length; i++) {
        var site = listOfSitesJson[i];                        
        var linkUrl=urlToDisplaySites+"?id="+site.id;
        tableBody.appendChild(singleCellLinkRow(site.name,linkUrl));
    }
    
    return tableObject;
    
}

function listOfSitesCrudTable(objectList){
    
    var objectsTable = document.createElement("table");
    
    var tblBody = document.createElement("tbody");
    objectsTable.appendChild(tblBody);
    
    var objectsTableHeaderRow = document.createElement("tr");
    
    
    var header0 = document.createElement("th");
    objectsTableHeaderRow.appendChild(header0);
            
    var header1 = document.createElement("th");        
    var header1text = document.createTextNode("Site Name");
    header1.appendChild(header1text);
    objectsTableHeaderRow.appendChild(header1);
    
    var arrowDownLink = document.createElement("a");
    arrowDownLink.onclick=displaySitesCrudSortedDown;
    var arrowDownImage= document.createElement("img")
    arrowDownImage.setAttribute("src","images/down.gif");
    arrowDownLink.appendChild(arrowDownImage);
    header1.appendChild(arrowDownLink);
    
    var arrowUpLink = document.createElement("a");
    arrowUpLink.onclick=displaySitesCrudSortedUp;
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
        radio.setAttribute("id", "siteIdField");
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
        
        cell1Link.setAttribute("href", urlToEditSite+"?id="+id);
        var cell1Text=document.createTextNode(objectName);  
        cell1Link.appendChild(cell1Text);
        cell1.appendChild(cell1Link);
        
        row.appendChild(cell1);
        
        tblBody.appendChild(row);
    }
    return objectsTable;
}