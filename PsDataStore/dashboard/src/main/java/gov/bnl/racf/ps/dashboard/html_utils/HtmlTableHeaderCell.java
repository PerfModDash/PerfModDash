/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.html_utils;

/**
 * This class defines a single cell in the header of an html table
 * 
* @author tomw
 * 
*/
public class HtmlTableHeaderCell {

    private HtmlText cellText = null;
    private HtmlLink cellLink = null;
    private HtmlColor cellColor = null;
    private String alignementLeft = "text-align: left;";
    private String alignementRight = "text-align: right;";
    private String alignementTop = "vertical-align: top'";
    private String alignement = "text-align: right;";
    private String whiteTextStyle = "color:white";
    private boolean whiteText = false;

    public HtmlTableHeaderCell(HtmlLink inputCellLink) {
        cellLink = inputCellLink;
    }

    public HtmlTableHeaderCell(HtmlLink inputCellLink, HtmlColor inputCellColor) {
        cellLink = inputCellLink;
        cellColor = inputCellColor;

        if (cellColor.equals(HtmlColor.RED)) {
            cellLink.setTextWhite();
        }
        if (cellColor.equals(HtmlColor.BROWN)) {
            cellLink.setTextWhite();
        }
        if (cellColor.equals(HtmlColor.GREY)) {
            cellLink.setTextWhite();
        }
    }

    public HtmlTableHeaderCell(HtmlText cellText) {
        this.cellText = cellText;
    }

    public String toString() {
        return this.toHtml();
    }

//    public String toHtml() {
//        String result = "";
//        result = HtmlTags.TABLE_HEADER_CELL
//                + this.cellText.toHtml()
//                + HtmlTags.TABLE_HEADER_CELL_END;
//        return result;
//    }
    public String toHtml() {
        String style = alignement;
        String cellHtml = "";

        if (cellColor != null) {
            style = style + "background-color: " + cellColor.toHtml();
        }
        if (whiteText) {
            style = style + "; " + whiteTextStyle;
        }

        cellHtml = cellHtml + "<td style=\"" + style + "\" >";

        boolean useLink = false;
        if (cellText == null) {
            useLink = true;
        } else {
            if (cellText.isEmpty()) {
                useLink = true;
            }
        }
        if (useLink) {
            cellHtml = cellHtml + cellLink.toHtml() + "</td>";
        } else {
            cellHtml = cellHtml + cellText.toHtml() + "</td>";
        }

        return cellHtml;
    }
}
