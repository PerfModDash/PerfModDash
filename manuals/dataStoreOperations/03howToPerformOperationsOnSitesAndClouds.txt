


You perform CRUD operations on sites and clouds in (almost) the same way as you do them on
hosts. You have to execute http requests to a url defined by

{url}/PsApi.SITE

where PsApi.SITE is a constant from the attached PsApi.java file. As
with hosts you can perform GET,POST,PUT and DELETE requests.

Let me go over them one by one.

1. To obtain information about sites you load

{url}/PsApi.SITE

you are returned a JSONArray with id's of known sites.

In order to obtain information about a site you do:

{url}/PsApi.SITE/{siteid}

you are given JSONObject with information about the site. The object
will contain site name, id and array of hosts which belong to this site.

2. To create a new site

Create JSONObject (follow example object you can obtain using GET). Fill
it with some information, like site name. Do not fill information about
hosts - we will do it later.

Put the JSONObject into data part of your request and execute POST
request to

{url}/PsApi.SITE

You will be returned JSONObject of your new site - including the newly
assigned site id.


3. To delete a site execute DELETE request of the form:

{url}/PsApi.SITE/{siteid}

the site siteid will be deleted. The hosts which belong to this site
will not be deleted.

4. To update site information.

If you want to change site name or other simple information then build a
JSONObject of the site, fill it with the information you want to update
and do a PUT request to

{url}/PsApi.SITE/{siteid}

with json object included in data part of your request. The data store
will update your site information.


5. To add/remove hosts.

Each site contains hosts, much like in the previous chapter each
host contained services. You can add hosts to sites - but unlike the
case with services - you have to create the host first. Since you
already know how to create hosts, I will not discus it any furhter.

Let us assume that you have a list of hosts which you want to add to
your site. You know your site Id and you know your host id's.

To add hosts to site you make a PUT request to

{url}/PsApi.SITE/{siteid}/PsApi.SITE_ADD_HOST_IDS

and in the data part of the request you include JSONArray of the host id's.


The server will return updated jsonobject of your site. You can verify
that your hosts have been added.


To remove hosts from a site you do a similar put request to

{url}/PsApi.SITE/{siteid}/PsApi.SITE_REMOVE_HOST_IDS

including in data part JSONArray of id's of hosts to be removed. You
will get back a JSONObject of the site. The hosts should be removed from it.


That is all. As you see this is very similar to the host manipulation
operations.

Once you can handle sites, then it is very easy to extend the concept
and build pages which manipulate clouds. Remember: Each cloud contains
sites and matrices. We do not worry about matrices yet. So all you need
to do is to be able to create, update and delete clouds and add/remove
sites to existing clouds. You do it in the same manner: by executing
GET/PUT/POST/DELETE request. The only difference is that instead
PsApi.SITE you use PsApi.CLOUD and to add/remove sites from cloud you
use PsApi.CLOUD_ADD_SITE_IDS and PsApi.CLOUD_REMOVE_SITE_IDS

Of course, before you add a site to a cloud you have to create this site first.

