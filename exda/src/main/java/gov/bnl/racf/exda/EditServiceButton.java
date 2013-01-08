package gov.bnl.racf.exda;
import java.io.*;
import java.sql.*;
import java.lang.Class;
public class EditServiceButton
{  
    private ParameterBag parameterBag = null;
    private DbConnector db=null;

    private String editPage=null;

    private String intervalVariable="state_time";
    private int timeZoneShift=0;

    public EditServiceButton(ParameterBag inputParameterBag,DbConnector inputDb, String editPageInput)
	{
	    parameterBag=(ParameterBag)inputParameterBag.clone();
	    db=inputDb;
	    editPage=editPageInput;
	}
 
    public String toHtml(){
	String form="";

	form="<form name=\"input\" action=\""+parameterBag.requestUri + "\" method=\"get\">";	


	form = form + "<input type=\"hidden\" name=\"page\" value=\"" +parameterBag.pageAddress(editPage)+  "\">";
	form = form + "<input type=\"hidden\" name=\"probeId\" value=\""+parameterBag.probeId+"\">";
	form=form+"<input type=\"submit\" value=\"Edit Service\" />";
	form = form + "</form> ";
	
	return form;
    }
   

    public String toString(){
	String result=toHtml();
	return result;
    }
}

