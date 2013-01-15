/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primitiveServ.operation;

import config.DataStoreConfig;
import config.PsApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

//import org.json.JSONObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import primitiveServ.bean.Parameters;
import primitiveServ.bean.PrimitiveServ;
import primitiveServ.bean.Result;
import primitiveServ.bean.ResultParameters;

/**
 * The class to query a primitive service from the data store.
 * @author siliu
 */
public class PrimitiveServQuery {
    
    /**
     * The method to execute the query of a primitive service by providing the primitive service id.
     * @param serviceid The primitive service one would like to query.
     * @return The queried PrimitiveServ object.
     */
    public static PrimitiveServ executePrimitiveServQuery(String serviceid){
        
        PrimitiveServ serv = new PrimitiveServ();
        Result result = new Result();
        ResultParameters resultPara = new ResultParameters();
        Parameters para = new Parameters();
        
        
        
        serv.setId(serviceid);
        
        JSONParser parser = new JSONParser();
	DataStoreConfig cfg = new DataStoreConfig();
	String SERVICE = PsApi.SERVICE;
	String serviceURL = cfg.getProperty("storeURL") + SERVICE + '/' + serviceid;
        
        try{
            URL url = new URL(serviceURL.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
            Object obj = parser.parse(reader);
            JSONObject serviceObj = (JSONObject)obj;
            
            //parse the whole primitive services JSONObject
            if(!serviceObj.isEmpty()){
                
                JSONObject resultObj = (JSONObject) serviceObj.get("result");
                String description = (String) serviceObj.get("description");
                String name = (String) serviceObj.get("name");
                String runningSince = (String) serviceObj.get("runningSince");
                String nextCheckTime = (String) serviceObj.get("nextCheckTime");
                long checkInterval = (Long)serviceObj.get("checkInterval");
                JSONObject parametersObj = (JSONObject) serviceObj.get("parameters");
                String prevCheckTime = (String) serviceObj.get("prevCheckTime");
                String type = (String) serviceObj.get("type");
                boolean running = (Boolean)serviceObj.get("running");
                long timeout = (Long) serviceObj.get("timeout");
                
                if(!resultObj.isEmpty()){
                    
                    //parse result JSONObject
                    String message = (String) resultObj.get("message");
                    String id = (String) resultObj.get("id");
                    String job_id = (String) resultObj.get("job-id");
                    String time = (String) resultObj.get("time");
                    long status = (Long)resultObj.get("status");
                    String service_id = (String) resultObj.get("service_id");
                    JSONObject resultParametersObj = (JSONObject) resultObj.get("parameters");
                    
                    if(!resultParametersObj.isEmpty()){
                        
                        //parse result parameters JSONObject
            
                        String paratime = (String) resultParametersObj.get("time");
                        String command = (String) resultParametersObj.get("command");
                        
                        //set attributes to the Primitive Services object
                        //1. resultPara object
                        resultPara.setCommand(command);
                        resultPara.setTime(paratime);
                    
                    }else{
                        resultPara = null;
                        
                    }
                    
                    //2. result object
                    result.setId(id);
                    result.setJob_id(job_id);
                    result.setMessage(message);
                    result.setResultParameters(resultPara);
                    result.setService_id(service_id);
                    result.setStatus(status);
                    result.setTime(time);
            
                
                
                
                }else{
                    
                    result = null;
                
                
                }
            
                
                if(!parametersObj.isEmpty()){
                    //parse primitive services parameters JSONObject
            
                    long port = 0; //set to 0 tempeialy.
                    String paraHost = (String) parametersObj.get("paraHost");
                    String hostid = (String) parametersObj.get("host-id");
                    
                    /*
                    
                    if(parametersObj.get("port")!= null){
                        port = (Long) parametersObj.get("port");
                    }else{
                        port = (Long) parametersObj.get("template");
                    
                    }
                    
                    */
                    
                    //3.para object
                     para.setPort(port);
                     para.setHost(paraHost);
                     para.setHostid(hostid);
                
                
                }else{
                
                    para = null;
                }
            
                
            //4. serv object
            serv.setResult(result);
            serv.setDescription(description);
            serv.setName(name);
            serv.setRunningSince(runningSince);
            serv.setNextCheckTime(nextCheckTime);
            serv.setCheckInterval(checkInterval);
            serv.setParameters(para);
            serv.setPrevCheckTime(prevCheckTime);
            serv.setType(type);
            serv.setRunning(running);
            serv.setTimeout(timeout);
            
            
            
            
            }else{
            
                serv = null;
            }
            
            
              
            
	}catch(IOException e){
            
            serv = null;
            e.printStackTrace();
            		
	}catch(ParseException e){
            
            serv = null;
            e.printStackTrace();
           
            
	}catch(ClassCastException e){
            
            serv = null;
            e.printStackTrace();		
	}
    
    
        return serv;
    }
    
}
