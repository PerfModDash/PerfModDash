package gov.bnl.racf.exda;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


public class ParameterBag implements Cloneable
{  

    public String id=null;

    public String interval="4";
    
    public String src=null;
    public String dst=null;
    public String mon=null;
    public String site=null;
    public String host=null;
    public String ip=null;
    public String service=null;

    public String serviceName=null;
    public String hostName=null;

    public String externalUrl=null;

    public String probeId=null;

    public String siteName=null;

    public String cloudName=null;

    public String schedulerName=null;

    public String active=null;

    public String readOnly=null;

    public String page="0";

    // execution parameters
    public String workDirectory=null;
    public String requestUri=null;
    public String serverName=null;
    public String serverPort=null;
    public String servletPath=null;

    public String executeNow=null;
    public String validUser=null;

    private boolean userOk=false;

    public String toggleActive=null;

    public String checkInterval=null;
    public String probeCommand=null;

    public String serviceMatrixName=null;
    public String serviceMatrixType=null;
    public String permament=null;

    public String save=null;

    public UserInfo user=null;

    String message=null;

    // user parameters
    public String userName="";
    public String userEmail="";
    public String userRoles="";
    public String userDN="";

    // alarm parameters
    public String alarmName="";
    public String alarmDescription="";
    //public String checkInterval=null; defined above
    public String notificationInterval=null;
    public String alarmUrl="";
    public String alarmText="";

    public String consecutiveOK = "";
    public String consecutiveNotOK = "";
    public String consecutiveOKCut = "";
    public String consecutiveNotOKCut = "";    



    public String numberOfChannelsForWarning="";
    public String numberOfChannelsForCritical="";
    public String numberOfChannelsForUnknown="";

    public List<Integer>listOfUserIds=new ArrayList<Integer>();    
    public List<Integer>listOfMatrixIds=new ArrayList<Integer>();    
    public List<Integer>listOfSiteIds=new ArrayList<Integer>();    
    public List<Integer>listOfServiceIds=new ArrayList<Integer>();    

    public List<Integer>listOfIds=new ArrayList<Integer>();

    public ParameterBag()
	{

	}

    public Object clone()
    {
	try
	    {
		return super.clone();
	    }
	catch ( CloneNotSupportedException e )
	    {
		return null;
	    }
    }

    public void addListOfIds( List<Integer>listOfIds){
	this.listOfIds=listOfIds;
    }
    public void addListOfUserIds( List<Integer>listOfIds){
	this.listOfUserIds=listOfIds;
    }
    public void addListOfSiteIds( List<Integer>listOfIds){
	this.listOfSiteIds=listOfIds;
    }
    public void addListOfMatrixIds( List<Integer>listOfIds){
	this.listOfMatrixIds=listOfIds;
    }
    public void addListOfServiceIds( List<Integer>listOfIds){
	this.listOfServiceIds=listOfIds;
    }

    public void setUser(UserInfo inputUser){
	user=inputUser;
    }
    public UserInfo getUser(){
	return user;
    }

    public static boolean variableNotEmpty(String inputVariable){
	// return true if variable is not null and not ""
	if(inputVariable==null){
	    return false;
	}else{
	    if(inputVariable.trim().equals("")){
		return false;
	    }else{
		return true;
	    }
	}
    }
    public boolean userIsValid(){
	return userOk;
    }
    public boolean swapActiveInactive(){
	if(toggleActive.equals("Y")){
	    return true;
	}else{
	    return false;
	}
    }

