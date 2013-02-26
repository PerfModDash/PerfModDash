/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.object_manipulators;

import gov.bnl.racf.ps.dashboard.db.data_objects.*;
import gov.racf.bnl.ps.dashboard.PsApi.PsApi;
import java.util.*;
import javax.persistence.Id;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * Utility class to convert perfsonar objects into their JSON representation
 *
 * @author tomw
 */
public class JsonConverter {

    public static JSONObject psHost2Json(PsHost host) {
        return psHost2Json(host, PsApi.DETAIL_LEVEL_LOW);
    }

    /**
     * static method to convert host to JsonObject
     *
     * @param host
     * @return
     */
    public static JSONObject psHost2Json(PsHost host, String detailLevel) {
        JSONObject json = new JSONObject();


        json.put(PsHost.ID, int2String(host.getId()));
        json.put(PsHost.HOSTNAME, host.getHostname());

        if (!PsApi.DETAIL_LEVEL_LOW.equals(detailLevel)) {
            
            json.put(PsHost.IPV4, host.getIpv4());
            json.put(PsHost.IPV6, host.getIpv6());

            JSONArray services = new JSONArray();
            Iterator<PsService> iter = host.serviceIterator();
            while (iter.hasNext()) {

                PsService service = (PsService) iter.next();
                if (PsApi.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                    JSONObject serviceObject = toJson(service, PsApi.DETAIL_LEVEL_LOW);
                    services.add(serviceObject);
                }
                if (PsApi.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                    JSONObject serviceObject = toJson(service, PsApi.DETAIL_LEVEL_HIGH);
                    services.add(serviceObject);
                }

            }
            json.put(PsHost.SERVICES, services);
        }

        return json;
    }

    /**
     * convert host object to Json
     *
     * @param host
     * @return
     */
    public static JSONObject toJson(PsHost host) {
        return psHost2Json(host);
    }

    public static JSONObject toJson(PsHost host, String detailLevel) {
        return psHost2Json(host, detailLevel);
    }

    /**
     * convert parameter info object into JSON
     *
     * @param parameterInfo
     * @return
     */
    public static JSONObject toJson(PsParameterInfo parameterInfo) {
        JSONObject json = new JSONObject();
        json.put(PsParameterInfo.DESCRIPTION, parameterInfo.getDescription());
        json.put(PsParameterInfo.UNIT, parameterInfo.getUnit());
        return json;
    }

    public static JSONObject toJson(PsServiceType serviceType) {
        JSONObject json = new JSONObject();
        // warning: externally id means 'service type id'
        json.put(PsServiceType.ID, serviceType.getServiceTypeId());
        // internal id is the id in the database
        json.put(PsServiceType.INTERNAL_ID, int2String(serviceType.getId()));
        json.put(PsServiceType.JOB_TYPE, serviceType.getJobType());
        json.put(PsServiceType.NAME, serviceType.getName());

        // get serviceParameterInfo
        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                serviceType.getServiceParameterInfo();
        JSONObject serviceParameterInfoJson = new JSONObject();
        if (serviceParameterInfo != null) {
            Iterator keyIterator = serviceParameterInfo.keySet().iterator();
            while (keyIterator.hasNext()) {
                String currentParameterName = (String) keyIterator.next();
                PsParameterInfo currentParameterInfo =
                        serviceParameterInfo.get(currentParameterName);
                JSONObject currentParameterInfoAsJson = toJson(currentParameterInfo);
                serviceParameterInfoJson.put(currentParameterName, currentParameterInfoAsJson);
            }
        }
        json.put(PsServiceType.SERVICE_PARAMETER_INFO, serviceParameterInfoJson);

        // get result parameter info 
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                serviceType.getResultParameterInfo();
        JSONObject resultParameterInfoJson = new JSONObject();

        if (resultParameterInfo != null) {
            Iterator keyIterator2 = resultParameterInfo.keySet().iterator();
            while (keyIterator2.hasNext()) {
                String currentParameterName = (String) keyIterator2.next();
                PsParameterInfo currentParameterInfo =
                        resultParameterInfo.get(currentParameterName);
                JSONObject currentParameterInfoAsJson = toJson(currentParameterInfo);
                resultParameterInfoJson.put(currentParameterName, currentParameterInfoAsJson);
            }
        }
        json.put(PsServiceType.RESULT_PARAMETER_INFO, resultParameterInfoJson);
        return json;
    }

