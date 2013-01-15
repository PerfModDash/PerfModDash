package mesh.bean;


import cloud.bean.Cloud;
import host.bean.Host;
import matrix.bean.Matrix;
import site.bean.Site;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class includes four basic objects in a mesh: Host, Site, Matrix, Cloud
 * Standard behaviors like the get and set methods for Mesh object properties are defined here.
 * @author siliu
 */
public class Mesh {
    
    private Host[] hosts;
    private Site[] sites;
    private Matrix[] matrices;
    private Cloud cloud;
    
    public Host[] getHosts(){	
	return hosts;
    }
    
    public void setHosts(Host[] hosts){
        this.hosts = hosts;
    }
    
    public Site[] getSites(){	
	return sites;
    }
    
    public void setSites(Site[] sites){
        this.sites = sites;
    }
    
    public Cloud getCloud(){	
	return cloud;
    }
    
    public void setCloud(Cloud cloud){
        this.cloud = cloud;
    }
    
    public Matrix[] getMatrices(){	
	return matrices;
    }
    
    public void setMatrices(Matrix[] matrices){
        this.matrices = matrices;
    }
    
}
