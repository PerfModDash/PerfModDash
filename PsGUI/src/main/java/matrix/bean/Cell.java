/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.bean;

/**
 * This class includes all basic attributes of a cell in one matrix.
 * Standard behaviors like the get and set methods for Host object properties are defined here.
 * @author siliu
 */
public class Cell {
    
    private Test upperCell;
    private Test lowerCell;

    public Test getUpperCell(){
	return upperCell;
    }
    
    public void setUpperCell( Test upperCell){
	this.upperCell = upperCell;
    }
	
    public Test getLowerCell(){
	return lowerCell;
    }
    
    public void setLowerCell(Test lowerCell){
	this.lowerCell = lowerCell;
    }
}
