/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function historyTableRow(historyRecordJson){
    tableRow = document.createElement("tr");
    
    tableRow.appendChild(singleCelltext(historyRecordJson.time));
    
    tableRow.appendChild(singleStatusCell(historyRecordJson.status));
    
    tableRow.appendChild(singleCelltext(historyRecordJson.message));
    
    return tableRow;
}

function historyTable(historyJsonArray){
    table = document.createElement("table");
    tableBody = document.createElement("tbody");
    table.appendChild(tableBody);
    
    for (var i = 0; i < historyJsonArray.length; i++) {
        historyRecord = historyJsonArray[i];
        tableBody.appendChild(historyTableRow(historyRecord));
    }
    
    return table;
}

function getHistoryLinkTable(serviceId){
    table = document.createElement("table");
    tableBody = document.createElement("tbody");
    table.appendChild(tableBody);
    
    tableRow = document.createElement("tr");
    tableBody.appendChild(tableRow);
    
    tableRow.appendChild(cellWithHistoryLinks(serviceId));
    
    return table;
}
