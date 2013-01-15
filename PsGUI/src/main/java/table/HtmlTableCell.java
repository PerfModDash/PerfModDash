/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

/**
 * This class defines a single cell in html table
 * 
* @author tomw
 * 
*/
public class HtmlTableCell {

    private HtmlText cellText = null;
    private HtmlLink cellLink = null;
    private HtmlColor cellColor = null;
    private String alignementLeft = "text-align: left;";
    private String alignementRight = "text-align: right;";
    private String alignementTop = "vertical-align: top'";
    private String alignement = "text-align: right;";
    private String whiteTextStyle = "color:white";
    private boolean whiteText = false;

    /**
     * constructor
     *
     * @param cellText
     */
    public HtmlTableCell(HtmlLink inputCellLink, HtmlColor inputCellColor) {
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

    public HtmlTableCell(HtmlLink inputCellLink) {
        cellLink = inputCellLink;
        cellColor = new HtmlColor(HtmlColor.WHITE);
    }

    public HtmlTableCell(String cellInputString) {
        HtmlText htmlText = new HtmlText(cellInputString);
        this.cellText = cellText;
    }

    public HtmlTableCell(HtmlText cellText) {
        this.cellText = cellText;
    }

    public String toString() {
        return this.toHtml();
    }

    public void alignLeft() {
        alignement = alignementLeft;
    }

    public void alignRight() {
        alignement = alignementRight;
    }

    public void alignTop() {
        alignement = alignementTop;
    }

//    public String toHtml() {
//        String result = "";
//        result = HtmlTags.TABLE_CELL
//                + this.cellText.toHtml()
//                + HtmlTags.TABLE_CELL_END;
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


        boolean useCellLink = false;
        if (cellText == null) {
            useCellLink = true;
        } else {
            if (cellText.isEmpty()) {
                useCellLink = true;
            }
        }
        if (useCellLink) {
            if (cellLink != null) {
                cellHtml = cellHtml + cellLink.toHtml() + HtmlTags.TABLE_CELL_END;
            }else{
                cellHtml = cellHtml + " " + HtmlTags.TABLE_CELL_END;
            }
        } else {
            cellHtml = cellHtml + cellText + HtmlTags.TABLE_CELL_END;
        }

        return cellHtml;
    }
}
