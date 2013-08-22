/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects.factories.impl;

import gov.bnl.racf.ps.dashboard3.domainobjects.PsHost;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsService;
import gov.bnl.racf.ps.dashboard3.domainobjects.PsServiceType;
import gov.bnl.racf.ps.dashboard3.domainobjects.factories.PsServiceFactory;
import gov.bnl.racf.ps.dashboard3.parameters.PsParameters;
import java.util.Date;
import java.util.TreeMap;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Hibernate implementation of the service factory class.
 * @author tomw
 */
public class PsServiceFactorySimpleImpl implements PsServiceFactory {
    // --- constants part --///
    private static int DEFAULT_CHECKINTERVAL = 1200;
    private static int DEFAULT_TIMEOUT = 60;

    // --- dependency injection part ---//
    
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
   
    /**
     * create primitive service of given type running on given host
     *
     * @param type
     * @param host
     */
    public  PsService createService(PsServiceType type, PsHost host) {

        PsService service = null;
        if (PsParameters.BWCTL_PORT_4823.equals(type.getServiceTypeId())) {
            service = this.create_BWCTL_PORT_4823( type, host);
        }
        if (PsParameters.BWCTL_PORT_8570.equals(type.getServiceTypeId())) {
            service = create_BWCTL_PORT_8570(type, host);
        }
        if (PsParameters.CHECK_LOOKUP_SERVICE.equals(type.getServiceTypeId())) {
            service = create_CHECK_LOOKUP_SERVICE(type, host);
        }
        if (PsParameters.NDT_PORT_3001.equals(type.getServiceTypeId())) {
            service = create_NDT_PORT_3001(type, host);
        }
        if (PsParameters.NDT_PORT_7123.equals(type.getServiceTypeId())) {
            service = create_NDT_PORT_7123(type, host);
        }
        if (PsParameters.NPAD_PORT_8000.equals(type.getServiceTypeId())) {
            service = create_NPAD_PORT_8000(type, host);
        }
        if (PsParameters.NPAD_PORT_8001.equals(type.getServiceTypeId())) {
            service = create_NPAD_PORT_8001(type, host);
        }
        if (PsParameters.OWP_861.equals(type.getServiceTypeId())) {
            service = create_OWP_861(type, host);
        }
        if (PsParameters.OWP_8569.equals(type.getServiceTypeId())) {
            service = create_OWP_8569(type, host);
        }
        if (PsParameters.PERFSONAR_PSB.equals(type.getServiceTypeId())) {
            service = create_PERFSONAR_PSB(type, host);
        }

        return service;
    }

    /**
     * create a two host service of given type running between source host and
     * destination host
     *
     * @param type
     * @param source
     * @param destination
     */
    public  PsService createService(PsServiceType type,
            PsHost source, PsHost destination) {

        PsService service = null;
        if (PsParameters.TRACEROUTE.equals(type.getId())) {
            service = this.create_TRACEROUTE(type, source, destination);
        }
        return service;
    }

    /**
     * create a three host service of given type between source and destination
     * hosts, monitored from monitor host
     *
     * @param type
     * @param source
     * @param destination
     * @param monitor
     * @return
     */
    public  PsService createService(PsServiceType type,
            PsHost source, PsHost destination, PsHost monitor) {
        PsService service = null;
        if (PsParameters.LATENCY.equals(type.getServiceTypeId())) {
            service = create_LATENCY(type, source, destination, monitor);
        }
        if (PsParameters.THROUGHPUT.equals(type.getServiceTypeId())) {
            service = create_THROUGHPUT(type, source, destination, monitor);
        }
        return service;
    }

