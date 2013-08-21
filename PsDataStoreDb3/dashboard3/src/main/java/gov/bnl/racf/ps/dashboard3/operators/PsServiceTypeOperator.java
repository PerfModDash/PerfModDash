/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceTypeDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.domainobjects.factories.PsServiceTypeFactory;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceTypeJson;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Operator on the PsServiceType class
 * @author tomw
 */
public class PsServiceTypeOperator {
    
    // --- dependency injection part ---//
    
    private PsServiceTypeDao psServiceTypeDao;

    public void setPsServiceTypeDao(PsServiceTypeDao psServiceTypeDao) {
        this.psServiceTypeDao = psServiceTypeDao;
    }
    
    private PsServiceTypeFactory psServiceTypeFactory;

    public void setPsServiceTypeFactory(PsServiceTypeFactory psServiceTypeFactory) {
        this.psServiceTypeFactory = psServiceTypeFactory;
    }
    
    private PsServiceTypeJson psServiceTypeJson;

    public void setPsServiceTypeJson(PsServiceTypeJson psServiceTypeJson) {
        this.psServiceTypeJson = psServiceTypeJson;
    }
    
    // --- body of the class ---//
    
    /**
     * initialize the service types in database
     */
    public List<String>  initServiceTypes(){
        List<String> knownServiceTypeNames=this.psServiceTypeFactory.listOfServiceTypes();
        List<String> listOfNewServiceNames=new ArrayList<String>();
        for(String serviceTypeName : knownServiceTypeNames){
            if(this.psServiceTypeDao.containsServiceType(serviceTypeName)){
                // do nothing
            }else{
                // create new service type
                PsServiceType newServiceType=
                        this.psServiceTypeFactory.createType(serviceTypeName);
                // insert it to database
                this.psServiceTypeDao.insert(newServiceType);
                // add to list of newly created services
                listOfNewServiceNames.add(serviceTypeName);
            }
        }
        // return list of newly created service names
        return listOfNewServiceNames;
    }
    /**
     * get list of all known service types in the database
     * @return 
     */
    public List<PsServiceType> getAll(){
        return this.psServiceTypeDao.getAll();
    }
    
    public void deleteAllServiceTypes(){
        List<PsServiceType> listOfAllServiceTypes = this.getAll();
        Iterator iter = listOfAllServiceTypes.iterator();
        while(iter.hasNext()){
            PsServiceType currentServiceType = (PsServiceType)iter.next();
            this.psServiceTypeDao.delete(currentServiceType);
        }
        // drop reference to it, just in case
        listOfAllServiceTypes=null;
    }
    /**
     * convert service type object to JSON
     * @param psServiceType
     * @return 
     */
    public JSONObject toJson(PsServiceType psServiceType){
        return this.psServiceTypeJson.toJson(psServiceType);
    }
    /**
     * convert service type object to json using prescribed detail level. 
     * @param psServiceType
     * @param detailLevel
     * @return 
     */
    public JSONObject toJson(PsServiceType psServiceType,String detailLevel){
        return this.psServiceTypeJson.toJson(psServiceType,detailLevel);
    }
    /**
     * convert list of service types to JSONArray
     * @param listOfServiceTypes
     * @return 
     */
    public JSONArray toJson(List<PsServiceType> listOfServiceTypes){
        JSONArray result=new JSONArray();
        for(PsServiceType serviceType : listOfServiceTypes){
            result.add(this.toJson(serviceType));
        }
        return result;
    }
    
    /**
     * convert list of service types to JSONArray using prescribes detail level
     * @param listOfServiceTypes
     * @param detailLevel
     * @return 
     */
    public JSONArray toJson(List<PsServiceType> listOfServiceTypes, String detailLevel){
        JSONArray result=new JSONArray();
        for(PsServiceType serviceType : listOfServiceTypes){
            result.add(this.toJson(serviceType,detailLevel));
        }
        return result;
    }
    
    /**
     * check if the service type is known
     * @param serviceTypeId
     * @return 
     */
    public boolean isKnownServiceType(String serviceTypeId){
        return this.psServiceTypeFactory.isKnownType(serviceTypeId);
    }
    /**
     * check if the given service type represents primitive service
     * @param serviceTypeId
     * @return 
     */
    public boolean isPrimitiveService(String serviceTypeId){
        return this.psServiceTypeFactory.isPrimitiveService(serviceTypeId);
    }
    

    PsServiceType getServiceType(String serviceTypeId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    //TODO finish rest of this class
}
