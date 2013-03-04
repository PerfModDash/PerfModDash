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


[{"id":"1","hostname":"lhcmon.bnl.gov"},{"id":"2","hostname":"lhcperfmon.bnl.gov"},
{"id":"3","hostname":"psmsu01.aglt2.org"},{"id":"4","hostname":"psmsu02.aglt2.org"},
{"id":"5","hostname":"psum01.aglt2.org"},{"id":"6","hostname":"psum02.aglt2.org"},
{"id":"7","hostname":"iut2-net1.iu.edu"},{"id":"8","hostname":"iut2-net2.iu.edu"},
{"id":"9","hostname":"uct2-net1.uchicago.edu"},{"id":"10","hostname":"uct2-net2.uchicago"},
{"id":"11","hostname":"mwt2-ps01.campuscluster.illinois.edu"},
{"id":"12","hostname":"mwt2-ps02.campuscluster.illinois.edu"},
{"id":"13","hostname":"atlas-npt1.bu.edu"},{"id":"14","hostname":"atlas-npt2.bu.edu"},
{"id":"15","hostname":"ps1.ochep.ou.edu"},{"id":"16","hostname":"ps2.ochep.ou.edu"},
{"id":"17","hostname":"netmon1.atlas-swt2.org"},{"id":"18","hostname":"netmon2.atlas-swt2.org"},
{"id":"19","hostname":"psnr-lat01.slac.stanford.edu"},{"id":"20","hostname":"psnr-bw01.slac.stanford.edu"}] 

   This list contains list of JSON objects which contain abbreviated information about hosts: each JSON object 
   contains host ID and host name.

If you want to know more about host with id 33025 you perform request:

   http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/3

and you will be returned striing representation of JSONObject corresponding to host 33025:


   {"ipv6":null,"id":"3","services":[{"id":"13","result":{"message":"perfSONAR response is unreadable.",
   "id":"33","job-id":"0","time":"2013-03-04T14:56:28.000Z","status":3,"service-id":"13","parameters":{},
   "service_result_id":"0"},"description":"perfSONAR Lookup Service Test",
   "name":"CheckLookupService_on_psmsu01.aglt2.org","runningSince":"2013-03-04T15:04:00.403Z",
   "nextCheckTime":"2013-03-04T15:16:28.000Z","checkInterval":1200,
   "parameters":{"port":9995,"host":"psmsu01.aglt2.org","host-id":"3","url":"http:\/\/psmsu01.aglt2.org:9995\/perfSONAR_PS\/services\/hLS"},
   "prevCheckTime":"2013-03-04T14:56:28.000Z","type":"CheckLookupService",
   "running":false,"timeout":60},{"id":"14","result":{"message":"0.035 second response time on port 861",
   "id":"17","job-id":"0","time":"2013-03-04T14:53:25.000Z","status":0,"service-id":"14",
   "parameters":{"time":"0.035094s","command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H psmsu01.aglt2.org -p 861"},
   "service_result_id":"0"},"description":"Nagios Check TCP","name":"owp_861_on_psmsu01.aglt2.org",
   "runningSince":"2013-03-04T15:04:00.403Z","nextCheckTime":"2013-03-04T15:13:25.000Z","checkInterval":1200,
   "parameters":{"port":861,"host":"psmsu01.aglt2.org","host-id":"3"},"prevCheckTime":"2013-03-04T14:53:25.000Z",
   "type":"owp_861","running":false,"timeout":60},{"id":"15",
   "result":{"message":"0.035 second response time on port 8569","id":"5","job-id":"0",
   "time":"2013-03-04T14:50:25.000Z","status":0,"service-id":"15","parameters":{"time":"0.035170s",
   "command":"\/usr\/lib\/nagios\/plugins\/check_tcp -H psmsu01.aglt2.org -p 8569"},"service_result_id":"0"},
   "description":"Nagios Check TCP","name":"owp_8569_on_psmsu01.aglt2.org","runningSince":"2013-03-04T15:04:00.403Z","nextCheckTime":
   "2013-03-04T15:10:25.000Z","checkInterval":1200,"parameters":{"port":8569,"host":"psmsu01.aglt2.org","host-id":"3"},
   "prevCheckTime":"2013-03-04T14:50:25.000Z","type":"owp_8569","running":false,"timeout":60},{"id":"16",
   "result":{"message":"perfSONAR service replied \"The echo request has passed.\"","id":"29","job-id":"0",
   "time":"2013-03-04T14:57:27.000Z","status":0,"service-id":"16","parameters":{},"service_result_id":"0"},
   "description":"perfSONAR PSB Echo Request Test","name":"perfSONAR_pSB_on_psmsu01.aglt2.org",
   "runningSince":"2013-03-04T15:04:00.403Z","nextCheckTime":"2013-03-04T15:17:27.000Z","checkInterval":1200,
   "parameters":{"template":"2","host":"psmsu01.aglt2.org","host-id":"3",
   "url":"http:\/\/192.41.236.31:8085\/perfSONAR_PS\/services\/pSB"},"prevCheckTime":"2013-03-04T14:57:27.000Z",
   "type":"perfSONAR_pSB","running":false,"timeout":60}],"ipv4":"192.41.236.31","hostname":"psmsu01.aglt2.org"} 


A note about amount of information presented in the JSONObjects.


The amount information included in the object JSON id determined by detailLevel parameter. 
The detailLevel can be: low, medium or high. Try out the varous values of the parameters:

http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/3?detailLevel=low

http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/3?detailLevel=medium

http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/3?detailLevel=high


and you will see what effect does it have.


When the detail level is low the datastore returns only id and name of the object. If it is medium then it returns
more information but for the component objects returns only id and name. When detail level is high it returns 
all information foe the object and for its componemt objects. For some objects, like matrices
the high detail level can lead to quite a big output.


What is the default value for detailLevel parametter? It depends. If you ask information about a particular obejct,
for example a particular host then the detailLevel is set to high. Try this:

http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts/3

and you will get full information about host with id=3.


On the other hand if you are asking for a list of hosts:


http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/hosts


you will return only low detail level information for each hosts. You can overrdide the default detailLevel.



That completes the discussion about detailLevel parameter. Let us go back to description of operations.


It is not a good practice to include and explicit command "hosts" in
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
