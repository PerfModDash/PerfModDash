/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsServiceTypeJson {

    @Transactional
    public JSONObject toJson(PsServiceType serviceType);

    @Transactional
    public JSONObject toJson(PsServiceType serviceType, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsServiceType> listOfServiceTypes);

    @Transactional
    public JSONArray toJson(List<PsServiceType> listOfServiceTypes, String detailLevel);
}
