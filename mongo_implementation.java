/*Task 2: MongoDB
 * You are required to load the data into MongoDB. In your report:
 * explain how have you chosen to structure the data inserted in MongoDB
 * provide details of the time taken to load the data (The mongoimport is one utility will provide such information). Please note that 
 * a naive import into a flat structure in Mongodb will not accrue you a great mark. You need to analyse the data and consider 
 * appropriate ways to structure the data and then using any scripting, programming or other tools to format the data accordingly.
 * Postgraduate students only: What alternative way or ways could you have organised the data when storing in MongoDB, 
 * and what advantages or disadvantages would these alternative designs have? 
*/

import java.io.*;
import java.util.*;
import com.mongodb.client.MongoDatabase
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCredential


public class Mongo_Implementation{

    private static final String DB_URL = "dbUrl";
    private static final String DB_USER = "dbUser";
    private static final String DB_PASSWORD = "dbPwd";
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DATA_FILE = "BUSINESS_NAMES_201803.csv";
    private MongoDatabase db;

    public Mongo_Implementation(){
        db = this.connectToDB();
    }

    public void writeProperties() throws IOException{
        //Properties prop = new Properties();
        //prop.setProperty();   
    }

    public MongoDatabase connectToDB() throws SQLException {
        
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase db = mongo.getDatabase("busNames");
        System.out.println("Connected to MongoDB");
        return db;
    }

    public void dropDatabaseTable(String collection) {
        this.db.drop(collection);
        System.out.println("Dropped Collection");
    }

    public void createDatabaseTable() {
        this.db.createCollections("busNames");
        System.out.println("Created Collection");
    }

    public void addData() throws IOException {


        BufferedReader bReader = new BufferedReader(new FileReader(DATA_FILE));
        MongoCollection<Document> busNamesColl = this.db.getCollection("busNames");        

        String line;

        //Ignore first line which contains headings

        long startTime = System.nanoTime();
        
        while((line = bReader.readLine()) != null){
            System.out.println(line);
            String values[] = line.split("\t");
            System.out.println(values.toString());
            
            Document doc = new Document("BN_NAME", values[1])
                .append("BN_STATUS", values[2])
                .append("BN_REG_DT", values[3])
                .append("BN_CANCEL_DT", values[4])
                .append("BN_RENEW_DT", values[5])
                .append("BN_STATE_NUM", values[6])
                .append("BN_STATE_OF_REG", values[7])
                .append("BN_ABN", values[8]);
           
            busNamesColl.insertOne(doc);  
       }

        long endTime = System.nanoTime();
        long duration = (endTime = startTime);
        System.out.println("Time to insert: " + duration);
    }

    public void readData() throws SQLException {
        try (Statement query = conn.createStatement()) {
            String sql = "SELECT * FROM busNames LIMIT 100";
            query.setFetchSize(100);

            try (ResultSet rs = query.executeQuery(sql)) {
                while (!rs.isClosed() && rs.next()) {

                    headings = "BN_NAME\tBN_STATUS\tBN_REG_DT\tBN_CANCEL_DT\tBN_RENEW_DT\tBN_STATE_NUM\tBN_STATE_OF_REG\tBN_ABN";
                    rowText = rs.getString(1) + "\t" + rs.getString(2) + "\t" + str(s.getDate(3))  + "\t" + str(rs.getDate(4))  + "\t"
                    rowText += str(rs.getDate(5))  + "\t" + rs.getString(6)  + "\t" + rs.getString(7)  + "\t" +  rs.getString(8);
                    System.out.println(headings);
                    System.out.println(rowText);
                }
            }

        }
    }


    public static void main(String[] args){
        System.out.println("Mongo Implementation");

        try{
            Mongo_Implementation m_imp = new Mongo_Implementation(); //Establishes connection to db
            m_imp.dropDatabaseTable();
            m_imp.createDatabaseTable();
            m_imp.addData();
            //m_imp.readData();

        }
        catch(Exception e){
            System.err.println(e.toString());//Needs to print to standard err
        }

    }
}

