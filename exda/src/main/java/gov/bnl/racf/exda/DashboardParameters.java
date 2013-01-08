package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.Collections;



public class DashboardParameters
{  
    private String baseUrl=null;

    private ParameterBag parameterBag = null;
    private DbConnector db=null;


    public DashboardParameters(ParameterBag paramBag,  DbConnector inputDb){
	this.parameterBag=paramBag;
	this.db=inputDb;
	this.load();
    }

    public void load(){
	String query = "SELECT * FROM Parameters";
	PreparedStatement queryPS=null;
	ResultSet rs=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    //queryPS.setInt(1,this.getParameterId());
	    rs=queryPS.executeQuery();
	    while(rs.next()){
		this.setBaseUrl(rs.getString("BaseUrl"));
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load parameters by query");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    rs.close();
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement or result set");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	
	}
    }

    public String getBaseUrl(){
	return this.baseUrl;
    }
    public void setBaseUrl(String inputVar){
	this.baseUrl=inputVar;
    }
}
