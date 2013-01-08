package gov.bnl.racf.exda;

public class IntervalSelector
{  

    private ParameterBag parameterBag = null;

    private String intervalVariable="state_time";
    private int timeZoneShift=0;

    public IntervalSelector(ParameterBag inputParameterBag)
	{
	    parameterBag=(ParameterBag)inputParameterBag.clone();
	}
    public IntervalSelector(ParameterBag inputParameterBag,String inputIntervalVariable,int inputTimeZoneShift)
	{
	    parameterBag=(ParameterBag)inputParameterBag.clone();
	    intervalVariable=inputIntervalVariable;
	    timeZoneShift=inputTimeZoneShift;
	}

    public void setTimeVariable(String timeVariable){
	intervalVariable=timeVariable;
    }

    public void setTimeZoneShift(int shift){
	timeZoneShift=shift;
    }

 
    public String toHtml(){
	String form="";

	form="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">";	
	form = form + "<table>";
	form = form + "<tr>";
	form = form + "<td><input type=\"radio\" name=\"interval\" value=\"1\"  /> </td><td> Past 6 hours </td>";
	form = form + "<td><input type=\"radio\" name=\"interval\" value=\"2\"  /> </td><td> Past 12 hours </td>";
	form = form + "<td><input type=\"radio\" name=\"interval\" value=\"3\"  /> </td><td> Past 24 hours </td>";
	form = form + "<td><input type=\"radio\" name=\"interval\" value=\"4\"  /> </td><td> Past 48 hours </td>";
	form = form + "<td><input type=\"radio\" name=\"interval\" value=\"5\"  /> </td><td> Past week</td>";
	form = form + "<td><input type=\"radio\" name=\"interval\" value=\"6\"  /> </td><td> Past month</td>";
	form = form + "<td><input type=\"radio\" name=\"interval\" value=\"7\"  /> </td><td> Past year</td>";
	form = form + "<td><input type=\"radio\" name=\"interval\" value=\"8\"  /> </td><td> All data</td>";

   
	form = form + "<td><input type =\"submit\" value =\"Select time interval\" /></td>";
	form = form + "</tr>";
	form = form + "</table>";
	if (parameterBag.variableNotEmpty(parameterBag.page)){
	    form = form + "<input type=\"hidden\" name=\"page\" value=\"" +parameterBag.page+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.src)){
	    form = form + "<input type=\"hidden\" name=\"src\" value=\"" +parameterBag.src+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.dst)){
	    form = form + "<input type=\"hidden\" name=\"dst\" value=\"" +parameterBag.dst+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.mon)){
	    form = form + "<input type=\"hidden\" name=\"mon\" value=\"" +parameterBag.mon+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.host)){
	    form = form + "<input type=\"hidden\" name=\"host\" value=\"" +parameterBag.host+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.service)){
	    form = form + "<input type=\"hidden\" name=\"service\" value=\"" +parameterBag.service+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.site)){
	    form = form + "<input type=\"hidden\" name=\"site\" value=\"" +parameterBag.site+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.serviceName)){
	    form = form + "<input type=\"hidden\" name=\"serviceName\" value=\"" +parameterBag.serviceName+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.hostName)){
	    form = form + "<input type=\"hidden\" name=\"hostName\" value=\"" +parameterBag.hostName+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.probeId)){
	    form = form + "<input type=\"hidden\" name=\"probeId\" value=\"" +parameterBag.probeId+  "\">";
	}
	if (parameterBag.variableNotEmpty(parameterBag.id)){
	    form = form + "<input type=\"hidden\" name=\"id\" value=\"" +parameterBag.id+  "\">";
	}
	form = form + "</form> ";
	
	return form;
    }

    public String toString(){
	String result=toHtml();
	return result;
    }

    public String buildQuery(String interval){
	String intervalQuery="AND TIME_VARIABLE > DATE_SUB(CURRENT_TIME,INTERVAL 24 HOUR) ORDER BY TIME_VARIABLE DESC";
	if (interval.equals("1")){
		intervalQuery="AND TIME_VARIABLE > DATE_SUB(CURRENT_TIME,INTERVAL 6 HOUR) ORDER BY TIME_VARIABLE DESC";
	    }
	if (interval.equals("2")){	
		intervalQuery="AND TIME_VARIABLE > DATE_SUB(CURRENT_TIME,INTERVAL 12 HOUR) ORDER BY TIME_VARIABLE DESC";
	    }
	if (interval.equals("3")){
		intervalQuery="AND TIME_VARIABLE > DATE_SUB(CURRENT_TIME,INTERVAL 24 HOUR) ORDER BY TIME_VARIABLE  DESC";	
	    }
	if (interval.equals("4")){
		intervalQuery="AND TIME_VARIABLE > DATE_SUB(CURRENT_TIME,INTERVAL 48 HOUR) ORDER BY TIME_VARIABLE  DESC";
	    }
	if (interval.equals("5")){
		intervalQuery="AND TIME_VARIABLE > DATE_SUB(CURRENT_TIME,INTERVAL 168 HOUR) ORDER BY TIME_VARIABLE DESC";
	    }
	if (interval.equals("6")){
		intervalQuery="AND TIME_VARIABLE > DATE_SUB(CURRENT_TIME,INTERVAL 720 HOUR) ORDER BY TIME_VARIABLE  DESC";
	    }
	if (interval.equals("7")){
		intervalQuery="AND TIME_VARIABLE > DATE_SUB(CURRENT_TIME,INTERVAL 8760 HOUR) ORDER BY TIME_VARIABLE DESC";
	    }
	if (interval.equals("8")){
		intervalQuery="AND 1=1 ORDER BY TIME_VARIABLE DESC";     
	    }
	intervalQuery=intervalQuery.replace("TIME_VARIABLE",intervalVariable);
	if (timeZoneShift==0){
	    intervalQuery=intervalQuery.replace("CURRENT_TIME","NOW()");
	}else{
	    intervalQuery=intervalQuery.replace("CURRENT_TIME","DATE_SUB(NOW(),INTERVAL "+ Integer.toString(timeZoneShift) +" HOUR)");
	}

	return intervalQuery;
    }

}

