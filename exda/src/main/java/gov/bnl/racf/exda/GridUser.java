package gov.bnl.racf.exda;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletContext;


import javax.security.cert.X509Certificate;
import java.security.cert.*;

import java.net.InetAddress;
import java.sql.*;

import java.util.Collections;

public class GridUser implements Comparable
{  

    HttpServletRequest request=null;

    private DbConnector db=null;

    private java.security.cert.X509Certificate userCert=null;
    private String userDN=null;
    private boolean authenticated=false;
    private String userEmail="";
    private String userName="";

    private String userRoles="";

    private int id=-1;

    private ListOfKnownUsers listOfKnownUsers=null;

    PrintWriter out=null;

    public GridUser(DbConnector inputDb)
	{
	    db=inputDb;
	}
    public GridUser(DbConnector inputDb,HttpServletRequest requestInput){
	db=inputDb;
	request=requestInput;
	java.security.cert.X509Certificate[] certs=(java.security.cert.X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
	unpackCerts(certs);
	readKnownUsers();
	compareToKnownUsers();
    }
    public GridUser(DbConnector inputDb,HttpServletRequest requestInput,ListOfKnownUsers listOfKnownUsersInput){
	db=inputDb;
	request=requestInput;
	java.security.cert.X509Certificate[] certs=(java.security.cert.X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
	unpackCerts(certs);
	listOfKnownUsers=listOfKnownUsersInput;
	compareToKnownUsers();
    }
    public GridUser(DbConnector inputDb, java.security.cert.X509Certificate[] certs){
	db=inputDb;
	unpackCerts(certs);
	readKnownUsers();
	compareToKnownUsers();
    }
    public GridUser(DbConnector inputDb, java.security.cert.X509Certificate[] certs,ListOfKnownUsers listOfKnownUsersInput){
	db=inputDb;
	unpackCerts(certs);
	listOfKnownUsers=listOfKnownUsersInput;
	compareToKnownUsers();
    }
    // ==== end of constructors === //
    
    // === set and get methods === //
    public void setUserDn(String inputVar){
	this.userDN=inputVar;
    }
    public String getUserDn(){
	return this.userDN;
    }
    public String getDN(){
	return userDN;
    }
    public String getUserEmail(){
	return this.userEmail;
    }
    public void setUserEmail(String inputVar){
	this.userEmail=inputVar;
    }
    public String getUserName(){
	return this.userName;
    }
    public void setUserName(String inputVar){
	this.userName=inputVar;
    }
    public String getUserRoles(){
	return this.userRoles;
    }
    public void setUserRoles(String inputVar){
	this.userRoles=inputVar;
    }
    public String getRoles(){
	return userRoles;
    }
    public int getId(){
	return this.id;
    }
    public void setId(int inputVar){
	this.id=inputVar;
    }


    // === load methods === //

    public void readKnownUsers(){
	listOfKnownUsers=new ListOfKnownUsers(db);
    }

    public void unpackCerts(java.security.cert.X509Certificate[] certs){
	if (certs != null) {
	    String dn;
	    for (int i = 0; i < certs.length; i++) {
		dn=certs[i].getSubjectDN().toString();
		System.out.println("GridUser cert line="+dn);
		if (dn.indexOf("People")!=-1){
		    System.out.println("GridUser accepted #1 as "+dn);
		    userDN=dn;
		    authenticated=true;
		    userCert=certs[i];
		}
	    }
	    if(!authenticated){
		dn=certs[0].getSubjectDN().toString();
		userDN=dn;
		userCert=certs[0];
		authenticated=true;
		System.out.println("GridUser accepted #2 as "+dn);
	    }
	} else {
	    // do nothing
	}
    }

    public void compareToKnownUsers(){
	Iterator itr=listOfKnownUsers.getList().iterator();
	while (itr.hasNext()){
	    UserInfo currentUser=(UserInfo)itr.next();
	    if(currentUser.getDN().equals(userDN)){
		userRoles=currentUser.getRoles();
	    }
	}
    }

    public boolean isDOEGrids(){
	if(userDN.indexOf("doegrids")!=-1){
	    return true;
	}else{
	    return false;
	}
    }

    public boolean isKnown(){
	if (listOfKnownUsers==null){
	    return false;
	}else{
	    if(userDN==null){
		return false;
	    }else{
		return true;
		//if (listOfKnownUsers.containsDN(userDN)){
		//    if (isDOEGrids()){
		//	return true;
		//    }else{
		//	return false;
		//    }
		//}else{
		//    return false;
		//}
	    }
	}
    }

    public boolean isApproved(){
	if (isValid() && isKnown()){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isValid(){
	boolean result=false;
	if (userCert!=null){
	    try{
		userCert.checkValidity();
		result=true;
	    }catch(Exception e){
		result=false;
	    }
	}else{
	    result=false;
	}
	return result;
    }

    public int compareTo(Object obj) {
	GridUser otherUser=(GridUser)obj;
	String otherUserDN=otherUser.getDN();
	if (otherUserDN==null){
	    if (userDN==null){
		return 0;
	    }else{
		return 1;
	    }
	}else{
	    if(userDN==null){
		return -1;
	    }else{
		return userDN.compareTo(otherUserDN);
	    }
	}
    }
    public boolean isAdmin(){
	String dn=this.getDN();
	if(dn==null){
	    return false;
	}else{
	    if(dn.indexOf("Wlodek")!=-1){
		return true;
	    }else{
		return false;
	    }
	}
    }

    public UserInfo getUserInfo(){
	boolean approved=isApproved();
	String dn=getDN();
	String roles=getRoles();
	UserInfo ui=new UserInfo(dn,approved,roles);
	return ui;
    }

    public void log(){

	//String addr = req.getRemoteAddr(); 
	String client_host = "not recorded";
	if(request!=null){
	    client_host = request.getRemoteHost(); // hostname.com
	}
	
	String insertSqlQuery="INSERT INTO AccessHistory (access_time,dn,hostname,client_hostname,knownUser) VALUES(NOW(),?,?,?,?)";
	//String insertSqlQuery="INSERT INTO AccessHistory (access_time,dn,hostname,client_hostname,knownUser) VALUES(NOW(),'{dn}','{ip}','{client_host}','{approved}')";
	//String insertSqlQuery="INSERT INTO AccessHistory (access_time,dn,hostname,client_hostname) VALUES(NOW(),?,?,?)";
	String ip="unknown";
	 try{
	     InetAddress addr = InetAddress.getLocalHost();
	     ip=addr.toString();
	 }catch (Exception e){
	     System.out.println(getClass().getName()+" error occured while obtaining host info");
	     System.out.println(e);
	 }

	 PreparedStatement insertSql=null;
	 try{
	     String approved="no";
	     if (isApproved()){
		 approved="yes";
	     }
	     insertSql= db.getConn().prepareStatement(insertSqlQuery);
	     insertSql.setString(1,userDN);
	     insertSql.setString(2,ip);
	     insertSql.setString(3,client_host);
	     insertSql.setString(4,approved);
	     insertSql.executeUpdate();
	 }catch (Exception e){
	     System.out.println(getClass().getName()+" error occured when writing to database");
	     System.out.println(getClass().getName()+" "+insertSqlQuery);
	     System.out.println(getClass().getName()+" "+e);
	 }
	 try{
	     insertSql.close();
	 }catch (Exception e){
	     System.out.println(getClass().getName()+" error occured when closing prepared statement");
	     System.out.println(getClass().getName()+" "+insertSqlQuery);
	     System.out.println(getClass().getName()+" "+e);
	 }	     
    }


    public boolean equals(Object obj) {
	GridUser otherUser=(GridUser)obj;
	if (otherUser==null){
	    if(userDN==null){
		return true;
	    }else{
		return false;
	    }
	}else{
	    String otherUserDN=otherUser.getDN();
	    if (otherUserDN==null){
		if (userDN==null){
		    return true;
		}else{
		    return false;
		}
	    }else{
		if(userDN==null){
		    return false;
		}else{
		    return userDN.equals(otherUserDN);
		}
	    }	
	}
    }
}

