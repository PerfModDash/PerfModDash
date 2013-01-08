package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.util.Hashtable;
import java.util.Enumeration;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class NodeMatrix
{  

    private String moduleName="NodeMatrix";
    private Hashtable hashtable = null;

    private List<String> sourceNames=new ArrayList<String>();
    private List<String> destinationNames=new ArrayList<String>();

    public NodeMatrix()
	{
	   hashtable = new Hashtable(); 
	}
    public void add(String source, String destination, Object obj ){
	if(!hashtable.containsKey(source)){
	    hashtable.put(source,new Hashtable());
	}
	Hashtable row=(Hashtable)hashtable.get(source);
	row.put(destination,obj);
	if(!sourceNames.contains(source)){
	    sourceNames.add(source);
	}
	if(!destinationNames.contains(destination)){
	    destinationNames.add(destination);
	}
	
    }
    public Object get(String source, String destination){
	Object result=null;
	if (hashtable.containsKey(source)){
	    if (((Hashtable)hashtable.get(source)).containsKey(destination)){
		result=((Hashtable)hashtable.get(source)).get(destination);
	    }
	}
	return result;
    }
    public List<String> getSourceNames(){
	return sourceNames;

    }
    public List<String> getDestinationNames(){
	return destinationNames;
    }



}