    /**
     * convert service object to JSON according with default detail level
     *
     * @param service
     * @param detailLevel
     * @return
     */
    public static JSONObject toJson(PsService service) {
        return toJson(service, PsApi.DETAIL_LEVEL_HIGH);
    }

    /**
     * Convert perfsonar service to Json object according to specified detail
     * level
     *
     * @param service
     * @return
     */
    public static JSONObject toJson(PsService service, String detailLevel) {

        JSONObject json = new JSONObject();

        if (service != null) {

            json.put(PsService.ID, int2String(service.getId()));
            json.put(PsService.NAME, service.getName());

            if (!PsApi.DETAIL_LEVEL_LOW.equals(detailLevel)) {

                json.put(PsService.TYPE, service.getType());
                json.put(PsService.DESCRIPTION, service.getDescription());


                // add service parameters

                if (service.getParameters() != null) {
                    JSONObject serviceParameters = serviceParametersAsJson(service);
                    json.put(PsService.PARAMETERS, serviceParameters);
                }


                json.put(PsService.CHECK_INTERVAL, new Integer(service.getCheckInterval()));
                json.put(PsService.RUNNING, service.isRunning());
                json.put(PsService.PREV_CHECK_TIME,
                        IsoDateConverter.dateToString(service.getPrevCheckTime()));
                json.put(PsService.NEXT_CHECK_TIME,
                        IsoDateConverter.dateToString(service.getNextCheckTime()));
                json.put(PsService.RUNNING_SINCE,
                        IsoDateConverter.dateToString(service.getRunningSince()));
                json.put(PsService.TIMEOUT, service.getTimeout());


                // add result
                PsRecentServiceResult result = service.getResult();
                JSONObject jsonResult = toJson(result);
                json.put(PsService.RESULT, jsonResult);
            }

        }

        return json;
    }

