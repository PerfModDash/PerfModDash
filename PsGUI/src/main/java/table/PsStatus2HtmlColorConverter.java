/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

/**
 *
 * @author tomw
 */
public class PsStatus2HtmlColorConverter {
    public static final String OK="0";
    public static final String WARNING="1";
    public static final String CRITICAL="2";
    public static final String UNKNOWN="3";
    public static final String TIMEOUT="4";
    public static HtmlColor convert(String status){
        HtmlColor result=null;
        if(OK.equals(status)){
            result=new HtmlColor(HtmlColor.GREEN);
        }
        if(WARNING.equals(status)){
            result=new HtmlColor(HtmlColor.YELLOW);
        }
        if(CRITICAL.equals(status)){
            result=new HtmlColor(HtmlColor.RED);
        }
        if(UNKNOWN.equals(status)){
            result=new HtmlColor(HtmlColor.BROWN);
        }
        if(TIMEOUT.equals(status)){
            result=new HtmlColor(HtmlColor.GREY);
        }
        return result;
    }
    
}
