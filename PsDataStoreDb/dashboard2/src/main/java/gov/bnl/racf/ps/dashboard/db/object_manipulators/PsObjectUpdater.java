/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * Class which takes Ps Objects and their JSON representation unpacks JSON
 * object and updates PsObject according to its values
 *
 * @author tomw
 */
public class PsObjectUpdater {

    /**
     * update collector object
     *
     * @param collector
     * @param json
     */
//    public static void update(PsCollector collector, JSONObject json) {
//        if (json.keySet().contains(PsCollector.NAME)) {
//            collector.setName((String) json.get(PsCollector.NAME));
//        }
//        if (json.keySet().contains(PsCollector.HOSTNAME)) {
//            collector.setHostname((String) json.get(PsCollector.HOSTNAME));
//        }
//        if (json.keySet().contains(PsCollector.IPV4)) {
//            collector.setIpv4((String) json.get(PsCollector.IPV4));
//        }
//        if (json.keySet().contains(PsCollector.IPV6)) {
//            collector.setIpv6((String) json.get(PsCollector.IPV6));
//        }
//    }
//    /**
//     * update PsServiceResult object with parameters from json object
//     *
//     * @param result
//     * @param json
//     */
//    public static void update(PsServiceResult result, JSONObject json) {
//        if (json.keySet().contains(PsServiceResult.SERVICE_ID)) {
//            result.setService_id((String) json.get(PsServiceResult.SERVICE_ID));
//        }
//        if (json.keySet().contains(PsServiceResult.ID)) {
//            result.setId((String) json.get(PsServiceResult.ID));
//        }
//        if (json.keySet().contains(PsServiceResult.JOB_ID)) {
//            result.setService_id((String) json.get(PsServiceResult.JOB_ID));
//        }
//        if (json.keySet().contains(PsServiceResult.STATUS)) {
//            Integer statusInteger = (Integer) json.get(PsServiceResult.STATUS);
//            int status = statusInteger.intValue();
//            result.setStatus(status);
//        }
//        if (json.keySet().contains(PsServiceResult.MESSAGE)) {
//            String message = (String) json.get(PsServiceResult.MESSAGE);
//            result.setMessage(message);
//        }
//        if (json.keySet().contains(PsServiceResult.TIME)) {
//            String timeAsString = (String) json.get(PsServiceResult.TIME);
//            Date time = IsoDateConverter.isoDate2Date(timeAsString);
//            result.setTime(time);
//        }
//        if (json.keySet().contains(PsServiceResult.PARAMETERS)) {
//            JSONObject parameters = (JSONObject) json.get(PsServiceResult.PARAMETERS);
//
//            Iterator iter = parameters.keySet().iterator();
//            while (iter.hasNext()) {
//                String key = (String) iter.next();
//                Object value = (Object) parameters.get(key);
//                result.setParameter(key, value);
//            }
//        }
//    }
//
//    public static void update(PsMatrix matrix, JSONObject json) {
//        if (json.keySet().contains(PsMatrix.NAME)) {
//            matrix.setName((String) json.get(PsMatrix.NAME));
//        }
//        //TODO rest
//    }
//
//    /**
//     * update the parameters object
//     *
//     * @param parameters
//     * @param json
//     */
//    public static void update(PsParameterInfo parameters, JSONObject json) {
//        if (json.keySet().contains(PsParameterInfo.UNIT)) {
//            parameters.setUnit((String) json.get(PsParameterInfo.UNIT));
//        }
//        if (json.keySet().contains(PsParameterInfo.DESCRIPTION)) {
//            parameters.setDescription((String) json.get(PsParameterInfo.DESCRIPTION));
//        }
//    }
//    /**
//     * update service type object
//     *
//     * @param type
//     * @param json
//     */
//    public static void update(PsServiceType type, JSONObject json) {
//        if (json.keySet().contains(PsServiceType.JOB_TYPE)) {
//            String jobType = (String) json.get(PsServiceType.JOB_TYPE);
//            type.setJobType(jobType);
//        }
//
//        if (json.keySet().contains(PsServiceType.NAME)) {
//            type.setName((String) json.get(PsServiceType.NAME));
//        }
//
//        if (json.keySet().contains(PsServiceType.SERVICE_PARAMETER_INFO)) {
//            JSONObject jsonParameterInfo =
//                    (JSONObject) json.get(PsServiceType.SERVICE_PARAMETER_INFO);
//            Iterator iter = jsonParameterInfo.keySet().iterator();
//            while (iter.hasNext()) {
//                String parameter = (String) iter.next();
//                JSONObject jsonDescriptionOfParameter =
//                        (JSONObject) jsonParameterInfo.get(parameter);
//                PsParameterInfo info = json2ParameterInfo(jsonParameterInfo);
//                type.addServiceParameterInfo(parameter, info);
//            }
//        }
//
//        if (json.keySet().contains(PsServiceType.RESULT_PARAMETER_INFO)) {
//
//            JSONObject jsonResultParameterInfo =
//                    (JSONObject) json.get(PsServiceType.RESULT_PARAMETER_INFO);
//            Iterator iter = jsonResultParameterInfo.keySet().iterator();
//            while (iter.hasNext()) {
//                String parameter = (String) iter.next();
//                JSONObject jsonDescriptionOfParameter =
//                        (JSONObject) jsonResultParameterInfo.get(parameter);
//                PsParameterInfo info =
//                        json2ParameterInfo(jsonDescriptionOfParameter);
//                type.addResultParameterInfo(parameter, info);
//            }
//        }
//    }
    /**
     * update host object with values from JSON
     *
     * @param host
     * @param json
     */
    public static void update(PsHost host, JSONObject json) {
        // first order of business is to compare id

        boolean jsonHasValidId = false;
        if (json.keySet().contains(PsHost.ID)) {
            if (json.get(PsHost.ID) != null && !"".equals(json.get(PsHost.ID))) {
                jsonHasValidId = true;
            }
        }
        
        boolean idTestPassed=false;
        
        if (jsonHasValidId) {
            int hostId = host.getId();
            Long hostIdAsLong = (Long) json.get(PsHost.ID);
            int hostIdInJson  = hostIdAsLong.intValue();
            if (hostId != hostIdInJson) {
                System.out.println("ERROR: host id and json id do not match");
                idTestPassed=false;
            }else{
                idTestPassed=true;
            }
        }else{
            idTestPassed=true;
        }

        if (!idTestPassed) {
            System.out.println("ERROR: failed the id test.");
        } else {
            if (json.keySet().contains(PsHost.HOSTNAME)) {
                host.setHostname((String) json.get(PsHost.HOSTNAME));
            }
            if (json.keySet().contains(PsHost.IPV4)) {
                host.setIpv4((String) json.get(PsHost.IPV4));
            }
            if (json.keySet().contains(PsHost.IPV6)) {
                host.setIpv6((String) json.get(PsHost.IPV6));
            }
            // We do not handle services here. 
            // Adding and removeing services is done by dedicated command from 
            // PsApi
        }

    }
//    /**
//     * update cloud with parameters stored in json object
//     *
//     * @param cloud
//     * @param json
//     */
//    public static void update(PsCloud cloud, JSONObject json) {
//
//        // first order of business is to compare id
//
//        boolean jsonHasValidId = false;
//        if (json.keySet().contains(PsCloud.ID)) {
//            if (json.get(PsCloud.ID) != null && !"".equals(json.get(PsCloud.ID))) {
//                jsonHasValidId = true;
//            }
//        }
//
//        if (jsonHasValidId && !cloud.getId().equals(json.get(PsCloud.ID))) {
//            System.out.println("ERROR: cloud id and json id do not match");
//        } else {
//
//            PsDataStore dataStore = PsDataStore.getDataStore();
//
//            if (json.keySet().contains(PsCloud.NAME)) {
//                cloud.setName((String) json.get(PsCloud.NAME));
//            }
//            
//            if (json.keySet().contains(PsCloud.STATUS)) {
//                cloud.setStatus(((Long) json.get(PsCloud.STATUS)).intValue());
//            }
//            
//            if (json.keySet().contains(PsCloud.SITES)) {
//                JSONArray sites = (JSONArray) json.get(PsCloud.SITES);
//                Iterator iter = sites.iterator();
//                while (iter.hasNext()) {
//                    String siteId = (String) iter.next();
//                    PsSite site = dataStore.getSite(siteId);
//                    cloud.addSite(site);
//                }
//            }
//            
//            if (json.keySet().contains(PsCloud.MATRICES)) {
//                JSONArray matrices = (JSONArray) json.get(PsCloud.MATRICES);
//                Iterator iter = matrices.iterator();
//                while (iter.hasNext()) {
//                    String matrixId = (String) iter.next();
//                    cloud.addMatrix(matrixId);
//                }
//            }
//        }
//    }
//
//    /**
//     * update service with parameters from json object
//     *
//     * @param service
//     * @param json
//     */
//    public static void update(PsService service, JSONObject json) {
//        //TODO
//        //         json.put("id", service.getId());
//        Set<String> keys = json.keySet();
//
//        if (keys.contains(PsService.TYPE)) {
//            String type = (String) json.get(PsService.TYPE);
//            service.setType(type);
//        }
//        if (keys.contains(PsService.NAME)) {
//            service.setName((String) json.get(PsService.NAME));
//        }
//        if (keys.contains(PsService.DESCRIPTION)) {
//            service.setDescription((String) json.get(PsService.DESCRIPTION));
//        }
//        if (keys.contains(PsService.PARAMETERS)) {
//            JSONObject jsonParameters = (JSONObject) json.get(PsService.PARAMETERS);
//            Iterator iter = jsonParameters.keySet().iterator();
//            while (iter.hasNext()) {
//                String parameterName = (String) iter.next();
//                Object parameterValue =
//                        (Object) jsonParameters.get(parameterName);
//                service.setParameter(parameterName, parameterValue);
//            }
//        }
//        if (keys.contains(PsService.CHECK_INTERVAL)) {
//            Integer checkIntervalInteger = (Integer) json.get(PsService.CHECK_INTERVAL);
//            int checkInterval = checkIntervalInteger.intValue();
//            service.setCheckInterval(checkInterval);
//        }
//        if (keys.contains(PsService.RUNNING)) {
//            service.setRunning(((Boolean) json.get(PsService.RUNNING)).booleanValue());
//        }
//        if (keys.contains(PsService.PREV_CHECK_TIME)) {
//            Date prevCheckTime =
//                    IsoDateConverter.isoDate2Date((String) json.get(PsService.PREV_CHECK_TIME));
//            service.setPrevCheckTime(prevCheckTime);
//        }
//        if (keys.contains(PsService.NEXT_CHECK_TIME)) {
//            Date nextCheckTime =
//                    IsoDateConverter.isoDate2Date((String) json.get(PsService.NEXT_CHECK_TIME));
//            service.setNextCheckTime(nextCheckTime);
//        }
//        if (keys.contains(PsService.RUNNING_SINCE)) {
//            Date runningSince =
//                    IsoDateConverter.isoDate2Date((String) json.get(PsService.RUNNING_SINCE));
//            service.setNextCheckTime(runningSince);
//        }
//        if (keys.contains(PsService.TIMEOUT)) {
//            service.setTimeout(((Integer) json.get(PsService.TIMEOUT)).intValue());
//        }
//        if (keys.contains(PsService.RESULT)) {
//            JSONObject jsonResult = (JSONObject) json.get(PsService.RESULT);
//            PsServiceResult serviceResult = json2ServiceResult(jsonResult);
//            service.setResult(serviceResult);
//        }
//    }
//
//    /**
//     * update site with parameters from json object
//     *
//     * @param site
//     * @param json
//     */
//    public static void update(PsSite site, JSONObject json) {
//
//
//        if (json.keySet().contains(PsSite.NAME)) {
//            site.setName((String) json.get(PsSite.NAME));
//        }
//        if (json.keySet().contains(PsSite.DESCRIPTION)) {
//            site.setDescription((String) json.get(PsSite.DESCRIPTION));
//        }
//        if (json.keySet().contains(PsSite.STATUS)) {
//            site.setStatus(((Long) json.get(PsSite.STATUS)).intValue());
//        }
//        if (json.keySet().contains(PsSite.HOSTS)) {
//            Vector arrayOfHostIds = (Vector) json.get(PsSite.HOSTS);
//            Iterator<String> iter = arrayOfHostIds.iterator();
//            while (iter.hasNext()) {
//                String hostId = (String) iter.next();
//                if (site.containsHostId(hostId)) {
//                    //do nothing
//                } else {
//                    // add this host to site
//                    PsDataStore psDataStore = PsDataStore.getDataStore();
//                    PsHost host = psDataStore.getHost(hostId);
//                    if (host != null) {
//                        site.addHost(host);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * modify host according to its json object. Only update simple fields:
//     * name, ip and ipv6. Do not touch services on this host
//     *
//     * @param host
//     * @param json
//     */
//    public static void edit(PsHost host, JSONObject json) {
//        // first order of business is to compare id
//        if (!host.getId().equals(json.get(PsHost.ID))) {
//            System.out.println("ERROR: host id and json id do not match");
//        } else {
//            if (json.keySet().contains(PsHost.HOSTNAME)) {
//                host.setHostname((String) json.get(PsHost.HOSTNAME));
//            }
//            if (json.keySet().contains(PsHost.IPV4)) {
//                host.setHostname((String) json.get(PsHost.IPV4));
//            }
//            if (json.keySet().contains(PsHost.IPV6)) {
//                host.setHostname((String) json.get(PsHost.IPV6));
//            }
//        }
//    }
//
//    /**
//     * modify mutable properties of service, like service name or description
//     *
//     * @param service
//     * @param json
//     */
//    public static void edit(PsService service, JSONObject json) {
//        //TODO
//        //         json.put("id", service.getId());
//        Set<String> keys = json.keySet();
//
//        if (keys.contains(PsService.TYPE)) {
//            //TODO
//        }
//        if (keys.contains(PsService.NAME)) {
//            service.setName((String) json.get(PsService.NAME));
//        }
//        if (keys.contains(PsService.DESCRIPTION)) {
//            service.setDescription((String) json.get(PsService.DESCRIPTION));
//        }
//        if (keys.contains(PsService.PARAMETERS)) {
//            //TODO
//        }
//        if (keys.contains(PsService.CHECK_INTERVAL)) {
//            int checkInterval = ((Integer) json.get(PsService.CHECK_INTERVAL)).intValue();
//            service.setCheckInterval(checkInterval);
//        }
//        if (keys.contains(PsService.RUNNING)) {
//            service.setRunning(((Boolean) json.get(PsService.RUNNING)).booleanValue());
//        }
//        if (keys.contains(PsService.PREV_CHECK_TIME)) {
//            Date prevCheckTime =
//                    IsoDateConverter.isoDate2Date((String) json.get(PsService.PREV_CHECK_TIME));
//            service.setPrevCheckTime(prevCheckTime);
//        }
//        if (keys.contains(PsService.NEXT_CHECK_TIME)) {
//            Date nextCheckTime =
//                    IsoDateConverter.isoDate2Date((String) json.get(PsService.NEXT_CHECK_TIME));
//            service.setNextCheckTime(nextCheckTime);
//        }
//        if (keys.contains(PsService.RUNNING_SINCE)) {
//            Date runningSince =
//                    IsoDateConverter.isoDate2Date((String) json.get(PsService.RUNNING_SINCE));
//            service.setNextCheckTime(runningSince);
//        }
//        if (keys.contains(PsService.TIMEOUT)) {
//            service.setTimeout(((Integer) json.get(PsService.TIMEOUT)).intValue());
//        }
//        if (keys.contains(PsService.RESULT)) {
//            //TODO
//        }
//    }
//
//    /**
//     * modify cloud. Only do simple changes, like name change does not add nor
//     * remove sites nor matrices
//     *
//     * @param cloud
//     * @param json
//     */
//    public static void edit(PsCloud cloud, JSONObject json) {
//        // first order of business is to compare id
//        if (!cloud.getId().equals(json.get(PsCloud.ID))) {
//            System.out.println("ERROR: cloud id and json id do not match");
//        } else {
//            if (json.keySet().contains(PsCloud.NAME)) {
//                cloud.setName((String) json.get(PsCloud.NAME));
//            }
//
//            // we do not handle matrices and sites here
//
//        }
//    }
//
//    /**
//     * modify the fields of collector
//     *
//     * @param collector
//     * @param json
//     */
//    public static void edit(PsCollector collector, JSONObject json) {
//        update(collector, json);
//    }
//
//    /**
//     * update site, change its name and description
//     *
//     * @param site
//     * @param json
//     */
//    public static void edit(PsSite site, JSONObject json) {
//        if (json.keySet().contains(PsSite.NAME)) {
//            site.setName((String) json.get(PsSite.NAME));
//        }
//        if (json.keySet().contains(PsSite.DESCRIPTION)) {
//            site.setDescription((String) json.get(PsSite.DESCRIPTION));
//        }
//    }
//
//    /**
//     * edit name of the service type
//     *
//     * @param type
//     * @param json
//     */
//    public static void edit(PsServiceType type, JSONObject json) {
//        if (json.keySet().contains(PsServiceType.NAME)) {
//            type.setName((String) json.get(PsServiceType.NAME));
//        }
//    }
//
//    public static void edit(PsMatrix matrix, JSONObject json) {
//        if (json.keySet().contains(PsMatrix.NAME)) {
//            matrix.setName((String) json.get(PsMatrix.NAME));
//        }
//    }
//
//    /**
//     * create a PsParameter info object from json object
//     *
//     * @param json
//     */
//    public static PsParameterInfo json2ParameterInfo(JSONObject json) {
//        PsParameterInfo parameterInfo = new PsParameterInfo();
//        if (json.keySet().contains(PsParameterInfo.UNIT)) {
//            parameterInfo.setUnit((String) json.get(PsParameterInfo.UNIT));
//        }
//        if (json.keySet().contains(PsParameterInfo.DESCRIPTION)) {
//            parameterInfo.setUnit((String) json.get(PsParameterInfo.DESCRIPTION));
//        }
//        return parameterInfo;
//    }
//
//    public static PsServiceResult json2ServiceResult(JSONObject json) {
//        PsServiceResult result = new PsServiceResult();
//
//        if (json.keySet().contains(PsServiceResult.ID)) {
//            String id = (String) json.get(PsServiceResult.ID);
//            result.setId(id);
//        }
//        if (json.keySet().contains(PsServiceResult.JOB_ID)) {
//            String job_id = (String) json.get(PsServiceResult.JOB_ID);
//            result.setJob_id(job_id);
//        }
//        if (json.keySet().contains(PsServiceResult.SERVICE_ID)) {
//            String service_id = (String) json.get(PsServiceResult.SERVICE_ID);
//            result.setService_id(service_id);
//        }
//        if (json.keySet().contains(PsServiceResult.STATUS)) {
//            Integer statusInteger = (Integer) json.get(PsServiceResult.STATUS);
//            int status = statusInteger.intValue();
//            result.setStatus(status);
//        }
//        if (json.keySet().contains(PsServiceResult.MESSAGE)) {
//            String message = (String) json.get(PsServiceResult.MESSAGE);
//            result.setMessage(message);
//        }
//        if (json.keySet().contains(PsServiceResult.TIME)) {
//            String isoTime = (String) json.get(PsServiceResult.TIME);
//            Date time = IsoDateConverter.isoDate2Date(isoTime);
//            result.setTime(time);
//        }
//        if (json.keySet().contains(PsServiceResult.PARAMETERS)) {
//            JSONObject jsonParameters = (JSONObject) json.get(PsServiceResult.PARAMETERS);
//            Iterator iter = jsonParameters.keySet().iterator();
//            while (iter.hasNext()) {
//                String parameterName = (String) iter.next();
//                Double parameterValue =
//                        (Double) jsonParameters.get(parameterName);
//                result.setParameter(parameterName, parameterValue);
//            }
//        }
//
//        return result;
//    }
}
