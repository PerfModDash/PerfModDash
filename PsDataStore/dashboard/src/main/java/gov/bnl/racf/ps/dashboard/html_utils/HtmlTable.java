/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.html_utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class representing html table
 * 
* @author Tomasz
 * 
*/
public class HtmlTable {

    public HtmlTableHeaderRow headerRow;
    private ArrayList<HtmlTableRow> rows = new ArrayList<HtmlTableRow>();
    private String border = "1";

    /**
     * constructor
     *
     * @author TomW
     */
    public HtmlTable() {
    }

    /**
     * add new row to the table
     *
     * @param row
     */
    public void addRow(HtmlTableRow row) {
        rows.add(row);
    }

    /**
     * add header row
     *
     * @param row
     */
    public void addHeaderRow(HtmlTableHeaderRow row) {
        this.headerRow = row;
    }

    /**
     * define the table border
     *
     * @param newBorder
     */
    public void setBorder(String newBorder) {
        border = newBorder;
    }

    /**
     * show string representation of the table for the time being it is the same
     * as html representation, but does not need to be
     */
    public String toString() {
        return this.toHtml();
    }

    /**
     * give html representation of the table to be used for rendering table,
     * before displaying it
     *
     * @return
     */
    public String toHtml() {
        String result = "";
        result = HtmlTags.TABLE;
        result = result.replace(HtmlTags.BORDER_ATTRIBUTE, this.border);
        if (this.headerRow != null) {
            result = result + this.headerRow.toHtml();
        }
        Iterator iter = this.rows.iterator();
        while (iter.hasNext()) {
            HtmlTableRow row = (HtmlTableRow) iter.next();
            result = result + row.toHtml();
        }
        result = result + HtmlTags.TABLE_END;
        return result;
    }
}