    private  PsService create_BWCTL_PORT_4823(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        parameters.put("port", new Integer(4823));
        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_BWCTL_PORT_8570(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        parameters.put("port", new Integer(4823));
        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_CHECK_LOOKUP_SERVICE(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        int port = 9995;
        parameters.put("port", new Integer(port));
        String url = "http://" + host.getHostname() + ":" + port + "/perfSONAR_PS/services/hLS";
        parameters.put("url", url);
        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_NDT_PORT_3001(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        parameters.put("port", new Integer(3001));

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_NDT_PORT_7123(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        parameters.put("port", new Integer(7123));

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_NPAD_PORT_8000(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        parameters.put("port", new Integer(8000));

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_NPAD_PORT_8001(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        parameters.put("port", new Integer(8001));

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_OWP_861(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        parameters.put("port", new Integer(861));

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_OWP_8569(PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        parameters.put("port", new Integer(8569));

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_PERFSONAR_PSB( PsServiceType type, PsHost host) {
        PsService service = new PsService();
        service.setType(type.getServiceTypeId());
        service.setName(type.getServiceTypeId() + "_on_" + host.getHostname());
        service.setDescription(type.getName());
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("host-id", host.getId());
        parameters.put("host", host.getHostname());
        String url="";
        if (host.getIpv4() != null) {
            url = "http://" + host.getIpv4() + ":8085/perfSONAR_PS/services/pSB";
        }else{
            if(host.getHostname()!=null){
                url = "http://" + host.getHostname() + ":8085/perfSONAR_PS/services/pSB";
            }else{
                throw new RuntimeException("Host has neither hostname nor ip name");
            }
        }
        parameters.put("url", url);
        parameters.put("template", "2");

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_LATENCY(PsServiceType type,
            PsHost source, PsHost destination, PsHost monitor) {
        PsService service = new PsService();

        service.setType(type.getServiceTypeId());
        String name = "packet loss_between_" + source.getHostname()
                + "_and_" + destination.getHostname()
                + "mon_" + monitor.getHostname();
        service.setName(name);
        service.setDescription(name);
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();

        parameters.put("source-host-id", source.getId());
        parameters.put("destination-host-id", destination.getId());
        parameters.put("ma-host-id", monitor.getId());

        parameters.put("source", source.getHostname());
        parameters.put("destination", destination.getHostname());
        parameters.put("monitor", monitor.getHostname());
        String port = "8085";
        String url = "http://" + monitor.getHostname() + ":" + port + "/perfSONAR_PS/services/pSB";
        parameters.put("url", url);

        parameters.put("timeRange", "1800");
        parameters.put("loss", new Integer(1));
        parameters.put("warningThreshold", ":2");
        parameters.put("criticalThreshold", ":10");

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;

    }

    private  PsService create_THROUGHPUT(PsServiceType type,
            PsHost source, PsHost destination, PsHost monitor) {

        PsService service = new PsService();

        service.setType(type.getServiceTypeId());
        String name = "throughput_between_" + source.getHostname()
                + "_and_" + destination.getHostname()
                + "mon_" + monitor.getHostname();
        service.setName(name);
        service.setDescription(name);
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();

        parameters.put("source-host-id", source.getId());
        parameters.put("destination-host-id", destination.getId());
        parameters.put("ma-host-id", monitor.getId());

        parameters.put("source", source.getHostname());
        parameters.put("destination", destination.getHostname());
        parameters.put("monitor", monitor.getHostname());
        String port = "8085";
        String url = "http://" + monitor.getHostname() + ":" + port + "/perfSONAR_PS/services/pSB";
        parameters.put("url", url);

        parameters.put("timeRange", "86400");
        parameters.put("warningThreshold", ".1:");
        parameters.put("criticalThreshold", ".01:");

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }

    private  PsService create_TRACEROUTE(PsServiceType type,
            PsHost source, PsHost destination) {
        PsService service = new PsService();

        service.setType(type.getServiceTypeId());
        String name = "traceroute_" + source.getHostname()
                + "_to_" + destination.getHostname();
        service.setName(name);
        service.setDescription(name);
        service.setCheckInterval(DEFAULT_CHECKINTERVAL);

        TreeMap<String, Object> parameters = new TreeMap<String, Object>();

        parameters.put("source-host-id", source.getId());
        parameters.put("destination-host-id", destination.getId());

        parameters.put("source", source.getHostname());
        parameters.put("destination", destination.getHostname());

        String port = "8086";
        String url = "http://" + source.getHostname() + ":" + port + "/perfSONAR_PS/services/tracerouteMA";
        parameters.put("url", url);

        parameters.put("timeRange", "3600");
        parameters.put("warningThreshold", "1:");
        parameters.put("criticalThreshold", "1:");

        service.setParameters(parameters);

        service.setPrevCheckTime(new Date(1));
        service.setNextCheckTime(new Date());

        service.setRunning(false);
        service.setRunningSince(null);
        service.setTimeout(DEFAULT_TIMEOUT);

        service.setResult(null);

        return service;
    }
}
