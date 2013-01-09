/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;

import java.util.Vector;
import java.util.Iterator;

/**
 * Object describing perfSonar host
 * @author tomw
 */
public class PsHost {

    // field names in JSONObjects
    public static final String ID = "id";
    public static final String HOSTNAME = "hostname";
    public static final String IPV4 = "ipv4";
    public static final String IPV6 = "ipv6";
    public static final String SERVICES = "services";
    
    // host API commands
    public static final String HOST_ADD_SERVICE_COMMAND = "addservice";
    
    
    private String id;
    private String hostname;
    private String ipv4;
    private String ipv6;
    private Vector<PsService> services = new Vector<PsService>();

    /**
     * get and set methods
     *
     * @return
     */
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    /**
     * Constructor to generate empty host, only id is filled
     */
    public PsHost() {
        id = PsIdGenerator.generateId();
    }

    /**
     * Constructor, hostname, ipv4 and ipv6 are parameters id is filled
     * automatically
     *
     * @param hostname
     * @param ipv4
     * @param ipv6
     */
    public PsHost(String hostname, String ipv4, String ipv6) {
        id = PsIdGenerator.generateId();
        this.hostname = hostname;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
    }

    /**
     * Constructor, host name and ipv4 are given as parameters id is filled
     * internally
     *
     * @param hostname
     * @param ipv4
     */
    public PsHost(String hostname, String ipv4) {
        id = PsIdGenerator.generateId();
        this.hostname = hostname;
        this.ipv4 = ipv4;
    }

    /**
     * get iterator over services
     *
     * @return
     */
    public Iterator<PsService> serviceIterator() {
        return services.iterator();
    }

    /**
     * remove all services from this host
     */
    public void removeAllServices() {
        services.removeAllElements();
    }

    /**
     * check if host contains servcieId
     *
     * @param serviceId
     * @return
     */
    public boolean containsService(String serviceId) {
        boolean result = false;
        Iterator iter = serviceIterator();
        while (iter.hasNext()) {
            if (((PsService) iter.next()).getId().equals(serviceId)) {
                return true;
            }
        }
        return result;
    }

    /**
     * check if host contains service
     *
     * @param service
     * @return
     */
    public boolean containsService(PsService service) {
        return containsService(service.getId());
    }

    /**
     * add service to a host
     *
     * @param service
     */
    public void addService(PsService service) {
        services.add(service);
    }

    /**
     * remove service from host this does not delete the service from data store
     * deletion from data store should be done by object manipulator
     *
     * @param serviceId
     */
    public void removeService(String serviceId) {
        Iterator iter = serviceIterator();
        while (iter.hasNext()) {
            if (((PsService) iter.next()).getId().equals(serviceId)) {
                iter.remove();
            }
        }
    }

    /**
     * remove service from host this does not delete the service from data store
     * deletion from data store should be done by object manipulator
     *
     * @param service
     */
    public void removeService(PsService service) {
        removeService(service.getId());
    }

    /**
     * checks if the host has a service of type type associated with it
     *
     * @param type
     * @return
     */
    public boolean hasServiceType(PsServiceType type) {
        Iterator<PsService> iter = serviceIterator();
        while (iter.hasNext()) {
            PsService currentService = (PsService) iter.next();
            String currentTypeId = currentService.getType();
            if (type != null) {
                if (currentTypeId.equals(type.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PsHost other = (PsHost) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
