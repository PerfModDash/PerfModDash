
package table;

import java.util.ArrayList;
import java.util.Iterator;




/**
* This class defines a row in html table
*
* @author tomw
*
*/
public class HtmlTableRow {
   private ArrayList<HtmlTableCell> cells = new ArrayList<HtmlTableCell>();
   public HtmlTableRow(){
      }
   public void addCell(HtmlTableCell cell){
       this.cells.add(cell);
   }
   /**
    * return string representation of this row
    * for the time being it is the same as html representation
    * but it does not need to be
    */
   public String toString(){
       return this.toHtml();
   }
             /**
    * return html representation of this row
    * @return
    */
          public String toHtml(){
       String result="";
       result=HtmlTags.TABLE_ROW;
       Iterator iter = this.cells.iterator();
       while(iter.hasNext()){
           HtmlTableCell cell=(HtmlTableCell)iter.next();
           result=result+cell.toHtml();
       }
       result=result+HtmlTags.TABLE_ROW_END;
       return result;
   }

} 