/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;
import java.util.Vector;
import java.util.Iterator;

/**
 * Object describing a perfSonar cloud.
 * @author tomw
 */
public class PsCloud {
    
    //field names in JSON objects
    public static final String ID="id";
    public static final String NAME="name";
    public static final String STATUS="status";
    public static final String SITES="sites";
    public static final String MATRICES="matrices";
    
    private String id;
    private String name;
    private int status;
    private Vector<PsSite> sites = new Vector<PsSite>();
    private Vector<PsMatrix> matrices = new Vector<PsMatrix>();
    
    /** 
     * create new cloud
     */
    public PsCloud(){
        id=PsIdGenerator.generateId();
    }
    /**
     * generate new cloud with a given name
     * @param name 
     */
    public PsCloud(String name){
        id=PsIdGenerator.generateId();
        this.name=name;
    }

    public String getId() {
        return id;
    }   

    public Vector<PsMatrix> getMatrices() {
        return matrices;
    }

    public void setMatrices(Vector<PsMatrix> matrices) {
        this.matrices = matrices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<PsSite> getSites() {
        return sites;
    }

    public void setSites(Vector<PsSite> sites) {
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
    public boolean containsSiteId(String siteId){
        boolean result=false;
        Iterator<PsSite> iter = sitesIterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.getId().equals(siteId)){
                result=true;
                return result;
            }
        }
        return result;
    }
    /**
     * check if current cloud contains matrix given by matrixId
     * @param matrixId
     * @return 
     */
    public boolean containsMatrixId(String matrixId){
        boolean result=false;
        Iterator<PsMatrix> iter = matrixIterator();
        while(iter.hasNext()){
            PsMatrix currentMatrix = (PsMatrix)iter.next();
            if(currentMatrix.getId().equals(matrixId)){
                result=true;
                return result;
            }
        }
        return result;
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
    public boolean removeSite(String siteId){        
        Iterator<PsSite> iter = sitesIterator();
        while(iter.hasNext()){
            PsSite currentSite = (PsSite)iter.next();
            if(currentSite.getId().equals(siteId)){
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
     * add to the cloud a matrix defined by matrix id
     * @param matrixId 
     */
    public void addMatrix(String matrixId){
        //TODO add matrix defined by matrixId
        System.out.println("ERROR: PsCloud.addMatrix(String matrixId) not implemented yet!!!!");
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
    
    public boolean removeMatrix(String matrixId){
        Iterator<PsMatrix> iter = matrixIterator();
        while(iter.hasNext()){
            PsMatrix currentMatrix = (PsMatrix)iter.next();
            if(currentMatrix.getId().equals(matrixId)){
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
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

   
    
}
