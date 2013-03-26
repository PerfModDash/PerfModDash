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