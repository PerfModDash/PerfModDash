/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.db.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Extract data of a POST request, convert them to JSON or JSONArray
 *
 * @author tomw
 */
public class PostRequestDataExtractor {

    public static JSONObject extractJson(HttpServletRequest request) throws IOException {
        String jb = "";
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb = jb + line;
            }
        } catch (Exception e) {
            System.out.println(new Date() + " " + e);
        }

        // parse input data into JSON object
        JSONObject jsonObject = null;
        try {
            JSONParser parser = new JSONParser();

            if (jb != "") {
                jsonObject = (JSONObject) parser.parse(jb);
            } else {
                jsonObject = new JSONObject();
                jsonObject.put("hostname", "AAAAA New Host");
            }

        } catch (Exception e) {
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        }
        return jsonObject;
    }

    public static JSONArray extractJsonArray(HttpServletRequest request) throws IOException {
        String jb = "";
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb = jb + line;
            }
        } catch (Exception e) {
            System.out.println(new Date() + " " + e);
        }

        // parse input data into JSON array
        JSONArray jsonArray = null;
        try {
            JSONParser parser = new JSONParser();

            jsonArray = (JSONArray) parser.parse(jb);

        } catch (Exception e) {
            // crash and burn
            throw new IOException("Error parsing JSONArray request string");
        }
        return jsonArray;
    }
}
