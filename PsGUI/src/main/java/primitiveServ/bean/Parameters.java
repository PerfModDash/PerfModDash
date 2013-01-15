/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitiveServ.bean;

/**
 * This class includes all attributes of parameters field in a primitive service.
 * Standard behaviors like the get and set methods for Parameters object properties are defined here.
 * @author siliu
 */
public class Parameters {
    
    private long port;
    private String host;
    private String hostid;
    
    public long getPort(){	
	return port;
    }
    
    public void setPort(long port){
        this.port = port;
    }
    
    public String getHost(){	
	return host;
    }
    
    public void setHost(String host){
        this.host = host;
    }
    
    
    public String getHostid(){
	return hostid;
    }
	
    public void setHostid(String hostid){
        this.hostid = hostid;
    }
    
}
