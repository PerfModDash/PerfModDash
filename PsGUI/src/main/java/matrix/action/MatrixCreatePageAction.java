/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.action;

import com.opensymphony.xwork2.Action;

/**
 * The action to display the create cloud page.
 * Standard behaviors like the get and set methods for MatrixCreatePageAction object properties are defined here.
 * @author siliu
 */
public class MatrixCreatePageAction {
    
    private String serviceTypeId;
        
    
    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
    public MatrixCreatePageAction() {
    }
    
    /**
     * The method to display the page to create matrix.
     * @return SUCCESS 
     * @throws Exception 
     */
    
    public String execute() throws Exception {
        return Action.SUCCESS;
    }
}
