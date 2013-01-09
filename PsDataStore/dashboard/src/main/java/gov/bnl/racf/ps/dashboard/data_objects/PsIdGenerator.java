/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.bnl.racf.ps.dashboard.data_objects;
import java.util.Date;
import java.lang.Long;
import java.util.Random;
/**
 * a class for generating id of perfsonar objects
 * there are other tools which can generate unique id for objects
 * we can replace them, if needed.
 * In hibernate we will use autogenerate key tool provided by hibernate.
 * @author tomw
 */
public class PsIdGenerator {
    
    public static String xgenerateId(){
        Date currentDate = new Date();
        long timeSinceBeginningOfTheWorld = currentDate.getTime();
        String timeSinceBeginningOfTheWorldAsString = 
                (new Long(timeSinceBeginningOfTheWorld)).toString();
        
        //generate random number between 0 and 100k
        int maxNumber=1000000;
        Random r=new Random();
        int randomInt = r.nextInt(maxNumber);
        String randomIntAsString = (new Integer(randomInt)).toString();
        
        return timeSinceBeginningOfTheWorldAsString+"."+randomIntAsString;
    }
    /**
     * return string representing id
     * @return 
     */
    public static String generateId(){
        String id = UniqueIdStore.getUniqueIdStore().getId();
        return id;
    }
    
}
