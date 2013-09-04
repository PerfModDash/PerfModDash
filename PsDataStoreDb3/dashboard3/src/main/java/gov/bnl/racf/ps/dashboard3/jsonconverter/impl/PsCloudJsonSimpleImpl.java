/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsCloud;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsSite;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsCloudJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsMatrixJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsSiteJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsCloudJsonSimpleImpl implements PsCloudJson {

    // === dependency injection ===//
    
    private PsSiteJson psSiteJson;

    public void setPsSiteJson(PsSiteJson psSiteJson) {
        this.psSiteJson = psSiteJson;
    }
    
    private PsMatrixJson psMatrixJson;

    public void setPsMatrixJson(PsMatrixJson psMatrixJson) {
        this.psMatrixJson = psMatrixJson;
    }
    
    // === business classes ===//

    @Override
    public JSONObject toJson(PsCloud cloud) {
        return this.toJson(cloud, PsParameters.DETAIL_LEVEL_HIGH);
    }

    @Override
    public JSONObject toJson(PsCloud cloud, String detailLevel) {

        JSONObject json = new JSONObject();
        if (cloud != null) {
            json.put(PsCloud.ID, cloud.getId() + "");
            json.put(PsCloud.NAME, cloud.getName());

            if (!PsParameters.DETAIL_LEVEL_LOW.equals(detailLevel)) {

                json.put(PsCloud.STATUS, cloud.getStatus());

                JSONArray sites = new JSONArray();
                Iterator<PsSite> iter = cloud.sitesIterator();
                while (iter.hasNext()) {
                    PsSite currentSite = (PsSite) iter.next();
                    if (PsParameters.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                        sites.add(this.psSiteJson.toJson(currentSite, PsParameters.DETAIL_LEVEL_LOW));
                    }
                    if (PsParameters.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                        sites.add(this.psSiteJson.toJson(currentSite, PsParameters.DETAIL_LEVEL_HIGH));
                    }
                }
                json.put(PsCloud.SITES, sites);

                JSONArray matrices = new JSONArray();
                Iterator<PsMatrix> iter2 = cloud.matrixIterator();
                while (iter2.hasNext()) {
                    PsMatrix currentMatrix = (PsMatrix) iter2.next();
                    if (PsParameters.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                        matrices.add(this.psMatrixJson.toJson(currentMatrix, PsParameters.DETAIL_LEVEL_LOW));
                    }
                    if (PsParameters.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                        matrices.add(this.psMatrixJson.toJson(currentMatrix, PsParameters.DETAIL_LEVEL_HIGH));
                    }
                }
                json.put(PsCloud.MATRICES, matrices);
            }
        }
        return json;


    }

    @Override
    public JSONArray toJson(List<PsCloud> listOfClouds) {
        JSONArray listOfCloudsJson = new JSONArray();
        Iterator iter = listOfClouds.iterator();
        while (iter.hasNext()) {
            PsCloud currentCloud = (PsCloud) iter.next();
            JSONObject currentCloudJson = this.toJson(currentCloud);
            listOfCloudsJson.add(currentCloudJson);
        }
        return listOfCloudsJson;
    }

    @Override
    public JSONArray toJson(List<PsCloud> listOfClouds, String detailLevel) {
        JSONArray listOfCloudsJson = new JSONArray();
        Iterator iter = listOfClouds.iterator();
        while (iter.hasNext()) {
            PsCloud currentCloud = (PsCloud) iter.next();
            JSONObject currentCloudJson = this.toJson(currentCloud, detailLevel);
            listOfCloudsJson.add(currentCloudJson);
        }
        return listOfCloudsJson;
    }
}
