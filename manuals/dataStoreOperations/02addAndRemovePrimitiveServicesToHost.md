In the previous assignment I explained how to perform
CRUD operations on hosts. So we can now create update and delete hosts.
Now it is time to make one step forward and attach (or remove) to hosts
primitive services.

As a background information I remind you that in the perfsonar world we
deal with two types of services: primitive services and matrix services.
We will deal with matrix services later.

The primitive services are services which run on one and only one host.
Here are the known service types (they are listed in the PsApi java class I mentioned earlier):

    // primitive services
    public static String BWCTL_PORT_4823 = "bwctl_port_4823";
    public static String BWCTL_PORT_8570 = "bwctl_port_8570";
    public static String CHECK_LOOKUP_SERVICE = "CheckLookupService";
    public static String NDT_PORT_3001 = "NDT_port_3001";
    public static String NDT_PORT_7123 = "NDT_port_7123";
    public static String NPAD_PORT_8000 = "NPAD_port_8000";
    public static String NPAD_PORT_8001 = "NPAD_port_8001";
    public static String OWP_861 = "owp_861";
    public static String OWP_8569 = "owp_8569";
    public static String PERFSONAR_PSB = "perfSONAR_pSB";


    // matrix services
    public static String LATENCY = "latency";
    public static String THROUGHPUT = "throughput";
    public static String TRACEROUTE = "traceroute";


We may define more service types later, here is the list as-of-today.

The primitive service types come in two broad, intersecting sets:

latency services: CHECK_LOOKUP_SERVICE,PERFSONAR_PSB,OWP_861,OWP_8569

throughput services: all primitive services except OWP_861,OWP_8569

================================================

Previously you have learned that to perform CRUD operations on hosts you
load certain URL using GET,POST,PUT and delete methods where the url is
of the form {url}/hosts_rest/{host id} and if additional parameters are
required they are passed in the data part of the request as JSON object.
GET requests obtain host information, POST create new hosts, PUT update
existing hosts and DELETE delete them.



Adding or removing services to hosts follows this convention. It is
essentially an extension of the PUT method, where you perform a PUT
request of the type:

{url}/hosts_rest/{host_id}/{command}

where command is the command you want to execute, host_id is the id of
the host you are working with and the data part of the request will
contain JSONArray (not JSONObject!) with the list of services you are
adding or removing.


Let us now go into details.


The command to add a service of a given type to a given host with hostId has the form:

{url}+PsApi.HOST+"/"+hostId+"/"+PsApi.HOST_ADD_SERVICE_TYPE_COMMAND

which will look like:

{url}/hosts/{host id}/addservicetype

Where PsApi.HOST and PsApi.HOST_ADD_SERVICE_TYPE_COMMAND refer to constants
from the by know familiar PsApi class.

(it is a better practice not to make explicity use of strings in the code
but have everything parametrized.)


Ok, now that we have our convention, make sure that you have the PsApi
class in your path and we can start coding.


Assume that you have host with host id=hostId. There are the following
operations we want to perform on the services related to this host:

1. We may want to add a primitive service of given type to this host

2. We may want to delete a primitive service of given type from this host

3. We may want to remove service of a given id from this host


here I need to stop for a moment: service may have a service type id (
for example CHECK_LOOKUP_SERVICE). Once a service is created it is given
its unique service id (for example 123456789.87654321). A host may run
only one instance of given service type therefore you may demand that
you want to delete a service identified by host id and service type or
host id and service id.


4. We may want to request to add all available primitive services to a host

5. We may request to remove all primitive services from a host

and finally

6. We may ask to add all latency primitive services to a host

7. We may want to add all throughput primitive services to host

Let us now go step by step with those commands.


1. In order to add primitive services of given types to a host you make
a PUT request in the form:

{url}/PsApi.HOST/{hostId}/PsApi.HOST_ADD_SERVICE_TYPE_COMMAND

or

url}/hosts/{host id}/addservicetype



where in the data part of the request you put a JSONArray of service
type id's.


For example to add service types: bwctl_port_4823 and CheckLookupService
you put in the data part

    ["bwctl_port_4823","CheckLookupService"]

The data store will create services of the requested types, atatch them
to the hosts and will return a JSON object of the host with the services
added.


2. To remove from host service of the requested type you do a PUT request

{url}/PsApi.HOST/{hostId}/PsApi.HOST_REMOVE_SERVICE_TYPE_COMMAND

or

{url}/hosts/{host id}/removeservicetype



and again in the data you include the service types you want to remove,
for example:

    ["bwctl_port_4823","CheckLookupService"]


3. If you want to remove from a host service, but you do not identify
the service by type but service id then you do a PUT request to

    {url}/PsApi.HOST/{hostId}/PsApi.HOST_REMOVE_SERVICE_ID_COMMAND
    
or
 
   {url}/hosts/{hostId}/removeserviceid
    

and in the data part you include list of service id's:


    ["123456.4444444","555532.5623"]


(Of course you need to know your service id's).


The data store will return JSONObject of your updated host.


4. If you want to add all primitive services to host then you do a PUT

    {url}/PsApi.HOST/{hostId}/PsApi.HOST_ADD_ALL_SERVICES_COMMAND
    
    or
    
    {url}/hosts/{hostId}/addallservices

no need to include any additional data


5. in order to delete all primitive services from a host do a PUT

    {url}/PsApi.HOST/{hostId}/PsApi.HOST_REMOVE_ALL_SERVICES_COMMAND
    
    or
    
    {url}/hosts/{hostId}/removeallservices
    


no need to include any data

6,7.

and to add all latency/throughput services do a PUT to:

    {url}/PsApi.HOST/{hostId}/PsApi.HOST_ADD_LATENCY_SERVICES_COMMAND
    {url}/PsApi.HOST/{hostId}/PsApi.HOST_ADD_THROUGHPUT_SERVICES_COMMAND
    
    or
    
    {url}/hosts/{hostId}/addlatencyservices
    {url}/hosts/{hostId}/addthroughputservices

and that is all. Now all you need is to code it and build around it a
GUI. 




