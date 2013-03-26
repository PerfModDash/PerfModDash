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

