/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function fullTableForHost(hostJson){
    
    var id=hostJson.id
        
    var hostTable = document.createElement("table");
    hostTable.setAttribute("id", "objects");
    
    var hostTblBody = document.createElement("tbody");
    hostTable.appendChild(hostTblBody);
    
   
    
    //  --- header row ---  //

    hostTblBody.appendChild(twoCellHeaderRow("Name",hostJson.hostname));
    
    // --- rows ---  //
    
    hostTblBody.appendChild(twoCellRow("Id",id));
    hostTblBody.appendChild(twoCellRow("IPV4",hostJson.ipv4));
    hostTblBody.appendChild(twoCellRow("IPV6",hostJson.ipv6));
    
    return hostTable;
    
}

function listOfHostsTable(objectList){
    
    var objectsTable = document.createElement("table");
    var tblBody = document.createElement("tbody");
    
    var objectsTableHeaderRow = document.createElement("tr");
    var header0 = document.createElement("th");
            
    var header0text = document.createTextNode("Host Name");
    header0.appendChild(header0text);
    objectsTableHeaderRow.appendChild(header0);
    
    var arrowDownLink = document.createElement("a");
    //arrowDownLink.setAttribute("href","google.com");
    arrowDownLink.onclick=displayHostsSortedDown;
    var arrowDownImage= document.createElement("img")
    arrowDownImage.setAttribute("src","images/down.gif");
    arrowDownLink.appendChild(arrowDownImage);
    header0.appendChild(arrowDownLink);
    
    var arrowUpLink = document.createElement("a");
    //arrowUpLink.setAttribute("href","google.com");
    arrowUpLink.onclick=displayHostsSortedUp;
    var arrowUpImage= document.createElement("img")
    arrowUpImage.setAttribute("src","images/up.gif");
    arrowUpLink.appendChild(arrowUpImage);
    header0.appendChild(arrowUpLink);
    
    tblBody.appendChild(objectsTableHeaderRow);
    
    objectsTable.appendChild(tblBody);
    
    objectsTable.setAttribute("id", "objects");
        
    for (var i = 0; i < objectList.length; i++) {
        var object = objectList[i];
        
        var row = document.createElement("tr");        
        var cell0 = document.createElement("td");
        objectName="---";
        if (object.hostname!=null){
            objectName=object.hostname;
        }
     
        
        id=object.id;
        var cell0Link = document.createElement("a");
        cell0Link.setAttribute("href", urlToDisplayHosts+"?id="+id);
        var cell0Text=document.createTextNode(objectName);  
        cell0Link.appendChild(cell0Text);
        cell0.appendChild(cell0Link);
        
        row.appendChild(cell0);
        
        tblBody.appendChild(row);
        
    }
    return objectsTable;
}

function listOfHostsCrudTable(objectList){
    
    var objectsTable = document.createElement("table");
    
    var tblBody = document.createElement("tbody");
    objectsTable.appendChild(tblBody);
    
    var objectsTableHeaderRow = document.createElement("tr");
    
    //    var header0 = document.createElement("th");
    //    var header0text = document.createTextNode(" ");
    //    header0.appendChild(header0text);
    //    objectsTableHeaderRow.appendChild(header0);

    var header0 = document.createElement("th");
    objectsTableHeaderRow.appendChild(header0);
            
    var header1 = document.createElement("th");        
    var header1text = document.createTextNode("Host Name");
    header1.appendChild(header1text);
    objectsTableHeaderRow.appendChild(header1);
    
    var arrowDownLink = document.createElement("a");
    arrowDownLink.onclick=displayHostsCrudSortedDown;
    var arrowDownImage= document.createElement("img")
    arrowDownImage.setAttribute("src","images/down.gif");
    arrowDownLink.appendChild(arrowDownImage);
    header1.appendChild(arrowDownLink);
    
    var arrowUpLink = document.createElement("a");
    arrowUpLink.onclick=displayHostsCrudSortedUp;
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
        radio.setAttribute("id", "hostIdField");
        radio.setAttribute("name", "id");
        radio.setAttribute("value", object.id);
        cell0.appendChild(radio);
        
 
        
        row.appendChild(cell0);
        
        // --- second cell ---
        
        var cell1 = document.createElement("td");
        objectName="---";
        if (object.hostname!=null){
            objectName=object.hostname;
        }
        
        id=object.id;
        var cell1Link = document.createElement("a");
        //cell1Link.setAttribute("href", urlToDisplayHosts+"?id="+id);
        cell1Link.setAttribute("href", urlToEditHost+"?id="+id);
        var cell1Text=document.createTextNode(objectName);  
        cell1Link.appendChild(cell1Text);
        cell1.appendChild(cell1Link);
        
        row.appendChild(cell1);
        
        tblBody.appendChild(row);
    }
    return objectsTable;
}

function hostsCrudForm(objectList){
    var form = document.createElement("form");
    form.setAttribute("method","GET");
    //form.setAttribute("action", deleteHostServletUrl);
    form.setAttribute("action", urlToCrudHosts);
    
    
    
    var objectsTable = listOfHostsCrudTable(objectList);
    form.appendChild(objectsTable);
    
    var operation = document.createElement("input"); 
    operation.setAttribute("type", "hidden");
    operation.setAttribute("value", "delete");
    operation.setAttribute("name", "operation");
    form.appendChild(operation);
    
    return form;
}

function createNewHostForm(){
    var form = document.createElement("form");
    form.setAttribute("method","POST");   
    form.setAttribute("action", urlToCrudHosts);
    
    var operation  = document.createElement("input"); 
    operation.setAttribute("type", "hidden");
    operation.setAttribute("value", "create");
    operation.setAttribute("name", "operation");
    form.appendChild(operation);
    
    var submitButton = document.createElement("input"); 
    submitButton.setAttribute("type","submit");
    submitButton.setAttribute("value","Create New Host");
    form.appendChild(submitButton);
    return form;
}

function getEditHostForm(host){
    var form = document.createElement("form");
    form.setAttribute("method","GET");   
    form.setAttribute("action", urlToCrudHosts);
    
    var table = getEditHostTable(host);
    form.appendChild(form);
    return form;
}

function getEditHostTable(host){
    var table = document.createElement("table");
    var tbody = document.createElement("tbody");
    table.appendChild(tbody);
    
    headerRow =  document.createElement("th");
    tbody.appendChild(headerRow);
    
    headerCell0=document.createElement("td");
    tbody.appendChild(headerCell0);
    headerCell1=document.createElement("td");
    tbody.appendChild(headerCell1);
    
    hostNameRow = document.createElement("tr");
    tbody.appendChild(hostNameRow);
    hostNameCell0 = document.createElement("td");
    hostNameRow.appendChild(hostNameCell0);
    
    hostNameCell1 = document.createElement("td");
    
    return table;
}
function hostContainsServiceType(host,serviceType){
    
    result=0;
    for (i=0;i<host.services.length;i=i+1){
        
        if(host.services[i].type==serviceType.id){
            return 1;
        }
    }
    return result;
}

function listOfPrimitiveServicesNotOnHost(host){
    resultList = [];
    for(j=0;j<listOfServiceTypes.length;j=j+1){
        serviceType = listOfServiceTypes[j];
        if(hostContainsServiceType(host,serviceType)==0){
            resultList.push(serviceType)
        }
    }
    return resultList;
}