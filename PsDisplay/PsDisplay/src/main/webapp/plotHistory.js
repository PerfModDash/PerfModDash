/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = init;

function init() {
    id = getUrlParameterByName("id");
    hoursAgo=getUrlParameterByName("hoursAgo");
    getHistory(id,hoursAgo);
}



function getHistory(id,hoursAgo){
    var url = servicesUrl+id+"/history";
    if(hoursAgo!=null){
        url=url+"?hoursAgo="+hoursAgo;
    }
    var request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function() {
        if (request.status == 200) {
            parseHistoryResult(request.responseText);
        }
    };
    request.send(null);
    
}


function parseHistoryResult(historyResult){
    historyJson=JSON.parse(historyResult);
    plotHistory(historyJson);
}

function plotHistory(historyJson){
    
    //alert(JSON.stringify(  historyJson));
    
    var dataArray=[];
    
    for (var i = 0; i < historyJson.length; i++) {
        if(i<399999){
            min=0;
            max=0;
            average=0;
            gbps="Gbps";
            pps="pps";
            
            historyRecord = historyJson[i];
            
            if("time" in historyRecord){
                if("parameters" in historyRecord){
                    
                    timeStampOfRecord=historyRecord["time"].replace("T"," ").replace(".000Z","");
                    timeStampOfRecord=timeStampOfRecord.replace("-","/");
                    timeStampOfRecord=timeStampOfRecord.replace("-","/");
            
                    //alert(JSON.stringify(historyRecord));
                    parameters = historyRecord["parameters"];
                    //alert(JSON.stringify(parameters));
                    min=0;
                    if("min" in parameters){
                        min=parseFloat(parameters["min"].replace(gbps,"").replace(pps,""));
                    }
                    max=0;
                    if("max" in parameters){
                        max=parseFloat(parameters["max"].replace(gbps,"").replace(pps,""));
                    }
                    average=0;
                    if("average" in parameters){
                        average=parseFloat(parameters["average"].replace(gbps,"").replace(pps,""));
                    }
                    //alert("time="+timeStampOfRecord+" min="+min+" max="+max+" average="+average);
                    arrayRow=[];
                    arrayRow.push(new Date(timeStampOfRecord));
                    arrayRow.push(min);
                    arrayRow.push(average);
                    arrayRow.push(max);
                    //alert(JSON.stringify(arrayRow));
                    dataArray.push(arrayRow);
                }
            }
           
        }
        
    }
    
    dataArray.reverse();
    
    
    g = new Dygraph(
        document.getElementById("body"),
        dataArray, 
        {
            labels: [ "time", "min", "mean","max" ]
        }
        );
            
            
//             g = new Dygraph(
//        document.getElementById("body"),
//        [ 
//        [new Date("2013/6/20 20:13:15"),10,100,120], 
//        [new Date("2013/6/21 19:34:23"),20,80,100], 
//        [new Date("2013/6/22 4:5:32"),50,60,80], 
//        [new Date("2013/6/23 16:40:20"),70,80,84] ], 
//        {
//            labels: [ "time", "min", "mean","max" ]
//        }
//        );
    
    
//    serviceId = historyJson[0]["service-id"];
//    
//    fillBodyHeader("History of service id="+serviceId);
//    
//    
//    var objectsDiv =document.getElementById("historyTableDiv");
//    
//    historyLinkTable = getHistoryLinkTable(serviceId);
//    
//    objectsDiv.appendChild(historyLinkTable);
//    
//    table = historyTable(historyJson);
//    objectsDiv.appendChild(table);
    
    
//objectsDiv.innerHTML=JSON.stringify(historyJson);
    
//var serviceTable =fullTableForService(serviceJson);
    
//objectsDiv.appendChild(serviceTable);
}

