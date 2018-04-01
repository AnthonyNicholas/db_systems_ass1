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

public class Derby_Implementation{

    private static final String DB_URL = "dbUrl";
    private static final String DB_USER = "dbUser";
    private static final String DB_PASSWORD = "dbPwd";
    private static final String DB_PROPERTIES = "db.properties";    

    private static final String DATA_FILE = "BUSINESS_NAMES_201803.csv";
//    private static final String DATA_FILE = "BUSINESS_NAMES_short_version.csv";

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

        String [] tableNames = {"busNames", "states", "registered"};

        Statement s = conn.createStatement();

        for (String name:tableNames){
            s.executeUpdate("DROP TABLE " + name);
 
        }

        System.out.println("Dropped Tables");             

   }


/* 
* Function creates four database tables we will need - busNames, states and registered tables 
* Populates codes for STATE_CODE and REG_CODE
*/

    public void createDatabaseTable(Connection conn) throws SQLException, IOException {
        Statement s = conn.createStatement();
        String doThisSql = "CREATE TABLE busNames";
        
        doThisSql += "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ";
        doThisSql += "BN_NAME VARCHAR(256), BN_STATUS VARCHAR(15), ";
        doThisSql += "BN_REG_DT DATE, BN_CANCEL_DT DATE, BN_RENEW_DT DATE, ";
        doThisSql += "BN_STATE_NUM VARCHAR(15), BN_STATE_OF_REG VARCHAR(15), BN_ABN VARCHAR(15), ";
        doThisSql += "PRIMARY KEY(ID))";
        s.executeUpdate(doThisSql);

        Statement s3 = conn.createStatement();
        String doThisSql3 = "CREATE TABLE states";
        doThisSql3 += "(STATE_CODE INT, STATE_NAME VARCHAR(4), "; 
        doThisSql3 += "PRIMARY KEY(STATE_CODE))";
        s3.executeUpdate(doThisSql3);
        
        Statement s6 = conn.createStatement();
        String doThisSql6 = "INSERT INTO states VALUES";
        doThisSql6 += "(1, 'VIC'), (2, 'NSW'), (3, 'ACT'), (4, 'QLD'), (5, 'NT'), (6, 'SA'), (7, 'WA'), (8, 'TAS'), (9, 'NULL')";
        s6.executeUpdate(doThisSql6);
 
        Statement s4 = conn.createStatement();
        String doThisSql4 = "CREATE TABLE registered";
        doThisSql4 += "(REG_CODE INT, REG_TEXT VARCHAR(15), "; 
        doThisSql4 += "PRIMARY KEY(REG_CODE))";
        s4.executeUpdate(doThisSql4);

        Statement s5 = conn.createStatement();
        String doThisSql5 = "INSERT INTO registered VALUES";
        doThisSql5 += "(1, 'Deregistered'), (2, 'Registered')";
        s5.executeUpdate(doThisSql5);
        
        System.out.println("Created Tables");             
    }



