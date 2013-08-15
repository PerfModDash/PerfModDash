/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.objects;

//import gov.bnl.racf.ps.dashboard.db.object_manipulators.PsServiceTypeFactory;
import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;
import java.util.Iterator;
import javax.persistence.*;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author tomw
 */
@Cacheable
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class PsHost {

    // field names in JSONObjects
    public static final String ID = "id";
    public static final String HOSTNAME = "hostname";
    public static final String IPV4 = "ipv4";
    public static final String IPV6 = "ipv6";
    public static final String SERVICES = "services";
    @Id
    @GeneratedValue
    private int id;
    private String hostname = "";
    private String ipv4 = "";
    private String ipv6 = "";

    // Commented out until we return services
//    @ManyToMany(cascade = CascadeType.ALL)
//    private Collection<PsService> services = new Vector<PsService>();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        this.hostname = hostname;
        this.ipv4 = ipv4;
    }

    /**
     * get iterator over services
     *
     * @return
     */
//    public Iterator<PsService> serviceIterator() {
//        return services.iterator();
//    }
    /**
     * check if host contains servcieId
     *
     * @param serviceId
     * @return
     */
//    public boolean containsService(int serviceId) {
//        boolean result = false;
//        Iterator iter = serviceIterator();
//        while (iter.hasNext()) {
//            if (((PsService) iter.next()).getId() == serviceId) {
//                return true;
//            }
//        }
//        return result;
//    }
    /**
     * check if host contains service
     *
     * @param service
     * @return
     */
//    public boolean containsService(PsService service) {
//        return containsService(service.getId());
//    }
    /**
     * add service to a host
     *
     * @param service
     */
//    public void addService(PsService service) {
//        if (!hasServiceType(service.getType())) {
//            services.add(service);
//        }
//    }
    /**
     * remove service from host this does not delete the service from data store
     * deletion from data store should be done by object manipulator
     *
     * @param serviceId
     */
//    public void removeService(int serviceId) {
//        Iterator iter = serviceIterator();
//        while (iter.hasNext()) {
//            if (((PsService) iter.next()).getId() == serviceId) {
//                iter.remove();
//            }
//        }
//    }
    /**
     * remove service from host this does not delete the service from data store
     * deletion from data store should be done by object manipulator
     *
     * @param service
     */
//    public void removeService(PsService service) {
//        removeService(service.getId());
//    }
    /**
     * checks if the host has a service of type type associated with it
     *
     * @param type
     * @return
     */
//    public boolean hasServiceType(PsServiceType type) {
//        Iterator<PsService> iter = serviceIterator();
//        while (iter.hasNext()) {
//            PsService currentService = (PsService) iter.next();
//            String currentTypeId = currentService.getType();
//            if (type != null) {
//                if (currentTypeId != null) {
//                    if (currentTypeId.equals(type.getServiceTypeId())) {
//                        return true;
//                    }
//                } else {
//                    Logger logger = Logger.getLogger(PsHost.class);
//                    logger.warn("service on host " + this.getId() + " has type null");
//                }
//            }
//        }
//        return false;
//    }
    /**
     * check if host contains primitive service of serviceTypeId
     *
     * @param serviceTypeId
     * @return
     */
//    public boolean hasServiceType(String serviceTypeId) {
//        Iterator<PsService> iter = serviceIterator();
//        while (iter.hasNext()) {
//            PsService currentService = (PsService) iter.next();
//            String currentTypeId = currentService.getType();
//            if (PsServiceTypeFactory.isKnownType(serviceTypeId)) {
//                if (currentTypeId != null) {
//                    if (currentTypeId.equals(serviceTypeId)) {
//                        return true;
//                    }
//                } else {
//                    Logger logger = Logger.getLogger(PsHost.class);
//                    logger.warn("service on host " + this.getId() + " has type null");
//                }
//            }
//        }
//        return false;
//    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PsHost other = (PsHost) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PsHost{" + "id=" + id + ", hostname=" + hostname + ", ipv4=" + ipv4 + ", ipv6=" + ipv6 + '}';
    }
    
    public static Comparator<PsHost>idComparator = new Comparator<PsHost>() {
        public int compare(PsHost host1, PsHost host2) {
            int id1=host1.getId();
            int id2=host2.getId();
            if(id1==id2){
                return 0;
            }else{
                if(id1<id2){
                    return -1;
                }else{
                    return 1;
                }
            }
        }
    };
    
    public static Comparator<PsHost>hostIpv4Comparator = new Comparator<PsHost>() {
        public int compare(PsHost host1, PsHost host2) {
            return host1.getIpv4().compareTo(host2.getIpv4());
            
        }
    };
    
    public static Comparator<PsHost>hostIpv6Comparator = new Comparator<PsHost>() {
        public int compare(PsHost host1, PsHost host2) {
            return host1.getIpv6().compareTo(host2.getIpv6());
            
        }
    };
    
    public static Comparator<PsHost> hostnameComparator = new Comparator<PsHost>() {

        public int compare(PsHost host1, PsHost host2) {

            String hostname1 = host1.getHostname();
            String hostname2 = host2.getHostname();

            return compareHostNames(hostname1, hostname2);
        }
        /**
         * Compare hostnames in inverted order
         */
        private int compareHostNames(String hostname1, String hostname2) {
            String invertedhostname1=invert(hostname1);
            String invertedhostname2=invert(hostname2);
            return invertedhostname1.compareTo(invertedhostname2);
        }

        /**
         * private method to invert order of hostnames
         * www.google.com-->com.google.www
         * Such inversion is useful when sorting host names 
         */
        private String invert(String hostname) {
            String[] hostnameSplit = hostname.split(".");
            String result="";
            for(int i=hostnameSplit.length-1;i>-1;i=i-1){
                result=result+hostnameSplit[i]+".";
            }    
            //remove trailing "." character
            result=result+"@QWERT";
            result=result.replace(".@QWERT","");
            return result;
        }
        
    };

    
        
    
}
