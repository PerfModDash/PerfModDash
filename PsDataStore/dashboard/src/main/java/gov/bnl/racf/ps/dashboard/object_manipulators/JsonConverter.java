/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.*;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * Utility class to convert perfsonar objects into their JSON representation
 *
 * @author tomw
 */
public class JsonConverter {

    /**
     * static method to convert host to JsonObject
     *
     * @param host
     * @return
     */
    public static JSONObject psHost2Json(PsHost host) {
        JSONObject json = new JSONObject();

        json.put(PsHost.ID, host.getId());
        json.put(PsHost.HOSTNAME, host.getHostname());
        json.put(PsHost.IPV4, host.getIpv4());
        json.put(PsHost.IPV6, host.getIpv6());

        JSONArray services = new JSONArray();
        Iterator<PsService> iter = host.serviceIterator();
        while (iter.hasNext()) {
            PsService service = (PsService) iter.next();
            String serviceId = service.getId();
            services.add(serviceId);
        }
        json.put(PsHost.SERVICES, services);

        return json;
    }

    public static JSONObject psSite2Json(PsSite site) {
        JSONObject json = new JSONObject();
        json.put(PsSite.ID, site.getId());
        json.put(PsSite.NAME, site.getName());
        json.put(PsSite.DESCRIPTION, site.getDescription());
        json.put(PsSite.STATUS, site.getStatus());

        JSONArray listOfHosts = new JSONArray();
        Vector<String> listOfHostIds = site.getHostIds();
        Iterator<String> iter = listOfHostIds.iterator();
        while (iter.hasNext()) {
            String currentId = (String) iter.next();
            listOfHosts.add(currentId);
        }
        json.put(PsSite.HOSTS, listOfHosts);

        return json;
    }

    /**
     * convert perfSonar cloud to JSON object
     *
     * @param cloud
     * @return
     */
    public static JSONObject psCloud2Json(PsCloud cloud) {
        JSONObject json = new JSONObject();
        json.put(PsCloud.ID, cloud.getId());
        json.put(PsCloud.NAME, cloud.getName());
        json.put(PsCloud.STATUS, cloud.getStatus());

        JSONArray sites = new JSONArray();
        Iterator<PsSite> iter = cloud.sitesIterator();
        while (iter.hasNext()) {
            PsSite currentSite = (PsSite) iter.next();
            String id = currentSite.getId();
            sites.add(id);
        }
        json.put(PsCloud.SITES, sites);

        JSONArray matrices = new JSONArray();
        Iterator<PsMatrix> iter2 = cloud.matrixIterator();
        while (iter2.hasNext()) {
            PsMatrix currentMatrix = (PsMatrix) iter2.next();
            String id = currentMatrix.getId();
            matrices.add(id);
        }
        json.put(PsCloud.MATRICES, matrices);

        return json;
    }

    /**
     * Convert perfSonar collector into Json object
     *
     * @param collector
     * @return
     */
    public static JSONObject psCollector2Json(PsCollector collector) {
        JSONObject json = new JSONObject();

        json.put(PsCollector.ID, collector.getId());
        json.put(PsCollector.NAME, collector.getName());
        json.put(PsCollector.IPV4, collector.getIpv4());
        json.put(PsCollector.IPV6, collector.getIpv6());
        json.put(PsCollector.HOSTNAME,collector.getHostname());
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
     * get parameters of this job and convert them into JSON object
     *
     * @param job
     * @return
     */
    public static JSONObject serviceParametersAsJson(PsJob job) {
        JSONObject serviceParameters = new JSONObject();

        Iterator iter = job.getParameters().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();

            Object value = job.getParameters().get(key);
            serviceParameters.put(key, value);
        }
        return serviceParameters;
    }

    /**
     * Convert perfsonar service to Json object
     *
     * @param service
     * @return
     */
    public static JSONObject psService2Json(PsService service) {

        JSONObject json = new JSONObject();

        if (service != null) {

            json.put(PsService.ID, service.getId());
            json.put(PsService.TYPE, service.getType());
            json.put(PsService.NAME, service.getName());
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
            PsServiceResult result = service.getResult();
            JSONObject jsonResult = psServiceResult2Json(result);
            json.put(PsService.RESULT, jsonResult);

        }

        return json;
    }

