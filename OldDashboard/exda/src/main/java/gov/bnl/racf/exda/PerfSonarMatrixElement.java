package gov.bnl.racf.exda;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


public class PerfSonarMatrixElement
{  

    String matrixType=null;
    int matrixId=-2;

    PerfSonarHost sourceHost=null;
    PerfSonarHost destinationHost=null;

    ServiceMatrixElement upperNode = null;
    ServiceMatrixElement lowerNode = null;

    ProbeStatus status = null;


    ParameterBag parameterBag=null;
    DbConnector db=null;
 
    public PerfSonarMatrixElement(ParameterBag paramBag,  DbConnector inputDb,String matrixType, PerfSonarHost sourceHost, PerfSonarHost destinationHost){
	this.parameterBag=paramBag;
	this.db=inputDb;
	this.setMatrixType(matrixType);
	this.setSourceHost(sourceHost);
	this.setDestinationHost(destinationHost);
	this.upperNode=new ServiceMatrixElement(this.parameterBag,db,matrixType,sourceHost,destinationHost,sourceHost);
	this.lowerNode=new ServiceMatrixElement(this.parameterBag,db,matrixType,sourceHost,destinationHost,destinationHost);
	this.setStatus(upperNode.getStatus());
	if(this.getStatus().compareTo(lowerNode.getStatus())==1){
	    this.setStatus(lowerNode.getStatus());
	}
    }

    // get and set methods
    public void setStatus(ProbeStatus inputVar){
	this.status=inputVar;
    }
    public ProbeStatus getStatus(){
	return this.status;
    }
    public void setMatrixType(String inputvar){
	this.matrixType=inputvar;
    }
    public String getMatrixType(){
	return this.matrixType;
    }
    public void setSourceHost(PerfSonarHost inputVar){
	this.sourceHost=inputVar;
    }
    public PerfSonarHost getSourceHost(){
	return this.sourceHost;
    }
    public void setDestinationHost(PerfSonarHost inputVar){
	this.destinationHost=inputVar;
    }
    public PerfSonarHost getDestinationHost(){
	return this.destinationHost;
    }
    // information methods
    public boolean isSingle(){
	if(this.getMatrixType().indexOf("APD")>-1){
	    return true;
	}else{
	    return false;
	}
    }

    public HtmlTableCell htmlTableCell(){
	HtmlTableCell upperCell=upperNode.numericStatusCell();
	if(this.lowerNode==null){
	    return upperCell;
	}else{
	    HtmlTableCell lowerCell=lowerNode.numericStatusCell();
	    String cellText=upperCell.toHtml()+"<BR>"+lowerCell.toHtml();
	    HtmlTable ht = new HtmlTable(1);
	    
	    ht.addCell(new HtmlTableCell(cellText));
	    ht.setBorder(0);
	    ht.setPadding(0);
	    HtmlTableCell resultCell = new HtmlTableCell(ht.toHtml());
	    return resultCell;
	}
    }

    public HtmlTable htmlTable(){
	HtmlTable ht=new HtmlTable(1);
	HtmlTableCell upperCell=upperNode.numericStatusCell();
	ht.addCell(upperCell);
	if(lowerNode!=null){
	    HtmlTableCell lowerCell=lowerNode.numericStatusCell();
	    ht.addCell(lowerCell);
	}
	ht.setBorder(0);
	ht.setPadding(0);
	return ht;	    	    		
    }
}
