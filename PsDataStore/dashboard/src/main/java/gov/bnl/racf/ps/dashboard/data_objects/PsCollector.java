/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;

/**
 * Describes a perfSonar collector 
 * @author tomw
 */
public class PsCollector {
    
    //filed names in JSON objects
    public static final String ID="id";
    public static final String NAME="name";
    public static final String IPV4="ipv4";
    public static final String IPV6="ipv6";
    public static final String HOSTNAME="hostname";
    
    private String id;
    private String name;
    private String ipv4;
    private String ipv6;
    private String hostname;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
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
     * Generate new, empty collector.
     * Set the collector id
     */
    public PsCollector(){
        id=PsIdGenerator.generateId();
    }
   
   

    public String getId() {
        return id;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PsCollector other = (PsCollector) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
        
}
