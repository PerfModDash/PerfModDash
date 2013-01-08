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
import org.json.simple.JSONObject;


public class User implements Comparable
{  

    HttpServletRequest request=null;

    private DbConnector db=null;
    private ParameterBag parameterBag=null;

    private java.security.cert.X509Certificate userCert=null;
    private String userDN=null;
    private boolean authenticated=false;
    private String userEmail="";
    private String userName="";

    private String userRoles="";

    private int userId=-1;

    private ListOfKnownUsers listOfKnownUsers=null;

    PrintWriter out=null;

    public User(ParameterBag parameterBag,DbConnector inputDb){
	this.db=inputDb;
	this.parameterBag=parameterBag;
    }

    public User(ParameterBag parameterBag,DbConnector inputDb,String inputDN){
	this.db=inputDb;
	this.parameterBag=parameterBag;
	this.setUserDn(inputDN);
	this.loadByDn();
    }
    public User(ParameterBag parameterBag,DbConnector inputDb,int inputId){
	this.db=inputDb;
	this.parameterBag=parameterBag;
	this.setUserId(inputId);
	this.loadById();
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
	return this.getUserId();
    }
    public void setId(int inputVar){
	this.setUserId(inputVar);
    }
    public int getUserId(){
	return this.userId;
    }
    public void setUserId(int inputVar){
	this.userId=inputVar;
    }



