package gov.bnl.racf.exda;

public class ProbeStatus implements Comparable
{  
    public static int UNDEFINED=-1;
    public static int OK=0;
    public static int WARNING=1;
    public static int CRITICAL=2;
    public static int UNKNOWN=3;
    public static int TIMEOUT=4;

    public static String UNDEFINED_STRING="  ";
    public static String UNDEFINED_STRING2="UNDEFINED";
    public static String OK_STRING="OK";
    public static String WARNING_STRING="WARNING";
    public static String CRITICAL_STRING="CRITICAL";
    public static String UNKNOWN_STRING="UNKNOWN";
    public static String TIMEOUT_STRING="TIMEOUT";

    public static String UNDEFINED_STRING_SHORT="  ";
    public static String UNDEFINED_STRING2_SHORT="UNDE";    
    public static String OK_STRING_SHORT="OK";
    public static String WARNING_STRING_SHORT="WARN";
    public static String CRITICAL_STRING_SHORT="CRIT";
    public static String UNKNOWN_STRING_SHORT="UNKN";
    public static String TIMEOUT_STRING_SHORT="TOUT";

    private boolean active=true;
    

    private int status=-1;

    public ProbeStatus( int inputStatus)
	{
	    status=inputStatus;
	}
    public ProbeStatus( String inputStatus)
	{
	    //System.out.println(getClass().getName()+" input probe status="+inputStatus);
	    status=string2intStatus(inputStatus);
	    //System.out.println(getClass().getName()+" translated into status code ="+status); 
	}   
    public ProbeStatus( int inputStatus, String activeInput)
	{
	    status=inputStatus;
	    if (activeInput.equals("Y")){
		active=true;
	    }else{
		active=false;
	    }
	}
    public ProbeStatus( String inputStatus, String activeInput)
	{
	    status=string2intStatus(inputStatus);
	    if (activeInput.equals("Y")){
		active=true;
	    }else{
		active=false;
	    }
	}
    public ProbeStatus( String inputStatus,boolean activeInput)
	{
	    status=string2intStatus(inputStatus);
	    active=activeInput;
	} 

    public int getStatusCode(){
	return status;
    }

