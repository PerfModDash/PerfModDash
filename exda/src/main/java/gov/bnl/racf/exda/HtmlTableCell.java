package gov.bnl.racf.exda;

public class HtmlTableCell
{  
    private HtmlLink cellLink;
    private HtmlColor cellColor;
    private String cellText="";

    private String alignementLeft="text-align: left;";
    private String alignementRight="text-align: right;";
    private String alignementTop="vertical-align: top'";
    private String alignement="text-align: right;";
    
    private String whiteTextStyle="color:white";

    private boolean whiteText=false;

    public HtmlTableCell(HtmlLink inputCellLink, HtmlColor inputCellColor)
	{
	    cellLink=  inputCellLink;
	    cellColor=inputCellColor;
	    // if (cellColor.colorIs("red")||cellColor.colorIs("brown")){
	    if (cellColor.colorIs("red")){	
		cellLink.setTextWhite();
	    }
	    if (cellColor.colorIs("brown")){	
		cellLink.setTextWhite();
	    }
	    if (cellColor.colorIsGrey()){	
		cellLink.setTextWhite();
	    }
	}
    public HtmlTableCell(HtmlLink inputCellLink)
	{
	    cellLink=  inputCellLink;
	    cellColor=new HtmlColor("white");
	}
    public HtmlTableCell(String inputCellText)
	{
	    cellLink=  new HtmlLink(inputCellText);
	    cellColor=new HtmlColor("white");
	}
    public HtmlTableCell(String inputCellText,HtmlColor inputCellColor)
	{
	    cellText=inputCellText;
	    cellColor=inputCellColor;
	}

    public void setTextWhite(){
	whiteText=true;
    }

    public void alignLeft(){
	alignement=alignementLeft;
    }
    public void alignRight(){
	alignement=alignementRight;
    }
    public void alignTop(){
	alignement=alignementTop;
    }
   
    public String toHtml(){
	String style=alignement;
	String cellHtml="";

	style=style + "background-color: " + cellColor.toHtml();
	if(whiteText){
	    style=style+"; "+whiteTextStyle;
	}

	cellHtml=cellHtml + "<td style=\"" + style + "\" >" ;

	if (cellText==""){
	    cellHtml=cellHtml+cellLink.toHtml() +"</td>";
	}else{
	     cellHtml=cellHtml+cellText+"</td>";
	}

	return cellHtml;
    }

}
