package gov.bnl.racf.ps.dashboard.html_utils;

/**
 * Class for building and handling html links.
 * @author tomw
 */
public class HtmlLink {

    private String linkUrl = "";
    private String linkDisplayedText = "";
    private String linkTitle = "";
    private HtmlColor linkTextColor = null;

    public HtmlLink(String inputText) {
        if (inputText != null) {
            linkDisplayedText = inputText.trim();
        }
    }

    public HtmlLink(String inputUrl, String inputText) {
        if (inputUrl != null) {
            linkUrl = inputUrl.trim();
        }
        if ( inputText!= null) {
            linkDisplayedText = inputText.trim();
        }
    }

    public HtmlLink(String inputUrl, String inputText, String inputTitle) {
        if (inputUrl != null) {
            linkUrl = inputUrl.trim();
        }
        if ( inputText!= null) {
            linkDisplayedText = inputText.trim();
        }
        if ( inputTitle!= null) {
            linkTitle = inputTitle.trim();
        }
    }

    public String getUrl() {
        return this.linkUrl;
    }

    public void setTextWhite() {
        linkTextColor = new HtmlColor(HtmlColor.WHITE);
    }

    public String toHtml() {
        String result = "<a ";

        String displayedText = linkDisplayedText;
        if (linkTextColor != null) {
            result = result + " style=\"color: " + linkTextColor.toHtml() + "\" ";
            //result=result+" style=\"color: #FFFFFF\" ";
            //displayedText="<FONT COLOR=\""+linkTextColor.toHtml()+"\">"+displayedText+"</FONT>";
        }

        if (!emptyLink()) {
            if (linkTitle.equals("")) {
                result = result + " href=\"" + linkUrl + "\">" + displayedText + "</a>";
            } else {
                result = result + " href=\"" + linkUrl + "\" title=\"" + linkTitle + "\">" + displayedText + "</a>";
            }
        } else {
            result = linkDisplayedText;
        }
        return result;
    }

    public String toString() {
        return toHtml();
    }

    public boolean emptyLink() {
        if (linkUrl != "") {
            return false;
        } else {
            return true;
        }
    }
}
