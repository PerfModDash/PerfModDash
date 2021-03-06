/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


//var listOfServiceTypes=[{"internalId":"1","id":"bwctl_port_4823","jobType":"nagios.tcp","resultParameterInfo":{"time":{"unit":"seconds","description":"The time it takes to check_tcp to open a connection to the target host and port"}},"serviceParameterInfo":{"port":{"unit":null,"description":"The TCP port where BWCTL should be listening. Should always be 4823 for this check."},"host":{"unit":null,"description":"The IP address or hostname of the target BWCTL host"}},"name":"BWCTL Port 4823 Check"},{"internalId":"2","id":"bwctl_port_8570","jobType":"nagios.tcp","resultParameterInfo":{"time":{"unit":"seconds","description":"The time it takes to check_tcp to open a connection to the target host and port"}},"serviceParameterInfo":{"port":{"unit":null,"description":"The TCP port where BWCTL should be listening. Should always be 8570 for this check."},"host":{"unit":null,"description":"The IP address or hostname of the target BWCTL host"}},"name":"BWCTL Port 8570 Check"},{"internalId":"3","id":"CheckLookupService","jobType":"nagios.ps.perfSONAR","resultParameterInfo":{},"serviceParameterInfo":{"host-id":{"unit":null,"description":"The id of the host where this service runs"},"url":{"unit":null,"description":"The URL of the lookup service to test"}},"name":"perfSONAR Lookup Service Test"},{"internalId":"4","id":"NDT_port_3001","jobType":"nagios.tcp","resultParameterInfo":{"time":{"unit":"seconds","description":"The time it takes to check_tcp to open a connection to the target host and port"}},"serviceParameterInfo":{"port":{"unit":null,"description":"The TCP port where NDT should be listening. Should always be 3001 for this check."},"host":{"unit":null,"description":"The IP address or hostname of the target NDT host"}},"name":"NDT Port 3001 Check"},{"internalId":"5","id":"NDT_port_7123","jobType":"nagios.tcp","resultParameterInfo":{"time":{"unit":"seconds","description":"The time it takes to check_tcp to open a connection to the target host and port"}},"serviceParameterInfo":{"port":{"unit":null,"description":"The TCP port where NDT should be listening. Should always be 7123 for this check."},"host":{"unit":null,"description":"The IP address or hostname of the target NDT host"}},"name":"NDT Port 7123 Check"},{"internalId":"6","id":"NPAD_port_8000","jobType":"nagios.tcp","resultParameterInfo":{"time":{"unit":"seconds","description":"The time it takes to check_tcp to open a connection to the target host and port"}},"serviceParameterInfo":{"port":{"unit":null,"description":"The TCP port where NPAD should be listening. Should always be 8000 for this check."},"host":{"unit":null,"description":"The IP address or hostname of the target NPAD host"}},"name":"NPAD Port 8001 Check"},{"internalId":"7","id":"NPAD_port_8001","jobType":"nagios.tcp","resultParameterInfo":{"time":{"unit":"seconds","description":"The time it takes to check_tcp to open a connection to the target host and port"}},"serviceParameterInfo":{"port":{"unit":null,"description":"The TCP port where NPAD should be listening. Should always be 8001 for this check."},"host":{"unit":null,"description":"The IP address or hostname of the target NPAD host"}},"name":"NPAD Port 8001 Check"},{"internalId":"8","id":"owp_861","jobType":"nagios.tcp","resultParameterInfo":{"time":{"unit":"seconds","description":"The time it takes to check_tcp to open a connection to the target host and port"}},"serviceParameterInfo":{"port":{"unit":null,"description":"The TCP port in the target host to check"},"host":{"unit":null,"description":"The IP address or hostname of the target host"}},"name":"Nagios Check TCP"},{"internalId":"9","id":"owp_8569","jobType":"nagios.tcp","resultParameterInfo":{"time":{"unit":"seconds","description":"The time it takes to check_tcp to open a connection to the target host and port"}},"serviceParameterInfo":{"port":{"unit":null,"description":"The TCP port in the target host to check"},"host":{"unit":null,"description":"The IP address or hostname of the target host"}},"name":"Nagios Check TCP"},{"internalId":"10","id":"perfSONAR_pSB","jobType":"nagios.ps.perfSONAR","resultParameterInfo":{},"serviceParameterInfo":{"template":{"unit":null,"description":"A number identifying the request to send to the server."},"host-id":{"unit":null,"description":"The id of the host where this service runs"},"url":{"unit":null,"description":"The URL of the measurement archive to test"}},"name":"perfSONAR PSB Echo Request Test"},{"internalId":"11","id":"latency","jobType":"nagios.ps.owdelay","resultParameterInfo":{"min":{"unit":"pps","description":"The minimum loss observed in the time range"},"max":{"unit":"pps","description":"The maximum loss observed in the time range"},"count":{"unit":null,"description":"The number of tests points returned by query"},"standard_deviation":{"unit":"pps","description":"The standard deviation of the loss observed in the time range"},"average":{"unit":"pps","description":"The average loss observed in the time range"}},"serviceParameterInfo":{"warningThreshold":{"unit":"Gbps","description":"The threshold for throughput values being categorized as warning. Should be in standard nagios format."},"destination-host-id":{"unit":null,"description":"The id of the destination host of this check"},"timeRange":{"unit":"seconds","description":"The number of seconds in the past to query data"},"source":{"unit":null,"description":"The IP address or hostname of the source of this check"},"loss":{"unit":null,"description":"Boolean indicating whether the check should look at loss. Should always be 1 for this check"},"ma-host-id":{"unit":null,"description":"The id of the measurement archive of this check"},"url":{"unit":null,"description":"The URL of the measurement archive"},"source-host-id":{"unit":null,"description":"The id of the source host of this check"},"criticalThreshold":{"unit":"Gbps","description":"The threshold for throughput values being categorized as critical. Should be in standard nagios format."},"destination":{"unit":null,"description":"The IP address or hostname of the destination of this check"}},"name":"OWAMP Loss Check"},{"internalId":"12","id":"throughput","jobType":"nagios.ps.throughput","resultParameterInfo":{"min":{"unit":"Gbps","description":"The minimum bandwidth observed in the time range"},"max":{"unit":"Gbps","description":"The maximum bandwidth observed in the time range"},"count":{"unit":null,"description":"The number of tests points returned by query"},"standard_deviation":{"unit":"Gbps","description":"The standard deviation of bandwidth observed in the time range"},"average":{"unit":"Gbps","description":"The average bandwidth observed in the time range"}},"serviceParameterInfo":{"warningThreshold":{"unit":"Gbps","description":"The threshold for throughput values being categorized as warning. Should be in standard nagios format."},"destination-host-id":{"unit":null,"description":"The id of the destination host of this check"},"timeRange":{"unit":"seconds","description":"The number of seconds in the past to query data"},"source":{"unit":null,"description":"The IP address or hostname of the source of this check"},"ma-host-id":{"unit":null,"description":"The id of the measurement archive of this check"},"url":{"unit":null,"description":"The URL of the measurement archive"},"source-host-id":{"unit":null,"description":"The id of the source host of this check"},"criticalThreshold":{"unit":"Gbps","description":"The threshold for throughput values being categorized as critical. Should be in standard nagios format."},"destination":{"unit":null,"description":"The IP address or hostname of the destination of this check"}},"name":"Throughput Check"},{"internalId":"13","id":"traceroute","jobType":"nagios.ps.traceroute","resultParameterInfo":{"":{"unit":null,"description":"testnumtimesrunaverage"},"pathcountstddev":{"unit":null,"description":"The standard deviation of the number of paths observed"},"pathcountaverage":{"unit":null,"description":"The average number of paths observed"},"testnumtimesrunstddev":{"unit":null,"description":"The standard deviation of the number of tests observed"},"pathcountmax":{"unit":null,"description":"The maximum number of paths observed"},"testnumtimesrunmax":{"unit":null,"description":"The maximum number of tests observed"},"testnumtimesrunaverage":{"unit":null,"description":"The average number of tests observed"},"pathcountmin":{"unit":null,"description":"The minimum number of paths observed"},"testcount":{"unit":null,"description":"The number of tests observed"}},"serviceParameterInfo":{"warningThreshold":{"unit":"Gbps","description":"The threshold for the number of paths being categorized as warning. Should be in standard nagios format."},"destination-host-id":{"unit":null,"description":"The id of the destination host of this check"},"timeRange":{"unit":"seconds","description":"The number of seconds in the past to query data"},"source":{"unit":null,"description":"The IP address or hostname of the source of this check"},"ma-host-id":{"unit":null,"description":"The id of the measurement archive of this check"},"url":{"unit":null,"description":"The URL of the measurement archive"},"source-host-id":{"unit":null,"description":"The id of the source host of this check"},"criticalThreshold":{"unit":"Gbps","description":"The threshold for number of paths being categorized as critical. Should be in standard nagios format."},"destination":{"unit":null,"description":"The IP address or hostname of the destination of this check"}},"name":"Traceroute Check"}] ;
var listOfServiceTypes=[
{
    "internalId":"1",
    "id":"bwctl_port_4823",
    "jobType":"nagios.tcp",
    "resultParameterInfo":{
        "time":{
            "unit":"seconds",
            "description":"The time it takes to check_tcp to open a connection to the target host and port"
        }
    },
"serviceParameterInfo":{
    "port":{
        "unit":null,
        "description":"The TCP port where BWCTL should be listening. Should always be 4823 for this check."
    },
    "host":{
        "unit":null,
        "description":"The IP address or hostname of the target BWCTL host"
    }
},
"name":"BWCTL Port 4823 Check"
},{
    "internalId":"2",
    "id":"bwctl_port_8570",
    "jobType":"nagios.tcp",
    "resultParameterInfo":{
        "time":{
            "unit":"seconds",
            "description":"The time it takes to check_tcp to open a connection to the target host and port"
        }
    },
"serviceParameterInfo":{
    "port":{
        "unit":null,
        "description":"The TCP port where BWCTL should be listening. Should always be 8570 for this check."
    },
    "host":{
        "unit":null,
        "description":"The IP address or hostname of the target BWCTL host"
    }
},
"name":"BWCTL Port 8570 Check"
},{
    "internalId":"4",
    "id":"NDT_port_3001",
    "jobType":"nagios.tcp",
    "resultParameterInfo":{
        "time":{
            "unit":"seconds",
            "description":"The time it takes to check_tcp to open a connection to the target host and port"
        }
    },
"serviceParameterInfo":{
    "port":{
        "unit":null,
        "description":"The TCP port where NDT should be listening. Should always be 3001 for this check."
    },
    "host":{
        "unit":null,
        "description":"The IP address or hostname of the target NDT host"
    }
},
"name":"NDT Port 3001 Check"
},{
    "internalId":"5",
    "id":"NDT_port_7123",
    "jobType":"nagios.tcp",
    "resultParameterInfo":{
        "time":{
            "unit":"seconds",
            "description":"The time it takes to check_tcp to open a connection to the target host and port"
        }
    },
"serviceParameterInfo":{
    "port":{
        "unit":null,
        "description":"The TCP port where NDT should be listening. Should always be 7123 for this check."
    },
    "host":{
        "unit":null,
        "description":"The IP address or hostname of the target NDT host"
    }
},
"name":"NDT Port 7123 Check"
},{
    "internalId":"6",
    "id":"NPAD_port_8000",
    "jobType":"nagios.tcp",
    "resultParameterInfo":{
        "time":{
            "unit":"seconds",
            "description":"The time it takes to check_tcp to open a connection to the target host and port"
        }
    },
"serviceParameterInfo":{
    "port":{
        "unit":null,
        "description":"The TCP port where NPAD should be listening. Should always be 8000 for this check."
    },
    "host":{
        "unit":null,
        "description":"The IP address or hostname of the target NPAD host"
    }
},
"name":"NPAD Port 8001 Check"
},{
    "internalId":"7",
    "id":"NPAD_port_8001",
    "jobType":"nagios.tcp",
    "resultParameterInfo":{
        "time":{
            "unit":"seconds",
            "description":"The time it takes to check_tcp to open a connection to the target host and port"
        }
    },
"serviceParameterInfo":{
    "port":{
        "unit":null,
        "description":"The TCP port where NPAD should be listening. Should always be 8001 for this check."
    },
    "host":{
        "unit":null,
        "description":"The IP address or hostname of the target NPAD host"
    }
},
"name":"NPAD Port 8001 Check"
},{
    "internalId":"8",
    "id":"owp_861",
    "jobType":"nagios.tcp",
    "resultParameterInfo":{
        "time":{
            "unit":"seconds",
            "description":"The time it takes to check_tcp to open a connection to the target host and port"
        }
    },
"serviceParameterInfo":{
    "port":{
        "unit":null,
        "description":"The TCP port in the target host to check"
    },
    "host":{
        "unit":null,
        "description":"The IP address or hostname of the target host"
    }
},
"name":"Nagios Check TCP"
},{
    "internalId":"9",
    "id":"owp_8569",
    "jobType":"nagios.tcp",
    "resultParameterInfo":{
        "time":{
            "unit":"seconds",
            "description":"The time it takes to check_tcp to open a connection to the target host and port"
        }
    },
"serviceParameterInfo":{
    "port":{
        "unit":null,
        "description":"The TCP port in the target host to check"
    },
    "host":{
        "unit":null,
        "description":"The IP address or hostname of the target host"
    }
},
"name":"Nagios Check TCP"
},{
    "internalId":"10",
    "id":"perfSONAR_pSB",
    "jobType":"nagios.ps.perfSONAR",
    "resultParameterInfo":{},
    "serviceParameterInfo":{
        "template":{
            "unit":null,
            "description":"A number identifying the request to send to the server."
        },
        "host-id":{
            "unit":null,
            "description":"The id of the host where this service runs"
        },
        "url":{
            "unit":null,
            "description":"The URL of the measurement archive to test"
        }
    },
"name":"perfSONAR PSB Echo Request Test"
},{
    "internalId":"11",
    "id":"latency",
    "jobType":"nagios.ps.owdelay",
    "resultParameterInfo":{
        "min":{
            "unit":"pps",
            "description":"The minimum loss observed in the time range"
        },
        "max":{
            "unit":"pps",
            "description":"The maximum loss observed in the time range"
        },
        "count":{
            "unit":null,
            "description":"The number of tests points returned by query"
        },
        "standard_deviation":{
            "unit":"pps",
            "description":"The standard deviation of the loss observed in the time range"
        },
        "average":{
            "unit":"pps",
            "description":"The average loss observed in the time range"
        }
    },
"serviceParameterInfo":{
    "warningThreshold":{
        "unit":"Gbps",
        "description":"The threshold for throughput values being categorized as warning. Should be in standard nagios format."
    },
    "destination-host-id":{
        "unit":null,
        "description":"The id of the destination host of this check"
    },
    "timeRange":{
        "unit":"seconds",
        "description":"The number of seconds in the past to query data"
    },
    "source":{
        "unit":null,
        "description":"The IP address or hostname of the source of this check"
    },
    "loss":{
        "unit":null,
        "description":"Boolean indicating whether the check should look at loss. Should always be 1 for this check"
    },
    "ma-host-id":{
        "unit":null,
        "description":"The id of the measurement archive of this check"
    },
    "url":{
        "unit":null,
        "description":"The URL of the measurement archive"
    },
    "source-host-id":{
        "unit":null,
        "description":"The id of the source host of this check"
    },
    "criticalThreshold":{
        "unit":"Gbps",
        "description":"The threshold for throughput values being categorized as critical. Should be in standard nagios format."
    },
    "destination":{
        "unit":null,
        "description":"The IP address or hostname of the destination of this check"
    }
},
"name":"OWAMP Loss Check"
},{
    "internalId":"12",
    "id":"throughput",
    "jobType":"nagios.ps.throughput",
    "resultParameterInfo":{
        "min":{
            "unit":"Gbps",
            "description":"The minimum bandwidth observed in the time range"
        },
        "max":{
            "unit":"Gbps",
            "description":"The maximum bandwidth observed in the time range"
        },
        "count":{
            "unit":null,
            "description":"The number of tests points returned by query"
        },
        "standard_deviation":{
            "unit":"Gbps",
            "description":"The standard deviation of bandwidth observed in the time range"
        },
        "average":{
            "unit":"Gbps",
            "description":"The average bandwidth observed in the time range"
        }
    },
"serviceParameterInfo":{
    "warningThreshold":{
        "unit":"Gbps",
        "description":"The threshold for throughput values being categorized as warning. Should be in standard nagios format."
    },
    "destination-host-id":{
        "unit":null,
        "description":"The id of the destination host of this check"
    },
    "timeRange":{
        "unit":"seconds",
        "description":"The number of seconds in the past to query data"
    },
    "source":{
        "unit":null,
        "description":"The IP address or hostname of the source of this check"
    },
    "ma-host-id":{
        "unit":null,
        "description":"The id of the measurement archive of this check"
    },
    "url":{
        "unit":null,
        "description":"The URL of the measurement archive"
    },
    "source-host-id":{
        "unit":null,
        "description":"The id of the source host of this check"
    },
    "criticalThreshold":{
        "unit":"Gbps",
        "description":"The threshold for throughput values being categorized as critical. Should be in standard nagios format."
    },
    "destination":{
        "unit":null,
        "description":"The IP address or hostname of the destination of this check"
    }
},
"name":"Throughput Check"
},{
    "internalId":"13",
    "id":"traceroute",
    "jobType":"nagios.ps.traceroute",
    "resultParameterInfo":{
        "":{
            "unit":null,
            "description":"testnumtimesrunaverage"
        },
        "pathcountstddev":{
            "unit":null,
            "description":"The standard deviation of the number of paths observed"
        },
        "pathcountaverage":{
            "unit":null,
            "description":"The average number of paths observed"
        },
        "testnumtimesrunstddev":{
            "unit":null,
            "description":"The standard deviation of the number of tests observed"
        },
        "pathcountmax":{
            "unit":null,
            "description":"The maximum number of paths observed"
        },
        "testnumtimesrunmax":{
            "unit":null,
            "description":"The maximum number of tests observed"
        },
        "testnumtimesrunaverage":{
            "unit":null,
            "description":"The average number of tests observed"
        },
        "pathcountmin":{
            "unit":null,
            "description":"The minimum number of paths observed"
        },
        "testcount":{
            "unit":null,
            "description":"The number of tests observed"
        }
    },
"serviceParameterInfo":{
    "warningThreshold":{
        "unit":"Gbps",
        "description":"The threshold for the number of paths being categorized as warning. Should be in standard nagios format."
    },
    "destination-host-id":{
        "unit":null,
        "description":"The id of the destination host of this check"
    },
    "timeRange":{
        "unit":"seconds",
        "description":"The number of seconds in the past to query data"
    },
    "source":{
        "unit":null,
        "description":"The IP address or hostname of the source of this check"
    },
    "ma-host-id":{
        "unit":null,
        "description":"The id of the measurement archive of this check"
    },
    "url":{
        "unit":null,
        "description":"The URL of the measurement archive"
    },
    "source-host-id":{
        "unit":null,
        "description":"The id of the source host of this check"
    },
    "criticalThreshold":{
        "unit":"Gbps",
        "description":"The threshold for number of paths being categorized as critical. Should be in standard nagios format."
    },
    "destination":{
        "unit":null,
        "description":"The IP address or hostname of the destination of this check"
    }
},
"name":"Traceroute Check"
}] ;

function serviceIsLatency(serviceType){
//    if(servieType.id=="CheckLookupService"){
//        return true;
//    }
    if(servieType.id=="owp_861"){
        return true;
    }
    if(servieType.id=="owp_8569"){
        return true;
    }
    if(servieType.id=="perfSONAR_pSB"){
        return true;
    }
    
    return false;
}
function serviceIsThroughput(serviceType){
    
    if(servieType.id=="owp_861"){
        return false;
    }
    if(servieType.id=="owp_8569"){
        return false;
    }
    return true;
}

function serviceIsPrimitive(serviceType){
    
    if(serviceType.id=="throughput"){
        return false;
    }
    if(serviceType.id=="latency"){
        return false;
    }
    if(serviceType.id=="traceroute"){
        return false;
    }
    return true;
}