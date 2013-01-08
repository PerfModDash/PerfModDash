package gov.bnl.racf.exda;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONObject;


public class ServiceMatrixElementPair
{  

    String matrixType=null;
    int matrixId=-2;

    PerfSonarHost sourceHost=null;
    PerfSonarHost destinationHost=null;
    ServiceMatrixElement upperNode=null;
    ServiceMatrixElement lowerNode=null;

    ParameterBag parameterBag=null;
    DbConnector db=null;
 
    public ServiceMatrixElementPair(ParameterBag paramBag,  DbConnector inputDb,String matrixType, PerfSonarHost sourceHost, PerfSonarHost destinationHost){
	this.parameterBag=paramBag;
	this.db=inputDb;
	this.matrixType=matrixType;
	this.sourceHost=sourceHost;
	this.destinationHost=destinationHost;
	upperNode=new ServiceMatrixElement(this.parameterBag,db,matrixType,sourceHost,destinationHost,sourceHost);
	lowerNode=new ServiceMatrixElement(this.parameterBag,db,matrixType,sourceHost,destinationHost,destinationHost);

    }
    public ServiceMatrixElementPair(ParameterBag paramBag,  DbConnector inputDb,int matrixId, PerfSonarHost sourceHost, PerfSonarHost destinationHost){
	//System.out.println(getClass().getName()+" element pair creator");
	this.parameterBag=paramBag;
	this.db=inputDb;
	//this.matrixType=matrixType;
	this.matrixId=matrixId;
	this.sourceHost=sourceHost;
	this.destinationHost=destinationHost;
	//System.out.println(getClass().getName()+" create ServiceMatrixElement");
	upperNode=new ServiceMatrixElement(this.parameterBag,db,this.matrixId,this.sourceHost,this.destinationHost,this.sourceHost);
	//System.out.println(getClass().getName()+" ServiceMatrixElement created");

	lowerNode=new ServiceMatrixElement(this.parameterBag,db,this.matrixId,this.sourceHost,this.destinationHost,this.destinationHost);

	//System.out.println(getClass().getName()+" element pair creator exit");
    }

    public ArrayList<JSONObject> toJson(){
	ArrayList<JSONObject> componentServices = new ArrayList<JSONObject>();
	JSONObject json1=upperNode.toJson();
	if(json1!=null){
	    componentServices.add(json1);
	}
	JSONObject json2=lowerNode.toJson();
	if(json2!=null){
	    componentServices.add(json2);
	}
	return componentServices;
    }


    public HtmlTable htmlTable(){
	HtmlTable ht=new HtmlTable(1);
	HtmlTableCell upperCell=upperNode.numericStatusCell();
	HtmlTableCell lowerCell=lowerNode.numericStatusCell();
	ht.addCell(upperCell);
	ht.addCell(lowerCell);
	ht.setBorder(0);
	ht.setPadding(0);
	return ht;	    	    		
    }
}
