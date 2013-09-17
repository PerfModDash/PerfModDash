/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard3.domainobjects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Object describing a perfSonar cloud.
 * @author tomw
 */
@Cacheable
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class PsCloud {
    
    //field names in JSON objects
    public static final String ID="id";
    public static final String NAME="name";
    public static final String STATUS="status";
    public static final String SITES="sites";
    public static final String MATRICES="matrices";
    
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int status;
    
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    //@ManyToMany(cascade = CascadeType.ALL)
    private List<PsSite> sites = new ArrayList<PsSite>();
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    //@ManyToMany(cascade = CascadeType.ALL)
    private List<PsMatrix> matrices = new ArrayList<PsMatrix>();
    
    /** 
     * create new cloud
     */
    public PsCloud(){
       
    }
    /**
     * generate new cloud with a given name
     * @param name 
     */
    public PsCloud(String name){
       
        this.name=name;
    }

    public int getId() {
        return id;
    }   

    public List<PsMatrix> getMatrices() {
        return matrices;
    }

    public void setMatrices(ArrayList<PsMatrix> matrices) {
        this.matrices = matrices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PsSite> getSites() {
        return sites;
    }

    public void setSites(ArrayList<PsSite> sites) {
        this.sites = sites;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    /**
     * get iterator over cloud sites
     * @return 
     */
    public Iterator<PsSite> sitesIterator(){
        return sites.iterator();
    }
    /**
     * iterator over cloud service matrices
     * @return 
     */
    public Iterator<PsMatrix> matrixIterator(){
        return matrices.iterator();
    }
    /** 
     * check if current cloud contains site with siteId
     * @param siteId
     * @return 
     */
    public boolean containsSiteId(int siteId){
        boolean result=false;
        Iterator<PsSite> iter = sitesIterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.getId()==siteId){
                result=true;
                return result;
            }
        }
        return result;
    }
    public boolean containsSite(PsSite site){
        return containsSiteId(site.getId());
    }
    /**
     * check if current cloud contains matrix given by matrixId
     * @param matrixId
     * @return 
     */
    public boolean containsMatrixId(int matrixId){
        boolean result=false;
        Iterator<PsMatrix> iter = matrixIterator();
        while(iter.hasNext()){
            PsMatrix currentMatrix = (PsMatrix)iter.next();
            if(currentMatrix.getId()==matrixId){
                result=true;
                return result;
            }
        }
        return result;
    }
    /**
     * check if cloud contains matrix
     * @param matrix
     * @return 
     */
    public boolean containsMatrix(PsMatrix matrix){
        return this.containsMatrixId(matrix.getId());
    }
    /**
     * add new site to cloud
     * @param site 
     */
    public void addSite(PsSite site) {
        if (!sites.contains(site)) {
            sites.add(site);
        }
    }
      
    /**
     * remove site from cloud
     * @param site 
     */
    public boolean removeSite(PsSite site){
        boolean result = sites.remove(site);
        return result;
    }
    /**
     * remove site identified by siteId from the cloud
     * @param siteId
     * @return 
     */
    public boolean removeSite(int siteId){        
        Iterator<PsSite> iter = sitesIterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.getId()==siteId){
                sites.remove(currentSite);
                return true;
            }
        }
        return false;
    }
    /**
     * add matrix to a cloud
     * @param matrix 
     */
    public void addMatrix(PsMatrix matrix){
        if(!matrices.contains(matrix)){
            matrices.add(matrix);   
        }
    }
   
    /**
     * remove matrix from cloud
     * @param matrix
     * @return 
     */
    public boolean removeMatrix(PsMatrix matrix){        
        boolean result = matrices.remove(matrix);
        return result;
    }
    
    public boolean removeMatrix(int matrixId){
        Iterator<PsMatrix> iter = matrixIterator();
        while(iter.hasNext()){
            PsMatrix currentMatrix = (PsMatrix)iter.next();
            if(currentMatrix.getId()==matrixId){
                removeMatrix(currentMatrix);
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
        final PsCloud other = (PsCloud) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public void removeAllSites() {
        sites.clear();
    }

    public void removeAllMatrices() {
        matrices.clear();
    }   
    
}
