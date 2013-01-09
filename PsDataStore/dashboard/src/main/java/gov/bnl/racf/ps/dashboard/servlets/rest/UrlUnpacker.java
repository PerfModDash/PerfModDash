/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.servlets.rest;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

/**
 * Unpack the url and return an Input parameter object
 *
 * @author tomw
 */
public class UrlUnpacker {

    public static Vector<String> unpack(String inputUrl) {
        Vector<String> parameters = new Vector<String>();

        if (inputUrl != null) {

            File file = new File(inputUrl);
            String parentPath = file.getParent();
            while (parentPath != null) {
                String fileName = file.getName();
                parameters.add(fileName);
                file = file.getParentFile();
                parentPath = file.getParent();
            }

            Collections.reverse(parameters);
        }

        return parameters;
    }
}
