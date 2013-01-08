package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.util.Hashtable;
import java.util.Enumeration;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class NodeMatrixElement
{  

    private String moduleName="NodeMatrixElement";

    private Object sourceNode=null;
    private Object destinationNode=null;

    public NodeMatrixElement(Object source,Object destination)
	{
	    sourceNode=source;
	    destinationNode=destination;
	}
    public Object getSource(){
	return sourceNode;
    }
    public Object getDestination(){
	return destinationNode;
    }
}
