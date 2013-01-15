package config;

//import java.io.File;
//import java.io.FileInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStream;
import java.util.Properties;

/**
 * The class to get the URL of the data store.
 * @author siliu
 */
public class DataStoreConfig {
	
                /*
		Properties confFile;
		public DataStoreConfig()
		{
			confFile = new java.util.Properties();
			
			try{
				//String separatorChar = File.separator;
		
				
				//Properties prop = new Properties(); 
				confFile.load(this.getClass().getClassLoader().getResourceAsStream("/config/data_store.properties"));
				
				//System.out.println(cf.getAbsolutePath());
				//System.out.println(cf.exists());
				//InputStream is = new FileInputStream(cf);
				//confFile.load(this.getClass().getClassLoader().getResourceAsStream("/Documents/workspace/JSONweb/src/config/config.cfg"));
				//confFile.load(is);
			}catch(IOException eta){
				eta.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
                */
		
		public String getProperty(String key){
			//String value = this.confFile.getProperty(key);
			//return value;
                        String storeURL = "http://perfsonar.racf.bnl.gov:8080/dashboard-1.0-SNAPSHOT/";
                        return storeURL;
		}
                
                /*
                public String createFile() throws IOException{
                    File file = new File("testFile.txt");
                    String path = "";
                    if(file.createNewFile()){
                        System.out.print(file.getAbsolutePath());
                        path = file.getAbsolutePath();
                        
                    }else{
                        System.out.print("Create file failed!");
                        path = "failed";
                        
                    }
                    
                    return path;
                }
                */

}
