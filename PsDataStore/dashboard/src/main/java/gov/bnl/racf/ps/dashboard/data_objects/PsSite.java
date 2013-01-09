/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;

import java.util.Vector;
import java.util.Iterator;

/**
 * object describing a perfSonar site
 * @author tomw
 */
public class PsSite {
    
    // field names in JSON
    
    public static final String ID="id";
    public static final String NAME="name";
    public static final String DESCRIPTION="description";
    public static final String STATUS="status";
    public static final String HOSTS="hosts";
    
    private String id;
    private String name;
    private String description;
    private int status;
    private Vector<PsHost> hosts=new Vector<PsHost>();;

    /**
     * get and set methods
     * @return 
     */
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get hosts belonging to this site
     * @return 
     */
    public Vector<PsHost> getHosts() {
        return hosts;
    }
    /**
     * set vector of hosts belonging to this site
     * @param hosts 
     */
    public void setHosts(Vector<PsHost> hosts) {
        this.hosts = hosts;
    }

    public String getId() {
        return id;
    }
    /**
     * get site name
     * @return 
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /**
     * get site status
     * @return 
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
    public Iterator<PsHost> hostsIterator(){
        return hosts.iterator();
    }
    
    /**
     * get vector of host id's belonging to this site
     * @return 
     */
    public Vector<String> getHostIds(){
        Vector<String> listOfIds=new Vector<String>();
        Iterator<PsHost> iter = hostsIterator();
        while(iter.hasNext()){
            PsHost currentHost = (PsHost)iter.next();
            String currentHostId = currentHost.getId();
            listOfIds.add(currentHostId);
        }
        return listOfIds;
    }
    
    /**
     * generate empty site, id is generated internally
     */
    public PsSite(){
        this.id=PsIdGenerator.generateId();
    }
    /**
     * general constructor, takes name and description as parameters
     * @param name
     * @param description 
     */
    public PsSite(String name, String description){
        this.id=PsIdGenerator.generateId();
        this.name=name;
        this.description=description;
    }
    /**
     * add new host to a site
     * @param host 
     */
    public void addHost(PsHost host){
        hosts.add(host);
    }
    /**
     * remove host from the site
     * @param host 
     */
    public void removeHost(PsHost host){
        String hostId = host.getId();
        removeHostId(hostId);
    }
    /**
     * remove host identified by hostId
     * @param hostId 
     */
    public void removeHostId(String hostId){
        Iterator<PsHost> iter = hostsIterator();
        while(iter.hasNext()){
            PsHost currentHost = (PsHost)iter.next();
            if(currentHost.getId().equals(hostId)){
                iter.remove();
            }
        }
    }
    /**
     * Remove all hosts from site
     * To be used when deleting entire sites
     */
    public void removeAllHosts(){
        this.hosts=null;
    }
    /**
     * check if this site contains given host
     * return true if yes, false otherwise
     * @param host
     * @return 
     */
    public boolean containsHost(PsHost host){
        String hostId=host.getId();        
        boolean result = containsHostId(hostId);
        return result;
    }
    /**
     * check if site contains host with hostName
     * @param hostName
     * @return 
     */
    public boolean containsHostName(String hostName){
        Iterator<PsHost> iter = hostsIterator();
         while(iter.hasNext()){
            PsHost currentHost = (PsHost)iter.next();
            if(currentHost.getHostname().equals(hostName)){
                return true;
            }
         }
         return false;
    }
    /**
     * check if this site contains host with hostId
     * return true if yes, false otherwise
     * @param hostId
     * @return 
     */
    public boolean containsHostId(String hostId){
        Iterator<PsHost> iter = hostsIterator();
        while(iter.hasNext()){
            PsHost currentHost = (PsHost)iter.next();
            if(currentHost.getId().equals(hostId)){
                return true;
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
        final PsSite other = (PsSite) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
}