    // === load methods === //
    public boolean loadById(){
	boolean result=true;
	String query="SELECT * FROM Users WHERE userId=?";	
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getUserId());
	    ResultSet rs=queryPS.executeQuery();
	    result=this.unpackResultSet(rs);
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load user by query");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" userId="+this.getUserId());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}	
	return result;
    }	
    public boolean loadByDn(){
	boolean result=true;
	String query="SELECT * FROM Users WHERE dn=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.getUserDn());
	    ResultSet rs=queryPS.executeQuery();
	    result=this.unpackResultSet(rs);
	    rs.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to load user by query");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" dn="+this.getUserDn());	    
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}	
	return result;
    }
    //=== unpack methods ===//
    public boolean unpackResultSet(ResultSet rs){
	boolean result=true;
	try{
	     while(rs.next()){
		 this.setUserId(rs.getInt("userId"));
		 this.setUserRoles(rs.getString("Roles"));
		 this.setUserName(rs.getString("Name"));
		 this.setUserEmail(rs.getString("Email"));
		 this.setUserDn(rs.getString("dn"));
	     }
	}catch(Exception e){
	    result=false;
	    System.out.println(getClass().getName()+" error while unpacking result set");
	    System.out.println(getClass().getName()+" "+e);
	}	     
	return result;
    }
    public void unpackParameterBag(){
	if(this.parameterBag.variableNotEmpty("userName")){
	    this.setUserName(this.parameterBag.userName);
	}
	if(this.parameterBag.variableNotEmpty("userDN")){
	    this.setUserDn(this.parameterBag.userDN);
	}
	if(this.parameterBag.variableNotEmpty("userEmail")){
	    this.setUserEmail(this.parameterBag.userEmail);
	}
	if(this.parameterBag.variableNotEmpty("userRoles")){
	    this.setUserRoles(this.parameterBag.userRoles);
	}
	if(this.parameterBag.variableNotEmpty("id")){
	    this.setUserId(Integer.parseInt(this.parameterBag.id));
	}
    }
    // === save,insert and delete  methods ===//
    public boolean saveUser(){
	boolean result=true;
	String query="UPDATE Users SET dn=?,Roles=?,Name=?,Email=? WHERE UserId=?";
	PreparedStatement queryPS=null;
	try{

	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.getUserDn());
	    queryPS.setString(2,this.getUserRoles());
	    queryPS.setString(3,this.getUserName());
	    queryPS.setString(4,this.getUserEmail());
	    queryPS.setInt(5,this.getUserId());
	    int updateResult = queryPS.executeUpdate();
	    System.out.println(getClass().getName()+" updateResult="+updateResult);
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to save user ");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" dn="+this.getUserDn());
	    System.out.println(getClass().getName()+" Roles="+this.getUserRoles());	    
	    System.out.println(getClass().getName()+" Name="+this.getUserName());
	    System.out.println(getClass().getName()+" Email="+this.getUserEmail());
	    System.out.println(getClass().getName()+" userId="+this.getUserId());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}finally{
	    try{
		queryPS.close();
	    }catch(Exception e){
		System.out.println(getClass().getName()+" failed to close prepared statement");
		System.out.println(getClass().getName()+" "+query);
		System.out.println(getClass().getName()+" "+e);	
	    }	
	}	
	return result;
    }
    public boolean insertUser(){
	boolean result=true;
	String query="INSERT INTO Users (dn,Roles,Name,Email) VALUES (?,?,?,?)";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setString(1,this.getUserDn());
	    queryPS.setString(2,this.getUserRoles());
	    queryPS.setString(3,this.getUserName());
	    queryPS.setString(4,this.getUserEmail());
	    queryPS.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to save user ");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" dn="+this.getUserDn());
	    System.out.println(getClass().getName()+" Roles="+this.getUserRoles());	    
	    System.out.println(getClass().getName()+" Name="+this.getUserName());
	    System.out.println(getClass().getName()+" Email="+this.getUserEmail());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	// get the index of the inserted object
	try{
	    ResultSet rs=queryPS.getGeneratedKeys();
	    while (rs.next ()){
		this.setUserId(rs.getInt(1));
	    }
	    rs.close();
	}catch (Exception e){
	    System.out.println(getClass().getName()+" could not obtain key of newly inserted user");
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	
	}	
	return result;
    }
    public boolean deleteUser(){
	boolean result=true;
	String query="DELETE FROM Users WHERE userId=?";
	PreparedStatement queryPS=null;
	try{
	    queryPS= db.getConn().prepareStatement(query);
	    queryPS.setInt(1,this.getUserId());
	    queryPS.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to delete user ");
	    System.out.println(getClass().getName()+" query="+query);
	    System.out.println(getClass().getName()+" userId="+this.getUserId());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    queryPS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query);
	    System.out.println(getClass().getName()+" "+e);	
	}	


	String query2="DELETE FROM UsersAlarms WHERE UserId=?";
	PreparedStatement query2PS=null;
	try{
	    query2PS= db.getConn().prepareStatement(query2);
	    query2PS.setInt(1,this.getUserId());
	    query2PS.executeUpdate();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to delete user ");
	    System.out.println(getClass().getName()+" query2="+query2);
	    System.out.println(getClass().getName()+" UserId="+this.getUserId());
	    System.out.println(getClass().getName()+" "+e);
	    result=false;
	}
	try{
	    query2PS.close();
	}catch(Exception e){
	    System.out.println(getClass().getName()+" failed to close prepared statement");
	    System.out.println(getClass().getName()+" "+query2);
	    System.out.println(getClass().getName()+" "+e);	
	}	
	return result;
    }

    // === packing methods ===//
    private JSONObject toJson(){
	JSONObject json = new JSONObject();

	json.put("userDN", this.getUserDn());
	json.put("userEmail",this.getUserEmail());
	json.put("userName",this.getUserName());
	json.put("userId",new Integer(this.getUserId()));
	
	return json;
    }
    //=== user information ===//
    public boolean knownUser(){
	if(this.getUserId()>0){
	    return true;
	}else{
	    return false;
	}
    }
    // public boolean isAdmin(){
    // 	if(this.getUserRole().indexOf("Admin")!=-1){
    // 	    return true;
    // 	}else{
    // 	    return false;
    // 	}
    // }
    // === links to pages ===//
    private HtmlLink getLinkToSelectDn(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Select DN")  );
	paramBagLocal.addParam("id",Integer.toString(this.getUserId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToAddDn(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Add DN")  );
	paramBagLocal.addParam("id",Integer.toString(this.getUserId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToEditUserPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Edit User")  );
	paramBagLocal.addParam("id",Integer.toString(this.getUserId()) );
	paramBagLocal.addParam("json",this.toJson().toString());	
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToSaveUserPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Save User")  );
	paramBagLocal.addParam("id",Integer.toString(this.getUserId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToInserUserPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Insert User")  );
	paramBagLocal.addParam("id",Integer.toString(this.getUserId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink getLinkToDeleteUserPage(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("Delete User")  );
	paramBagLocal.addParam("id",Integer.toString(this.getUserId()) );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    private HtmlLink     getListOfRegisteredUsers(String linkText){
	ParameterBag paramBagLocal=new ParameterBag();
	paramBagLocal.setRequestUri(this.parameterBag.requestUri);
	paramBagLocal.addParam("page",ParameterBag.pageAddress("List Registered Users")  );
	String linkToDetailPage=paramBagLocal.makeLink();
	HtmlLink link=new HtmlLink(linkToDetailPage,linkText);
	return link;
    }
    // === html pages === //
    public String editUserForm(){
	String result="";
	result=result+"<form>";
	HtmlTable formTable=new HtmlTable(2);

	formTable.addCell("User Name");
	formTable.addCell("<input type=\"text\" name=\"userName\" value=\""+this.getUserName()+"\" /><br>");

	formTable.addCell("User Email");
	formTable.addCell("<input type=\"text\" name=\"userEmail\" value=\""+this.getUserEmail()+"\" /><br>");

	formTable.addCell("User Roles");
	formTable.addCell("<input type=\"text\" name=\"userRoles\" value=\""+this.getUserRoles()+"\" /><br>");

	formTable.addCell("User DN");
	formTable.addCell("<input type=\"text\" name=\"userDN\" value=\""+this.getUserDn()+"\" /><br>");

	result=result+"<br>"+formTable.toHtml()+"<br>";

	result=result+"<input type=\"hidden\" name=\"page\" value=\""+parameterBag.pageAddress("Save User")+"\" />";
	result=result+"<input type=\"hidden\" name=\"id\" value=\""+this.getUserId()+"\" />";

	result=result+"<input type=\"submit\" value=\"Save\" /><br>";
	result=result+"</form>";
	
	return result;
    }
    public String createUserPage(){
	String result="<H1>Create New User</H1><br>";
	this.setUserName("New User");
	
	this.setUserEmail("");
	//this.setUserDn("");
	this.setUserId(-1);
	result=result+this.editUserForm();
	return result;
    }
    public String editUserPage(){
	String result="<H1>Edit User</H1><br>";
	result=result+this.editUserForm();
	result=result+"<br>";
	result=result+this.getListOfRegisteredUsers("<strong>Get list of registered users</strong><br>");
	return result;
    }
    public String saveUserPage(){
	String result="<H1>Save User</H1><br>";
	boolean saveUserResult=false;
	if(this.getUserId()>0){
	    saveUserResult=this.saveUser();
	}else{
	    saveUserResult=this.insertUser();
	}

	if(saveUserResult){
	    result=result+"<strong>User has been saved</strong><br>";
	}else{
	    result=result+"<strong>Failed to save the user, please check the logs.</strong><br>";
	}

	result=result+this.editUserForm();
	result=result+this.getListOfRegisteredUsers("<strong>Get list of registered users</strong><br>");
	return result;
    }
    public String deleteUserPage(){
	String result="<H1>Delete User</H1><br>";
	boolean deleteUserResult=this.deleteUser();
	result=result+"<br>";
	if(deleteUserResult){
	    result=result+"<strong>User has been deleted</strong><br>";
	}else{
	    result=result+"<strong>Failed to delete the user, please check the logs.</strong><br>";
	}
	result=result+"<br>";
	result=result+this.getListOfRegisteredUsers("<strong>Get list of registered users</strong><br>");
	return result;
    }
    // === info methods === //
    public  List<HtmlTableCell> infoTableRow(){
	List<HtmlTableCell> result=new ArrayList<HtmlTableCell>();

	HtmlTableCell cell1=new  HtmlTableCell(this.getUserName());
	cell1.alignLeft();
	result.add(cell1);

	//HtmlTableCell emptyCell=new  HtmlTableCell("  ");	
	//result.add(emptyCell);

	//result.add(emptyCell);


	HtmlLink link2edit=this.getLinkToEditUserPage("edit");
	HtmlTableCell cell2=new  HtmlTableCell(link2edit);
	result.add(cell2);

	//result.add(emptyCell);


	String delete="delete";
	HtmlLink link2delete=this.getLinkToDeleteUserPage("delete");
	HtmlTableCell cell4=new  HtmlTableCell(link2delete);
	result.add(cell4);


	return result;
    }

    public void unpackCerts(java.security.cert.X509Certificate[] certs){
	if (certs != null) {
	    for (int i = 0; i < certs.length; i++) {
		String dn=certs[i].getSubjectDN().toString();
		if (dn.indexOf("People")!=-1){
		    userDN=dn;
		    authenticated=true;
		    userCert=certs[i];
		}
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
		if (listOfKnownUsers.containsDN(userDN)){
		    if (isDOEGrids()){
			return true;
		    }else{
			return false;
		    }
		}else{
		    return false;
		}
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
	User otherUser=(User)obj;
	if (otherUser==null){
	    if(this.getUserDn()==null){
		return true;
	    }else{
		return false;
	    }
	}else{
	    String otherUserDN=otherUser.getDN();
	    if (otherUserDN==null){
		if (this.getUserDn()==null){
		    return true;
		}else{
		    return false;
		}
	    }else{
		if(this.getUserDn()==null){
		    return false;
		}else{
		    return this.getUserDn().equals(otherUserDN);
		}
	    }	
	}
    }
}

