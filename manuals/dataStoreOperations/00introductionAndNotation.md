Introuction and Notation
======================

This manual explains how to perform simple CRUD (create, update, delete)
operations on hosts objects in data store. But before we start let me describe
our convention.

All commands to the datastore are issued by performing web requests. All requests have the form:

    {url}/{command}

where {url} is the http address of the server on which the datastore instance is located (for example pfdatastore-itb.grid.iu.edu:8080/dashboard-1.0-SNAPSHOT/) and the {command} is the command to be exacuted.

The available commands are described in the API deesign document which can be seen here:

https://docs.google.com/document/d/10kfu7Bhee7SEKQTgUyr1WOwUaAlxGzGHnMh5CqSoJNQ/edit

for the collector API and here:

https://docs.google.com/document/d/1NnVNF6TKnTIZkL9BQNyRlqX9dNXH1K-62Ax9rFnZvKE/edit

for the data access API.


For example: to obtain list of host id's known to the datastore instance

    perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/

you should perform the request:

    http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts

you will be returned a string representation of JSONArray of host Id's which may look like this:

    ["33025","33030","33039","33044","33054","33059","33069","33074","33083","33088",
     "33097","33102","33118","33119","33120","33121","33122","33123","33124","33125",
     "33126","33127","33128","33129","33130","33131","33132","33133","33134","33135",
     "33136","33137"] 

If you want to know more about host with id 33025 you perform request:

   http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/33025

and you will be returned striing representation of JSONObject corresponding to host 33025:


    {
     "ipv6":null,
     "id":"33025",
     "services":["33026","33027","33028","33029"],
     "ipv4":"192.41.236.31",
     "hostname":"psmsu01.aglt2.org"
    } 


However it is not a good practice to include and explicit command "hosts" in
the code, as a string. For that reason I have created a static java class PsApi
which contains, as static public variables the codes for all API commands. The
PsApi class can be found in the datastore code.

From now on I will use the notation:

    {url}/PsApi.HOST

to denote the "host" command on a particular datastore instance.

The PsApi class is used by the GUI code when building url requests to contact
the datastore. In the future we may need to put this class in some common
package which is accessible both by gui code and datastore - or find some
other, alternative, way to share the command information between gui and
datastore and collector modules.