    public static JSONObject psServiceResult2Json(PsServiceResult result) {
        JSONObject json = new JSONObject();

        if (result != null) {
            json.put(PsServiceResult.ID, result.getId());
            json.put(PsServiceResult.JOB_ID, result.getJob_id());
            json.put(PsServiceResult.SERVICE_ID, result.getService_id());
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
     * convert service type object into Json
     *
     * @param type
     * @return
     */
    public static JSONObject psServiceType2Json(PsServiceType type) {

        JSONObject json = new JSONObject();


        json.put(PsServiceType.ID, type.getId());
        json.put(PsServiceType.JOB_TYPE, type.getJobType());
        json.put(PsServiceType.NAME, type.getName());

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                type.getServiceParameterInfo();
        JSONObject serviceParameters = new JSONObject();
        for (Map.Entry<String, PsParameterInfo> entry : serviceParameterInfo.entrySet()) {
            String key = entry.getKey();
            PsParameterInfo infoAsJson = entry.getValue();
            JSONObject jo = JsonConverter.toJson(infoAsJson);
            serviceParameters.put(key, jo);
        }
        json.put(PsServiceType.SERVICE_PARAMETER_INFO, serviceParameters);

        TreeMap<String, PsParameterInfo> resultParameterInfo =
                type.getResultParameterInfo();
        JSONObject resultParameters = new JSONObject();



        if (resultParameterInfo != null) {
            for (Map.Entry<String, PsParameterInfo> entry : resultParameterInfo.entrySet()) {
                String key = entry.getKey();
                PsParameterInfo info = entry.getValue();
                JSONObject infoAsJson = toJson(info);
                resultParameters.put(key, infoAsJson);
            }
        }
        json.put(PsServiceType.RESULT_PARAMETER_INFO, resultParameters);

        return json;
    }

    /**
     * return JSON representation of service matrix //TODO finish coding
     *
     * @param matrix
     * @return
     */
    public static JSONObject psServiceMatrix2Json(PsMatrix matrix) {
        JSONObject json = new JSONObject();
        json.put(PsMatrix.ID, matrix.getId());
        json.put(PsMatrix.NAME, matrix.getName());
        json.put(PsMatrix.DETAIL_LEVEL, matrix.getDetailLevel());

        String[] statusLabels = matrix.getStatusLabels();
        JSONArray jsonArray = new JSONArray();
        if (statusLabels != null) {
            for (int i = 0; i < statusLabels.length; i = i + 1) {
                jsonArray.add(statusLabels[i]);
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
            serviceNames.add(matrix.getServiceNames()[i]);
        }
        json.put(PsMatrix.SERVICE_NAMES, serviceNames);

        JSONArray matrixArray = new JSONArray();
        for (int i = 0; i < matrix.getNumberOfColumns(); i = i + 1) {
            JSONArray rowsArray = new JSONArray();
            for (int j = 0; j < matrix.getNumberOfRows(); j = j + 1) {
                JSONArray serviceArray = new JSONArray();
                for (int k = 0; k < matrix.getNumberOfServiceNames(); k = k + 1) {
                    PsService currentService = matrix.getService(i, j, k);
                    JSONObject currentServiceJson =
                            JsonConverter.psService2Json(currentService);
                    serviceArray.add(currentServiceJson);
                }
                rowsArray.add(serviceArray);
            }
            matrixArray.add(rowsArray);
        }
        json.put(PsMatrix.MATRIX, matrixArray);

        return json;
    }

    /**
     * convert parameter description object into JSON
     *
     * @param info
     * @return
     */
    public static JSONObject psParameterInfo2Json(PsParameterInfo info) {
        JSONObject json = new JSONObject();
        // unit is not mandatory parameter, skip if it is missing
        if (info.getUnit() != null) {
            json.put(PsParameterInfo.UNIT, info.getUnit());
        }
        json.put(PsParameterInfo.DESCRIPTION, info.getDescription());
        return json;
    }

    public static JSONObject psJob2Json(PsJob job) {
        JSONObject json = new JSONObject();

        json.put(PsJob.ID, job.getId());
        json.put(PsJob.SERVICE_ID, job.getService_id());
        json.put(PsJob.TYPE, job.getType());

        JSONObject parameters = serviceParametersAsJson(job);
        json.put(PsJob.PARAMETERS, parameters);

        return json;
    }

    public static JSONObject toJson(PsHost host) {
        return psHost2Json(host);
    }

    public static JSONObject toJson(PsSite site) {
        return psSite2Json(site);
    }

    public static JSONObject toJson(PsCloud cloud) {
        return psCloud2Json(cloud);
    }

    public static JSONObject toJson(PsCollector collector) {
        return psCollector2Json(collector);
    }

    public static JSONObject toJson(PsService service) {
        return psService2Json(service);
    }

    public static JSONObject toJson(PsServiceResult result) {
        return psServiceResult2Json(result);
    }

    public static JSONObject toJson(PsServiceType type) {
        return psServiceType2Json(type);
    }

    public static JSONObject toJson(PsMatrix matrix) {
        return psServiceMatrix2Json(matrix);

    }

    public static JSONObject toJson(PsParameterInfo info) {
        return psParameterInfo2Json(info);
    }

    public static JSONObject toJson(PsJob job) {
        return psJob2Json(job);
    }
}
