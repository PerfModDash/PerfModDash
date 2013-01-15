/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh.operation;

import cloud.bean.Cloud;
import cloud.operation.CloudARSite;
import cloud.operation.CloudCreate;
import cloud.operation.CloudQuery;
import config.DataStoreConfig;
import config.PsApi;
import host.bean.Host;
import host.operation.HostCreate;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import matrix.bean.Matrix;
import matrix.operation.MatrixARHost;
import matrix.operation.MatrixCreate;
import matrix.operation.MatrixQuery;
import mesh.bean.Mesh;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import site.bean.Site;
import site.operation.SiteARHost;
import site.operation.SiteCreate;
import site.operation.SiteQuery;

/**
 * The class to create a mesh in the data store by creating Cloud, Host, Site and Matrix.
 * One mesh is one cloud.
 * One mesh has list of hosts, sites and at least one matrix.
 * @author siliu
 */
public class MeshConfig {
    
    /**
     * The method to execute the creation of mesh in the data store by providing a mesh object.
     * @param one_mesh The mesh to create.
     * @return a mesh object with all created attributes.
     */
    public static Mesh executeMeshConfig(Mesh one_mesh){
        
        Mesh newMesh = new Mesh();
        
        //get the raw information from the mesh
        Host[] hosts = one_mesh.getHosts();
        Site[] sites = one_mesh.getSites();
        Matrix[] matrices = one_mesh.getMatrices();
        Cloud cloud = one_mesh.getCloud();
        
        // create the four objects
        Host[] createdHosts = new Host[hosts.length];
        Site[] createdSites = new Site[sites.length];
        Matrix[] createdMatrices = new Matrix[matrices.length];
        Cloud createdCloud = new Cloud();
        
        // connect the four objects
        // then query it to get the new objects
        Host[] newHosts = new Host[hosts.length];
        Site[] newSites = new Site[sites.length];
        
        Cloud newCloud = new Cloud();
        
        
        //create JSONObject of each objects need to be created
        /*
        JSONObject[] hosts_obj = new JSONObject[hosts.length];
        JSONObject[] sites_obj = new JSONObject[sites.length];
        JSONObject[] matrices_obj = new JSONObject[matrices.length];
        JSONObject cloud_obj = new JSONObject();
        */
        
        try{
            //1. create all hosts in this mesh
            
            
            
            JSONArray hosts_name_list = new JSONArray();
            JSONArray hosts_id_list = new JSONArray();
            
            for(int i=0; i<hosts.length; i++){
                JSONObject hostObj =  new JSONObject();
                String hostname = hosts[i].getHostname();
                hostObj.put("hostname", hostname); 
                
                Host h = HostCreate.executeHostCreate(hostObj);
                
                if( h != null){
                    
                    hosts_name_list.add(h.getHostname());
                    hosts_id_list.add(h.getHostid()); 
                    createdHosts[i] = h;
                    newHosts[i] = h;
                }else{
                
                    System.out.println("Create host failed in the mesh!");
                }
                
            }
        
            //2. create all sites in this mesh
            
            
            for(int j=0; j<sites.length; j++){
                JSONObject siteObj =  new JSONObject();
                String sitename = sites[j].getSitename();
                siteObj.put("name", sitename); 
                
                Site s = SiteCreate.executeSiteCreate(siteObj);
                
                if(s != null){
                    
                    createdSites[j] = s;
                    
                }else{
                    
                    System.out.println("Create site failed in the mesh!");
                
                }
            }
            
            //3. create all matrices in this mesh
            
            int createdMatrix_num = 0;
            for(int m=0; m<matrices.length; m++){
                             
                String matrixName = matrices[m].getMatrixName();
                String serviceTypeId = matrices[m].getServiceTypeId();
                
                //can only add and remove hosts to throughput and latency matrix
                //so we only create these two and exclude others.
                if(serviceTypeId.equals("throughput") || serviceTypeId.equals("latency")){
                    
                    JSONObject matrixObj = new JSONObject();
                
                    matrixObj.put("name", matrixName);
                    matrixObj.put("serviceTypeId", serviceTypeId);
                
                    Matrix ma = MatrixCreate.executeMatrixCreate(matrixObj);
                
                    if(ma != null){
                        createdMatrices[createdMatrix_num] = ma;
                        createdMatrix_num++;
                
                    }else{
                        System.out.println("Create matrix failed in the mesh!");
                    }
                
                }
                
            }
            //
            Matrix[] newMatrices = new Matrix[createdMatrix_num];
            
            //4. create cloud for this mesh
            JSONObject cloudObj = new JSONObject();
            String cloudname = cloud.getCloudname();
            cloudObj.put("name", cloudname);
            Cloud c = CloudCreate.executeCloudCreate(cloudObj);
            
            if(c != null ){
                
                createdCloud = c;
            
            }else{
                
                System.out.println("Create cloud failed in the mesh!");
                
            }
            
            boolean flag_SiteARHost = false;
            boolean flag_MatrixARHost = false;
            boolean flag_CloudARSite = false;
            
            if(createdCloud != null){
                
                //get the cloudid
                String cloudid = createdCloud.getCloudid();
                
                if(createdHosts!=null){
                    
                    // add hosts to site
                    //get the sites id list at the same time to add to the cloud
                    
                    if( newSites!=null){
                        
                        JSONArray sites_in_cloud_id = new JSONArray();
                        
                        for(int p=0; p<createdSites.length; p++){
                            
                            //parameters need to add hosts to site
                            //(1) need siteid
                            String siteid = createdSites[p].getSiteid();
                            System.out.print("site id in meshConfig: " + siteid + "/n");
                            
                            sites_in_cloud_id.add(siteid);
                            //(2)need button_flag_site=2 ,the same as button "add selected hosts"
                            int button_flag_site = 2;
                            
                            //(3) need host id to add to site
                            JSONArray hosts_in_site_id = new JSONArray();
                            
                            JSONArray hosts_in_site = sites[p].getHosts();
                            
                            for(int q=0; q<hosts_in_site.size(); q++){
                                String hs = (String) hosts_in_site.get(q);
                                int hs_index = hosts_name_list.indexOf(hs);
                                hosts_in_site_id.add(hosts_id_list.get(hs_index));    
                            }
                            
                            flag_SiteARHost = SiteARHost.executeSiteARHost(siteid, button_flag_site, hosts_in_site_id);
                            
                            if(flag_SiteARHost == true){
                                newSites[p] = SiteQuery.executeSiteQuery(siteid);
                     
                            }
    
                        }
                        
                        //add sites to cloud
                        int button_flag_cloud = 2;
                        flag_CloudARSite = CloudARSite.executeCloudARSite(cloudid, button_flag_cloud, sites_in_cloud_id);
                        
                        if(flag_CloudARSite == true){
                            
                            newCloud = CloudQuery.executeCloudQuery(cloudid);
                        
                        }
                        
                    
                    }else{
                        System.out.println("Create new sites failed!");
                        newMesh = null;
                    }
                    
                    //add hosts to matrix
                    if(createdMatrices != null){
                        
                        for(int k=0; k<createdMatrix_num; k++){
                            
                                
                                String matrixId = createdMatrices[k].getMatrixId();
                            
                            
                                //columes and rows are the same at the beginning
                            
                                JSONArray hosts_in_matrix = matrices[k].getColumns();
                                //System.out.println("hosts_in_matrix:" + hosts_in_matrix);
                                JSONArray hosts_in_matrix_id = new JSONArray();
                            
                                for(int q=0; q<hosts_in_matrix.size(); q++){
                                
                                    String hm = (String) hosts_in_matrix.get(q);
                                    int hm_index = hosts_name_list.indexOf(hm);
                                    String host_id = (String) hosts_id_list.get(hm_index);
                                    hosts_in_matrix_id.add(host_id);    
                                }
                            
                            
                                int button_flag_matrix = 6;
                                System.out.println("matrixId:" + matrixId);
                                System.out.println("button_flag_matrix:" + button_flag_matrix);
                                System.out.println("hosts_in_matrix_id: "+ hosts_in_matrix_id);
                            
                                flag_MatrixARHost = MatrixARHost.executeMatrixARHost(matrixId, button_flag_matrix, hosts_in_matrix_id);
                                //query the new created matrices
                                if(flag_MatrixARHost == true){
                            
                                    newMatrices[k] = MatrixQuery.executeMatrixQuery(matrixId);
                                
                            
                                }
    
                         
                        }
                    
                    }else{
                        
                        System.out.println("Create new matrices failed!");
                        newMesh = null; 
                    }
                   
                }else{
                    
                    System.out.println("Create new hosts failed!");
                    newMesh = null;
                }
                
            }else{
                System.out.println("Create new cloud failed!");
                newMesh = null;
                
            }
            
            if(flag_SiteARHost == true && flag_MatrixARHost==true && flag_CloudARSite==true){
                
                newMesh.setCloud(newCloud);
                newMesh.setHosts(newHosts);
                newMesh.setMatrices(newMatrices);
                newMesh.setSites(newSites);
            
            }else{
                System.out.println("Create mesh failed!");
                newMesh = null;
            
            }
        
        }catch(Exception e){
            
            e.printStackTrace();
            newMesh = null;
         
        }
        
        return newMesh;
        
    
    }
    
}
