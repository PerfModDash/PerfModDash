package gov.bnl.racf.exda;

public class HtmlColor
{   
    private  static String red= "rgb(255, 0, 0)";
    private  static String green= "rgb(51, 255, 51)";
    private  static String yellow="rgb(255, 255, 51)";
    private  static String brown="rgb(204, 102, 0)";
    private  static String white="rgb(255, 255, 255)";
    private  static String black="rgb(0, 0, 0)";
    private  static String blue="rgb(0,0,255)";

    private static String lightred="rgb( 247,170 ,143 )";
    private static String lightgreen="rgb(197,251,139)";
    private static String lightyellow="rgb(255, 255, 181)";
    private static String lightbrown="rgb(255 ,193 ,133 )";
    private static String lightblack="rgb(158 ,158 ,158 )";
    private static String lightblue="rgb(0,255,255)";

    private static String grey="rgb(158 ,158 ,158 )";



    private String currentColor="";
    private String currentColorName="";

    public HtmlColor(String inputColor){
	this.setCurrentColor(translateColor(inputColor));
	this.setCurrentColorName(inputColor);
    }
    public String getCurrentColor(){
	return this.currentColor;
    }
    public void setCurrentColor(String inputVar){
	this.currentColor=inputVar;
    }
    public String getCurrentColorName(){
	return this.currentColorName;
    }
    public void setCurrentColorName(String inputVar){
	this.currentColorName=inputVar;
    }
    public void makeLighter(){
	this.setCurrentColorName(this.lighter(this.getCurrentColorName()));
	this.setCurrentColor(translateColor(this.getCurrentColorName()));
    }
    public void makeDarker(){
	this.setCurrentColorName(darker(this.getCurrentColorName()));
	this.setCurrentColor(translateColor(this.getCurrentColorName()));
    }
    public String toHtml(){
	return this.getCurrentColor();
    }
    public static String translateColor(String inputColor){
	String result="white";
	if (inputColor.equals("lightred")){
	    result=lightred;
	}
	if (inputColor.equals("lightyellow")){
	    result=lightyellow;
	}
	if (inputColor.equals("lightgreen")){
	    result=lightgreen;
	}
	if (inputColor.equals("lightbrown")){
	    result=lightbrown;
	}
	if (inputColor.equals("lightblack")){
	    result=lightblack;
	}
	if (inputColor.equals("lightblue")){
	    result=lightblue;
	}
	if (inputColor.equals("grey")){
	    result=grey;
	}
	if (inputColor.equals("red")){
	    result=red;
	}
	if (inputColor.equals("green")){
	    result=green;
	}
	if (inputColor.equals("yellow")){
	    result=yellow;
	}
	if (inputColor.equals("brown")){
	    result=brown;
	}
	if (inputColor.equals("white")){
	    result=white;
	}
	if (inputColor.equals("black")){
	    result=black;
	}
	if (inputColor.equals("blue")){
	    result=blue;
	}
	return result;
    }
    public static String invertColor(String inputColor){
	String result=inputColor;
	if(inputColor.equals("red")){
	    result="white";
	}
	if(inputColor.equals("yellow")){
	    result="black";
	}
	if(inputColor.equals("green")){
	    result="black";
	}
	if(inputColor.equals("brown")){
	    result="white";
	}
	if(inputColor.equals("black")){
	    result="white";
	}
	if(inputColor.equals("grey")){
	    result="white";
	}
	if(inputColor.equals("white")){
	    result="black";
	}
	return result;
    }
    public HtmlColor negative(){
	HtmlColor result = new HtmlColor(this.invertColor(this.getCurrentColorName()));
	return result;
    }
    public static String lighter(String inputColor){
	String result=inputColor;
	if(inputColor.equals("red")){
	    result="lightred";
	}
	if(inputColor.equals("yellow")){
	    result="lightyellow";
	}
	if(inputColor.equals("green")){
	    result="lightgreen";
	}
	if(inputColor.equals("brown")){
	    result="lightbrown";
	}
	if(inputColor.equals("black")){
	    result="lightblack";
	}
	return result;
    }
    public static String darker(String inputColor){
	String result=inputColor;
	if(inputColor.equals("lightred")){
	    result="red";
	}
	if(inputColor.equals("lightyellow")){
	    result="yellow";
	}
	if(inputColor.equals("lightgreen")){
	    result="green";
	}
	if(inputColor.equals("lightbrown")){
	    result="brown";
	}
	if(inputColor.equals("lightblack")){
	    result="black";
	}
	if(inputColor.equals("grey")){
	    result="black";
	}
	return result;
    }
    public static String htmlColor(String inputColor){
	return translateColor(inputColor);
    }
    public String colorName(){
	return currentColorName;
    }
    public boolean colorIsGrey(){
	return this.colorIs("grey");
    }
    public boolean colorIsRed(){
	return this.colorIs("red");
    }
    public boolean colorIsBrown(){
	return this.colorIs("brown");
    }
    public boolean colorIs(String colorNameInput){
	if (currentColorName.equals(colorNameInput)){
	    return true;
	}else{
	    return false;
	}
    }
}