    public void addParam(String key, String value){
	// update value of parameter key to value
	if (value==null){
	    value="";
	}
	if (key.equals("consecutiveOK")){
	    this.consecutiveOK=value;
	}
	if (key.equals("consecutiveNotOK")){
	    this.consecutiveNotOK=value;
	}
	if (key.equals("consecutiveOKCut")){
	    this.consecutiveOKCut=value;
	}
	if (key.equals("consecutiveNotOKCut")){
	    this.consecutiveNotOKCut=value;
	}

	if (key.equals("alarmText")){
	    this.alarmText=value;
	}
	if (key.equals("alarmUrl")){
	    this.alarmUrl=value;
	}
	if (key.equals("numberOfChannelsForWarning")){
	    this.numberOfChannelsForWarning=value;
	}
	if (key.equals("numberOfChannelsForCritical")){
	    this.numberOfChannelsForCritical=value;
	}
	if (key.equals("numberOfChannelsForUnknown")){
	    this.numberOfChannelsForUnknown=value;
	}
	if (key.equals("notificationInterval")){
	    this.notificationInterval=value;
	}
	if (key.equals("checkInterval")){
	    this.checkInterval=value;
	}
	if (key.equals("alarmDescription")){
	    this.alarmDescription=value;
	}
	if (key.equals("alarmName")){
	    this.alarmName=value;
	}
	if (key.equals("userDN")){
	    this.userDN=value;
	}
	if (key.equals("userRoles")){
	    this.userRoles=value;
	}
	if (key.equals("userEmail")){
	    this.userEmail=value;
	}
	if (key.equals("userName")){
	    this.userName=value;
	}
	if (key.equals("readOnly")){
	    this.readOnly=value;
	}
	if (key.equals("serviceMatrixType")){
	    this.serviceMatrixType=value;
	}
	if (key.equals("serviceMatrixName")){
	    this.serviceMatrixName=value;
	}
	if (key.equals("externalUrl")){
	    externalUrl=value;
	}
	if (key.equals("active")){
	    active=value;
	}
	if (key.equals("ip")){
	    ip=value;
	}
	if (key.equals("id")){
	    id=value;
	}
	if (key.equals("message")){
	    message=value;
	}
	if (key.equals("cloudName")){
	    cloudName=value;
	}
	if (key.equals("toggleActive")){
	    toggleActive=value;
	}
	if (key.equals("save")){
	    save=value;
	}
	if (key.equals("checkInterval")){
	    checkInterval=value;
	}
	if (key.equals("probeCommand")){
	    probeCommand=value;
	}
	if (key.equals("probeId")){
	    probeId=value;
	}
	if (key=="validUser"){
	    validUser=value;
	    userOk=true;
	}
	if (key=="siteName"){
	    siteName=value;
	}
	if (key=="executeNow"){
	    executeNow=value;
	}
	if (key=="serviceName"){
	    serviceName=value;
	}
	if (key=="hostName"){
	    hostName=value;
	}
	if (key=="schedulerName"){
	    schedulerName=value;
	}		
	if (key=="src"){
	    src=value;
	}
	if (key=="dst"){
	    dst=value;
	}
	if (key=="mon"){
	    mon=value;
	}
	if (key=="site"){
	    site=value;
	}
	if (key=="host"){
	    host=value;
	}
	if (key=="service"){
	    service=value;
	}
	if (key=="page"){
	    page=value;
	}
	if (key=="interval"){
	    interval=value;
	}
	if (key=="workDirectory"){
	    workDirectory=value;
	}
	if (key=="requestUri"){
	    requestUri=value;
	}
	if (key=="serverName"){
	    serverName=value;
	}
	if (key=="serverPort"){
	    serverPort=value;
	}
	if (key=="servletPath"){
	    servletPath=value;
	}	
    }
    public void setRequestUri(String requestUri){
	this.requestUri=requestUri;
    }
    public void setWorkDirectory(String workDirectory){
	this.workDirectory=workDirectory;
    }

    public String makeLink(){
	String result="";
	result=requestUri+"?";
	if (variableNotEmpty(externalUrl)){
	    result=result+"externalUrl="+this.externalUrl+"&";
	}
	if (variableNotEmpty(this.serviceMatrixName)){
	    result=result+"serviceMatrixName="+this.serviceMatrixName+"&";
	}
	if (variableNotEmpty(this.serviceMatrixType)){
	    result=result+"serviceMatrixType="+this.serviceMatrixType+"&";
	}
	if (variableNotEmpty(active)){
	    result=result+"active="+this.active+"&";
	}
	if (variableNotEmpty(ip)){
	    result=result+"ip="+this.ip+"&";
	}
	if (variableNotEmpty(id)){
	    result=result+"id="+this.id+"&";
	}
	if (variableNotEmpty(executeNow) && !executeNow.equals("no")){
	    result=result+"executeNow="+executeNow+"&";
	}
	if (variableNotEmpty(toggleActive)){
	    result=result+"toggleActive="+toggleActive+"&";
	}
	if (variableNotEmpty(save)){
	    result=result+"save="+save+"&";
	}
	if (variableNotEmpty(probeCommand)){
	    result=result+"probeCommand="+probeCommand+"&";
	}
	if (variableNotEmpty(checkInterval)){
	    result=result+"checkInterval="+checkInterval+"&";
	}
	if (variableNotEmpty(probeId)){
	    result=result+"probeId="+probeId+"&";
	}
	if (variableNotEmpty(validUser)){
	    result=result+"validUser="+validUser+"&";
	}
	if (variableNotEmpty(serviceName)){
	    result=result+"serviceName="+serviceName+"&";
	}
	if (variableNotEmpty(schedulerName)){
	    result=result+"schedulerName="+this.schedulerName+"&";
	}
	if (variableNotEmpty(cloudName)){
	    result=result+"cloudName="+cloudName+"&";
	}
	if (variableNotEmpty(siteName)){
	    result=result+"siteName="+siteName+"&";
	}
	if (variableNotEmpty(hostName)){
	    result=result+"hostName="+hostName+"&";
	}	
	if (variableNotEmpty(interval)){
	    result=result+"interval="+interval+"&";
	}
	if (variableNotEmpty(src)){
	    result=result+"src="+src+"&";
	}
	if (variableNotEmpty(dst)){
	    result=result+"dst="+dst+"&";
	}
	if (variableNotEmpty(mon)){
	    result=result+"mon="+mon+"&";
	}
	if (variableNotEmpty(site)){
	    result=result+"site="+site+"&";
	}
	if (variableNotEmpty(host)){
	    result=result+"host="+host+"&";
	}
	if (variableNotEmpty(service)){
	    result=result+"service="+service+"&";
	}	
	if (variableNotEmpty(page)){
	    result=result+"page="+page+"&";
	}
	if (variableNotEmpty(message)){
	    result=result+"messasge="+message+"&";
	}		    
	return result;
    }
 
