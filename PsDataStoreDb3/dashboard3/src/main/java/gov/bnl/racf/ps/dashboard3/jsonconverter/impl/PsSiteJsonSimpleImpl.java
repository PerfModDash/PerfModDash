/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsSiteJson;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsSiteJsonSimpleImpl implements PsSiteJson{

    @Override
    public JSONObject toJson(PsSite site) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject toJson(PsSite site, String detailLevel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONArray toJson(List<PsSite> listOfSites) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONArray toJson(List<PsSite> listOfSites, String detailLevel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
