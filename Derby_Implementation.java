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
    private static final String DATA_FILE = "BUSINESS_NAMES_201803.csv";

    
    public void writeProperties() throws IOException{
        //Properties prop = new Properties();
        //prop.setProperty();	
    }

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
        doThisSql += "BN_REG_DT DATE, BN_CANCEL_DT DATE, BN_RENEW_DT DATE, ";
        doThisSql += "BN_STATE_NUM VARCHAR(256), BN_STATE_OF_REG VARCHAR(256), BN_ABN VARCHAR(256))";
        s.executeUpdate(doThisSql);
        System.out.println("Created Table");             
    }

    public void addData(Connection conn) throws SQLException, IOException, ParseException {
	

        BufferedReader bReader = new BufferedReader(new FileReader(DATA_FILE));
        String line;
        
        //Ignore first line which contains headings
        bReader.readLine();
 
        long startTime = System.nanoTime();

        while((line = bReader.readLine()) != null){
            //System.out.println(line);
            String values[] = line.split("\t");
            
            if (values.length != 9){
                    continue;
            }

            SimpleDateFormat parser = new SimpleDateFormat("dd/MMM/yyyy");

            String doThisSql = "INSERT INTO busNames VALUES (?,?,?,?,?,?,?,?)";
		
            try (PreparedStatement ps = conn.prepareStatement(doThisSql)) {
                

                String input = "Thu Jun 18 20:56:02 EDT 2009";
                java.util.Date utilDate1 = parser.parse(values[4]);
                java.sql.Date sqlDate1 = new java.sql.Date(utilDate1.getTime());
                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String formattedDate = formatter.format(date); 
                
                /*Date [] dates = new Date[3];
                                
                for (int i = 0; i < 3; i++){
                    int year = Integer.parseInt(values[i+4].split("/")[2]);
                    int month =  Integer.parseInt(values[i+4].split("/")[1]);
                    int day =  Integer.parseInt(values[i+4].split("/")[0]);
     
                    dates[i] = new Date(year, month, day);
                    System.out.println(dates[i]);
                }
                */
             
                //java.sql.Date sqlDate = new java.sql.Date(util.Date.getTime());
               

               //java.sql.Date sqlDate1 = new java.sql.Date(System.currentTimeMillis());
 
               //java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

                System.out.println("inserting values"); 
                
                //Ignore first value (which is table name) 
                ps.setString(1, values[1]);
                ps.setString(2, values[2]);
                ps.setDate(3, sqlDate1); 
                ps.setDate(4, sqlDate1);
                ps.setDate(5, sqlDate1);
                ps.setString(6, values[6]);
                ps.setString(7, values[7]);
                ps.setString(8, values[8]);
                ps.executeUpdate();
		    }
        }

        long endTime = System.nanoTime();
        long duration = (endTime = startTime);	
        System.out.println("Time to insert: " + duration); 
	}

    public void readData(Connection conn) throws SQLException {
		try (Statement query = conn.createStatement()) {
			String sql = "SELECT * FROM busNames LIMIT 100";
			query.setFetchSize(100);
			
			try (ResultSet rs = query.executeQuery(sql)) {
				while (!rs.isClosed() && rs.next()) {
	                
                    System.out.println(rs.getString(1));
                    /*
                    String name, status, reg_dt, cancel_dt, renew_dt, state_num, state_of_reg, abn 
                    name = status = reg_dt = cancel_dt = renew_dt = state_num = state_of_reg = abn = null 
                    
                    name = 
                    status = 
                    reg_dt =
                    cancel_dt = 
                    renew_dt = 
                    state_num = 
                    state_of_reg = 
                    abn = null 
	
                    String headings = "BN_NAME\tBN_STATUS\tBN_REG_DT\tBN_CANCEL_DT\tBN_RENEW_DT\tBN_STATE_NUM\tBN_STATE_OF_REG\tBN_ABN";
                    String rowText = rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getDate(3)  + "\t" + rs.getDate(4)  + "\t";
                    rowText += rs.getDate(5)  + "\t" + rs.getString(6)  + "\t" + rs.getString(7)  + "\t" +  rs.getString(8);
                    System.out.println(headings);
                    System.out.println(rowText);
			        */
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

