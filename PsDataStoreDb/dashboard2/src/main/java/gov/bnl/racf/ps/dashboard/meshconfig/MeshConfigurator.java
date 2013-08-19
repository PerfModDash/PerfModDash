/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.meshconfig;

import gov.bnl.racf.ps.dashboard.db.data_objects.*;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsHostManipulator;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsMatrixManipulator;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsObjectCreator;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class MeshConfigurator {

    private String mode;
    private PrintWriter out;
    private Session session;
    private JSONObject json;
    // cloud to be configured by current json
    private PsCloud cloud;
    private List<PsMatrix> listOfMatrices = new ArrayList<PsMatrix>();
    private StringBuffer outBuffer;

    public void setSession(Session session) {
        this.session = session;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setJson(JSONObject jsonObject) {
        this.json = jsonObject;
    }

    public JSONObject getJson() {
        return json;
    }

    void setOutBuffer(StringBuffer outBuffer) {
        this.outBuffer = outBuffer;
    }

    public void configure() throws Exception {
        //throw new UnsupportedOperationException("Not yet implemented");
        configureCloud();
        //this.out.println("keys json=" + json2keys(json));
        this.outBuffer.append("keys json=" + json2keys(json) + "\n");
        configureMatrices();

        this.outBuffer.append("assign matrices to cloud\n");
        this.assignMatricesToCloud();

    }

    private PsCloud createCloudIfNotExists(String cloudName) {
        PsCloud cloud = PsDataStore.getCloudByName(this.session, cloudName);
        if (cloud == null) {
            cloud = PsObjectCreator.createNewCloudWithGivenName(session, cloudName);
            this.outBuffer.append("Created new cloud " + cloudName + "\n");
        } else {
            this.outBuffer.append("Known cloud " + cloudName + "\n");
        }
        return cloud;
    }

    private PsSite createSiteIfNotExists(String siteName) {
        PsSite site = PsDataStore.getSiteByName(this.session, siteName);
        if (site == null) {
            site = PsObjectCreator.createNewSiteWithGivenName(session, siteName);
            this.outBuffer.append("Created new site " + siteName + "\n");
        } else {
            this.outBuffer.append("Known site " + siteName + "\n");
        }
        return site;
    }

    private PsHost createHostIfNotExists(String hostName) {
        PsHost host = PsDataStore.getHostByName(this.session, hostName);
        if (host == null) {
            host = PsObjectCreator.createNewHostWithGivenName(session, hostName);
            this.outBuffer.append("Created new host " + hostName + "\n");
        } else {
            this.outBuffer.append("Known host " + hostName + "\n");
        }
        return host;
    }

    private PsMatrix createMatrixIfNotExists(String matrixName, PsServiceType matrixType) {
        PsMatrix matrix = PsDataStore.getMatrixByNameAndType(this.session, matrixName, matrixType);
        if (matrix == null) {
            matrix = PsObjectCreator.createNewMatrix(session, matrixType, matrixName);
        }
        return matrix;
    }

    private String json2keys(JSONObject jsonObject) {
        String result = "";
        Iterator it = jsonObject.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            result = result + " " + key;
        }
        return result;
    }

    private boolean isBandwidthHost(JSONObject host) {

        if (host.containsKey("measurement_archives")) {
            JSONArray measurementArchives =
                    (JSONArray) host.get("measurement_archives");
            Iterator archiveIterator = measurementArchives.iterator();

            while (archiveIterator.hasNext()) {
                JSONObject archiveJson = (JSONObject) archiveIterator.next();
                if (archiveJson.containsKey("type")) {
                    String type = (String) archiveJson.get("type");
                    if (type.contains("bwctl")) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean isLatencyHost(JSONObject host) {
        if (host.containsKey("measurement_archives")) {
            JSONArray measurementArchives =
                    (JSONArray) host.get("measurement_archives");
            Iterator archiveIterator = measurementArchives.iterator();

            while (archiveIterator.hasNext()) {
                JSONObject archiveJson = (JSONObject) archiveIterator.next();
                if (archiveJson.containsKey("type")) {
                    String type = (String) archiveJson.get("type");
                    if (type.contains("owamp")) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private void configureCloud() {
        String jsonCloudName = (String) json.get("description");
        this.cloud = this.createCloudIfNotExists(jsonCloudName);

        JSONArray jsonOrganisations = (JSONArray) this.json.get("organizations");
        Iterator iter = jsonOrganisations.iterator();
        while (iter.hasNext()) {
            JSONObject organisation = (JSONObject) iter.next();
            JSONArray sites = (JSONArray) organisation.get("sites");
            Iterator sitesIterator = sites.iterator();
            while (sitesIterator.hasNext()) {
                JSONObject site = (JSONObject) sitesIterator.next();
                String siteDescription = (String) site.get("description");

                PsSite currentSite = createSiteIfNotExists(siteDescription);
                if (this.cloud.containsSite(currentSite)) {
                } else {
                    this.cloud.addSite(currentSite);
                    this.outBuffer.append("Site " + currentSite.getName() + " added to cloud " + this.cloud.getName() + "\n");
                }
                if (site.containsKey("hosts")) {
                    JSONArray listOfHostsAtSite = (JSONArray) site.get("hosts");

                    Iterator hostsIterator = listOfHostsAtSite.iterator();
                    while (hostsIterator.hasNext()) {
                        JSONObject hostJson = (JSONObject) hostsIterator.next();

                        String hostName = (String) ((JSONArray) hostJson.get("addresses")).get(0);

                        PsHost host = createHostIfNotExists(hostName);

                        if (isBandwidthHost(hostJson)) {
                            outBuffer.append("This is bandwidth host\n");
                            PsHostManipulator.addThroughputServices(session, host);
                        } else {
                        }
                        if (isLatencyHost(hostJson)) {
                            outBuffer.append("This is latency host\n");
                            PsHostManipulator.addLatencyServices(session, host);
                        } else {
                        }


                        if (currentSite.containsHost(host)) {
                        } else {
                            currentSite.addHost(host);
                            this.outBuffer.append("Host " + host.getHostname() + " added to " + currentSite.getName() + "\n");
                        }
                    }
                } else {
                    this.outBuffer.append("site " + siteDescription + " does not contain hosts information\n");
                }
            }

        }
        this.outBuffer.append("\n");
    }

    private boolean testIsOfKnownType(JSONObject test) {
        if (testIsBandwidth(test)) {
            return true;
        }
        if (testIsLatency(test)) {
            return true;
        }
        if (testIsTraceroute(test)) {
            return true;
        }
        return false;
    }

    private boolean testIsTraceroute(JSONObject test) {
        if (test.containsKey("parameters")) {
            JSONObject parameters = (JSONObject) test.get("parameters");
            if (parameters.containsKey("type")) {
                String type = (String) parameters.get("type");
                if (type != null) {
                    if (type.contains("traceroute")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean testIsBandwidth(JSONObject test) {
        if (test.containsKey("parameters")) {
            JSONObject parameters = (JSONObject) test.get("parameters");
            if (parameters.containsKey("type")) {
                String type = (String) parameters.get("type");
                if (type != null) {
                    if (type.contains("bwctl")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean testIsLatency(JSONObject test) {
        if (test.containsKey("parameters")) {
            JSONObject parameters = (JSONObject) test.get("parameters");
            if (parameters.containsKey("type")) {
                String type = (String) parameters.get("type");
                if (type != null) {
                    if (type.contains("owamp")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean testMembersAreOfKnownType(JSONObject test) {
        JSONObject members = (JSONObject) test.get("members");
        if (membersTypeIsMesh(members)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean membersTypeIsMesh(JSONObject members) {
        if (members.containsKey("type")) {
            String type = (String) members.get("type");
            if ("mesh".equals(type)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private PsServiceType testType(JSONObject test) throws Exception {
        String typeId = "";
        if (testIsLatency(test)) {
            typeId = PsServiceType.LATENCY;
        }
        if (testIsBandwidth(test)) {
            typeId = PsServiceType.THROUGHPUT;
        }
        if (testIsTraceroute(test)) {
            typeId = PsServiceType.TRACEROUTE;
        }
        PsServiceType serviceType =
                PsDataStore.getServiceType(this.session, typeId);
        if ("".equals(typeId)) {
            throw new Exception("unknown test type for test=" + test.toString());
        }
        return serviceType;
    }

    private void configureMatrices() throws Exception {
        JSONArray tests = (JSONArray) this.json.get("tests");
        Iterator testsIter = tests.iterator();
        while (testsIter.hasNext()) {
            JSONObject test = (JSONObject) testsIter.next();

            this.outBuffer.append("test: description=" + (String) test.get("description") + "\n");
            this.outBuffer.append("test: parameters=" + (JSONObject) test.get("parameters") + "\n");
            this.outBuffer.append("test: members=" + (JSONObject) test.get("members") + "\n");

            if (testIsOfKnownType(test)) {
                if (testMembersAreOfKnownType(test)) {

                    if (testIsLatency(test)) {
                        this.outBuffer.append("test is latency+\n");
                    }
                    if (testIsBandwidth(test)) {
                        this.outBuffer.append("test is bandwidth+\n");
                    }
                    if (testIsTraceroute(test)) {
                        this.outBuffer.append("test is traceroute+\n");
                    }
                    if (!testIsTraceroute(test)) {
                        PsServiceType serviceType = testType(test);
                        String matrixName = (String) test.get("description");

                        if (PsDataStore.containsMatrixOfNameAndType(this.session,
                                matrixName,
                                serviceType)) {
                            this.outBuffer.append("This is known matrix+\n");
                        } else {
                            this.outBuffer.append("This is unknown matrix+\n");
                        }
                        PsMatrix matrix = createMatrixIfNotExists(matrixName, serviceType);
                        this.listOfMatrices.add(matrix);

                        JSONObject members = (JSONObject) test.get("members");
                        if (membersTypeIsMesh(members)) {
                            if (members.containsKey("members")) {
                                JSONArray listOfHostNames = (JSONArray) members.get("members");
                                Iterator hostNameIter = listOfHostNames.iterator();
                                while (hostNameIter.hasNext()) {
                                    String hostName = (String) hostNameIter.next();
                                    PsHost host = PsDataStore.getHostByName(session, hostName);

                                    if (host == null) {
                                        throw new Exception("Unknown host " + hostName);
                                    } else {
                                        if (matrix.containsHost(host)) {
                                            this.outBuffer.append("Matrix contains host " + hostName + "\n");
                                        } else {
                                            this.outBuffer.append("Matrix does not contain host " + hostName + "\n");
                                            PsMatrixManipulator.addHostToMatrix(session, matrix, host);
                                        }
                                    }
                                }
                            } else {
                                this.outBuffer.append("Test has no hosts associated with it \n");
                            }

                        } else {
                            throw new Exception("Unsupported members configuration type " + test);
                        }
                    } else {
                        this.outBuffer.append("Traceroute matrix type is not supported yet \n");
                    }
                } else {
                    this.outBuffer.append("test: test members are of unknown type, skip\n");
                }
            } else {
                this.outBuffer.append("test: test is of unknown type, skip\n");
            }

        }

    }

    private void assignMatricesToCloud() {
        this.outBuffer.append("Assign matrices to clouds\n");
        Iterator iter = this.listOfMatrices.iterator();
        while (iter.hasNext()) {
            PsMatrix currentMatrix = (PsMatrix) iter.next();
            if (!this.cloud.containsMatrix(currentMatrix)) {
                this.outBuffer.append("Assign marix " + currentMatrix.getName()
                        + " to cloud " + this.cloud.getName() + "\n");
                this.cloud.addMatrix(currentMatrix);
            } else {
                this.outBuffer.append("Cloud " + this.cloud.getName()
                        + " contains matrix " + currentMatrix.getName() + "\n");
            }
        }
    }
}
