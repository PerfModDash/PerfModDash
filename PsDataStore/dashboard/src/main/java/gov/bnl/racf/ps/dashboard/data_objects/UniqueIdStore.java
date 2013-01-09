/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;

/**
 * utility singleton class used to generate and store unique object id
 * used by the unique is generator.
 * @author tomw
 */
public class UniqueIdStore {
    private static UniqueIdStore uniqueIdStore=null;
    private int id=0;
    private UniqueIdStore(){
        
    }
    public static UniqueIdStore getUniqueIdStore(){
        if(uniqueIdStore==null){
            uniqueIdStore=new UniqueIdStore();
        }
        return uniqueIdStore;
    }
    public String getId(){
        id=id+1;
        return id+"";
    }
    
}
