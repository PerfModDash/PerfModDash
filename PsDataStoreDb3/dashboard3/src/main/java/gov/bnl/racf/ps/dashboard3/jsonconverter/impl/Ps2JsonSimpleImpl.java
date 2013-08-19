/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.jsonconverter.Ps2Json;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class Ps2JsonSimpleImpl implements Ps2Json {

    @Override
    public JSONObject toJson(PsHost host, String detailLevel) {
        JSONObject json = new JSONObject();
        
        json.put(PsHost.ID, int2String(host.getId()));
        json.put(PsHost.HOSTNAME, host.getHostname());

        if (!PsParameters.DETAIL_LEVEL_LOW.equals(detailLevel)) {

            json.put(PsHost.IPV4, host.getIpv4());
            json.put(PsHost.IPV6, host.getIpv6());

            //TODO services part is to be added later
            
//            JSONArray services = new JSONArray();
//            Iterator<PsService> iter = host.serviceIterator();
//            while (iter.hasNext()) {
//
//                PsService service = (PsService) iter.next();
//                if (PsParameters.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
//                    JSONObject serviceObject = toJson(service, PsParameters.DETAIL_LEVEL_LOW);
//                    services.add(serviceObject);
//                }
//                if (PsParameters.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
//                    JSONObject serviceObject = toJson(service, PsParameters.DETAIL_LEVEL_HIGH);
//                    services.add(serviceObject);
//                }
//
//            }
//            json.put(PsHost.SERVICES, services);
        }
        
        return json;
    }
    
    @Override
    public JSONObject toJson(PsHost host) {
        return toJson(host, PsParameters.DETAIL_LEVEL_LOW);
    }

    @Override
    public JSONArray toJson(List<PsHost> listOfHosts) {
        return toJson(listOfHosts,PsParameters.DETAIL_LEVEL_LOW);
    }
    
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
    
    
    public static String int2String(int intValue) {
        Integer integerVariable = new Integer(intValue);
        return integerVariable.toString();
    }

    

    
}