    public boolean isOK(){
	if (status==this.OK){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isWARNING(){
	if (status==this.WARNING){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isCRITICAL(){
	if (status==this.CRITICAL){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isUNKNOWN(){
	if (status==this.UNKNOWN){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isUNDEFINED(){
	if (status==this.UNDEFINED){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean isTIMEOUT(){
	if (status==this.TIMEOUT){
	    return true;
	}else{
	    return false;
	}
    }
   
    public HtmlColor color(){
	HtmlColor statusColor=new HtmlColor("brown");
	if (status==UNDEFINED){
	    statusColor=new HtmlColor("white");
	}
	if (status==OK){
	    statusColor=new HtmlColor("green");
	}
	if (status==WARNING){
	    statusColor=new HtmlColor("yellow");
	}
	if (status==CRITICAL){
	    statusColor=new HtmlColor("red");
	}	
	if (status==UNKNOWN){
	    statusColor=new HtmlColor("brown");
	}
	if (this.isTIMEOUT() ){
	    statusColor=new HtmlColor("grey");
	}
	if (!active){
	    statusColor.makeLighter();
	}
	return statusColor;
    }
    public void setActive(){
	active=true;
    }
    public void setInActive(){
	active=false;
    }


    public String statusWord(){
	String result="";
	if (this.isOK()){
	    result=this.OK_STRING;
	}else{
	    if (this.isWARNING()){
		result=this.WARNING_STRING;
	    }else{
		if (this.isCRITICAL()){
		    result= this.CRITICAL_STRING;
		}else{
		    if (this.isUNKNOWN()){
			result= this.UNKNOWN_STRING;
		    }else{
			if (this.isUNDEFINED()){
			    result= this.UNDEFINED_STRING;
			}else{
			    if (this.isTIMEOUT()){
				result= this.TIMEOUT_STRING;
			    }else{
				result=this.UNKNOWN_STRING;
			    }
			}
		    }
		}
	    }
	}
	return result;
    }
    public String statusWordShort(){
	String result="";
	if (this.isOK()){
	    result=this.OK_STRING_SHORT;
	}else{
	    if (this.isWARNING()){
		result=this.WARNING_STRING_SHORT;
	    }else{
		if (this.isCRITICAL()){
		    result= this.CRITICAL_STRING_SHORT;
		}else{
		    if (this.isUNKNOWN()){
			result= this.UNKNOWN_STRING_SHORT;
		    }else{
			if (this.isUNDEFINED()){
			    result= this.UNDEFINED_STRING_SHORT;
			}else{
			    if (this.isTIMEOUT()){
				result= this.TIMEOUT_STRING_SHORT;
			    }else{
				result=this.UNKNOWN_STRING_SHORT;
			    }
			}
		    }
		}
	    }
	}
	return result;
    }

    public String toString(){
	return statusWord();
    }
    public static int string2intStatus(String inputStatus){
	if (inputStatus.equals(OK_STRING)){
	    return OK;
	}else{
	    if (inputStatus.equals(WARNING_STRING) || inputStatus.equals(WARNING_STRING_SHORT)){  
		return WARNING;
	    }else{
		if (inputStatus.equals(CRITICAL_STRING) ||inputStatus.equals(CRITICAL_STRING_SHORT)){
		    return CRITICAL;
		}else{
		    if (inputStatus.equals(UNKNOWN_STRING) ||inputStatus.equals(UNKNOWN_STRING_SHORT)){
			return UNKNOWN;
		    }else{
			if (inputStatus.equals(TIMEOUT_STRING) ||inputStatus.equals(TIMEOUT_STRING_SHORT)){			
			    return TIMEOUT;
			}else{
			    if (inputStatus.equals(UNDEFINED_STRING) ||inputStatus.equals(UNDEFINED_STRING_SHORT) || inputStatus.equals(UNDEFINED_STRING2) || inputStatus.equals(UNDEFINED_STRING2_SHORT)){			
				return UNDEFINED;
			    }
			}
		    }
		}
	    }
	}
	return UNKNOWN;
    }

    public int fixStatusCode(int inputStatus,String probeOutput){
	// correct status probe code in case status is UNKNOWN
	int result=OK;
	if (inputStatus==OK||inputStatus==WARNING||inputStatus==CRITICAL||inputStatus==UNKNOWN){
	    result=inputStatus;
	}else{
	    if(probeOutput.indexOf(OK_STRING)>-1){
		result=OK;
	    }else{
		if(probeOutput.indexOf(WARNING_STRING)>-1){
		    result=WARNING;
		}else{
		    if(probeOutput.indexOf(CRITICAL_STRING)>-1){
			result=CRITICAL;
		    }else{
			result=UNKNOWN;
		    }
		}
	    }
	    
	}
	return result;
    }

    public int statusLevel(){
	int level=-1;
	if (this.isUNDEFINED()){
	    level=-1;
	}
	if (this.isOK()){
	    level=0;
	}
	if (this.isUNKNOWN()){
	    level=1;
	}
	if (this.isTIMEOUT()){
	    level=2;
	}	
	if (this.isWARNING()){
	    level=3;
	}
	if (this.isCRITICAL()){
	    level=4;
	}	
	return level;
    }
    public static int statusLevel2statusCode(int statusLevel){
	int result=UNDEFINED;
	if(statusLevel==-1){
	    result=UNDEFINED;
	}
	if(statusLevel==0){
	    result=OK;
	}
	if(statusLevel==1){
	    result=UNKNOWN;
	}
	if(statusLevel==2){
	    result=TIMEOUT;
	}
	if(statusLevel==3){
	    result=WARNING;
	}	
	if(statusLevel==3){
	    result=CRITICAL;
	}
	return result;
    }

    public void setOK(){
	status=this.OK;
    }
    public void setWARNING(){
	status=this.WARNING;
    }    
    public void setCRITICAL(){
	status=this.CRITICAL;
    }   
    public void setUNKNOWN(){
	status=this.UNKNOWN;
    } 
    public void setTIMEOUT(){
	status=this.UNKNOWN;
    }    
    public int getStatusInt(){
	return this.status;
    }

    public int compareTo(Object obj) {
	ProbeStatus otherStatus=(ProbeStatus)obj;
	int otherStatusLevel=otherStatus.statusLevel();
	int thisStatusLevel=statusLevel();
	if(thisStatusLevel>otherStatusLevel){
	    return 1;
	}else{
	    if(thisStatusLevel==otherStatusLevel){
		return 0;
	    }else{
		return -1;
	    }
	}
    }    
   
}
