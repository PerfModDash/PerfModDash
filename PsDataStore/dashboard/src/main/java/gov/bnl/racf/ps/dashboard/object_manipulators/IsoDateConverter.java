/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.object_manipulators;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
/**
 * utility class for converting between Date and ISO8601 date format
 * and the other way around too
 * @author tomw
 */
public class IsoDateConverter {
    public static Date isoDate2Date(String isoDateString){
        DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
        DateTime dateTime = formatter.parseDateTime(isoDateString);
        Date date = dateTime.toDate();
        return date;
    }
    public static String date2IsoDate(Date date){
        return dateToString(date);
    }
    public static String dateToString(Date d) {
        return ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC).print(new DateTime(d));
    }
}
