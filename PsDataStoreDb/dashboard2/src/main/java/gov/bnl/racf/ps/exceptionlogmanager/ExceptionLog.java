/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.exceptionlogmanager;

import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;

/**
 *
 * @author tomw
 */
public interface ExceptionLog {
    public  void log(String className, Exception e);
    public  void log(Date time, String className, Exception e);
    public  void log(Date time, String className, String message);
    public  void log(ExceptionRecord exceptionRecord);
    public List<ExceptionRecord> get();
    public List<ExceptionRecord> get(Date time);
    public void delete();
    public void delete(Date time);
    public JSONArray toJson();
    public JSONArray toJson(Date time);
}
