package gov.bnl.racf.exda;

import java.sql.*;
import java.util.Calendar;
import java.io.*;

import java.lang.Class;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class ListOfUsers
{  

    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private ResultSet rs=null;

    private List<User>listOfUsers=new ArrayList<User>();


    private List<String> listOfRegisteredUserDNs = new ArrayList<String>();
    private List<String> listOfRecordedUserDNs   = new ArrayList<String>();
    private List<String> listOfNotRegisteredDNs  = new ArrayList<String>();


    public ListOfUsers(ParameterBag paramBag,   DbConnector inputDb)
	{
	    this.parameterBag=paramBag;
	    this.db=inputDb;
	    this.loadListOfUsers();
	}
    private void loadListOfUsers(){

	String getQuery="select userId from Users";
	PreparedStatement getQueryPS= null;
	try{
	    getQueryPS= db.getConn().prepareStatement(getQuery);

	    rs=getQueryPS.executeQuery();
	    int count = 0;
	    while (rs.next ())
		{
		    int userId=rs.getInt("userId");
		    this.listOfUsers.add(new User(this.parameterBag,this.db,userId));
		    count=count+1;
		}
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when reading users from database");
	    System.out.println(getClass().getName()+" "+getQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    getQueryPS.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" error occured when closing prepared statement");
	    System.out.println(getClass().getName()+" "+getQuery);
	    System.out.println(getClass().getName()+" "+e);
	}
    }

    public HtmlLink  getLinkToCreateUserPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Create User")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToSelectDnPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Select DN")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }

    public HtmlTable manipulationHtmlTable(){
	HtmlTable ht=new HtmlTable(3);
	ht.setBorder(1);
	ht.setPadding(0);
	
	Iterator itr = this.listOfUsers.iterator(); 
	while(itr.hasNext()) {
	    User user=(User)itr.next();
	    //ht.addFullRow(user.infoTableRow());
	    ht.addRow(user.infoTableRow());
	} 
	return ht;
    }

    public String manipulationHtmlPage(){
	String result="<H1>Registered Users</H1>";
	result=result+"<br>";
	result=result+this.getLinkToCreateUserPage("<strong>Click here to create new user</strong>")+" <strong>or</strong> ";
	result=result+this.getLinkToSelectDnPage("<strong>click here to create new user from list of known DN's</strong>")+"<BR>";

	result=result+this.manipulationHtmlTable().toHtml();
	return result;
    }
    // === get list of DN's === //
    private void loadRecordedDNs(){
	String query="SELECT DISTINCT dn FROM AccessHistory WHERE NOT dn is NULL AND NOT dn='' AND NOT dn='unknown'";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    ResultSet rs = queryPS.executeQuery();
	    while(rs.next()){
		 String dn = rs.getString("dn");
		 this.listOfRecordedUserDNs.add(dn);
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to get list of dns");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	
	}	
	
    }
    private void loadRegisteredDNs(){
	String query="SELECT DISTINCT dn FROM Users WHERE NOT dn is NULL AND NOT dn='' AND NOT dn='unknown'";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    ResultSet rs = queryPS.executeQuery();
	    while(rs.next()){
		 String dn = rs.getString("dn");
		 this.listOfRegisteredUserDNs.add(dn);
	    }
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to get list of dns from Users table");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" "+e);
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	
	}	
    }
    private void getListOfNotRegisteredDNs(){
	//private List<String> listOfRegisteredUserDNs = new ArrayList<String>();
	//private List<String> listOfRecordedUserDNs   = new ArrayList<String>();	
	this.loadRegisteredDNs();
	this.loadRecordedDNs();
	this.listOfNotRegisteredDNs.clear();

	Iterator iter = this.listOfRecordedUserDNs.iterator();
	while(iter.hasNext()){
	    String currentDN=(String)iter.next();
	    if(this.listOfRegisteredUserDNs.contains(currentDN)){
		// do nothing
	    }else{
		this.listOfNotRegisteredDNs.add(currentDN);
	    }
	}
	
    }
    private String getTableOfNotRegisteredDNs(){
	String result="";
	result=result+"<form>";	

	HtmlTable ht = new HtmlTable(2);
	ht.setPadding(0);
	ht.setBorder(0);

	this.getListOfNotRegisteredDNs();
	
	Iterator iter = this.listOfNotRegisteredDNs.iterator();
	while(iter.hasNext()){
	    String dn=(String)iter.next();
	    String cell1Content="<input type=\"radio\" name=\"userDN\" value=\"" +dn + "\">";
	    HtmlTableCell cell1=new HtmlTableCell(cell1Content);
	    String cell2Content=dn;
	    HtmlTableCell cell2=new HtmlTableCell(cell2Content);
	    cell2.alignLeft();
	    ht.addCell(cell1);
	    ht.addCell(cell2);
	}
	result=result+ht.toHtml();
	result=result+"<BR>";
	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Add DN")+"\" />";
	result=result+"<input type=\"submit\" value=\"Create user with the selected DN\" /><br>";
	
	return result;
    }
    public String displayKnownDNPage(){
	String result="<H1>List of DN's of users widely believed to be real persons:</H1>";
	result=result+"<br>";
	result=result+this.getTableOfNotRegisteredDNs();
	return result;
    }

}
