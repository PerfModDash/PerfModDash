/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.jsonconverter.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsMatrix;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsHostJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsMatrixJson;
import gov.bnl.racf.ps.dashboard3.jsonconverter.PsServiceJson;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import gov.bnl.racf.utils.IsoDateConverter;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class PsMatrixJsonSimpleImpl implements PsMatrixJson{
    
    //=== dependency injection ===//
    private PsHostJson psHostJson;

    public void setPsHostJson(PsHostJson psHostJson) {
        this.psHostJson = psHostJson;
    }
    
    private PsServiceJson psServiceJson;

    public void setPsServiceJson(PsServiceJson psServiceJson) {
        this.psServiceJson = psServiceJson;
    }
    

    /**
     * convert matrix to JSONObject
     * @param matrix
     * @return 
     */
    @Override
    public JSONObject toJson(PsMatrix matrix) {
        return this.toJson(matrix, PsParameters.DETAIL_LEVEL_LOW);
    }

    /**
     * convert matrix to JSONObject using requested detail level
     * @param matrix
     * @param detailLevel
     * @return 
     */
    @Override
    public JSONObject toJson(PsMatrix matrix, String detailLevel) {
        
        JSONObject json = new JSONObject();
        if (matrix != null) {
            json.put(PsMatrix.ID, ""+matrix.getId());
            json.put(PsMatrix.NAME, matrix.getName());

            if (!PsParameters.DETAIL_LEVEL_LOW.equals(detailLevel)) {

                json.put(PsMatrix.DETAIL_LEVEL, matrix.getDetailLevel());
                json.put(PsMatrix.SERVICE_TYPE_ID, matrix.getMatrixType().getServiceTypeId());

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
                    JSONObject hostAsJson = this.psHostJson.toJson(currentHost,PsParameters.DETAIL_LEVEL_LOW);
                    //rows.add(currentHost.getHostname());
                    rows.add(hostAsJson);
                }
                json.put(PsMatrix.ROWS, rows);

                JSONArray columns = new JSONArray();
                for (int i = 0; i < matrix.getNumberOfColumns(); i = i + 1) {
                    PsHost currentHost = matrix.getHostInColumn(i);
                    JSONObject hostAsJson = this.psHostJson.toJson(currentHost,PsParameters.DETAIL_LEVEL_LOW);
                    //columns.add(currentHost.getHostname());
                    columns.add(hostAsJson);
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

                            String serviceDetailLevel = PsParameters.DETAIL_LEVEL_HIGH;
                            if (PsParameters.DETAIL_LEVEL_MEDIUM.equals(detailLevel)) {
                                serviceDetailLevel = PsParameters.DETAIL_LEVEL_LOW;
                            }
                            if (PsParameters.DETAIL_LEVEL_HIGH.equals(detailLevel)) {
                                serviceDetailLevel = PsParameters.DETAIL_LEVEL_HIGH;
                            }
                            JSONObject currentServiceJson =
                                    this.psServiceJson.toJson(currentService, serviceDetailLevel);
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

    /**
     * convert list of matrices to JSONArray using requested detail level
     * @param listOfMatrices
     * @param detailLevel
     * @return 
     */
    @Override
    public JSONArray toJson(List<PsMatrix> listOfMatrices, String detailLevel) {
        JSONArray resultList = new JSONArray();
        for(PsMatrix matrix : listOfMatrices){
            resultList.add(this.toJson(matrix, detailLevel));
        }
        return resultList;
    }

    /**
     * convert list of matrices to JSONArray
     * @param listOfMatrices
     * @return 
     */
    @Override
    public JSONArray toJson(List<PsMatrix> listOfMatrices) {
        JSONArray resultList = new JSONArray();
        for(PsMatrix matrix : listOfMatrices){
            resultList.add(this.toJson(matrix));
        }
        return resultList;
    }
    
}
