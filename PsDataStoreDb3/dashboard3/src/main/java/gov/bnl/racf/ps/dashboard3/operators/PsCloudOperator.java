/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsCloudDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsCloudNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsMatrixNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsUnknownCommandException;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsCloudJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * placeholder for PsCloud operator //TODO supply the PsCloud operator code
 *
 * @author tomw
 */
public class PsCloudOperator {

    //=== useful properties ==//
    JSONParser jsonParser = new JSONParser();
    // === dependency injection ===//
    private PsCloudDao psCloudDao;

    public void setPsCloudDao(PsCloudDao psCloudDao) {
        this.psCloudDao = psCloudDao;
    }
    private PsCloudJson psCloudJson;

    public void setPsCloudJson(PsCloudJson psCloudJson) {
        this.psCloudJson = psCloudJson;
    }
    private PsMatrixOperator psMatrixOperator;

    public void setPsMatrixOperator(PsMatrixOperator psMatrixOperator) {
        this.psMatrixOperator = psMatrixOperator;
    }
    private PsSiteOperator psSiteOperator;

    public void setPsSiteOperator(PsSiteOperator psSiteOperator) {
        this.psSiteOperator = psSiteOperator;
    }

    // === CRUD methods ===//
    public PsCloud getById(int id) throws PsCloudNotFoundException {
        return this.psCloudDao.getById(id);
    }

    public List<PsCloud> getAll() {
        return this.psCloudDao.getAll();
    }

    private void update(PsCloud cloud) {
        this.psCloudDao.update(cloud);
    }

    public void delete(int id) {
        this.psCloudDao.delete(id);
    }

    // === JSON methods ===/
    public JSONArray toJson(List<PsCloud> listOfClouds) {
        return this.psCloudJson.toJson(listOfClouds);
    }

    public JSONArray toJson(List<PsCloud> listOfClouds, String detailLevel) {
        return this.psCloudJson.toJson(listOfClouds, detailLevel);
    }

    public JSONObject toJson(PsCloud cloud) {
        return this.psCloudJson.toJson(cloud);
    }

    public JSONObject toJson(PsCloud cloud, String detailLevel) {
        return this.psCloudJson.toJson(cloud, detailLevel);
    }

    // === main business methods ===//
    public PsCloud insertNewCloudFromJson(JSONObject jsonInput) {
        // first order of business is to create new cloud
        PsCloud newCloud = this.psCloudDao.create();
        //second order of business is to update the cloud object
        this.updateCloudFromJson(newCloud, jsonInput);
        //third order of business is to save the updated object
        this.psCloudDao.update(newCloud);

        return newCloud;

    }

    public PsCloud updateCloudFromJson(int id, JSONObject jsonInput) throws PsCloudNotFoundException {

        //first order of business is to find the requested cloud
        PsCloud cloud = this.getById(id);

        // second order of business is to update the cloud
        this.updateCloudFromJson(cloud, jsonInput);

        //third order of business is to update the cloud in database
        this.update(cloud);

        // last order of business is to return it 
        return cloud;

    }

    private void updateCloudFromJson(PsCloud cloud, JSONObject jsonInput) {
        // first order of business is to compare id

        boolean jsonHasValidId = false;
        if (jsonInput.keySet().contains(PsCloud.ID)) {
            if (jsonInput.get(PsCloud.ID) != null && !"".equals(jsonInput.get(PsCloud.ID))) {
                jsonHasValidId = true;
            }
        }

        boolean idTestPassed = false;

        if (jsonHasValidId) {
            int cloudId = cloud.getId();
            Long cloudIdAsLong = (Long) jsonInput.get(PsCloud.ID);
            int cloudIdInJson = cloudIdAsLong.intValue();
            if (cloudId != cloudIdInJson) {
                throw new UnsupportedOperationException(this.getClass().getName()
                        + " ERROR: cloud id=" + cloudId
                        + " and json id=" + cloudIdInJson + " do not match");
                //idTestPassed=false;
            } else {
                idTestPassed = true;
            }
        } else {
            idTestPassed = true;
        }

        if (!idTestPassed) {
            throw new UnsupportedOperationException(this.getClass().getName()
                    + " ERROR: cloud id and json id do not match");
        } else {
            if (jsonInput.keySet().contains(PsCloud.NAME)) {
                cloud.setName((String) jsonInput.get(PsCloud.NAME));
            }
            // we do not handle matrices and sites here
        }
    }

