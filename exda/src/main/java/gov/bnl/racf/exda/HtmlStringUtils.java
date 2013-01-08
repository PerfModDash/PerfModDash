package gov.bnl.racf.exda;

public class HtmlStringUtils
{  

     
    public HtmlStringUtils()
	{
	}
    public static String addFontTags(String inputString,int fontSize){
	String startTag="<font size=\""+fontSize+"\">";
	String endTag="</font>";
	String result=startTag+inputString+endTag;
	return result;
    }
}
