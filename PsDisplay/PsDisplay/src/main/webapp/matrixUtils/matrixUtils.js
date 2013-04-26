/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function summaryTableForMatricesList(listOfMatrices){
  
    var tableObject = document.createElement("table");
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    tableBody.appendChild(singleCellHeaderRow("Matrix"));
    
    for (var i = 0; i < listOfMatrices.length; i++) {
        var matrix = listOfServices[i];                        
        var linkUrl=urlToDisplayMatrices+"?id="+matrix.id;
        tableBody.appendChild(singleCellLinkRow(matrix.name,linkUrl));
    }    
    return tableObject;    
}

function matrixHeaderRow(columnsJson){
    
    var row = document.createElement("tr");
    
    var firstCellContent = document.createTextNode(" ");
    var firstCell = document.createElement("td");
    firstCell.appendChild(firstCellContent);
    row.appendChild(firstCell);
    
    for (var i = 0; i < columnsJson.length; i++) {
        var host=columnsJson[i];
        var hostUrl=urlToDisplayHosts+"?id="+host.id;
        var cell = singleCellLink(i,hostUrl);
        row.appendChild(cell);
    }
    
    return row;
}

function matrixHeaderRowEdit(columnsJson){
    
    var row = document.createElement("tr");
    
    
    var firstCell = document.createElement("td");
    firstCell.innerHTML='<button type="button" onclick="removeHostFromMatrixAndReDisplay()" >Remove Selected Hosts</button>';
    
    
//    removeHostButton = document.createElement("button");
//    removeHostButton.setAttribute("type","button");
//    removeHostButton.setAttribute("onclick","removeHostFromMatrixAndReDisplay");
//    buttonText = document.createTextNode("Remove Selected Host");
//    removeHostButton.appendChild(buttonText);
//    firstCell.appendChild(removeHostButton);
    
    row.appendChild(firstCell);
    
    for (var i = 0; i < columnsJson.length; i++) {
        var host=columnsJson[i];
        var hostUrl=urlToDisplayHosts+"?id="+host.id;
        var cell = singleCellLink(i,hostUrl);
        row.appendChild(cell);
    }
    
    return row;
}

function matrixElementServiceCell(element){
    var elementCell=document.createElement("td");
    
    
    if(element.hasOwnProperty("result")){
        if(element.result.hasOwnProperty("status")){
            var status = element.result.status;
            var cellText = document.createTextNode("");
            
            var bfNode = document.createElement("strong");
            bfNode.appendChild(cellText);
            bfNode.style.backgroundcolor=status2color(status);
            
            elementCell.style.backgroundColor=status2color(status);
            
            elementCell.appendChild(bfNode);
            
            var createClickHandler = function(cellElement)   {
                return function(){
                    //alert("clicked "+cellElement.id);
                    window.location=urlToDisplayServices+"?id="+cellElement.id;
                }
                                 
            };

            elementCell.onclick = createClickHandler(element);
            //elementCell.onmouseover=tooltip.pop(this, '<span class=\'red\'>Hi</span> there');
            
            elementCell.setAttribute("title", element.result.message);
            
            
        }
    }
    return elementCell;
}

function matrixElement(elementList){
    var elementTable = document.createElement("table");
    var tableBody = document.createElement("tbody");
    elementTable.appendChild(tableBody);
    
    for (var i = 0; i < elementList.length; i++) {
        var element = elementList[i];
        var elementRow=document.createElement("tr");
        
        var elementCell=matrixElementServiceCell(element);
        elementRow.appendChild(elementCell);
        
        tableBody.appendChild(elementRow);
    }
    return elementTable;
}

function matrixRow(rowNumber,host,listOfTableElements,tableType){
    var row = document.createElement("tr");
    
    var hostUrl=urlToDisplayHosts+"?id="+host.id;
    var firstCell= singleCellLink(rowNumber+":"+host.hostname,hostUrl);
    row.appendChild(firstCell);
    
    for (var i = 0; i < listOfTableElements.length; i++) {
        var element=listOfTableElements[i];
        
        var cell = document.createElement("td");
        var cellContent = matrixElement(element);
        cell.appendChild(cellContent);
        row.appendChild(cell);
    }
    
    return row;
}


function matrixRowEdit(rowNumber,host,listOfTableElements,tableType){
    var row = document.createElement("tr");
    
    var hostUrl=urlToDisplayHosts+"?id="+host.id;
    var firstCell= singleCellDeleteHostFromMatrixLink(rowNumber,host,hostUrl);
    row.appendChild(firstCell);
    
    for (var i = 0; i < listOfTableElements.length; i++) {
        var element=listOfTableElements[i];
        
        var cell = document.createElement("td");
        var cellContent = matrixElement(element);
        cell.appendChild(cellContent);
        row.appendChild(cell);
    }
    
    return row;
}

