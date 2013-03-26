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
    
    hostTblBody.appendChild(twoCellRow("IPV4",hostJson.ipv4));
    hostTblBody.appendChild(twoCellRow("IPV6",hostJson.ipv6));
    
    return hostTable;
    
}