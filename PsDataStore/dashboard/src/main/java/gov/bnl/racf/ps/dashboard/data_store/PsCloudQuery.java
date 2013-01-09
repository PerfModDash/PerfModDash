/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_store;

import gov.bnl.racf.ps.dashboard.data_objects.PsCloud;
import java.util.Iterator;

/**
 * query data store for clouds
 * @author tomw
 */
public class PsCloudQuery {
    /**
     * get cloud with given cloud id
     * @param cloudId
     * @return 
     */
    public static PsCloud getCloudById(String cloudId){
        PsDataStore dataStore = PsDataStore.getDataStore();
        return dataStore.getCloud(cloudId);
    }
    /**
     * get cloud with a given name
     * @param cloudName
     * @return 
     */
    public static PsCloud getCloudByName(String cloudName){
        PsCloud result = null;
        PsDataStore dataStore = PsDataStore.getDataStore();
        Iterator<PsCloud> iter = dataStore.cloudIterator();
        while(iter.hasNext()){
            PsCloud currentCloud = (PsCloud)iter.next();
            if(currentCloud.getName().equals(cloudName)){
                return currentCloud;
            }
        }
        return result;
    }
}
