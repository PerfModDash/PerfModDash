package gov.bnl.racf.exda;

import java.lang.Math;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;



public class ParameterStore
{  
   
    private DbConnector db=null;
    private String baseDirectory="";
    private PrintWriter out=null;

    private String hostname="";
    private String hostAlias="";

    private static ParameterStore parameterStore=null;

    private ParameterStore(){
    }

    public static ParameterStore getParameterStore(){
	if(parameterStore==null){
	    parameterStore=new ParameterStore();
	}
	return parameterStore;
    }
    public void setHostname(String s){
	hostname=s;
    }
    public String getHostname(){
	return hostname;
    }
    public void setHostAlias(String s){
	hostAlias=s;
    }
    public String getHostAlias(){
	return hostAlias;
    }


    public DbConnector getDb(){
	return db;
    }
    public void setDb(DbConnector dbInit){
	db=dbInit;
    }
    public String getBaseDirectory(){
	return baseDirectory;
    }
    public void setBaseDirectory(String baseDir){
	baseDirectory=baseDir;
    }

    public void setOut(PrintWriter outInput){
	out=outInput;
    }
    public PrintWriter getOut(){
	return out;
    }

}
  
