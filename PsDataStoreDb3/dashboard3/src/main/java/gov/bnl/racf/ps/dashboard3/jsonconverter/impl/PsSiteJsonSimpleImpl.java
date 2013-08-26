/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsHostJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsSiteJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsSiteJsonSimpleImpl implements PsSiteJson{

    // === Dependency injection ===//
    
    private PsHostJson psHostJson;

    public void setPsHostJson(PsHostJson psHostJson) {
        this.psHostJson = psHostJson;
    }
    
    
    // === main code ===//
    
    @Override
    public JSONObject toJson(PsSite site) {
        return this.toJson(site, PsParameters.DETAIL_LEVEL_LOW);
    }

    @Override
    public JSONObject toJson(PsSite site, String detailLevel) {
         JSONObject json = new JSONObject();
        if (site != null) {
            json.put(PsSite.ID, site.getId()+"");
            json.put(PsSite.NAME, site.getName());

            if (!PsParameters.DETAIL_LEVEL_LOW.equals(detailLevel)) {


                json.put(PsSite.DESCRIPTION, site.getDescription());
                json.put(PsSite.STATUS, site.getStatus());

                JSONArray listOfHosts = new JSONArray();
                Collection<PsHost> listOfHostsInSite = site.getHosts();
                Iterator<PsHost> iter = listOfHostsInSite.iterator();
                while (iter.hasNext()) {
                    PsHost currentHost = (PsHost) iter.next();
                    if (PsParameters.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                        JSONObject currentHostJson = 
                                this.psHostJson.toJson(currentHost, PsParameters.DETAIL_LEVEL_LOW);
                        listOfHosts.add(currentHostJson);
                    }
                    if (PsParameters.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                        JSONObject currentHostJson = 
                                this.psHostJson.toJson(currentHost, PsParameters.DETAIL_LEVEL_HIGH);
                        listOfHosts.add(currentHostJson);
                    }
                }
                json.put(PsSite.HOSTS, listOfHosts);
            }
        }
        return json;
    }

    @Override
    public JSONArray toJson(List<PsSite> listOfSites) {
        JSONArray listOfResultsJson = new JSONArray();
        for(PsSite site : listOfSites){
            listOfResultsJson.add(this.toJson(site));
        }
        return listOfResultsJson;
    }

    @Override
    public JSONArray toJson(List<PsSite> listOfSites, String detailLevel) {
        JSONArray listOfResultsJson = new JSONArray();
        for(PsSite site : listOfSites){
            listOfResultsJson.add(this.toJson(site,detailLevel));
        }
        return listOfResultsJson;
    }
    
}
