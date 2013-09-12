/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.operators;

import gov.bnl.racf.ps.dashboard3.dao.PsSiteDao;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.exceptions.PsHostNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsSiteNotFoundException;
import gov.bnl.racf.ps.dashboard3.exceptions.PsUnknownCommandException;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsSiteJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.Iterator;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.transaction.annotation.Transactional;

/**
 * PsSite operator class
 *
 * @author tomw
 */
public class PsSiteOperator {
    //=== useful properties ==//

    JSONParser jsonParser = new JSONParser();
    // === dependency injection part ===//
    private PsSiteDao psSiteDao;

    public void setPsSiteDao(PsSiteDao psSiteDao) {
        this.psSiteDao = psSiteDao;
    }
    private PsHostOperator psHostOperator;

    public void setPsHostOperator(PsHostOperator psHostOperator) {
        this.psHostOperator = psHostOperator;
    }
    private PsSiteJson psSiteJson;

    public void setPsSiteJson(PsSiteJson psSiteJson) {
        this.psSiteJson = psSiteJson;
    }

    // === CRUD methods ===//
    @Transactional
    public PsSite create() {
        return this.psSiteDao.create();
    }

    @Transactional
    public void insert(PsSite site) {
        this.psSiteDao.insert(site);
    }

    @Transactional
    public void update(PsSite site) {
        this.psSiteDao.update(site);
    }

    @Transactional
    public List<PsSite> getAll() {
        return this.psSiteDao.getAll();
    }

    @Transactional
    public PsSite getById(int id) throws PsSiteNotFoundException {
        return this.psSiteDao.getById(id);
    }

    @Transactional
    public void delete(int id) {
        this.psSiteDao.delete(id);
    }

    @Transactional
    public void delete(PsSite site) {
        // 1. remove all hosts from site
        this.removeAllHosts(site);
        // 2. delete site        
        this.psSiteDao.delete(site);
    }

    // === JSON methods ===//
    @Transactional
    public JSONObject toJson(PsSite site, String detailLevel) {
        return this.psSiteJson.toJson(site, detailLevel);
    }

    @Transactional
    public JSONObject toJson(PsSite site) {
        return this.psSiteJson.toJson(site);
    }

    @Transactional
    public JSONArray toJson(List<PsSite> listOfSites) {
        return this.psSiteJson.toJson(listOfSites);
    }

    @Transactional
    public JSONArray toJson(List<PsSite> listOfSites, String detailLevel) {
        return this.psSiteJson.toJson(listOfSites, detailLevel);
    }

    // === main methods ===//
    @Transactional
    public PsSite insertNewSiteFromJson(JSONObject jsonInput) {
        // first order of business is to create new PsSite object
        PsSite newSite = new PsSite();

        // second order of business is to update it with fields from JSON
        this.update(newSite, jsonInput);

        //third order of business is to insert the new site object to database
        this.insert(newSite);

        // last order of business is to return it 
        return newSite;
    }

    @Transactional
    public PsSite updateSiteFromJson(int id, JSONObject jsonInput) throws PsSiteNotFoundException, ParseException, PsSiteNotFoundException {
        //first order of business is to find the requested site
        PsSite site = this.getById(id);

        // second order of business is to update the site
        this.update(site, jsonInput);

        //third order of business is to update the site in database
        this.update(site);

        // last order of business is to return it 
        return site;
    }

    @Transactional
    public void update(PsSite site, JSONObject json) {

        if (json.keySet().contains(PsSite.NAME)) {
            site.setName((String) json.get(PsSite.NAME));
        }
        if (json.keySet().contains(PsSite.DESCRIPTION)) {
            site.setDescription((String) json.get(PsSite.DESCRIPTION));
        }
        if (json.keySet().contains(PsSite.STATUS)) {
            site.setStatus(((Long) json.get(PsSite.STATUS)).intValue());
        }
        // we do not handle hosts here. Adding and removing of hosts
        // is done by the SITE_ADD_HOST_IDS and SITE_REMOVE_HOST_IDS
        // command in the API

        // we do not make id comparison between json and site
        // //TODO Make the id comparison test
    }

    @Transactional
    public PsSite executeCommand(int id, String userCommand, String requestBody)
            throws PsUnknownCommandException, PsSiteNotFoundException, PsHostNotFoundException, ParseException {


        PsSite site = this.getById(id);

        JSONArray arrayOfHostIds = (JSONArray) this.jsonParser.parse(requestBody);

        boolean thisIsKnownCommand = false;

        if (PsParameters.SITE_ADD_HOST_IDS.equals(userCommand)) {
            thisIsKnownCommand = true;
            // user wants to add hosts to site

            // add those hosts
            this.addHosts(site, arrayOfHostIds);
        }

        if (PsParameters.SITE_REMOVE_HOST_IDS.equals(userCommand)) {
            thisIsKnownCommand = true;
            // user wants to remove hosts from site

            // remove those hosts
            this.removeHosts(site, arrayOfHostIds);
        }

        if (PsParameters.SITE_REMOVE_ALL_HOSTS.equals(userCommand)) {
            thisIsKnownCommand = true;
            // user wants to remove hosts from site

            // remove those hosts
            this.removeAllHosts(site);
        }

        if (!thisIsKnownCommand) {
            throw new PsUnknownCommandException(this.getClass().getName() + " unknown command: " + userCommand);
        }
        return site;
    }

    // === commands for adding hosts to a site ===//
    @Transactional
    public void addHosts(PsSite site, JSONArray listOfHostIds) throws PsHostNotFoundException {
        Iterator iter = listOfHostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            this.addHostId(site, hostId);
        }
    }

    @Transactional
    public void addHostId(PsSite site, int hostId) throws PsHostNotFoundException {
        PsHost host = this.psHostOperator.getById(hostId);
        this.addHost(site, host);
    }

    @Transactional
    private void addHost(PsSite site, PsHost host) {
        // firs order of business is to add the host
        site.addHost(host);
        // then save the updated site
        this.update(site);
    }

    // === commands for removing hosts from site === //
    /**
     * remove host from site. The host is not deleted, just removed.
     *
     * @param site
     * @param host
     */
    @Transactional
    public void removeHost(PsSite site, PsHost host) {
        site.removeHost(host);
        this.psSiteDao.update(site);
    }

    /**
     * remove host with given id from site
     *
     * @param site
     * @param hostId
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void removeHostId(PsSite site, int hostId) throws PsHostNotFoundException {
        PsHost host = this.psHostOperator.getById(hostId);
        this.removeHost(site, host);
    }

    /**
     * remove hosts whose list of id's is in JSONArray from the designated site
     *
     * @param site
     * @param listOfHostIds
     * @throws PsHostNotFoundException
     */
    @Transactional
    public void removeHosts(PsSite site, JSONArray listOfHostIds) throws PsHostNotFoundException {
        Iterator iter = listOfHostIds.iterator();
        while (iter.hasNext()) {
            String hostIdString = (String) iter.next();
            int hostId = Integer.parseInt(hostIdString);
            this.removeHostId(site, hostId);
        }
    }

    /**
     * remove all hosts from the designated site
     *
     * @param site
     */
    @Transactional
    private void removeAllHosts(PsSite site) {

        site.removeAllHosts();

        this.psSiteDao.update(site);
    }
}
