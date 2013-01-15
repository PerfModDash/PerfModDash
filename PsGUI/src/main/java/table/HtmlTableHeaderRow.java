/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class defines a header row in an html table
 * 
* @author tomw
 * 
*/
public class HtmlTableHeaderRow {

    private ArrayList<HtmlTableHeaderCell> cells = new ArrayList<HtmlTableHeaderCell>();

    public HtmlTableHeaderRow() {
    }

    public void addCell(HtmlTableHeaderCell cell) {
        this.cells.add(cell);
    }

    public String toString() {
        return this.toHtml();
    }

    public String toHtml() {
        String result = "";
        result = HtmlTags.TABLE_ROW;
        Iterator iter = this.cells.iterator();
        while (iter.hasNext()) {
            HtmlTableHeaderCell cell = (HtmlTableHeaderCell) iter.next();
            result = result + cell.toHtml();
        }
        result = result + HtmlTags.TABLE_ROW_END;
        return result;
    }
}
