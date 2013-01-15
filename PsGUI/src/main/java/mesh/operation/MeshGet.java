/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh.operation;

import cloud.bean.Cloud;
import host.bean.Host;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import matrix.bean.Matrix;
import mesh.bean.Mesh;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import site.bean.Site;

/**
 * The class to get a mesh information from a file URL.
 * @author siliu
 */
public class MeshGet {
    
    /**
     * The method to execute the parse of a mesh configuration file by providing the URL of the file.
     * @param meshFileURL The URL of the mesh configuration file.
     * @return a mesh object
     */
    public static Mesh executeMeshGet(String meshFileURL){
        
        System.out.println(meshFileURL);
        
        Mesh newMesh = new Mesh();
        
        JSONParser parser = new JSONParser();
        
        try{
            URL url = new URL(meshFileURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
            Object obj = parser.parse(reader);
			
            JSONObject meshObj = (JSONObject)obj;
	
            //JSONArray administrators = (JSONArray)meshObj.get("administrators");
            JSONArray organizations = (JSONArray)meshObj.get("organizations");
            JSONArray tests = (JSONArray)meshObj.get("tests");
            String description = (String) meshObj.get("description");
            
            //one file for one cloud, use the first word of "description" fied as the cloud name first
            Cloud cloud = new Cloud();
            int index_space = description.indexOf(" ");
            String cloudName = description.substring(0,index_space);
            cloud.setCloudname(cloudName);
            
            //get the sites in one cloud
            JSONArray sites_in_cloud = new JSONArray();
            
            //get the matrices in one cloud
            JSONArray matrices_in_cloud = new JSONArray();
            
            Site[] sites_verbose = new Site[200];
            Host[] hosts_verbose = new Host[200];
            
            
            int host_num = 0;
            int site_num = 0;
            for(int i=0; i<organizations.size(); i++){
                
                JSONObject orgObj = (JSONObject) organizations.get(i);
                
                JSONArray sites_array = (JSONArray)orgObj.get("sites");
                String org_description = (String) orgObj.get("description");
                
                for(int j=0; j<sites_array.size(); j++){
                    
                    
                    JSONObject siteObj = (JSONObject)sites_array.get(j);
                    
                    JSONArray hosts_array = (JSONArray)siteObj.get("hosts");
                    String site_description = (String) siteObj.get("description");
                    
                    //use site description as the site name
                    sites_in_cloud.add(site_description);
                    
                    //get the hosts in one site
                    JSONArray hosts_in_site = new JSONArray();
                    System.out.println("org " + i + "/n");
                    System.out.println("site " + j + "/n");
                    System.out.println("hosts_array " + j + ": " + hosts_array + "/n");
                    
                    if(hosts_array != null){
                        
                        for(int m=0; m<hosts_array.size(); m++){
                        
                            JSONObject hostObj = (JSONObject)hosts_array.get(m);
                        
                            JSONArray addresses = (JSONArray)hostObj.get("addresses");
                        
                        
                            for(int n=0; n<addresses.size(); n++){
                            
                                String hostname = (String) addresses.get(n);
                                Host one_host = new Host();
                                one_host.setHostname(hostname);
                                hosts_verbose[host_num] = one_host;
                                //hosts_verbose[host_num].setHostname(hostname);
                                hosts_in_site.add(hostname);
                                host_num++;
                        
                            }
                    
                        }
                        
                    }
                    
                    
                    //sites_verbose[site_num].setSitename(site_description); //maybe change to org_description later
                    //sites_verbose[site_num].setHosts(hosts_in_site);
                    
                    Site one_site = new Site();
                    one_site.setSitename(site_description);
                    one_site.setHosts(hosts_in_site);
                    sites_verbose[site_num] = one_site;
                    
                    site_num++;
                    
                }//sites_array loop end
                
            }// organizations loop end
            
            cloud.setSites(sites_in_cloud);
            
            //get the sites and hosts
            Site[] sites = new Site[site_num];
            sites = ArrayUtils.subarray(sites_verbose, 0, site_num);
            Host[] hosts = new Host[host_num]; 
            hosts = ArrayUtils.subarray(hosts_verbose,0,host_num);
            
            
            //deal with matrices: latency, throughput, traceroute
            Matrix[] matrices = new Matrix[tests.size()];
            
            for(int p=0; p<tests.size(); p++){
                
                JSONObject testObj = (JSONObject) tests.get(p);
                JSONObject members = (JSONObject) testObj.get("members");
                JSONArray hosts_in_matrix = (JSONArray) members.get("members");
                String matrix_description = (String) testObj.get("description"); //use matrix description as the matrix name
                
                //get the first word of matrix description, then get the serviceTypeId
                // 1. TCP -> throughput
                // 2. OWAMP -> latency
                // 3. Traceroute -> traceroute
                int indexOfSpace = matrix_description.indexOf(" ");
                String firstWord = matrix_description.substring(0,indexOfSpace);
                String serviceTypeId = "";
                if(firstWord.equals("TCP")){
                    serviceTypeId = "throughput";
                }else if(firstWord.equals("OWAMP")){
                    serviceTypeId = "latency";
                }else if(firstWord.equals("Traceroute")){
                    serviceTypeId = "traceroute";
                }
                matrices_in_cloud.add(matrix_description);
                
                Matrix one_matrix = new Matrix();
                
                one_matrix.setMatrixName(matrix_description);
                one_matrix.setServiceTypeId(serviceTypeId);
                one_matrix.setColumns(hosts_in_matrix);
                one_matrix.setRows(hosts_in_matrix); 
                
                matrices[p] = one_matrix;
            }
            
	    cloud.setMatrices(matrices_in_cloud);
            
            newMesh.setCloud(cloud);
            newMesh.setHosts(hosts);
            newMesh.setSites(sites);
            newMesh.setMatrices(matrices);
            
            
        }catch(IOException e){
            newMesh = null;
            e.printStackTrace();
		
			
        }catch(ParseException e){
            newMesh = null;
            e.printStackTrace();
		
        }catch(ClassCastException e){
            newMesh = null;
            e.printStackTrace();
		
        }
        
        return newMesh;
    }
    
}
