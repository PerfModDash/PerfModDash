package gov.bnl.racf.exda;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;




public class HtmlTable
{  
    private int numColumns=0;
    private int numCells=0;

    List<HtmlTableCell> listOfCells=new ArrayList<HtmlTableCell>();

    private String tableHeader="<table border=\"BORDER\" cellpadding=\"PADDING\"  >";
    private String tableFooter="</table>";

    private int border=8;
    private int padding=8;
	
    public HtmlTable( int inputNumColumns)
	{
	    numColumns= inputNumColumns;
	}
    public void setNumColumns(int inputNumColumns){
	this.numColumns= inputNumColumns;
    }
    public int getNumColumns(){
	return this.numColumns;
    }
    public void setBorder(int newBorder){
	this.border=newBorder;
    }
    public void setPadding(int newPadding){
	this.padding=newPadding;
    }
    public void addCell(HtmlTableCell inputCell){
	//listOfCells[numCells]=inputCell;
	this.numCells=this.numCells+1;
	this.listOfCells.add(inputCell);
    }
    public void addCell(String inputCellText){
	HtmlTableCell cell = new HtmlTableCell(inputCellText);
	this.listOfCells.add(cell);
    }
    public void addRow( List<HtmlTableCell> list){
	// add a list of html table cells, only first numColumns cells are added
	int count=0;
	Iterator itr = list.iterator(); 
	while(itr.hasNext()) {
	    HtmlTableCell cell=(HtmlTableCell)itr.next();
	    if(this.numColumns>count){
		this.addCell(cell);
	    }
	    count=count+1;
	}
    }
    public void addFullRow( List<HtmlTableCell> list){
	// add a list of html table cells, all cells are added, numColumns is increased if needed
	int count=0;
	Iterator itr = list.iterator(); 
	while(itr.hasNext()) {
	    HtmlTableCell cell=(HtmlTableCell)itr.next();
	    if(this.numColumns>count){
		this.addCell(cell);
	    }
	    count=count+1;
	}
	this.setNumColumns(count-1);
    }
    public String toHtml(){
	String result="";
	String workTableHeader=tableHeader.replace("BORDER",Integer.toString(border));
	workTableHeader=workTableHeader.replace("PADDING",Integer.toString(padding));
	result=workTableHeader;
	result=result+"<tr>";
	int columnCounter=0;
	
	Iterator it=listOfCells.iterator();

	while(it.hasNext())
        {
          HtmlTableCell currentCell=(HtmlTableCell)it.next();
	  result=result+currentCell.toHtml();
	  columnCounter=columnCounter+1;
	  if (columnCounter==numColumns){
		columnCounter=0;
		result=result+"</tr><tr>";
	    }
        }
	String endMark="!@#$%^&*()_+";
	result=result+endMark;
	result=result.replace("<tr>"+endMark,"");
	result=result.replace(endMark,"");
	
	result=result+tableFooter;
	return result;
    }

}

