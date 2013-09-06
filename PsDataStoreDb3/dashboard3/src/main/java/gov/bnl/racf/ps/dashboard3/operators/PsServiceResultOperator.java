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
import java.util.Iterator;
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

    public PsServiceResult getById(int id) throws PsServiceResultNotFoundException {
        return this.psServiceResultDao.getById(id);
    }

    public void delete(PsServiceResult serviceResult) {
        this.psServiceResultDao.delete(serviceResult);
    }

    public void delete(int id) {
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
    /**
     * get JSONObject containing number of results between times tmin and tmax, represented by strings
     * @param tMinString
     * @param tMaxString
     * @return 
     */
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

    /**
     * get JSONObject containing number of results between times tmin and tmax, represented by Date objects
     * @param tmin
     * @param tmax
     * @return 
     */
    public JSONObject getResultsCount(Date tmin, Date tmax) {
        if (tmin == null) {
            long secondAfterBeginningOfWorld = 1;
            tmin = new Date(secondAfterBeginningOfWorld);
        }
        if (tmax == null) {
            tmax = new Date();
        }

        JSONObject resultsObject = new JSONObject();

        int numberOfRecords = this.psServiceResultDao.getResultsCount(tmin, tmax);

        resultsObject.put("numberOfRecords", numberOfRecords);

        Date minTime = this.psServiceResultDao.getResultsTimeMin();
        resultsObject.put("minTime", IsoDateConverter.date2IsoDate(minTime));

        return resultsObject;
    }

    /**
     * take request content uploaded by client, parse it to Json object, store imn history table and update the corresponding service
     * @param requestBody
     * @return
     * @throws ParseException
     * @throws PsServiceNotFoundException 
     */
    public PsService uploadResult(String requestBody) throws ParseException, PsServiceNotFoundException {

        // parse the json part of the request
        JSONObject json = (JSONObject) jsonParser.parse(requestBody);

        // convert json to service result
        PsServiceResult serviceResult = this.unpackJson(json);

        PsService service = this.psServiceOperator.updateServiceResult(serviceResult);
        
        this.deletePsJobCorrespondingToService(service);

        return service;
    }

    /**
     * take json object representing service result and convert it to PsServiceResult object
     * @param json
     * @return 
     */
    public PsServiceResult unpackJson(JSONObject json) {
       
        PsServiceResult result = new PsServiceResult();


        if (json.keySet().contains(PsServiceResult.ID)) {
            result.setId(toInt( (String)json.get(PsServiceResult.ID) ));
        }

        if (json.keySet().contains(PsServiceResult.JOB_ID)) {
            result.setJob_id(toInt( (String)json.get(PsServiceResult.JOB_ID)));
        }


        if (json.keySet().contains(PsServiceResult.SERVICE_ID)) {
            result.setService_id(toInt((String) json.get(PsServiceResult.SERVICE_ID)));
        }

        if (json.keySet().contains(PsServiceResult.STATUS)) {
            int status = toInt( (Long) json.get(PsServiceResult.STATUS));
            result.setStatus(status);
        }

        if (json.keySet().contains(PsServiceResult.MESSAGE)) {
            result.setMessage((String) json.get(PsServiceResult.MESSAGE));
        }

        if (json.keySet().contains(PsServiceResult.TIME)) {
            result.setTime(IsoDateConverter.isoDate2Date((String) json.get(PsServiceResult.TIME)));
        }

        if (json.keySet().contains(PsServiceResult.PARAMETERS)) {
            JSONObject parametersAsJson =
                    (JSONObject) json.get(PsServiceResult.PARAMETERS);
            Iterator iter = parametersAsJson.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                Object val = (Object) parametersAsJson.get(key);
                result.setParameter(key, val);
            }
        }

        return result;
    }

    /**
     * delete all results between tmin and tmax represented by strings
     * @param tMinString
     * @param tMaxString
     * @return 
     */
    public JSONObject deleteResults(String tMinString, String tMaxString) {

        Date tmin = null;
        Date tmax = null;
        long secondAfterBeginningOfWorld = 1;

        if (tMinString == null) {
            tmin = new Date(secondAfterBeginningOfWorld);
        } else {
            tmin = IsoDateConverter.isoDate2Date(tMinString);
        }

        if (tMaxString == null) {
            tmax = new Date();
        } else {
            tmax = IsoDateConverter.isoDate2Date(tMinString);
        }

        return this.deleteResults(tmin, tmax);
    }

    /**
     * delete all results between tmin and tmax represented by Date objects
     * @param tmin
     * @param tmax
     * @return 
     */
    public JSONObject deleteResults(Date tmin, Date tmax) {
        int numberOfObjectsDeleted = this.psServiceResultDao.deleteBetween(tmin, tmax);
        JSONObject resultObject = new JSONObject();
        resultObject.put("numberOfRecordsDeleted", numberOfObjectsDeleted);
        return resultObject;
    }

    /**
     * take service result, find out to which service it corresponds, get recent result for this service, update it.
     * @param serviceResult
     * @return
     * @throws PsServiceNotFoundException 
     */
    public PsRecentServiceResult toRecentServiceResult(PsServiceResult serviceResult) throws PsServiceNotFoundException {

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
    
    private static int toInt(Long inputLong){
        int result = inputLong.intValue();
        return result;
    }
    private static int toInt(String inputString){
        int inputAsInt = Integer.parseInt(inputString);
        return inputAsInt;
    }

    /**
     * delete all results for given service, return number of objects deleted
     * @param serviceToBeDeleted
     * @return 
     */
    public int deleteResultsForService(PsService service) {
        return deleteResultsForServiceId(service.getId());
    }

    /**
     * delete all results for given service id, return number of objects deleted
     * @param id
     * @return 
     */
    public int deleteResultsForServiceId(int id) {
       return this.psServiceResultDao.deleteForServiceId(id);
    }

    private void deletePsJobCorrespondingToService(PsService service) {
        //TODO implement this
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
