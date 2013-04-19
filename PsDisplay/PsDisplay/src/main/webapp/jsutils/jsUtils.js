/* 
 * Various javascript utilities
 */

function compareStrings(nA,nB){

    if(nA==null){
        return 1;
    }else{
        if(nB==null){
            return -1;
        }else{
            if(nA < nB)
                return 1;
            else if(nA > nB)
                return -1;
            return 0;
        }
    }
}

function invertHostName(hostName){
    if(hostName==null){
        return null;
    }else{
        result="";
        splitArray=hostName.split(".");
        for(i=0;i<splitArray.length;i=i+1){
            result=splitArray[i]+"."+result;
        }
        markerString="!@#$%^&"
        result=result+markerString;
        result=result.replace("."+markerString,"");
        return result;
    }
}

function compareHostNames(hostA,hostB){    
    return compareStrings(invertHostName(hostA),invertHostName(hostB));
}

function invertJsonArray(jsonArray){
    result=[];
    for(i=jsonArray.length-1;i>-1;i=i-1){
        element=jsonArray[i];
        result.push(element);
    }
    return result;
}