/* 
* Function inserts data into database, timing insert operations and printing result to consoles
*/

    public void improveStructure(Connection conn) throws SQLException, IOException, ParseException {

        Statement s = conn.createStatement();
        String doThisSql = "ALTER TABLE busNames ADD STATE_CODE INT";
        s.executeUpdate(doThisSql);

        Statement s2 = conn.createStatement();
        String doThisSql2 = "ALTER TABLE busNames ADD REG_CODE INT";
        s2.executeUpdate(doThisSql2);
        System.out.println("added additional columns");

        Statement s3 = conn.createStatement();
        String doThisSql3 = "UPDATE busNames ";
        doThisSql3 += "SET REG_CODE = 1 "; 
        doThisSql3 += "WHERE BN_STATUS LIKE 'Deregistered'";
        s3.executeUpdate(doThisSql3);

        Statement s4 = conn.createStatement();
        String doThisSql4 = "UPDATE busNames ";
        doThisSql4 += "SET REG_CODE = 2 "; 
        doThisSql4 += "WHERE BN_STATUS LIKE 'Registered'";
        s3.executeUpdate(doThisSql4);

        System.out.println("Updated Reg Code");

        String [] doThisSql5 = new String [9];

        String [] states = {"VIC", "NSW", "ACT", "QLD", "NT", "SA", "WA", "TAS", ""};
        
        for (int i = 0; i < 9; i++){

            doThisSql5[i] = "UPDATE busNames ";
            doThisSql5[i] += "SET STATE_CODE = " + i + " "; 
            doThisSql5[i] += "WHERE BN_STATE_OF_REG LIKE '%" + states[i] + "%'";
            conn.createStatement().executeUpdate(doThisSql5[i]);
        }
 
        System.out.println("Updated State Code");

        Statement s6 = conn.createStatement();
        String doThisSql6 = "ALTER TABLE busNames ";
        doThisSql6 += "DROP BN_STATUS"; 
        s6.executeUpdate(doThisSql6);


        Statement s7 = conn.createStatement();
        String doThisSql7 = "ALTER TABLE busNames ";
        doThisSql7 += "DROP BN_STATE_OF_REG"; 
        s7.executeUpdate(doThisSql7);

    }


/* 
* Function inserts data into database, timing insert operations and printing result to consoles
*/

    public void addData(Connection conn) throws SQLException, IOException, ParseException {

        BufferedReader bReader = new BufferedReader(new FileReader(DATA_FILE));
        String line;
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dates[] = new java.sql.Date[3];
        java.util.Date utilDate; 

        //Ignore first line which contains headings
        bReader.readLine();
        

        while((line = bReader.readLine()) != null){
            
            String values[] = line.split("\t");
            
            if (values.length != 9){ //If line does not contain full set of values, ignore
                    continue;
            }
            
            String doThisSql = "INSERT INTO busNames (BN_NAME, BN_STATUS, BN_REG_DT, BN_CANCEL_DT, BN_RENEW_DT, BN_STATE_NUM, BN_STATE_OF_REG, BN_ABN) ";
            doThisSql += "VALUES (?,?,?,?,?,?,?,?)";

	
            try (PreparedStatement ps = conn.prepareStatement(doThisSql)) {
                
                // First prepare the date values for insertion - they need to be in java.sql.Date format
                for (int i = 0; i < 3; i++){
                    try{
                        utilDate = parser.parse(values[i+3]);
                        
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

}

/* 
* Function prints business names for first 10 rows of data 
*/

    public void readData(Connection conn) throws SQLException {
		try (Statement query = conn.createStatement()) {
			String sql = "SELECT * FROM busNames FETCH FIRST 10 ROWS ONLY";
			//query.setFetchSize(100);
			
			try (ResultSet rs = query.executeQuery(sql)) {
				while (!rs.isClosed() && rs.next()) {
	               
                    int colNum = rs.getMetaData().getColumnCount();
                    
                    for (int i = 1; i <= colNum; i++){
 
                        System.out.print(rs.getString(i)+ "\t");
                    }
                    System.out.println();        
                }
			}
			
		}
	}

/* 
* Main - Establishes connection, drops any existing tables in db, calls functions to create db structure, add data (timing this step), improve structure of db and then print results (first 10 rows only).
*/

    public static void main(String[] args){
        System.out.println("Derby Implementation");
        Derby_Implementation d_imp = new Derby_Implementation();
        
        try{
            Connection conn = d_imp.connectToDB();

            d_imp.dropDatabaseTable(conn);
            d_imp.createDatabaseTable(conn);
            
            long startTime = System.currentTimeMillis();
            
            d_imp.addData(conn);

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);	
            
            System.out.println("Time to insert: " + duration + " milliseconds"); 

            d_imp.improveStructure(conn);
	
            d_imp.readData(conn);
            

        } 
        catch(Exception e){
            System.err.println(e.toString());//Needs to print to standard err
        }
        
    }
}

