Crud Operations on Hosts
========================

As I said previously all dashboard operations are performed by issuing web
requests which contain in the url the command and parameters.

There are four types of web requests: GET, POST, PUT and DELETE. We use them
for different purposes. Let me explain their roles by showing how can we
manipulate hosts.

(How exactly you performa those requests in java, python, perl etc you need to
refer to manuals for those languages. In this tutorial I only explain the
general idea how API works).


1. GET request. 
-----------------

We use it to query host information. You simply load web page

    {url}/PsApi.HOSTS
    

which can, for example, look like:

    http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts

and it will return a JSONArray of JSONObjects representing low detail level information of hosts.


Example: you can perform this request by hand as a shell command:

    curl -i -X GET -H "Content-Type: application/json"
    http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts_rest/


It will return something which looks like this:

    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Date: Fri, 05 Oct 2012 18:43:02 GMT
    Content-Length: 278
    Proxy-Connection: Keep-Alive
    Connection: Keep-Alive
    Age: 0

    [{"id":"1","hostname":"lhcmon.bnl.gov"},{"id":"2","hostname":"lhcperfmon.bnl.gov"}, ...]

The latter is a JSONArray of host abbreviated objects.

If you would like to obtain information about a particular host you need to do

    {url}/PsApi.HOSTS/{hostId}

where instead of {hostId} you put infor about a particular host, for example:
    http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/1

or, using the curl command:

    curl -i -X GET -H "Content-Type: application/json"  http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/33025
    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: text/html;charset=UTF-8
    Date: Thu, 10 Jan 2013 18:47:47 GMT
    Content-Length: 127
    Proxy-Connection: Keep-Alive
    Connection: Keep-Alive
    Age: 0

     {"ipv6":null,"id":"1","services":[{"id":"1","result":{"message":"0.005 second response time on port 4823","id":"6","job-id":"0","time":"2013-03-04T15:15:32.000Z","status":0,"service-id":"1","parameters":{"time":"0.005343s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H lhcmon.bnl.gov -p 4823"},"service_result_id":"0"},"description":"BWCTL Port 4823 Check","name":"bwctl_port_4823_on_lhcmon.bnl.gov","runningSince":"2013-03-04T15:18:06.911Z","nextCheckTime":"2013-03-04T15:35:32.000Z","checkInterval":1200,"parameters":{"port":4823,"host":"lhcmon.bnl.gov","host-id":"1"},"prevCheckTime":"2013-03-04T15:15:32.000Z","type":"bwctl_port_4823","running":false,"timeout":60},{"id":"2","result":{"message":"0.001 second response time on port 4823","id":"10","job-id":"0","time":"2013-03-04T15:14:32.000Z","status":0,"service-id":"2","parameters":{"time":"0.001319s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H lhcmon.bnl.gov -p 4823"},"service_result_id":"0"},"description":"BWCTL Port 8570 Check","name":"bwctl_port_8570_on_lhcmon.bnl.gov","runningSince":"2013-03-04T15:18:06.911Z","nextCheckTime":"2013-03-04T15:34:32.000Z","checkInterval":1200,"parameters":{"port":4823,"host":"lhcmon.bnl.gov","host-id":"1"},"prevCheckTime":"2013-03-04T15:14:32.000Z","type":"bwctl_port_8570","running":false,"timeout":60},{"id":"3","result":{"message":"perfSONAR response is unreadable.","id":"15","job-id":"0","time":"2013-03-04T15:09:33.000Z","status":3,"service-id":"3","parameters":{},"service_result_id":"0"},"description":"perfSONAR Lookup Service Test","name":"CheckLookupService_on_lhcmon.bnl.gov","runningSince":"2013-03-04T15:18:06.911Z","nextCheckTime":"2013-03-04T15:29:33.000Z","checkInterval":1200,"parameters":{"port":9995,"host":"lhcmon.bnl.gov","host-id":"1","url":"http:\/\/lhcmon.bnl.gov:9995\/perfSONAR_PS\/services\/hLS"},"prevCheckTime":"2013-03-04T15:09:33.000Z","type":"CheckLookupService","running":false,"timeout":60},{"id":"4","result":{"message":"0.002 second response time on port 3001","id":"3","job-id":"0","time":"2013-03-04T15:11:31.000Z","status":0,"service-id":"4","parameters":{"time":"0.001706s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H lhcmon.bnl.gov -p 3001"},"service_result_id":"0"},"description":"NDT Port 3001 Check","name":"NDT_port_3001_on_lhcmon.bnl.gov","runningSince":"2013-03-04T15:18:06.911Z","nextCheckTime":"2013-03-04T15:31:31.000Z","checkInterval":1200,"parameters":{"port":3001,"host":"lhcmon.bnl.gov","host-id":"1"},"prevCheckTime":"2013-03-04T15:11:31.000Z","type":"NDT_port_3001","running":false,"timeout":60},{"id":"5","result":{"message":"0.001 second response time on port 7123","id":"14","job-id":"0","time":"2013-03-04T15:12:31.000Z","status":0,"service-id":"5","parameters":{"time":"0.001427s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H lhcmon.bnl.gov -p 7123"},"service_result_id":"0"},"description":"NDT Port 7123 Check","name":"NDT_port_7123_on_lhcmon.bnl.gov","runningSince":"2013-03-04T15:18:06.911Z","nextCheckTime":"2013-03-04T15:32:31.000Z","checkInterval":1200,"parameters":{"port":7123,"host":"lhcmon.bnl.gov","host-id":"1"},"prevCheckTime":"2013-03-04T15:12:31.000Z","type":"NDT_port_7123","running":false,"timeout":60},{"id":"6","result":{"message":"0.002 second response time on port 8000","id":"9","job-id":"0","time":"2013-03-04T15:12:32.000Z","status":0,"service-id":"6","parameters":{"time":"0.001551s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H lhcmon.bnl.gov -p 8000"},"service_result_id":"0"},"description":"NPAD Port 8001 Check","name":"NPAD_port_8000_on_lhcmon.bnl.gov","runningSince":"2013-03-04T15:18:06.911Z","nextCheckTime":"2013-03-04T15:32:32.000Z","checkInterval":1200,"parameters":{"port":8000,"host":"lhcmon.bnl.gov","host-id":"1"},"prevCheckTime":"2013-03-04T15:12:32.000Z","type":"NPAD_port_8000","running":false,"timeout":60},{"id":"7","result":{"message":"0.001 second response time on port 8001","id":"13","job-id":"0","time":"2013-03-04T15:09:31.000Z","status":0,"service-id":"7","parameters":{"time":"0.001459s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H lhcmon.bnl.gov -p 8001"},"service_result_id":"0"},"description":"NPAD Port 8001 Check","name":"NPAD_port_8001_on_lhcmon.bnl.gov","runningSince":"2013-03-04T15:18:06.911Z","nextCheckTime":"2013-03-04T15:29:31.000Z","checkInterval":1200,"parameters":{"port":8001,"host":"lhcmon.bnl.gov","host-id":"1"},"prevCheckTime":"2013-03-04T15:09:31.000Z","type":"NPAD_port_8001","running":false,"timeout":60},{"id":"8","result":{"message":"perfSONAR service replied \"The echo request has passed.\"","id":"31","job-id":"0","time":"2013-03-04T15:10:33.000Z","status":0,"service-id":"8","parameters":{},"service_result_id":"0"},"description":"perfSONAR PSB Echo Request Test","name":"perfSONAR_pSB_on_lhcmon.bnl.gov","runningSince":"2013-03-04T15:18:06.911Z","nextCheckTime":"2013-03-04T15:30:33.000Z","checkInterval":1200,"parameters":{"template":"2","host":"lhcmon.bnl.gov","host-id":"1","url":"http:\/\/192.12.15.23:8085\/perfSONAR_PS\/services\/pSB"},"prevCheckTime":"2013-03-04T15:10:33.000Z","type":"perfSONAR_pSB","running":false,"timeout":60}],"ipv4":"192.12.15.23","hostname":"lhcmon.bnl.gov"} 
     
     
