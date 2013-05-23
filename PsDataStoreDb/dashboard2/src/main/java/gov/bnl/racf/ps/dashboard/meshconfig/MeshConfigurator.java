/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.meshconfig;

import gov.bnl.racf.ps.dashboard.db.data_objects.PsCloud;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsHost;
import gov.bnl.racf.ps.dashboard.db.data_objects.PsSite;
import gov.bnl.racf.ps.dashboard.db.data_store.PsDataStore;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsHostManipulator;
import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsObjectCreator;
import java.io.PrintWriter;
import java.util.Iterator;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class MeshConfigurator {

    private JSONObject json;
    private String mode;
    private PrintWriter out;
    private Session session;

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

    public void configure() {
        //throw new UnsupportedOperationException("Not yet implemented");
        configureCloud();
    }

    private PsCloud createCloudIfNotExists(String cloudName) {
        PsCloud cloud = PsDataStore.getCloudByName(this.session, cloudName);
        if (cloud == null) {
            cloud = PsObjectCreator.createNewCloudWithGivenName(session, cloudName);
            this.out.println("Created new cloud " + cloudName+"<BR>");
        } else {
            this.out.println("Known cloud " + cloudName+"<BR>");
        }
        return cloud;
    }

    private PsSite createSiteIfNotExists(String siteName) {
        PsSite site = PsDataStore.getSiteByName(this.session, siteName);
        if (site == null) {
            site = PsObjectCreator.createNewSiteWithGivenName(session, siteName);
            this.out.println("Created new site " + siteName+"<BR>");
        } else {
            this.out.println("Known site " + siteName+"<BR>");
        }
        return site;
    }

    private PsHost createHostIfNotExists(String hostName) {
        PsHost host = PsDataStore.getHostByName(this.session, hostName);
        if (host == null) {
            host = PsObjectCreator.createNewHostWithGivenName(session, hostName);
            this.out.println("Created new host " + hostName+"<BR>");
        } else {
            this.out.println("Known host " + hostName+"<BR>");
        }
        return host;
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
        PsCloud cloud = this.createCloudIfNotExists(jsonCloudName);

        JSONArray jsonOrganisations = (JSONArray) this.json.get("organizations");
        Iterator iter = jsonOrganisations.iterator();
        while (iter.hasNext()) {
            JSONObject organisation = (JSONObject) iter.next();
            //out.println("========================");
            //out.println("organisation description="
            //        + (String) organisation.get("description"));
            JSONArray sites = (JSONArray) organisation.get("sites");
            Iterator sitesIterator = sites.iterator();
            while (sitesIterator.hasNext()) {
                JSONObject site = (JSONObject) sitesIterator.next();
                String siteDescription = (String) site.get("description");
                //out.println("site description = " + siteDescription);
                
                PsSite currentSite = createSiteIfNotExists(siteDescription);
                if (cloud.containsSite(currentSite)) {
                    //out.println("cloud " + cloud.getName()
                    //        + " contains site " + currentSite.getName());
                } else {
                    //out.println("cloud " + cloud.getName()
                    //        + " does not contain site " + currentSite.getName());
                    cloud.addSite(currentSite);
                    this.out.println("Site "+currentSite.getName()+" added to cloud "+cloud.getName()+"<BR>");
                }
                //out.println("site keys=" + json2keys(site));
                if (site.containsKey("hosts")) {
                    JSONArray listOfHostsAtSite = (JSONArray) site.get("hosts");

                    Iterator hostsIterator = listOfHostsAtSite.iterator();
                    while (hostsIterator.hasNext()) {
                        JSONObject hostJson = (JSONObject) hostsIterator.next();

                        //out.println("host keys=" + json2keys(hostJson));
                        String hostName = (String) ((JSONArray) hostJson.get("addresses")).get(0);
                        //out.println("host info: description=" + hostJson.get("description")
                        //        + " address=" + hostJson.get("addresses"));
                        //if (PsDataStore.getHostByName(this.session, hostName) != null) {
                        //    out.println("This is known host");
                        //} else {
                        //    out.println("This is unknown host");
                        //}
                        PsHost host = createHostIfNotExists(hostName);

                        if (isBandwidthHost(hostJson)) {
                            //out.println("This is bandwidth host");
                            PsHostManipulator.addThroughputServices(session, host);
                        }
                        if (isLatencyHost(hostJson)) {
                            //out.println("This is latency host");
                            PsHostManipulator.addLatencyServices(session, host);
                        }


                        if (currentSite.containsHost(host)) {
                            //out.println("Site " + currentSite.getName() + " contains host " + host.getHostname());
                        } else {
                            currentSite.addHost(host);
                            this.out.println("Host " + host.getHostname() + " added to " + currentSite.getName()+"<BR>");
                        }
                    }
                } else {
                    this.out.println("site "+siteDescription+" does not contain hosts information<BR>");
                }
            }

        }
        this.out.println("<BR>");
    }
}
