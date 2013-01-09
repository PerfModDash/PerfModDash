/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;

import gov.bnl.racf.ps.dashboard.data_objects.PsMatrix;
import java.util.Iterator;
import java.util.Vector;


/**
 * Class for querying the data store for matrices
 * @author tomw
 */
public class PsMatrixQuery {

    /**
     * get iterator over matrices in data store
     *
     * @return
     */
    public static Iterator<PsMatrix> getMatrixIterator() {
        PsDataStore psDataStore = PsDataStore.getDataStore();
        Iterator<PsMatrix> iter = psDataStore.matrixIterator();
        return iter;
    }
    /**
     * get matrix with given service id
     * return null if not found
     * @param id
     * @return 
     */
    public static PsMatrix getMatrixById(String id){
        Iterator<PsMatrix> iter = getMatrixIterator();
        while(iter.hasNext()){
             PsMatrix matrix = (PsMatrix)iter.next();
             if(matrix.getId().equals(id)){
                 return matrix;
             }
        }
        return null;
    }
    /**
     * get matrix identified by name, null if not found
     * @param name
     * @return 
     */
    public static PsMatrix getMatrixByName(String name){
        Iterator<PsMatrix> iter = getMatrixIterator();
        while(iter.hasNext()){
             PsMatrix matrix = (PsMatrix)iter.next();
             if(matrix.getName().equals(name)){
                 return matrix;
             }
        }
        return null;
    }
    
}