So now you know how to query the hosts. Let us now create new hosts


2. POST requests.
-------------------

To create a new host you use POST request.

First of all you have to create your JSON object of the host you want to
create. You know some details about your host, for example: it should have name
abc.host.com, ipv4=999.999.99.99 and no ipv6. You have to build a JSON object
for that host. To know how a JSON object of a host should look like you can GET
information about any of the existing hosts and use the JSONObject as a
template.

Your JSONObject for your new host will then look like:

    {"ipv6":null,"ipv4":"999.999.99.99","hostname":"abc.host.com"}

Please note that this host has no id yet - the id will be assigned to it
internally, once it is created by the datastore.

Now you have to perform a POST request to address

    {url}/PsApi.HOST
    
    or
    
    http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts

with the string representation of the JSONObject located in data part of your request. How exactly this is done depends on your programming language. In shell this would look like:


    curl -i -X POST -H "Content-Type: application/json"
    -d'{"ipv6":null,"ipv4":"999.999.99.99","hostname":"abc.host.com"}'
    http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts

if you execute it you will get

    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: text/html;charset=UTF-8
    Date: Thu, 10 Jan 2013 18:54:42 GMT
    Content-Length: 91
    Proxy-Connection: Keep-Alive
    Connection: Keep-Alive

    {"ipv6":null,"id":"34384","services":[],"ipv4":"999.999.99.99","hostname":"abc.host.com"}

you get back JSOBObject of your host - please note that it has been assigned a
unique id. From now on each time you refer to this host you will have to use
this id.

3. PUT request. 
-----------------

PUT request is used to modify the host information.


Suppose you want to change host name. First you build JSONObject corresponding
to your host (you may obtain is using GET request). Then you modify the
"hostname" field in this object. Finally you make a POST request

    {url}/PsApi.HOSTS
    
    or
    
     http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/34384

with the string representation of JSOBobject of your host in the data part. 


For example, to change the name of the host from previous example to xxx.zzz.com you do

    curl -i -X PUT -H "Content-Type: application/json" -d'{"id":"34384","ipv6":null,"ipv4":"999.999.99.99","hostname":"xxx.zzz.com"}' http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/34384

you get

    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: text/html;charset=UTF-8
    Date: Thu, 10 Jan 2013 19:03:22 GMT
    Content-Length: 90
    Proxy-Connection: Keep-Alive
    Connection: Keep-Alive

    {"ipv6":null,"id":"34384","services":[],"ipv4":"999.999.99.99","hostname":"xxx.zzz.com"}


you can now use GET request described earlied to see that hostname of host 34384 has indeed been changed.



4. DELETE request. 
-------------------

Yes, you guessed it: we use it to delete hosts.

To delete host with a given id {hostId} you make a DELETE request:

    {url}/PsApi.HOSTS/{hostId}

a concrete example would be:

    curl -i -X DELETE -H "Content-Type: application/json"  http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/34384
    
    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: text/html;charset=UTF-8
    Date: Thu, 10 Jan 2013 19:12:46 GMT
    Content-Length: 20
    Proxy-Connection: Keep-Alive
    Connection: Keep-Alive

    host 34384 deleted

and that is all. You can verify using GET request that the host 34384 is gone.


As you can now imagine: we can perform CRUD operations on other objects, like
sites, clouds, marices and services in a similar manner. But I need to warn
you: for those objects things are similar to hosts, however there are some
important differences. I will describe them in the subsequent manuals. 

