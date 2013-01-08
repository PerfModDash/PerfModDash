package gov.bnl.racf.exda;

import java.lang.Class;
import java.io.*;


import java.util.Collections;

public class UserInfo 
{  
    private String userDN=null;
    private boolean authenticated=false;
    private boolean approved=false;

    private String roles = "";


    public UserInfo(String inputDN,boolean inputApproved, String inputRoles)
	{
	    userDN= inputDN;
	    approved=inputApproved;
	    roles=inputRoles;
	}
 
    public boolean isAuthenticated(){
	return authenticated;
    }
    public boolean isApproved(){
	return approved;
    }
    public String getRoles(){
	return roles;
    }
    public boolean hasRole(String role){
	if (roles.indexOf(role)!=-1){
	    return true;
	}else{
	    return false;
	}
    }
    public String getDN(){
	return userDN;
    }

    public boolean isDOEGrids(){
	if(userDN.indexOf("doegrids")!=-1){
	    return true;
	}else{
	    return false;
	}
    }


}

