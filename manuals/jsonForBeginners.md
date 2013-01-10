The datastore code uses JSON to encode data exchanged with GUI or collector. For that reason I inlcude a short tutorial about JSON I wrote for Lucy some time ago. Apologies to all who think this is to elementary, however I think that even elementary things can be useful for someone.


JSON is a way to encode data so that  you can transfer data between programs. Imagine that we want to have a host object. What properties does a host have? A name, ipv4 address, possibly ipv6 address and a list of services which run on it.

In python you could represent host by a dictionary type:

host["hostname"]="abcd.com"
host["ipv4"]="999.999.99.99"
host["ipv6"]=""
host["services"]=[] ( we do not have any services on this host yet, list is empty).


In java you can represent this host as an object:

public class Host {

    private String hostname="";
    private ipv6="";
    private ipv4="";
    /// and so on. Also you should add get and set methods for the properties

}

Now imagine that you want to pass the host object between applications, which run on different places and possibly in different languages. How would you do this? Answer: JSON. JSON converts your HOST object into text string.


Here is how it happens in Java:

import org.json.simple.JSONObject;

JSONObject json=new JSONObject();

json.put("hostname",host.getHostname());
json.put("ipv4",host.getIpv4());
json.put("ipv6",json.getIpv6());

and now you can convert json to string

json.toString();

and you will get something like:

[{"ipv6":null,"id":"1347041058999.966010","services":[],"ipv4":"999.999.99.99","hosname":"abc.com"}]


such a string can now be sent to another application or posted to a web server or whatever.

On the receiving end the application can take this json string and convert it into json object:


String jsonString= .... the above string ...

JSONParser parser = new JSONParser();
JSONObject json = (JSONObject)parser.parse(jsonString);


and now you have json object. You cna now extract from it its properties by

String hostname = json.get("hostname");
String ipv4 = json.get("ipv4");

and so on.


Another useful JSON object we make use of is JSONArray. It is an object which represents a list of other objects (which could be JSONObjects). You will find examples how to use JSONArray objects in the datastore code.



There are similar ways to encode (and decode) data into json objects in python and javascript. For JSON in jave you cna start with 


http://www.mkyong.com/java/json-simple-example-read-and-write-json/

for json in python and javascript google 'simplejson python" and "simplejson javascript"



