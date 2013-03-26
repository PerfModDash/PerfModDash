/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function fullTableOfSite(siteJson){
    
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