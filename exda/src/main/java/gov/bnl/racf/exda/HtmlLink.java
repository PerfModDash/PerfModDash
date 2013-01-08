package gov.bnl.racf.exda;

public class HtmlLink
{  
    private String linkUrl="";
    private String linkDisplayedText="";
    private String linkTitle="";

    private HtmlColor linkTextColor=null;
     
    public HtmlLink(String inputText )
	{
	    linkDisplayedText=inputText;
	}
   
    public HtmlLink(String inputUrl, String inputText )
	{
	    linkUrl=inputUrl;
	    linkDisplayedText=inputText;
	}
    public HtmlLink(String inputUrl, String inputText,String inputTitle )
	{
	    linkUrl=inputUrl.trim();
	    linkDisplayedText=inputText.trim();
	    linkTitle=inputTitle.trim();
	}
    public String getUrl(){
	return this.linkUrl;
    }

    public void setTextWhite(){
	linkTextColor=new HtmlColor("white");
    }
   
    public String toHtml(){
	String result="<a ";

	String displayedText=linkDisplayedText;
	if (linkTextColor!=null){
	    result=result+" style=\"color: "+linkTextColor.toHtml()+"\" ";
	    //result=result+" style=\"color: #FFFFFF\" ";
	    //displayedText="<FONT COLOR=\""+linkTextColor.toHtml()+"\">"+displayedText+"</FONT>";
	}

	if ( !emptyLink()){
	    if (linkTitle.equals("")){
		result=result+" href=\"" + linkUrl + "\">"+displayedText+"</a>";
	    }else{
		result=result+" href=\"" + linkUrl + "\" title=\""+linkTitle+"\">"+displayedText+"</a>";
	    }
	}else{
	    result=linkDisplayedText;
	}
	return result;
    }

    public String toString(){
	return toHtml();
    }

    public boolean emptyLink(){
	if ( linkUrl!=""){
	    return false;
	}else{
	    return true;
	}
    }

}
