package gov.bnl.racf.exda;

import java.lang.Class;
import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


import java.sql.*;

import java.util.Collections;

public class ListOfKnownUsers
{  
    private ParameterBag parameterBag = null;

    private DbConnector db=null;

    //private List<String> knownUsers = new ArrayList<String>();
    private List<UserInfo> knownUsers = new ArrayList<UserInfo>();

    public ListOfKnownUsers(DbConnector inputDb)
	{
	    db=inputDb;
	    loadKnownUsers();
	}
    public void loadKnownUsers(){
	String selectUsersQuery="SELECT * from Users";
	PreparedStatement query=null;
	try{
	    query= db.getConn().prepareStatement(selectUsersQuery);
	    ResultSet rs=query.executeQuery();
	    while(rs.next()){
		String dn=rs.getString("dn");
		String userRoles=rs.getString("Roles");
		boolean approved=true;
		knownUsers.add(new UserInfo(dn,approved,userRoles));
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error when reaing list of known users");
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    query.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" error when closing prepared statement");
	    System.out.println(getClass().getName()+" "+selectUsersQuery);
	    System.out.println(getClass().getName()+" "+e);
	}	    
    }
    public boolean containsDN(String inputDN){
	boolean result=false;
	Iterator itr=knownUsers.iterator();
	while (itr.hasNext()){
	    String currentDn=(String)((UserInfo)itr.next()).getDN();
	    if(currentDn.equals(inputDN)){
		result=true;
		break;
	    }
	}
	return result;
    }
    public List<UserInfo> getList(){
	return knownUsers;
    }
}
