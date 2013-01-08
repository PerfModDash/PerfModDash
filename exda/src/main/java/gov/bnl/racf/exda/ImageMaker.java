package gov.bnl.racf.exda;


import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import java.io.File;

public class ImageMaker extends HttpServlet {

    private String moduleName="ImageMaker";


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
	String pictureDirectory = getServletContext().getRealPath("/");
	String pictureFile=pictureDirectory+"temp_picture.jpg";
	OutputStream out = response.getOutputStream();
	
	// Get the absolute path of the image
	ServletContext sc = getServletContext();
	String filename = pictureFile;
	
	// Get the MIME type of the image
	String mimeType = sc.getMimeType(filename);
	if (mimeType == null) {
	    sc.log("Could not get MIME type of "+filename);
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    return;
	}
	
	// Set content type
	response.setContentType(mimeType);
	
	// Set content size
	File file = new File(filename);
	response.setContentLength((int)file.length());
	
	// Open the file and output streams
	FileInputStream in = new FileInputStream(file);
	//OutputStream out = resp.getOutputStream();
	
	// Copy the contents of the file to the output stream
	byte[] buf = new byte[1024];
	int count = 0;
	while ((count = in.read(buf)) >= 0) {
	    out.write(buf, 0, count);
	}
	in.close();
	out.close();
	
    }
}