    public PsCloud executeCommand(int id, String userCommand, String requestBody)
            throws
            PsCloudNotFoundException,
            PsUnknownCommandException, ParseException, PsMatrixNotFoundException, PsSiteNotFoundException {

        PsCloud cloud = this.getById(id);

        boolean thisIsKnownCommand = false;

        if (PsParameters.CLOUD_ADD_MATRIX_IDS.equals(userCommand)) {
            thisIsKnownCommand = true;
            // user wants to add matrices to cloud

            JSONArray arrayOfMatrixIds = (JSONArray) this.jsonParser.parse(requestBody);

            // add those sites
            this.addMatrixIds(cloud, arrayOfMatrixIds);
        }

        if (PsParameters.CLOUD_ADD_SITE_IDS.equals(userCommand)) {
            thisIsKnownCommand = true;
            // user wants to add sites to cloud

            JSONArray arrayOfSiteIds = (JSONArray) this.jsonParser.parse(requestBody);

            // remove those sites
            this.addSiteIds(cloud, arrayOfSiteIds);
        }

        if (PsParameters.CLOUD_REMOVE_SITE_IDS.equals(userCommand)) {
            thisIsKnownCommand = true;
            // user wants to remove sites from cloud
            JSONArray arrayOfSiteIds = (JSONArray) this.jsonParser.parse(requestBody);
            // remove those sites
            this.removeSiteIds(cloud, arrayOfSiteIds);
        }

        if (PsParameters.CLOUD_REMOVE_MATRIX_IDS.equals(userCommand)) {
            thisIsKnownCommand = true;
            // user wants to remove matrices from cloud
            JSONArray arrayOfMatrixIds = (JSONArray) this.jsonParser.parse(requestBody);

            // remove those hosts
            this.removeMatrixIds(cloud, arrayOfMatrixIds);
        }

        if (!thisIsKnownCommand) {
            throw new PsUnknownCommandException(this.getClass().getName()+" unknown command: "+userCommand);
        }
        return cloud;
    }

    private void addMatrixIds(PsCloud cloud, JSONArray arrayOfMatrixIds) throws PsMatrixNotFoundException {
        Iterator iter = arrayOfMatrixIds.iterator();
        while (iter.hasNext()) {
            String marixIdString = (String) iter.next();
            int matrixId = Integer.parseInt(marixIdString);
            this.addMatrixId(cloud, matrixId);
        }
    }

    private void removeMatrixIds(PsCloud cloud, JSONArray arrayOfMatrixIds) throws PsMatrixNotFoundException {
        Iterator iter = arrayOfMatrixIds.iterator();
        while (iter.hasNext()) {
            String marixIdString = (String) iter.next();
            int matrixId = Integer.parseInt(marixIdString);
            this.removeMatrixId(cloud, matrixId);
        }
    }

    private void addSiteIds(PsCloud cloud, JSONArray arrayOfSiteIds) throws PsSiteNotFoundException {
        Iterator iter = arrayOfSiteIds.iterator();
        while (iter.hasNext()) {
            String siteIdString = (String) iter.next();
            int siteId = Integer.parseInt(siteIdString);
            this.addSiteId(cloud, siteId);
        }
    }

    private void removeSiteIds(PsCloud cloud, JSONArray arrayOfSiteIds) throws PsSiteNotFoundException {
        Iterator iter = arrayOfSiteIds.iterator();
        while (iter.hasNext()) {
            String siteIdString = (String) iter.next();
            int siteId = Integer.parseInt(siteIdString);
            this.removeSiteId(cloud, siteId);
        }
    }

    private void addMatrixId(PsCloud cloud, int matrixId) throws PsMatrixNotFoundException {
        PsMatrix matrix = this.psMatrixOperator.getById(matrixId);

        this.addMatrix(cloud, matrix);
    }

    private void removeMatrixId(PsCloud cloud, int matrixId) throws PsMatrixNotFoundException {
        boolean matrixFound=false;
        
        Iterator<PsMatrix> iter = cloud.matrixIterator();
        while (iter.hasNext()) {
            PsMatrix currentMatrix = (PsMatrix) iter.next();
            if (currentMatrix.getId() == matrixId) {
                iter.remove();
                matrixFound=true;
            }
        }
        if(matrixFound){
            this.psCloudDao.update(cloud);
        }else{
            
        }
    }

    private void addSiteId(PsCloud cloud, int siteId) throws PsSiteNotFoundException {
        PsSite site = this.psSiteOperator.getById(siteId);

        this.addSite(cloud, site);
    }

    private void removeSiteId(PsCloud cloud, int siteId) throws PsSiteNotFoundException {
        boolean siteFound=false;
        Iterator<PsSite> iter = cloud.sitesIterator();
        while (iter.hasNext()) {
            PsSite currentSite = (PsSite) iter.next();
            if (currentSite.getId()==siteId) {
                iter.remove();
                siteFound=true;
            }
        }
        if(siteFound){
            this.update(cloud);
        }else{
            throw new PsSiteNotFoundException("no site id="+siteId+" found on cloud id="+cloud.getId());
        }
    }

    private void addMatrix(PsCloud cloud, PsMatrix matrix) {
        cloud.addMatrix(matrix);
        this.update(cloud);
    }

    private void removeMatrix(PsCloud cloud, PsMatrix matrix) throws PsMatrixNotFoundException {
        this.removeMatrixId(cloud, matrix.getId());
    }

    private void addSite(PsCloud cloud, PsSite site) {
        cloud.addSite(site);
        this.update(cloud);
    }

    private void removeSite(PsCloud cloud, PsSite site) throws PsSiteNotFoundException {
        this.removeSiteId(cloud,site.getId());
    }
}
