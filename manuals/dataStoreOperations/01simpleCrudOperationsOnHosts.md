Crud Operations on Hosts
========================

As I said previously all dashboard operations are performed by issuing web
requests which contain in the url the command and parameters.

There are four types of web requests: GET, POST, PUT and DELETE. We use them
for different purposes. Let me explain their roles by showing how can we
manipulate hosts.

(For how exactly you perform those requests in java, python, perl, etc., you need to
refer to manuals for those languages. In this tutorial I only explain the
general idea how the API works).


1. GET request. 
-----------------

We use it to query host information. You simply load the web page

    {url}/PsApi.HOSTS
    

which can, for example, look like:

    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts

and it will return a JSONArray of JSONObjects representing low detail level information of hosts.


Example: you can perform this request by hand as a shell command:

    curl -i -X GET -H "Content-Type: application/json"
    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts


It will return something which looks like this:

    [{"id":"23","hostname":"psum01.aglt2.org"},{"id":"24","hostname":"psmsu01.aglt2.org"}, ...]

The latter is a JSONArray of host abbreviated objects.

If you would like to obtain information about a particular host you need to do

    {url}/PsApi.HOSTS/{hostId}

where instead of {hostId} you put a particular host ID; for example:
    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/24

or, using the curl command:

    curl -i -X GET -H "Content-Type: application/json"  http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/24
    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: text/html;charset=UTF-8
    Content-Length: 5472
    Date: Mon, 10 Jun 2013 20:40:24 GMT

    {"ipv6":null,"id":"24","services":[{"id":"844","result":{"message":"0.038 second response time on port 8569","id":"805","job-id":"0","time":"2013-06-10T20:24:31.000Z","status":0,"service-id":"844","parameters":{"time":"0.037988s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H psmsu01.aglt2.org -p 8569"},"service_result_id":"0"},"description":"Nagios Check TCP","name":"owp_8569_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:44:31.000Z","checkInterval":1200,"parameters":{"port":8569,"host":"psmsu01.aglt2.org","host-id":"24"},"prevCheckTime":"2013-06-10T20:24:31.000Z","type":"owp_8569","running":false,"timeout":60},{"id":"843","result":{"message":"0.035 second response time on port 861","id":"859","job-id":"0","time":"2013-06-10T20:24:32.000Z","status":0,"service-id":"843","parameters":{"time":"0.034782s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H psmsu01.aglt2.org -p 861"},"service_result_id":"0"},"description":"Nagios Check TCP","name":"owp_861_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:44:32.000Z","checkInterval":1200,"parameters":{"port":861,"host":"psmsu01.aglt2.org","host-id":"24"},"prevCheckTime":"2013-06-10T20:24:32.000Z","type":"owp_861","running":false,"timeout":60},{"id":"842","result":{"message":"Unknown return status 22 from command. Verify that it is a valid Nagios plug-in","id":"862","job-id":"0","time":"2013-06-10T20:27:36.000Z","status":3,"service-id":"842","parameters":{"command":"\/opt\/perfsonar_ps\/nagios\/bin\/check_perfSONAR.pl  http:\/\/null:8085\/perfSONAR_PS\/services\/pSB -t 2"},"service_result_id":"0"},"description":"perfSONAR PSB Echo Request Test","name":"perfSONAR_pSB_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:47:36.000Z","checkInterval":1200,"parameters":{"template":"2","host":"psmsu01.aglt2.org","host-id":"24","url":"http:\/\/null:8085\/perfSONAR_PS\/services\/pSB"},"prevCheckTime":"2013-06-10T20:27:36.000Z","type":"perfSONAR_pSB","running":false,"timeout":60},{"id":"841","result":{"message":"Connection refused","id":"827","job-id":"0","time":"2013-06-10T20:24:31.000Z","status":2,"service-id":"841","parameters":{},"service_result_id":"0"},"description":"NPAD Port 8001 Check","name":"NPAD_port_8001_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:44:31.000Z","checkInterval":1200,"parameters":{"port":8001,"host":"psmsu01.aglt2.org","host-id":"24"},"prevCheckTime":"2013-06-10T20:24:31.000Z","type":"NPAD_port_8001","running":false,"timeout":60},{"id":"840","result":{"message":"Connection refused","id":"821","job-id":"0","time":"2013-06-10T20:26:31.000Z","status":2,"service-id":"840","parameters":{},"service_result_id":"0"},"description":"NPAD Port 8001 Check","name":"NPAD_port_8000_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:46:31.000Z","checkInterval":1200,"parameters":{"port":8000,"host":"psmsu01.aglt2.org","host-id":"24"},"prevCheckTime":"2013-06-10T20:26:31.000Z","type":"NPAD_port_8000","running":false,"timeout":60},{"id":"839","result":{"message":"Connection refused","id":"858","job-id":"0","time":"2013-06-10T20:24:32.000Z","status":2,"service-id":"839","parameters":{},"service_result_id":"0"},"description":"NDT Port 7123 Check","name":"NDT_port_7123_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:44:32.000Z","checkInterval":1200,"parameters":{"port":7123,"host":"psmsu01.aglt2.org","host-id":"24"},"prevCheckTime":"2013-06-10T20:24:32.000Z","type":"NDT_port_7123","running":false,"timeout":60},{"id":"838","result":{"message":"0.034 second response time on port 3001","id":"865","job-id":"0","time":"2013-06-10T20:24:32.000Z","status":0,"service-id":"838","parameters":{"time":"0.034466s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H psmsu01.aglt2.org -p 3001"},"service_result_id":"0"},"description":"NDT Port 3001 Check","name":"NDT_port_3001_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:44:32.000Z","checkInterval":1200,"parameters":{"port":3001,"host":"psmsu01.aglt2.org","host-id":"24"},"prevCheckTime":"2013-06-10T20:24:32.000Z","type":"NDT_port_3001","running":false,"timeout":60},{"id":"836","result":{"message":"Connection refused","id":"854","job-id":"0","time":"2013-06-10T20:24:32.000Z","status":2,"service-id":"836","parameters":{},"service_result_id":"0"},"description":"BWCTL Port 8570 Check","name":"bwctl_port_8570_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:44:32.000Z","checkInterval":1200,"parameters":{"port":4823,"host":"psmsu01.aglt2.org","host-id":"24"},"prevCheckTime":"2013-06-10T20:24:32.000Z","type":"bwctl_port_8570","running":false,"timeout":60},{"id":"835","result":{"message":"Connection refused","id":"861","job-id":"0","time":"2013-06-10T20:24:32.000Z","status":2,"service-id":"835","parameters":{},"service_result_id":"0"},"description":"BWCTL Port 4823 Check","name":"bwctl_port_4823_on_psmsu01.aglt2.org","runningSince":"2013-06-10T20:40:24.974Z","nextCheckTime":"2013-06-10T20:44:32.000Z","checkInterval":1200,"parameters":{"port":4823,"host":"psmsu01.aglt2.org","host-id":"24"},"prevCheckTime":"2013-06-10T20:24:32.000Z","type":"bwctl_port_4823","running":false,"timeout":60}],"ipv4":null,"hostname":"psmsu01.aglt2.org"}
     
