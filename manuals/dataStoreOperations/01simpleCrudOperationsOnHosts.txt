As I said previously all dashboard operations are performed by issuing web requests which contain in the url the command and parameters.

There are four types of web requests: GET, POST, PUT and DELETE. We use them for different purposes. Let me explain their roles by showing how can we manipulate hosts.

(How exactly you performa those requests in java, python, perl etc you need to refer to manuals for those languages. In this tutorial I only explain the general idea how API works).


1. Get request. We use it to query host information. You simply load web page

{url}/PsApi.HOSTS

which can, for example, look like:

http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts

and it will return a JSONArray of host id's. 


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

["1349461278462.661314","1349461278465.579684","1349461278465.526829","1349461278465.440649","1349461278466.950077","1349461278466.269199","1349461278466.18808","1349461278466.788238","1349461278466.252016","1349461278466.223642","1349461278467.289606","1349461278467.413121"]

The latter is a JSONArray of host id's.


If you would like to obtain information about a particular host you need to do

{url}/PsApi.HOSTS/{hostId}

where instead of {hostId} you put infor about a particular host, for example:


 http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/33025

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

{"ipv6":null,"id":"33025","services":["33026","33027","33028","33029"],"ipv4":"192.41.236.31","hostname":"psmsu01.aglt2.org"}
===============================================================================================

So now you know how to query the hosts. Let us now create new hosts


2. POST requests.

To create a new host you use POST request.

First of all you have to create your JSON object of the host you want to
create. You know some details about your host, for example: it should have name abc.host.com, ipv4=999.999.99.99 and no ipv6. You have to build a JSON object for that host. To know how a JSON object of a host should look like you can GET information about any of the existing hosts and use the JSONObject as a template.

Your JSONObject for your new host will then look like:

{"ipv6":null,"ipv4":"999.999.99.99","hostname":"abc.host.com"}

Please note that this host has no id yet - the id will be assigned to it internally, once it is created by the datastore.

Now you have to perform a POST request to address

{url}/PsApi.HOST

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

you get back JSOBObject of your host - please note that it has been assigned a unique id. From now on each time you refer to this host you will have to use this id.


==========================================================================================

3. PUT request. PUT request is used to modify the host information.


Suppose you want to change host name. First you build JSONObject corresponding to your host (you may obtain is using GET request). Then you modify the "hostname" field in this object. Finally you make a POST request

{url}/PsApi.HOSTS

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

=========================================================================


4. DELETE request. Yes, you guessed it: we use it to delete hosts.


To delete host with a given id {hostId} you make a DELETE request:

{url}/PsApi.HOSTS/{hostId}

a concrete example would be:

curl -i -X DELETE -H "Content-Type: application/json"  http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/34384HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: text/html;charset=UTF-8
Date: Thu, 10 Jan 2013 19:12:46 GMT
Content-Length: 20
Proxy-Connection: Keep-Alive
Connection: Keep-Alive

host 34384 deleted

and that is all. You can verify using GET request that the host 34384 is gone.


As you can now imagine: we can perform CRUD operations on other objects, like sites, clouds, marices and services in a similar manner. But I need to warn you: for those objects things are similar to hosts, however there are some important differences. I will describe them in the subsequent manuals. 

