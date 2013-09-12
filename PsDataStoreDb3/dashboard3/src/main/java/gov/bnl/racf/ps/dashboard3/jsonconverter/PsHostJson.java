/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public interface PsHostJson {

    @Transactional
    public JSONObject toJson(PsHost host);

    @Transactional
    public JSONObject toJson(PsHost host, String detailLevel);

    @Transactional
    public JSONArray toJson(List<PsHost> listOfHosts);

    @Transactional
    public JSONArray toJson(List<PsHost> listOfHosts, String detailLevel);
}
