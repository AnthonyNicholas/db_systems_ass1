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

public class Derby_Implementation{

    private static final String DB_URL = "dbUrl";
    private static final String DB_USER = "dbUser";
    private static final String DB_PASSWORD = "dbPwd";
    private static final String DB_PROPERTIES = "db.properties";    
    private static final String DATA_FILE = "BUSINESS_NAMES_short_version.csv";

/* 
* Function establishes connection with database
*/

    public Connection connectToDB() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "an");
        connectionProps.put("password", "pwd");    
   
        //DriverManager.registerDriver(new org.apache.derby.jbdc.ClientDriver());
        Connection conn = DriverManager.getConnection(
            "jdbc:derby:test.db;create=true", connectionProps);
        
        System.out.println("Connected");

        return conn;
    }


/* 
* Function drops database table
*/

    public void dropDatabaseTable(Connection conn) throws SQLException, IOException {
        Statement s = conn.createStatement();
        String doThisSql = "DROP TABLE busNames";
        s.executeUpdate(doThisSql);
        System.out.println("Dropped Table");             
    }


/* 
* Function creates database table
*/

    public void createDatabaseTable(Connection conn) throws SQLException, IOException {
        Statement s = conn.createStatement();
        String doThisSql = "CREATE TABLE busNames";
        doThisSql += "(BN_NAME VARCHAR(256), BN_STATUS VARCHAR(256), ";
        doThisSql += "BN_REG_DT DATE, BN_CANCEL_DT DATE, BN_RENEW_DT DATE, ";
        doThisSql += "BN_STATE_NUM VARCHAR(256), BN_STATE_OF_REG VARCHAR(256), BN_ABN VARCHAR(256))";
        s.executeUpdate(doThisSql);
        System.out.println("Created Table");             
    }


/* 
* Function inserts data into database, timing insert operations and printing result to consoles
*/

    public void addData(Connection conn) throws SQLException, IOException, ParseException {

        BufferedReader bReader = new BufferedReader(new FileReader(DATA_FILE));
        String line;
        SimpleDateFormat parser = new SimpleDateFormat("dd/mm/yyyy");
        java.sql.Date dates[] = new java.sql.Date[3];
        java.util.Date utilDate; 

        //Ignore first line which contains headings
        bReader.readLine();
        
        long startTime = System.currentTimeMillis();

        while((line = bReader.readLine()) != null){
            
            String values[] = line.split("\t");
            
            if (values.length != 9){ //If line does not contain full set of values, ignore
                    continue;
            }
            
            String doThisSql = "INSERT INTO busNames VALUES (?,?,?,?,?,?,?,?)";
		
            try (PreparedStatement ps = conn.prepareStatement(doThisSql)) {
                
                // First prepare the date values for insertion - they need to be in java.sql.Date format
                for (int i = 0; i < 3; i++){
                    try{
                        utilDate = parser.parse(values[i+4]);
                    } catch(ParseException e){
                        utilDate = parser.parse("01/01/1900"); //If cannot parse date, insert dummy value   
                    }
                    dates[i] = new java.sql.Date(utilDate.getTime());
                }
             
                System.out.print("."); 
                

                // Ignore first value (table name) and insert all other values
                ps.setString(1, values[1]);
                ps.setString(2, values[2]);
                ps.setDate(3, dates[0]); 
                ps.setDate(4, dates[1]);
                ps.setDate(5, dates[2]);
                ps.setString(6, values[6]);
                ps.setString(7, values[7]);
                ps.setString(8, values[8]);
                ps.executeUpdate();
		    }
        }

        bReader.close();

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);	
        System.out.println("Time to insert: " + duration + " milliseconds"); 
	}

/* 
* Function prints business names for first 5 rows of data 
*/

    public void readData(Connection conn) throws SQLException {
		try (Statement query = conn.createStatement()) {
			String sql = "SELECT * FROM busNames FETCH FIRST 5 ROWS ONLY";
			//query.setFetchSize(100);
			
			try (ResultSet rs = query.executeQuery(sql)) {
				while (!rs.isClosed() && rs.next()) {
	                
                    System.out.println(rs.getString(1));
            
                    }
			}
			
		}
	}


    public static void main(String[] args){
        System.out.println("Derby Implementation");
        Derby_Implementation d_imp = new Derby_Implementation();
        
        try{
            Connection conn = d_imp.connectToDB();
            d_imp.dropDatabaseTable(conn);
            d_imp.createDatabaseTable(conn);
            d_imp.addData(conn);
            d_imp.readData(conn);

        } 
        catch(Exception e){
            System.err.println(e.toString());//Needs to print to standard err
        }
        
    }
}