    public String toHtml(){
	String result="";
	result=result+"id="+this.id+"<br>";
	result=result+"ip="+this.ip+"<br>";
	result=result+"interval="+interval+"<br>";
	result=result+"serviceName="+serviceName+"<br>";
	result=result+"hostName="+hostName+"<br>";	
	result=result+"src="+src+"<br>";
	result=result+"dst="+dst+"<br>";
	result=result+"mon="+mon+"<br>";
	result=result+"site="+site+"<br>";
	result=result+"host="+host+"<br>";
	result=result+"service="+service+"<br>";
	result=result+"page="+page+"<br>";
	result=result+"workDirectory="+workDirectory+"<br>";
	result=result+"requestUri="+requestUri+"<br>";
	result=result+"executeNow="+executeNow+"<br>";
	result=result+"validUser="+validUser+"<br>";
	result=result+"probeId="+probeId+"<br>";
	result=result+"save="+save+"<br>";
	result=result+"message="+message+"<br>";
	result=result+"serviceMatrixName="+this.serviceMatrixName+"<br>";
	result=result+"serviceMatrixType="+this.serviceMatrixType+"<br>";
	//result=result+"="++"<br>";
	return result;
    }

    public static String pageAddress(String pageName){
	String result="0";
	if (pageName.equals("Main")){
		result="";
	}
	if (pageName.equals("Throughput Matrix")){
		result="0";
	}	
	if (pageName.equals("Throughput Node")){
		result="2";
	}
	if (pageName.equals("Throughput Node History Table")){
		result="3";
	}	
	if (pageName.equals("Throughput Node History Plot")){
		result="4";
	}
	if (pageName.equals("perfSonar Primitive")){
		result="5";
	}	
	if (pageName.equals("Primitive Node History Table")){
		result="6";
	}
	if (pageName.equals("Link to history table")){
		result="7";
	}
	if (pageName.equals("List Of Services Page")){
		result="8";
	}
	if (pageName.equals("Latency Node History Table")){
		result="9";
	}
	if (pageName.equals("Latency Node History Plot")){
		result="10";
	}	
	if (pageName.equals("Latency Matrix")){
		result="11";
	}
	if (pageName.equals("Latency Node")){
		result="12";
	}	
	if (pageName.equals("List of perfSonar Primitive Services")){
		result="13";
	}
	if (pageName.equals("List of Sites")){
		result="14";
	}
	if (pageName.equals("Site Services")){
		result="15";
	}
	if (pageName.equals("Picture")){
		result="16";
	}
	if (pageName.equals("Edit Primitive Service")){
		result="17";
	}
	if (pageName.equals("Save Primitive Service")){
		result="18";
	}
	if (pageName.equals("Edit Latency Node")){
		result="19";
	}
	if (pageName.equals("Save Latency Node")){
		result="20";
	}
	if (pageName.equals("Edit Throughput Node")){
		result="21";
	}
	if (pageName.equals("Save Throughput Node")){
		result="22";
	}
	if (pageName.equals("Cloud Sites")){
		result="23";
	}
	if (pageName.equals("Cloud Overview")){
		result="25";
	}
	if (pageName.equals("All Clouds")){
		result="26";
	}
	if (pageName.equals("PerfSonar Host")){
		result="24";
	}
	if (pageName.equals("PerfSonar Host Active Form")){
		result="27";
	}
	if (pageName.equals("PerfSonar Host Active Update")){
		result="28";
	}
	if (pageName.equals("Primitive Service Confirm Active Update")){
		result="29";
	}
	if (pageName.equals("Update Active For Primitive Service")){
		result="30";
	}
	if (pageName.equals("List of Matrix Services")){
		result="31";
	}
	if (pageName.equals("Matrix Element")){
		result="32";
	}
	if (pageName.equals("Matrix Element History Table")){
		result="33";
	}
	if (pageName.equals("Matrix Element History Plot")){
		result="34";
	}
	if (pageName.equals("Force Test Matrix Element")){
		result="35";
	}
	if (pageName.equals("Toggle Active")){
		result="36";
	}
	if (pageName.equals("Confirm Active Update")){
		result="37";
	}
	if (pageName.equals("Edit Matrix Element")){
		result="38";
	}
	if (pageName.equals("Save Matrix Element")){
		result="39";
	}
	if (pageName.equals("List of Hosts")){
		result="40";
	}
	if (pageName.equals("Edit Host")){
		result="41";
	}
	if (pageName.equals("Save Host")){
		result="42";
	}
	if (pageName.equals("Clone Host")){
		result="43";
	}
	if (pageName.equals("Delete Host")){
		result="44";
	}
	if (pageName.equals("Confirm Delete Host")){
		result="45";
	}
	if (pageName.equals("List of Matrices")){
		result="46";
	}
	if (pageName.equals("Display Service Matrix")){
		result="47";
	}
	if (pageName.equals("Edit Service Matrix")){
		result="48";
	}
	if (pageName.equals("Delete Service Matrix")){
		result="49";
	}
	if (pageName.equals("Confirm Delete Service Matrix")){
		result="50";
	}
	if (pageName.equals("Create Service Matrix")){
		result="51";
	}
	if (pageName.equals("Save Service Matrix")){
		result="52";
	}
	if (pageName.equals("Add Hosts to Service Matrix")){
		result="53";
	}
	if (pageName.equals("Select Hosts to add to Service Matrix")){
		result="54";
	}
	if (pageName.equals("Remove Hosts from Service Matrix")){
		result="55";
	}
	if (pageName.equals("Create Throughput Service Matrix")){
		result="56";
	}
	if (pageName.equals("Create Latency Service Matrix")){
		result="57";
	}
	if (pageName.equals("Toggle Active ServiceMatrixElement")){
		result="58";
	}
	if (pageName.equals("Manipulate Clouds")){
		result="59";
	}
	if (pageName.equals("Create Cloud")){
		result="60";
	}
	if (pageName.equals("Insert New Cloud")){
		result="61";
	}
	if (pageName.equals("Delete Cloud")){
		result="62";
	}
	if (pageName.equals("Confirm Delete Cloud")){
		result="63";
	}
	if (pageName.equals("Create Site")){
		result="64";
	}
	if (pageName.equals("Display Site")){
		result="65";
	}
	if (pageName.equals("Delete Site")){
		result="66";
	}
	if (pageName.equals("Confirm Delete Site")){
		result="67";
	}
	if (pageName.equals("Manipulate Sites")){
		result="68";
	}
	if (pageName.equals("Insert New Site")){
		result="69";
	}
	if (pageName.equals("Add Remove Hosts to Site")){
		result="70";
	}
	if (pageName.equals("Add Hosts to Site")){
		result="71";
	}
	if (pageName.equals("Remove Hosts from Site")){
		result="72";
	}
	if (pageName.equals("Insert Host")){
		result="73";
	}
	if (pageName.equals("Create Host")){
		result="74";
	}
	if (pageName.equals("Add Remove Primitive Services to Host")){
		result="75";
	}
	if (pageName.equals("Add Primitives to Host")){
		result="76";
	}
	if (pageName.equals("Remove Primitives from Host")){
		result="77";
	}
	if (pageName.equals("Add remove Sites to Cloud Page")){
		result="78";
	}
	if (pageName.equals("Remove Sites From Cloud Page")){
		result="79";
	}
	if (pageName.equals("Add Sites To Cloud Page")){
		result="80";
	}
	if (pageName.equals("Add Remove Matrices to Cloud Page")){
		result="81";
	}
	if (pageName.equals("Remove Matrices From Cloud Page")){
		result="82";
	}
	if (pageName.equals("Add Matrices To Cloud Page")){
		result="83";
	}
	if (pageName.equals("Choose Name for Throughput Matrix")){
		result="84";
	}
	if (pageName.equals("Choose Name for Latency Matrix")){
		result="85";
	}
	if (pageName.equals("Force Test Primitive Service")){
		result="86";
	}
	if (pageName.equals("Manipulate Schedulers")){
		result="87";
	}
	if (pageName.equals("Confirm Delete Scheduler")){
		result="88";
	}
	if (pageName.equals("Delete Scheduler")){
		result="89";
	}
	if (pageName.equals("Edit Scheduler")){
		result="90";
	}
	if (pageName.equals("Create Scheduler")){
		result="91";
	}
	if (pageName.equals("Insert New Scheduler")){
		result="92";
	}
	if (pageName.equals("Save Scheduler")){
		result="93";
	}
	if (pageName.equals("Select Scheduler for Service Matrix")){
		result="94";
	}
	if (pageName.equals("Update Scheduler for Service Matrix")){
		result="95";
	}
	if (pageName.equals("Admin Page")){
		result="96";
	}
	if (pageName.equals("Edit User")){
	    result="97";
	}
	if (pageName.equals("Save User")){
	    result="98";
	}
	if (pageName.equals("Insert User")){
	    result="99";
	}
	if (pageName.equals("Delete User")){
	    result="100";
	}
	if (pageName.equals("Create User")){
	    result="101";
	}
	if (pageName.equals("List Registered Users")){
	    result="102";
	}
	if (pageName.equals("Add DN")){
	    result="103";
	}
	if (pageName.equals("Select DN")){
	    result="104";
	}
	if (pageName.equals("Create Alarm")){
	    result="105";
	}
	if (pageName.equals("Delete Alarm")){
	    result="106";
	}
	if (pageName.equals("Confirm Delete Alarm")){
	    result="107";
	}
	if (pageName.equals("Edit Alarm")){
	    result="108";
	}
	if (pageName.equals("Save Alarm")){
	    result="109";
	}
	if (pageName.equals("Insert Alarm")){
	    result="110";
	}
	if (pageName.equals("List of Alarms")){
	    result="111";
	}
	if (pageName.equals("Add Remove Site to Alarm")){
	    result="112";
	}
	if (pageName.equals("Add Sites to Alarm")){
	    result="113";
	}
	if (pageName.equals("Remove Sites from Alarm")){
	    result="114";
	}
	if (pageName.equals("Site Alarms")){
	    result="115";
	}
	if (pageName.equals("Select Site to Add Alarms")){
	    result="116";
	}
	if (pageName.equals("Select Services From Site to Alarm")){
	    result="117";
	}
	if (pageName.equals("Add Services From Site to Alarm")){
	    result="118";
	}
	if (pageName.equals("Display Alarm Overview")){
	    result="119";
	}
	if (pageName.equals("Remove Primitive Services From Alarm")){
	    result="120";
	}
	if (pageName.equals("Add Or Remove Users To Alarm")){
	    result="121";
	}
	if (pageName.equals("Add Users To Alarm Page")){
	    result="122";
	}
	if (pageName.equals("Remove Users From Alarm Page")){
	    result="123";
	}
	if (pageName.equals("Overview of Alarms")){
	    result="124";
	}
	if (pageName.equals("Create APD Throughput Service Matrix")){
	    result="125";
	}
	if (pageName.equals("Choose Name for APD Throughput Matrix")){
	    result="126";
	}
	if (pageName.equals("Display PerfSonarMatrix")){
	    result="127";
	}
	if (pageName.equals("APD Matrix Element")){
	    result="128";
	}
	if (pageName.equals("Create APD Latency Service Matrix")){
	    result="129";
	}
	if (pageName.equals("Choose Name for APD Latency Matrix")){
	    result="130";
	}
	if (pageName.equals("Choose Name for Traceroute Matrix")){
	    result="131";
	}
	if (pageName.equals("Create Traceroute Service Matrix")){
	    result="132";
	}
	if (pageName.equals("Alarm History Page")){
	    result="133";
	}
	if (pageName.equals("Alarm History Details Page")){
	    result="134";
	}
	if (pageName.equals("Alarm History Message Details Page")){
	    result="135";
	}
	if (pageName.equals("Reset Selected Probes Page")){
	    result="136";
	}
	return result;
    }


    public String toString(){
	String result=toHtml();
	return result;
    }

}

