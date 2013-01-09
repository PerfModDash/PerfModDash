/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;

import gov.bnl.racf.ps.dashboard.data_objects.PsParameterInfo;
import gov.bnl.racf.ps.dashboard.data_objects.PsServiceType;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Factory of service types //TODO add second owamp service!!!
 *
 * @author tomw
 */
public class PsServiceTypeFactory {

    public static String BWCTL_PORT_4823 = "bwctl_port_4823";
    public static String BWCTL_PORT_8570 = "bwctl_port_8570";
    public static String CHECK_LOOKUP_SERVICE = "CheckLookupService";
    public static String NDT_PORT_3001 = "NDT_port_3001";
    public static String NDT_PORT_7123 = "NDT_port_7123";
    public static String NPAD_PORT_8000 = "NPAD_port_8000";
    public static String NPAD_PORT_8001 = "NPAD_port_8001";
    public static String OWP_861 = "owp_861";
    public static String OWP_8569 = "owp_8569";
    public static String PERFSONAR_PSB = "perfSONAR_pSB";
    public static String LATENCY = "latency";
    public static String THROUGHPUT = "throughput";
    public static String TRACEROUTE = "traceroute";

    public static PsServiceType createType(String typeName) {
        PsServiceType type = null;
        if (isKnownType(typeName)) {

            if (BWCTL_PORT_4823.equals(typeName)) {
                type = create_BWCTL_PORT_4823();
            }
            if (BWCTL_PORT_8570.equals(typeName)) {
                type = create_BWCTL_PORT_8570();
            }
            if (CHECK_LOOKUP_SERVICE.equals(typeName)) {
                type = create_CHECK_LOOKUP_SERVICE();
            }
            if (NDT_PORT_3001.equals(typeName)) {
                type = create_NDT_PORT_3001();
            }
            if (NDT_PORT_7123.equals(typeName)) {
                type = create_NDT_PORT_7123();
            }
            if (NPAD_PORT_8000.equals(typeName)) {
                type = create_NPAD_PORT_8000();
            }
            if (NPAD_PORT_8001.equals(typeName)) {
                type = create_NPAD_PORT_8001();
            }
            if (OWP_861.equals(typeName)) {
                type = create_OWP_861();
            }
            if (OWP_8569.equals(typeName)) {
                type = create_OWP_8569();
            }
            if (PERFSONAR_PSB.equals(typeName)) {
                type = create_PERFSONAR_PSB();
            }
            if (LATENCY.equals(typeName)) {
                type = create_LATENCY();
            }
            if (THROUGHPUT.equals(typeName)) {
                type = create_THROUGHPUT();
            }
            if (TRACEROUTE.equals(typeName)) {
                type = create_TRACEROUTE();
            }
        }
        return type;
    }

    public static boolean isKnownType(String typeName) {
        if (BWCTL_PORT_4823.equals(typeName)) {
            return true;
        }
        if (BWCTL_PORT_8570.equals(typeName)) {
            return true;
        }
        if (CHECK_LOOKUP_SERVICE.equals(typeName)) {
            return true;
        }
        if (NDT_PORT_3001.equals(typeName)) {
            return true;
        }
        if (NDT_PORT_7123.equals(typeName)) {
            return true;
        }
        if (NPAD_PORT_8000.equals(typeName)) {
            return true;
        }
        if (NPAD_PORT_8001.equals(typeName)) {
            return true;
        }
        if (OWP_861.equals(typeName)) {
            return true;
        }
        if (OWP_8569.equals(typeName)) {
            return true;
        }
        if (PERFSONAR_PSB.equals(typeName)) {
            return true;
        }
        if (LATENCY.equals(typeName)) {
            return true;
        }
        if (THROUGHPUT.equals(typeName)) {
            return true;
        }
        if (TRACEROUTE.equals(typeName)) {
            return true;
        }
        return false;
    }

