package gov.bnl.racf.exda;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletContext;
import java.lang.Class;
import java.util.Date;
import java.util.TimerTask;
import java.util.Properties;

import java.net.InetAddress;
import java.net.*;
import java.sql.*;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class AlarmCheck extends TimerTask {

    private Properties dbParameters = null;
    private DbConnector dB=null;    
    private String dbConfigFileName="WEB-INF/classes/db.properties"; 

    public void run() {
	Date date = new Date();

	if(this.dB==null){
	    this.openConnection();
	}
	ParameterBag pB=new ParameterBag();
	// the user to make changes to alarm is the current class (not a human!)
	pB.setUser(new UserInfo(getClass().getName(), true, ""));

	DashboardParameters dashboardParameters = new DashboardParameters(pB,this.dB);
	//System.out.println(date+" "+getClass().getName()+" dashboardParameters.getBaseUrl()="+ dashboardParameters.getBaseUrl());
	//pB.setRequestUri("/exda/");
	pB.setRequestUri( dashboardParameters.getBaseUrl() );

	ListOfAlarms listOfAlarms = new ListOfAlarms(pB,this.dB);
	Iterator iter = listOfAlarms.list().iterator();
	while(iter.hasNext()){
	    Alarm alarm=(Alarm)iter.next();
	    String result=alarm.doNotificationCheck();
	}

	//this.sendMail();
	//this.mail2();

    }
    private void openConnection(){

	Date date = new Date();

	String hostname="";
	try {
	    InetAddress addr = InetAddress.getLocalHost();
	    
	    // Get IP Address
	    byte[] ipAddr = addr.getAddress();
	    
	    // Get hostname
	    hostname = addr.getHostName();


	} catch (UnknownHostException e) {
	    System.out.println(date+" "+getClass().getName()+" failed to get hostname: hostname="+hostname);
	    System.out.println(date+" "+getClass().getName()+" "+e);
	}
	//String configFile =getServletContext().getRealPath("/")+dbConfigFileName;
	//System.out.println(getClass().getName()+" db config file="+configFile);
	
	String userName="exda";
	String userPwd="exda-pwd";
	String url="jdbc:mysql://griddev03.usatlas.bnl.gov:49152/exda?autoReconnect=true";


	//TODO maybe replace hostnames grid21 and grid09 with localhost
	if(!hostname.equals("talos.racf.bnl.gov")){
	    if(hostname.equals("grid09.racf.bnl.gov")){
		url="jdbc:mysql://grid09.racf.bnl.gov:3306/exda?autoReconnect=true";
	    }else{
		url="jdbc:mysql://grid21.racf.bnl.gov:23306/exda?autoReconnect=true";
	    }
	}



	this.dB=new  DbConnector(userName,userPwd,url);
    }  

    private void sendMail(){
	Date date = new Date();
	try{
	    //System.out.println(date+" "+getClass().getName()+" trying to send mail...");
	    String host = "rcf2.rhic.bnl.gov";
	    String to = "tomw@bnl.gov";
	    String from = "tomasz.wlodek@cern.ch";
	    
	    // Get system properties
	    Properties props = System.getProperties();
	    
	    // Setup mail server
	    //props.put("mail.smtp.host", host);

	    props.put("mail.host", host);
	    props.put("mail.transport.protocol", "smtp");

	    
	    // Get session
	    Session session = Session.getDefaultInstance(props, null);
	    
	    // Define message
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));
	    message.addRecipient(Message.RecipientType.TO, 
				 new InternetAddress(to));
	    message.setSubject("Hello JavaMail");
	    message.setText("Welcome to JavaMail");
	    
	    // Send message
	    Transport.send(message);
	    //System.out.println(date+" "+getClass().getName()+" mail sent!!!");
	}catch(Exception e){
	    System.out.println(date+" "+getClass().getName()+" mail send failed");
	    System.out.println(date+" "+getClass().getName()+" "+e);
	    System.out.println(e.getStackTrace());
	    System.out.println(e.getMessage());
	}
    }

    public void mail2() {

        // SUBSTITUTE YOUR EMAIL ADDRESSES HERE!!!
        String to = "tomw@bnl.gov";

	ParameterStore parameterStore = ParameterStore.getParameterStore();
	String hostname = parameterStore.getHostname();
	String hostAlias = parameterStore.getHostAlias();
	String from = "perfsonar@"+hostAlias;

        //String from = "perfsonar@perfsonar.usatlas.bnl.gov";
        // SUBSTITUTE YOUR ISP'S MAIL SERVER HERE!!!
        String host = "rcf.rhic.bnl.gov";

        // Create properties, get Session
        Properties props = new Properties();

        // If using static Transport.send(),
        // need to specify which host to send it to
        props.put("mail.smtp.host", host);
        // To see what is going on behind the scene
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props);

        try {
            // Instantiatee a message
            Message msg = new MimeMessage(session);

            //Set message attributes
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("Test E-Mail through Java XXXXXXXXXXXXXXX");
            msg.setSentDate(new Date());

            // Set message content
            msg.setText("This is a test of sending a " +
                        "plain text e-mail through Java.\n" +
                        "Here is line 2.");

            //Send the message
            Transport.send(msg);
        }
        catch (MessagingException mex) {
            // Prints all nested (chained) exceptions as well
            mex.printStackTrace();
        }
    }

    public static void mail3() throws Exception{
	Properties props = new Properties();
	props.setProperty("mail.transport.protocol", "smtp");
	props.setProperty("mail.host", "rcf.rhic.bnl.gov");
	props.setProperty("mail.user", "tomw");
	props.setProperty("mail.password", "");

      Session mailSession = Session.getDefaultInstance(props, null);
      Transport transport = mailSession.getTransport();

      MimeMessage message = new MimeMessage(mailSession);
      message.setSubject("Testing javamail html");
      message.setContent
         ("This is a test <b>HOWTO<b>", "text/html; charset=ISO-8859-1");
      message.addRecipient(Message.RecipientType.TO,
         new InternetAddress("elvis@presley.org"));

      transport.connect();
      transport.sendMessage(message,
         message.getRecipients(Message.RecipientType.TO));
      transport.close();
    }



}
