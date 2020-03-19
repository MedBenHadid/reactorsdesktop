package utils.Utils.Connector;

import java.sql.*;


public class ConnectionUtil {

    public Connection conn;
    private Statement statement;

    private static ConnectionUtil db;

    private ConnectionUtil() {
        String dbDriver = "jdbc:mysql:";
        String dbHost = "//localhost";
        String dbName = "/reactorsdb";
        String dbUser = "root";
        String dbPort = ":3306";
        String dbPassword = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            this.conn =(Connection) DriverManager.getConnection(dbDriver+dbHost+dbPort+dbName, dbUser , dbPassword);
        }
        catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Connection Failed! Check output console");
            System.err.println("ConnectionUtil Exception : "+ex.getMessage()+ ex.getCause());
        }
    }
    /**
     *
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
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
        statement = db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public int insert(String insertQuery) throws SQLException {
        statement = db.conn.createStatement();
        return statement.executeUpdate(insertQuery);
    }
}