    /**
     * check if the type refers to a matrix service type
     * @param typeName
     * @return 
     */
    public static boolean isMatrixType(String typeName){
        if (LATENCY.equals(typeName)) {
            return true;
        }
        if (THROUGHPUT.equals(typeName)) {
            return true;
        }
        if (TRACEROUTE.equals(typeName)) {
            return true;
        }
        return false;
    }
    public static boolean isPrimitiveServiceThroughput(String typeName) {
        if (BWCTL_PORT_4823.equals(typeName)) {
            return true;
        }
        if (BWCTL_PORT_8570.equals(typeName)) {
            return true;
        }
        if (CHECK_LOOKUP_SERVICE.equals(typeName)) {
            return true;
        }
        if (NDT_PORT_3001.equals(typeName)) {
            return true;
        }
        if (NDT_PORT_7123.equals(typeName)) {
            return true;
        }
        if (NPAD_PORT_8000.equals(typeName)) {
            return true;
        }
        if (NPAD_PORT_8001.equals(typeName)) {
            return true;
        }

        if (PERFSONAR_PSB.equals(typeName)) {
            return true;
        }

        return false;
    }

    public static boolean isPrimitiveService(String typeName) {
        if (BWCTL_PORT_4823.equals(typeName)) {
            return true;
        }
        if (BWCTL_PORT_8570.equals(typeName)) {
            return true;
        }
        if (CHECK_LOOKUP_SERVICE.equals(typeName)) {
            return true;
        }
        if (NDT_PORT_3001.equals(typeName)) {
            return true;
        }
        if (NDT_PORT_7123.equals(typeName)) {
            return true;
        }
        if (NPAD_PORT_8000.equals(typeName)) {
            return true;
        }
        if (NPAD_PORT_8001.equals(typeName)) {
            return true;
        }
        if (OWP_861.equals(typeName)) {
            return true;
        }
        if (OWP_8569.equals(typeName)) {
            return true;
        }
        if (PERFSONAR_PSB.equals(typeName)) {
            return true;
        }
        return false;
    }

    public static boolean isPrimitiveServiceLatency(String typeName) {

        if (CHECK_LOOKUP_SERVICE.equals(typeName)) {
            return true;
        }
        if (PERFSONAR_PSB.equals(typeName)) {
            return true;
        }
        if (OWP_861.equals(typeName)) {
            return true;
        }
        if (OWP_8569.equals(typeName)) {
            return true;
        }
        return false;
    }

    public static Vector<String> listOfServiceTypes() {
        Vector<String> serviceTypes = new Vector<String>();
        serviceTypes.add(BWCTL_PORT_4823);
        serviceTypes.add(BWCTL_PORT_8570);
        serviceTypes.add(CHECK_LOOKUP_SERVICE);
        serviceTypes.add(NDT_PORT_3001);
        serviceTypes.add(NDT_PORT_7123);
        serviceTypes.add(NPAD_PORT_8000);
        serviceTypes.add(NPAD_PORT_8001);
        serviceTypes.add(OWP_861);
        serviceTypes.add(OWP_8569);
        serviceTypes.add(PERFSONAR_PSB);
        serviceTypes.add(LATENCY);
        serviceTypes.add(THROUGHPUT);
        serviceTypes.add(TRACEROUTE);

        return serviceTypes;
    }

    private static PsServiceType create_BWCTL_PORT_4823() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(BWCTL_PORT_4823);
        type.setJobType("nagios.tcp");
        type.setName("BWCTL Port 4823 Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the target BWCTL host");
        serviceParameterInfo.put("host", param);

        param = new PsParameterInfo();
        param.setDescription("The TCP port where BWCTL should be listening. Should always be 4823 for this check.");
        serviceParameterInfo.put("port", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The time it takes to check_tcp to open a connection to the target host and port");
        param.setUnit("seconds");
        resultParameterInfo.put("time", param);

        type.setResultParameterInfo(resultParameterInfo);


        return type;
    }

