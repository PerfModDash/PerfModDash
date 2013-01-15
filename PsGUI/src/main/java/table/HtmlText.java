/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

/**
 * This class is used to convert plain text and convert it to html text by
 * adding all required tags and others bells and whistles
 * 
* @author tomw
 * 
*/
public class HtmlText {

    private String text;
    private boolean strong = false;
    private boolean italic = false;
    private int header = 0;
    private boolean center = false;
    private HtmlColor color = null;
    // constructor

    public HtmlText(String inputText) {
        this.text = inputText;
    }

    /**
     * return true if the text is null or empty string
     *
     * @return
     */
    public boolean isEmpty() {
        if (this.text == null) {
            return true;
        } else {
            if ("".equals(this.text)) {
                return true;
            } else {
                return false;
            }
        }
    }

    // the tetxt should be enclosed in center tags
    public void setCenter(boolean centerInput) {
        center = centerInput;
    }
    // the text should be enclosed in header tags

    public void setHeader(int headerInput) {
        header = headerInput;
    }
    // the text should be enclosed in string tags

    public void setStrong(boolean strongValue) {
        this.strong = strongValue;
    }
    // the text should be enclosed in italic tags

    public void setItalic(boolean italicValue) {
        this.italic = italicValue;
    }       // change the text content of the class

    public void setText(String inputText) {
        this.text = inputText;
    }
    // append content to the text

    public void addText(String inputText) {
        this.text = this.text + inputText;
    }
    // add a line break

    public void addLineBreak() {
        this.text = this.text + HtmlTags.LINE_BREAK;
    }       // define color of the text

    public void setColor(HtmlColor inputColor) {
        color = inputColor;
    }
    // remove color

    public void unsetColor() {
        color = null;
    }
    // build Html representation of the string

    public String toHtml() {
        String result = text;
        if (color != null) {
            String fontTagStart = HtmlTags.FONT;
            fontTagStart = fontTagStart.replace(HtmlTags.COLOR_ATTRIBUTE, color.toHexString());
            result = fontTagStart + result + HtmlTags.FONT_END;
        }
        if (strong) {
            result = HtmlTags.STRONG + result + HtmlTags.STRONG_END;
        }
        if (italic) {
            result = HtmlTags.ITALIC + result + HtmlTags.ITALIC_END;
        }
        if (header == 1) {
            result = HtmlTags.H1 + result + HtmlTags.H1END;
        }
        if (header == 2) {
            result = HtmlTags.H2 + result + HtmlTags.H2END;
        }
        if (header == 3) {
            result = HtmlTags.H3 + result + HtmlTags.H3END;
        }
        if (header == 4) {
            result = HtmlTags.H4 + result + HtmlTags.H4END;
        }
        if (header == 5) {
            result = HtmlTags.H5 + result + HtmlTags.H5END;
        }
        if (header == 6) {
            result = HtmlTags.H6 + result + HtmlTags.H6END;
        }
        if (center) {
            result = HtmlTags.CENTER + result + HtmlTags.CENTER_END;
        }
        return result;
    }
    // get string representation of the string
    // for the time being it is the same thing
    // as html representation
    // but it does not need to be

    public String toString() {
        return this.toHtml();
    }
}
