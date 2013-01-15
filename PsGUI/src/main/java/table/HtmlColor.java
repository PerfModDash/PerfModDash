/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import java.awt.Color;

/**
 * This class contains utilities to handle the HtmlColor codes
 *
 * @author Tomasz
 * 
*/
public class HtmlColor {

    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String YELLOW = "yellow";
    public static final String BROWN = "brown";
    public static final String WHITE = "white";
    public static final String BLACK = "black";
    public static final String BLUE = "blue";
    public static final String GREY = "grey";
    public static final String LIGHT_RED = "lightRed";
    public static final String LIGHT_GREEN = "light_green";
    public static final String LIGHT_YELLOW = "light_yellow";
    public static final String LIGHT_BROWN = "light_brown";
    public static final String LIGHT_BLACK = "light_blask";
    public static final String LIGHT_BLUE = "light_blue";
    
    private int red = 0;
    private int green = 0;
    private int blue = 0;
    
    private static String redCode = "rgb(255, 0, 0)";
    private static String greenCode = "rgb(51, 255, 51)";
    private static String yellowCode = "rgb(255, 255, 51)";
    private static String brownCode = "rgb(204, 102, 0)";
    private static String whiteCode = "rgb(255, 255, 255)";
    private static String blackCode = "rgb(0, 0, 0)";
    private static String blueCode = "rgb(0,0,255)";
    private static String lightredCode = "rgb( 247,170 ,143 )";
    private static String lightgreenCode = "rgb(197,251,139)";
    private static String lightyellowCode = "rgb(255, 255, 181)";
    private static String lightbrownCode = "rgb(255 ,193 ,133 )";
    private static String lightblackCode = "rgb(158 ,158 ,158 )";
    private static String lightblueCode = "rgb(0,255,255)";
    private static String greyCode = "rgb(158 ,158 ,158 )";

    /**
     * default constructor
     *
     * @param red
     * @param green
     * @param blue
     */
    public HtmlColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public HtmlColor(String inputColor) {
        if (HtmlColor.RED.equals(inputColor)) {
            this.red = 255;
            this.green = 0;
            this.blue = 0;
        }
        if (HtmlColor.GREEN.equals(inputColor)) {
            this.red = 51;
            this.green = 255;
            this.blue = 51;
        }
        if (HtmlColor.YELLOW.equals(inputColor)) {
            this.red = 255;
            this.green = 255;
            this.blue = 51;
        }
        if (HtmlColor.BROWN.equals(inputColor)) {
            this.red = 204;
            this.green = 102;
            this.blue = 0;
        }
        if (HtmlColor.WHITE.equals(inputColor)) {
            this.red = 255;
            this.green = 255;
            this.blue = 255;
        }
        if (HtmlColor.BLACK.equals(inputColor)) {
            this.red = 0;
            this.green = 0;
            this.blue = 0;
        }
        if (HtmlColor.BLUE.equals(inputColor)) {
            this.red = 0;
            this.green = 0;
            this.blue = 255;
        }
        if (HtmlColor.LIGHT_RED.equals(inputColor)) {
            this.red = 247;
            this.green = 170;
            this.blue = 143;
        }
        if (HtmlColor.LIGHT_YELLOW.equals(inputColor)) {
            this.red = 255;
            this.green = 255;
            this.blue = 181;
        }
        if (HtmlColor.LIGHT_GREEN.equals(inputColor)) {
            this.red = 197;
            this.green = 251;
            this.blue = 139;
        }
        if (HtmlColor.LIGHT_BROWN.equals(inputColor)) {
            this.red = 155;
            this.green = 193;
            this.blue = 133;
        }
        if (HtmlColor.LIGHT_BLACK.equals(inputColor)) {
            this.red = 158;
            this.green = 158;
            this.blue = 158;
        }
        if (HtmlColor.LIGHT_BLUE.equals(inputColor)) {
            this.red = 0;
            this.green = 255;
            this.blue = 255;
        }
        if (HtmlColor.GREY.equals(inputColor)) {
            this.red = 158;
            this.green = 158;
            this.blue = 158;
        }
    }

    /**
     * utility method to convert integers [0..255] into string representing
     * hexadecimal number,
     *
     * @param inputInteger
     * @return
     */
    private String int2HexString(int inputInteger) {
        // convert a number 0..255 to hex, fill with 0 if necessary
        int bitMask = 0xFF;
        String hexResult = java.lang.Integer.toHexString(inputInteger & bitMask);
        if (hexResult.length() == 1) {
            hexResult = "0" + hexResult;
        }
        return hexResult;
    }

    /**
     * convert class HtmlColor into java representation (that is into java Color
     * class)
     *
     * @return
     */
    public Color toJavaColor() {
        Color color = new Color(red, green, blue);
        return color;
    }
    /**
     * return color in the rgb form 'rgb(r,g,b)
     * @return 
     */
    public String toHtml(){
        System.out.println(getClass()+" entry");
        String result="rgb("+this.red+","+this.green+","+this.blue+")";
        System.out.println(getClass()+" resuult="+result);
        return result;
    }

    /**
     * convert the color into string of three hexadecimal numbers, to be used
     * inside html
     *
     * @return
     */
    public String toHexString() {
        String hexString = int2HexString(red) + int2HexString(green) + int2HexString(blue);
        return hexString;
    }

    /**
     * for the time being, the same as toHexString in the future we may include
     * trlanslation of colors into human readable color names
     */
    public String toString() {
        return toHexString();
    }
}
