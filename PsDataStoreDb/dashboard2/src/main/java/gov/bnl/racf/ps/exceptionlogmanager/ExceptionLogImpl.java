/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.exceptionlogmanager;

import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tomw
 */
public class ExceptionLogImpl implements ExceptionLog {

    private String fileName = "exceptionLog.log";

    public ExceptionLogImpl(String baseDirectory) {

        fileName = baseDirectory + fileName;
//        System.out.println(fileName);
//        System.out.flush();
    }

    @Override
    public void log(String className, Exception e) {
        log(new Date(), className, e);
    }

    @Override
    public List<ExceptionRecord> get() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ExceptionRecord> get(Date time) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete() {
        writeToFile(" ", false);
    }

    @Override
    public void delete(Date time) {
        JSONArray currentList = toJson();

        String result = "";
        Iterator iter = currentList.iterator();
        while (iter.hasNext()) {
            JSONObject json = (JSONObject) iter.next();
            Date jsonTime = (Date) json.get("time");
            if (time.before(jsonTime)) {
                result = result + "," + json.toString() + "\n";
            }
        }
        writeToFile(result, false);
    }

    @Override
    public JSONArray toJson() {
        JSONArray jsonArray = new JSONArray();

        String everything = logFileContent();
        everything = everything.trim();
        if (everything.startsWith(",")) {
            String marker = "QWERTYUIOP";
            everything = marker + everything;
            everything = everything.replace(marker + ",", "");
        }

        everything = "[" + everything + "]";

        JSONParser parser = new JSONParser();
        try {
            jsonArray = (JSONArray) parser.parse(everything);
        } catch (ParseException ex) {
            Logger.getLogger(ExceptionLogImpl.class.getName()).log(Level.SEVERE, null, ex);
        }


        return jsonArray;
    }

    private String logFileContent() {
//        String workingDir = System.getProperty("user.dir");
//        System.out.println("Current work dir = " + workingDir);
//        System.out.flush();


        String everything = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (Exception ex) {
            Logger.getLogger(ExceptionLogImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
                Logger.getLogger(ExceptionLogImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return everything;
    }

    @Override
    public JSONArray toJson(Date time) {
        JSONArray listOfAllRecords = toJson();
        JSONArray resultList = new JSONArray();

        Iterator iter = listOfAllRecords.iterator();
        while (iter.hasNext()) {
            JSONObject json = (JSONObject) iter.next();
            Long jsonTimeLong = (Long) json.get("time");
            Date jsonTime = new Date();
            jsonTime.setTime(jsonTimeLong.longValue());
            if (jsonTime.after(time)) {
                resultList.add(json);
            }
        }
        return resultList;
    }

    @Override
    public void log(ExceptionRecord er) {
        log(er.getTime(), er.getClassName(), er.getMessage());
    }

    @Override
    public void log(Date time, String className, Exception e) {
        log(time, className, e.toString());
    }

    @Override
    public void log(Date time, String className, String message) {
        JSONObject json = new JSONObject();
        json.put("time", time.getTime());
        json.put("className", className);
        json.put("message", message);
        this.writeToFile(json.toString(), true);

    }

    private void writeToFile(String data, boolean append) {


        try {
            File file = new File(this.fileName);

            //if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            //true = append file
            //FileWriter fileWritter = new FileWriter(file.getName(), append);
            FileWriter fileWritter = new FileWriter(file, append);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(data + "\n");
            
            bufferWritter.flush();
            bufferWritter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
