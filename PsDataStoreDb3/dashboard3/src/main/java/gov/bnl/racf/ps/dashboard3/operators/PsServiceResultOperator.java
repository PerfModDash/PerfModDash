/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsServiceResultDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsRecentServiceResult;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceResult;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsServiceResultNotFoundException;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceResultJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import gov.bnl.racf.utils.IsoDateConverter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsServiceResultOperator {

    //=== dependency injection ===//
    private PsServiceResultJson psServiceResultJson;

    public void setPsServiceResultJson(PsServiceResultJson psServiceResultJson) {
        this.psServiceResultJson = psServiceResultJson;
    }
    private PsServiceResultDao psServiceResultDao;

    public void setPsServiceResultDao(PsServiceResultDao psServiceResultDao) {
        this.psServiceResultDao = psServiceResultDao;
    }
    
    private PsServiceOperator psServiceOperator;

    public void setPsServiceOperator(PsServiceOperator psServiceOperator) {
        this.psServiceOperator = psServiceOperator;
    }
    
    
    // === utility objects ===//
    private JSONParser jsonParser = new JSONParser();

    // === simple CRUD methods ===//
    
    public void insert(PsServiceResult serviceResult) {
        this.psServiceResultDao.insert(serviceResult);
    }
    
    public PsServiceResult getById(int id) throws PsServiceResultNotFoundException{
        return this.psServiceResultDao.getById(id);
    }
    
    public void delete(PsServiceResult serviceResult){
        this.psServiceResultDao.delete(serviceResult);
    }
    public void delete(int id){
        try {
            PsServiceResult serviceResult = this.psServiceResultDao.getById(id);
            this.delete(serviceResult);
        } catch (PsServiceResultNotFoundException ex) {
            Logger.getLogger(PsServiceResultOperator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    //=== main code ===//
    /**
     * convert service result to JSON, default (high) detail level
     *
     * @param psServiceResult
     * @return JSONObject
     */
    @Transactional
    public JSONObject toJson(PsServiceResult psServiceResult) {
        return this.toJson(psServiceResult, PsParameters.DETAIL_LEVEL_HIGH);
    }

    /**
     * convert recent result to JSON at requested detail level
     *
     * @param psRecentServiceResult
     * @param detailLevel
     * @return
     */
    @Transactional
    public JSONObject toJson(PsServiceResult psServiceResult,
            String detailLevel) {
        return this.psServiceResultJson.toJson(psServiceResult, detailLevel);
    }

    /**
     * convert list of recent results into JSONArray at requested detail level
     *
     * @param listOfRecentResults
     * @param detailLevel
     * @return
     */
    @Transactional
    public JSONArray toJson(List<PsServiceResult> listOfResults,
            String detailLevel) {
        JSONArray listOfResultsJson = new JSONArray();
        for (PsServiceResult recentResult : listOfResults) {
            listOfResultsJson.add(this.toJson(listOfResults, detailLevel));
        }
        return listOfResultsJson;
    }

    /**
     * convert list of recent results into JSONArray at default (high) detail
     * level
     *
     * @param listOfRecentResults
     * @return
     */
    @Transactional
    public JSONArray toJson(List<PsServiceResult> listOfResults) {
        return this.toJson(listOfResults, PsParameters.DETAIL_LEVEL_HIGH);
    }

    // === business methods ===//
    public JSONObject getResultsCount(String tMinString, String tMaxString) {

        Date tmin;
        Date tmax;

        if (tMinString != null) {
            tmin = IsoDateConverter.isoDate2Date(tMinString);
        } else {
            long secondAfterBeginningOfWorld = 1;
            tmin = new Date(secondAfterBeginningOfWorld);
        }
        if (tMaxString != null) {
            tmax = IsoDateConverter.isoDate2Date(tMaxString);
        } else {
            tmax = new Date();
        }
        return this.getResultsCount(tmin, tmax);
    }

    private JSONObject getResultsCount(Date tmin, Date tmax) {
        if (tmin == null) {
            long secondAfterBeginningOfWorld = 1;
            tmin = new Date(secondAfterBeginningOfWorld);
        }
        if (tmax == null) {
            tmax = new Date();
        }

        JSONObject resultsObject = new JSONObject();

        int numberOfRecords = this.psServiceResultDao.getResultsCount(tmin, tmax);
//         Query query = session.createQuery("select count(*) from PsServiceResult where time between :time_start and :time_end");
//        query.setParameter("time_start", tmin);
//        query.setParameter("time_end", tmax);
//        int result = ((Long) query.iterate().next()).intValue();

        resultsObject.put("numberOfRecords", numberOfRecords);

        Date minTime = this.psServiceResultDao.getResultsTimeMin();
        resultsObject.put("minTime", IsoDateConverter.date2IsoDate(minTime));

        return resultsObject;
    }

    public PsService uploadResult(String requestBody) throws ParseException, PsServiceNotFoundException {

        // parse the json part of the request
        JSONObject json = (JSONObject) jsonParser.parse(requestBody);

        // convert json to service result
        PsServiceResult serviceResult = this.unpackJson(json);
        
        PsService service = this.psServiceOperator.updateServiceResult(serviceResult);
        
        return service;
    }

    private PsServiceResult unpackJson(JSONObject json) {
        //TODO implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public JSONObject deleteResults(String tMin, String tMax) {
        //TODO implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public PsRecentServiceResult toRecentServiceResult(PsServiceResult serviceResult) {
        //throw new UnsupportedOperationException("Not yet implemented");
        
        int serviceId = serviceResult.getService_id();
        PsRecentServiceResult recentResult = 
                this.psServiceOperator.getRecentResultForService(serviceId);
        if (recentResult == null) {
            recentResult = new PsRecentServiceResult();
        }

        recentResult.setJob_id(serviceResult.getJob_id());
        recentResult.setMessage(serviceResult.getMessage());
        recentResult.setParameters(serviceResult.getParameters());
        recentResult.setServiceResultId(serviceResult.getId());
        recentResult.setService_id(serviceResult.getService_id());
        recentResult.setStatus(serviceResult.getStatus());
        recentResult.setTime(serviceResult.getTime());

        return recentResult;
        
    }

    
}