    /**
     * get service parameters of this particular service and convert them into
     * JSON object
     *
     * @param service
     * @return
     */
    public static JSONObject serviceParametersAsJson(PsService service) {
        JSONObject serviceParameters = new JSONObject();

        Iterator iter = service.getParameters().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();

            Object value = service.getParameters().get(key);
            serviceParameters.put(key, value);
        }
        return serviceParameters;
    }

    /**
     * convert service result object to JSON
     *
     * @param result
     * @return
     */
    public static JSONObject toJson(PsServiceResult result) {
        JSONObject json = new JSONObject();

        if (result != null) {
            json.put(PsServiceResult.ID, int2String(result.getId()));
            json.put(PsServiceResult.JOB_ID, int2String(result.getJob_id()));
            json.put(PsServiceResult.SERVICE_ID, int2String(result.getService_id()));
            json.put(PsServiceResult.STATUS, result.getStatus());
            json.put(PsServiceResult.MESSAGE, result.getMessage());

            json.put(PsServiceResult.TIME, IsoDateConverter.dateToString(result.getTime()));

            JSONObject parameters = new JSONObject();
            TreeMap<String, Object> treeMap = result.getParameters();
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                parameters.put(key, value);
            }
            json.put(PsServiceResult.PARAMETERS, parameters);

        }

        return json;
    }
    
    /**
     * convert service result object to JSON
     *
     * @param result
     * @return
     */
    public static JSONObject toJson(PsRecentServiceResult result) {
        JSONObject json = new JSONObject();

        if (result != null) {
            json.put(PsRecentServiceResult.ID, int2String(result.getId()));
            json.put(PsRecentServiceResult.SERVICE_RESULT_ID, int2String(result.getServiceResultId()   ));
            json.put(PsRecentServiceResult.JOB_ID, int2String(result.getJob_id()));
            json.put(PsRecentServiceResult.SERVICE_ID, int2String(result.getService_id()));
            json.put(PsRecentServiceResult.STATUS, result.getStatus());
            json.put(PsRecentServiceResult.MESSAGE, result.getMessage());

            json.put(PsRecentServiceResult.TIME, IsoDateConverter.dateToString(result.getTime()));

            JSONObject parameters = new JSONObject();
            TreeMap<String, Object> treeMap = result.getParameters();
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                parameters.put(key, value);
            }
            json.put(PsRecentServiceResult.PARAMETERS, parameters);

        }

        return json;
    }
    

    public static JSONObject toJson(PsSite site) {
        return toJson(site, PsApi.DETAIL_LEVEL_HIGH);
    }

    public static JSONObject toJson(PsSite site, String detailLevel) {
        JSONObject json = new JSONObject();
        if (site != null) {
            json.put(PsSite.ID, int2String(site.getId()));
            json.put(PsSite.NAME, site.getName());

            if (!PsApi.DETAIL_LEVEL_LOW.equals(detailLevel)) {

                
                json.put(PsSite.DESCRIPTION, site.getDescription());
                json.put(PsSite.STATUS, site.getStatus());

                JSONArray listOfHosts = new JSONArray();
                Collection<PsHost> listOfHostsInSite = site.getHosts();
                Iterator<PsHost> iter = listOfHostsInSite.iterator();
                while (iter.hasNext()) {
                    PsHost currentHost = (PsHost) iter.next();
                    if (PsApi.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                        JSONObject currentHostJson = toJson(currentHost, PsApi.DETAIL_LEVEL_LOW);
                        listOfHosts.add(currentHostJson);
                    }
                    if (PsApi.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                        JSONObject currentHostJson = toJson(currentHost, PsApi.DETAIL_LEVEL_HIGH);
                        listOfHosts.add(currentHostJson);
                    }
                }
                json.put(PsSite.HOSTS, listOfHosts);
            }
        }
        return json;
    }

    public static JSONObject toJson(PsMatrix matrix) {
        return toJson(matrix, PsApi.DETAIL_LEVEL_HIGH);
    }

    public static JSONObject toJson(PsMatrix matrix, String detailLevel) {
        JSONObject json = new JSONObject();
        if (matrix != null) {
            json.put(PsMatrix.ID, int2String(matrix.getId()));
            json.put(PsMatrix.NAME, matrix.getName());
            
            if (!PsApi.DETAIL_LEVEL_LOW.equals(detailLevel)) {
                
                json.put(PsMatrix.DETAIL_LEVEL, matrix.getDetailLevel());

                List<String> statusLabels = matrix.getStatusLabels();
                JSONArray jsonArray = new JSONArray();
                if (statusLabels != null) {
                    if (!statusLabels.isEmpty()) {
                        for (int i = 0; i < statusLabels.size(); i = i + 1) {
                            jsonArray.add(statusLabels.get(i));
                        }
                    }
                }
                json.put(PsMatrix.STATUS_LABELS, jsonArray);

                Date lastUpdateTime = matrix.getLastUpdateTime();
                json.put(PsMatrix.LAST_UPDATE_TIME,
                        IsoDateConverter.dateToString(lastUpdateTime));

                JSONArray rows = new JSONArray();
                for (int i = 0; i < matrix.getNumberOfRows(); i = i + 1) {
                    PsHost currentHost = matrix.getHostInRow(i);
                    rows.add(currentHost.getHostname());
                }
                json.put(PsMatrix.ROWS, rows);

                JSONArray columns = new JSONArray();
                for (int i = 0; i < matrix.getNumberOfColumns(); i = i + 1) {
                    PsHost currentHost = matrix.getHostInColumn(i);
                    columns.add(currentHost.getHostname());
                }
                json.put(PsMatrix.COLUMNS, columns);

                JSONArray serviceNames = new JSONArray();
                for (int i = 0; i < matrix.getNumberOfServiceNames(); i = i + 1) {
                    serviceNames.add(matrix.getServiceNames().get(i));
                }
                json.put(PsMatrix.SERVICE_NAMES, serviceNames);

                JSONArray matrixArray = new JSONArray();
                for (int i = 0; i < matrix.getNumberOfColumns(); i = i + 1) {
                    JSONArray rowsArray = new JSONArray();
                    for (int j = 0; j < matrix.getNumberOfRows(); j = j + 1) {
                        JSONArray serviceArray = new JSONArray();
                        for (int k = 0; k < matrix.getNumberOfServiceNames(); k = k + 1) {
                            PsService currentService = matrix.getService(i, j, k);

                            String serviceDetailLevel = PsApi.DETAIL_LEVEL_HIGH;
                            if (PsApi.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                                serviceDetailLevel = PsApi.DETAIL_LEVEL_LOW;
                            }
                            if (PsApi.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                                serviceDetailLevel = PsApi.DETAIL_LEVEL_HIGH;
                            }
                            JSONObject currentServiceJson =
                                    JsonConverter.toJson(currentService, serviceDetailLevel);
                            serviceArray.add(currentServiceJson);
                        }
                        rowsArray.add(serviceArray);
                    }
                    matrixArray.add(rowsArray);
                }
                json.put(PsMatrix.MATRIX, matrixArray);
            }
        }

        return json;
    }

    public static JSONObject toJson(PsCloud cloud) {
        return toJson(cloud, PsApi.DETAIL_LEVEL_HIGH);
    }

    public static JSONObject toJson(PsCloud cloud, String detailLevel) {
        JSONObject json = new JSONObject();
        if (cloud != null) {
            json.put(PsCloud.ID, int2String(cloud.getId()));
            json.put(PsCloud.NAME, cloud.getName());

            if (!PsApi.DETAIL_LEVEL_LOW.equals(detailLevel)) {
                
                json.put(PsCloud.STATUS, cloud.getStatus());

                JSONArray sites = new JSONArray();
                Iterator<PsSite> iter = cloud.sitesIterator();
                while (iter.hasNext()) {
                    PsSite currentSite = (PsSite) iter.next();
                    if (PsApi.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                        sites.add(toJson(currentSite, PsApi.DETAIL_LEVEL_LOW));
                    }
                    if (PsApi.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                        sites.add(toJson(currentSite, PsApi.DETAIL_LEVEL_HIGH));
                    }
                }
                json.put(PsCloud.SITES, sites);

                JSONArray matrices = new JSONArray();
                Iterator<PsMatrix> iter2 = cloud.matrixIterator();
                while (iter2.hasNext()) {
                    PsMatrix currentMatrix = (PsMatrix) iter2.next();
                    if (PsApi.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                        matrices.add(toJson(currentMatrix,PsApi.DETAIL_LEVEL_LOW));
                    }
                    if (PsApi.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                        matrices.add(toJson(currentMatrix,PsApi.DETAIL_LEVEL_HIGH));
                    }
                }
                json.put(PsCloud.MATRICES, matrices);
            }
        }
        return json;
    }

    public static JSONObject toJson(PsJob job) {
        JSONObject json = new JSONObject();

        json.put(PsJob.ID, int2String(job.getId()));
        json.put(PsJob.SERVICE_ID, int2String(job.getService_id()));
        json.put(PsJob.TYPE, job.getType());

        JSONObject parameters = serviceParametersAsJson(job);
        json.put(PsJob.PARAMETERS, parameters);

        return json;
    }

    private static JSONObject serviceParametersAsJson(PsJob job) {
        JSONObject serviceParameters = new JSONObject();

        Iterator iter = job.getParameters().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();

            Object value = job.getParameters().get(key);
            serviceParameters.put(key, value);
        }
        return serviceParameters;
    }

    public static String int2String(int intValue) {
        Integer integerVariable = new Integer(intValue);
        return integerVariable.toString();
    }
}
