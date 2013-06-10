Introduction and Notation
======================

This manual explains how to perform simple CRUD (create, update, delete)
operations on host objects in data store. But before we start let me describe
our convention.

All commands to the datastore are issued by performing web requests. All requests have the form:

    {url}/{command}

where {url} is the http address of the server on which the datastore instance is located
(for example pfdatastore-itb.grid.iu.edu:8080/dashboard2-1.0-SNAPSHOT/) and {command} is the
command to be executed.

The available commands are described in the API design document which can be seen here:

    https://docs.google.com/document/d/10kfu7Bhee7SEKQTgUyr1WOwUaAlxGzGHnMh5CqSoJNQ/edit

for the collector API and here:

    https://docs.google.com/document/d/1NnVNF6TKnTIZkL9BQNyRlqX9dNXH1K-62Ax9rFnZvKE/edit

for the data access API.


### Examples ###
To obtain list of host id's known to the datastore instance

    perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/

you should perform the request:

    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts

you will be returned a string representation of JSONArray of host Id's which may look like this:


    [{"id":"23","hostname":"psum01.aglt2.org"},{"id":"24","hostname":"psmsu01.aglt2.org"},
    {"id":"25","hostname":"uct2-net1.uchicago.edu"},{"id":"26","hostname":"iut2-net1.iu.edu"},
    {"id":"27","hostname":"mwt2-ps01.campuscluster.illinois.edu"},{"id":"28","hostname":"netmon1.atlas-swt2.org"},
    {"id":"29","hostname":"ps1.ochep.ou.edu"},{"id":"30","hostname":"atlas-owamp.bu.edu"},
    {"id":"31","hostname":"psndt2.accre.vanderbilt.edu"},{"id":"32","hostname":"perfsonar02.cmsaf.mit.edu"},
    {"id":"33","hostname":"perfsonar01.cmsaf.mit.edu"},{"id":"34","hostname":"hcc-ps02.unl.edu"},
    {"id":"35","hostname":"hcc-ps01.unl.edu"},{"id":"36","hostname":"perfsonar02.hep.wisc.edu"},
    {"id":"37","hostname":"perfsonar01.hep.wisc.edu"},{"id":"38","hostname":"perfsonar-bw.sprace.org.br"},
    {"id":"39","hostname":"perfsonar-lt.sprace.org.br"},{"id":"40","hostname":"mannperf2.itns.purdue.edu"},
    {"id":"41","hostname":"mannperf.itns.purdue.edu"},{"id":"42","hostname":"perfsonar-2.t2.ucsd.edu"},
    {"id":"43","hostname":"perfsonar2.ultralight.org"},{"id":"44","hostname":"perfsonar-bw.grid.iu.edu"},
    {"id":"45","hostname":"perfsonar-lt.grid.iu.edu"},{"id":"46","hostname":"AAAAANewHost.com"}] 

This list contains list of JSON objects which contain abbreviated information about hosts: each JSON object 
contains host ID and host name.

If you want to know more about host with id 37 you perform request:

    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/37

and you will be returned string representation of JSONObject corresponding to host 37:


    {"ipv6":null,"id":"37","services":[{"id":"806","result":{"message":"Unknown return status 22 from command. Verify
    that it is a valid Nagios plug-in","id":"819","job-id":"0","time":"2013-06-10T18:44:00.000Z","status":3,"service-id":
    "806","parameters":{"command":"\/opt\/perfsonar_ps\/nagios\/bin\/check_perfSONAR.pl 
    http:\/\/null:8085\/perfSONAR_PS\/services\/pSB -t 2"},"service_result_id":"0"},"description":
    "perfSONAR PSB Echo Request Test","name":"perfSONAR_pSB_on_perfsonar01.hep.wisc.edu","runningSince":
    "2013-06-10T18:49:59.860Z","nextCheckTime":"2013-06-10T19:04:00.000Z","checkInterval":1200,"parameters":
    {"template":"2","host":"perfsonar01.hep.wisc.edu","host-id":"37","url":"http:\/\/null:8085\/perfSONAR_PS\/services\/pSB"},
    "prevCheckTime":"2013-06-10T18:44:00.000Z","type":"perfSONAR_pSB","running":false,"timeout":60},{"id":"805",
    "result":{"message":"0.037 second response time on port 8569","id":"772","job-id":"0","time":"2013-06-10T18:43:59.000Z",
    "status":0,"service-id":"805","parameters":{"time":"0.036898s","command":
    "\/usr\/lib\/nagios\/plugins\/check_tcp -H perfsonar01.hep.wisc.edu -p 8569"},"service_result_id":"0"},"description":
    "Nagios Check TCP","name":"owp_8569_on_perfsonar01.hep.wisc.edu","runningSince":"2013-06-10T18:49:59.860Z","nextCheckTime":
    "2013-06-10T19:03:59.000Z","checkInterval":1200,"parameters":{"port":8569,"host":"perfsonar01.hep.wisc.edu","host-id":"37"},
    "prevCheckTime":"2013-06-10T18:43:59.000Z","type":"owp_8569","running":false,"timeout":60},{"id":"804","result":
    {"message":"0.036 second response time on port 861","id":"777","job-id":"0","time":"2013-06-10T18:42:00.000Z","status":0,
    "service-id":"804","parameters":{"time":"0.035594s","command":
    "\/usr\/lib\/nagios\/plugins\/check_tcp -H perfsonar01.hep.wisc.edu -p 861"},"service_result_id":"0"},"description":
    "Nagios Check TCP","name":"owp_861_on_perfsonar01.hep.wisc.edu","runningSince":"2013-06-10T18:49:59.860Z","nextCheckTime":
    "2013-06-10T19:02:00.000Z","checkInterval":1200,"parameters":{"port":861,"host":"perfsonar01.hep.wisc.edu","host-id":"37"},
    "prevCheckTime":"2013-06-10T18:42:00.000Z","type":"owp_861","running":false,"timeout":60}],"ipv4":null,"hostname":
    "perfsonar01.hep.wisc.edu"}



### Controlling the quantity of information returned ###

The amount information included in the object JSON id is determined by the detailLevel parameter. 
The detailLevel can be: low, medium or high. Try out the varous values of the parameters:

    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/3?detailLevel=low

    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/3?detailLevel=medium

    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/3?detailLevel=high


and you will see what effect it has.


When the detail level is low the datastore returns only the id and name of the object. If it is medium then it returns
more information but for the component objects returns only the id and name. When detail level is high it returns 
all information for the object and for its component objects. For some objects, like matrices, the high detail level
can lead to quite a big output.


What is the default value for detailLevel parameter? It depends. If you ask information about a particular obejct,
for example a particular host, then detailLevel is set to high. Try this:

    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts/30

and you will get full information about host with id=30.


On the other hand if you are asking for a list of hosts:


    http://perfsonar.racf.bnl.gov:8080/dashboard2-1.0-SNAPSHOT/hosts


you will return only low detail level information for each hosts. You can override the default detailLevel.


That completes the discussion about detailLevel parameter. Let us go back to description of operations.

### A note about commands ###

It is not good practice to include the explicit command "hosts" in the
code, as a string. For that reason I have created a static java class PsApi
which contains, as static public variables the codes for all API commands. The
PsApi class can be found in the datastore code.

From now on I will use the notation:

    {url}/PsApi.HOST

to denote the "host" command on a particular datastore instance.

The PsApi class is used by the GUI code when building url requests to contact
the datastore. In the future we may need to put this class in some common
package which is accessible both by the gui code and the datastore - or find some
other, alternative, way to share the command information between gui, datastore,
and collector modules.
