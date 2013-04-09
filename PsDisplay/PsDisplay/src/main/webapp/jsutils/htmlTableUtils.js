/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function singleCellText(cell0Text){
    var cell0 = document.createElement("td");    
    var cell0textNode = document.createTextNode(cell0Text);
    cell0.appendChild(cell0textNode);
    return cell0;
}

function singleCelltext(cell0Text){
    return singleCellText(cell0Text);
}

function singleCellRow(cell0Text){
    var row = document.createElement("tr");
    
    var header0 = document.createElement("td");            
    var header0text = document.createTextNode(cell0Text);
    header0.appendChild(header0text);
    row.appendChild(header0);
    
    return row;
}

function singleCellHeaderRow(cell0Text){
    var row = document.createElement("tr");
    
    var header0 = document.createElement("th");            
    var header0text = document.createTextNode(cell0Text);
    header0.appendChild(header0text);
    row.appendChild(header0);
    
    return row;
}

function twoCellRow(cell0Text,cell1Text){
    var row = document.createElement("tr");
    
    var header0 = document.createElement("td");            
    var header0text = document.createTextNode(cell0Text);
    header0.appendChild(header0text);
    row.appendChild(header0);
    
    var header1 = document.createElement("td");            
    var header1text = document.createTextNode(cell1Text);
    header1.appendChild(header1text);
    row.appendChild(header1);
    
    return row;
}


function twoCellStatusRow(cell0Text,cell1Text){
    
   
    
    var row = document.createElement("tr");
    
    var header0 = document.createElement("td");            
    var header0text = document.createTextNode(cell0Text);
    header0.appendChild(header0text);
    row.appendChild(header0);
    
    var header1 = document.createElement("td");    
    header1.appendChild(status2ShortText(cell1Text));
    //var header1text = document.createTextNode(cell1Text);
    //header1.appendChild(header1text);
    row.appendChild(header1);
    
    return row;
}

function twoCellHeaderRow(cell0Text,cell1Text){
    var row = document.createElement("tr");
    
    var header0 = document.createElement("th");            
    var header0text = document.createTextNode(cell0Text);
    header0.appendChild(header0text);
    row.appendChild(header0);
    
    var header1 = document.createElement("th");            
    var header1text = document.createTextNode(cell1Text);
    header1.appendChild(header1text);
    row.appendChild(header1);
    
    return row;
}


function singleCellLinkRow(cell0Text,cell0url){
    
    row=document.createElement("tr");
    
    var cell0Link = document.createElement("a");
    cell0Link.setAttribute("href", cell0url);
    
    var cell0TextNode=document.createTextNode(cell0Text); 
    cell0Link.appendChild(cell0TextNode);
    cell0=document.createElement("td");
    cell0.appendChild(cell0Link);    
    row.appendChild(cell0);
            
    return row;
}

function twoCellLinkStatusRow(cell0Text,cell0url,status){
    
    row=document.createElement("tr");
    
    var cell0Link = document.createElement("a");
    cell0Link.setAttribute("href", cell0url);
    
    var cell0TextNode=document.createTextNode(cell0Text); 
    cell0Link.appendChild(cell0TextNode);
    cell0=document.createElement("td");
    cell0.appendChild(cell0Link);    
    row.appendChild(cell0);
    
    statusTextNode = status2ShortText(status);
    cell1=document.createElement("td");
    cell1.appendChild(statusTextNode);
    row.appendChild(cell1);
    
    return row;
}

function singleCellLink(cellText,cellUrl){
    var cell=document.createElement("td");
    var cellTextElement=document.createTextNode(cellText);
    var bfNode = document.createElement("strong");
    bfNode.appendChild(cellTextElement);
    
    var cellLinkElement=document.createElement("a");
    cellLinkElement.appendChild(bfNode);
    cellLinkElement.setAttribute("href", cellUrl);
    cell.appendChild(cellLinkElement);
    return cell;
    
}

function singleCellLinkStatus(cellText,cellUrl,cellStatus){
    
    var cell=document.createElement("td");
    var cellTextElement=document.createTextNode(cellText);
    
    var bfNode = document.createElement("strong");
    bfNode.appendChild(cellTextElement);
    bfNode.style.color=status2color(cellStatus);
    
    
    var cellLinkElement=document.createElement("a");
    
    cellLinkElement.appendChild(bfNode);
    cellLinkElement.setAttribute("href", cellUrl);
    //cell.style.borderColor=status2color(cellStatus);
    cell.appendChild(cellLinkElement);
    return cell;
}

function singleStatusCell(cellStatus){
    
    var cell=document.createElement("td");
    
    var cellTextElement=document.createTextNode(status2PlainText(cellStatus));
    
    var bfNode = document.createElement("strong");
    bfNode.appendChild(cellTextElement);
    bfNode.style.color=status2color(cellStatus);
    
    cell.appendChild(bfNode);
    
    return cell;
}



