/*Task 3: MongoDB
 * You are required to load the data into MongoDB. In your report:
 * explain how have you chosen to structure the data inserted in MongoDB
 * provide details of the time taken to load the data (The mongoimport is one utility will provide such information). Please note that 
 * a naive import into a flat structure in Mongodb will not accrue you a great mark. You need to analyse the data and consider 
 * appropriate ways to structure the data and then using any scripting, programming or other tools to format the data accordingly.
 * Postgraduate students only: What alternative way or ways could you have organised the data when storing in MongoDB, 
 * and what advantages or disadvantages would these alternative designs have?
 *
 * Structuring the data - You need to look for any repeated information that is consistent.
 *
 *
 *
 *
*/

import java.io.*;
import java.util.*;

public class Mongo_Implementation{

    private static final String DB_URL = "dbUrl";
    private static final String DB_USER = "dbUser";
    private static final String DB_PASSWORD = "dbPwd";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DATA_FILE = "Bus_Names_proc_forMongoInsert.csv";
    //private static final String DATA_FILE = "BUSINESS_NAMES_201803.csv";


    public void dropCollection() {

        String clean_command = "mongo < mongo_clean_all.js";       
        System.out.println(clean_command);
        String result = this.executeCommand(clean_command);    

        System.out.println("Dropped Collection");
    }

    /*
    * This function first removes " marks from BUSINESS_NAMES_201803.csv (as this interferes with the import process).  It then 
    * imports tsv file into mongo db ASIC into busNames collection.
    *
    */

    public void addData() throws IOException, InterruptedException {

        this.executeCommand("./prepare_data.sh");
        Thread.sleep(3000);
        String import_command = "mongoimport --db ASIC --collection busNames --type tsv --headerline --file ./" + DATA_FILE;
        String output = this.executeCommand(import_command);    
  
    }
        
    /*
    *This function runs a js script that creates addition collections in Mongo ("Registered" and "Deregistered") which holds _ids of 
    * records corresponding to each BN_STATUS.  It then removes BN_STATUS from busNames collection.
    *
    */
    
    public void improveCollectionStructure() {
        
        //Runtime.getRuntime().exec("mongo < mongo_create_collection.js");
        
        String reorg_command = "mongo < mongo_reorganise_data.js";
        
        System.out.println(reorg_command);
        String result = this.executeCommand(reorg_command);    
        
        System.out.println("Reorganised Data");
        
    }
       
    /*
    * Function which will run given bash command, print and return output
    *
    * Acknowledgement:  https://www.mkyong.com/java/how-to-execute-shell-command-from-java/
    */
    
    private String executeCommand(String command) {
       
        System.out.println("Executing Bash command: " + command);
 
        StringBuffer output = new StringBuffer();
        
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        
        String line = "";           
        while ((line = reader.readLine())!= null) {
            output.append(line + "\n");
        }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("BASH command finished");
        System.out.println(output.toString());
        return output.toString();
        
    }
        
    /*
    * Main method - Triggers import into mongo db
    *
    */
    
    public static void main(String[] args){
        
        System.out.println("Mongo Implementation");
        Mongo_Implementation m_imp = new Mongo_Implementation(); //Establishes connection to db
        
        try{

            long startTime = System.currentTimeMillis(); 
            
            m_imp.addData();
           m_imp.improveCollectionStructure(); 

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            System.out.println("Time to insert: " + duration);
        
        } catch (Exception e){
            System.err.println(e.toString());//Needs to print to standard err
        }
    }

} // End Mongo_Implementation class

