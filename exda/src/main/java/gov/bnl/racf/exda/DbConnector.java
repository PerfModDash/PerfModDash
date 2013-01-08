package gov.bnl.racf.exda;

import java.lang.Class;

import java.sql.*;
import java.util.Properties;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.*;

public class DbConnector
{  
    private String moduleName="DbConnector";
    

    private String userName1 = "";
    private String userPwd1="";
    private String url1="";

    private String userName2 = "";
    private String userPwd2="";
    private String url2="";

    private String userName3 = "";
    private String userPwd3="";
    private String url3="";

    private String userName4 = "";
    private String userPwd4="";
    private String url4="";
    
    private String userName5 = "";
    private String userPwd5="";
    private String url5="";

    private String userName6 = "";
    private String userPwd6="";
    private String url6="";

    private String userName = "";
    private String userPwd="";
    private String url="";

    private Connection conn = null;

    private ResultSet rs=null;
    private Statement s=null;

    private String configFileName="";


    public DbConnector(){	    
	loadConfiguration();
	openConnection(1);	
    }

    public DbConnector(String userName,String userPwd, String url){
	this.userName=userName;
	this.userPwd=userPwd;
	this.url=url;
	this.openDatabase();
    }

    public DbConnector(int dbNumber,String inputConfigFileName  ){
	configFileName=inputConfigFileName;
	loadConfiguration();
	openConnection(dbNumber);	
    }

    public DbConnector(String hostname,String inputConfigFileName  ){
	this.configFileName=inputConfigFileName;

	System.out.println(getClass().getName()+"configFileName="+configFileName);
	System.out.println(getClass().getName()+"hostname="+hostname);

	Properties prop = new Properties();
	try{
	    prop.load(new FileInputStream( this.configFileName ));

	    String databaseId=prop.getProperty(hostname);
	    if(databaseId.equals("1")){
		userName=prop.getProperty("userName1");
		userPwd =prop.getProperty("userPwd1");
		url     =prop.getProperty("url1");
	    }
	    if(databaseId.equals("2")){
		userName=prop.getProperty("userName2");
		userPwd =prop.getProperty("userPwd2");
		url     =prop.getProperty("url2");
	    }
	    if(databaseId.equals("3")){
		userName=prop.getProperty("userName3");
		userPwd =prop.getProperty("userPwd3");
		url     =prop.getProperty("url3");
	    }
	    if(databaseId.equals("4")){
		userName=prop.getProperty("userName4");
		userPwd =prop.getProperty("userPwd4");
		url     =prop.getProperty("url4");
	    }
	    if(databaseId.equals("5")){
		userName=prop.getProperty("userName5");
		userPwd =prop.getProperty("userPwd5");
		url     =prop.getProperty("url5");
	    }
	    if(databaseId.equals("6")){
		userName=prop.getProperty("userName6");
		userPwd =prop.getProperty("userPwd6");
		url     =prop.getProperty("url6");
	    }
	}catch (Exception e){
	    System.out.println(getClass().getName()+": failed to open database properties file");
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    Class.forName ("com.mysql.jdbc.Driver").newInstance ();
	}catch (Exception e){
	    System.out.println(getClass().getName()+": failed to load database driver");
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    this.conn = DriverManager.getConnection (url,userName,userPwd);
	}catch (Exception e){
	    System.out.println(getClass().getName()+": failed to establish database connection");
	    System.out.println(getClass().getName()+" "+e);
	}	
    }

    private void loadConfiguration(){
      
	
	Properties prop = new Properties();
	try{
	    prop.load(new FileInputStream( configFileName ));

	    userName1=prop.getProperty("userName1");
	    userPwd1 =prop.getProperty("userPwd1");
	    url1     =prop.getProperty("url1");
	    
	    userName2=prop.getProperty("userName2");
	    userPwd2 =prop.getProperty("userPwd2");
	    url2     =prop.getProperty("url2");
	    
	    userName3 =prop.getProperty("userName3");
	    userPwd3 =prop.getProperty("userPwd3");
	    url3     =prop.getProperty("url3");

	    userName4 =prop.getProperty("userName4");
	    userPwd4 =prop.getProperty("userPwd4");
	    url4     =prop.getProperty("url4");

	    userName5 =prop.getProperty("userName5");
	    userPwd5 =prop.getProperty("userPwd5");
	    url5     =prop.getProperty("url5");

	    userName6 =prop.getProperty("userName6");
	    userPwd6 =prop.getProperty("userPwd6");
	    url6     =prop.getProperty("url6");

	} catch (IOException ex) {
	    System.out.println(getClass().getName()+" Error: unable to open configuration file "+configFileName);
	    System.out.println(getClass().getName()+" "+ex);
	    ex.printStackTrace();
        }
	
    }

    private void openConnection (int dbNumber){

	url=url1;
	userName=userName1;
	userPwd=userPwd1;

	if (dbNumber==1){
	    url=url1;
	    userName=userName1;
	    userPwd=userPwd1;
	}
	if (dbNumber==2){
	    url=url2;
	    userName=userName2;
	    userPwd=userPwd2;
	}
	if (dbNumber==3){
	    url=url3;
	    userName=userName3;
	    userPwd=userPwd3;
	}
	if (dbNumber==4){
	    url=url4;
	    userName=userName4;
	    userPwd=userPwd4;
	}
	if (dbNumber==5){
	    url=url5;
	    userName=userName5;
	    userPwd=userPwd5;
	}
	if (dbNumber==6){
	    url=url6;
	    userName=userName6;
	    userPwd=userPwd6;
	}
	this.openDatabase();
    }
    private void openDatabase(){
	System.out.println(getClass().getName()+" url="+url);
	System.out.println(getClass().getName()+" userName="+userName);	
	System.out.println(getClass().getName()+" userPwd="+userPwd);	
	
	try{
	    Class.forName ("com.mysql.jdbc.Driver").newInstance ();
	}catch (Exception e){
	    System.out.println(getClass().getName()+": failed to load database driver");
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    conn = DriverManager.getConnection (url,userName,userPwd);
	}catch (Exception e){
	    System.out.println(getClass().getName()+": failed to establish database connection");
	    System.out.println(getClass().getName()+" "+e);
	}	


    }

    public Connection getConn(){
	return conn;
    }
    public ResultSet  query(String sql){
	
	try{
	    s = conn.createStatement ();
	    s.executeQuery (sql);
	}catch (Exception e){
	    System.out.println(getClass().getName()+"Point ASDF");
	    System.out.println(getClass().getName()+": failed to execute query "+sql);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    rs = s.getResultSet ();
	    //s.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+": failed to obtain result set for query query "+sql);
	    System.out.println(getClass().getName()+" "+e);
	}
	return rs;
    }
    public void close(){
	try{
	    s.close();
	    conn.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+": failed to close dB connection");
	    System.out.println(getClass().getName()+" "+e);
	}
    }
    public Connection getConnection(){
	return conn;
    }
    public boolean isClosed(){
	boolean result=true;
	try{
	    result=conn.isClosed();
	}catch (Exception e){
	    System.out.println(getClass().getName()+": failed to check if dB is closed");
	    System.out.println(getClass().getName()+" "+e);
	}
	return result;
    }

}