function tableForMatrix(matrixJson){
    var tableObject = document.createElement("table");
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    var columnHosts = matrixJson.columns;
    var rowHosts = matrixJson.rows;
    
    var firstRow = matrixHeaderRow(columnHosts);
    tableBody.appendChild(firstRow);
    
    for (var i = 0; i < rowHosts.length; i++) {
        var host=rowHosts[i];
        var row = matrixRow(i,host,matrixJson.matrix[i],"dummy");
        tableBody.appendChild(row);
    }
    
    return tableObject
}


function tableForMatrixEdit(matrixJson){
    var tableObject = document.createElement("table");
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    var columnHosts = matrixJson.columns;
    var rowHosts = matrixJson.rows;
    
    var firstRow = matrixHeaderRowEdit(columnHosts);
    tableBody.appendChild(firstRow);
    
    for (var i = 0; i < rowHosts.length; i++) {
        var host=rowHosts[i];
        var row = matrixRowEdit(i,host,matrixJson.matrix[i],"dummy");
        tableBody.appendChild(row);
    }
    
    return tableObject
}



function getTableOfMatrices(listOfMatricesJson){
    var tableObject = document.createElement("table");  
    tableObject.setAttribute("id", "objects");
    var tableBody = document.createElement("tbody");
    tableObject.appendChild(tableBody);
    
    var tableHeaderRow = document.createElement("tr");
    tableObject.appendChild(tableHeaderRow);
    
    for (var i = 0; i < listOfMatricesJson.length; i++) {
        var matrix = listOfMatricesJson[i];
        var tableCell = document.createElement("th");
        var cellText = document.createTextNode(matrix.name);
        tableCell.appendChild(cellText);
        
        tableHeaderRow.appendChild(tableCell);
    }
    
    var tableRow = document.createElement("tr");
    tableObject.appendChild(tableRow);
    
    for (var i = 0; i < listOfMatricesJson.length; i++) {
        var matrix = listOfMatricesJson[i];
        var tableCell = document.createElement("td");
        //var cellText = document.createTextNode(matrix.name);
        //tableCell.appendChild(cellText);
        
        var matrixTable = tableForMatrix(matrix);
        tableCell.appendChild(matrixTable);
        
        
        tableRow.appendChild(tableCell);
    }
    return tableObject;
}

function listOfMatricesCrudTable(objectList){
    
    var objectsTable = document.createElement("table");
    
    var tblBody = document.createElement("tbody");
    objectsTable.appendChild(tblBody);
    
    var objectsTableHeaderRow = document.createElement("tr");
    
    
    var header0 = document.createElement("th");
    objectsTableHeaderRow.appendChild(header0);
            
    var header1 = document.createElement("th");        
    var header1text = document.createTextNode("Matrix Name");
    header1.appendChild(header1text);
    objectsTableHeaderRow.appendChild(header1);
    
    var arrowDownLink = document.createElement("a");
    arrowDownLink.onclick=displayMatricesCrudSortedDown;
    var arrowDownImage= document.createElement("img")
    arrowDownImage.setAttribute("src","images/down.gif");
    arrowDownLink.appendChild(arrowDownImage);
    header1.appendChild(arrowDownLink);
    
    var arrowUpLink = document.createElement("a");
    arrowUpLink.onclick=displayMatricesCrudSortedUp;
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
        radio.setAttribute("id", "matrixIdField");
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
        
        cell1Link.setAttribute("href", urlToEditMatrix+"?id="+id);
        var cell1Text=document.createTextNode(objectName);  
        cell1Link.appendChild(cell1Text);
        cell1.appendChild(cell1Link);
        
        row.appendChild(cell1);
        
        tblBody.appendChild(row);
    }
    return objectsTable;
}


function matrixContainsHost(matrix,host){
    for(j=0;j<matrix.rows.length;j=j+1){
        if(host.id==matrix.rows[j].id){
            return true;
        }
    }
    for(k=0;k<matrix.columns.length;k=k+1){
        if(host.id==matrix.rows[k].id){
            return true;
        }
    }
    return false;
}
function getListOfHostsNotInMatrix(matrix){
    listOfAllHosts = JSON.parse(sessionStorage.getItem('allHosts'));
    
    listOfHostsNotInMatrix=[];
    for(i=0;i<listOfAllHosts.length;i=i+1){
        host=listOfAllHosts[i];
        if(!matrixContainsHost(matrix,host)){
            listOfHostsNotInMatrix.push(host);
        }
    }
    return listOfHostsNotInMatrix;
}