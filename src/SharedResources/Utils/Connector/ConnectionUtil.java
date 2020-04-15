package SharedResources.Utils.Connector;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectionUtil {

    public Connection conn;
    private Statement statement;

    private static ConnectionUtil db;

    private ConnectionUtil() {
        String dbDriver = "jdbc:mysql:";
        String dbHost = "//localhost";
        String dbName = "/reactorsdb";
        // to get version 5.1.33 of MySQL JDBC driver to work with UTC time zone
        String supportForUTC = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String dbUser = "root";
        String dbPort = ":3306";
        String dbPassword = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            this.conn = DriverManager.getConnection(dbDriver + dbHost + dbPort + dbName + supportForUTC, dbUser, dbPassword);
        }
        catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Connection Failed! Check output console");
            System.err.println("ConnectionUtil Exception : "+ex.getMessage()+ ex.getCause());
            Logger.getLogger(
                    ConnectionUtil.class.getName()).log(
                    Level.INFO, null, ex
            );
        }
    }

    /**
     * @return MysqlConnect Database connection object
     */
    public static synchronized ConnectionUtil conDB() {
        if ( db == null ) {
            db = new ConnectionUtil();
        }
        return db;

    }
    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException when you WABALABADUBDUB BITCH
     */
    public ResultSet query(String query) throws SQLException{
        statement = db.conn.createStatement();
        return statement.executeQuery(query);
    }
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException when you WABALABADUBDUB BITCH
     */
    public int insert(String insertQuery) throws SQLException {
        statement = db.conn.createStatement();
        return statement.executeUpdate(insertQuery);
    }
}