So now you know how to query the hosts. Let us now create new hosts


2. POST requests.
-------------------

To create a new host you use the POST request.

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
    
    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts

with the string representation of the JSONObject located in the data part of your request.
How exactly this is done depends on your programming language. From the shell this would look like:


    curl -i -X POST -H "Content-Type: application/json"
    -d'{"ipv6":null,"ipv4":"999.999.99.99","hostname":"abc.host.com"}'
    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts

If you execute it you will get

    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: text/html;charset=UTF-8
    Date: Thu, 10 Jan 2013 18:54:42 GMT
    Content-Length: 91
    Proxy-Connection: Keep-Alive
    Connection: Keep-Alive

    {"ipv6":null,"id":"34384","services":[],"ipv4":"999.999.99.99","hostname":"abc.host.com"}

You get back the JSOBObject of your host. Please note that it has been assigned a
unique id. From now on each time you refer to this host you will have to use
this id.

3. PUT request. 
-----------------

The PUT request is used to modify the host information.


Suppose you want to change host name. First you build JSONObject corresponding
to your host (you may obtain it using the GET request). Then you modify the
"hostname" field in this object. Finally you make a POST request

    {url}/PsApi.HOSTS
    
    or
    
     http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/34384

with the string representation of JSOBobject of your host in the data part. 


For example, to change the name of the host from previous example to xxx.zzz.com you do

    curl -i -X PUT -H "Content-Type: application/json" -d'{"id":"34384","ipv6":null,"ipv4":"999.999.99.99","hostname":"xxx.zzz.com"}' http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/34384

you get

    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: text/html;charset=UTF-8
    Date: Thu, 10 Jan 2013 19:03:22 GMT
    Content-Length: 90
    Proxy-Connection: Keep-Alive
    Connection: Keep-Alive

    {"ipv6":null,"id":"34384","services":[],"ipv4":"999.999.99.99","hostname":"xxx.zzz.com"}


You can now use the GET request described earlier to see that hostname of host 34384 has indeed been changed.



4. DELETE request. 
-------------------

Yes, you guessed it: we use it to delete hosts.

To delete host with a given id {hostId} you make a DELETE request:

    {url}/PsApi.HOSTS/{hostId}

A concrete example would be:

    curl -i -X DELETE -H "Content-Type: application/json"  http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/34384
    
    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: text/html;charset=UTF-8
    Date: Thu, 10 Jan 2013 19:12:46 GMT
    Content-Length: 20
    Proxy-Connection: Keep-Alive
    Connection: Keep-Alive

    host 34384 deleted

And that is all. You can verify using the GET request that the host 34384 is gone.


As you can now imagine we can perform CRUD operations on other objects like
sites, clouds, matrices, and services in a similar manner. But I need to warn
you: for those objects things are similar to hosts, however there are some
important differences. I will describe them in the subsequent manuals. 

