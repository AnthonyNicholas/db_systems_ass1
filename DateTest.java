/*Task 1: Derby
 * You are required to load the data into Derby. In your report:
 * explain how have you chosen to structure the Derby relational database and give reasons.
 * provide details of the time to load the data into Derby. You need to analyse the data and consider appropriate ways to 
 * structure the data and then using any scripting, programming or other tools to format the data accordingly.
 * Postgraduate students only: What alternative way or ways could you have organised the data when storing in Derby, and what advantages 
 * or disadvantages would these al- ternative designs have? 
*/

import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.lang.Integer.*;

public class DateTest{

    public static void main(String[] args){
       
        String dateString = "12/10/2017";
        java.util.Date utilDate; 
        java.sql.Date sqlDate;
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
 
        try{
            utilDate = parser.parse(dateString);
            sqlDate = new java.sql.Date(utilDate.getTime());
            System.out.println(sqlDate.toString());
        } catch(ParseException e){
            System.out.println("Couldn't parse date");
        }
        
    }
}

