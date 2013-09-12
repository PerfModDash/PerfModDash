/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;


import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsHostJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tomw
 */
public class PsHostJsonSimpleImpl implements PsHostJson {

    // === dependency injection part === //
    
    private PsServiceJson psServiceJson;

    public void setPsServiceJson(PsServiceJson psServiceJson) {
        this.psServiceJson = psServiceJson;
    }
    
    //=== body of the class ===//
    
    @Transactional
    @Override
    public JSONObject toJson(PsHost host, String detailLevel) {
        JSONObject json = new JSONObject();
        
        json.put(PsHost.ID, int2String(host.getId()));
        json.put(PsHost.HOSTNAME, host.getHostname());

        if (!PsParameters.DETAIL_LEVEL_LOW.equals(detailLevel)) {

            json.put(PsHost.IPV4, host.getIpv4());
            json.put(PsHost.IPV6, host.getIpv6());
          
            List<PsService>listOfServicesOnThisHost=(List<PsService>)host.getServices();
            json.put(PsHost.SERVICES, this.psServiceJson.toJson(listOfServicesOnThisHost));
        }
        
        return json;
    }
    
    @Transactional
    @Override
    public JSONObject toJson(PsHost host) {
        return toJson(host, PsParameters.DETAIL_LEVEL_LOW);
    }

    @Transactional
    @Override
    public JSONArray toJson(List<PsHost> listOfHosts) {
        return toJson(listOfHosts,PsParameters.DETAIL_LEVEL_LOW);
    }
    
    @Transactional
    @Override
    public JSONArray toJson(List<PsHost> listOfHosts, String detailLevel) {
        JSONArray jsonArray = new JSONArray();
        Iterator iter = listOfHosts.iterator();
        while(iter.hasNext()){
            PsHost currentHost = (PsHost)iter.next();
            jsonArray.add(toJson(currentHost,detailLevel));
        }
        return jsonArray;
    }
    
    @Transactional
    public static String int2String(int intValue) {
        Integer integerVariable = new Integer(intValue);
        return integerVariable.toString();
    }
    
}
