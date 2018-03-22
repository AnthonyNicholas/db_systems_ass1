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

public class Derby_Implementation{

    private static final String DB_URL = "dbUrl";
    private static final String DB_USER = "dbUser";
    private static final String DB_PASSWORD = "dbPwd";
    private static final String DB_PROPERTIES = "db.properties";    
    private static final String DATA_FILE = "BUSINESS_NAMES_201803.csv";

    
    public void writeProperties() throws IOException{
        //Properties prop = new Properties();
        //prop.setProperty();	
    }

    public Connection connectToDB() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "an");
        connectionProps.put("password", "pwd");    
   
///opt/Apache/db-derby-10.14.1.0-bin/lib/derby.jar:/opt/Apache/db-derby-10.14.1.0-bin/lib/derbytools.jar
 
        //DriverManager.registerDriver(new org.apache.derby.jbdc.ClientDriver());
        Connection conn = DriverManager.getConnection(
            "jdbc:derby:test.db;create=true", connectionProps);
        
        System.out.println("Connected");

        return conn;
    }

    public void dropDatabaseTable(Connection conn) throws SQLException, IOException {
        Statement s = conn.createStatement();
        String doThisSql = "DROP TABLE busNames";
        s.executeUpdate(doThisSql);
        System.out.println("Dropped Table");             
    }

    public void createDatabaseTable(Connection conn) throws SQLException, IOException {
        Statement s = conn.createStatement();
        String doThisSql = "CREATE TABLE busNames";
        doThisSql += "(BN_NAME VARCHAR(256), BN_STATUS VARCHAR(256), ";
        doThisSql += "BN_REG_DT VARCHAR(256), BN_CANCEL_DT VARCHAR(256), BN_RENEW_DT VARCHAR(256), ";
        doThisSql += "BN_STATE_NUM VARCHAR(256), BN_STATE_OF_REG VARCHAR(256), BN_ABN VARCHAR(256))";
        s.executeUpdate(doThisSql);
        System.out.println("Created Table");             
    }

    public void addData(Connection conn) throws SQLException, IOException {
		
        BufferedReader bReader = new BufferedReader(new FileReader(DATA_FILE));
        
        String line;
        
        //Ignore first line which contains headings
        bReader.readLine();

        while((line = bReader.readLine()) != null){
            System.out.println(line);
            String values[] = line.split("\t");
            System.out.println(values.toString());           
 
            String doThisSql = "INSERT INTO busNames VALUES (?,?,?,?,?,?,?,?)";
		
            try (PreparedStatement ps = conn.prepareStatement(doThisSql)) {
                
                //Ignore first value (which is table name) 
		        ps.setString(1, values[1]);
		        ps.setString(2, values[2]);
		        ps.setString(3, values[3]);
		        ps.setString(4, values[4]);
		        ps.setString(5, values[5]);
		        ps.setString(6, values[6]);
		        ps.setString(7, values[7]);
		        ps.setString(8, values[8]);
                ps.executeUpdate();
		    }
        }
	}

    public void readData(Connection conn) throws SQLException {
		try (Statement query = conn.createStatement()) {
			String sql = "SELECT * FROM testdata";
			query.setFetchSize(100);
			
			try (ResultSet rs = query.executeQuery(sql)) {
				while (!rs.isClosed() && rs.next()) {
					int num = rs.getInt(1);
					Timestamp dt = rs.getTimestamp(2);
					String txt = rs.getString(3);
					System.out.println(num + "; " + dt + "; " + txt);
				}
			}
			
		}
	}




    public static void main(String[] args){
        System.out.println("Hello World");
        Derby_Implementation d_imp = new Derby_Implementation();
        
        try{
            Connection conn = d_imp.connectToDB();
            d_imp.dropDatabaseTable(conn);
            d_imp.createDatabaseTable(conn);
            d_imp.addData(conn);
            d_imp.readData(conn);

        } 
        catch(Exception e){
            System.out.println(e.toString());
        }
        
    }
}

