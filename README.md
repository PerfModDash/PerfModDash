PerfModDash
================
PerfModDash is a modular dashboard for gathering performance data from
[perfSonar](http://www.internet2.edu/performance/pS-PS/) hosts.

Documentation on how to use the service is available form the (manuals
direcory)[https://github.com/PerfModDash/PerfModDash/tree/master/manuals]

Installation
-------------

In order to deploy the dashboard on SL 6 or CentOS 6 install tomcat 6 :

    $ sudo yum install yum-conf-epel
    $ sudo yum install tomcat6


To build the project you will need maven and Oracle Java(I think?). First
obtain the right RPM for your platform from
[Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html) :

    $ yum localinstall jdk-7u10-linux-x64.rpm 

set you environment to look something like this:

    export JAVA_HOME=/usr/java/jdk1.7.0_10/jre
    export M2_HOME=/usr/local/apache-maven/apache-maven-3.0.4
    export M2=$M2_HOME/bin
    export PATH=$M2:$PATH

now checkout the project and build it:

    $ git clone git://github.com/PerfModDash/PerfModDash.git
    $ cd PerfModDash/PsDataStore/dashboard
    $ mvn package
    $ sudo cp target/dashboard-1.0-SNAPSHOT.war /var/lib/tomcat6/webapps/.
    $ sudo /sbin/service tomcat6 start

now check to see if you it's running by visiting the site:

   $ http://hostname.com:8080/dashboard-1.0-SNAPSHOT/