    private static PsServiceType create_BWCTL_PORT_8570() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(BWCTL_PORT_8570);
        type.setJobType("nagios.tcp");
        type.setName("BWCTL Port 8570 Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the target BWCTL host");
        serviceParameterInfo.put("host", param);

        param = new PsParameterInfo();
        param.setDescription("The TCP port where BWCTL should be listening. Should always be 8570 for this check.");
        serviceParameterInfo.put("port", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The time it takes to check_tcp to open a connection to the target host and port");
        param.setUnit("seconds");
        resultParameterInfo.put("time", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_CHECK_LOOKUP_SERVICE() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(CHECK_LOOKUP_SERVICE);
        type.setJobType("nagios.ps.perfSONAR");
        type.setName("perfSONAR Lookup Service Test");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The id of the host where this service runs");
        serviceParameterInfo.put("host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The URL of the lookup service to test");
        serviceParameterInfo.put("url", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        resultParameterInfo = null;

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_NDT_PORT_3001() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(NDT_PORT_3001);
        type.setJobType("nagios.tcp");
        type.setName("NDT Port 3001 Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the target NDT host");
        serviceParameterInfo.put("host", param);

        param = new PsParameterInfo();
        param.setDescription("The TCP port where NDT should be listening. Should always be 3001 for this check.");
        serviceParameterInfo.put("port", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The time it takes to check_tcp to open a connection to the target host and port");
        param.setUnit("seconds");
        resultParameterInfo.put("time", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;

    }

    private static PsServiceType create_NDT_PORT_7123() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(NDT_PORT_7123);
        type.setJobType("nagios.tcp");
        type.setName("NDT Port 7123 Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the target NDT host");
        serviceParameterInfo.put("host", param);

        param = new PsParameterInfo();
        param.setDescription("The TCP port where NDT should be listening. Should always be 7123 for this check.");
        serviceParameterInfo.put("port", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The time it takes to check_tcp to open a connection to the target host and port");
        param.setUnit("seconds");
        resultParameterInfo.put("time", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_NPAD_PORT_8000() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(NPAD_PORT_8000);
        type.setJobType("nagios.tcp");
        type.setName("NPAD Port 8001 Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the target NPAD host");
        serviceParameterInfo.put("host", param);

        param = new PsParameterInfo();
        param.setDescription("The TCP port where NPAD should be listening. Should always be 8000 for this check.");
        serviceParameterInfo.put("port", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The time it takes to check_tcp to open a connection to the target host and port");
        param.setUnit("seconds");
        resultParameterInfo.put("time", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_NPAD_PORT_8001() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(NPAD_PORT_8001);
        type.setJobType("nagios.tcp");
        type.setName("NPAD Port 8001 Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the target NPAD host");
        serviceParameterInfo.put("host", param);

        param = new PsParameterInfo();
        param.setDescription("The TCP port where NPAD should be listening. Should always be 8001 for this check.");
        serviceParameterInfo.put("port", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The time it takes to check_tcp to open a connection to the target host and port");
        param.setUnit("seconds");
        resultParameterInfo.put("time", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_OWP_861() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(OWP_861);
        type.setJobType("nagios.tcp");
        type.setName("Nagios Check TCP");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the target host");
        serviceParameterInfo.put("host", param);

        param = new PsParameterInfo();
        param.setDescription("The TCP port in the target host to check");
        serviceParameterInfo.put("port", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The time it takes to check_tcp to open a connection to the target host and port");
        param.setUnit("seconds");
        resultParameterInfo.put("time", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_OWP_8569() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(OWP_8569);
        type.setJobType("nagios.tcp");
        type.setName("Nagios Check TCP");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the target host");
        serviceParameterInfo.put("host", param);

        param = new PsParameterInfo();
        param.setDescription("The TCP port in the target host to check");
        serviceParameterInfo.put("port", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The time it takes to check_tcp to open a connection to the target host and port");
        param.setUnit("seconds");
        resultParameterInfo.put("time", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_PERFSONAR_PSB() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(PERFSONAR_PSB);
        type.setJobType("nagios.ps.perfSONAR");
        type.setName("perfSONAR PSB Echo Request Test");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The id of the host where this service runs");
        serviceParameterInfo.put("host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The URL of the measurement archive to test");
        serviceParameterInfo.put("url", param);

        param = new PsParameterInfo();
        param.setDescription("A number identifying the request to send to the server.");
        serviceParameterInfo.put("template", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        resultParameterInfo = null;

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_LATENCY() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(LATENCY);
        type.setJobType("nagios.ps.owdelay");
        type.setName("OWAMP Loss Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The id of the source host of this check");
        serviceParameterInfo.put("source-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The id of the destination host of this check");
        serviceParameterInfo.put("destination-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The id of the measurement archive of this check");
        serviceParameterInfo.put("ma-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the source of this check");
        serviceParameterInfo.put("source", param);

        param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the destination of this check");
        serviceParameterInfo.put("destination", param);

        param = new PsParameterInfo();
        param.setDescription("The URL of the measurement archive");
        serviceParameterInfo.put("url", param);

        param = new PsParameterInfo();
        param.setDescription("The number of seconds in the past to query data");
        param.setUnit("seconds");
        serviceParameterInfo.put("timeRange", param);

        param = new PsParameterInfo();
        param.setDescription("Boolean indicating whether the check should look at loss. Should always be 1 for this check");
        serviceParameterInfo.put("loss", param);

        param = new PsParameterInfo();
        param.setDescription("The threshold for throughput values being categorized as warning. Should be in standard nagios format.");
        param.setUnit("Gbps");
        serviceParameterInfo.put("warningThreshold", param);

        param = new PsParameterInfo();
        param.setDescription("The threshold for throughput values being categorized as critical. Should be in standard nagios format.");
        param.setUnit("Gbps");
        serviceParameterInfo.put("criticalThreshold", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The number of tests points returned by query");
        resultParameterInfo.put("count", param);

        param = new PsParameterInfo();
        param.setDescription("The minimum loss observed in the time range");
        param.setUnit("pps");
        resultParameterInfo.put("min", param);

        param = new PsParameterInfo();
        param.setDescription("The maximum loss observed in the time range");
        param.setUnit("pps");
        resultParameterInfo.put("max", param);

        param = new PsParameterInfo();
        param.setDescription("The average loss observed in the time range");
        param.setUnit("pps");
        resultParameterInfo.put("average", param);

        param = new PsParameterInfo();
        param.setDescription("The standard deviation of the loss observed in the time range");
        param.setUnit("pps");
        resultParameterInfo.put("standard_deviation", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_THROUGHPUT() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(THROUGHPUT);
        type.setJobType("nagios.ps.throughput");
        type.setName("Throughput Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The id of the source host of this check");
        serviceParameterInfo.put("source-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The id of the destination host of this check");
        serviceParameterInfo.put("destination-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The id of the measurement archive of this check");
        serviceParameterInfo.put("ma-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the source of this check");
        serviceParameterInfo.put("source", param);

        param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the destination of this check");
        serviceParameterInfo.put("destination", param);

        param = new PsParameterInfo();
        param.setDescription("The URL of the measurement archive");
        serviceParameterInfo.put("url", param);

        param = new PsParameterInfo();
        param.setDescription("The number of seconds in the past to query data");
        param.setUnit("seconds");
        serviceParameterInfo.put("timeRange", param);

        param = new PsParameterInfo();
        param.setDescription("The threshold for throughput values being categorized as warning. Should be in standard nagios format.");
        param.setUnit("Gbps");
        serviceParameterInfo.put("warningThreshold", param);

        param = new PsParameterInfo();
        param.setDescription("The threshold for throughput values being categorized as critical. Should be in standard nagios format.");
        param.setUnit("Gbps");
        serviceParameterInfo.put("criticalThreshold", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The number of tests points returned by query");
        resultParameterInfo.put("count", param);

        param = new PsParameterInfo();
        param.setDescription("The minimum bandwidth observed in the time range");
        param.setUnit("Gbps");
        resultParameterInfo.put("min", param);

        param = new PsParameterInfo();
        param.setDescription("The maximum bandwidth observed in the time range");
        param.setUnit("Gbps");
        resultParameterInfo.put("max", param);

        param = new PsParameterInfo();
        param.setDescription("The average bandwidth observed in the time range");
        param.setUnit("Gbps");
        resultParameterInfo.put("average", param);

        param = new PsParameterInfo();
        param.setDescription("The standard deviation of bandwidth observed in the time range");
        param.setUnit("Gbps");
        resultParameterInfo.put("standard_deviation", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }

    private static PsServiceType create_TRACEROUTE() {
        PsServiceType type = PsObjectCreator.createNewServiceType();

        type.setId(TRACEROUTE);
        type.setJobType("nagios.ps.traceroute");
        type.setName("Traceroute Check");

        TreeMap<String, PsParameterInfo> serviceParameterInfo =
                new TreeMap<String, PsParameterInfo>();
        TreeMap<String, PsParameterInfo> resultParameterInfo =
                new TreeMap<String, PsParameterInfo>();

        PsParameterInfo param = new PsParameterInfo();
        param.setDescription("The id of the source host of this check");
        serviceParameterInfo.put("source-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The id of the destination host of this check");
        serviceParameterInfo.put("destination-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The id of the measurement archive of this check");
        serviceParameterInfo.put("ma-host-id", param);

        param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the source of this check");
        serviceParameterInfo.put("source", param);

        param = new PsParameterInfo();
        param.setDescription("The IP address or hostname of the destination of this check");
        serviceParameterInfo.put("destination", param);

        param = new PsParameterInfo();
        param.setDescription("The URL of the measurement archive");
        serviceParameterInfo.put("url", param);

        param = new PsParameterInfo();
        param.setDescription("The number of seconds in the past to query data");
        param.setUnit("seconds");
        serviceParameterInfo.put("timeRange", param);

        param = new PsParameterInfo();
        param.setDescription("The threshold for the number of paths being categorized as warning. Should be in standard nagios format.");
        param.setUnit("Gbps");
        serviceParameterInfo.put("warningThreshold", param);

        param = new PsParameterInfo();
        param.setDescription("The threshold for number of paths being categorized as critical. Should be in standard nagios format.");
        param.setUnit("Gbps");
        serviceParameterInfo.put("criticalThreshold", param);

        type.setServiceParameterInfo(serviceParameterInfo);

        param = new PsParameterInfo();
        param.setDescription("The minimum number of paths observed");
        resultParameterInfo.put("pathcountmin", param);

        param = new PsParameterInfo();
        param.setDescription("The maximum number of paths observed");
        resultParameterInfo.put("pathcountmax", param);

        param = new PsParameterInfo();
        param.setDescription("The average number of paths observed");
        resultParameterInfo.put("pathcountaverage", param);

        param = new PsParameterInfo();
        param.setDescription("The standard deviation of the number of paths observed");
        resultParameterInfo.put("pathcountstddev", param);

        param = new PsParameterInfo();
        param.setDescription("");
        resultParameterInfo.put("", param);

        param = new PsParameterInfo();
        param.setDescription("The number of tests observed");
        resultParameterInfo.put("testcount", param);

        param = new PsParameterInfo();
        param.setDescription("The maximum number of tests observed");
        resultParameterInfo.put("testnumtimesrunmax", param);

        param = new PsParameterInfo();
        param.setDescription("testnumtimesrunaverage");
        resultParameterInfo.put("", param);

        param = new PsParameterInfo();
        param.setDescription("The average number of tests observed");
        resultParameterInfo.put("testnumtimesrunaverage", param);

        param = new PsParameterInfo();
        param.setDescription("The standard deviation of the number of tests observed");
        resultParameterInfo.put("testnumtimesrunstddev", param);

        type.setResultParameterInfo(resultParameterInfo);

        return type;
    }
}
