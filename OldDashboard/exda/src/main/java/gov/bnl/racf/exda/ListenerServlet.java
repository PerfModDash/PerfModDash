package gov.bnl.racf.exda;

import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.Class;
import java.util.Date;

import org.apache.log4j.*;

public class ListenerServlet implements ServletContextListener {
    private java.util.Timer timer = null;

    public void contextInitialized(ServletContextEvent e) {
	Date date = new Date();
	System.out.println(date+" "+getClass().getName()+" ListenerServlet initializes timer");

	timer = new java.util.Timer(true);
	// the interval between tasks should 10 minutes
	long checkInterval = 10 * 60 * 1000;

	long firstDelay=0;
	timer.schedule(new AlarmCheck(),firstDelay, checkInterval);
	System.out.println(date+" "+getClass().getName()+" ListenerServlet timer initialized!!!");

	long probeCheckDelay = 60 * 60 * 1000;
	long probeCheckInterval = 2 * 60 * 60 * 1000;
	timer.schedule(new ProbeCheck(), probeCheckDelay, probeCheckInterval);

	System.out.println(date+" "+getClass().getName()+" ListenerServlet timer for ProbeCheck initialized!!!");


	// define log4j
	ServletContext ctx = e.getServletContext();
    
	String prefix =  ctx.getRealPath("/");    
	String file = "WEB-INF"+System.getProperty("file.separator")+"classes"+System.getProperty("file.separator")+"log4j.properties";
           
	if(file != null) {
	    PropertyConfigurator.configure(prefix+file);
	    System.out.println("Log4J Logging started for application: " + prefix+file);
	}else{
	    System.out.println("Log4J Is not configured for application Application: " + prefix+file);
	} 


	Logger log = Logger.getLogger("exdalogger");
	log.debug("Some string to print out");
 


    }

    public void contextDestroyed(ServletContextEvent e) {
	Date date = new Date();
	System.out.println(date+" "+getClass().getName()+" ListenerServlet close timer");
	timer.cancel();
	System.out.println(date+" "+getClass().getName()+" ListenerServlet timer cancelled!!!");
    }
}
