PerfModDash  
================  
PerfModDash is a modular dashboard for gathering performance data from
[perfSonar](http://www.internet2.edu/performance/pS-PS/) hosts.

Documentation on how to use the service is available from the [manuals
 directory](https://github.com/PerfModDash/PerfModDash/tree/master/manuals).

Installation  
-------------

To deploy the dashboard on SL 6 or CentOS 6 follow these steps.

### Install tomcat 6 ###

    $ sudo yum install yum-conf-epel
    $ sudo yum install tomcat6

### Install Java JDK ###

Download the proper [Java RPM](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
by clicking on the JDK download button.

    $ sudo yum install /[path-to]/jdk-7u10-linux-x64.rpm

### Install Apache Maven ###

Download the [Apache Maven](http://maven.apache.org/download.cgi) binary, currently v3.0.5.  
Extract to /usr/local/apache-maven, creating subdirectory apache-maven-3.0.5.

    $ mkdir /usr/local/apache-maven
    $ cd /usr/local/apache-maven/
    $ mv /[path-to]/apache-maven-3.0.5-bin.tar.gz .
    $ tar xzvf apache-maven-3.0.5-bin.tar.gz

### Set environment variables ###

    export JAVA_HOME=/usr/java/jdk1.7.0_17
    export M2_HOME=/usr/local/apache-maven/apache-maven-3.0.5
    export M2=$M2_HOME/bin
    export PATH=$JAVA_HOME/bin:$M2:$PATH

### Confirm Maven is properly installed ###

    $ mvn --version

### Install git ###

    $ sudo yum install git

### Checkout and build PerfModDash ###
    $ git clone git://github.com/PerfModDash/PerfModDash.git
    $ cd PerfModDash/PsDataStore/dashboard
    $ mvn package
    $ sudo cp target/dashboard-1.0-SNAPSHOT.war /var/lib/tomcat6/webapps/.
    $ sudo /sbin/service tomcat6 start

### Confirm the site works ###
    http://[hostname.org]:8080/dashboard-1.0-SNAPSHOT/

Note: If the page is not accessible, check for firewall issues.
